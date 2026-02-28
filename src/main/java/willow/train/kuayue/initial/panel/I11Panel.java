package willow.train.kuayue.initial.panel;

import kasuga.lib.registrations.common.BlockReg;
import net.createmod.catnip.data.Couple;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import willow.train.kuayue.block.panels.FullShapeDirectionalBlock;
import willow.train.kuayue.block.panels.TrainHingePanelBlock;
import willow.train.kuayue.block.panels.TrainPanelBlock;
import willow.train.kuayue.block.panels.carport.DF11ChimneyBlock;
import willow.train.kuayue.block.panels.conductor.DF11CowCatcherBlock;
import willow.train.kuayue.block.panels.door.CustomRenderedDoorBlock;
import willow.train.kuayue.block.panels.slab.HingeSlabBlock;
import willow.train.kuayue.block.panels.slab.TrainSlabBlock;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.registration.PanelRegistration;
import willow.train.kuayue.initial.registration.SlabRegistration;

public class I11Panel {

    public static final BlockReg<DF11CowCatcherBlock> DF11_COWCATCHER =
            new BlockReg<DF11CowCatcherBlock>("df11_cowcatcher")
                    .blockType(DF11CowCatcherBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<FullShapeDirectionalBlock> DF11_HEAD =
            new BlockReg<FullShapeDirectionalBlock>("df11_head")
                    .blockType(FullShapeDirectionalBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> DF11_FLOOR =
            new SlabRegistration<TrainSlabBlock>("df11_floor")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLACK)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> DF11_CARPORT_EQUIP_B =
            new SlabRegistration<HingeSlabBlock>("df11_carport_equip_b")
                    .block(p -> new HingeSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> DF11_CARPORT_EQUIP_A =
            new SlabRegistration<HingeSlabBlock>("df11_carport_equip_a")
                    .block(p -> new HingeSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> DF11_CARPORT_CENTER_COOLING_FAN =
            new SlabRegistration<HingeSlabBlock>("df11_carport_center_cooling_fan")
                    .block(p -> new HingeSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> DF11_CARPORT_GENERAL =
            new SlabRegistration<TrainSlabBlock>("df11_carport_general")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> DF11_CARPORT_EQUIP_BLINDS =
            new SlabRegistration<TrainSlabBlock>("df11_carport_equip_blinds")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> DF11_CARPORT_LOUVER_FAN =
            new SlabRegistration<TrainSlabBlock>("df11_carport_louver_fan")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<FullShapeDirectionalBlock> DF11_RESERVOIR_TANK =
            new BlockReg<FullShapeDirectionalBlock>("df11_reservoir_tank")
                    .blockType(FullShapeDirectionalBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedDoorBlock> DF11_DOOR =
            new PanelRegistration<CustomRenderedDoorBlock>("df11_door")
                    .block(p -> new CustomRenderedDoorBlock(p,
                            Couple.create(
                                    AllElements.testRegistry.asResource("df11/door/df11_door_bottom_left"),
                                    AllElements.testRegistry.asResource("df11/door/df11_door_upper_left")
                            ), Couple.create(
                            AllElements.testRegistry.asResource("df11/door/df11_door_bottom_right"),
                            AllElements.testRegistry.asResource("df11/door/df11_door_upper_right")
                    ), new Vec3(0, 0, 0), new Vec3(0, 0, 0),RenderShape.ENTITYBLOCK_ANIMATED, false
                    ))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .tab(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> DF11_PANEL_AIR_INTAKE =
            new PanelRegistration<TrainPanelBlock>("df11_panel_air_intake")
                    .block(p -> new TrainPanelBlock(p, new Vec2(-1,0),new Vec2(2,1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> DF11_PANEL_EQUIP_BOTTOM =
            new PanelRegistration<TrainHingePanelBlock>("df11_panel_equip_bottom")
                    .block(TrainHingePanelBlock::new)
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> DF11_WINDOW_EQUIP_SMALL =
            new PanelRegistration<TrainHingePanelBlock>("df11_window_equip_small")
                    .block(TrainHingePanelBlock::new)
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> PANEL_BOTTOM_DF11 =
            new PanelRegistration<TrainPanelBlock>("panel_bottom_df11")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0,0),new Vec2(1,1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> PANEL_TOP_DF11 =
            new PanelRegistration<TrainPanelBlock>("panel_top_df11")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0,0),new Vec2(1,1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> DF11_PANEL_EQUIP_TOP =
            new PanelRegistration<TrainHingePanelBlock>("df11_panel_equip_top")
            .block(TrainHingePanelBlock::new)
            .materialAndColor(MapColor.COLOR_GREEN)
            .tab(AllElements.neoKuayueLocoTab)
            .noOcclusion().strengthAndTool(1.5f,3f)
            .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> DF11_PANEL_BLIND_TOP =
            new PanelRegistration<TrainPanelBlock>("df11_panel_blind_top")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0,0),new Vec2(1,1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> DF11_PANEL_BLIND_BOTTOM =
            new PanelRegistration<TrainPanelBlock>("df11_panel_blind_bottom")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0,0),new Vec2(1,1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> DF11_ROAD_SIGN =
            new PanelRegistration<TrainPanelBlock>("df11_road_sign")
                    .block(p -> new TrainPanelBlock(p, new Vec2(-1,0),new Vec2(2,1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> DF11_EQUIP_ROOM_WINDOW_1 =
            new PanelRegistration<TrainPanelBlock>("df11_equip_room_window_1")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0,0),new Vec2(1,1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> DF11_EQUIP_ROOM_WINDOW_2 =
            new PanelRegistration<TrainPanelBlock>("df11_equip_room_window_2")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0,0),new Vec2(1,1)))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<DF11ChimneyBlock> DF11_CHIMNEY =
            new SlabRegistration<DF11ChimneyBlock>("df11_chimney")
                    .block(p -> new DF11ChimneyBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static void invoke(){}
}
