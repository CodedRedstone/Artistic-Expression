package com.unessesaryguide.artisticexpression.block;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ArtisticExpression.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TackedPaperBlockEntity>> TACKED_PAPER_BE =
        BLOCK_ENTITIES.register("tacked_paper",
            () -> BlockEntityType.Builder
                .of(TackedPaperBlockEntity::new, GeneralBlocks.TACKED_PAPER.get())
                .build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
