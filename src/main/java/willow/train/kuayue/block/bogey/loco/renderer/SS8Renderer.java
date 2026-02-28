package willow.train.kuayue.block.bogey.loco.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
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

public class SS8Renderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    public static final PartialModel
            SS8_FRAME = PartialModel.of(asBlockModelResource("bogey/ss8/ss8_frame")),
            SS8_WHEEL = PartialModel.of(asBlockModelResource("bogey/ss8/ss8_wheel"));

    // 轴距与位移常量
    public static final double DP = 2.604d; // 轴距
    public static final double AD = 1.304d; // 轮对中心位移

    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                       MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
        renderSS8(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, false, 1.0f);
    }

    // 通用渲染逻辑
    protected void renderSS8(CompoundTag bogeyData, float wheelAngle, PoseStack ms,
                             MultiBufferSource bufferSource, int light, int overlay,
                             boolean inContraption, boolean backward, float scaleX) {

        var buffer = bufferSource.getBuffer(RenderType.cutoutMipped());
        var air = Blocks.AIR.defaultBlockState();

        Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                : Direction.NORTH;

        boolean isPositive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;

        // 核心逻辑判定：决定是否需要 180 度翻转
        // 逻辑遵循：(正方向 ^ 是否反向变体 ^ 是否组装在车上)
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
        CachedBuffers.partial(SS8_FRAME, air)
                .light(light)
                .overlay(overlay)
                .renderInto(ms, buffer);

        // --- 2. 渲染两对动轮 ---
        for (int side = -1; side < 1; side++) {
            ms.pushPose();
            // 计算 Z 轴位移：side = -1 时位移为 -DP + AD，side = 0 时位移为 AD
            double zOffset = ((double) side) * DP + AD;

            ms.translate(0, 0.905, zOffset);
            ms.mulPose(Axis.XP.rotationDegrees(wheelAngle * angleMultiplier));

            CachedBuffers.partial(SS8_WHEEL, air)
                    .light(light)
                    .overlay(overlay)
                    .renderInto(ms, buffer);
            ms.popPose();
        }

        ms.popPose();
    }


    // --- 子类变体：反向版本 ---
    public static class Backward extends SS8Renderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            super.renderSS8(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, true, 1.0f);
        }

    }

    // --- 子类变体：Andesite 版本 ---
    public static class Andesite extends SS8Renderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            super.renderSS8(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, false, 1.2f);
        }

        public static class Backward extends SS8Renderer {
            @Override
            public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                               MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
                super.renderSS8(bogeyData, wheelAngle, ms, bufferSource, light, overlay, inContraption, true, 1.2f);
            }
        }
    }

}