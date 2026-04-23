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

public class SS3Renderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    public static final PartialModel
            SS3_FRAME = PartialModel.of(asBlockModelResource("bogey/ss3/ss3_frame")),
            SS3_WHEEL = PartialModel.of(asBlockModelResource("bogey/ss3/ss3_wheel"));

    // SS3 非对称轴距偏移
    private static final double[] OFFSETS = {-2.11, 1.96, -0.01};

    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                       MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
        renderSS3(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, false, 1.0f);
    }

    // 核心渲染逻辑：整合所有变体
    protected void renderSS3(CompoundTag bogeyData, float wheelAngle, PoseStack ms,
                             MultiBufferSource bufferSource, int light, int overlay,
                             boolean inContraption, boolean backward, float scaleX) {

        var buffer = bufferSource.getBuffer(RenderType.cutoutMipped());
        var air = Blocks.AIR.defaultBlockState();

        Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                : Direction.NORTH;

        boolean isPositive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;

        // 核心异或逻辑判定翻转
        // SS3 原代码逻辑中方向和组装状态的判定决定了 frame 是否 rotateY(180)
        boolean shouldFlip = (isPositive ^ backward ^ !inContraption);
        float yaw = shouldFlip ? 180 : 0;
        float angleMultiplier = (backward ^ shouldFlip) ? -1 : 1;

        ms.pushPose();

        // 处理 Andesite 变体的拉伸
        if (scaleX != 1.0f) ms.scale(scaleX, 1.0f, 1.0f);

        // 整体转向架偏转
        ms.mulPose(Axis.YP.rotationDegrees(yaw));

        // --- 1. 渲染架体 ---
        ms.pushPose();
        ms.translate(0, 0.012, 0);
        CachedBuffers.partial(SS3_FRAME, air).light(light).overlay(overlay).renderInto(ms, buffer);
        ms.popPose();

        // --- 2. 渲染三个轮对 ---
        // 注意：SS3 是非对称的，通过 OFFSETS 数组定义位置
        for (int i = 0; i < 3; i++) {
            ms.pushPose();
            double zOffset = OFFSETS[i];
            ms.translate(0, 0.88, zOffset);
            ms.mulPose(Axis.XP.rotationDegrees(wheelAngle * angleMultiplier));
            CachedBuffers.partial(SS3_WHEEL, air).light(light).overlay(overlay).renderInto(ms, buffer);
            ms.popPose();
        }

        ms.popPose();
    }


    // --- 子类变体：反向渲染器 ---
    public static class Backward extends SS3Renderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            super.renderSS3(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, true, 1.0f);
        }
    }

    // --- 子类变体：Andesite 渲染器 (支持拉伸) ---
    public static class Andesite extends SS3Renderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            super.renderSS3(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, false, 1.2f);
        }

        public static class Backward extends SS3Renderer {
            @Override
            public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                               MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
                super.renderSS3(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, true, 1.2f);
            }
        }
    }
}