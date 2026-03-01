package willow.train.kuayue.systems.device.track.train_station;

import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.initial.AllEdgePoints;
import willow.train.kuayue.systems.device.AllDeviceBlockEntities;
import willow.train.kuayue.systems.device.AllDeviceSystems;

import java.util.List;
import java.util.UUID;

public class TrainStationBlockEntity extends SmartBlockEntity implements MenuProvider {
    public TrackTargetingBehaviour<TrainStation> edgePoint;

    public TrainStationBlockEntity(BlockPos blockPos, BlockState state) {
        //super(AllDeviceBlockEntities.STATION_BLOCK_ENTITY.getType(), blockPos, state);
        super(null, blockPos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(edgePoint = new TrackTargetingBehaviour<>(this, AllEdgePoints.TRAIN_STATION));
    }

    public InteractionResult onUse(Level pLevel, Player pPlayer) {
        if(pLevel.isClientSide) return InteractionResult.SUCCESS;
        TrainStation station = edgePoint.getEdgePoint();
        if(station == null || station.segmentId == null)
            return InteractionResult.FAIL;
        UUID uuid = station.segmentId;
        NetworkHooks.openScreen((ServerPlayer) pPlayer, this, (buf)->{
            buf.writeUUID(uuid);
            if(station.localInfo == null) {
                GraphStationInfo.EMPTY.write(buf);
                return;
            }
            station.localInfo.write(buf);
        });
        return InteractionResult.SUCCESS;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Train Station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new TrainStationMenu(pContainerId, pPlayerInventory, this, new SimpleContainerData(0));
    }

    public TrainStation getEdgePoint() {
        return edgePoint.getEdgePoint();
    }
}
