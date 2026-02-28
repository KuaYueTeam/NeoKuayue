package willow.train.kuayue.block.panels;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.utils.DirectionUtil;

public class SkirtBlock extends TrainPanelBlock implements IWrenchable {
    public static final EnumProperty<TrainPanelProperties.SkirtType> SKIRT_TYPE = TrainPanelProperties.SKIRT_TYPE;
    public SkirtBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.EAST)
                .setValue(SKIRT_TYPE, TrainPanelProperties.SkirtType.LEFT)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(SKIRT_TYPE));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext)
                .setValue(FACING, pContext.getHorizontalDirection().getOpposite())
                .setValue(SKIRT_TYPE, getSkirtHinge(pContext));
    }

    public TrainPanelProperties.SkirtType getSkirtHinge(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos left = DirectionUtil.left(pos, facing, 1);
        BlockPos right = DirectionUtil.right(pos, facing, 1);
        BlockState leftState = level.getBlockState(left);
        BlockState rightState = level.getBlockState(right);
        boolean hasLeft = leftState.is(this);
        boolean hasRight = rightState.is(this);
        if(hasRight && hasLeft)
            return TrainPanelProperties.SkirtType.MIDDLE;
        Vec3 leftCenter = DirectionUtil.centerOf(left), rightCenter = DirectionUtil.centerOf(right),
                clickPos = context.getClickLocation();
        boolean leftHinge = clickPos.distanceToSqr(leftCenter) <= clickPos.distanceToSqr(rightCenter);
        return leftHinge ? TrainPanelProperties.SkirtType.LEFT : TrainPanelProperties.SkirtType.RIGHT;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        if (!pIsMoving)
            updateLeftAndRight(pPos, pLevel, pState.getValue(FACING));
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        if (!pIsMoving)
            updateLeftAndRight(pPos, pLevel, pState.getValue(FACING));
    }

    public void updateLeftAndRight(BlockPos pos, Level level, Direction facing) {
        BlockPos left = DirectionUtil.left(pos, facing, 1);
        neighborChange(level.getBlockState(left), level, left, pos);
        BlockPos right = DirectionUtil.right(pos, facing, 1);
        neighborChange(level.getBlockState(right), level, right, pos);
    }

    public void neighborChange(BlockState state, Level level, BlockPos pos, BlockPos neighbor) {
        if (!state.is(this)) return;
        Direction facing = state.getValue(FACING);
        BlockPos left = DirectionUtil.left(pos, facing, 1);
        BlockPos right = DirectionUtil.right(pos, facing, 1);
        if (!neighbor.equals(left) && !neighbor.equals(right)) return;
        BlockState leftState = level.getBlockState(left), rightState = level.getBlockState(right);
        if (leftState.is(this) && rightState.is(this)) {
            if (neighbor.equals(left) && leftState.getValue(SKIRT_TYPE) == TrainPanelProperties.SkirtType.LEFT) return;
            if (neighbor.equals(right) && rightState.getValue(SKIRT_TYPE) == TrainPanelProperties.SkirtType.RIGHT) return;
            state = state.setValue(SKIRT_TYPE, TrainPanelProperties.SkirtType.MIDDLE);
            level.setBlock(pos, state, 11);
            return;
        }
        if (!leftState.is(this) && rightState.is(this)) {
            state = state.setValue(SKIRT_TYPE, TrainPanelProperties.SkirtType.RIGHT);
            level.setBlock(pos, state, 11);
            return;
        }
        if (leftState.is(this) && !rightState.is(this)) {
            state = state.setValue(SKIRT_TYPE, TrainPanelProperties.SkirtType.LEFT);
            level.setBlock(pos, state, 11);
        }

    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockState rotated = state.cycle(SKIRT_TYPE);
        if (!rotated.canSurvive(world, context.getClickedPos()))
            return InteractionResult.PASS;

        KineticBlockEntity.switchToBlockState(world, context.getClickedPos(), updateAfterWrenched(rotated, context));

        BlockEntity be = context.getLevel()
                .getBlockEntity(context.getClickedPos());
        if (be instanceof GeneratingKineticBlockEntity) {
            ((GeneratingKineticBlockEntity) be).reActivateSource = true;
        }

        if (world.getBlockState(context.getClickedPos()) != state)
            IWrenchable.playRotateSound(world, context.getClickedPos());

        return InteractionResult.SUCCESS;
    }
}
