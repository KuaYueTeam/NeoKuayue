package willow.train.kuayue.event.server;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.foundation.utility.Couple;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.initial.AllPackets;
import willow.train.kuayue.network.s2c.TrainMergePacket;
import willow.train.kuayue.systems.train_extension.CarriageAdditionalData;
import willow.train.kuayue.systems.train_extension.TrainAdditionalData;
import willow.train.kuayue.systems.train_extension.conductor.Conductable;
import willow.train.kuayue.systems.train_extension.conductor.ConductorHelper;
import willow.train.kuayue.systems.train_extension.conductor.ConductorLocation;

import java.lang.ref.WeakReference;
import java.util.*;

import static willow.train.kuayue.utils.CarriageUtil.remapCarriage;

@Slf4j
public class TrainCouplerTickEvents {

    private static int tickCounter = 0;
    private static final int TICK_INTERVAL = 40;

    private static final HashSet<UUID> removed = new HashSet<>();
    private static final HashSet<Train> removeFromNewlyMerged = new HashSet<>();
    private static final HashSet<Train> trainMerging = new HashSet<>();

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        //需要节流的逻辑
        tickCounter++;
        if(tickCounter >= TICK_INTERVAL) {
            for(Map.Entry<UUID, TrainAdditionalData> entry : Kuayue.TRAIN_EXTENSION.getData().entrySet()) {
                List<CarriageAdditionalData> carriages = entry.getValue().getCarriages();
                for(int i = 0; i < carriages.size(); i++) {
                    CarriageAdditionalData carriageData = carriages.get(i);
                    if(carriageData.shouldRemap){
                        carriageData.shouldRemap = !remapCarriage(
                                Create.RAILWAYS.trains.get(entry.getKey()).carriages.get(i),
                                false
                        );
                    }
                }
            }

            tickCounter = 0;
        }

        if (event.phase != TickEvent.Phase.START) return;
        for (Train t : Kuayue.TRAIN_EXTENSION.newlyMerged) {
            if (!t.derailed) continue;
            t.derailed = false;
            removeFromNewlyMerged.add(t);
        }
        Kuayue.TRAIN_EXTENSION.newlyMerged.clear();
        for (UUID id : Kuayue.TRAIN_EXTENSION.trainsToRemove) {
            Train train = Create.RAILWAYS.trains.get(id);
            if (train == null) {
                removed.add(id);
                continue;
            }
            if (train.owner == null) continue;
            GlobalStation station = train.getCurrentStation();
            if (station == null) continue;
            ServerLevel level = event.getServer().getLevel(station.blockEntityDimension);
            if (level == null) continue;
            BlockEntity be = level.getBlockEntity(station.blockEntityPos);

            if (!(be instanceof StationBlockEntity sbe)) continue;
            if (!train.canDisassemble()) continue;
            station.nearestTrain = new WeakReference<>(train);
            if (sbe.tryDisassembleTrain((ServerPlayer) level.getPlayerByUUID(train.owner))) {
                Create.RAILWAYS.removeTrain(id);
                removed.add(id);
            }
        }
        Kuayue.TRAIN_EXTENSION.trainsToRemove.removeAll(removed);
        removed.clear();

        HashSet<Couple<ConductorLocation>> coolingDownToRemove = new HashSet<>();
        Kuayue.TRAIN_EXTENSION.conductorsCoolingDown.forEach((pair, info) -> {
            info.checkInterval++;
            if (info.checkInterval < 10) {
                return;
            }
            info.checkInterval = 0;

            float distanceToSqr = ConductorHelper.getConductorFlatDistToSqr(pair, info);
            if(distanceToSqr > .5f || distanceToSqr == -1) {
                coolingDownToRemove.add(pair);
            }
        });
        if(!coolingDownToRemove.isEmpty()) {
            coolingDownToRemove.forEach(Kuayue.TRAIN_EXTENSION.conductorsCoolingDown::remove);
        }

