package willow.train.kuayue.systems.device.track.entry;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import willow.train.kuayue.systems.device.AllDeviceBlockEntities;
import willow.train.kuayue.systems.device.AllDeviceBlocks;

import java.util.Properties;

public class StationEntryBlock extends Block implements IBE<StationEntryBlockEntity> {
    public StationEntryBlock(Properties pProperties) {
        super(pProperties.noOcclusion());
    }

    @Override
    public Class<StationEntryBlockEntity> getBlockEntityClass() {
        return StationEntryBlockEntity.class;
    }

    @Override
    public BlockEntityType<StationEntryBlockEntity> getBlockEntityType() {
        //return AllDeviceBlockEntities.STATION_ENTRY_BLOCK_ENTITY.getType();
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
}
