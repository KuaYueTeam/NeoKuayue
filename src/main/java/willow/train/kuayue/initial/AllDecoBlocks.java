package willow.train.kuayue.initial;

import kasuga.lib.registrations.common.BlockReg;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import willow.train.kuayue.block.panels.SkirtBlock;
import willow.train.kuayue.block.panels.deco.ACOutdoorUnitBlock;
import willow.train.kuayue.block.panels.deco.TeaBoilerBlock;
import willow.train.kuayue.block.panels.deco.YZTableBlock;
import willow.train.kuayue.block.panels.slab.CeilinShelfBlock;
import willow.train.kuayue.block.panels.slab.TrainSlabBlock;
import willow.train.kuayue.block.seat.M1SeatBlock;
import willow.train.kuayue.block.seat.RZSeatBlock;
import willow.train.kuayue.block.seat.YZSeatBlock;
import willow.train.kuayue.initial.registration.SkirtRegistration;
import willow.train.kuayue.initial.registration.SlabRegistration;

public class AllDecoBlocks {

    public static final YZSeatBlock.OffsetFunction YZ_FUNCTION_1 =
            ((state, index) -> {
                Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (facing == Direction.NORTH || facing == Direction.SOUTH) {
                    return switch (index) {
                        case 0 -> new Vec3(-.25, 0, -0.3125);
                        case 1 -> new Vec3(.25, 0, -0.3125);
                        case 2 -> new Vec3(.25, 0, 0.3125);
                        default -> new Vec3(-.25, 0, 0.3125);
                    };
                }
                return switch (index) {
                    case 0 -> new Vec3(0.3125, 0, .25);
                    case 1 -> new Vec3(0.3125, 0, -.25);
                    case 2 -> new Vec3(-0.3125, 0, -.25);
                    default -> new Vec3(-0.3125, 0, .25);
                };
            });

    public static final YZSeatBlock.OffsetFunction M1_SEAT_FUNCTION =
            ((state, index) -> {
                Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                Boolean isOffset = state.getValue(M1SeatBlock.SEAT_OFFSET);
                double yOffset = -.8;
                if (facing == Direction.NORTH || facing == Direction.SOUTH) {
                    if (!isOffset)
                        return index == 0 ? new Vec3(-.35, yOffset, 0) : new Vec3(.35, yOffset, 0);
                    if (facing == Direction.NORTH)
                        return index == 0 ? new Vec3(.25, yOffset, 0) : new Vec3(.85, yOffset, 0);
                    else
                        return index == 0 ? new Vec3(-.85, yOffset, 0) : new Vec3(-.25, yOffset, 0);
                }
                if (!isOffset)
                    return index == 0 ? new Vec3(0, yOffset, -.35) : new Vec3(0, yOffset, .35);
                if (facing == Direction.WEST)
                    return index == 0 ? new Vec3(0, yOffset, -.85) : new Vec3(0, yOffset, -.25);
                else
                    return index == 0 ? new Vec3(0, yOffset, .25) : new Vec3(0, yOffset, .85);
            });

