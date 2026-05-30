package com.unessesaryguide.artisticexpression;

import com.unessesaryguide.artisticexpression.entity.ModEntities;
import com.unessesaryguide.artisticexpression.entity.TackedPaperRenderer;
import com.unessesaryguide.artisticexpression.particle.LargeFlameParticle;
import com.unessesaryguide.artisticexpression.particle.ModParticleTypes;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = ArtisticExpression.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.LARGE_FLAME.get(), LargeFlameParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.TACKED_PAPER.get(), ctx -> new TackedPaperRenderer(ctx));
    }


    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        System.out.println("Registering TackedPaperRenderer for: " + ModEntities.TACKED_PAPER.get());
        event.register(ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath("artisticexpression", "block/tacked_paper")
        ));
    }
}
