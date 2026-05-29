package com.unessesaryguide.artisticexpression.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;

public class TackedPaperRenderer implements BlockEntityRenderer<TackedPaperBlockEntity> {

    // The model to render for each face — points north by default,
    // rotated per face below. Replace the model JSON with your BlockBench export.
    private static final ModelResourceLocation MODEL_LOC =
        ModelResourceLocation.standalone(
            ResourceLocation.fromNamespaceAndPath("artisticexpression", "block/tacked_paper")
        );

    public TackedPaperRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(TackedPaperBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        System.out.println("TackedPaperRenderer.render called, faces: " + be.getOccupiedFaces());

        BakedModel model = Minecraft.getInstance().getBlockRenderer()
            .getBlockModel(GeneralBlocks.TACKED_PAPER.get().defaultBlockState());

        System.out.println("Model: " + model + ", missing: " +
            (model == Minecraft.getInstance().getModelManager().getMissingModel()));

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.cutoutMipped());

        for (Direction face : be.getOccupiedFaces()) {
            poseStack.pushPose();

            poseStack.translate(0.5, 0.5, 0.5);
            applyFaceRotation(poseStack, face);
            poseStack.translate(-0.5, -0.5, -0.5);

            Minecraft.getInstance().getBlockRenderer().getModelRenderer()
                .renderModel(poseStack.last(), consumer, null, model,
                    1f, 1f, 1f, packedLight, packedOverlay);

            poseStack.popPose();
        }
    }

    private void applyFaceRotation(PoseStack poseStack, Direction face) {
        switch (face) {
            case NORTH -> {}  // default orientation
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case EAST  -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            case WEST  -> poseStack.mulPose(Axis.YP.rotationDegrees(270));
            case UP    -> poseStack.mulPose(Axis.XP.rotationDegrees(270));
            case DOWN  -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
        }
    }
}
