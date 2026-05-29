package com.unessesaryguide.artisticexpression.block;

import com.unessesaryguide.artisticexpression.particle.ModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class GiantCandleBlock extends Block {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public GiantCandleBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        if (!neighborPos.equals(pos.above())) return;
        if (!state.getValue(LIT)) return;

        BlockState above = level.getBlockState(pos.above());
        boolean isSolid = !above.isAir() && !above.canBeReplaced() && above.isSolidRender(level, pos.above());
        boolean isCandle = above.getBlock() instanceof GiantCandleBlock;

        if (isSolid || isCandle) {
            level.setBlock(pos, state.setValue(LIT, false), 3);
            level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    private BlockPos getTopCandle(Level level, BlockPos pos) {
        BlockPos current = pos;
        while (level.getBlockState(current.above()).getBlock() instanceof GiantCandleBlock) {
            current = current.above();
        }
        return current;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;
        if (level.getBlockState(pos.above()).getBlock() instanceof GiantCandleBlock) return;

        double baseX = pos.getX() + 0.5;
        double baseY = pos.getY() + 1.4;
        double baseZ = pos.getZ() + 0.5;

        int count = 3 + random.nextInt(2);
        for (int i = 0; i < count; i++) {
            double x = baseX + (random.nextDouble() - 0.5) * 0.1;
            double z = baseZ + (random.nextDouble() - 0.5) * 0.1;

            level.addParticle(ModParticleTypes.LARGE_FLAME.get(), x, baseY, z, 0, 0, 0);
        }

        if (random.nextInt(2) == 0) {
            level.addParticle(ParticleTypes.SMOKE,
                baseX + (random.nextDouble() - 0.5) * 0.1,
                baseY + 0.1,
                baseZ + (random.nextDouble() - 0.5) * 0.1,
                0, 0.01, 0);
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                            Player player, BlockHitResult hit) {
        BlockPos topPos = getTopCandle(level, pos);

        if (!topPos.equals(pos)) {
            BlockState topState = level.getBlockState(topPos);
            return topState.useWithoutItem(level, player, hit.withPosition(topPos));
        }

        if (state.getValue(LIT)) {
            level.setBlock(pos, state.setValue(LIT, false), 3);
            level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public ItemInteractionResult useItemOn(net.minecraft.world.item.ItemStack stack, BlockState state,
                                           Level level, BlockPos pos, Player player,
                                           InteractionHand hand, BlockHitResult hit) {
        BlockPos topPos = getTopCandle(level, pos);

        if (!topPos.equals(pos)) {
            BlockState topState = level.getBlockState(topPos);
            return topState.useItemOn(stack, level, player, hand, hit.withPosition(topPos));
        }

        if (!state.getValue(LIT) && stack.is(Items.FLINT_AND_STEEL)) {
            level.setBlock(pos, state.setValue(LIT, true), 3);
            level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
            stack.hurtAndBreak(1, player, player.getUsedItemHand() == InteractionHand.MAIN_HAND
                ? net.minecraft.world.entity.EquipmentSlot.MAINHAND
                : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
