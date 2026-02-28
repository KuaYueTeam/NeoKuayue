package willow.train.kuayue.systems.train_extension;

import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.trains.entity.*;
import com.simibubi.create.content.trains.graph.TrackGraph;
import kasuga.lib.core.util.data_type.Pair;
import lombok.NonNull;
import net.createmod.catnip.data.Couple;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.mixins.mixin.AccessorTrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Test {

    public static void trackTrainData(UUID trainId) {
        Train train = Create.RAILWAYS.trains.get(trainId);
        Logger logger = Kuayue.LOGGER;
        if (train == null) {
            logger.warn("Train not found for trainId: {}", trainId);
            return;
        }
        logger.info("Tracking train data for trainId: {}", trainId);
        logger.info("Train speed: {}", train.speed);
        logger.info("Is Train moving backward: {}", train.currentlyBackwards);
        logger.info("Player Steering: {}", train.manualSteer);
        logger.info("");
    }

    public static boolean canMigrateTrains(Train trainA, Train trainB) {
        // NOTICE: 判断列车整体的一些基本数据
        // 两个列车必须都有效
        // 不能有列车处于脱轨状态
        // 两个列车所在的铁路图必须一样
        if (trainA.invalid || trainB.invalid) return false;
        if (trainA.derailed || trainB.derailed) return false;
        if (!Objects.equals(trainA.graph, trainB.graph)) return false;

        // NOTICE: 判断 dimensions
        // 这个判断确保两个车厢的 dimension 必须是一样的
        // 1. 都没有 level (null), 2. 都有 level 且 level 都一样.
        @Nullable Level level = null;
        boolean notFirst = false;
        for (Carriage carriage : trainA.carriages) {
            if (notFirst) {
                if (carriage.anyAvailableEntity() != null) {
                    if (carriage.anyAvailableEntity().level() != level) return false;
                } else if (level != null) return false;
            } else {
                level = carriage.anyAvailableEntity() != null ?
                        carriage.anyAvailableEntity().level() : null;
                notFirst = true;
            }
        }

        for (Carriage carriage : trainB.carriages) {
            if (carriage.anyAvailableEntity() != null) {
                if (carriage.anyAvailableEntity().level() != level) return false;
            } else if (level != null) return false;
        }

        // NOTICE: 返回 true
        return true;
    }


    public static void tryMigrateTrains(Train trainA, Train trainB,
                                        @NonNull Level defaultLevel,
                                        int spacing,
                                        boolean shouldReverseBCarriage,
                                        boolean clientSide) {
        List<Carriage> carriagesB = trainB.carriages;
        List<Carriage> carriagesA = trainA.carriages;
        int trainALength = carriagesA.size();
        List<Integer> carriageSpacingB = trainB.carriageSpacing;

        Carriage cA = carriagesA.get(carriagesA.size() - 1);
        cA.presentConductors = Couple.create(cA.presentConductors.getFirst(), true);
        Carriage cB = carriagesB.get(0);
        cB.presentConductors = Couple.create(true, cB.presentConductors.getSecond());

        carriagesB.forEach(carriage -> {
            carriage.setTrain(trainA);
            CarriageContraptionEntity entity = carriage.anyAvailableEntity();
            if (entity == null) return;
            entity.trainId = trainA.id;
            entity.carriageIndex += trainALength;
        });


        if (shouldReverseBCarriage) {
            ArrayList<Carriage> invertCarriageB =
                    new ArrayList<>(carriagesB.size());
            for (int i = carriagesB.size() - 1; i >= 0; i--) {
                invertCarriageB.add(carriagesB.get(i));
            }
            trainA.carriages.addAll(invertCarriageB);
        } else {
            trainA.carriages.addAll(carriagesB);
        }
        trainA.carriageSpacing.add(spacing);
        if (shouldReverseBCarriage) {
            ArrayList<Carriage> invertCarriageSpacingB =
                    new ArrayList<>(carriageSpacingB.size());
            for (int i = carriagesB.size() - 1; i >= 0; i--) {
                invertCarriageSpacingB.add(carriagesB.get(i));
            }
            trainA.carriages.addAll(invertCarriageSpacingB);
        } else {
            trainA.carriageSpacing.addAll(carriageSpacingB);
        }

        AccessorTrain accessorA = (AccessorTrain) trainA;
        AccessorTrain accessorB = (AccessorTrain) trainB;
        double[] stressA = accessorA.getStress();
        double[] stressB = accessorB.getStress();
        int stressALength = stressA.length;
        int stressBLength = stressB.length;
        double[] neoStress = new double[stressALength +
                                        stressBLength + 1];
        for (int i = 0; i < neoStress.length; i++) {
            if (i < stressALength) {
                neoStress[i] = stressA[i];
            } else if (i == stressALength) {
                neoStress[i] = 0;
            } else {
                if (shouldReverseBCarriage) {
                    neoStress[i] = stressB[neoStress.length - (i - stressALength)];
                } else {
                    neoStress[i] = stressB[i - stressALength];
                }
            }
        }
        accessorA.setStress(neoStress);

        if (clientSide) {
            CreateClient.RAILWAYS.removeTrain(trainB.id);
        } else {
            Create.RAILWAYS.trains.remove(trainB.id);
        }

//        if (!clientSide) {
//            ServerLevel level = (ServerLevel)
//                    (cA.anyAvailableEntity() != null ?
//                     cA.anyAvailableEntity().level :
//                     defaultLevel);
//            AllPackets.CHANNEL.boardcastToClients(
//                    new TrainMigrationSyncPacket(trainA.id, trainB.id),
//                    level, BlockPos.ZERO);
//        }
//        AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new TrainPacket(trainA, false));
//        AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new TrainPacket(trainA, true));
//        AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new TrainPacket(trainB, false));
    }


    public static boolean conductWithOtherTrains(Level level, Train train,
                                                 Carriage carriage,
                                                 float maxConductSpeed,
                                                 boolean clientSide) {
        if (train.derailed)
            return false;

        TravellingPoint trailingPoint = carriage.getTrailingPoint();
        TravellingPoint leadingPoint = carriage.getLeadingPoint();

        if (leadingPoint.node1 == null || trailingPoint.node1 == null)
            return false;
        ResourceKey<Level> dimension = leadingPoint.node1.getLocation().dimension;
        if (!dimension.equals(trailingPoint.node1.getLocation().dimension))
            return false;

        double speed = train.speed;
        TrackGraph graph = train.graph;
        Vec3 start = (speed < 0 ? trailingPoint : leadingPoint).getPosition(graph);
        Vec3 end = (speed < 0 ? leadingPoint : trailingPoint).getPosition(graph);

//        Pair<Carriage, Vec3> collision = findCollidingCarriage(level, train, start, end, dimension, 5f);
//        if (collision == null)
//            return false;
//        Pair<Train, Pair<Byte, Byte>>
//
//        Carriage colliedCarriage = collision.getFirst();
//        Train colliedTrain = colliedCarriage.train;
//
//        if (colliedTrain.carriages.isEmpty()) return false;
//        if (!canMigrateTrains(train, colliedTrain)) return false;
//        byte direction = isCarriageInEdge(colliedCarriage, trailingPoint, leadingPoint);
//        if (direction == 0) {
////            TrackGraph tg = train.graph;
////            TrackEdge edge = graph.getConnectionsFrom();
//            return false;
//        }
//        boolean isSameDirection = direction > 0;
//        double selfSpeedAbs = Math.abs(speed),
//               otherSpeedAbs = Math.abs(colliedTrain.speed);
//        double combinedSpeed = isSameDirection ?
//                Math.abs(selfSpeedAbs - otherSpeedAbs) :
//                selfSpeedAbs + otherSpeedAbs;
//
//        if (combinedSpeed > maxConductSpeed) {
//            return false;
//        }
//
//        Pair<Pair<Train, Boolean>, Pair<Train, Boolean>> leadingTrainPair =
//                getLeadingTrain(train, carriage, selfSpeedAbs,
//                        colliedTrain, colliedCarriage, otherSpeedAbs);
//        if (leadingTrainPair == null) return false;
//
//        int anchorDistance = getAnchorDistance(carriage, colliedCarriage);
//        if (anchorDistance < 0) return false;
//
//        tryMigrateTrains(
//                leadingTrainPair.getFirst().getFirst(),
//                leadingTrainPair.getSecond().getFirst(),
//                level, anchorDistance, leadingTrainPair.getSecond().getSecond(),
//                clientSide);
        return true;
    }

    public static int getAnchorDistance(Carriage self, Carriage collied) {
        CarriageBogey selfLeadingBogey = self.leadingBogey();
        CarriageBogey colliedLeadingBogey = collied.leadingBogey();
        CarriageBogey selfTrailingBogey = self.isOnTwoBogeys() ? self.trailingBogey() : null;
        CarriageBogey colliedTrailingBogey = collied.isOnTwoBogeys() ? collied.trailingBogey() : null;

        if (selfLeadingBogey == null || colliedLeadingBogey == null) return -1;
        if (selfLeadingBogey.getAnchorPosition() == null ||
            colliedLeadingBogey.getAnchorPosition() == null) return -1;
        boolean selfTrailingValid = selfTrailingBogey != null && selfTrailingBogey.getAnchorPosition() != null;
        boolean colliedTrailingValid = colliedTrailingBogey != null && colliedTrailingBogey.getAnchorPosition() != null;
        double leadingDistance = selfLeadingBogey.getAnchorPosition().distanceToSqr(colliedLeadingBogey.getAnchorPosition());
        double leadingTrailDistance = colliedTrailingValid ?
                selfLeadingBogey.getAnchorPosition().distanceToSqr(colliedTrailingBogey.getAnchorPosition()) : Double.MAX_VALUE;
        double trailLeadingDistance = selfTrailingValid ?
                colliedLeadingBogey.getAnchorPosition().distanceToSqr(selfTrailingBogey.getAnchorPosition()) : Double.MAX_VALUE;
        double TrailDistance = colliedTrailingValid && selfTrailingValid ?
                selfTrailingBogey.getAnchorPosition().distanceToSqr(colliedTrailingBogey.getAnchorPosition()) : Double.MAX_VALUE;
        return (int) Math.ceil(
                        Math.sqrt(Math.min(leadingDistance, Math.min(TrailDistance,
                                Math.min(leadingTrailDistance, trailLeadingDistance)))));
    }

    @Nullable
    public static Pair<Pair<Train, Boolean>, Pair<Train, Boolean>> getLeadingTrain(
            Train selfTrain, Carriage selfCarriage, double selfSpeedAbs,
            Train otherTrain, Carriage otherCarriage, double otherSpeedAbs)
    {
        Train leading = selfSpeedAbs >= otherSpeedAbs ? selfTrain : otherTrain;

        Train later = selfTrain == leading ? otherTrain : selfTrain;
        Carriage laterCart =  selfTrain == leading ? otherCarriage : selfCarriage;

        int carriageIndexInLater = later.carriages.indexOf(laterCart);
        if (carriageIndexInLater < 0) return null;
        if (carriageIndexInLater == 0) {
            return Pair.of(Pair.of(leading, false), Pair.of(later, false));
        } else if (carriageIndexInLater == later.carriages.size() - 1) {
            return Pair.of(Pair.of(leading, false), Pair.of(later, true));
        } else {
            return null;
        }
    }

    public static byte isCarriageInEdge(Carriage carriage,
                                           TravellingPoint trailing,
                                           TravellingPoint leading) {
        TravellingPoint carriageLeading = carriage.getLeadingPoint();
        TravellingPoint carriageTrailing = carriage.getTrailingPoint();
        boolean selfAtSameEdge = travellingPointEquals(leading, trailing);
        boolean carriageAtSameEdge = travellingPointEquals(carriageLeading, carriageTrailing);
        if (selfAtSameEdge && carriageAtSameEdge) {
            if (carriageLeading.node1 == leading.node1 &&
            carriageLeading.node2 == leading.node2) return 1;
            if (carriageLeading.node1 == leading.node2 &&
            carriageLeading.node2 == leading.node1) return -1;
            return 0;
        }
        if (selfAtSameEdge ^ carriageAtSameEdge) {
            TravellingPoint p = selfAtSameEdge ? leading : carriageLeading;
            TravellingPoint pLeading = selfAtSameEdge ? carriageLeading : leading;
            TravellingPoint pTrailing = selfAtSameEdge ? carriageTrailing : trailing;
            if ((p.node1 == pLeading.node1 && p.node2 == pLeading.node2) ||
                    (p.node1 == pTrailing.node1 && p.node2 == pTrailing.node1)) return 1;
            if ((p.node1 == pLeading.node2 && p.node2 == pLeading.node1) ||
                    (p.node1 == pTrailing.node2 && p.node2 == pTrailing.node1)) return -1;
            return 0;
        }
        if (travellingPointEquals(leading, carriageLeading) ||
            travellingPointEquals(trailing, carriageTrailing)) return 1;
        if (travellingPointEquals(leading, carriageTrailing) ||
            travellingPointEquals(trailing, carriageLeading)) return -1;
        return 0;
    }

    public static boolean travellingPointEquals(TravellingPoint a, TravellingPoint b) {
        if (a == null || b == null) return false;
        return (Objects.equals(a.node1, b.node1) && Objects.equals(a.node2, b.node2)) ||
                (Objects.equals(a.node1, b.node2) && Objects.equals(a.node2, b.node1));
    }


    public static @NotNull Pair<Train, Pair<Byte, Byte>> getConductableTrain(
            Train train, boolean isClientSide
    ) {
//        GlobalRailwayManager manager = isClientSide ? CreateClient.RAILWAYS : Create.RAILWAYS;
//        for (Map.Entry<UUID, Train> entry : manager.trains.entrySet()) {
//            Train t = entry.getValue();
//            if (t.equals(train)) continue;
//            Pair<Byte, Byte> collideResult = ConductorHelper.isTwoTrainConductorCollide(train, t, .125f);
//            if (!ConductorHelper.isValidCollide(collideResult)) continue;
//            return Pair.of(t, collideResult);
//        }
        return Pair.of(null, null);
    }
}
