package com.unessesaryguide.artisticexpression.datagen;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import com.unessesaryguide.artisticexpression.block.GeneralBlocks;
import com.unessesaryguide.artisticexpression.item.GeneralItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public static final TagKey<Item> SHAVED_FLEECE = ItemTags.create(
        ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "shaved_fleece")
    );

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    public static final TagKey<Item> STRAND = ItemTags.create(
        ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "strand")
    );
    public static final TagKey<Item> WAX = ItemTags.create(
        ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "wax")
    );
    public static final TagKey<Item> GIANT_CANDLE = ItemTags.create(
        ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "giant_candle")
    );

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // existing fleece tag...
        var fleeceTag = tag(SHAVED_FLEECE);
        for (DyeColor color : DyeColor.values()) {
            var fleece = GeneralItems.SHAVED_FLEECE.get(color);
            if (fleece != null) fleeceTag.add(fleece.get());
        }

        var candleTag = tag(GIANT_CANDLE);
        candleTag.add(GeneralItems.GIANT_CANDLE.get());
        for (DyeColor color : DyeColor.values()) {
            candleTag.add(GeneralItems.COLORED_GIANT_CANDLE.get(color).get());
        }

        tag(STRAND)
            .add(Items.STRING)
            .add(GeneralItems.THREAD.get());

        tag(WAX)
            .add(Items.HONEYCOMB)
            .add(GeneralItems.LARD.get());
    }
}
