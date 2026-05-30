package com.unessesaryguide.artisticexpression.entity;

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
        BlockPos attachPos = event.getPos();

        // Check the surface is solid enough to hang on
        if (!level.getBlockState(attachPos).isFaceSturdy(level, attachPos, face)) return;

        event.setCanceled(true);

        if (!level.isClientSide) {
            TackedPaperEntity entity = new TackedPaperEntity(level, attachPos, face);
            level.addFreshEntity(entity);
            level.playSound(null, attachPos, SoundEvents.ITEM_FRAME_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);

            if (!player.isCreative()) {
                held.shrink(1);
            }
        }
    }
}
