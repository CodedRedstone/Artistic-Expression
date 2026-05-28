package com.unessesaryguide.artisticexpression.datagen;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import com.unessesaryguide.artisticexpression.block.GeneralBlocks;
import com.unessesaryguide.artisticexpression.block.GiantCandleBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    private final ExistingFileHelper existingFileHelper;

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArtisticExpression.MODID, existingFileHelper);
        this.existingFileHelper = existingFileHelper; // store it
    }

    @Override
    protected void registerStatesAndModels() {
        registerGiantCandle("giant_candle");

        for (DyeColor color : DyeColor.values()) {
            String name = color.getName() + "_giant_candle";

            if (!existingFileHelper.exists(
                ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "textures/block/" + name + ".png"),
                net.minecraft.server.packs.PackType.CLIENT_RESOURCES)) {
                continue;
            }

            registerGiantCandle(name);
        }
    }

    private void registerGiantCandle(String name) {
        boolean isBase = name.equals("giant_candle");

        ResourceLocation unlitLoc = ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "block/" + name);
        ResourceLocation litLoc = ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "block/" + name + "_lit");

        ModelFile unlit = new ModelFile.UncheckedModelFile(unlitLoc);

        // Use lit model if it exists, otherwise fall back to unlit
        boolean hasLit = existingFileHelper.exists(
            ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "textures/block/" + name + "_lit.png"),
            net.minecraft.server.packs.PackType.CLIENT_RESOURCES);

        ModelFile lit = hasLit
            ? new ModelFile.UncheckedModelFile(litLoc)
            : unlit; // fallback to unlit model

        getVariantBuilder(isBase
            ? GeneralBlocks.GIANT_CANDLE.get()
            : GeneralBlocks.COLORED_GIANT_CANDLE.get(DyeColor.byName(name.replace("_giant_candle", ""), DyeColor.WHITE)).get())
            .partialState().with(BlockStateProperties.LIT, false).modelForState().modelFile(unlit).addModel()
            .partialState().with(BlockStateProperties.LIT, true).modelForState().modelFile(lit).addModel();
    }
}
