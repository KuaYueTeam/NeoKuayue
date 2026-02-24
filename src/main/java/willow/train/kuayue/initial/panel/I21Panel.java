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
import willow.train.kuayue.block.panels.conductor.DF21HeadBlock;
import willow.train.kuayue.block.panels.conductor.DF21HeadEarlyBlock;
import willow.train.kuayue.block.panels.door.CustomRenderedDoorBlock;
import willow.train.kuayue.block.panels.slab.CarportHingeSlabBlock;
import willow.train.kuayue.block.panels.slab.TrainSlabBlock;
import willow.train.kuayue.block.panels.window.TrainUnOpenableWindowBlock;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.registration.PanelRegistration;
import willow.train.kuayue.initial.registration.SlabRegistration;

public class I21Panel {

    public static final BlockReg<DF21HeadBlock> HEAD_DF21 =
            new BlockReg<DF21HeadBlock>("head_df21")
                    .blockType(DF21HeadBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<DF21HeadEarlyBlock> HEAD_EARLY_DF21 =
            new BlockReg<DF21HeadEarlyBlock>("head_early_df21")
                    .blockType(DF21HeadEarlyBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<CarportHingeSlabBlock> EXHAUST_FAN_DF21 =
            new SlabRegistration<CarportHingeSlabBlock>("exhaust_fan_df21")
                    .block(p -> new CarportHingeSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_RED)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<CarportHingeSlabBlock> DYNAMIC_CARPORT_DF21 =
            new SlabRegistration<CarportHingeSlabBlock>("dynamic_carport_df21")
                    .block(p -> new CarportHingeSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<CarportHingeSlabBlock> DYNAMIC_TRANSITION_CARPORT_DF21 =
            new SlabRegistration<CarportHingeSlabBlock>("dynamic_transition_carport_df21")
                    .block(p -> new CarportHingeSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<CarportHingeSlabBlock> DYNAMIC_RADIATOR_DF21 =
            new SlabRegistration<CarportHingeSlabBlock>("dynamic_radiator_df21")
                    .block(p -> new CarportHingeSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<CarportHingeSlabBlock> GENERAL_CARPORT_DF21 =
            new SlabRegistration<CarportHingeSlabBlock>("general_carport_df21")
                    .block(p -> new CarportHingeSlabBlock(p, true))
                    .materialAndColor(MapColor.COLOR_RED)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> GENERAL_SLAB_DF21 =
            new SlabRegistration<TrainSlabBlock>("general_slab_df21")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(MapColor.COLOR_RED)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_CABIN_DF21 =
            new PanelRegistration<CustomRenderedDoorBlock>("door_cabin_df21")
                    .block(p -> new CustomRenderedDoorBlock(p,
                            Couple.create(
                                    AllElements.testRegistry.asResource("df21/door/df21_cabin_door_bottom_left"),
                                    AllElements.testRegistry.asResource("df21/door/df21_cabin_door_upper_left")
                            ), Couple.create(
                            AllElements.testRegistry.asResource("df21/door/df21_cabin_door_bottom_right"),
                            AllElements.testRegistry.asResource("df21/door/df21_cabin_door_upper_right")
                    ), new Vec3(0, 0, 0),new Vec3(0, 0, 0), RenderShape.ENTITYBLOCK_ANIMATED, false
                    ))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .tab(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<CustomRenderedDoorBlock> DOOR_EQUIP_DF21 =
            new PanelRegistration<CustomRenderedDoorBlock>("door_equip_df21")
                    .block(p -> new CustomRenderedDoorBlock(p,
                            Couple.create(
                                    AllElements.testRegistry.asResource("df21/door/df21_equip_door_bottom_left"),
                                    AllElements.testRegistry.asResource("df21/door/df21_equip_door_upper_left")
                            ), Couple.create(
                            AllElements.testRegistry.asResource("df21/door/df21_equip_door_bottom_right"),
                            AllElements.testRegistry.asResource("df21/door/df21_equip_door_upper_right")
                    ), new Vec3(0, 0, 0),new Vec3(0, 0, 0), RenderShape.MODEL, false
                    ))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .tab(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainUnOpenableWindowBlock> WINDOW_EQUIP_DF21 =
            new PanelRegistration<TrainUnOpenableWindowBlock>("window_equip_df21")
                    .block(p -> new TrainUnOpenableWindowBlock(p, -1, 2))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainUnOpenableWindowBlock> EQUIP_MIDDLE_EARLY_DF21 =
            new PanelRegistration<TrainUnOpenableWindowBlock>("equip_middle_early_df21")
                    .block(p -> new TrainUnOpenableWindowBlock(p, 1, 0, 2))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainHingePanelBlock> GENERAL_BOTTOM_SLAB_DF21 =
            new PanelRegistration<TrainHingePanelBlock>("general_bottom_slab_df21")
                    .block(p -> new TrainHingePanelBlock(p, new Vec2(0, 0), new Vec2(1, 1)))
                    .materialAndColor(MapColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<FullShapeDirectionalBlock> FUEL_TANK_DF21 =
            new BlockReg<FullShapeDirectionalBlock>("fuel_tank_df21")
                    .blockType(FullShapeDirectionalBlock::new)
                    .materialColor(MapColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static void invoke(){}
}
