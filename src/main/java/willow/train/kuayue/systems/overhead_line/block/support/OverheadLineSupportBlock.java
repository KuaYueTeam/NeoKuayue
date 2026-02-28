package willow.train.kuayue.systems.overhead_line.block.support;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.systems.overhead_line.OverheadLineSystem;

public abstract class OverheadLineSupportBlock<T extends OverheadLineSupportBlockEntity> extends Block implements IBE<T>, IWrenchable{
    public OverheadLineSupportBlock(Properties pProperties) {
        super(pProperties.noOcclusion());
        this.registerDefaultState(getDefaultState());
    }

    protected BlockState getDefaultState(){
        return this.getStateDefinition().any()
                .setValue(FACING, Direction.EAST);
    }


    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        T be = getBlockEntity(pLevel, pPos);
        if(be != null){
            be.onPlacement();
        }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        pLevel.scheduleTick(pPos, pState.getBlock(), 2);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack item = pPlayer.getItemInHand(pHand);
        
        if (item.is(AllItems.WRENCH.get())) {
            if (!pLevel.isClientSide) {
                net.minecraft.world.level.block.entity.BlockEntity rawBlockEntity = pLevel.getBlockEntity(pPos);
                
                if (rawBlockEntity instanceof OverheadLineSupportBlockEntity) {
                    OverheadLineSupportBlockEntity blockEntity = (OverheadLineSupportBlockEntity) rawBlockEntity;
                    try {
                        NetworkHooks.openScreen((ServerPlayer) pPlayer, blockEntity, pPos);
                    } catch (Exception e) {
                        System.out.println("[ERROR] Failed to open GUI: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        
        return InteractionResult.PASS;
    }

    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.mirror != null) {
            state = state.mirror(transform.mirror);
        } 

        if (state.hasProperty(FACING)) {
            Direction facing = state.getValue(FACING);
            Direction f = transform.rotateFacing(facing);
            state = state.setValue(FACING, f);
        }

        return state;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        if (pState.hasProperty(FACING)) {
            return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
        }
        return pState;
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        if (pState.hasProperty(FACING)) {
            return pState.setValue(FACING, pMirror.mirror(pState.getValue(FACING)));
        }
        return pState;
    }
}
