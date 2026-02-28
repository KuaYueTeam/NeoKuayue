package willow.train.kuayue.initial.panel;

import kasuga.lib.registrations.common.BlockReg;
import net.createmod.catnip.data.Couple;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import willow.train.kuayue.block.panels.HeadBlock;
import willow.train.kuayue.block.panels.TrainHingePanelBlock;
import willow.train.kuayue.block.panels.conductor.SS3CowCatcherBlock;
import willow.train.kuayue.block.panels.door.CustomRenderedDoorBlock;
import willow.train.kuayue.block.panels.slab.HingeSlabBlock;
import willow.train.kuayue.block.panels.slab.TrainSlabBlock;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.registration.PanelRegistration;
import willow.train.kuayue.initial.registration.SlabRegistration;

public class ISS3Panel {

    public static final PanelRegistration<TrainHingePanelBlock> SS3_PANEL_BOTTOM =
            new PanelRegistration<TrainHingePanelBlock>("ss3_panel_bottom")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> SS3_PANEL_UPPER =
            new PanelRegistration<TrainHingePanelBlock>("ss3_panel_upper")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> SS3_PANEL_UPPER_WINDOWS =
            new PanelRegistration<TrainHingePanelBlock>("ss3_panel_upper_windows")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainHingePanelBlock> SS3_PANEL_BOTTOM_AD =
            new PanelRegistration<TrainHingePanelBlock>("ss3_panel_bottom_ad")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> SS3_PANEL_UPPER_AD =
            new PanelRegistration<TrainHingePanelBlock>("ss3_panel_upper_ad")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> SS3_CARPORT =
            new SlabRegistration<TrainSlabBlock>("ss3_carport")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_BLACK)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> SS3_CARPORT_CENTER =
            new SlabRegistration<TrainSlabBlock>("ss3_carport_center")
                    .block(p -> new TrainSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_BLACK)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> SS3_ROOF =
            new SlabRegistration<HingeSlabBlock>("ss3_roof")
                    .block(p -> new HingeSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> SS3_CARPORT_AD =
            new SlabRegistration<HingeSlabBlock>("ss3_carport_ad")
                    .block(p -> new HingeSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final SlabRegistration<TrainSlabBlock> SS3_FLOOR =
            new SlabRegistration<TrainSlabBlock>("ss3_floor")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLACK)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static final SlabRegistration<TrainSlabBlock> SS3_CARPORT_SKYLIGHT =
            new SlabRegistration<TrainSlabBlock>("ss3_carport_skylight")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLACK)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> SS3_ROOF_VENT =
            new SlabRegistration<HingeSlabBlock>("ss3_roof_vent")
                    .block(p -> new HingeSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);


    public static final SlabRegistration<TrainSlabBlock> SS3_FLOOR_CENTER =
            new SlabRegistration<TrainSlabBlock>("ss3_floor_center")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLACK)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> SS3_TRANSFORMER =
            new SlabRegistration<TrainSlabBlock>("ss3_transformer")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_BLACK)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<HeadBlock> SS3_HEAD =
            new BlockReg<HeadBlock>("ss3_head")
                    .blockType(HeadBlock::new)
                    .materialColor(MapColor.COLOR_BLUE)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedDoorBlock> SS3_DOOR =
            new PanelRegistration<CustomRenderedDoorBlock>("ss3_door")
                    .block(p -> new CustomRenderedDoorBlock(p,
                            Couple.create(
                                    AllElements.testRegistry.asResource("ss3/ss3_door_bottom_lh"),
                                    AllElements.testRegistry.asResource("ss3/ss3_door_upper_lh")
                            ), Couple.create(
                            AllElements.testRegistry.asResource("ss3/ss3_door_bottom"),
                            AllElements.testRegistry.asResource("ss3/ss3_door_upper")
                    ), new Vec3(-.1875, 0, 0),new Vec3(0, 0, 0), RenderShape.MODEL, false
                    ))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .tab(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<SS3CowCatcherBlock> SS3_COWCATCHER =
            new BlockReg<SS3CowCatcherBlock>("ss3_cowcatcher")
                    .blockType(SS3CowCatcherBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);
    public static void invoke(){}
}
