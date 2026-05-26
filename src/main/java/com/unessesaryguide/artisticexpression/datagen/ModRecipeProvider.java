package com.unessesaryguide.artisticexpression.datagen;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import com.unessesaryguide.artisticexpression.item.GeneralItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.DyeColor;
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
            System.out.println("Processing: " + color.getName() + " fleece=" + fleeceEntry + " wool=" + woolItem);
            if (fleeceEntry == null || woolItem == null) continue;

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woolItem)
                .pattern("FF")
                .pattern("FF")
                .define('F', fleeceEntry.get())
                .unlockedBy("has_fleece", has(fleeceEntry.get()))
                .save(output, ArtisticExpression.MODID + ":fleece_to_" + color.getName() + "_wool");
        }
    }
}
