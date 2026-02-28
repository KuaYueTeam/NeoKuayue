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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import willow.train.kuayue.initial.create.AllLocoBogeys;
import willow.train.kuayue.initial.AllElements;

public class DFH21Renderer implements BogeyRenderer {

    private static ResourceLocation asBlockModelResource(String path) {
        return AllElements.testRegistry.asResource("block/" + path);
    }

    protected static PartialModel DFH21_MAIN;
    protected static PartialModel DFH21_WHEEL;
    protected static PartialModel DFH21_TRANSMISSION_ROD;

    protected static PartialModel DFH21_MAIN_STANDARD;
    protected static PartialModel DFH21_WHEEL_STANDARD;
    protected static PartialModel DFH21_TRANSMISSION_ROD_STANDARD;

    public static final double dp = 2.4; // 轴距，可根据实际情况调整
    public static final double ad = 1.2; // 轮对位移，可根据实际情况调整

    static {
        DFH21_MAIN = PartialModel.of(asBlockModelResource("bogey/dfh21/dfh21_main"));
        DFH21_WHEEL = PartialModel.of(asBlockModelResource("bogey/dfh21/dfh21_wheel"));
        DFH21_TRANSMISSION_ROD = PartialModel.of(asBlockModelResource("bogey/dfh21/dfh21_transmission_rod"));

        DFH21_MAIN_STANDARD = PartialModel.of(asBlockModelResource("bogey/dfh21/dfh21_main_standard"));
        DFH21_WHEEL_STANDARD = PartialModel.of(asBlockModelResource("bogey/dfh21/dfh21_wheel_standard"));
        DFH21_TRANSMISSION_ROD_STANDARD = PartialModel.of(asBlockModelResource("bogey/dfh21/dfh21_transmission_rod_standard"));
    }


    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {

        // 1. 基础方向与环境准备
        Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                : Direction.NORTH;

        RenderType type = RenderType.cutoutMipped();
        BlockState air = Blocks.AIR.defaultBlockState();
        int overlay = OverlayTexture.NO_OVERLAY;

        // 2. 预计算方向修正
        // 0.6 版转向架：正向 (SOUTH/EAST) 为 0°，负向 (NORTH/WEST) 为 180°
        boolean isPositive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;
        float yaw = isPositive ? 0 : 180;
        float angleMultiplier = isPositive ? 1 : -1;

        poseStack.pushPose();

        // 整体转向旋转
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yaw));

        // --- 渲染：DFH21 主框架 ---
        CachedBuffers.partial(DFH21_MAIN, air)
                .light(packedLight)
                .overlay(overlay)
                .renderInto(poseStack, bufferSource.getBuffer(type));

        // --- 渲染：两对轮轴 ---
        for (int side : new int[]{-1, 1}) { // side 为 -1(前) 和 1(后)
            poseStack.pushPose();

            // 计算轮轴位置：dp 为轴距的一半，ad 为整体平移量
            double zOffset = (double) side * dp + ad;
            poseStack.translate(0, 0.9, zOffset);

            // 轮子滚动
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(wheelAngle * angleMultiplier));

            CachedBuffers.partial(DFH21_WHEEL, air)
                    .light(packedLight)
                    .overlay(overlay)
                    .renderInto(poseStack, bufferSource.getBuffer(type));

            poseStack.popPose();
        }

        // --- 渲染：传动连杆 (Transmission Rod) ---
        poseStack.pushPose();

        // 假设传动轴位于 y=0.85
        poseStack.translate(0, 0.85, 0);
        // 连杆通常绕 Z 轴旋转（如果是液力箱输出）
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(wheelAngle * 3.256f * angleMultiplier));

        CachedBuffers.partial(DFH21_TRANSMISSION_ROD, air)
                .light(packedLight)
                .overlay(overlay)
                .renderInto(poseStack, bufferSource.getBuffer(type));

        poseStack.popPose();

        poseStack.popPose(); // 结束整体变换
    }

    public static class Backward extends DFH21Renderer {


        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {

            // 1. 获取基础数据 (参考 CW2 风格)
            RenderType type = RenderType.cutoutMipped();
            BlockState air = Blocks.AIR.defaultBlockState();
            int overlay = OverlayTexture.NO_OVERLAY;

            // 2. 核心朝向逻辑
            // 判定组装方向：SOUTH/EAST 为正向，NORTH/WEST 为负向
            Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                    ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                    : Direction.NORTH;

            boolean isPositive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;

            // CW2 风格：统一由一个旋转矩阵决定朝向
            // 如果是正向，不旋转；如果是负向，旋转 180 度。这样就不需要写两套 translate 逻辑。
            float yaw = isPositive ? 0 : 180;

            // 旋转 180 度后，相对坐标系的 X 轴旋转方向在物理上会反向，所以需要乘这个系数
            float angleMultiplier = isPositive ? 1 : -1;

            poseStack.pushPose();

            // 应用转向架整体转向
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yaw));

            // --- 3. 渲染主体框架 (CW2 风格：CachedBuffers) ---
            CachedBuffers.partial(DFH21_MAIN, air)
                    .light(packedLight)
                    .overlay(overlay)
                    .renderInto(poseStack, bufferSource.getBuffer(type));

            // --- 4. 渲染轮对 ---
            for (int side : new int[]{-1, 1}) { // 遍历前轴(-1)和后轴(1)
                poseStack.pushPose();

                // 计算局部偏移：dp 为半轴距，ad 为中心偏移
                double zOffset = (double) side * dp + ad;
                poseStack.translate(0, 0.9, zOffset);

                // 轮子自转：绕 X 轴
                poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(wheelAngle * angleMultiplier));

                CachedBuffers.partial(DFH21_WHEEL, air)
                        .light(packedLight)
                        .overlay(overlay)
                        .renderInto(poseStack, bufferSource.getBuffer(type));

                poseStack.popPose();
            }

            // --- 5. 渲染传动杆 (Transmission Rod) ---
            poseStack.pushPose();

            // 设定传动轴中心位置
            poseStack.translate(0, 0.85, 0);
            // 连杆旋转：如果是绕铁轨方向转，用 ZP；如果是横向转，用 XP
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(wheelAngle * 3.256f * angleMultiplier));

            CachedBuffers.partial(DFH21_TRANSMISSION_ROD, air)
                    .light(packedLight)
                    .overlay(overlay)
                    .renderInto(poseStack, bufferSource.getBuffer(type));

            poseStack.popPose();

            poseStack.popPose(); // 结束整体变换
        }
    }

    public static class Standard extends DFH21Renderer {

        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {

            RenderType type = RenderType.cutoutMipped();
            BlockState air = Blocks.AIR.defaultBlockState();
            int overlay = OverlayTexture.NO_OVERLAY;

            // 核心朝向计算
            Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                    ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                    : Direction.NORTH;

            // 这里的 logic 决定了转向架是否需要旋转 180 度来对齐模型
            // 现代写法不再区分 inContraption 的分支，而是统一由方向驱动
            boolean isPositive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;
            float yaw = isPositive ? 180 : 0;
            float angleMultiplier = isPositive ? -1 : 1; // 旋转方向随朝向自动翻转

            poseStack.pushPose();

            // 3. 统一应用整体变换
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yaw));

            // --- 渲染主体框架 (Main) ---
            CachedBuffers.partial(DFH21_MAIN_STANDARD, air)
                    .light(packedLight)
                    .overlay(overlay)
                    .renderInto(poseStack, bufferSource.getBuffer(type));

            // --- 渲染轮对 (Wheels) ---
            for (int side : new int[]{-1, 1}) {
                poseStack.pushPose();

                // 使用你的变量 dp (轴距一半) 和 ad (偏移)
                double zOffset = (double) side * dp + ad;
                poseStack.translate(0, 0.875, zOffset);

                // 轮子旋转：绕 X 轴
                poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(wheelAngle * angleMultiplier));

                CachedBuffers.partial(DFH21_WHEEL_STANDARD, air)
                        .light(packedLight)
                        .overlay(overlay)
                        .renderInto(poseStack, bufferSource.getBuffer(type));

                poseStack.popPose();
            }

            // --- 渲染传动杆 (Transmission Rod) ---
            poseStack.pushPose();

            poseStack.translate(0, 0.85, 0);
            // 传动杆随轮旋转，系数为 3.256
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(wheelAngle * 3.256f * angleMultiplier));

            CachedBuffers.partial(DFH21_TRANSMISSION_ROD_STANDARD, air)
                    .light(packedLight)
                    .overlay(overlay)
                    .renderInto(poseStack, bufferSource.getBuffer(type));

            poseStack.popPose();

            poseStack.popPose();
        }

        public static class Backward extends DFH21Renderer.Backward {


            @Override
            public void render(CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {

                // 1. 准备渲染环境 (CW2 标准渲染器)
                RenderType type = RenderType.cutoutMipped();
                BlockState air = Blocks.AIR.defaultBlockState();
                int overlay = OverlayTexture.NO_OVERLAY;

                // 2. 核心朝向逻辑 (CW2 风格：统一由一个旋转矩阵决定)
                Direction direction = bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                        ? NBTHelper.readEnum(bogeyData, BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                        : Direction.NORTH;

                // 确定是否需要旋转 180 度。如果是在 Contraption 上，通常需要根据方向翻转。
                // 我们将朝向映射为一个简单的 yaw 变量和一个旋转乘数
                boolean isPositive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;

                // 注意：这里是修复你代码逻辑的关键点。通过 yaw 统一旋转，下面的 translate 就只需要写一遍。
                float yaw = (isPositive ^ inContraption) ? 180 : 0;
                float angleMultiplier = (isPositive ^ inContraption) ? -1 : 1;

                poseStack.pushPose();

                // 3. 应用整体变换：一劳永逸处理所有 main, wheels, rod 的朝向
                poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yaw));

                // --- 渲染主体框架 (Main) ---
                CachedBuffers.partial(DFH21_MAIN_STANDARD, air)
                        .light(packedLight)
                        .overlay(overlay)
                        .renderInto(poseStack, bufferSource.getBuffer(type));

                // --- 渲染轮对 (Wheels) ---
                for (int side : new int[]{-1, 1}) {
                    poseStack.pushPose();

                    // 统一的平移逻辑
                    double zOffset = (double) side * dp + ad;
                    poseStack.translate(0, 0.875, zOffset);

                    // 轮子旋转：乘上 multiplier 确保滚动方向正确
                    poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(wheelAngle * angleMultiplier));

                    CachedBuffers.partial(DFH21_WHEEL_STANDARD, air)
                            .light(packedLight)
                            .overlay(overlay)
                            .renderInto(poseStack, bufferSource.getBuffer(type));

                    poseStack.popPose();
                }

                // --- 渲染传动杆 (Transmission Rod) ---
                poseStack.pushPose();

                poseStack.translate(0, 0.85, 0);
                // 传动杆同步旋转
                poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(wheelAngle * 3.256f * angleMultiplier));

                CachedBuffers.partial(DFH21_TRANSMISSION_ROD_STANDARD, air)
                        .light(packedLight)
                        .overlay(overlay)
                        .renderInto(poseStack, bufferSource.getBuffer(type));

                poseStack.popPose();

                poseStack.popPose();
            }
        }

        public static class Andesite extends DFH21Renderer.Standard {
            @Override
            public void render(
                    CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {
                poseStack.pushPose();
                poseStack.scale(1.2F, 1, 1);
                super.render(bogeyData, wheelAngle, partialTick, poseStack, bufferSource, packedLight, packedOverlay, inContraption);
                poseStack.popPose();
            }

            public static class Backward extends DFH21Renderer.Standard.Backward {
                @Override
                public void render(
                        CompoundTag bogeyData, float wheelAngle, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inContraption) {
                    poseStack.pushPose();
                    poseStack.scale(1.2F, 1, 1);
                    super.render(bogeyData, wheelAngle, partialTick, poseStack, bufferSource, packedLight, packedOverlay, inContraption);
                    poseStack.popPose();
                }
            }
        }
    }
}
