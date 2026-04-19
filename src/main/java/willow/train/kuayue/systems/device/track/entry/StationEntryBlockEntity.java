package willow.train.kuayue.systems.device.track.entry;

import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import willow.train.kuayue.systems.device.AllDeviceBlockEntities;
import willow.train.kuayue.systems.device.AllDeviceBlocks;
import willow.train.kuayue.systems.device.AllDeviceEdgePoints;

import java.util.List;

public class StationEntryBlockEntity extends SmartBlockEntity{
    private TrackTargetingBehaviour<StationEntry> edgePoint;

    public StationEntryBlockEntity(BlockPos blockPos, BlockState state) {
        //super(AllDeviceBlockEntities.STATION_ENTRY_BLOCK_ENTITY.getType(), blockPos, state);
        super(null, blockPos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
        list.add(edgePoint = new TrackTargetingBehaviour<>(this, AllDeviceEdgePoints.STATION_ENTRY.getType()));
    }
}
