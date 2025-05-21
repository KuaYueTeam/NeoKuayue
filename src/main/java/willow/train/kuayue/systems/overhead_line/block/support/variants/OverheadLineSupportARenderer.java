package willow.train.kuayue.systems.overhead_line.block.support.variants;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import kasuga.lib.core.client.model.BedrockModelLoader;
import kasuga.lib.core.client.model.anim_model.AnimModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.systems.overhead_line.block.support.AllOverheadLineSupportBlocks;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlock;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlockEntity;

import java.util.List;
import java.util.Vector;

public abstract class OverheadLineSupportARenderer {

    public static class A1Renderer implements BlockEntityRenderer<OverheadLineSupportBlockEntity> {
        public A1Renderer(BlockEntityRendererProvider.Context context) {}
        @Override
        public void render(OverheadLineSupportBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
            pPoseStack.pushPose();
            pPoseStack.mulPoseMatrix(
                    AllOverheadLineSupportModels.getDirectionOf.apply(
                            pBlockEntity.getBlockState().getValue(OverheadLineSupportBlock.FACING), 1.3f
                    )
            );
            // pPoseStack.mulPose(new Quaternion(90, 0, 0, true));
            AllOverheadLineSupportModels.OVERHEAD_LINE_SUPPORT_A1_MODEL.render(
                    pPoseStack,
                    pBufferSource,
                    pPackedLight,
                    pPackedOverlay
            );
            pPoseStack.popPose();
        }
    }

    public static class A2Renderer implements BlockEntityRenderer<OverheadLineSupportBlockEntity> {
        public A2Renderer(BlockEntityRendererProvider.Context context) {}
        @Override
        public void render(OverheadLineSupportBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
            pPoseStack.pushPose();
            pPoseStack.mulPoseMatrix(
                    AllOverheadLineSupportModels.getDirectionOf.apply(
                            pBlockEntity.getBlockState().getValue(OverheadLineSupportBlock.FACING), 1.3f
                    )
            );
            AllOverheadLineSupportModels.OVERHEAD_LINE_SUPPORT_A2_MODEL.render(
                    pPoseStack,
                    pBufferSource,
                    pPackedLight,
                    pPackedOverlay
            );
            pPoseStack.popPose();
        }
    }
}