    public static final BlockReg<TeaBoilerBlock> BOILING_WATER_PLACE =
            new BlockReg<TeaBoilerBlock>("boiling_water_place")
                    .blockType(p -> new TeaBoilerBlock(p, new Vec2(0, 0), new Vec2(1, 2)))
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<CeilinShelfBlock> CEILIN_SHELF =
            new BlockReg<CeilinShelfBlock>("ceilin_shelf")
                    .blockType(CeilinShelfBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<CeilinShelfBlock> CEILIN_SHELF_2 =
            new BlockReg<CeilinShelfBlock>("ceilin_shelf_2")
                    .blockType(CeilinShelfBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<CeilinShelfBlock> CEILIN_SHELF_3 =
            new BlockReg<CeilinShelfBlock>("ceilin_shelf_3")
                    .blockType(CeilinShelfBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<CeilinShelfBlock> LUGGAGE_RACK_M1 =
            new BlockReg<CeilinShelfBlock>("luggage_rack_m1")
                    .blockType(CeilinShelfBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<YZTableBlock> YZ_TABLE =
            new BlockReg<YZTableBlock>("yz_table")
                    .blockType(YZTableBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<RZSeatBlock> RZ_SEAT =
            new BlockReg<RZSeatBlock>("seat_rz")
                    .blockType(RZSeatBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<YZSeatBlock> YZ_SEAT_BLUE =
            new BlockReg<YZSeatBlock>("yz_seat_blue")
                    .blockType(p -> new YZSeatBlock(p, 4, YZ_FUNCTION_1))
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLUE)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<YZSeatBlock> YZ_SEAT_BLACK =
            new BlockReg<YZSeatBlock>("yz_seat_black")
                    .blockType(p -> new YZSeatBlock(p, 4, YZ_FUNCTION_1))
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<YZSeatBlock> YZ_SEAT_GREEN =
            new BlockReg<YZSeatBlock>("yz_seat_green")
                    .blockType(p -> new YZSeatBlock(p, 4, YZ_FUNCTION_1))
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<YZSeatBlock> YZ_SEAT_RED =
            new BlockReg<YZSeatBlock>("yz_seat_red")
                    .blockType(p -> new YZSeatBlock(p, 4, YZ_FUNCTION_1))
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<YZSeatBlock> YZ_SEAT_YELLOW =
            new BlockReg<YZSeatBlock>("yz_seat_yellow")
                    .blockType(p -> new YZSeatBlock(p, 4, YZ_FUNCTION_1))
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<YZSeatBlock> YZ_SEAT_CYAN =
            new BlockReg<YZSeatBlock>("yz_seat_cyan")
                    .blockType(p -> new YZSeatBlock(p, 4, YZ_FUNCTION_1))
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<YZSeatBlock> YZ_SEAT_GRAY =
            new BlockReg<YZSeatBlock>("yz_seat_gray")
                    .blockType(p -> new YZSeatBlock(p, 4, YZ_FUNCTION_1))
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<YZSeatBlock> YZ_SEAT_2 =
            new BlockReg<YZSeatBlock>("yz_seat_22")
                    .blockType(p -> new YZSeatBlock(p, 2,
                                ((state, index) -> {
                                    Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                                    if (facing == Direction.NORTH || facing == Direction.SOUTH) {
                                        return index == 0 ? new Vec3(-.25, 0, 0) : new Vec3(.25, 0, 0);
                                    }
                                    return index == 0 ? new Vec3(0, 0, -.25) : new Vec3(0, 0, .25);
                                })
                            ))
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<M1SeatBlock> SEAT_M1 =
            new BlockReg<M1SeatBlock>("seat_m1")
                    .blockType(properties ->
                            new M1SeatBlock(properties, 2, M1_SEAT_FUNCTION))
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<TrainSlabBlock> FLOURESCENT_LIGHT =
            new BlockReg<TrainSlabBlock>("flourescent_light")
                    .blockType(p -> new TrainSlabBlock(p, false))
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<ACOutdoorUnitBlock> AC_OUTDOOR_UNIT =
            new BlockReg<ACOutdoorUnitBlock>("ac_outdoor_unit")
                    .blockType(ACOutdoorUnitBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .addProperty(properties -> properties.strength(1.0f, 2.0f))
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);
    public static final SkirtRegistration<SkirtBlock> AUXILIARY_RESERVOIR =
            new SkirtRegistration<SkirtBlock>("auxiliary_reservoir")
                    .block(SkirtBlock::new)
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SkirtRegistration<SkirtBlock> AUXILIARY_RESERVOIR_LONGITUDINAL =
            new SkirtRegistration<SkirtBlock>("auxiliary_reservoir_longitudinal")
                    .block(SkirtBlock::new)
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SkirtRegistration<SkirtBlock> CARRIAGE_BATTERY =
            new SkirtRegistration<SkirtBlock>("carriage_battery")
                    .block(SkirtBlock::new)
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> CARRIAGE_BRAKE_CYLINDER =
            new SlabRegistration<TrainSlabBlock>("carriage_brake_cylinder")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueCarriageTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);
    public static void invoke(){}
}
