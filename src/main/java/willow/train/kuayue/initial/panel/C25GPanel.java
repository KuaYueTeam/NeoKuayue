package willow.train.kuayue.initial.panel;

import net.createmod.catnip.data.Couple;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.block.panels.*;
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

public class C25GPanel {

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_25G =
            new PanelRegistration<CustomRenderedDoorBlock>("door_25g")
                    .block(p -> new CustomRenderedDoorBlock(p,
                            Couple.create(
                                    new ResourceLocation(Kuayue.MODID, "door/original_25g_door_bottom_hinge"),
                                    new ResourceLocation(Kuayue.MODID, "door/original_25g_door_top_hinge")
                            ), Couple.create(
                                    new ResourceLocation(Kuayue.MODID, "door/original_25g_door_bottom"),
                                    new ResourceLocation(Kuayue.MODID, "door/original_25g_door_top")
                            ), new Vec3(0, 0, 0), new Vec3(0, 0, -.124), RenderShape.ENTITYBLOCK_ANIMATED, false))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_SLIDING_25G =
            new PanelRegistration<CustomRenderedDoorBlock>("door_sliding_25g")
                    .block(properties ->
                            new CustomRenderedDoorBlock(properties, Couple.create(
                                    registry.asResource("door/sliding_door_25g_bottom"),
                                    registry.asResource("door/sliding_door_25g_upper"))
                                    , Couple.create(
                                    registry.asResource("door/sliding_door_25g_bottom_lh"),
                                    registry.asResource("door/sliding_door_25g_upper_lh")),
                                    RenderShape.ENTITYBLOCK_ANIMATED, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_25G_1 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_25g_1")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.ROTATE_SINGLE_SIDED,
                                    "carriage/carriage25g/end_face/end_face_door_original_25g",
                                    null,
                                    "carriage/carriage25g/end_face/end_face_original_25g_1"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_25G_2 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_25g_2")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.ROTATE_SINGLE_SIDED,
                                    "carriage/carriage25g/end_face/end_face_door_original_25g",
                                    null,
                                    "carriage/carriage25g/end_face/end_face_original_25g_2"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_25G_3 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_25g_3")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.ROTATE_SINGLE_SIDED,
                                    "carriage/carriage25g/end_face/end_face_door_original_25g",
                                    null,
                                    "carriage/carriage25g/end_face/end_face_original_25g_3"
                            )
                    ).materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> PANEL_BOTTOM_25G =
            new PanelRegistration<TrainPanelBlock>("panel_bottom_25g")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> PANEL_MIDDLE_25G =
            new PanelRegistration<TrainPanelBlock>("panel_middle_25g")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> PANEL_SYMBOL_MARSHALLED_25G =
            new PanelRegistration<TrainHingePanelBlock>("panel_symbol_marshalled_25g")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> PANEL_TOP_25G =
            new PanelRegistration<TrainPanelBlock>("panel_top_25g")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainUnOpenableSmallWindowBlock> WINDOW_OC_SEALED_SMALL_25G =
            new PanelRegistration<TrainUnOpenableSmallWindowBlock>("window_oc_sealed_small_25g")
                    .block(TrainUnOpenableSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainUnOpenableWindowBlock> WINDOW_OC_SEALED_WIDE_25G =
            new PanelRegistration<TrainUnOpenableWindowBlock>("window_oc_sealed_wide_25g")
                    .block(p -> new TrainUnOpenableWindowBlock(p, 2))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_OC_SMALL_25G =
            new PanelRegistration<TrainSmallWindowBlock>("window_oc_small_25g")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_OC_SMALL_25G_2 =
            new PanelRegistration<TrainSmallWindowBlock>("window_oc_small_25g_2")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_OC_TOILET_25G =
            new PanelRegistration<TrainSmallWindowBlock>("window_oc_toilet_25g")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainOpenableWindowBlock> WINDOW_OC_WIDE_25G =
            new PanelRegistration<TrainOpenableWindowBlock>("window_oc_wide_25g")
                    .block(p -> new TrainOpenableWindowBlock(p, 2))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> FLOOR_25G =
            new SlabRegistration<TrainSlabBlock>("floor_25g")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> TOILET_DD_25G =
            new SlabRegistration<TrainSlabBlock>("toilet_dd_25g")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainLadderBlock> LADDER_25G =
            new SlabRegistration<TrainLadderBlock>("ladder_25g")
                    .block(TrainLadderBlock::new)
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion()
                    .strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static void invoke(){}
}
