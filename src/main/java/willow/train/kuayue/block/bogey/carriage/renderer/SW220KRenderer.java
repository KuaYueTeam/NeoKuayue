package willow.train.kuayue.block.bogey.carriage.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import willow.train.kuayue.initial.create.AllCarriageBogeys;
import willow.train.kuayue.initial.AllElements;


public class SW220KRenderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    public static PartialModel SW220K_FRAME =
            PartialModel.of(asBlockModelResource("bogey/sw220k/sw220k_frame"));
    public static PartialModel SW220K_WHEEL =
            PartialModel.of(asBlockModelResource("bogey/sw220k/sw220k_wheel"));

    public static double SW220K_FRAME_TRANS_Y = 0.925F;

    

    @Override
    public void render(
            CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {

        RenderType type = RenderType.cutoutMipped();
        BlockState air = Blocks.AIR.defaultBlockState();

        // 1. 渲染框架
        CachedBuffers.partial(SW220K_FRAME, air)
                .light(packedLight)
                .overlay(packedOverlay)
                .renderInto(poseStack, bufferSource.getBuffer(type));

        // 2. 渲染轮对
        for (int side : Iterate.positiveAndNegative) {
            poseStack.pushPose();

            // 应用变换逻辑
            poseStack.translate(0, SW220K_FRAME_TRANS_Y, (double) side * 1.18d);
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(wheelAngle));

            CachedBuffers.partial(SW220K_WHEEL, air)
                    .light(packedLight)
                    .overlay(packedOverlay)
                    .renderInto(poseStack, bufferSource.getBuffer(type));

            poseStack.popPose();
        }
    }

    public static class Backward implements BogeyRenderer {
        

        @Override
        public void render(
                CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {

            RenderType type = RenderType.cutoutMipped();
            BlockState air = Blocks.AIR.defaultBlockState();

            // 1. 渲染框架
            CachedBuffers.partial(SW220K_FRAME, air)
                    .light(packedLight)
                    .overlay(packedOverlay)
                    .renderInto(poseStack, bufferSource.getBuffer(type));

            // 2. 渲染轮对
            for (int side : Iterate.positiveAndNegative) {
                poseStack.pushPose();

                // 应用变换逻辑
                poseStack.translate(0, SW220K_FRAME_TRANS_Y, (double) side * 1.18d);
                poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(wheelAngle));

                CachedBuffers.partial(SW220K_WHEEL, air)
                        .light(packedLight)
                        .overlay(packedOverlay)
                        .renderInto(poseStack, bufferSource.getBuffer(type));

                poseStack.popPose();
            }
        }
    }
    public static class Andesite extends SW220KRenderer {
        @Override
        public void render(
                CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {
            poseStack.pushPose();
            poseStack.scale(1.2F, 1, 1);
            super.render(bogeyData,
                    wheelAngle,
                    partialTick,
                    poseStack,
                    bufferSource,
                    packedLight,
                    packedOverlay,
                    inContraption);
            poseStack.popPose();
        }

        public static class Backward extends SW220KRenderer.Backward {
            @Override
            public void render(
                    CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {
                poseStack.pushPose();
                poseStack.scale(1.2F, 1, 1);
                super.render(bogeyData,
                        wheelAngle,
                        partialTick,
                        poseStack,
                        bufferSource,
                        packedLight,
                        packedOverlay,
                        inContraption);
                poseStack.popPose();
            }
        }
    }
}
