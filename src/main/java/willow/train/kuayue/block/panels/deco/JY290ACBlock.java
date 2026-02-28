package willow.train.kuayue.block.panels.deco;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.block.panels.window.TrainOpenableWindowBlock;

public class JY290ACBlock extends Block implements IWrenchable {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;

    public JY290ACBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.EAST)
                .setValue(HINGE, DoorHingeSide.RIGHT));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = pState.getValue(FACING).getOpposite();
        return switch (facing) {
            case NORTH -> Block.box(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 12.0D);
            case SOUTH -> Block.box(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 12.0D);
            case WEST -> Block.box(4.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);
            case EAST -> Block.box(4.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);
            default -> Block.box(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 12.0D);
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING).add(HINGE));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext)
                .setValue(FACING, pContext.getHorizontalDirection())
                .setValue(HINGE, TrainOpenableWindowBlock.getHinge(pContext));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if(!level.isClientSide()) {
            level.setBlock(pos, state.cycle(HINGE),3);
        }
        if (level.getBlockState(context.getClickedPos()) != state)
            IWrenchable.playRotateSound(level, context.getClickedPos());
        return InteractionResult.SUCCESS;
    }
}
