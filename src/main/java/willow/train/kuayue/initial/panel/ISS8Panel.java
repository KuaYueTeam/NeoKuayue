package willow.train.kuayue.initial.panel;

import com.simibubi.create.foundation.utility.Couple;
import kasuga.lib.registrations.common.BlockReg;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import willow.train.kuayue.block.panels.FullShapeDirectionalBlock;
import willow.train.kuayue.block.panels.HeadBlock;
import willow.train.kuayue.block.panels.TrainHingePanelBlock;
import willow.train.kuayue.block.panels.TrainPanelBlock;
import willow.train.kuayue.block.panels.conductor.SS8CowCatcherBlock;
import willow.train.kuayue.block.panels.door.CustomRenderedDoorBlock;
import willow.train.kuayue.block.panels.slab.HingeSlabBlock;
import willow.train.kuayue.block.panels.slab.TrainSlabBlock;
import willow.train.kuayue.block.panels.window.TrainUnOpenableWindowBlock;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.registration.PanelRegistration;
import willow.train.kuayue.initial.registration.SlabRegistration;

public class ISS8Panel {

    public static final PanelRegistration<TrainUnOpenableWindowBlock> SS8_WINDOWS_SMALL =
            new PanelRegistration<TrainUnOpenableWindowBlock>("ss8_windows_small")
                    .block(p -> new TrainUnOpenableWindowBlock(p, 1))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<SS8CowCatcherBlock> SS8_COWCATCHER =
            new BlockReg<SS8CowCatcherBlock>("ss8_cowcatcher")
                    .blockType(SS8CowCatcherBlock::new)
                    .material(Material.METAL).materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> SS8_CARPORT =
            new SlabRegistration<HingeSlabBlock>("ss8_carport")
                    .block(p -> new HingeSlabBlock(p, true))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> SS8_CARPORT_AD =
            new SlabRegistration<HingeSlabBlock>("ss8_carport_ad")
                    .block(p -> new HingeSlabBlock(p, true))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> SS8_CARPORT_FAN =
            new SlabRegistration<HingeSlabBlock>("ss8_carport_fan")
                    .block(p -> new HingeSlabBlock(p, true))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> SS8_CARPORT_LINK =
            new SlabRegistration<HingeSlabBlock>("ss8_carport_link")
                    .block(p -> new HingeSlabBlock(p, true))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> SS8_FLOOR =
            new SlabRegistration<TrainSlabBlock>("ss8_floor")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_BLACK)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> SS8_WINDOWS_BIG =
            new PanelRegistration<TrainHingePanelBlock>("ss8_windows_big")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(-1,0),new Vec2(2,1)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> SS8_PANEL_BOTTOM_AD =
            new PanelRegistration<TrainHingePanelBlock>("ss8_panel_bottom_ad")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0,0),new Vec2(1,1)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);
    public static final PanelRegistration<TrainHingePanelBlock> SS8_PANEL_UPPER_AD =
            new PanelRegistration<TrainHingePanelBlock>("ss8_panel_upper_ad")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0,0),new Vec2(1,1)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> SS8_PANEL_BOTTOM =
            new PanelRegistration<TrainHingePanelBlock>("ss8_panel_bottom")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0,0),new Vec2(1,1)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> SS8_PANEL_UPPER =
            new PanelRegistration<TrainHingePanelBlock>("ss8_panel_upper")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0,0),new Vec2(1,1)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<HeadBlock> SS8_HEAD =
            new BlockReg<HeadBlock>("ss8_head")
                    .blockType(HeadBlock::new)
                    .material(Material.METAL).materialColor(MaterialColor.COLOR_BLUE)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<FullShapeDirectionalBlock> SS8_TRANSFORMER =
            new BlockReg<FullShapeDirectionalBlock>("ss8_transformer")
                    .blockType(FullShapeDirectionalBlock::new)
                    .material(Material.METAL).materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> SS8_ROOF =
            new SlabRegistration<HingeSlabBlock>("ss8_roof")
                    .block(p -> new HingeSlabBlock(p, true))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> SS8_CARPORT_CENTER =
            new SlabRegistration<TrainSlabBlock>("ss8_carport_center")
                    .block(p -> new TrainSlabBlock(p, true ))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> SS8_SANDBOX_FRONT =
            new SlabRegistration<HingeSlabBlock>("ss8_sandbox_front")
                    .block(p -> new HingeSlabBlock(p, false))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedDoorBlock> SS8_DOOR =
            new PanelRegistration<CustomRenderedDoorBlock>("ss8_door")
                    .block(p -> new CustomRenderedDoorBlock(p,
                            Couple.create(
                                    AllElements.testRegistry.asResource("ss8/ss8_door_bottom_lh"),
                                    AllElements.testRegistry.asResource("ss8/ss8_door_upper_lh")
                            ), Couple.create(
                            AllElements.testRegistry.asResource("ss8/ss8_door_bottom"),
                            AllElements.testRegistry.asResource("ss8/ss8_door_upper")
                    ), new Vec3(0, 0, 0), new Vec3(0, 0, 0),RenderShape.MODEL, false
                    ))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .tab(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> KM240_MONUMENT =
            new PanelRegistration<TrainPanelBlock>("240km_monument")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static void invoke(){}

}
