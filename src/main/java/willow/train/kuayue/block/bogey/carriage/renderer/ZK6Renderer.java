package willow.train.kuayue.block.bogey.carriage.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.mojang.math.Axis;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.create.AllCarriageBogeys;

public class ZK6Renderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    // 漏洞修复 1: 改用 PartialModel.of
    public static final PartialModel
            ZK6_FRAME = PartialModel.of(asBlockModelResource("bogey/zk6/zk6_frame")),
            ZK6_WHEEL = PartialModel.of(asBlockModelResource("bogey/zk6/zk6_wheel"));

    // 漏洞修复 2: 修正参数列表签名
    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                       MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {

        var buffer = bufferSource.getBuffer(RenderType.cutoutMipped());
        var air = Blocks.AIR.defaultBlockState();

        ms.pushPose();

        // --- 渲染 ZK6 框架 ---
        ms.pushPose();
        ms.translate(0, 0.380, 0); // 保持你原有的 ZK6 框架高度
        CachedBuffers.partial(ZK6_FRAME, air)
                .light(light)
                .overlay(overlay)
                .renderInto(ms, buffer);
        ms.popPose();

        // --- 渲染轮对 ---
        // 使用 Iterate.positiveAndNegative 简化 wheels[0]/wheels[1] 的写法
        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            // ZK6 轴距参数: 0.917, 轮心高度: 0.745
            ms.translate(0, 0.745, (double) side * 0.917d);
            // 旋转系数: 1.09
            ms.mulPose(Axis.XP.rotationDegrees(wheelAngle * 1.09f));

            CachedBuffers.partial(ZK6_WHEEL, air)
                    .light(light)
                    .overlay(overlay)
                    .renderInto(ms, buffer);
            ms.popPose();
        }

        ms.popPose();
    }


    // 子类 Andesite 同样需要同步参数列表
    public static class Andesite extends ZK6Renderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            ms.pushPose();
            ms.scale(1.2F, 1, 1);
            super.render(bogeyData, wheelAngle, partialTick, ms, bufferSource, light, overlay, inContraption);
            ms.popPose();
        }
    }

}