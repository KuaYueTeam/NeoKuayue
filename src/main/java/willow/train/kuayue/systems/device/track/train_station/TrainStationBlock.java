package willow.train.kuayue.systems.device.track.train_station;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import willow.train.kuayue.systems.device.AllDeviceBlockEntities;
import willow.train.kuayue.systems.device.AllDeviceBlocks;

import java.util.function.Function;

public class TrainStationBlock extends Block implements IBE<TrainStationBlockEntity> {

    public TrainStationBlock(Properties pProperties) {
        super(pProperties.noOcclusion());
    }

    @Override
    public Class<TrainStationBlockEntity> getBlockEntityClass() {
        return TrainStationBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TrainStationBlockEntity> getBlockEntityType() {
        //return AllDeviceBlockEntities.STATION_BLOCK_ENTITY.getType();
        return null;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state,level,pos,newState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }


    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        TrainStationBlockEntity blockEntity = IBE.super.getBlockEntity(pLevel, pPos);
        return blockEntity.onUse(pLevel, pPlayer);
    }
}
