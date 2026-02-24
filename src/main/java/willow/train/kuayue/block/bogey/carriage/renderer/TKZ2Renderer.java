package willow.train.kuayue.block.bogey.carriage.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.mojang.math.Axis;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import kasuga.lib.core.create.BogeyDataConstants;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.nbt.NBTHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.create.AllCarriageBogeys;

public class TKZ2Renderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    // 漏洞修复 1: 使用 PartialModel.of 和正确路径
    public static final PartialModel
            TKZ2_FRAME = PartialModel.of(asBlockModelResource("bogey/tkz2/tkz2_frame")),
            TKZ2_WHEEL = PartialModel.of(asBlockModelResource("bogey/tkz2/tkz2_wheel")),
            TKZ2_MOTOR_FRAME = PartialModel.of(asBlockModelResource("bogey/tkz2/tkz2_motor_frame")),
            TKZ2_MOTOR_WHEEL = PartialModel.of(asBlockModelResource("bogey/tkz2/tkz2_motor_wheel")),
            TKZ2_MOTOR_PONY_WHEEL = PartialModel.of(asBlockModelResource("bogey/tkz2/tkz2_motor_pony_wheel"));

    // 漏洞修复 2: 修正参数列表
    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                       MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {

        var buffer = bufferSource.getBuffer(RenderType.cutoutMipped());
        var air = Blocks.AIR.defaultBlockState();
        int adjustedLight = (int) (light * 1.1f);

        ms.pushPose();
        // 渲染基础框架
        ms.pushPose();
        ms.translate(0, 0.195, 0);
        CachedBuffers.partial(TKZ2_FRAME, air).light(adjustedLight).overlay(overlay).renderInto(ms, buffer);
        ms.popPose();

        // 渲染基础轮对
        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            ms.translate(0, 0.695, (double) side * 0.97d);
            ms.mulPose(Axis.XP.rotationDegrees(wheelAngle * 1.22f));
            CachedBuffers.partial(TKZ2_WHEEL, air).light(adjustedLight).overlay(overlay).renderInto(ms, buffer);
            ms.popPose();
        }
        ms.popPose();
    }


    // --- 内部类：带有动力装置的 TKZ2 ---
    public static class Motor implements BogeyRenderer {
        private final boolean backward; // 区分 Motor 和 MotorBackward

        public Motor(boolean backward) {
            this.backward = backward;
        }

        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {

            var buffer = bufferSource.getBuffer(RenderType.cutoutMipped());
            var air = Blocks.AIR.defaultBlockState();
            int adjustedLight = (int) (light * 1.1f);

            // 核心数学逻辑：计算是否需要 180 度翻转
            Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                    ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                    : Direction.NORTH;

            boolean isPositive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;

            // 如果是正向挂载且是 Motor，或者负向挂载且是 Backward，则需要旋转
            // 这种异或逻辑直接干掉了所有 if-else
            boolean shouldFlip = (isPositive ^ backward);
            float yaw = shouldFlip ? 180 : 0;
            float angleMultiplier = shouldFlip ? -1 : 1;

            ms.pushPose();
            ms.mulPose(Axis.YP.rotationDegrees(yaw));

            // 1. 动力车架
            ms.pushPose();
            ms.translate(0, 0.129, 0);
            CachedBuffers.partial(TKZ2_MOTOR_FRAME, air).light(adjustedLight).overlay(overlay).renderInto(ms, buffer);
            ms.popPose();

            // 2. 前轮 (普通)
            ms.pushPose();
            ms.translate(0, 0.695, 0.9761);
            ms.mulPose(Axis.XP.rotationDegrees(wheelAngle * 1.22f * angleMultiplier));
            CachedBuffers.partial(TKZ2_WHEEL, air).light(adjustedLight).overlay(overlay).renderInto(ms, buffer);
            ms.popPose();

            // 3. 后轮 (动力轮)
            ms.pushPose();
            ms.translate(0, 0.695, -0.9761);
            ms.mulPose(Axis.XP.rotationDegrees(wheelAngle * 1.22f * angleMultiplier));
            CachedBuffers.partial(TKZ2_MOTOR_WHEEL, air).light(adjustedLight).overlay(overlay).renderInto(ms, buffer);
            ms.popPose();

            // 4. 从轮 (Pony Wheel)
            ms.pushPose();
            ms.translate(0, 0.7235, -1.94);
            ms.mulPose(Axis.XP.rotationDegrees(wheelAngle * 3.256f * angleMultiplier));
            CachedBuffers.partial(TKZ2_MOTOR_PONY_WHEEL, air).light(adjustedLight).overlay(overlay).renderInto(ms, buffer);
            ms.popPose();

            ms.popPose();
        }

    }
}