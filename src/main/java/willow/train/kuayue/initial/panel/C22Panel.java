package willow.train.kuayue.initial.panel;

import net.createmod.catnip.data.Couple;
import net.minecraft.world.level.block.RenderShape;
import kasuga.lib.registrations.common.BlockReg;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import willow.train.kuayue.block.panels.TrainHingePanelBlock;
import willow.train.kuayue.block.panels.TrainPanelBlock;
import willow.train.kuayue.block.panels.door.CustomRenderedDoorBlock;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.block.panels.carport.AirVentBlock;
import willow.train.kuayue.block.panels.end_face.CustomRenderedEndfaceBlock;
import willow.train.kuayue.block.panels.slab.TrainLadderBlock;
import willow.train.kuayue.block.panels.slab.TrainSlabBlock;
import willow.train.kuayue.block.panels.window.TrainOpenableWindowBlock;
import willow.train.kuayue.block.panels.window.TrainSmallWindowBlock;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.registration.PanelRegistration;
import willow.train.kuayue.initial.registration.SlabRegistration;

public class C22Panel {

    public static final SlabRegistration<TrainSlabBlock> C22_FLOOR =
            new SlabRegistration<TrainSlabBlock>("22_floor")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> C22_FLOOR_CENTER =
            new SlabRegistration<TrainSlabBlock>("22_floor_center")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor( MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> C22_PANEL_BOTTOM =
            new PanelRegistration<TrainPanelBlock>("22_panel_bottom")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor( MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> C22_PANEL_MIDDLE =
            new PanelRegistration<TrainPanelBlock>("22_panel_middle")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor( MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainLadderBlock> C22_LADDER =
            new SlabRegistration<TrainLadderBlock>("22_ladder")
                    .block((properties) -> new TrainLadderBlock(properties, false))
                    .materialAndColor( MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainSmallWindowBlock> C22_WINDOW =
            new PanelRegistration<TrainSmallWindowBlock>("22_window")
                    .block(p -> new TrainSmallWindowBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor( MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainOpenableWindowBlock> C22_LARGE_WINDOW =
            new PanelRegistration<TrainOpenableWindowBlock>("22_large_window")
                    .block(p -> new TrainOpenableWindowBlock(p, 2))
                    .materialAndColor( MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainOpenableWindowBlock> C22_DOUBLE_SMALL_WINDOW =
            new PanelRegistration<TrainOpenableWindowBlock>("22_double_small_window")
                    .block(p -> new TrainOpenableWindowBlock(p, 2))
                    .materialAndColor( MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainOpenableWindowBlock> C22_SMALL_WINDOW =
            new PanelRegistration<TrainOpenableWindowBlock>("22_small_window")
                    .block(p -> new TrainOpenableWindowBlock(p, 1))
                    .materialAndColor( MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_22 =
            new PanelRegistration<CustomRenderedDoorBlock>("22_door")
                    .block(p -> new CustomRenderedDoorBlock(p,
                            Couple.create(
                                    AllElements.testRegistry.asResource("carriage/carriage_22/door/22_door_bottom_lh"),
                                    AllElements.testRegistry.asResource("carriage/carriage_22/door/22_door_upper_lh")
                            ), Couple.create(
                            AllElements.testRegistry.asResource("carriage/carriage_22/door/22_door_bottom"),
                            AllElements.testRegistry.asResource("carriage/carriage_22/door/22_door_upper")
                    ), new Vec3(0, 0, 0),new Vec3(0, 0, 0), RenderShape.MODEL, false
                    ))
                    .materialAndColor( MapColor.COLOR_BLUE)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> C22_COUPLER =
            new SlabRegistration<TrainSlabBlock>("22_coupler")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor( MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedEndfaceBlock> C22_END_FACE =
            new PanelRegistration<CustomRenderedEndfaceBlock>("22_end_face")
                    .block(properties ->
                            new CustomRenderedEndfaceBlock(
                                    properties, TrainPanelProperties.DoorType.NO_DOOR,
                                    null,
                                    null,
                                    "carriage/carriage_22/22_end_face"
                            )
                    ).materialAndColor( MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> C22_CARPORT_PANEL_TOP =
            new PanelRegistration<TrainPanelBlock>("22_carport_panel_top")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor( MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> C22_CARPORT_PANEL_TOP_LAMP =
            new PanelRegistration<TrainHingePanelBlock>("22_carport_panel_top_lamp")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor( MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> C22_CARPORT_TOP_CAP =
            new SlabRegistration<TrainSlabBlock>("22_carport_top_cap")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor( MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<AirVentBlock> C22_AIR_VENT =
            new BlockReg<AirVentBlock>("22_air_vent")
                    .blockType(AirVentBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueCarriageTab)
                    .submit(AllElements.testRegistry);

    public static void invoke(){}
}