        for (Map.Entry<UUID, Train> entry : Create.RAILWAYS.trains.entrySet()) {
            Train train = entry.getValue();
            if (trainMerging.contains(train)) continue;
            for (Map.Entry<UUID, Train> e2 : Create.RAILWAYS.trains.entrySet()) {
                Train t2 = e2.getValue();
                if (t2 == train) continue;
                if (trainMerging.contains(t2)) continue;
                ConductorHelper.CollidedConnectors conductorPair =
                        ConductorHelper.getCollidedConnector(train, t2, .1f);
                if (!ConductorHelper.isValidCollide2(conductorPair)) continue;
                System.out.println("conductor1: " + conductorPair.conductorA() +
                        ", conductor2: " + conductorPair.conductorB());

                Conductable conductorA = conductorPair.conductorA();
                Conductable conductorB = conductorPair.conductorB();
                boolean isAHead = conductorPair.isAHead();
                boolean isBHead = conductorPair.isBHead();

                if(conductorA.carriage() < 0 || conductorA.carriage() >= train.carriages.size()) continue;
                if(conductorB.carriage() < 0 || conductorB.carriage() >= t2.carriages.size()) continue;

                Carriage carriageA = train.carriages.get(conductorA.carriage());
                Carriage carriageB = t2.carriages.get(conductorB.carriage());

                Level level = null;
                if (carriageA.anyAvailableEntity() != null) {
                    level = carriageA.anyAvailableEntity().level();
                } else if (carriageB.anyAvailableEntity() != null) {
                    level = carriageB.anyAvailableEntity().level();
                }
                if( level == null) continue;

                if(!conductorA.canConnectTo(level, t2, carriageB, conductorB)) continue;
                if(!conductorB.canConnectTo(level, train, carriageA, conductorA)) continue;

                ConductorHelper.TrainSortResult sorted =
                        ConductorHelper.sortTrains(
                                conductorA, isAHead,
                                conductorB, isBHead,
                                false
                        );
                if (sorted == null) continue;

                Kuayue.TRAIN_EXTENSION.trainsToMerge.add(
                        new ConductorHelper.TrainMergeRequest(sorted.loco(),
                                sorted.carriages(), sorted.shouldReverseCarriages(),
                                sorted.isLocoHead(), conductorPair.spacing(),
                                false)
                );
                trainMerging.add(train);
                trainMerging.add(t2);
            }
        }

        for (ConductorHelper.TrainMergeRequest request : Kuayue.TRAIN_EXTENSION.trainsToMerge) {
            Kuayue.LOGGER.debug("[SERVER] Before MergeTrain method call");
            boolean b = ConductorHelper.mergeTrains(
                    request.loco(),
                    request.carriages(),
                    request.shouldReverseCarriages(),
                    request.isLocoHead(),
                    request.spacing(),
                    request.clientSide()
            );
            if(b) {
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                if(server != null) {
                    server.getPlayerList().getPlayers().forEach(p -> {
                        AllPackets.CHANNEL.sendToClient(
                                new TrainMergePacket(request),
                                p
                        );
                    });
                } else {
                    Kuayue.LOGGER.debug("Failed to send TrainMergePacket: MinecraftServer is null");
                }
                Kuayue.TRAIN_EXTENSION.newlyMerged.add(request.loco());
            } else {
                Kuayue.LOGGER.debug("[SERVER] MergeTrain failed!");
            }
        }

        Kuayue.TRAIN_EXTENSION.trainsToMerge.clear();
        Kuayue.TRAIN_EXTENSION.newlyMerged.removeAll(removeFromNewlyMerged);
        removeFromNewlyMerged.clear();
        trainMerging.clear();
        // NOTICE: 列车速度自带方向属性
        // NOTICE: 列车的bogey的TravellingPoint也自带方向属性, 可以用于单转向架列车方向的判定

//        for (UUID id : trackingTrains) {
//            Test.trackTrainData(id);
//        }
//        if (event.phase == TickEvent.Phase.START) {
//            int i = 0;
//            Train trainA = null, trainB = null;
//            for (Map.Entry<UUID, Train> entry : Create.RAILWAYS.trains.entrySet()) {
//                if (i > 1) break;
//                if (i > 0) {
//                    trainB = entry.getValue();
//                } else {
//                    trainA = entry.getValue();
//                }
//                i++;
//            }
//            boolean flag = false;
//            if (trainA != null && trainB != null && flag) {
//                Test.tryMigrateTrains(trainA, trainB,
//                        event.getServer().getLevel(ServerLevel.OVERWORLD),
//                        false);
//            }
//        }
    }
}
