package com.unessesaryguide.artisticexpression.item;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;

public class GeneralItems {
    public static final DeferredRegister.Items ITEMS =
        DeferredRegister.createItems(ArtisticExpression.MODID);

    public static final Map<DyeColor, DeferredItem<Item>> SHAVED_FLEECE =
        new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            SHAVED_FLEECE.put(color, ITEMS.registerSimpleItem(
                color.getName() + "_shaved_fleece",
                new Item.Properties()
            ));
        }
    }

    public static final DeferredItem<Item> THREAD = ITEMS.registerSimpleItem(
        "thread",
        new Item.Properties()
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
