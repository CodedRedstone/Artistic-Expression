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
        boolean hasBaseLit = existingFileHelper.exists(
            ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "textures/block/giant_candle_lit.png"),
            net.minecraft.server.packs.PackType.CLIENT_RESOURCES);

        models().getBuilder("giant_candle_lit")
            .parent(new ModelFile.UncheckedModelFile(
                ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "block/giant_candle")))
            .texture("1", ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID,
                hasBaseLit ? "block/giant_candle_lit" : "block/giant_candle"));

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

        ModelFile unlit;
        ModelFile lit;

        if (isBase) {
            unlit = new ModelFile.UncheckedModelFile(
                ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "block/" + name));
            lit = new ModelFile.UncheckedModelFile(
                ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "block/" + name + "_lit"));
        } else {
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "block/" + name);
            ResourceLocation textureLit = ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "block/" + name + "_lit");

            boolean hasLit = existingFileHelper.exists(
                ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "textures/block/" + name + "_lit.png"),
                net.minecraft.server.packs.PackType.CLIENT_RESOURCES);

            boolean hasParticle = existingFileHelper.exists(
                ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "textures/block/" + name + "_particle.png"),
                net.minecraft.server.packs.PackType.CLIENT_RESOURCES);

            ResourceLocation particleTexture = ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID,
                hasParticle ? "block/" + name + "_particle" : "block/giant_candle_particle");

            unlit = models().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(
                    ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "block/giant_candle")))
                .texture("1", texture)
                .texture("particle", particleTexture);

            lit = models().getBuilder(name + "_lit")
                .parent(new ModelFile.UncheckedModelFile(
                    ResourceLocation.fromNamespaceAndPath(ArtisticExpression.MODID, "block/giant_candle_lit")))
                .texture("1", hasLit ? textureLit : texture)
                .texture("particle", particleTexture);
        }

        getVariantBuilder(isBase
            ? GeneralBlocks.GIANT_CANDLE.get()
            : GeneralBlocks.COLORED_GIANT_CANDLE.get(DyeColor.byName(name.replace("_giant_candle", ""), DyeColor.WHITE)).get())
            .partialState().with(BlockStateProperties.LIT, false).modelForState().modelFile(unlit).addModel()
            .partialState().with(BlockStateProperties.LIT, true).modelForState().modelFile(lit).addModel();
    }
}
