package willow.train.kuayue.initial.panel;

import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec2;
import willow.train.kuayue.block.panels.TrainPanelBlock;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.block.panels.conductor.FreightC70EndFaceBlock;
import willow.train.kuayue.block.panels.conductor.FreightNX70EndFaceBlock;
import willow.train.kuayue.block.panels.door.DoubleRotateDoorBlock;
import willow.train.kuayue.block.panels.end_face.FreightEndFaceBlock;
import willow.train.kuayue.block.panels.slab.HingeSlabBlock;
import willow.train.kuayue.block.panels.slab.TrainSlabBlock;
import willow.train.kuayue.block.panels.window.TrainOpenableWindowBlock;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.registration.PanelRegistration;
import willow.train.kuayue.initial.registration.SlabRegistration;

public class CFreightPanel {

    public static final PanelRegistration<FreightC70EndFaceBlock> FREIGHT_C70_END_FACE =
            new PanelRegistration<FreightC70EndFaceBlock>("freight_c70_end_face")
                    .block((properties) -> new FreightC70EndFaceBlock(properties,
                            FreightEndFaceBlock.FreightType.C70))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> FREIGHT_C70_SLAB_BOTTOM =
            new PanelRegistration<TrainPanelBlock>("freight_c70_slab_bottom")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(2, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainOpenableWindowBlock> FREIGHT_C70_SLAB_TOP =
            new PanelRegistration<TrainOpenableWindowBlock>("freight_c70_slab_top")
                    .block(p -> new TrainOpenableWindowBlock(p, -1, 1, 1))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<DoubleRotateDoorBlock> FREIGHT_C70_DOOR =
            new PanelRegistration<DoubleRotateDoorBlock>("freight_c70_door")
                    .block(properties ->
                            new DoubleRotateDoorBlock(
                                    properties, TrainPanelProperties.DoorType.ROTATE_SINGLE_SIDED,
                                    "carriage/freight/c70/freight_c70_door_left",
                                    "carriage/freight/c70/freight_c70_door_right",
                                    "carriage/freight/c70/freight_c70_door_frame"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> FREIGHT_NX70_FLOOR_CENTER =
            new SlabRegistration<TrainSlabBlock>("freight_nx70_floor_center")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> FREIGHT_NX70_FLOOR_HIGH =
            new SlabRegistration<TrainSlabBlock>("freight_nx70_floor_high")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> FREIGHT_NX70_FLOOR_COVER_PLATE =
            new SlabRegistration<TrainSlabBlock>("freight_nx70_floor_cover_plate")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> FREIGHT_NX70_FLOOR_CONNECTION =
            new SlabRegistration<HingeSlabBlock>("freight_nx70_floor_connection")
                    .block(p -> new HingeSlabBlock(p, false, 2))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> FREIGHT_NX70_FLOOR_LOW =
            new SlabRegistration<TrainSlabBlock>("freight_nx70_floor_low")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> FREIGHT_NX70_FLOOR_LOW_LADDER =
            new SlabRegistration<TrainSlabBlock>("freight_nx70_floor_low_ladder")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<FreightNX70EndFaceBlock> FREIGHT_NX70_END_FACE =
            new PanelRegistration<FreightNX70EndFaceBlock>("freight_nx70_end_face")
                    .block((properties) -> new FreightNX70EndFaceBlock(properties,
                            FreightEndFaceBlock.FreightType.NX70))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static void invoke(){}
}
