package com.unessesaryguide.artisticexpression.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public class TackedPaperBlock extends BaseEntityBlock {

    public TackedPaperBlock() {
        super(BlockBehaviour.Properties.of()
            .strength(0.1f)
            .sound(SoundType.WOOL)
            .noCollission()
            .noOcclusion()
            .instabreak()
        );
    }

    // -------------------------------------------------------------------------
    // Block Entity
    // -------------------------------------------------------------------------


    public static final MapCodec<TackedPaperBlock> CODEC =
        MapCodec.unit(TackedPaperBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TackedPaperBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    // -------------------------------------------------------------------------
    // Shape — union of thin slabs for each occupied face
    // -------------------------------------------------------------------------

    private static final VoxelShape SHAPE_DOWN  = Block.box(1, 15, 1, 15, 16, 15);
    private static final VoxelShape SHAPE_UP    = Block.box(1,  0, 1, 15,  1, 15);
    private static final VoxelShape SHAPE_NORTH = Block.box(1,  1, 15, 15, 15, 16);
    private static final VoxelShape SHAPE_SOUTH = Block.box(1,  1,  0, 15, 15,  1);
    private static final VoxelShape SHAPE_WEST  = Block.box(15, 1,  1, 16, 15, 15);
    private static final VoxelShape SHAPE_EAST  = Block.box(0,  1,  1,  1, 15, 15);

    private static VoxelShape shapeForFace(Direction face) {
        return switch (face) {
            case DOWN   -> SHAPE_DOWN;
            case UP     -> SHAPE_UP;
            case NORTH  -> SHAPE_NORTH;
            case SOUTH  -> SHAPE_SOUTH;
            case WEST   -> SHAPE_WEST;
            case EAST   -> SHAPE_EAST;
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof TackedPaperBlockEntity tpbe)) return Shapes.empty();

        VoxelShape combined = Shapes.empty();
        for (Direction face : tpbe.getOccupiedFaces()) {
            combined = Shapes.or(combined, shapeForFace(face));
        }
        return combined;
    }

    // -------------------------------------------------------------------------
    // Breaking — remove only the face the player hit, drop 1 paper
    // -------------------------------------------------------------------------

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.isClientSide) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof TackedPaperBlockEntity tpbe)) return;

        // Determine which face the player is looking at via ray cast
        BlockHitResult hit = (BlockHitResult) player.pick(5.0, 1.0f, false);
        if (!hit.getBlockPos().equals(pos)) return;

        Direction face = hit.getDirection();
        if (!tpbe.hasFace(face)) return;

        tpbe.removeFace(face);
        level.sendBlockUpdated(pos, state, state, 3);

        // Drop vanilla paper at the block center
        if (!player.isCreative()) {
            popResource(level, pos, new ItemStack(Items.PAPER));
        }

        // Remove the block entirely if no faces remain
        if (tpbe.isEmpty()) {
            level.removeBlock(pos, false);
        }
    }

    // -------------------------------------------------------------------------
    // Neighbor changes — pop off faces whose support block was removed
    // -------------------------------------------------------------------------

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof TackedPaperBlockEntity tpbe)) return state;

        // direction is FROM this block TO the changed neighbor.
        // If we have paper on that face, check if its support is still solid.
        if (tpbe.hasFace(direction)) {
            BlockPos supportPos = pos.relative(direction);
            BlockState support = level.getBlockState(supportPos);
            boolean sturdy = support.isFaceSturdy(level, supportPos, direction.getOpposite());

            if (!sturdy) {
                tpbe.removeFace(direction);
                if (!level.isClientSide() && level instanceof Level lvl) {
                    popResource(lvl, pos, new ItemStack(Items.PAPER));
                    lvl.sendBlockUpdated(pos, state, state, 3);
                }
            }
        }

        if (tpbe.isEmpty()) {
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }

        return state;
    }
}
