package willow.train.kuayue.block.bogey.carriage.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.math.Axis;
import willow.train.kuayue.initial.AllElements;

public class TKZ1Renderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    public static final PartialModel
            TKZ1_FRAME = PartialModel.of(asBlockModelResource("bogey/tkz1/tkz1_frame")),
            TKZ1_WHEEL = PartialModel.of(asBlockModelResource("bogey/tkz2/tkz2_wheel"));

    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {

        // 1. 现代渲染环境初始化
        RenderType type = RenderType.cutoutMipped();
        BlockState air = Blocks.AIR.defaultBlockState();
        int overlay = OverlayTexture.NO_OVERLAY;

        int adjustedLight = (int) (packedLight * 1.1f);

        poseStack.pushPose();

        // --- 2. 渲染框架 (Frame) ---
        poseStack.pushPose();
        poseStack.translate(0, 0.14, 0); // 原代码偏移

        CachedBuffers.partial(TKZ1_FRAME, air)
                .light(adjustedLight)
                .overlay(overlay)
                .renderInto(poseStack, bufferSource.getBuffer(type));
        poseStack.popPose();

        // --- 3. 渲染轮对 (Wheels) ---
        for (int side : Iterate.positiveAndNegative) {
            poseStack.pushPose();

            // 原代码偏移量：y=0.695, z=±0.97
            poseStack.translate(0, 0.695, (double) side * 0.97d);

            // 轮子旋转：原代码系数为 1.22
            poseStack.mulPose(Axis.XP.rotationDegrees(wheelAngle * 1.22f));

            CachedBuffers.partial(TKZ1_WHEEL, air)
                    .light(adjustedLight)
                    .overlay(overlay)
                    .renderInto(poseStack, bufferSource.getBuffer(type));

            poseStack.popPose();
        }

        poseStack.popPose();
    }

}