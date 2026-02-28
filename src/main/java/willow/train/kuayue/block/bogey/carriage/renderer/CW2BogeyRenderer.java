package willow.train.kuayue.block.bogey.carriage.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;

import static willow.train.kuayue.block.bogey.loco.renderer.HXD3DRenderer.asBlockModelResource;

public class CW2BogeyRenderer implements BogeyRenderer {

    // 假设你依然使用之前定义的 PartialModel
    public static final PartialModel CW2_FRAME = PartialModel.of(asBlockModelResource("bogey/cw2/bogey_cw2_temple"));
    public static final PartialModel CW2_WHEEL = PartialModel.of(asBlockModelResource("bogey/cw2/cw2_wheel"));

    @Override
    public void render(
            CompoundTag bogeyData,
            float wheelAngle,
            float partialTick,
            PoseStack ms,
            MultiBufferSource bufferSource,
            int light,
            int overlay,
            boolean inContraption) {

        // 在新版接口中，我们需要手动获取 RenderType
        // 通常转向架使用剪切或实色渲染
        RenderType type = RenderType.cutoutMipped();
        BlockState air = Blocks.AIR.defaultBlockState();

        // 1. 渲染框架
        CachedBuffers.partial(CW2_FRAME, air)
                .light(light)
                .overlay(overlay)
                .renderInto(ms, bufferSource.getBuffer(type));

        // 2. 渲染轮对
        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();

            // 应用变换逻辑
            ms.translate(0, 0.805, (double) side * 1.18d);
            // 绕 X 轴旋转（注意：Create 0.6 推荐使用 Vector3f 或 Axis.XP）
            ms.mulPose(com.mojang.math.Axis.XP.rotationDegrees(wheelAngle));

            CachedBuffers.partial(CW2_WHEEL, air)
                    .light(light)
                    .overlay(overlay)
                    .renderInto(ms, bufferSource.getBuffer(type));

            ms.popPose();
        }
    }
    public static class Andesite extends CW2BogeyRenderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {
            poseStack.pushPose();
            poseStack.scale(1.2F, 1.0F, 1.0F);

            // 必须严格按照 8 个参数的顺序传递
            super.render(
                    bogeyData,
                    wheelAngle,
                    partialTick,
                    poseStack,
                    bufferSource,
                    packedLight,
                    packedOverlay,
                    inContraption
            );

            poseStack.popPose();
        }
    }
}