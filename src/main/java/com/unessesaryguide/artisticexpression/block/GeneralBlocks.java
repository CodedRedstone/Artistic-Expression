package com.unessesaryguide.artisticexpression.block;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;

public class GeneralBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
        DeferredRegister.createBlocks(ArtisticExpression.MODID);

    // Base uncolored candle
    public static final DeferredBlock<GiantCandleBlock> GIANT_CANDLE =
        BLOCKS.register("giant_candle",
            () -> new GiantCandleBlock(BlockBehaviour.Properties.of()
                .strength(0.1f)
                .sound(SoundType.CANDLE)
                .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 12 : 0)
                .noOcclusion()
            ));

    public static final Map<DyeColor, DeferredBlock<GiantCandleBlock>> COLORED_GIANT_CANDLE =
        new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            COLORED_GIANT_CANDLE.put(color, BLOCKS.register(
                color.getName() + "_giant_candle",
                () -> new GiantCandleBlock(BlockBehaviour.Properties.of()
                    .strength(0.1f)
                    .sound(SoundType.CANDLE)
                    .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 14 : 0)
                    .noOcclusion()
                )
            ));
        }
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
