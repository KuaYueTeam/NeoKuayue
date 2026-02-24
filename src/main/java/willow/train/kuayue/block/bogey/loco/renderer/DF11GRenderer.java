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

public class DF11GRenderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    // 漏洞修复：PartialModel.of
    public static final PartialModel
            DF11G_FRAME = PartialModel.of(asBlockModelResource("bogey/df11g/df11g_bogey_temple")),
            DF11G_WHEEL = PartialModel.of(asBlockModelResource("bogey/df11g/df11g_wheel"));

    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                       MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
        renderDF11G(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, false, 1.0f);
    }

    // 核心渲染逻辑：统一处理缩放、朝向、正反向
    protected void renderDF11G(CompoundTag bogeyData, float wheelAngle, PoseStack ms,
                               MultiBufferSource bufferSource, int light, int overlay,
                               boolean inContraption, boolean backward, float scaleX) {

        var buffer = bufferSource.getBuffer(RenderType.cutoutMipped());
        var air = Blocks.AIR.defaultBlockState();

        Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                : Direction.NORTH;

        boolean isPositive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;

        // 180度翻转逻辑：DF11G原代码中方向判断逻辑的数学抽象
        boolean shouldFlip = (isPositive ^ backward ^ !inContraption);
        float yaw = shouldFlip ? 180 : 0;
        float angleMultiplier = shouldFlip ? -1 : 1;

        ms.pushPose();

        // 应用整体变换
        if (scaleX != 1.0f) ms.scale(scaleX, 1, 1);
        ms.mulPose(Axis.YP.rotationDegrees(yaw));

        // --- 渲染架体 ---
        ms.pushPose();
        ms.translate(0, 0.375, 0);
        CachedBuffers.partial(DF11G_FRAME, air).light(light).overlay(overlay).renderInto(ms, buffer);
        ms.popPose();

        // --- 渲染轮对 (Co-Co 三轴) ---
        for (int side = -1; side < 2; side++) {
            ms.pushPose();
            // DF11G 轴距 2.0m, 轮径偏置 0.88m
            ms.translate(0, 0.88, (double) side * 2.0d);
            ms.mulPose(Axis.XP.rotationDegrees(wheelAngle * angleMultiplier));
            CachedBuffers.partial(DF11G_WHEEL, air).light(light).overlay(overlay).renderInto(ms, buffer);
            ms.popPose();
        }

        ms.popPose();
    }


    // --- 子类：反向渲染器 ---
    public static class Backward extends DF11GRenderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            super.renderDF11G(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, true, 1.0f);
        }

    }

    // --- 子类：Andesite 变体（1.2倍缩放） ---
    public static class Andesite extends DF11GRenderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            super.renderDF11G(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, false, 1.2f);
        }

        public static class Backward extends DF11GRenderer {
            @Override
            public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                               MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
                super.renderDF11G(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, true, 1.2f);
            }
        }
    }

}