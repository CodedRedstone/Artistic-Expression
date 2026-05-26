package com.unessesaryguide.artisticexpression.datagen;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import com.unessesaryguide.artisticexpression.item.GeneralItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.util.Map;

public class ModItemModelProvider extends ItemModelProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    public ModItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, ArtisticExpression.MODID, helper);
    }

    @Override
    protected void registerModels() {
        addColoredItemModels("shaved_fleece", GeneralItems.SHAVED_FLEECE);

        // Future additions:
        // addColoredItemModels("silk", TextileBlocks.SILK);
        // addColoredItemModels("terracotta_bricks", TerracottaBlocks.TERRACOTTA_BRICKS);
    }

    /**
     * Loops through all DyeColors, generates a model JSON pointing
     * to the texture for each color variant.
     * Logs a warning if a color is missing from the map.
     */
    private <T extends Item> void addColoredItemModels(
        String baseName,
        Map<DyeColor, DeferredItem<T>> itemMap) {

        for (DyeColor color : DyeColor.values()) {
            DeferredItem<T> item = itemMap.get(color);

            if (item == null) {
                LOGGER.warn("[Artistic Expression] Missing model for color '{}' with base name '{}'",
                    color.getName(), baseName);
                continue;
            }

            String itemName = color.getName() + "_" + baseName;

            withExistingParent(itemName, "item/generated")
                .texture("layer0", ArtisticExpression.MODID + ":item/" + itemName);
        }
    }
}
