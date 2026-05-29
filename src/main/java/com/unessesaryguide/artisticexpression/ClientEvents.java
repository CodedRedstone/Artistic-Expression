package com.unessesaryguide.artisticexpression;

import com.unessesaryguide.artisticexpression.particle.LargeFlameParticle;
import com.unessesaryguide.artisticexpression.particle.ModParticleTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = ArtisticExpression.MODID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.LARGE_FLAME.get(), LargeFlameParticle.Provider::new);
    }
}
