package com.unessesaryguide.artisticexpression.block;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = ArtisticExpression.MODID)
public class PaperPlacementHandler {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        ItemStack held = player.getItemInHand(event.getHand());

        if (!held.is(Items.PAPER)) return;

        Direction face = event.getHitVec().getDirection();
        BlockPos clickedPos = event.getPos();
        BlockPos placePos = clickedPos.relative(face);

        BlockState existing = level.getBlockState(placePos);

        if (existing.getBlock() instanceof TackedPaperBlock) {
            BlockEntity be = level.getBlockEntity(placePos);
            if (be instanceof TackedPaperBlockEntity tpbe && !tpbe.hasFace(face.getOpposite())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
                if (!level.isClientSide) {
                    tpbe.addFace(face.getOpposite());
                    tpbe.setChanged();
                    level.sendBlockUpdated(placePos, existing, existing, 3);
                    if (!player.isCreative()) held.shrink(1);

                }
            }
        } else if (existing.canBeReplaced()) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
            if (!level.isClientSide) {
                level.setBlock(placePos, GeneralBlocks.TACKED_PAPER.get().defaultBlockState(), 3);
                BlockEntity be = level.getBlockEntity(placePos);
                if (be instanceof TackedPaperBlockEntity tpbe) {
                    tpbe.addFace(face.getOpposite());
                    tpbe.setChanged();
                }
                if (level.getBlockEntity(placePos) instanceof TackedPaperBlockEntity tpbe) {
                    tpbe.addFace(face);
                    level.sendBlockUpdated(placePos, level.getBlockState(placePos), level.getBlockState(placePos), 3);
                }
                level.playSound(null, placePos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0f, 1.2f);
                if (!player.isCreative()) held.shrink(1);
            }
        }
    }
}
