package willow.train.kuayue.behaviour;

import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.trains.entity.*;
import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.core.BlockPos;

import net.minecraft.server.level.ServerLevel;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.initial.AllPackets;
import willow.train.kuayue.network.s2c.TrainCrashSyncPacket;
import willow.train.kuayue.systems.train_extension.CarriageAdditionalData;
import willow.train.kuayue.systems.train_extension.ExtensionHelper;
import willow.train.kuayue.systems.train_extension.TrainAdditionalData;
import willow.train.kuayue.systems.train_extension.bogey_weight.BogeyAdditionalData;
import willow.train.kuayue.systems.train_extension.bogey_weight.BogeyExtensionSystem;
import willow.train.kuayue.systems.train_extension.conductor.ConductorHelper;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BogeyOverweightBehavior implements MovementBehaviour {

    public static final HashMap<MovementContext, AtomicInteger> tickers = new HashMap<>();

    @Override
    public void tick(MovementContext context) {
        if (context.world.isClientSide() ||
                !BlockPos.ZERO.equals(context.localPos)) return;
        if (!(context.contraption instanceof CarriageContraption cc))
            return;
        if (!(cc.entity instanceof CarriageContraptionEntity cce))
            return;
        UUID trainId = cce.trainId;
        Train train = Create.RAILWAYS.trains.get(trainId);
        if (train == null || train.derailed || train.getCurrentStation() != null) return;

        if (tickers.computeIfAbsent(context,
                c -> new AtomicInteger(0)).decrementAndGet() < 10) {
            return;
        }
        tickers.get(context).set(0);
        Carriage carriage = cce.getCarriage();
        boolean isOverweighted = ExtensionHelper.isCarriageOverweighted(
                train,
                carriage,
                cce.carriageIndex,
                carriage.isOnTwoBogeys()
        );
        if (!isOverweighted) return;
        train.crash();
        AllPackets.CHANNEL.boardcastToClients(
                new TrainCrashSyncPacket("msg.train_extension.crash.overweight", trainId),
                (ServerLevel) cce.level,
                new BlockPos(context.position)
        );
    }
}
