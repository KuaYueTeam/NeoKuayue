package willow.train.kuayue.initial.panel;

import net.createmod.catnip.data.Couple;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec3;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.block.panels.SkirtBlock;
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
import willow.train.kuayue.initial.registration.SkirtRegistration;
import willow.train.kuayue.initial.registration.SlabRegistration;

public class C25TPanel {

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_25T =
            new PanelRegistration<CustomRenderedDoorBlock>("door_25t")
                    .block(p -> new CustomRenderedDoorBlock(p,
                            Couple.create(
                                    new ResourceLocation(Kuayue.MODID, "door/original_25t_door_bottom_hinge"),
                                    new ResourceLocation(Kuayue.MODID, "door/original_25t_door_top_hinge")
                            ), Couple.create(
                                    new ResourceLocation(Kuayue.MODID, "door/original_25t_door_bottom"),
                                    new ResourceLocation(Kuayue.MODID, "door/original_25t_door_top")
                    ), new Vec3(0, 0, 0), new Vec3(0, 0, -.124), RenderShape.ENTITYBLOCK_ANIMATED, false))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_SLIDING_25T =
            new PanelRegistration<CustomRenderedDoorBlock>("door_sliding_25t")
                    .block(p -> new CustomRenderedDoorBlock(p,
                            Couple.create(
                                    new ResourceLocation(Kuayue.MODID, "door/sliding_door_25t_bottom"),
                                    new ResourceLocation(Kuayue.MODID, "door/sliding_door_25t_upper")
                            ), Couple.create(
                                    new ResourceLocation(Kuayue.MODID, "door/sliding_door_25t_bottom_lh"),
                                    new ResourceLocation(Kuayue.MODID, "door/sliding_door_25t_upper_lh")
                    ), RenderShape.ENTITYBLOCK_ANIMATED, true))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_25T_1 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_25t_1")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.ROTATE_SINGLE_SIDED,
                                    "carriage/carriage25t/end_face/end_face_door_original_25t",
                                    null,
                                    "carriage/carriage25t/end_face/end_face_original_rubber_25t_1"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_25T_2 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_25t_2")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.ROTATE_SINGLE_SIDED,
                                    "carriage/carriage25t/end_face/end_face_door_original_25t",
                                    null,
                                    "carriage/carriage25t/end_face/end_face_original_rubber_25t_2"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_25T_3 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_25t_3")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.ROTATE_SINGLE_SIDED,
                                    "carriage/carriage25t/end_face/end_face_door_original_25t",
                                    null,
                                    "carriage/carriage25t/end_face/end_face_original_rubber_25t_3"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_SLIDING_25T_1 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_sliding_25t_1")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.SLIDE_2,
                                    "carriage/carriage25t/end_face/end_face_door_sliding_original_25t_right",
                                    "carriage/carriage25t/end_face/end_face_door_sliding_original_25t_left",
                                    "carriage/carriage25t/end_face/end_face_original_25t_1"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_SLIDING_25T_2 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_sliding_25t_2")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.SLIDE_2,
                                    "carriage/carriage25t/end_face/end_face_door_sliding_original_25t_right",
                                    "carriage/carriage25t/end_face/end_face_door_sliding_original_25t_left",
                                    "carriage/carriage25t/end_face/end_face_original_25t_2"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_SLIDING_25T_3 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_sliding_25t_3")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.SLIDE_2,
                                    "carriage/carriage25t/end_face/end_face_door_sliding_original_25t_right",
                                    "carriage/carriage25t/end_face/end_face_door_sliding_original_25t_left",
                                    "carriage/carriage25t/end_face/end_face_original_25t_3"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> PANEL_BOTTOM_25T =
            new PanelRegistration<TrainPanelBlock>("panel_bottom_25t")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainHingePanelBlock> PANEL_BOTTOM_25T_2 =
            new PanelRegistration<TrainHingePanelBlock>("panel_bottom_25t_2")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainPanelBlock> PANEL_MIDDLE_25T =
            new PanelRegistration<TrainPanelBlock>("panel_middle_25t")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainHingePanelBlock> PANEL_MIDDLE_25T_2 =
            new PanelRegistration<TrainHingePanelBlock>("panel_middle_25t_2")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainPanelBlock> PANEL_TOP_25T =
            new PanelRegistration<TrainPanelBlock>("panel_top_25t")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainHingePanelBlock> PANEL_TOP_25T_2 =
            new PanelRegistration<TrainHingePanelBlock>("panel_top_25t_2")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainPanelBlock> PANEL_TOP_XL25T =
            new PanelRegistration<TrainPanelBlock>("panel_top_xl25t")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_OC_BLUE_25T =
            new PanelRegistration<TrainSmallWindowBlock>("window_oc_blue_25t")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainUnOpenableSmallWindowBlock> WINDOW_OC_SEALED_BLUE_25T =
            new PanelRegistration<TrainUnOpenableSmallWindowBlock>("window_oc_sealed_blue_25t")
                    .block(TrainUnOpenableSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainUnOpenableSmallWindowBlock> WINDOW_OC_SEALED_SMALL_25T =
            new PanelRegistration<TrainUnOpenableSmallWindowBlock>("window_oc_sealed_small_25t")
                    .block(TrainUnOpenableSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainUnOpenableSmallWindowBlock> WINDOW_OC_SEALED_SMALL_BLUE_25T =
            new PanelRegistration<TrainUnOpenableSmallWindowBlock>("window_oc_sealed_small_blue_25t")
                    .block(TrainUnOpenableSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainUnOpenableSmallWindowBlock> WINDOW_OC_SEALED_SMALL_BLUE_BSP25T =
            new PanelRegistration<TrainUnOpenableSmallWindowBlock>("window_oc_sealed_small_blue_bsp25t")
                    .block(TrainUnOpenableSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainUnOpenableWindowBlock> WINDOW_OC_SEALED_WIDE_BLUE_BSP25T =
            new PanelRegistration<TrainUnOpenableWindowBlock>("window_oc_sealed_wide_blue_bsp25t")
                    .block(p -> new TrainUnOpenableWindowBlock(p, 2))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_OC_SMALL_25T =
            new PanelRegistration<TrainSmallWindowBlock>("window_oc_small_25t")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_OC_SMALL_BLUE_25T =
            new PanelRegistration<TrainSmallWindowBlock>("window_oc_small_blue_25t")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_OC_SMALL_BLUE_BSP25T =
            new PanelRegistration<TrainSmallWindowBlock>("window_oc_small_blue_bsp25t")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_OC_TOILET_25T =
            new PanelRegistration<TrainSmallWindowBlock>("window_oc_toilet_25t")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_OC_TOILET_BLUE_25T =
            new PanelRegistration<TrainSmallWindowBlock>("window_oc_toilet_blue_25t")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_OC_TOILET_BLUE_BSP25T =
            new PanelRegistration<TrainSmallWindowBlock>("window_oc_toilet_blue_bsp25t")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainOpenableWindowBlock> WINDOW_OC_WIDE_25T =
            new PanelRegistration<TrainOpenableWindowBlock>("window_oc_wide_25t")
                    .block(p -> new TrainOpenableWindowBlock(p, 2))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainOpenableWindowBlock> WINDOW_OC_WIDE_BLUE_25T =
            new PanelRegistration<TrainOpenableWindowBlock>("window_oc_wide_blue_25t")
                    .block(p -> new TrainOpenableWindowBlock(p, 2))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainOpenableWindowBlock> WINDOW_OC_WIDE_BLUE_BSP25T =
            new PanelRegistration<TrainOpenableWindowBlock>("window_oc_wide_blue_bsp25t")
                    .block(p -> new TrainOpenableWindowBlock(p, 2))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainUnOpenableWindowBlock> WINDOW_OC_WIDE_SEALED_25T =
            new PanelRegistration<TrainUnOpenableWindowBlock>("window_oc_wide_sealed_25t")
                    .block(p -> new TrainUnOpenableWindowBlock(p, 2))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainUnOpenableWindowBlock> WINDOW_OC_WIDE_SEALED_BLUE_25T =
            new PanelRegistration<TrainUnOpenableWindowBlock>("window_oc_wide_sealed_blue_25t")
                    .block(p -> new TrainUnOpenableWindowBlock(p, 2))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SkirtRegistration<SkirtBlock> SKIRT_25T =
            new SkirtRegistration<SkirtBlock>("skirt_25t")
                    .block(SkirtBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SkirtRegistration<SkirtBlock> SKIRT_MARSHALLED_25T =
            new SkirtRegistration<SkirtBlock>("skirt_marshalled_25t")
                    .block(SkirtBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainLadderBlock> LADDER_SLIDING_25T =
            new SlabRegistration<TrainLadderBlock>("ladder_sliding_25t")
                    .block(TrainLadderBlock::new)
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final SlabRegistration<TrainSlabBlock> FLOOR_25T =
            new SlabRegistration<TrainSlabBlock>("floor_25t")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> TOILET_DD_25T =
            new SlabRegistration<TrainSlabBlock>("toilet_dd_25t")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> CARPORT_25T =
            new SlabRegistration<TrainSlabBlock>("carport_25t")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final SlabRegistration<TrainSlabBlock> CARPORT_CENTER_NSP25T =
            new SlabRegistration<TrainSlabBlock>("carport_center_bsp25t")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final SlabRegistration<TrainSlabBlock> CARPORT_TOILET_25T =
            new SlabRegistration<TrainSlabBlock>("carport_toilet_25t")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> AIR_CONDITION_BSP25T =
            new SlabRegistration<TrainSlabBlock>("air_condition_bsp25t")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static void invoke(){}
}
