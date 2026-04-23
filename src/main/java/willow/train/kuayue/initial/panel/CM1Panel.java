package willow.train.kuayue.initial.panel;

import net.createmod.catnip.data.Couple;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import willow.train.kuayue.block.panels.TrainPanelBlock;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.block.panels.door.CustomRenderedDoorBlock;
import willow.train.kuayue.block.panels.end_face.MeterCarriageEndFaceBlock;
import willow.train.kuayue.block.panels.slab.HingeSlabBlock;
import willow.train.kuayue.block.panels.slab.TrainLadderBlock;
import willow.train.kuayue.block.panels.slab.TrainSlabBlock;
import willow.train.kuayue.block.panels.window.TrainOpenableWindowBlock;
import willow.train.kuayue.block.panels.window.TrainSmallWindowBlock;
import willow.train.kuayue.block.panels.window.TrainUnOpenableWindowBlock;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.registration.PanelRegistration;
import willow.train.kuayue.initial.registration.SlabRegistration;

public class CM1Panel {

    public static final SlabRegistration<TrainSlabBlock> FLOOR_M1 =
            new SlabRegistration<TrainSlabBlock>("floor_m1")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> BOTTOM_SLAB_M1 =
            new PanelRegistration<TrainPanelBlock>("slab_bottom_m1")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainOpenableWindowBlock> WINDOW_LARGE_M1 =
            new PanelRegistration<TrainOpenableWindowBlock>("window_large_m1")
                    .block(p -> new TrainOpenableWindowBlock(p, -1, 1, 2))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_LARGE_BETWEEN_M1 =
            new PanelRegistration<TrainSmallWindowBlock>("window_large_between_m1")
                    .block(p -> new TrainSmallWindowBlock(p, 2))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainOpenableWindowBlock> WINDOW_SMALL_M1 =
            new PanelRegistration<TrainOpenableWindowBlock>("window_small_m1")
                    .block(p -> new TrainOpenableWindowBlock(p, -1, 1, 2))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainOpenableWindowBlock> WINDOW_DOUBLE_SMALL_M1 =
            new PanelRegistration<TrainOpenableWindowBlock>("window_double_small_m1")
                    .block(p -> new TrainOpenableWindowBlock(p, -1, 1, 2))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_M1 =
            new PanelRegistration<CustomRenderedDoorBlock>("door_m1")
                    .block(p -> new CustomRenderedDoorBlock(p,
                            Couple.create(
                                    AllElements.testRegistry.asResource("carriage/carriage_m1/door/m1_door_bottom_lh"),
                                    AllElements.testRegistry.asResource("carriage/carriage_m1/door/m1_door_upper_lh")
                            ), Couple.create(
                            AllElements.testRegistry.asResource("carriage/carriage_m1/door/m1_door_bottom"),
                            AllElements.testRegistry.asResource("carriage/carriage_m1/door/m1_door_upper")
                    ), new Vec3(0, 0, 0),new Vec3(0, 0, 0), RenderShape.MODEL, false
                    ))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainLadderBlock> LADDER_M1 =
            new SlabRegistration<TrainLadderBlock>("ladder_m1")
                    .block((properties) -> new TrainLadderBlock(properties, true))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<MeterCarriageEndFaceBlock> END_FACE_M1 =
            new PanelRegistration<MeterCarriageEndFaceBlock>("end_face_m1")
                    .block(properties ->
                            new MeterCarriageEndFaceBlock(
                                    properties, TrainPanelProperties.DoorType.NO_DOOR, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<MeterCarriageEndFaceBlock> END_FACE_MIDDLE_M1 =
            new PanelRegistration<MeterCarriageEndFaceBlock>("end_face_middle_m1")
                    .block(properties -> new MeterCarriageEndFaceBlock(
                            properties, TrainPanelProperties.DoorType.SLIDE,
                            "carriage/carriage_m1/end_face/m1_middle_end_face_door",
                            null,
                            "carriage/carriage_m1/end_face/m1_middle_end_face_frame", false))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> GENERAL_CARPORT_M1 =
            new SlabRegistration<TrainSlabBlock>("carport_general_m1")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> AIR_VENT_CARPORT_M1 =
            new SlabRegistration<TrainSlabBlock>("carport_air_vent_m1")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> AIR_VENT_CARPORT_M1_2 =
            new SlabRegistration<TrainSlabBlock>("carport_air_vent_m1_2")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> MIDDLE_CARPORT_M1 =
            new SlabRegistration<TrainSlabBlock>("carport_middle_m1")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainOpenableWindowBlock> WINDOW_LARGE_JY30 =
            new PanelRegistration<TrainOpenableWindowBlock>("window_large_jy30")
                    .block(p -> new TrainOpenableWindowBlock(p, -1, 1, 2))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_LARGE_BETWEEN_JY30 =
            new PanelRegistration<TrainSmallWindowBlock>("window_large_between_jy30")
                    .block(p -> new TrainSmallWindowBlock(p, 2))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> BOTTOM_SLAB_JY30 =
            new PanelRegistration<TrainPanelBlock>("slab_bottom_jy30")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> FLOOR_BATTERY_M1 =
            new SlabRegistration<HingeSlabBlock>("floor_battery_m1")
                    .block(p -> new HingeSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> FLOOR_RESERVOIR_BOX_M1 =
            new SlabRegistration<TrainSlabBlock>("floor_reservoir_box_m1")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainUnOpenableWindowBlock> SLAB_TOP_M1 =
            new PanelRegistration<TrainUnOpenableWindowBlock>("slab_top_m1")
                    .block(p -> new TrainUnOpenableWindowBlock(p, 2, 0, 2))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static void invoke(){}
}
