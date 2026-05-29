package com.unessesaryguide.artisticexpression.datagen;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import com.unessesaryguide.artisticexpression.block.GeneralBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider {

    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new LootTableProvider(output, Set.of(), List.of(
            new LootTableProvider.SubProviderEntry(
                prov -> new ModBlockLoot(prov),
                net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.BLOCK
            )
        ), registries);
    }

    private static class ModBlockLoot extends BlockLootSubProvider {

        protected ModBlockLoot(HolderLookup.Provider provider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
        }

        @Override
        protected void generate() {
            dropSelf(GeneralBlocks.GIANT_CANDLE.get());

            for (DyeColor color : DyeColor.values()) {
                Block block = GeneralBlocks.COLORED_GIANT_CANDLE.get(color).get();
                if (block != null) dropSelf(block);
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            List<Block> blocks = new java.util.ArrayList<>();
            blocks.add(GeneralBlocks.GIANT_CANDLE.get());
            for (DyeColor color : DyeColor.values()) {
                Block block = GeneralBlocks.COLORED_GIANT_CANDLE.get(color).get();
                if (block != null) blocks.add(block);
            }
            return blocks;
        }
    }
}
