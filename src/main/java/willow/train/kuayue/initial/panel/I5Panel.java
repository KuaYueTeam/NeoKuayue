package willow.train.kuayue.initial.panel;

import kasuga.lib.registrations.common.BlockReg;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.VoxelShape;
import willow.train.kuayue.block.panels.FullShapeDirectionalBlock;
import willow.train.kuayue.block.panels.TrainHingePanelBlock;
import willow.train.kuayue.block.panels.TrainPanelBlock;
import willow.train.kuayue.block.panels.base.TrainPanelShapes;
import willow.train.kuayue.block.panels.carport.DF11GChimneyBlock;
import willow.train.kuayue.block.panels.carport.DF5ChimneyBlock;
import willow.train.kuayue.block.panels.conductor.DF5CowCatcherBlock;
import willow.train.kuayue.block.panels.door.TrainDoorBlock;
import willow.train.kuayue.block.panels.slab.*;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.registration.PanelRegistration;
import willow.train.kuayue.initial.registration.SlabRegistration;

public class I5Panel {

    public static final BlockReg<FullShapeDirectionalBlock> DF5_END_FACE_1 =
            new BlockReg<FullShapeDirectionalBlock>("df5_end_face_1")
                    .blockType(FullShapeDirectionalBlock::new)
                    .material(Material.METAL).materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<FullShapeDirectionalBlock> DF5_END_FACE_2 =
            new BlockReg<FullShapeDirectionalBlock>("df5_end_face_2")
                    .blockType(FullShapeDirectionalBlock::new)
                    .material(Material.METAL).materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<DF5CowCatcherBlock> DF5_COWCATCHER =
            new BlockReg<DF5CowCatcherBlock>("df5_cowcatcher")
                    .blockType(DF5CowCatcherBlock::new)
                    .material(Material.METAL).materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> DF5_FLOOR =
            new SlabRegistration<TrainSlabBlock>("df5_floor")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_BLACK)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> DF5_CENTER_FLOOR =
            new SlabRegistration<TrainSlabBlock>("df5_center_floor")
                    .block(p -> new TrainSlabBlock(p, false))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_BLACK)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> DF5_TRANSIT_FLOOR =
            new SlabRegistration<HingeSlabBlock>("df5_transit_floor")
                    .block(p -> new HingeSlabBlock(p, false))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<FullShapeDirectionalBlock> DF5_FUEL_TANK =
            new BlockReg<FullShapeDirectionalBlock>("df5_fuel_tank")
                    .blockType(FullShapeDirectionalBlock::new)
                    .material(Material.METAL).materialColor(MaterialColor.COLOR_BLACK)
                    .addProperty(BlockBehaviour.Properties::noOcclusion)
                    .addProperty(properties -> properties.strength(1.5f, 3f))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueLocoTab)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> DF5_HANDRAIL =
            new PanelRegistration<TrainPanelBlock>("df5_handrail")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0,0),new Vec2(1,1)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapePanelBlock> DF5_PANEL_BOTTOM =
            new PanelRegistration<VariableShapePanelBlock>("df5_panel_bottom")
                    .block(p -> new VariableShapePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.HALF_PANEL_SHAPE_EAST),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.QUARTER_PANEL_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapePanelBlock> DF5_PANEL_TOP =
            new PanelRegistration<VariableShapePanelBlock>("df5_panel_top")
                    .block(p -> new VariableShapePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.HALF_PANEL_SHAPE_EAST),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.QUARTER_PANEL_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapePanelBlock> DF5_CARPORT_1 =
            new PanelRegistration<VariableShapePanelBlock>("df5_carport_1")
                    .block(p -> new VariableShapePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_SHAPE_EAST),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_COLLISION_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapePanelBlock> DF5_CARPORT_2 =
            new PanelRegistration<VariableShapePanelBlock>("df5_carport_2")
                    .block(p -> new VariableShapePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_SHAPE_EAST),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_COLLISION_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapeHingePanelBlock> DF5_CARPORT_3 =
            new PanelRegistration<VariableShapeHingePanelBlock>("df5_carport_3")
                    .block(p -> new VariableShapeHingePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_SHAPE_EAST),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_COLLISION_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapeHingePanelBlock> DF5_CARPORT_4 =
            new PanelRegistration<VariableShapeHingePanelBlock>("df5_carport_4")
                    .block(p -> new VariableShapeHingePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_SHAPE_EAST),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_COLLISION_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapePanelBlock> DF5_CARPORT_5 =
            new PanelRegistration<VariableShapePanelBlock>("df5_carport_5")
                    .block(p -> new VariableShapePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_SHAPE_EAST),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_COLLISION_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapePanelBlock> DF5_CARPORT_GRILLE =
            new PanelRegistration<VariableShapePanelBlock>("df5_carport_grille")
                    .block(p -> new VariableShapePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_SHAPE_EAST),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_COLLISION_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<HingeSlabBlock> DF5_CARPORT_CABIN =
            new SlabRegistration<HingeSlabBlock>("df5_carport_cabin")
                    .block(p -> new HingeSlabBlock(p, false))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab )
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapePanelBlock> DF5_AIR_INTAKE =
            new PanelRegistration<VariableShapePanelBlock>("df5_air_intake")
                    .block(p -> new VariableShapePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.HALF_PANEL_SHAPE_EAST),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.QUARTER_PANEL_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapePanelBlock> DF5_AIR_INTAKE_2 =
            new PanelRegistration<VariableShapePanelBlock>("df5_air_intake_2")
                    .block(p -> new VariableShapePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_SHAPE_EAST),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CARPORT_COLLISION_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapePanelBlock> DF5_AIR_INTAKE_3 =
            new PanelRegistration<VariableShapePanelBlock>("df5_air_intake_3")
                    .block(p -> new VariableShapePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.HALF_PANEL_SHAPE_EAST),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.QUARTER_PANEL_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapePanelBlock> DF5_RADIATOR_GRID =
            new PanelRegistration<VariableShapePanelBlock>("df5_radiator_grid")
                    .block(p -> new VariableShapePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 2),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_RADIATOR_GRID_SHAPE_EAST),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_RADIATOR_GRID_COLLISION_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<TrainSlabBlock> DF5_COOLING_FAN =
            new SlabRegistration<TrainSlabBlock>("df5_cooling_fan")
                    .block(p -> new TrainSlabBlock(p, true ))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapeTrainDoorBlock> DF5_ENGINE_ACCESS_DOOR =
            new PanelRegistration<VariableShapeTrainDoorBlock>("df5_engine_access_door")
                    .block(p -> new VariableShapeTrainDoorBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_ENGINE_ACCESS_DOOR_SHAPE_EAST),
                            () -> (state, level, blockPos, context) -> {
                                if (state.getValue(TrainDoorBlock.OPEN))
                                    return TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_ENGINE_ACCESS_DOOR_OPEN_COLLISION_SHAPE_EAST);
                                return TrainPanelShapes.rotateShape(Direction.EAST,
                                        state.getValue(TrainPanelBlock.FACING),
                                        TrainPanelShapes.DF5_ENGINE_ACCESS_DOOR_COLLISION_SHAPE_EAST);
                            }))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapeTrainDoorBlock> DF5_EQUIP_DOOR_1 =
            new PanelRegistration<VariableShapeTrainDoorBlock>("df5_equip_door_1")
                    .block(p -> new VariableShapeTrainDoorBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) -> {
                                DoorHingeSide hingeSide = state.getValue(BlockStateProperties.DOOR_HINGE);
                                VoxelShape shape = TrainPanelShapes.DF5_EQUIP_DOOR_1_SHAPE_EAST;
                                return TrainPanelShapes.rotateShape(Direction.EAST,
                                        state.getValue(TrainPanelBlock.FACING),
                                        hingeSide == DoorHingeSide.RIGHT ? shape :
                                                shape.move(0, 0, -0.375));
                            },
                            () -> (state, level, blockPos, context) -> {
                                DoorHingeSide hingeSide = state.getValue(BlockStateProperties.DOOR_HINGE);
                                VoxelShape collisionShape = TrainPanelShapes.DF5_EQUIP_DOOR_1_COLLISION_SHAPE_EAST;
                                VoxelShape openCollisionShape = TrainPanelShapes.DF5_EQUIP_DOOR_1_OPEN_COLLISION_SHAPE_EAST;
                                if (state.getValue(TrainDoorBlock.OPEN))
                                    return TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            hingeSide == DoorHingeSide.RIGHT ? openCollisionShape :
                                                    openCollisionShape.move(0, 0, -0.375));
                                return TrainPanelShapes.rotateShape(Direction.EAST,
                                        state.getValue(TrainPanelBlock.FACING),
                                        hingeSide == DoorHingeSide.RIGHT ? collisionShape :
                                                collisionShape.move(0, 0, -0.375));
                            }))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapeTrainDoorBlock> DF5_EQUIP_DOOR_2 =
            new PanelRegistration<VariableShapeTrainDoorBlock>("df5_equip_door_2")
                    .block(p -> new VariableShapeTrainDoorBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_ENGINE_ACCESS_DOOR_SHAPE_EAST),
                            () -> (state, level, blockPos, context) -> {
                                if (state.getValue(TrainDoorBlock.OPEN))
                                    return TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_ENGINE_ACCESS_DOOR_OPEN_COLLISION_SHAPE_EAST);
                                return TrainPanelShapes.rotateShape(Direction.EAST,
                                        state.getValue(TrainPanelBlock.FACING),
                                        TrainPanelShapes.DF5_ENGINE_ACCESS_DOOR_COLLISION_SHAPE_EAST);
                            }))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapeTrainDoorBlock> DF5_CABIN_DOOR =
            new PanelRegistration<VariableShapeTrainDoorBlock>("df5_cabin_door")
                    .block(p -> new VariableShapeTrainDoorBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CABIN_DOOR_SHAPE_EAST),
                            () -> (state, level, blockPos, context) -> {
                                if (state.getValue(TrainDoorBlock.OPEN))
                                    return TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_CABIN_DOOR_OPEN_COLLISION_SHAPE_EAST);
                                return TrainPanelShapes.rotateShape(Direction.EAST,
                                        state.getValue(TrainPanelBlock.FACING),
                                        TrainPanelShapes.DF5_CABIN_DOOR_SHAPE_EAST);
                            }))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapePanelBlock> DF5_HEAD_END_FACE =
            new PanelRegistration<VariableShapePanelBlock>("df5_head_end_face")
                    .block(p -> new VariableShapePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_ENGINE_ACCESS_DOOR_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapePanelBlock> DF5_HEAD_END_FACE_2 =
            new PanelRegistration<VariableShapePanelBlock>("df5_head_end_face_2")
                    .block(p -> new VariableShapePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_ENGINE_ACCESS_DOOR_SHAPE_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<TrainPanelBlock> DF5_HEAD_PANEL =
            new PanelRegistration<TrainPanelBlock>("df5_head_panel")
                    .block(p -> new TrainPanelBlock(p, new Vec2(0, 0), new Vec2(1, 2)))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final SlabRegistration<DF5ChimneyBlock> DF5_CHIMNEY =
            new SlabRegistration<DF5ChimneyBlock>("df5_chimney")
                    .block(p -> new DF5ChimneyBlock(p, false))
                    .materialAndColor(Material.METAL, MaterialColor.COLOR_GREEN)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f, 3f)
                    .submit(AllElements.testRegistry);

    public static final PanelRegistration<VariableShapeHingePanelBlock> DF5_COWCATCHER_LADDER =
            new PanelRegistration<VariableShapeHingePanelBlock>("df5_cowcatcher_ladder")
                    .block(p -> new VariableShapeHingePanelBlock(p,
                            new Vec2(0, 0), new Vec2(1, 1),
                            () -> (state, level, blockPos, context) ->
                                    TrainPanelShapes.rotateShape(Direction.EAST,
                                            state.getValue(TrainPanelBlock.FACING),
                                            TrainPanelShapes.DF5_COWCATCHER_LADDER_EAST)))
                    .materialAndColor(Material.METAL,MaterialColor.COLOR_BLUE)
                    .tab(AllElements.neoKuayueLocoTab)
                    .noOcclusion().strengthAndTool(1.5f,3f)
                    .submit(AllElements.testRegistry);

    public static void invoke(){}
}
