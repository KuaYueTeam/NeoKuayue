package willow.train.kuayue.systems.device.driver.combustion;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.systems.device.AllDeviceBlockEntities;

public class InternalCombustionDriveControllerBlock extends Block implements IBE<InternalCombustionDriveControllerBlockEntity>, IWrenchable {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty OFFSET = IntegerProperty.create("effect_offset", 0 ,15);

    public InternalCombustionDriveControllerBlock(Properties pProperties) {
        super(pProperties.noOcclusion());
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(FACING, Direction.EAST)
                        .setValue(OFFSET, 0)
        );
    }

    @Override
    public Class<InternalCombustionDriveControllerBlockEntity> getBlockEntityClass() {
        return InternalCombustionDriveControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends InternalCombustionDriveControllerBlockEntity> getBlockEntityType() {
        return AllDeviceBlockEntities.INTERNAL_COMBUSTION_DRIVE_CONTROLLER_BLOCK_ENTITY.getType();
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    private static VoxelShape EMPTY_SHAPE = Block.box(0, 0, 0, 1, 1, 1);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return super.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, OFFSET);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if(context.getClickedFace() == Direction.UP){
            return IWrenchable.super.onWrenched(state, context);
        }
        // Set OFFSET to next value
        int offset = state.getValue(OFFSET);
        offset = (offset + 1) % 16;
        IWrenchable.super.updateAfterWrenched(state.setValue(OFFSET, offset) ,context);
        context.getLevel().setBlock(context.getClickedPos(), state.setValue(OFFSET, offset), 3);
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(BlockState pState, Rotation pRotation) {
        Rotation opposite = Rotation.NONE;
        switch (pRotation) {
            case CLOCKWISE_90 ->  opposite = Rotation.COUNTERCLOCKWISE_90;
            case COUNTERCLOCKWISE_90 ->   opposite = Rotation.CLOCKWISE_90;
            case CLOCKWISE_180 -> opposite = Rotation.CLOCKWISE_180;
        }
        return pState.setValue(FACING, opposite.rotate(pState.getValue(FACING)));
    }
}
