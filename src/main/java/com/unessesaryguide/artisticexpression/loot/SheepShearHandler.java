package com.unessesaryguide.artisticexpression.loot;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import com.unessesaryguide.artisticexpression.item.GeneralItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = ArtisticExpression.MODID)
public class SheepShearHandler {

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Sheep sheep)) return;
        if (sheep.isSheared()) return;

        Player player = event.getEntity();
        ItemStack held = player.getItemInHand(event.getHand());

        if (!held.is(Items.SHEARS)) return;

        DyeColor color = sheep.getColor();
        var fleeceItem = GeneralItems.SHAVED_FLEECE.get(color);
        if (fleeceItem == null) return;

        // Cancel the vanilla interaction to prevent default wool drop
        event.setCanceled(true);

        if (!sheep.level().isClientSide) {
            sheep.setSheared(true);
            EquipmentSlot slot = player.getUsedItemHand() == net.minecraft.world.InteractionHand.MAIN_HAND
                ? EquipmentSlot.MAINHAND
                : EquipmentSlot.OFFHAND;
            held.hurtAndBreak(1, player, slot);
            int woolCount = sheep.level().getRandom().nextIntBetweenInclusive(1, 3);
            sheep.spawnAtLocation(new ItemStack(fleeceItem.get(), woolCount * 4));
        }
    }
}
