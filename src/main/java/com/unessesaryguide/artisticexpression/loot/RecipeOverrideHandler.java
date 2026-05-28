package com.unessesaryguide.artisticexpression.loot;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.minecraft.server.MinecraftServer;

@EventBusSubscriber(modid = ArtisticExpression.MODID)
public class RecipeOverrideHandler {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        var recipeManager = server.getRecipeManager();

        // Log to verify recipes are being found
        var bow = recipeManager.byKey(ResourceLocation.fromNamespaceAndPath("minecraft", "bow"));
        bow.ifPresent(r -> System.out.println("Found bow recipe: " + r.id()));
    }
}
