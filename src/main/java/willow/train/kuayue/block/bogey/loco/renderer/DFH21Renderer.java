package willow.train.kuayue.block.bogey.loco.renderer;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.NBTHelper;
import kasuga.lib.core.create.BogeyDataConstants;
import kasuga.lib.example_env.AllExampleBogey;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import willow.train.kuayue.initial.create.AllLocoBogeys;
import willow.train.kuayue.initial.AllElements;

public class DFH21Renderer extends BogeyRenderer {

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
        DFH21_MAIN = new PartialModel(asBlockModelResource("bogey/dfh21/dfh21_main"));
        DFH21_WHEEL = new PartialModel(asBlockModelResource("bogey/dfh21/dfh21_wheel"));
        DFH21_TRANSMISSION_ROD = new PartialModel(asBlockModelResource("bogey/dfh21/dfh21_transmission_rod"));

        DFH21_MAIN_STANDARD = new PartialModel(asBlockModelResource("bogey/dfh21/dfh21_main_standard"));
        DFH21_WHEEL_STANDARD = new PartialModel(asBlockModelResource("bogey/dfh21/dfh21_wheel_standard"));
        DFH21_TRANSMISSION_ROD_STANDARD = new PartialModel(asBlockModelResource("bogey/dfh21/dfh21_transmission_rod_standard"));
    }

    @Override
    public void initialiseContraptionModelData(
            MaterialManager materialManager, CarriageBogey carriageBogey) {
        this.createModelInstance(materialManager, DFH21_MAIN);
        this.createModelInstance(materialManager, DFH21_WHEEL, 2);
        this.createModelInstance(materialManager, DFH21_TRANSMISSION_ROD);
    }

    @Override
    public BogeySizes.BogeySize getSize() {
        return AllLocoBogeys.dfh21.getSize();
    }

    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, PoseStack ms,
                       int light, VertexConsumer vb, boolean inContraption) {

        Direction direction =
                bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                        ? NBTHelper.readEnum(
                        bogeyData,
                        BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY,
                        Direction.class)
                        : Direction.NORTH;

        boolean inInstancedContraption = vb == null;

        BogeyModelData main = getTransform(DFH21_MAIN, ms, inInstancedContraption);
        BogeyModelData[] wheels = getTransform(DFH21_WHEEL, ms, inInstancedContraption, 2);
        BogeyModelData transmissionRod = getTransform(DFH21_TRANSMISSION_ROD, ms, inInstancedContraption);

        if (direction == Direction.SOUTH || direction == Direction.EAST) {
            if (inContraption) {
                main.translate(0, 0, 0).render(ms, light, vb);

                for (int side = -1; side < 1; side++) {
                    if (!inInstancedContraption) ms.pushPose();
                    BogeyModelData wheel = wheels[side + 1];
                    wheel.translate(0, 0.9, ((double) side) * dp + ad)
                            .rotateX(wheelAngle)
                            .render(ms, light, vb);
                    if (!inInstancedContraption) ms.popPose();
                }

                transmissionRod
                        .translate(0,0.85, 0)
                        .rotateX(wheelAngle * 3.256)
                        .render(ms, light, vb);
            } else {
                main.rotateY(180).translate(0, 0, 0).render(ms, light, vb);

                for (int side = -1; side < 1; side++) {
                    if (!inInstancedContraption) ms.pushPose();
                    BogeyModelData wheel = wheels[side + 1];
                    wheel.translate(0, 0.9, ((double) side) * dp + ad)
                            .rotateY(180)
                            .rotateX(-wheelAngle)
                            .render(ms, light, vb);
                    if (!inInstancedContraption) ms.popPose();
                }

                transmissionRod
                        .translate(0,0.85, 0)
                        .rotateY(180)
                        .rotateZ(-wheelAngle * 3.256)
                        .render(ms, light, vb);
            }
        } else {
            main.rotateY(180).translate(0, 0, 0).render(ms, light, vb);

            for (int side = -1; side < 1; side++) {
                if (!inInstancedContraption) ms.pushPose();
                BogeyModelData wheel = wheels[side + 1];
                wheel.translate(0, 0.9, ((double) side) * dp + ad)
                        .rotateX(wheelAngle)
                        .render(ms, light, vb);
                if (!inInstancedContraption) ms.popPose();
            }

            transmissionRod
                    .translate(0,0.85, 0)
                    .rotateZ(wheelAngle * 3.256)
                    .render(ms, light, vb);
        }
    }

    public static class Backward extends DFH21Renderer {
        @Override
        public void initialiseContraptionModelData(
                MaterialManager materialManager, CarriageBogey carriageBogey) {
            this.createModelInstance(materialManager, DFH21_MAIN);
            this.createModelInstance(materialManager, DFH21_WHEEL, 2);
            this.createModelInstance(materialManager, DFH21_TRANSMISSION_ROD);
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return AllLocoBogeys.dfh21Backward.getSize();
        }

        @Override
        public void render(
                CompoundTag bogeyData,
                float wheelAngle,
                PoseStack ms,
                int light,
                VertexConsumer vb,
                boolean inContraption) {
            light *= 1.1; // 调整光照

            boolean forwards = BogeyDataConstants.isForwards(bogeyData, inContraption);

            Direction direction =
                    bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                            ? NBTHelper.readEnum(
                            bogeyData,
                            BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY,
                            Direction.class)
                            : Direction.NORTH;

            boolean inInstancedContraption = vb == null;
            // 转向架架体
            BogeyModelData main = getTransform(DFH21_MAIN, ms, inInstancedContraption);
            // 普通轮对
            BogeyModelData[] wheels = getTransform(DFH21_WHEEL, ms, inInstancedContraption, 2);
            // 传动杆
            BogeyModelData transmissionRod = getTransform(DFH21_TRANSMISSION_ROD, ms, inInstancedContraption);

            // 反方向渲染
            if (direction == Direction.SOUTH || direction == Direction.EAST) {
                if (inContraption) {
                    main.rotateY(180).translate(0, 0, 0).render(ms, light, vb);

                    for (int side = -1; side < 1; side++) {
                        if (!inInstancedContraption) ms.pushPose();
                        BogeyModelData wheel = wheels[side + 1];
                        wheel.translate(0, 0.9, ((double) side) * dp + ad)
                                .rotateY(180)
                                .rotateX(-wheelAngle)
                                .render(ms, light, vb);
                        if (!inInstancedContraption) ms.popPose();
                    }

                    transmissionRod
                            .translate(0,0.85, 0)
                            .rotateY(180)
                            .rotateZ(-wheelAngle * 3.256)
                            .render(ms, light, vb);
                } else {
                    main.translate(0, 0, 0).render(ms, light, vb);

                    for (int side = -1; side < 1; side++) {
                        if (!inInstancedContraption) ms.pushPose();
                        BogeyModelData wheel = wheels[side + 1];
                        wheel.translate(0, 0.9, ((double) side) * dp + ad)
                                .rotateX(wheelAngle)
                                .render(ms, light, vb);
                        if (!inInstancedContraption) ms.popPose();
                    }

                    transmissionRod
                            .translate(0,0.85, 0)
                            .rotateZ(wheelAngle * 3.256)
                            .render(ms, light, vb);
                }
            } else {
                main.translate(0, 0, 0).render(ms, light, vb);

                for (int side = -1; side < 1; side++) {
                    if (!inInstancedContraption) ms.pushPose();
                    BogeyModelData wheel = wheels[side + 1];
                    wheel.translate(0, 0.9, ((double) side) * dp + ad)
                            .rotateY(180)
                            .rotateX(-wheelAngle)
                            .render(ms, light, vb);
                    if (!inInstancedContraption) ms.popPose();
                }

                transmissionRod
                        .translate(0,0.85, 0)
                        .rotateY(180)
                        .rotateZ(-wheelAngle * 3.256)
                        .render(ms, light, vb);
            }
        }
    }

    public static class Standard extends DFH21Renderer {
        @Override
        public void initialiseContraptionModelData(
                MaterialManager materialManager, CarriageBogey carriageBogey) {
            this.createModelInstance(materialManager, DFH21_MAIN_STANDARD);
            this.createModelInstance(materialManager, DFH21_WHEEL_STANDARD, 2);
            this.createModelInstance(materialManager, DFH21_TRANSMISSION_ROD_STANDARD);
        }

        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            Direction direction =
                    bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                            ? NBTHelper.readEnum(
                            bogeyData,
                            BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY,
                            Direction.class)
                            : Direction.NORTH;

            boolean inInstancedContraption = vb == null;

            BogeyModelData main = getTransform(DFH21_MAIN_STANDARD, ms, inInstancedContraption);
            BogeyModelData[] wheels = getTransform(DFH21_WHEEL_STANDARD, ms, inInstancedContraption, 2);
            BogeyModelData transmissionRod = getTransform(DFH21_TRANSMISSION_ROD_STANDARD, ms, inInstancedContraption);

            if (direction == Direction.SOUTH || direction == Direction.EAST) {
                if (inContraption) {
                    main.translate(0, 0, 0).render(ms, light, vb);

                    for (int side = -1; side < 1; side++) {
                        if (!inInstancedContraption) ms.pushPose();
                        BogeyModelData wheel = wheels[side + 1];
                        wheel.translate(0, 0.875, ((double) side) * dp + ad)
                                .rotateX(wheelAngle)
                                .render(ms, light, vb);
                        if (!inInstancedContraption) ms.popPose();
                    }

                    transmissionRod
                            .translate(0,0.85, 0)
                            .rotateZ(wheelAngle * 3.256)
                            .render(ms, light, vb);
                } else {
                    main.rotateY(180).translate(0, 0, 0).render(ms, light, vb);

                    for (int side = -1; side < 1; side++) {
                        if (!inInstancedContraption) ms.pushPose();
                        BogeyModelData wheel = wheels[side + 1];
                        wheel.translate(0, 0.875, ((double) side) * dp + ad)
                                .rotateY(180)
                                .rotateX(-wheelAngle)
                                .render(ms, light, vb);
                        if (!inInstancedContraption) ms.popPose();
                    }

                    transmissionRod
                            .translate(0,0.85, 0)
                            .rotateY(180)
                            .rotateZ(-wheelAngle * 3.256)
                            .render(ms, light, vb);
                }
            } else {
                main.translate(0, 0, 0).render(ms, light, vb);

                for (int side = -1; side < 1; side++) {
                    if (!inInstancedContraption) ms.pushPose();
                    BogeyModelData wheel = wheels[side + 1];
                    wheel.translate(0, 0.875, ((double) side) * dp + ad)
                            .rotateX(wheelAngle)
                            .render(ms, light, vb);
                    if (!inInstancedContraption) ms.popPose();
                }

                transmissionRod
                        .translate(0,0.85, 0)
                        .rotateZ(wheelAngle * 3.256)
                        .render(ms, light, vb);
            }
        }

        public static class Backward extends DFH21Renderer.Backward {
            @Override
            public void initialiseContraptionModelData(
                    MaterialManager materialManager, CarriageBogey carriageBogey) {
                this.createModelInstance(materialManager, DFH21_MAIN_STANDARD);
                this.createModelInstance(materialManager, DFH21_WHEEL_STANDARD, 2);
                this.createModelInstance(materialManager, DFH21_TRANSMISSION_ROD_STANDARD);
            }

            @Override
            public void render(CompoundTag bogeyData, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
                light *= 1.1; // 调整光照

                boolean forwards = BogeyDataConstants.isForwards(bogeyData, inContraption);

                Direction direction =
                        bogeyData.contains(BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                                ? NBTHelper.readEnum(
                                bogeyData,
                                BogeyDataConstants.BOGEY_ASSEMBLY_DIRECTION_KEY,
                                Direction.class)
                                : Direction.NORTH;

                boolean inInstancedContraption = vb == null;
                // 转向架架体
                BogeyModelData main = getTransform(DFH21_MAIN_STANDARD, ms, inInstancedContraption);
                // 普通轮对
                BogeyModelData[] wheels = getTransform(DFH21_WHEEL_STANDARD, ms, inInstancedContraption, 2);
                // 传动杆
                BogeyModelData transmissionRod = getTransform(DFH21_TRANSMISSION_ROD_STANDARD, ms, inInstancedContraption);

                // 反方向渲染
                if (direction == Direction.SOUTH || direction == Direction.EAST) {
                    if (inContraption) {
                        main.rotateY(180).translate(0, 0, 0).render(ms, light, vb);

                        for (int side = -1; side < 1; side++) {
                            if (!inInstancedContraption) ms.pushPose();
                            BogeyModelData wheel = wheels[side + 1];
                            wheel.translate(0, 0.875, ((double) side) * dp + ad)
                                    .rotateY(180)
                                    .rotateX(-wheelAngle)
                                    .render(ms, light, vb);
                            if (!inInstancedContraption) ms.popPose();
                        }

                        transmissionRod
                                .translate(0,0.85, 0)
                                .rotateY(180)
                                .rotateZ(-wheelAngle * 3.256)
                                .render(ms, light, vb);
                    } else {
                        main.translate(0, 0, 0).render(ms, light, vb);

                        for (int side = -1; side < 1; side++) {
                            if (!inInstancedContraption) ms.pushPose();
                            BogeyModelData wheel = wheels[side + 1];
                            wheel.translate(0, 0.875, ((double) side) * dp + ad)
                                    .rotateX(wheelAngle)
                                    .render(ms, light, vb);
                            if (!inInstancedContraption) ms.popPose();
                        }

                        transmissionRod
                                .translate(0,0.85, 0)
                                .rotateZ(wheelAngle * 3.256)
                                .render(ms, light, vb);
                    }
                } else {
                    main.rotateY(0).translate(0, 0, 0).render(ms, light, vb);

                    for (int side = -1; side < 1; side++) {
                        if (!inInstancedContraption) ms.pushPose();
                        BogeyModelData wheel = wheels[side + 1];
                        wheel.translate(0, 0.875, ((double) side) * dp + ad)
                                .rotateY(180)
                                .rotateX(-wheelAngle)
                                .render(ms, light, vb);
                        if (!inInstancedContraption) ms.popPose();
                    }

                    transmissionRod
                            .translate(0,0.85, 0)
                            .rotateY(180)
                            .rotateZ(-wheelAngle * 3.256)
                            .render(ms, light, vb);
                }
            }
        }
    }

    public static class Andesite extends DFH21Renderer.Standard {
        @Override
        public void render(
                CompoundTag bogeyData,
                float wheelAngle,
                PoseStack ms,
                int light,
                VertexConsumer vb,
                boolean inContraption) {
            ms.pushPose();
            ms.scale(1.2F, 1, 1);
            super.render(bogeyData, wheelAngle, ms, light, vb, inContraption);
            ms.popPose();
        }

        public static class Backward extends DFH21Renderer.Standard.Backward {
            @Override
            public void render(
                    CompoundTag bogeyData,
                    float wheelAngle,
                    PoseStack ms,
                    int light,
                    VertexConsumer vb,
                    boolean inContraption) {
                ms.pushPose();
                ms.scale(1.2F, 1, 1);
                super.render(bogeyData, wheelAngle, ms, light, vb, inContraption);
                ms.popPose();
            }
        }
    }
}