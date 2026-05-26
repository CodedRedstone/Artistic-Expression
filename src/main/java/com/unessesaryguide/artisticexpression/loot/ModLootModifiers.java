package com.unessesaryguide.artisticexpression.loot;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
        DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ArtisticExpression.MODID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<SheepFleeceLootModifier>> SHEEP_FLEECE =
        LOOT_MODIFIERS.register("sheep_fleece", SheepFleeceLootModifier.CODEC);
}
