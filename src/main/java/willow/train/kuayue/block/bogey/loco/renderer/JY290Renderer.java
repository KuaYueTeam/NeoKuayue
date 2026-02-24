package willow.train.kuayue.block.bogey.loco.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.entity.CarriageBogey;
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
import willow.train.kuayue.initial.create.AllLocoBogeys;

public class JY290Renderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    // 漏洞修复 1: 使用 PartialModel.of
    public static final PartialModel
            JY290_FRAME = PartialModel.of(asBlockModelResource("bogey/jy290/jy290_frame")),
            JY290_WHEEL = PartialModel.of(asBlockModelResource("bogey/jy290/jy290_wheel"));

    private static final double FRAME_TRANS_Y = -0.07;
    private static final double WHEEL_TRANS_Y = 0.775;
    private static final double WHEEL_TRANS_Z = 1.195;

    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                       MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
        renderJY290(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, false);
    }

    // 统一渲染核心逻辑
    protected void renderJY290(CompoundTag bogeyData, float wheelAngle, PoseStack ms,
                               MultiBufferSource bufferSource, int light, int overlay,
                               boolean inContraption, boolean backward) {

        var buffer = bufferSource.getBuffer(RenderType.cutoutMipped());
        var air = Blocks.AIR.defaultBlockState();

        Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                : Direction.NORTH;

        boolean isPositive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;

        // 核心翻转逻辑：处理南北朝向、正反向挂载、组装状态
        boolean shouldFlip = (isPositive ^ backward ^ !inContraption);
        float yaw = shouldFlip ? 180 : 0;
        float angleMultiplier = shouldFlip ? -1 : 1;

        ms.pushPose();
        ms.mulPose(Axis.YP.rotationDegrees(yaw));

        // --- 1. 渲染架体 ---
        ms.pushPose();
        ms.translate(0, FRAME_TRANS_Y, 0);
        CachedBuffers.partial(JY290_FRAME, air).light(light).overlay(overlay).renderInto(ms, buffer);
        ms.popPose();

        // --- 2. 渲染轮对 (双轴) ---
        // 注意：原代码中轮对相对于 yaw 翻转有特定的平移偏置，这里通过 Iterate.positiveAndNegative 统一
        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            // 保持轴距 1.195d
            ms.translate(0, WHEEL_TRANS_Y, (double) side * WHEEL_TRANS_Z);
            ms.mulPose(Axis.XP.rotationDegrees(wheelAngle * angleMultiplier));
            CachedBuffers.partial(JY290_WHEEL, air).light(light).overlay(overlay).renderInto(ms, buffer);
            ms.popPose();
        }

        ms.popPose();
    }


    // --- 子类：反向渲染器 ---
    public static class Backward extends JY290Renderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            super.renderJY290(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, true);
        }

    }

}