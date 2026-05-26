package com.unessesaryguide.artisticexpression.datagen;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import com.unessesaryguide.artisticexpression.item.GeneralItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredItem;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.util.Map;

public class ModLanguageProvider extends LanguageProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    public ModLanguageProvider(PackOutput output) {
        super(output, ArtisticExpression.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Creative tab
        add("itemGroup.artisticexpression", "Artistic Expression");

        // Add colored variants by passing the base name and item map
        addColoredItems("shaved_fleece", GeneralItems.SHAVED_FLEECE);
    }

    /**
     * Loops through all DyeColors, looks up the item in the map,
     * and adds "[Color] [Formatted Name]" as the translation.
     * Logs a warning if a color is missing from the map.
     */
    private <T extends Item> void addColoredItems(
        String baseName,
        Map<DyeColor, DeferredItem<T>> itemMap) {

        for (DyeColor color : DyeColor.values()) {
            DeferredItem<T> item = itemMap.get(color);

            if (item == null) {
                LOGGER.warn("[Artistic Expression] Missing item for color '{}' with base name '{}'",
                    color.getName(), baseName);
                continue;
            }

            add(item.get(), formatName(color.getName()) + " " + formatName(baseName));
        }
    }

    private String formatName(String name) {
        String[] words = name.split("_");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0)))
                .append(word.substring(1))
                .append(" ");
        }
        return sb.toString().trim();
    }
}
