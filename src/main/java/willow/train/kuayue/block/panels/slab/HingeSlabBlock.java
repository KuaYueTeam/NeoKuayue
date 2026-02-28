package willow.train.kuayue.block.panels.slab;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.block.panels.base.TrainPanelShapes;
import willow.train.kuayue.block.panels.window.TrainOpenableWindowBlock;

public class HingeSlabBlock extends TrainSlabBlock implements IWrenchable {

    int width;

    public HingeSlabBlock(Properties pProperties, boolean isCarport) {
        super(pProperties, isCarport);
        registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.EAST)
                .setValue(BlockStateProperties.DOOR_HINGE, DoorHingeSide.LEFT)
        );
        this.width = 1;
    }

    public HingeSlabBlock(Properties pProperties, boolean isCarport, int width) {
        super(pProperties, isCarport);
        registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.EAST)
                .setValue(BlockStateProperties.DOOR_HINGE, DoorHingeSide.LEFT)
        );
        this.width = width;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        DoorHingeSide hinge = pState.getValue(BlockStateProperties.DOOR_HINGE);
        Direction facing = pState.getValue(FACING).getOpposite();
        if (carport)
            return TrainPanelShapes.CARPORT_CENTER;
        if (width == 2) {
            if (hinge == DoorHingeSide.RIGHT)
                return switch (facing) {
                    case NORTH -> TrainPanelShapes.FLOOR_TWO_GRID_NORTH;
                    case SOUTH -> TrainPanelShapes.FLOOR_TWO_GRID_SOUTH;
                    case WEST -> TrainPanelShapes.FLOOR_TWO_GRID_WEST;
                    case EAST -> TrainPanelShapes.FLOOR_TWO_GRID_EAST;
                    default -> TrainPanelShapes.FLOOR_TWO_GRID_NORTH;
                };
            return switch (facing) {
                case NORTH -> TrainPanelShapes.FLOOR_TWO_GRID_NORTH.move(1, 0, 0);
                case SOUTH -> TrainPanelShapes.FLOOR_TWO_GRID_SOUTH.move(-1, 0, 0);
                case WEST -> TrainPanelShapes.FLOOR_TWO_GRID_WEST.move(0, 0, -1);
                case EAST -> TrainPanelShapes.FLOOR_TWO_GRID_EAST.move(0, 0, 1);
                default -> TrainPanelShapes.FLOOR_TWO_GRID_NORTH.move(1, 0, 0);
            };
        }
        return TrainPanelShapes.FLOOR;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(BlockStateProperties.DOOR_HINGE));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext)
                .setValue(BlockStateProperties.DOOR_HINGE, TrainOpenableWindowBlock.getHinge(pContext));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockState rotated = state.cycle(BlockStateProperties.DOOR_HINGE);
        if (!rotated.canSurvive(world, context.getClickedPos()))
            return InteractionResult.PASS;

        KineticBlockEntity.switchToBlockState(world, context.getClickedPos(), updateAfterWrenched(rotated, context));

        if (world.getBlockState(context.getClickedPos()) != state)
            IWrenchable.playRotateSound(world, context.getClickedPos());

        return InteractionResult.SUCCESS;
    }
}
