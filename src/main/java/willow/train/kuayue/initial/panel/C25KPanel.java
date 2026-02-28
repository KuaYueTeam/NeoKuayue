package willow.train.kuayue.initial.panel;

import net.createmod.catnip.data.Couple;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.block.panels.TrainHingePanelBlock;
import willow.train.kuayue.block.panels.TrainPanelBlock;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.block.panels.door.CustomRenderedDoorBlock;
import willow.train.kuayue.block.panels.end_face.CustomRenderedEndfaceBlock;
import willow.train.kuayue.block.panels.slab.TrainLadderBlock;
import willow.train.kuayue.block.panels.slab.TrainSlabBlock;
import willow.train.kuayue.block.panels.window.TrainOpenableWindowBlock;
import willow.train.kuayue.block.panels.window.TrainSmallWindowBlock;
import willow.train.kuayue.block.panels.window.TrainUnOpenableSmallWindowBlock;
import willow.train.kuayue.block.panels.window.TrainUnOpenableWindowBlock;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.registration.PanelRegistration;
import willow.train.kuayue.initial.registration.SlabRegistration;

import static willow.train.kuayue.initial.panel.CR200JPanel.registry;

public class C25KPanel {

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_25K =
            new PanelRegistration<CustomRenderedDoorBlock>("door_25k")
                    .block(p -> new CustomRenderedDoorBlock(p,
                            Couple.create(
                                    new ResourceLocation(Kuayue.MODID, "door/original_25k_door_bottom_hinge"),
                                    new ResourceLocation(Kuayue.MODID, "door/original_25k_door_top_hinge")
                            ), Couple.create(
                            new ResourceLocation(Kuayue.MODID, "door/original_25k_door_bottom"),
                            new ResourceLocation(Kuayue.MODID, "door/original_25k_door_top")
                    ), new Vec3(0, 0, 0), new Vec3(0, 0, -.124), RenderShape.ENTITYBLOCK_ANIMATED, false))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_SLIDING_25K =
            new PanelRegistration<CustomRenderedDoorBlock>("door_sliding_25k")
                    .block(properties ->
                            new CustomRenderedDoorBlock(properties, Couple.create(
                                    registry.asResource("door/sliding_door_25k_bottom"),
                                    registry.asResource("door/sliding_door_25k_upper"))
                                    , Couple.create(
                                    registry.asResource("door/sliding_door_25k_bottom_lh"),
                                    registry.asResource("door/sliding_door_25k_upper_lh")),
                                    RenderShape.ENTITYBLOCK_ANIMATED, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_25K_1 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_25k_1")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.ROTATE_SINGLE_SIDED,
                                    "carriage/carriage25k/end_face/end_face_door_original_25k",
                                    null,
                                    "carriage/carriage25k/end_face/end_face_original_25k_1"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_25K_2 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_25k_2")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.ROTATE_SINGLE_SIDED,
                                    "carriage/carriage25k/end_face/end_face_door_original_25k",
                                    null,
                                    "carriage/carriage25k/end_face/end_face_original_25k_2"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_25K_3 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_25k_3")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.ROTATE_SINGLE_SIDED,
                                    "carriage/carriage25k/end_face/end_face_door_original_25k",
                                    null,
                                    "carriage/carriage25k/end_face/end_face_original_25k_3"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_SLIDING_25K_1 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_sliding_25k_1")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.SLIDE_2,
                                    "carriage/carriage25k/end_face/end_face_sliding_door_original_25k_right",
                                    "carriage/carriage25k/end_face/end_face_sliding_door_original_25k_left",
                                    "carriage/carriage25k/end_face/end_face_sliding_original_25k_1"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_SLIDING_25K_2 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_sliding_25k_2")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.SLIDE_2,
                                    "carriage/carriage25k/end_face/end_face_sliding_door_original_25k_right",
                                    "carriage/carriage25k/end_face/end_face_sliding_door_original_25k_left",
                                    "carriage/carriage25k/end_face/end_face_sliding_original_25k_2"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_SLIDING_25K_3 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_sliding_25k_3")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.SLIDE_2,
                                    "carriage/carriage25k/end_face/end_face_sliding_door_original_25k_right",
                                    "carriage/carriage25k/end_face/end_face_sliding_door_original_25k_left",
                                    "carriage/carriage25k/end_face/end_face_sliding_original_25k_3"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> PANEL_BOTTOM_25K =
            new PanelRegistration<TrainPanelBlock>("panel_bottom_25k")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainPanelBlock> PANEL_BOTTOM_LINE_25K =
            new PanelRegistration<TrainPanelBlock>("panel_bottom_line_25k")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainPanelBlock> PANEL_MIDDLE_25K =
            new PanelRegistration<TrainPanelBlock>("panel_middle_25k")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainHingePanelBlock> PANEL_SYMBOL_25K =
            new PanelRegistration<TrainHingePanelBlock>("panel_symbol_25k")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainHingePanelBlock> PANEL_SYMBOL_MARSHALLED_25K =
            new PanelRegistration<TrainHingePanelBlock>("panel_symbol_marshalled_25k")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> PANEL_SYMBOL_MARSHALLED_25K_B =
            new PanelRegistration<TrainHingePanelBlock>("panel_symbol_marshalled_25k_b")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainPanelBlock> PANEL_TOP_25K =
            new PanelRegistration<TrainPanelBlock>("panel_top_25k")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainUnOpenableSmallWindowBlock> WINDOW_OC_SEALED_SMALL_25K =
            new PanelRegistration<TrainUnOpenableSmallWindowBlock>("window_oc_sealed_small_25k")
                    .block(TrainUnOpenableSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_OC_SMALL_25K =
            new PanelRegistration<TrainSmallWindowBlock>("window_oc_small_25k")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_OC_TOILET_25K =
            new PanelRegistration<TrainSmallWindowBlock>("window_oc_toilet_25k")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainOpenableWindowBlock> WINDOW_OC_WIDE_25K =
            new PanelRegistration<TrainOpenableWindowBlock>("window_oc_wide_25k")
                    .block(p -> new TrainOpenableWindowBlock(p, 2))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainUnOpenableWindowBlock> WINDOW_OC_WIDE_SEALED_25K =
            new PanelRegistration<TrainUnOpenableWindowBlock>("window_oc_wide_sealed_25k")
                    .block(p -> new TrainUnOpenableWindowBlock(p, 2))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> FLOOR_25K =
            new SlabRegistration<TrainSlabBlock>("floor_25k")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> TOILET_DD_25K =
            new SlabRegistration<TrainSlabBlock>("toilet_dd_25k")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainLadderBlock> LADDER_SLIDING_25K =
            new SlabRegistration<TrainLadderBlock>("ladder_sliding_25k")
                    .block(TrainLadderBlock::new)
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static void invoke(){}
}
