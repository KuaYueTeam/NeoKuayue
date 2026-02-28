package willow.train.kuayue.initial.panel;

import kasuga.lib.registrations.common.BlockReg;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec2;
import willow.train.kuayue.block.panels.FullShapeDirectionalBlock;
import willow.train.kuayue.block.panels.TrainHingePanelBlock;
import willow.train.kuayue.block.panels.conductor.JY290CowCatcherBlock;
import willow.train.kuayue.block.panels.deco.JY290ACBlock;
import willow.train.kuayue.block.panels.door.JY290DoorBlock;
import willow.train.kuayue.block.panels.slab.TrainSlabBlock;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.registration.PanelRegistration;
import willow.train.kuayue.initial.registration.SlabRegistration;

public class IJY290Panel {

    public static final BlockReg<FullShapeDirectionalBlock> JY290_HEAD_2 =
            new BlockReg<FullShapeDirectionalBlock>("jy290_head_2")
                    .blockType(FullShapeDirectionalBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<FullShapeDirectionalBlock> JY290_HEAD_1 =
            new BlockReg<FullShapeDirectionalBlock>("jy290_head_1")
                    .blockType(FullShapeDirectionalBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<JY290CowCatcherBlock> JY290_COWCATCHER =
            new BlockReg<JY290CowCatcherBlock>("jy290_cowcatcher")
                    .blockType(JY290CowCatcherBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> JY290_FLOOR =
            new SlabRegistration<TrainSlabBlock>("jy290_floor")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLACK)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> JY290_FLOOR_LADDER =
            new SlabRegistration<TrainSlabBlock>("jy290_floor_ladder")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLACK)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> JY290_CARPORT =
            new SlabRegistration<TrainSlabBlock>("jy290_carport")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<FullShapeDirectionalBlock> JY290_EQUIPMENT =
            new BlockReg<FullShapeDirectionalBlock>("jy290_equipment")
                    .blockType(FullShapeDirectionalBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> JY290_PANEL =
            new PanelRegistration<TrainHingePanelBlock>("jy290_panel")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(-1, 0), new Vec2(1, 2)))
                    .materialAndColor(MapColor.COLOR_RED)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> JY290_PANEL_LOGO =
            new PanelRegistration<TrainHingePanelBlock>("jy290_panel_logo")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(-1, 0), new Vec2(1, 2)))
                    .materialAndColor(MapColor.COLOR_RED)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<JY290ACBlock> JY290_AC =
            new BlockReg<JY290ACBlock>("jy290_ac")
                    .blockType(JY290ACBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> JY290_VENT =
            new SlabRegistration<TrainSlabBlock>("jy290_vent")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<JY290DoorBlock> JY290_DOOR =
            new PanelRegistration<JY290DoorBlock>("jy290_door")
                    .block(p -> new JY290DoorBlock(p,new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .tab(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static void invoke(){}
}
