package willow.train.kuayue.block.panels;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import kasuga.lib.core.base.UnModeledBlockProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.block.panels.base.CompanyTrainPanel;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.block.panels.base.TrainPanelShapes;
import willow.train.kuayue.block.panels.block_entity.EditablePanelEntity;
import willow.train.kuayue.initial.AllBlocks;
import willow.train.kuayue.initial.AllTags;
import willow.train.kuayue.initial.item.EditablePanelItem;
import willow.train.kuayue.utils.DirectionUtil;

import java.util.Objects;

public class TrainPanelBlock extends Block implements IWrenchable, EntityBlock {
    public final Vec2 beginPos, endPos;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final UnModeledBlockProperty<TrainPanelProperties.EditType, EnumProperty<TrainPanelProperties.EditType>> EDIT_TYPE =
            UnModeledBlockProperty.create(EnumProperty.create("edit_type", TrainPanelProperties.EditType.class));
    public static final int
            YELLOW = 16776961,
            YELLOW2 = 16776960,
            RED = 15216648,
            BLUE = 22220,
            BLUE2 = 0x60A0B0,
            BLUE3 = 468326,
            BLACK = 789516;

    public static int getSignColor(BlockState state) {

        if (state.is(Objects.requireNonNull(AllTags.C25B.tag())))
            return YELLOW2;
        if (state.is(Objects.requireNonNull(AllTags.C25G.tag())))
            return RED;
        if (state.is(Objects.requireNonNull(AllTags.C25K.tag())))
            return BLUE;
        if (state.is(Objects.requireNonNull(AllTags.C25Z.tag())))
            return BLUE2;
        if (state.is(Objects.requireNonNull(AllTags.C25T.tag())))
            return BLUE3;

        return YELLOW;
    }

    public TrainPanelBlock(Properties pProperties, Vec2 beginPos, Vec2 endPos) {
        super(pProperties);
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(FACING, Direction.EAST)
                        .setValue(EDIT_TYPE, TrainPanelProperties.EditType.NONE)
        );
        this.beginPos = beginPos;
        this.endPos = endPos;
    }

    public TrainPanelBlock(Properties properties) {
        this(properties, Vec2.ZERO, Vec2.ONE);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return TrainPanelShapes.getShape(pState.getValue(FACING).getOpposite());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return TrainPanelShapes.getShape(pState.getValue(FACING).getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING, EDIT_TYPE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if (pState.getValue(EDIT_TYPE) != TrainPanelProperties.EditType.NONE)
            return new EditablePanelEntity(pPos, pState);
        return null;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.getItemInHand(pHand).is(EditablePanelItem.COLORED_BRUSH.getItem())) {

            if (pState.is(Objects.requireNonNull(AllTags.BOTTOM_PANEL.tag()))) {
                int signColor = getSignColor(pState);
            }

            pState.setValue(EDIT_TYPE, TrainPanelProperties.EditType.TYPE);
            if (!pLevel.isClientSide) {
                if (pLevel.getBlockEntity(pPos) == null)
//                NetworkHooks.openScreen(
//                        (ServerPlayer) pPlayer,
//                        (CarriageTypeSignEntity) pLevel.getBlockEntity(pPos),
//                        pPos);
                return InteractionResult.PASS;
            }
            return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext)
                .setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        generateCompanyBlock(pLevel, pState, pPos, pIsMoving);
    }

    public boolean hasHinge() {
        if (!defaultBlockState().hasProperty(BlockStateProperties.DOOR_HINGE)) return false;
        int width = (int) endPos.x - (int) beginPos.x;
        if (width % 2 == 0) return true;
        return !(width % 2 == 1 && - beginPos.x == (width / 2));
    }

    public void generateCompanyBlock(Level level, BlockState state, BlockPos pos, boolean isMoving) {
        Direction direction = state.getValue(FACING);
        boolean open = state.hasProperty(BlockStateProperties.OPEN) ?
                state.getValue(BlockStateProperties.OPEN) : false;
        boolean leftHinge = !state.hasProperty(BlockStateProperties.DOOR_HINGE) || state.getValue(BlockStateProperties.DOOR_HINGE) == DoorHingeSide.LEFT;
        BlockUseFunction function = (l, p, parentState, myPos, myState, player, hand, hit) -> {
            if (p.equals(myPos)) return InteractionResult.SUCCESS;
            BlockState state1 = generateCompanyState(direction, leftHinge ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT, open);
            level.setBlock(myPos, state1, 10);
            CompanyTrainPanel.setParentBlock(myPos, level, state1, pos);
            return InteractionResult.SUCCESS;
        };
        walkAllValidPos(level, pos, state, null, null, null, function);
    }

    public BlockState generateCompanyState(Direction direction, DoorHingeSide hingeSide, boolean open) {
        return AllBlocks.COMPANY_TRAIN_PANEL.instance().defaultBlockState()
                .setValue(CompanyTrainPanel.FACING, direction)
                .setValue(BlockStateProperties.DOOR_HINGE, hingeSide);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        removeCompanyBlock(pLevel, pState, pPos, pIsMoving);
        if(pNewState.getBlock().getClass().equals(pState.getBlock().getClass())) return;
        pLevel.removeBlock(pPos, pIsMoving);
    }

    public void removeCompanyBlock(Level level, BlockState state, BlockPos pos, boolean isMoving) {
        BlockUseFunction function = (l, p, parentState, myPos, myState, player, hand, hit) -> {
            if (p.equals(myPos)) return InteractionResult.SUCCESS;
            l.removeBlockEntity(myPos);
            l.removeBlock(myPos, isMoving);
            return InteractionResult.SUCCESS;
        };
        walkAllValidPos(level, pos, state, null, null, null, function);
    }

    public void walkAllValidPos(Level level, BlockPos blockPos, BlockState parentState,
                                Player player, InteractionHand hand, BlockHitResult hit, BlockUseFunction function) {
        Direction direction = parentState.getValue(FACING);
        BlockPos firstPos = blockPos;
        boolean leftHinge = !parentState.hasProperty(BlockStateProperties.DOOR_HINGE) ||
                parentState.getValue(BlockStateProperties.DOOR_HINGE) == DoorHingeSide.LEFT;
        firstPos = DirectionUtil.left(firstPos, direction, (int) (leftHinge ? - beginPos.x : beginPos.x));
        firstPos = firstPos.offset(0, beginPos.y, 0);
        int length = (int) (endPos.x - beginPos.x),
                height = (int) (endPos.y - beginPos.y);

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                function.apply(level, blockPos, parentState, firstPos, level.getBlockState(firstPos), player, hand, hit);
                firstPos = firstPos.above();
            }
            firstPos = firstPos.offset(0, - height, 0);
            firstPos = DirectionUtil.right(firstPos, direction, leftHinge ? 1 : -1);
        }
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.PASS;
    }

    public interface BlockUseFunction {
        InteractionResult apply(Level level, BlockPos parentPos, BlockState parentState, BlockPos myPos,
                               BlockState myState, Player player, InteractionHand hand, BlockHitResult hit);
    }
}
