package com.unessesaryguide.artisticexpression.datagen;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import com.unessesaryguide.artisticexpression.item.GeneralItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private static final Map<DyeColor, net.minecraft.world.item.Item> WOOL_BY_COLOR = Map.ofEntries(
        Map.entry(DyeColor.WHITE,      Items.WHITE_WOOL),
        Map.entry(DyeColor.ORANGE,     Items.ORANGE_WOOL),
        Map.entry(DyeColor.MAGENTA,    Items.MAGENTA_WOOL),
        Map.entry(DyeColor.LIGHT_BLUE, Items.LIGHT_BLUE_WOOL),
        Map.entry(DyeColor.YELLOW,     Items.YELLOW_WOOL),
        Map.entry(DyeColor.LIME,       Items.LIME_WOOL),
        Map.entry(DyeColor.PINK,       Items.PINK_WOOL),
        Map.entry(DyeColor.GRAY,       Items.GRAY_WOOL),
        Map.entry(DyeColor.LIGHT_GRAY, Items.LIGHT_GRAY_WOOL),
        Map.entry(DyeColor.CYAN,       Items.CYAN_WOOL),
        Map.entry(DyeColor.PURPLE,     Items.PURPLE_WOOL),
        Map.entry(DyeColor.BLUE,       Items.BLUE_WOOL),
        Map.entry(DyeColor.BROWN,      Items.BROWN_WOOL),
        Map.entry(DyeColor.GREEN,      Items.GREEN_WOOL),
        Map.entry(DyeColor.RED,        Items.RED_WOOL),
        Map.entry(DyeColor.BLACK,      Items.BLACK_WOOL)
    );

    @Override
    public void buildRecipes(RecipeOutput output) {
        for (DyeColor color : DyeColor.values()) {
            var fleeceEntry = GeneralItems.SHAVED_FLEECE.get(color);
            var woolItem = WOOL_BY_COLOR.get(color);
            if (fleeceEntry == null || woolItem == null) continue;

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woolItem)
                .pattern("FF")
                .pattern("FF")
                .define('F', fleeceEntry.get())
                .unlockedBy("has_fleece", has(fleeceEntry.get()))
                .save(output, ArtisticExpression.MODID + ":fleece_to_" + color.getName() + "_wool");
        }

        for (DyeColor color : DyeColor.values()) {
            var candleItem = GeneralItems.COLORED_GIANT_CANDLE.get(color);
            if (candleItem == null) continue;

            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, candleItem.get())
                .requires(ModItemTagProvider.GIANT_CANDLE) // any giant candle
                .requires(DyeItem.byColor(color))
                .unlockedBy("has_candle", has(GeneralItems.GIANT_CANDLE.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID,
                    color.getName() + "_giant_candle_from_dyeing"));
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, GeneralItems.GIANT_CANDLE)
            .pattern("S ")
            .pattern("##")
            .pattern("##")
            .define('S', ModItemTagProvider.STRAND)
            .define('#', ModItemTagProvider.WAX)
            .unlockedBy("has_strand", has(ModItemTagProvider.STRAND))
            .save(output, ResourceLocation.fromNamespaceAndPath("artisticexpression", "giant_candle"));

        // Bow - uses minecraft:bow as the key, overriding vanilla
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.BOW)
            .pattern(" S#")
            .pattern("S #")
            .pattern(" S#")
            .define('#', ModItemTagProvider.STRAND)
            .define('S', Items.STICK)
            .unlockedBy("has_strand", has(ModItemTagProvider.STRAND))
            .save(output, ResourceLocation.fromNamespaceAndPath("minecraft", "bow"));


        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.BOW)
            .pattern("  S")
            .pattern(" S#")
            .pattern("S #")
            .define('#', ModItemTagProvider.STRAND)
            .define('S', Items.STICK)
            .unlockedBy("has_strand", has(ModItemTagProvider.STRAND))
            .save(output, ResourceLocation.fromNamespaceAndPath("minecraft", "fishing_rod"));


        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.LOOM)
            .pattern("SS")
            .pattern("##")
            .define('S', ModItemTagProvider.STRAND)
            .define('#', ItemTags.PLANKS)
            .unlockedBy("has_strand", has(ModItemTagProvider.STRAND))
            .save(output, ResourceLocation.fromNamespaceAndPath("minecraft", "loom"));


        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.LOOM)
            .pattern("S")
            .pattern("#")
            .define('S', ModItemTagProvider.STRAND)
            .define('#', ModItemTagProvider.WAX)
            .unlockedBy("has_strand", has(ModItemTagProvider.STRAND))
            .save(output, ResourceLocation.fromNamespaceAndPath("minecraft", "candle"));

    }
}
