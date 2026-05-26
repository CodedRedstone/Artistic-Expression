package com.unessesaryguide.artisticexpression;

import com.unessesaryguide.artisticexpression.item.GeneralItems;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import org.slf4j.Logger;

@Mod(ArtisticExpression.MODID)
public class ArtisticExpression {
    public static final String MODID = "artisticexpression";
    public static final Logger LOGGER = LogUtils.getLogger();

    // Creative Tab
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARTISTIC_TAB =
        CREATIVE_MODE_TABS.register("artistic_expression_tab", () ->
            CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.artisticexpression"))
                .withTabsBefore(CreativeModeTabs.COMBAT)
                .icon(() -> GeneralItems.SHAVED_FLEECE.get(DyeColor.WHITE).get().getDefaultInstance())
                .displayItems((parameters, output) -> {

                    for (DyeColor color : DyeColor.values()) {
                        output.accept(GeneralItems.SHAVED_FLEECE.get(color).get());
                    }
                }).build());

    public ArtisticExpression(IEventBus modEventBus) {
        GeneralItems.ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
