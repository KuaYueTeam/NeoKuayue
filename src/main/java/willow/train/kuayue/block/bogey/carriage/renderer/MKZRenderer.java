package willow.train.kuayue.block.bogey.carriage.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import willow.train.kuayue.initial.AllElements;

public class MKZRenderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    public static final PartialModel
            MKZ_FRAME = PartialModel.of(asBlockModelResource("bogey/mkz/mkz_frame")),
            MKZ_WHEEL = PartialModel.of(asBlockModelResource("bogey/mkz/mkz_wheel"));





    @Override
    public void render(
            CompoundTag bogeyData,
            float wheelAngle,
            float partialTick,
            PoseStack ms,
            MultiBufferSource bufferSource,
            int light,
            int overlay,
            boolean inContraption) {

        // 在新版接口中，我们需要手动获取 RenderType
        // 通常转向架使用剪切或实色渲染
        RenderType type = RenderType.cutoutMipped();
        BlockState air = Blocks.AIR.defaultBlockState();

        // 1. 渲染框架
        CachedBuffers.partial(MKZ_FRAME, air)
                .light(light)
                .overlay(overlay)
                .renderInto(ms, bufferSource.getBuffer(type));

        // 2. 渲染轮对
        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();

            // 应用变换逻辑
            ms.translate(0, 0.805, (double) side * 1.18d);
            // 绕 X 轴旋转（注意：Create 0.6 推荐使用 Vector3f 或 Axis.XP）
            ms.mulPose(com.mojang.math.Axis.XP.rotationDegrees(wheelAngle));

            CachedBuffers.partial(MKZ_WHEEL, air)
                    .light(light)
                    .overlay(overlay)
                    .renderInto(ms, bufferSource.getBuffer(type));

            ms.popPose();
        }
    }
}
