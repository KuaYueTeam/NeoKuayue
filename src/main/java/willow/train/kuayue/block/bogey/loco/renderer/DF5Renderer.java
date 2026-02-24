package willow.train.kuayue.block.bogey.loco.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.mojang.math.Axis;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import kasuga.lib.core.create.BogeyDataConstants;
import net.createmod.catnip.nbt.NBTHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.create.AllLocoBogeys;

public class DF5Renderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    // 漏洞修复 1: PartialModel.of + 正确路径
    public static final PartialModel
            DF5_FRAME = PartialModel.of(asBlockModelResource("bogey/df5/df5_frame")),
            DF5_WHEEL = PartialModel.of(asBlockModelResource("bogey/df5/df5_wheel"));

    private static final double FRAME_TRANS_Y = 1.00;
    private static final double WHEEL_TRANS_Y = 0.878;
    private static final double WHEEL_TRANS_Z = 1.995;

    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                       MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {

        renderDF5(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, false);
    }

    // 统一渲染核心逻辑，干掉子类的冗余
    private void renderDF5(CompoundTag bogeyData, float wheelAngle, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay,
                           boolean inContraption, boolean backward) {

        var buffer = bufferSource.getBuffer(RenderType.cutoutMipped());
        var air = Blocks.AIR.defaultBlockState();

        Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                : Direction.NORTH;

        boolean isPositive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;

        // 核心旋转逻辑：DF5 原代码中 Forward 和 Backward 的 Y 轴偏置逻辑
        // 通过 XOR 运算统一判定是否需要旋转 180 度
        boolean shouldFlip = (isPositive ^ backward ^ !inContraption);
        float yaw = shouldFlip ? 180 : 0;
        float angleMultiplier = shouldFlip ? -1 : 1;

        ms.pushPose();
        ms.mulPose(Axis.YP.rotationDegrees(yaw));

        // 渲染架体
        ms.pushPose();
        ms.translate(0, FRAME_TRANS_Y, 0);
        CachedBuffers.partial(DF5_FRAME, air).light(light).overlay(overlay).renderInto(ms, buffer);
        ms.popPose();

        // 渲染轮对 (Co-Co 三轴)
        // 定义三根轴的 Z 轴偏移量
        double[] zOffsets = {0, -WHEEL_TRANS_Z, WHEEL_TRANS_Z};
        for (double z : zOffsets) {
            ms.pushPose();
            ms.translate(0, WHEEL_TRANS_Y, z);
            ms.mulPose(Axis.XP.rotationDegrees(wheelAngle * angleMultiplier));
            CachedBuffers.partial(DF5_WHEEL, air).light(light).overlay(overlay).renderInto(ms, buffer);
            ms.popPose();
        }

        ms.popPose();
    }


    public static class Backward extends DF5Renderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            // 调用父类逻辑，传入 backward = true
            super.renderDF5(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, true);
        }

    }

}