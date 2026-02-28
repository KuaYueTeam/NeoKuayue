package willow.train.kuayue.block.bogey.loco.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.entity.CarriageBogey;
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

public class HXD3DRenderer implements BogeyRenderer {

    public static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    public static final PartialModel
            HXD3D_FRAME = PartialModel.of(asBlockModelResource("bogey/hxd3d/hxd3d_frame")),
            HXD3D_WHEEL = PartialModel.of(asBlockModelResource("bogey/hxd3d/hxd3d_wheel"));

    // HXD3D 非对称轴距基准值
    private static final double[] WHEEL_OFFSETS = {0.23, 2.365, -1.69};

    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                       MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
        renderHXD3D(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, false, 1.0f);
    }

    protected void renderHXD3D(CompoundTag bogeyData, float wheelAngle, PoseStack ms,
                               MultiBufferSource bufferSource, int light, int overlay,
                               boolean inContraption, boolean backward, float scaleX) {

        var buffer = bufferSource.getBuffer(RenderType.cutoutMipped());
        var air = Blocks.AIR.defaultBlockState();

        Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                : Direction.NORTH;

        boolean isPositive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;

        // 核心翻转逻辑：控制架体和轮对位移的整体方向
        boolean shouldFlip = (isPositive ^ backward ^ !inContraption);
        float yaw = shouldFlip ? 180 : 0;

        // 车轮旋转方向系数
        float angleMultiplier = (backward ^ shouldFlip) ? -1 : 1;

        ms.pushPose();

        // 处理 Andesite 变体缩放
        if (scaleX != 1.0f) ms.scale(scaleX, 1.0f, 1.0f);

        // 整体转向架偏转
        ms.mulPose(Axis.YP.rotationDegrees(yaw));

        // --- 1. 渲染架体 ---
        ms.pushPose();
        ms.translate(0, 0.012, 0);
        CachedBuffers.partial(HXD3D_FRAME, air).light(light).overlay(overlay).renderInto(ms, buffer);
        ms.popPose();

        // --- 2. 渲染三对动轮 ---
        for (double zOffset : WHEEL_OFFSETS) {
            ms.pushPose();
            ms.translate(0, 0.905, zOffset);
            ms.mulPose(Axis.XP.rotationDegrees(wheelAngle * angleMultiplier));
            CachedBuffers.partial(HXD3D_WHEEL, air).light(light).overlay(overlay).renderInto(ms, buffer);
            ms.popPose();
        }

        ms.popPose();
    }


    // --- 子类变体：反向版本 ---
    public static class Backward extends HXD3DRenderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            super.renderHXD3D(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, true, 1.0f);
        }
    }

    // --- 子类变体：Andesite 版本 ---
    public static class Andesite extends HXD3DRenderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            super.renderHXD3D(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, false, 1.2f);
        }

        public static class Backward extends HXD3DRenderer {
            @Override
            public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                               MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
                super.renderHXD3D(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, true, 1.2f);
            }
        }
    }

}