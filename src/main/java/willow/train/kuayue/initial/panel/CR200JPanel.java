package willow.train.kuayue.initial.panel;

import kasuga.lib.registrations.common.BlockReg;
import kasuga.lib.registrations.registry.CreateRegistry;
import net.createmod.catnip.data.Couple;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec2;
import willow.train.kuayue.block.panels.*;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.block.panels.block_entity.CR200jFrontBlockEntity;
import willow.train.kuayue.block.panels.block_entity.renderer.CR200jFrontRenderer;
import willow.train.kuayue.block.panels.cr200j.CR200jFrontBlock;
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

public class CR200JPanel {
    public static final MapColor CR200J_COLOR = MapColor.COLOR_GREEN;

    public static final CreateRegistry registry = AllElements.testRegistry;

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_CR200J =
            new PanelRegistration<CustomRenderedDoorBlock>("door_marshalled_cr200j")
                    .block(properties ->
                            new CustomRenderedDoorBlock(properties, Couple.create(
                                    registry.asResource("carriage/carriage_marshalled_cr200j/door/door_bottom_lh"),
                                    registry.asResource("carriage/carriage_marshalled_cr200j/door/door_top_lh"))
                            , Couple.create(
                                    registry.asResource("carriage/carriage_marshalled_cr200j/door/door_bottom"),
                                    registry.asResource("carriage/carriage_marshalled_cr200j/door/door_top")),
                            RenderShape.ENTITYBLOCK_ANIMATED, false))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_CR200J_2 =
            new PanelRegistration<CustomRenderedDoorBlock>("door_marshalled_cr200j_2")
                    .block(properties ->
                            new CustomRenderedDoorBlock(properties, Couple.create(
                                    registry.asResource("carriage/carriage_marshalled_cr200j/door/door_bottom_lh_2"),
                                    registry.asResource("carriage/carriage_marshalled_cr200j/door/door_top_lh"))
                                    , Couple.create(
                                    registry.asResource("carriage/carriage_marshalled_cr200j/door/door_bottom_2"),
                                    registry.asResource("carriage/carriage_marshalled_cr200j/door/door_top")),
                                    RenderShape.ENTITYBLOCK_ANIMATED, true))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_CABIN_MARSHALLED_CR200J =
            new PanelRegistration<CustomRenderedDoorBlock>("door_cabin_marshalled_cr200j")
                    .block(properties ->
                            new CustomRenderedDoorBlock(properties, Couple.create(
                                    registry.asResource("carriage/carriage_marshalled_cr200j/door/door_bottom_lh"),
                                    registry.asResource("carriage/carriage_marshalled_cr200j/door/door_top_lh_2"))
                                    , Couple.create(
                                    registry.asResource("carriage/carriage_marshalled_cr200j/door/door_bottom"),
                                    registry.asResource("carriage/carriage_marshalled_cr200j/door/door_top_2")),
                                    RenderShape.MODEL, true))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_MARSHALLED_CR200J =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_marshalled_cr200j")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.SLIDE,
                                    "carriage/carriage_marshalled_cr200j/end_face/end_face_door_lh",
                                    "carriage/carriage_marshalled_cr200j/end_face/end_face_door",
                                    "carriage/carriage_marshalled_cr200j/end_face/end_face_frame"
                            )
                    ).materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> END_FACE_MARSHALLED_CR200J_2 =
            new PanelRegistration<CustomRenderedEndfaceBlock>("end_face_marshalled_cr200j_2")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.ROTATE,
                                    "carriage/carriage_marshalled_cr200j/end_face/end_face_door_lh",
                                    "carriage/carriage_marshalled_cr200j/end_face/end_face_door",
                                    "carriage/carriage_marshalled_cr200j/end_face/end_face_frame"
                            )
                    ).materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> PANEL_BOTTOM_MARSHALLED_CR200J =
            new PanelRegistration<TrainPanelBlock>("panel_bottom_marshalled_cr200j")
            .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
            .materialAndColor(CR200J_COLOR)
            .tab(AllElements.neoKuayueCarriageTab )
            .noOcclusion().strengthAndTool(1.5f, 3f)
            .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> PANEL_MIDDLE_LOGO_CR_MARSHALLED_CR200J =
            new PanelRegistration<TrainHingePanelBlock>("panel_middle_logo_cr_marshalled_cr200j")
            .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
            .materialAndColor(CR200J_COLOR)
            .tab(AllElements.neoKuayueCarriageTab )
            .noOcclusion().strengthAndTool(1.5f, 3f)
            .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainUnOpenableSmallWindowBlock> WINDOW_SEALED_MARSHALLED_CR200J =
            new PanelRegistration<TrainUnOpenableSmallWindowBlock>("window_sealed_marshalled_cr200j")
                    .block(TrainUnOpenableSmallWindowBlock::new)
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> PANEL_MIDDLE_MARSHALLED_CR200J =
            new PanelRegistration<TrainHingePanelBlock>("panel_middle_marshalled_cr200j")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> PANEL_TOP_MARSHALLED_CR200J =
            new PanelRegistration<TrainPanelBlock>("panel_top_marshalled_cr200j")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> PANEL_TOP_MARSHALLED_CR200J_2 =
            new PanelRegistration<TrainHingePanelBlock>("panel_top_marshalled_cr200j_2")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> PANEL_TOP_MARSHALLED_CR200J_3 =
            new PanelRegistration<TrainPanelBlock>("panel_top_marshalled_cr200j_3")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> PANEL_WIDE_FU_XING_MARSHALLED_CR200J =
            new PanelRegistration<TrainHingePanelBlock>("panel_wide_fu_xing_marshalled_cr200j")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(-1, 0), new Vec2(2, 1)))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> PANEL_WIDE_LOGO_CR_MARSHALLED_CR200J =
            new PanelRegistration<TrainHingePanelBlock>("panel_wide_logo_cr_marshalled_cr200j")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(-1, 0), new Vec2(2, 2)))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> PANEL_WIDE_MARSHALLED_CR200J =
            new PanelRegistration<TrainHingePanelBlock>("panel_wide_marshalled_cr200j")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(-1, 0), new Vec2(2, 2)))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainSmallWindowBlock> WINDOW_TOILET_MARSHALLED_CR200J =
            new PanelRegistration<TrainSmallWindowBlock>("window_toilet_marshalled_cr200j")
                    .block(TrainSmallWindowBlock::new)
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainOpenableWindowBlock> WINDOW_WIDE_MARSHALLED_CR200J =
            new PanelRegistration<TrainOpenableWindowBlock>("window_wide_marshalled_cr200j")
                    .block(p -> new TrainOpenableWindowBlock(p, 2))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainUnOpenableWindowBlock> WINDOW_WIDE_SEALED_MARSHALLED_CR200J =
            new PanelRegistration<TrainUnOpenableWindowBlock>("window_wide_sealed_marshalled_cr200j")
                    .block(p -> new TrainUnOpenableWindowBlock(p, 2))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SkirtRegistration<SkirtBlock> SKIRT_MARSHALLED_CR200J =
            new SkirtRegistration<SkirtBlock>("skirt_marshalled_cr200j")
                    .block(SkirtBlock::new)
                    .materialAndColor(MapColor.COLOR_BROWN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainLadderBlock> LADDER_MARSHALLED_CR200J =
            new SlabRegistration<TrainLadderBlock>("ladder_marshalled_cr200j")
                    .block(TrainLadderBlock::new)
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> FLOOR_MARSHALLED_CR200J =
            new SlabRegistration<TrainSlabBlock>("floor_marshalled_cr200j")
                    .block(properties -> new TrainSlabBlock(properties, false))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> TOILET_DD_MARSHALLED_CR200J =
            new SlabRegistration<TrainSlabBlock>("toilet_dd_marshalled_cr200j")
                    .block(properties -> new TrainSlabBlock(properties, false))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> CARPORT_CENTER_MARSHALLED_CR200J =
            new SlabRegistration<TrainSlabBlock>("carport_center_marshalled_cr200j")
                    .block(properties -> new TrainSlabBlock(properties, true))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> CARPORT_MARSHALLED_CR200J =
            new SlabRegistration<TrainSlabBlock>("carport_marshalled_cr200j")
                    .block(properties -> new TrainSlabBlock(properties, true))
                    .materialAndColor(CR200J_COLOR)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<CR200jFrontBlock> CR200J_FRONT_BLOCK =
            new BlockReg<CR200jFrontBlock>("head_cr200j")
                    .blockType(CR200jFrontBlock::new)
                    .materialColor(MapColor.COLOR_GREEN)
                    .defaultBlockItem()
                    .withBlockEntity("cr200j_front_block_entity", CR200jFrontBlockEntity::new)
                    .withBlockEntityRenderer(() -> CR200jFrontRenderer::new)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .tabTo(AllElements.neoKuayueCarriageTab)
                    .submit(AllElements.testRegistry);

    public static void invoke(){}
}
