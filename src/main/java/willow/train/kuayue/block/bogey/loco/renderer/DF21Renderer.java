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

public class DF21Renderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    // 漏洞修复 1: 使用 PartialModel.of 处理异步加载
    public static final PartialModel
            DF21_FRAME = PartialModel.of(asBlockModelResource("bogey/df21/df21_bogey_temple")),
            DF21_WHEEL = PartialModel.of(asBlockModelResource("bogey/df21/df21_wheel"));

    // 漏洞修复 2: 使用 1.20.1 版本的正确参数列表
    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                       MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
        renderDF21(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, false);
    }

    // 统一渲染核心，合并 Forward 和 Backward
    protected void renderDF21(CompoundTag bogeyData, float wheelAngle, PoseStack ms,
                              MultiBufferSource bufferSource, int light, int overlay,
                              boolean inContraption, boolean backward) {

        var buffer = bufferSource.getBuffer(RenderType.cutoutMipped());
        var air = Blocks.AIR.defaultBlockState();

        Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                : Direction.NORTH;

        boolean isPositive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;

        // 核心异或逻辑：处理组装状态、正反向和南北朝向
        // DF21 原始逻辑中 !inContraption 会导致 frame 翻转 180，这里予以保留
        boolean shouldFlip = (isPositive ^ backward ^ !inContraption);
        float yaw = shouldFlip ? 180 : 0;
        float angleMultiplier = shouldFlip ? -1 : 1;

        ms.pushPose();
        ms.mulPose(Axis.YP.rotationDegrees(yaw));

        // --- 1. 渲染架体 ---
        // 注意：DF21 框架有独特的 Z 轴偏移 0.17
        ms.pushPose();
        ms.translate(0, 0.225, 0.17);
        CachedBuffers.partial(DF21_FRAME, air).light(light).overlay(overlay).renderInto(ms, buffer);
        ms.popPose();

        // --- 2. 渲染轮对 (Co-Co 三轴) ---
        for (int side = -1; side < 2; side++) {
            ms.pushPose();
            // DF21 轴距 1.805m, 轮径高度 0.88m
            ms.translate(0, 0.88, (double) side * 1.805d);
            ms.mulPose(Axis.XP.rotationDegrees(wheelAngle * angleMultiplier));
            CachedBuffers.partial(DF21_WHEEL, air).light(light).overlay(overlay).renderInto(ms, buffer);
            ms.popPose();
        }

        ms.popPose();
    }

    // --- 子类：反向渲染器 ---
    public static class Backward extends DF21Renderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            super.renderDF21(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, true);
        }

    }

}