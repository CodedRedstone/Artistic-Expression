package com.unessesaryguide.artisticexpression.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TackedPaperRenderer extends EntityRenderer<TackedPaperEntity> {

    private static final ResourceLocation TEXTURE =
        ResourceLocation.fromNamespaceAndPath("artisticexpression", "textures/block/tacked_paper.png");

    public TackedPaperRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TackedPaperEntity entity, float yaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        System.out.println("RENDER CALLED");

        poseStack.pushPose();

        // Rotate to face the correct wall
        switch (entity.getDirection()) {
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(0));
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case EAST  -> poseStack.mulPose(Axis.YP.rotationDegrees(270));
            case WEST  -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            default    -> {}
        }

        // Shift so the paper sits on the wall surface facing north (Z = -0.5 from entity center)
        poseStack.translate(0, 0, -0.5);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));
        PoseStack.Pose pose = poseStack.last();

        // Draw a simple flat quad: 12x11 pixels centered, sitting flush on the wall
        float x0 = -2f, x1 = 2f;
        float y0 = -2f, y1 = 2f;
        float z  =  0.1f;

        // front face (facing away from wall)
        vertex(consumer, pose, x0, y1, z, 0, 0, packedLight);
        vertex(consumer, pose, x0, y0, z, 0, 1, packedLight);
        vertex(consumer, pose, x1, y0, z, 1, 1, packedLight);
        vertex(consumer, pose, x1, y1, z, 1, 0, packedLight);

        poseStack.popPose();

        super.render(entity, yaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void vertex(VertexConsumer consumer, PoseStack.Pose pose,
                        float x, float y, float z, float u, float v, int light) {
        consumer.addVertex(pose, x, y, z)
            .setColor(255, 255, 255, 255)
            .setUv(u, v)
            .setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
            .setLight(light)
            .setNormal(pose, 0, 0, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(TackedPaperEntity entity) {
        return TEXTURE;
    }
}
