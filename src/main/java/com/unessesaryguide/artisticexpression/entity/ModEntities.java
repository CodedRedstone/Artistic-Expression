package com.unessesaryguide.artisticexpression.entity;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
        DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ArtisticExpression.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<TackedPaperEntity>> TACKED_PAPER =
        ENTITIES.register("tacked_paper", () ->
            EntityType.Builder.<TackedPaperEntity>of(TackedPaperEntity::new, MobCategory.MISC)
                .sized(0.5f, 0.5f)
                .clientTrackingRange(10)
                .updateInterval(Integer.MAX_VALUE)
                .build("tacked_paper")
        );

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
