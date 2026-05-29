package com.unessesaryguide.artisticexpression.item;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import com.unessesaryguide.artisticexpression.block.GeneralBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;

import static com.unessesaryguide.artisticexpression.block.GeneralBlocks.COLORED_GIANT_CANDLE;

public class GeneralItems {
    public static final DeferredRegister.Items ITEMS =
        DeferredRegister.createItems(ArtisticExpression.MODID);

    public static final Map<DyeColor, DeferredItem<Item>> SHAVED_FLEECE =
        new EnumMap<>(DyeColor.class);

    public static final Map<DyeColor, DeferredItem<BlockItem>> COLORED_GIANT_CANDLE =
        new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            SHAVED_FLEECE.put(color, ITEMS.registerSimpleItem(
                color.getName() + "_shaved_fleece",
                new Item.Properties()
            ));
        }

        for (DyeColor color : DyeColor.values()) {
            COLORED_GIANT_CANDLE.put(color, ITEMS.registerSimpleBlockItem(
                GeneralBlocks.COLORED_GIANT_CANDLE.get(color)
            ));
        }
    }

    public static final DeferredItem<Item> THREAD = ITEMS.registerSimpleItem(
        "thread",
        new Item.Properties()
    );

    public static final DeferredItem<Item> LARD = ITEMS.registerSimpleItem(
        "lard",
        new Item.Properties()
    );

    public static final DeferredItem<BlockItem> GIANT_CANDLE =
        ITEMS.registerSimpleBlockItem(GeneralBlocks.GIANT_CANDLE);

    // Tacked paper — BlockItem is registered so it exists in the registry,
    // but placement is handled by PaperPlacementHandler (right-click vanilla paper).
    // Players don't obtain this item directly; it's internal to the block.
    public static final DeferredItem<BlockItem> TACKED_PAPER =
        ITEMS.registerSimpleBlockItem(GeneralBlocks.TACKED_PAPER);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
