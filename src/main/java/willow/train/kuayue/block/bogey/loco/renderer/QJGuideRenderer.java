package willow.train.kuayue.block.bogey.loco.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import kasuga.lib.core.create.BogeyDataConstants;
import net.createmod.catnip.nbt.NBTHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.create.AllLocoBogeys;

public class QJGuideRenderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }
    public static final PartialModel
            QJ_GUIDE_FRAME = PartialModel.of(asBlockModelResource("bogey/qj/qj_guide_frame")),
            QJ_GUIDE_WHEEL = PartialModel.of(asBlockModelResource("bogey/qj/qj_guide_wheel"));

    @Override
    public void render(
            CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {

        // 1. 获取基础数据与渲染设置
        Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                : Direction.NORTH;

        RenderType type = RenderType.cutoutMipped();
        BlockState air = Blocks.AIR.defaultBlockState();

        int overlay = OverlayTexture.NO_OVERLAY;

        poseStack.pushPose();

        // 2. 处理整体朝向
        // 如果组装方向是正向（SOUTH/EAST），需要旋转 180 度来对齐模型
        if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180));
        }

        // 3. 渲染导向轮框架 (QJ_GUIDE_FRAME)
        CachedBuffers.partial(QJ_GUIDE_FRAME, air)
                .light(packedLight)
                .overlay(overlay)
                .renderInto(poseStack, bufferSource.getBuffer(type));

        // 4. 渲染轮对 (QJ_GUIDE_WHEEL)
        // 进轮通常只有一根轴，不需要 Iterate.positiveAndNegative
        poseStack.pushPose();

        poseStack.translate(0, 0.77, 0);
        // 轮子滚动：绕 X 轴旋转
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(wheelAngle));

        CachedBuffers.partial(QJ_GUIDE_WHEEL, air)
                .light(packedLight)
                .overlay(overlay)
                .renderInto(poseStack, bufferSource.getBuffer(type));

        poseStack.popPose();
        poseStack.popPose();
    }


    public static class Andesite extends QJGuideRenderer {
        @Override
        public void render(
                CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {
            poseStack.pushPose();
            poseStack.scale(1.2F, 1, 1);
            super.render(bogeyData, wheelAngle, partialTick, poseStack, bufferSource, packedLight,packedOverlay,inContraption);
            poseStack.popPose();
        }

    }
}
