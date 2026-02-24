package willow.train.kuayue.block.bogey.carriage.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
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
import net.minecraft.world.level.block.state.BlockState;
import willow.train.kuayue.initial.create.AllCarriageBogeys;
import willow.train.kuayue.initial.AllElements;

public class PK209PRenderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    public static final PartialModel PK209P_MAIN = PartialModel.of(asBlockModelResource("bogey/pk209p/pk209p_main"));
    public static final PartialModel PK209P_WHEEL = PartialModel.of(asBlockModelResource("bogey/pk209p/pk209p_wheel"));
    public static final PartialModel PK209P_WHEEL2 = PartialModel.of(asBlockModelResource("bogey/pk209p/pk209p_wheel2"));
    public static final PartialModel PK209P_MOTORWHEEL = PartialModel.of(asBlockModelResource("bogey/pk209p/pk209_motorwheel"));
    public static final PartialModel PK209P_NO_MOTOR = PartialModel.of(asBlockModelResource("bogey/pk209p/pk209_nomotor"));





    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                       MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {

        // 1. 获取方向 (保持原逻辑)
        Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                : Direction.NORTH;

        // 2. 准备渲染器
        // 在接口版中，vb 不再是参数，而是通过 bufferSource 获取
        VertexConsumer vb = bufferSource.getBuffer(RenderType.cutoutMipped());
        BlockState air = Blocks.AIR.defaultBlockState();

        // 3. 处理方向逻辑
        boolean shouldFlip = (direction == Direction.SOUTH || direction == Direction.EAST);

        ms.pushPose();

        if (shouldFlip) {
            if (inContraption) {
                // 情况 A: 翻转方向但在结构体中
                renderPart(ms, vb, air, PK209P_MAIN, 0, 0.91, 0, 0, 0,light);
                renderWheel(ms, vb, air, PK209P_WHEEL, 0, 0.8, 1.2, wheelAngle, 0,light);
                renderWheel(ms, vb, air, PK209P_WHEEL2, 0, 0.8, -1.2, wheelAngle, 0,light);
                renderWheel(ms, vb, air, PK209P_MOTORWHEEL, 1.117, 0.82, 2.165, wheelAngle * 3.256f, 0,light);
            } else {
                // 情况 B: 翻转方向且不在结构体中 (需要 Y 轴旋转 180)
                renderPart(ms, vb, air, PK209P_MAIN, 0, 0.91, 0, 0, 180,light);
                renderWheel(ms, vb, air, PK209P_WHEEL, 0, 0.8, -1.2, -wheelAngle, 180,light);
                renderWheel(ms, vb, air, PK209P_WHEEL2, 0, 0.8, 1.2, -wheelAngle, 180,light);
                renderWheel(ms, vb, air, PK209P_MOTORWHEEL, -1.117, 0.82, -2.165, -wheelAngle * 3.256f, 180,light);
            }
        } else {
            // 情况 C: 默认方向
            renderPart(ms, vb, air, PK209P_MAIN, 0, 0.91, 0, 0, 0,light);
            renderWheel(ms, vb, air, PK209P_WHEEL, 0, 0.8, 1.2, wheelAngle, 0,light);
            renderWheel(ms, vb, air, PK209P_WHEEL2, 0, 0.8, -1.2, wheelAngle, 0,light);
            renderWheel(ms, vb, air, PK209P_MOTORWHEEL, 1.117, 0.82, 2.165, wheelAngle * 3.256f, 0,light);
        }

        ms.popPose();
    }
    /**
     * 替代旧版 BogeyModelData 的渲染工具方法
     */
    private static void renderComponent(PoseStack ms, VertexConsumer vb, PartialModel model,
                                        double x, double y, double z,
                                        float rotationX, float rotationY,
                                        int light, int overlay) {
        ms.pushPose();
        ms.translate(x, y, z);
        if (rotationY != 0) ms.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotationY));
        if (rotationX != 0) ms.mulPose(com.mojang.math.Axis.XP.rotationDegrees(rotationX));

        CachedBuffers.partial(model, Blocks.AIR.defaultBlockState())
                .light(light)
                .overlay(overlay)
                .renderInto(ms, vb);
        ms.popPose();
    }

    /**
     * 辅助方法：渲染静态部件
     */
    private void renderPart(PoseStack ms, VertexConsumer vb, BlockState state, PartialModel model,
                            double x, double y, double z, float rotateX, float rotateY,int light) {
        ms.pushPose();
        ms.translate(x, y, z);
        if (rotateY != 0) ms.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotateY));
        if (rotateX != 0) ms.mulPose(com.mojang.math.Axis.XP.rotationDegrees(rotateX));

        CachedBuffers.partial(model, state)
                .light(light) // 注意：这里的 light 需要从 render 参数传入
                .renderInto(ms, vb);
        ms.popPose();
    }

    /**
     * 辅助方法：渲染轮对（带旋转）
     */
    private void renderWheel(PoseStack ms, VertexConsumer vb, BlockState state, PartialModel model,
                             double x, double y, double z, float wheelAngle, float rotateY,int light) {
        ms.pushPose();
        ms.translate(x, y, z);
        if (rotateY != 0) ms.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotateY));
        ms.mulPose(com.mojang.math.Axis.XP.rotationDegrees(wheelAngle));

        CachedBuffers.partial(model, state)
                .light(light)
                .renderInto(ms, vb);
        ms.popPose();
    }

    public static class Backward implements BogeyRenderer {

        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {

            Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                    ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                    : Direction.NORTH;

            VertexConsumer vb = bufferSource.getBuffer(RenderType.cutoutMipped());
            BlockState air = Blocks.AIR.defaultBlockState();

            boolean shouldFlip = (direction == Direction.SOUTH || direction == Direction.EAST);

            // 逻辑判断：如果 flip 且 inContraption，或者是默认方向（else 分支逻辑其实一样）
            // 这里简化了你原始代码中的 if-else 嵌套逻辑
            boolean isReversedMode = shouldFlip ? !inContraption : true;

            if (isReversedMode) {
                // 反方向渲染逻辑 (main rotateY 180)
                renderComponent(ms, vb, PK209P_MAIN, 0, 0.91, 0, 0, 180, light, overlay);
                renderComponent(ms, vb, PK209P_WHEEL, 0, 0.8, -1.2, -wheelAngle, 180, light, overlay);
                renderComponent(ms, vb, PK209P_WHEEL2, 0, 0.8, 1.2, -wheelAngle, 180, light, overlay);
                renderComponent(ms, vb, PK209P_MOTORWHEEL, -1.117, 0.82, -2.165, -wheelAngle * 3.256f, 180, light, overlay);
            } else {
                // 正方向渲染逻辑
                renderComponent(ms, vb, PK209P_MAIN, 0, 0.91, 0, 0, 0, light, overlay);
                renderComponent(ms, vb, PK209P_WHEEL, 0, 0.8, 1.2, wheelAngle, 0, light, overlay);
                renderComponent(ms, vb, PK209P_WHEEL2, 0, 0.8, -1.2, wheelAngle, 0, light, overlay);
                renderComponent(ms, vb, PK209P_MOTORWHEEL, 1.117, 0.82, 2.165, wheelAngle * 3.256f, 0, light, overlay);
            }
        }
    }

    public static class NoMotor implements BogeyRenderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {

            VertexConsumer vb = bufferSource.getBuffer(RenderType.cutoutMipped());

            // 渲染主体
            renderComponent(ms, vb, PK209P_NO_MOTOR, 0, 0.91, 0, 0, 0, light, overlay);

            // 渲染双轮对
            for (int side : Iterate.positiveAndNegative) {
                ms.pushPose();
                ms.translate(0, 0.8, (double) side * 1.2d);
                ms.mulPose(com.mojang.math.Axis.XP.rotationDegrees(wheelAngle));

                CachedBuffers.partial(PK209P_WHEEL2, Blocks.AIR.defaultBlockState())
                        .light(light)
                        .overlay(overlay)
                        .renderInto(ms, vb);
                ms.popPose();
            }
        }
    }
    public static class Andesite extends PK209PRenderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack ms,
                           MultiBufferSource bufferSource, int light, int overlay, boolean inContraption) {
            ms.pushPose();
            ms.scale(1.2F, 1.0F, 1.0F);
            // 这里调用你基类的渲染实现
            super.render(bogeyData, wheelAngle, partialTick, ms, bufferSource, light, overlay, inContraption);
            ms.popPose();
        }
    }
}