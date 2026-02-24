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

public class SW160Renderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    public static PartialModel SW160_FRAME = PartialModel.of(asBlockModelResource("bogey/sw160/sw160_frame"));
    public static PartialModel SW160_WHEEL = PartialModel.of(asBlockModelResource("bogey/sw160/sw160_wheel"));
    

    @Override
    public void render(
            CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {

        RenderType type = RenderType.cutoutMipped();
        BlockState air = Blocks.AIR.defaultBlockState();
        CachedBuffers.partial(SW160_FRAME, air)
                .light(packedLight)
                .overlay(packedOverlay)
                .renderInto(poseStack, bufferSource.getBuffer(type));

        for (int side : Iterate.positiveAndNegative) {
            poseStack.pushPose();

            // 应用变换逻辑
            poseStack.translate(0, 0.805, (double) side * 1.18d);
            // 绕 X 轴旋转（注意：Create 0.6 推荐使用 Vector3f 或 Axis.XP）
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(wheelAngle));

            CachedBuffers.partial(SW160_WHEEL, air)
                    .light(packedLight)
                    .overlay(packedOverlay)
                    .renderInto(poseStack, bufferSource.getBuffer(type));

            poseStack.popPose();
        }
    }
    public static class Andesite extends SW160Renderer {
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
