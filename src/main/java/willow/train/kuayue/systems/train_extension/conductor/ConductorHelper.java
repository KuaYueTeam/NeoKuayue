package willow.train.kuayue.systems.train_extension.conductor;

import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.trains.GlobalRailwayManager;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.entity.*;
import com.simibubi.create.foundation.utility.Couple;
import kasuga.lib.core.util.Envs;
import kasuga.lib.core.util.data_type.Pair;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.event.server.TrainCouplerPostDivideEvent;
import willow.train.kuayue.event.server.TrainCouplerPostMergeEvent;
import willow.train.kuayue.event.server.TrainCouplerPreDivideEvent;
import willow.train.kuayue.event.server.TrainCouplerPreMergeEvent;
import willow.train.kuayue.initial.AllSounds;
import willow.train.kuayue.mixins.mixin.AccessorTrain;
import willow.train.kuayue.systems.train_extension.CarriageAdditionalData;
import willow.train.kuayue.systems.train_extension.TrainAdditionalData;
import willow.train.kuayue.systems.train_extension.TrainExtensionSystem;
import willow.train.kuayue.systems.train_extension.conductor.registry.ConductorCandidateRegistry;
import willow.train.kuayue.systems.train_extension.conductor.schedule_handle.ScheduleHandlerProvider;

import java.util.*;

import static willow.train.kuayue.utils.CarriageUtil.*;

public class ConductorHelper {

    // ------------------------------- records ---------------------------------

    public record TrainCollideResult(byte aFlag, byte bFlag, float spacing) {
        public static TrainCollideResult invalid() {
            return new TrainCollideResult((byte) 0, (byte) 0, -1);
        }
    }

    public record CollidedConnectors(
            byte aFlag, Conductable conductorA,
            byte bFlag, Conductable conductorB,
            float spacing
    ) {
        public static CollidedConnectors invalid() {
            return new CollidedConnectors((byte) 0, null, (byte) 0, null, -1);
        }

        public boolean isAHead() {
            return aFlag > 0;
        }

        public boolean isBHead() {
            return bFlag > 0;
        }
    }

    public record TrainSortResult(
            Train loco, Conductable locoConductor,
            Train carriages, Conductable carriageConductor,
            boolean isLocoHead, boolean shouldReverseCarriages
    ) {}

    public record TrainMergeRequest(
            Train loco, Train carriages,
            boolean shouldReverseCarriages,
            boolean isLocoHead, float spacing,
            boolean clientSide
    ) {}

    public record TrainDivideRequest(
            Train loco,
            UUID newTrainUUID,
            int carriageIndex
    ) {}

    public record MergeEventContext(
        UUID locoId,
        UUID carriageId,
        boolean isLocoHead,
        boolean isCarriageTail,
        float spacing
    ) {}

    @Getter
    public static class MergeContext {
        final Train loco;
        final Train carriages;
        final boolean isLocoHead;
        final boolean isCarriageTail;
        final boolean isClientSide;
        final float spacing;
        final Pair<Pair<Conductable, Vec3>, Pair<Conductable, Vec3>> locoConductors;
        final Pair<Pair<Conductable, Vec3>, Pair<Conductable, Vec3>> carriageConductors;

        final Vec3 effectPos;
        final float oldLocoSpeed;
        final float oldCarriageSpeed;

        MergeContext(
                Train loco,
                Train carriages,
                boolean isLocoHead,
                boolean isCarriageTail,
                boolean isClientSide,
                float spacing,
                final Pair<Pair<Conductable, Vec3>, Pair<Conductable, Vec3>> locoConductors,
                final Pair<Pair<Conductable, Vec3>, Pair<Conductable, Vec3>> carriageConductors
        ) {
            this.loco = loco;
            this.carriages = carriages;
            this.isLocoHead = isLocoHead;
            this.isCarriageTail = isCarriageTail;
            this.isClientSide = isClientSide;
            this.spacing = spacing;
            this.locoConductors = locoConductors;
            this.carriageConductors = carriageConductors;

            this.oldLocoSpeed = (float) loco.speed;
            this.oldCarriageSpeed = (float) carriages.speed;

            if (isClientSide) {
                this.effectPos = null;
            } else {
                if (isLocoHead) {
                    effectPos = locoConductors.getFirst().getSecond();
                } else {
                    effectPos = locoConductors.getSecond().getSecond();
                }
            }
        }
    }

    private record MergeResult(
            List<Carriage> carriages,
            List<Integer> spacing,
            double[] stress,
            boolean doubleEnded,
            float newSpeed,
            Map<Integer, Boolean> carriageRemapStatus
    ) {}

    @Getter
    public static class DivideContext {
        final Train train;
        final UUID newTrain;
        final int carriageIndex;
        final boolean isClientSide;

        //server side only
        Conductable locoTail = null;
        Conductable carriageHead = null;
        Vec3 locoTailPos = null;
        Vec3 carriageHeadPos = null;

        DivideContext(
                Train train,
                UUID newTrain,
                int carriageIndex,
                boolean isClientSide
        ) {
            this.train = train;
            this.newTrain = newTrain;
            this.carriageIndex = carriageIndex;
            this.isClientSide = isClientSide;

            initConductable();
        }

        void initConductable() {
            if(this.isClientSide) return;
            TrainAdditionalData locoData = Kuayue.TRAIN_EXTENSION.get(train.id);
            if(locoData == null) return;
            locoTail = locoData.getConductorAt(new ConductorLocation(train.id, carriageIndex, false));
            carriageHead = locoData.getConductorAt(new ConductorLocation(train.id, carriageIndex + 1, true));
            locoTailPos = getConductorPosition(train.carriages.get(carriageIndex), locoTail, false);
            carriageHeadPos = getConductorPosition(train.carriages.get(carriageIndex + 1), carriageHead, true);
        }

        boolean hasValidConductable() {
            return locoTail != null && carriageHead != null;
        }
    }

    private record DivideResult(
            List<Carriage> locoCarts,
            List<Integer> locoSpacing,
            List<Carriage> carriageCarts,
            List<Integer> carriageSpacing,
            double[] locoStress,
            double[] carriageStress,
            float newLocoSpeed,
            float newCarriageSpeed
    ) {}

    // ------------------------------- functions ---------------------------------

    public static @Nullable Pair<ConductorProvider, Vec2> getConductorBlock(
            @NonNull BlockPos bogeyPos,
            @NonNull Map<BlockPos, StructureTemplate.StructureBlockInfo> blocks,
            @NonNull Direction assembleDirection,
            boolean isLeading) {
        BlockPos posCache = bogeyPos;
        Pair<ConductorProvider, Vec2> provider = null;
        int distance = 0;
        while (true) {
            posCache = posCache.relative(
                    isLeading ? assembleDirection.getOpposite() : assembleDirection
            );
            distance++;
            if (!blocks.containsKey(posCache) && !blocks.containsKey(posCache.above())) {
                break;
            };
            StructureTemplate.StructureBlockInfo below = blocks.get(posCache);
            StructureTemplate.StructureBlockInfo above = blocks.get(posCache.above());
            if( below == null && above == null) continue;
            if(above != null) {
                ConductorProvider p = ConductorCandidateRegistry.getProvider(above.state());
                if(p != null) provider = Pair.of(p, new Vec2(distance, posCache.getY()));
            }
            if(below != null) {
                ConductorProvider p = ConductorCandidateRegistry.getProvider(below.state());
                if(p != null) provider = Pair.of(p, new Vec2(distance, posCache.getY()));
            }
        }
        return provider;
    }

    public static boolean isValidCollide(TrainCollideResult pair) {
        return pair.aFlag != 0 && pair.bFlag != 0;
    }

    public static boolean isValidCollide2(CollidedConnectors pair) {
        return pair.aFlag != 0 && pair.bFlag != 0;
    }

    public static TrainCollideResult isTwoTrainConductorCollide(
            Train trainA, Train trainB, float distance
    ) {
        distance *= distance;
        Pair<Pair<Conductable, Vec3>, Pair<Conductable, Vec3>> conductorA =
                ConductorHelper.getConductorPosition(trainA);
        Pair<Pair<Conductable, Vec3>, Pair<Conductable, Vec3>> conductorB =
                ConductorHelper.getConductorPosition(trainB);
        if (conductorA.getFirst() == null || conductorA.getSecond() == null ||
            conductorB.getFirst() == null || conductorB.getSecond() == null) {
            return TrainCollideResult.invalid();
        }
        Vec3 leadingA = conductorA.getFirst().getSecond();
        Conductable leadingAConductor = conductorA.getFirst().getFirst();
        Vec3 leadingB = conductorB.getFirst().getSecond();
        Conductable leadingBConductor = conductorB.getFirst().getFirst();
        Vec3 trailingA = conductorA.getSecond().getSecond();
        Conductable trailingAConductor = conductorA.getSecond().getFirst();
        Vec3 trailingB = conductorB.getSecond().getSecond();
        Conductable trailingBConductor = conductorB.getSecond().getFirst();

        if ((leadingA == null && trailingA == null) || (leadingB == null && trailingB == null)) {
            return TrainCollideResult.invalid();
        }
        //A head - B head
        if (leadingA != null && leadingB != null) {
            leadingA = leadingA.subtract(0, leadingA.y(), 0);
            leadingB = leadingB.subtract(0, leadingB.y(), 0);
            if (leadingA.distanceToSqr(leadingB) < distance)
                return new TrainCollideResult(
                        (byte) 1, (byte) 1,
                        leadingAConductor.getTotalOffset() + leadingBConductor.getTotalOffset()
                );
        }

        //A head - B tail
        if(leadingA != null && trailingB != null) {
            leadingA = leadingA.subtract(0, leadingA.y(), 0);
            trailingB = trailingB.subtract(0, trailingB.y(), 0);
            if (leadingA.distanceToSqr(trailingB) < distance) {
                if(Kuayue.TRAIN_EXTENSION.conductorsCoolingDown.containsKey(Couple.create(trailingBConductor.getLoc(), leadingAConductor.getLoc()))) {
                    return TrainCollideResult.invalid();
                }
                return new TrainCollideResult(
                        (byte) 1, (byte) -1,
                        leadingAConductor.getTotalOffset() + trailingBConductor.getTotalOffset()
                );
            }
        }

        //A tail - B head
        if (trailingA != null && leadingB != null) {
            trailingA = trailingA.subtract(0, trailingA.y(), 0);
            leadingB = leadingB.subtract(0, leadingB.y(), 0);
            if (trailingA.distanceToSqr(leadingB) < distance) {
                if(Kuayue.TRAIN_EXTENSION.conductorsCoolingDown.containsKey(Couple.create(trailingAConductor.getLoc(), leadingBConductor.getLoc()))) {
                    return TrainCollideResult.invalid();
                }
                return new TrainCollideResult(
                        (byte) -1, (byte) 1,
                        trailingAConductor.getTotalOffset() + leadingBConductor.getTotalOffset()
                );
            }
        }

        //A tail - B tail
        if(trailingA != null && trailingB != null) {
            trailingA = trailingA.subtract(0, trailingA.y(), 0);
            trailingB = trailingB.subtract(0, trailingB.y(), 0);
            if (trailingA.distanceToSqr(trailingB) < distance) {
                return new TrainCollideResult(
                        (byte) -1, (byte) -1,
                        trailingAConductor.getTotalOffset() + trailingBConductor.getTotalOffset()
                );
            }
        }

        return TrainCollideResult.invalid();
    }

    public static CollidedConnectors getCollidedConnector(
            Train trainA, Train trainB, float distance
    ) {
        TrainCollideResult result = isTwoTrainConductorCollide(trainA, trainB, distance);
        if (!isValidCollide(result)) {
            return CollidedConnectors.invalid();
        }
        TrainAdditionalData dataA = Kuayue.TRAIN_EXTENSION.get(trainA.id);
        TrainAdditionalData dataB = Kuayue.TRAIN_EXTENSION.get(trainB.id);
        Objects.requireNonNull(dataA);
        Objects.requireNonNull(dataB);
        Conductable a = dataA.getConductor(result.aFlag);
        Conductable b = dataB.getConductor(result.bFlag);
        return new CollidedConnectors(
                result.aFlag(), a,
                result.bFlag(), b,
                result.spacing()
        );
    }

    public static @NotNull Pair<Pair<Conductable, Vec3>, Pair<Conductable, Vec3>> getConductorPosition(Train train) {
        if (!Kuayue.TRAIN_EXTENSION.contains(train.id)) return Pair.of(null, null);
        TrainAdditionalData data = Kuayue.TRAIN_EXTENSION.get(train.id);
        Pair<Conductable, Conductable> sidedConductor = data.getSidedConductors();
        if (sidedConductor.getFirst() == null && sidedConductor.getSecond() == null)
            return Pair.of(null, null);
        List<Carriage> carriages = train.carriages;
        Vec3 firstPos = getConductorPosition(
                carriages.get(0), sidedConductor.getFirst(), true);
        Vec3 secondPos = getConductorPosition(
                carriages.get(carriages.size() - 1), sidedConductor.getSecond(), false);
        return Pair.of(Pair.of(sidedConductor.getFirst(), firstPos),
                Pair.of(sidedConductor.getSecond(), secondPos));
    }

    public static @Nullable Vec3 getConductorPosition(
                                            Carriage carriage,
                                            Conductable conductor,
                                            boolean isLeading
    ) {
        if (conductor == null) return null;
        CarriageBogey bogey = carriage.isOnTwoBogeys() ?
                carriage.bogeys.get(isLeading) : carriage.leadingBogey();
        Vec3 position = bogey.getAnchorPosition();
        if (position == null) return null;
        return position.add(getCarriageDirection(carriage).scale(
                (conductor.getTotalOffset()) * (isLeading ? 1f : -1f))
        );
    }

    public static float getConductorFlatDistToSqr(Couple<ConductorLocation> couple, TrainExtensionSystem.ConductorCDInfo info) {
        Train trainA = Create.RAILWAYS.trains.get(couple.getFirst().getTrainId());
        Train trainB = Create.RAILWAYS.trains.get(couple.getSecond().getTrainId());
        if(trainA == null || trainB == null) return -1;

        Conductable first = info.conductorA;
        Conductable second = info.conductorB;

        Carriage firstCarriage = trainA.carriages.get(first.carriage());
        Carriage secondCarriage = trainB.carriages.get(second.carriage());

        Vec3 firstPos = getConductorPosition(firstCarriage, first, couple.getFirst().isLeading());
        Vec3 secondPos = getConductorPosition(secondCarriage, second, couple.getSecond().isLeading());
        if (firstPos == null || secondPos == null) return -1;

        firstPos = firstPos.subtract(0, firstPos.y(), 0);
        secondPos = secondPos.subtract(0, secondPos.y(), 0);

        return (float) firstPos.distanceToSqr(secondPos);
    }

    /**
     *
     * @param trainA 参与碰撞的列车 A
     * @param trainB 参与碰撞的列车 B
     * @param e 恢复系数e
     * @return 碰撞后两车各自的速度
     */
    public static @Nullable Pair<Float, Float> momentumExchange(
            Train trainA, Train trainB, float e) {
        if (!Kuayue.TRAIN_EXTENSION.contains(trainA.id) ||
            !Kuayue.TRAIN_EXTENSION.contains(trainB.id)) {
            return null;
        }
        TrainAdditionalData dataA = Kuayue.TRAIN_EXTENSION.get(trainA.id);
        TrainAdditionalData dataB = Kuayue.TRAIN_EXTENSION.get(trainB.id);
        float totalWeightA = dataA.totalWeight();
        float totalWeightB = dataB.totalWeight();
        float totalWeightAB =  totalWeightA + totalWeightB;

        float speedA = (float) trainA.speed;
        float speedB = (float) trainB.speed;
        float deltaSpeed = speedA - speedB;

        return Pair.of(
                speedA - totalWeightA / totalWeightAB * (1f + e) * deltaSpeed,
                speedB - totalWeightB / totalWeightAB * (1f + e) * deltaSpeed
        );
    }

    public static @Nullable TrainSortResult sortTrains(
            Conductable conductorA, boolean isAHead,
            Conductable conductorB, boolean isBHead,
            boolean clientSide
    ) {
        GlobalRailwayManager manager = clientSide ?
                CreateClient.RAILWAYS : Create.RAILWAYS;
        Train trainA = manager.trains.get(conductorA.train());
        Train trainB = manager.trains.get(conductorB.train());
        if (trainA == null || trainB == null) return null;

        // 速度太小也不行
        if (Math.abs(trainA.speed) - Math.abs(trainB.speed) < .01f &&
            Math.abs(trainA.speed) < .03f) return null;

        // 根据两个连接器的优先级确定顺序，优先级越小越优先作为 conduct 后的 train.
        TrainSortResult resultAB = new TrainSortResult(
                trainA, conductorA,
                trainB, conductorB,
                isAHead, !isBHead);

        TrainSortResult resultBA = new TrainSortResult(
                trainB, conductorB,
                trainA, conductorA,
                isBHead, !isAHead
        );

        if (conductorA.getPriority() < conductorB.getPriority()) {
            return resultAB;
        } else if (conductorA.getPriority() > conductorB.getPriority()) {
            return resultBA;
        }

        // 如果一头一尾，则连接器在尾部那一辆车作为合并后的 train
        if (isAHead ^ isBHead) {
            return isBHead ? resultAB : resultBA;
        }

        // 优先速度绝对值大的一方
        if (Math.abs(trainA.speed) < Math.abs(trainB.speed)) {
            return resultBA;
        }
        return resultAB;
    }

    public static boolean shouldReverseCarriage(
            Conductable conductable, boolean flag
    ) {
        if (conductable.carriage() > 0) return true;
        return !flag;
    }

    public static boolean mergeTrains(
            Train loco, Train carriages,
            boolean isCarriageTail,
            boolean isLocoHead, float spacing,
            boolean isClientSide
    ) {
        try {
            MergeContext context = new MergeContext(
                    loco,
                    carriages,
                    isLocoHead,
                    isCarriageTail,
                    isClientSide,
                    spacing,
                    isClientSide ? null : getConductorPosition(loco),
                    isClientSide ? null : getConductorPosition(carriages)
            );
            MergeEventContext eventContext = new MergeEventContext(loco.id, carriages.id, isLocoHead, isCarriageTail, spacing);

            if(!isClientSide) {
                boolean shouldContinue = broadcastPreMerge(context, eventContext);
                if(!shouldContinue) return false;
            }

            MergeResult result = calculateMergeResult(context);

            commitMergeState(context, result);

            postProcessMerge(context, loco, carriages);

            if(!isClientSide) {
                broadcastPostMerge(context, eventContext);
            }

            return true;
        } catch (Exception e) {
            Kuayue.LOGGER.error("Error while merging trains: locoId={}, carriageId={}", loco.id, carriages.id, e);
            Kuayue.LOGGER.error("Current side: {}", isClientSide ? "Client" : "Server");
            e.printStackTrace();
            return false;
        }
    }

    private static MergeResult calculateMergeResult(MergeContext context) {
        Train loco = context.loco;
        Train carriages = context.carriages;

        List<Carriage> locoCarts = new ArrayList<>(loco.carriages);
        List<Integer> locoSpacing = new ArrayList<>(loco.carriageSpacing);
        List<Carriage> carriageCarts = new ArrayList<>(carriages.carriages);
        List<Integer> cartSpacing = new ArrayList<>(carriages.carriageSpacing);

        double[] locoStress = ((AccessorTrain) loco).getStress();
        double[] cartStress = ((AccessorTrain) carriages).getStress();
        double[] neoStress = new double[locoStress.length + cartStress.length + 1];

        Map<Integer, Boolean> carriageRemapStatus = new HashMap<>();

        //头-头或尾-尾情况需要反转
        if(context.isLocoHead ^ context.isCarriageTail) {
            carriageCarts.forEach(c -> {
                if (context.isClientSide) {
                    CarriageContraptionEntity cce = c.anyAvailableEntity();
                    if (cce == null) return; // carriage is out of view
                }
                reverseBogeys(c);
                boolean success = remapCarriage(c, context.isClientSide);
                carriageRemapStatus.put(c.id, !success);
            });
            Collections.reverse(carriageCarts);
            Collections.reverse(cartSpacing);
        }

        if (context.isLocoHead) {
            locoCarts.addAll(0, carriageCarts);
            locoSpacing.addAll(0, cartSpacing);
            locoSpacing.add(cartSpacing.size(), (int) Math.floor(context.spacing));
            copyStress(cartStress, locoStress, neoStress);
        } else {
            locoCarts.addAll(carriageCarts);
            locoSpacing.add((int) Math.floor(context.spacing));
            locoSpacing.addAll(cartSpacing);
            copyStress(locoStress, cartStress, neoStress);
        }

        // 处理列车连接后的速度
        Pair<Float, Float> speedPair = momentumExchange(loco, carriages, 0f);
        float newSpeed = speedPair != null ? speedPair.getFirst() : context.oldLocoSpeed;

        boolean doubleEnded = loco.doubleEnded || carriages.doubleEnded;

        return new MergeResult(
                locoCarts,
                locoSpacing,
                neoStress,
                doubleEnded,
                newSpeed,
                carriageRemapStatus
        );
    }

    private static void commitMergeState(MergeContext context, MergeResult result) {
        Train loco = context.loco;
        Train carriages = context.carriages;

        loco.carriages = result.carriages;
        loco.carriageSpacing = result.spacing;
        ((AccessorTrain) loco).setStress(result.stress);
        loco.doubleEnded = result.doubleEnded;
        loco.speed = result.newSpeed;

        if(!context.isClientSide) {
            TrainAdditionalData trainAdditionalData = Kuayue.TRAIN_EXTENSION.get(carriages.id);
            if(trainAdditionalData != null) {
                for(int i = 0; i < carriages.carriages.size(); i++) {
                    Carriage c = carriages.carriages.get(i);
                    CarriageAdditionalData carriageAdditionalData = trainAdditionalData.getCarriages().get(i);
                    carriageAdditionalData.shouldRemap = result.carriageRemapStatus.getOrDefault(c.id, false);
                }
            }
        }

        updateCarriageReferences(loco);
    }

    private static void postProcessMerge(MergeContext context, Train loco, Train carriages) {
        if (context.isClientSide) {
            CreateClient.RAILWAYS.removeTrain(carriages.id);
        } else {
            ScheduleHandlerProvider.get().handleMerge(context);
            mergeTrainExtensionData(loco, carriages, context.isCarriageTail, context.isLocoHead);
            Create.RAILWAYS.removeTrain(carriages.id);
            Entity entity = loco.carriages.get(0).anyAvailableEntity();
            playEffects(context.effectPos, entity);
        }

        loco.collectInitiallyOccupiedSignalBlocks();
    }

    private static void updateCarriageReferences(Train train) {
        for (int i = 0; i < train.carriages.size(); i++) {
            Carriage c = train.carriages.get(i);
            c.setTrain(train);
            CarriageContraptionEntity entity = c.anyAvailableEntity();
            if (entity != null) {
                entity.trainId = train.id;
                entity.carriageIndex = i;
                entity.setCarriage(c);
            }
            c.presentConductors = Couple.create(i > 0, i < train.carriages.size() - 1);
        }
    }

    //boolean: continue
    private static boolean broadcastPreMerge(MergeContext context, MergeEventContext eventContext) {
        if(context.isClientSide) return true;
        TrainCouplerPreMergeEvent preEvent = new TrainCouplerPreMergeEvent(
                eventContext,
                Pair.of(context.isLocoHead ? context.locoConductors.getFirst() : context.locoConductors.getSecond(),
                        context.isCarriageTail ? context.carriageConductors.getSecond() : context.carriageConductors.getFirst()),
                context.oldLocoSpeed,
                context.oldCarriageSpeed);

        MinecraftForge.EVENT_BUS.post(preEvent);
        return !preEvent.isCanceled();
    }

    private static void broadcastPostMerge(MergeContext context, MergeEventContext eventContext) {
        if(context.isClientSide) return;
        MinecraftForge.EVENT_BUS.post(new TrainCouplerPostMergeEvent(
                eventContext,
                Pair.of(context.isLocoHead ? context.locoConductors.getFirst() : context.locoConductors.getSecond(),
                        context.isCarriageTail ? context.carriageConductors.getSecond() : context.carriageConductors.getFirst()),
                context.oldLocoSpeed,
                context.oldCarriageSpeed,
                (float) context.loco.speed
        ));
    }

    // here carriageIndex represents the carriage that coupler is on
    // assume that this carriage has a coupler
    public static boolean canDivideTrain(@NonNull Train train, int carriageIndex, boolean isLeading) {
        if(carriageIndex < 0 || carriageIndex > train.carriages.size() - 1) return false;

        TrainAdditionalData trainData = Kuayue.TRAIN_EXTENSION.get(train.id);
        if(trainData == null) return false;
        if(trainData.getCarriages().size() != train.carriages.size()) return false;

        if(isLeading) {
            int frontCarriageIndex = carriageIndex - 1;
            if(frontCarriageIndex < 0) return false; // no front carriage
            CarriageAdditionalData carriageData = trainData.getCarriages().get(frontCarriageIndex);
            return carriageData.getSecondConductor() != null; //front has second, this has first
        } else {
            int backCarriageIndex = carriageIndex + 1;
            if(backCarriageIndex >= train.carriages.size()) return false; // no back carriage
            CarriageAdditionalData carriageData = trainData.getCarriages().get(backCarriageIndex);
            return carriageData.getFirstConductor() != null; //this has first, back has second
        }
    }

    //规定：主车在前，从车在后，从index车厢后方分开
    public static boolean divideTrains(
            Train loco,
            UUID newTrainUUID,
            int carriageIndex,
            boolean isClientSide
    ) {
        if(loco == null) return false;
        if(carriageIndex < 0 || carriageIndex >= loco.carriages.size() - 1) return false;

        try {
            DivideContext context = new DivideContext(
                    loco,
                    newTrainUUID,
                    carriageIndex,
                    isClientSide
            );
            //pre event
            if(!isClientSide) {
                if(!context.hasValidConductable()) return false;
                boolean shouldContinue = broadcastPreDivide(context);
                if(!shouldContinue) return true;
            }

            DivideResult result = calculateDivideResult(context);

            Train carriages = commitDivideState(context, result);

            updateCarriageReferences(loco);
            updateCarriageReferences(carriages);

            if(isClientSide) {
                CreateClient.RAILWAYS.addTrain(carriages);
            } else {
                Create.RAILWAYS.addTrain(carriages);
            }

            postProcessDivide(context, loco, carriages);

            if(!isClientSide) {
                broadcastPostDivide(context);
            }

            return true;
        } catch (Exception e) {
            Kuayue.LOGGER.error("Error while dividing train: locoId={}, carriageIndex={}", loco.id, carriageIndex, e);
            e.printStackTrace();
            return false;
        }
    }

    //boolean: shouldContinue
    private static boolean broadcastPreDivide(DivideContext context) {
        if(context.isClientSide) return true;
        TrainCouplerPreDivideEvent event = new TrainCouplerPreDivideEvent(
                context.train.id,
                context.carriageIndex,
                (float) context.train.speed,
                Pair.of(
                        Pair.of(context.locoTail, context.locoTailPos),
                        Pair.of(context.carriageHead, context.carriageHeadPos)
                )
        );
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }

    private static void broadcastPostDivide(DivideContext context) {
        if(context.isClientSide) return;
        TrainCouplerPostDivideEvent event = new TrainCouplerPostDivideEvent(
                context.train.id,
                context.newTrain,
                context.carriageIndex,
                (float) context.train.speed,
                Pair.of(
                        Pair.of(context.locoTail, context.locoTailPos),
                        Pair.of(context.carriageHead, context.carriageHeadPos)
                )
        );
        MinecraftForge.EVENT_BUS.post(event);
    }

    private static DivideResult calculateDivideResult(DivideContext context) {
        Train train = context.train;
        int carriageIndex = context.carriageIndex;

        List<Carriage> locoCarts = new ArrayList<>(train.carriages.subList(0, carriageIndex + 1));
        List<Carriage> carriageCarts = new ArrayList<>(train.carriages.subList(carriageIndex + 1, train.carriages.size()));
        List<Integer> locoSpacing = new ArrayList<>(train.carriageSpacing.subList(0, carriageIndex));
        List<Integer> carriageSpacing = new ArrayList<>(train.carriageSpacing.subList(carriageIndex + 1, train.carriageSpacing.size()));

        double[] locoStress = new double[carriageIndex];
        double[] carriageStress = new double[train.carriages.size() - carriageIndex - 1];
        double[] fullStress = ((AccessorTrain) train).getStress();

        for(int i = 0; i < fullStress.length; i++){
            if(i < carriageIndex){
                locoStress[i] = fullStress[i];
            } else if(i > carriageIndex){
                carriageStress[i - carriageIndex - 1] = fullStress[i];
            }
        }

        float newLocoSpeed = (float) train.speed;
        float newCarriageSpeed = (float) train.speed;

        return new DivideResult(
                locoCarts,
                locoSpacing,
                carriageCarts,
                carriageSpacing,
                locoStress,
                carriageStress,
                newLocoSpeed,
                newCarriageSpeed
        );
    }

    private static Train commitDivideState(DivideContext context, DivideResult result) {
        Train loco = context.train;
        loco.doubleEnded = isDoubleEnded(result.locoCarts);
        loco.carriages = result.locoCarts;
        loco.carriageSpacing = result.locoSpacing;
        ((AccessorTrain) loco).setStress(result.locoStress);

        Train carriages = new Train(
                context.newTrain,
                loco.owner,
                loco.graph,
                result.carriageCarts,
                result.carriageSpacing,
                isDoubleEnded(result.carriageCarts)
        );
        ((AccessorTrain) carriages).setStress(result.carriageStress);

        carriages.speed = loco.speed;
        carriages.throttle = loco.throttle;

        return carriages;
    }

    private static void postProcessDivide(DivideContext context, Train loco, Train carriages) {
        if(!context.isClientSide) {
            ScheduleHandlerProvider.get().handleDivide(context);
            divideTrainExtensionData(loco, carriages, context.carriageIndex);
            TrainExtensionSystem.ConductorCDInfo info = new TrainExtensionSystem.ConductorCDInfo(context.locoTail, context.carriageHead);
            Kuayue.TRAIN_EXTENSION.conductorsCoolingDown.put(
                    Couple.create(context.locoTail.getLoc(), context.carriageHead.getLoc()), info
            );
        }

        loco.collectInitiallyOccupiedSignalBlocks();
        carriages.collectInitiallyOccupiedSignalBlocks();
    }

    private static void copyStress(double[] locoStress, double[] cartStress, double[] neoStress) {
        System.arraycopy(locoStress, 0, neoStress, 0, locoStress.length);
        neoStress[locoStress.length] = 0;
        for (int i = 0; i < cartStress.length; i++) {
            neoStress[locoStress.length + 1 + i] = cartStress[i];
        }
    }

    public static void playEffects(Vec3 effectPos, Entity entity) {
        if(entity == null || effectPos == null) return;
        if(effectPos.equals(Vec3.ZERO)) return;

        SoundEvent sound = AllSounds.TRAIN_COUPLER_SOUND.getSoundEvent();
        entity.level().playSound(null, BlockPos.containing(effectPos), sound, entity.getSoundSource(), 0.2f, 1.0f);
        ((ServerLevel) entity.level()).sendParticles(ParticleTypes.CRIT, effectPos.x, effectPos.y, effectPos.z,
                20, 0.2, 0.2, 0.2,0.8);
    }

    public static void mergeTrainExtensionData(
            Train loco, Train carriages,
            boolean isCarriageTail, boolean isLocoHead) {
        TrainAdditionalData locoData = Kuayue.TRAIN_EXTENSION.get(loco.id);
        TrainAdditionalData carriageData = Kuayue.TRAIN_EXTENSION.get(carriages.id);
        if (locoData == null || carriageData == null) return;

        if(isLocoHead ^ isCarriageTail) {
            carriageData.reverse(carriages);
        }
        if (isLocoHead) {
            locoData.getCarriages().addAll(0, carriageData.getCarriages());
        } else {
            locoData.getCarriages().addAll(carriageData.getCarriages());
        }
        locoData.reIndexAll(loco);
        locoData.updateInternalConnections();
        locoData.updateConductorMap();
        Kuayue.TRAIN_EXTENSION.syncChange(locoData);
        Kuayue.TRAIN_EXTENSION.remove(carriages.id);
        Kuayue.TRAIN_EXTENSION.syncRemove(carriages.id);
    }

    public static void divideTrainExtensionData(Train loco, Train carriages, int carriageIndex) {
        TrainAdditionalData locoData = Kuayue.TRAIN_EXTENSION.get(loco.id);
        if(locoData == null) return;
        List<CarriageAdditionalData> allCarriages = locoData.getCarriages();
        if(carriageIndex < 0 || carriageIndex >= allCarriages.size() - 1) return;

        List<CarriageAdditionalData> newLocoCarriages = new ArrayList<>(allCarriages.subList(0, carriageIndex + 1));
        List<CarriageAdditionalData> newCarriageCarriages = new ArrayList<>(allCarriages.subList(carriageIndex + 1, allCarriages.size()));

        locoData.getCarriages().clear();
        locoData.getCarriages().addAll(newLocoCarriages);
        locoData.reIndexAll(loco);
        locoData.updateInternalConnections();
        locoData.updateConductorMap();

        TrainAdditionalData carriageData = new TrainAdditionalData(carriages);
        carriageData.getCarriages().addAll(newCarriageCarriages);
        carriageData.reIndexAll(carriages);
        carriageData.updateInternalConnections();
        carriageData.updateConductorMap();
        Kuayue.TRAIN_EXTENSION.add(carriageData);

        Kuayue.TRAIN_EXTENSION.syncChange(locoData);
        Kuayue.TRAIN_EXTENSION.syncChange(carriageData);
    }


    public @Nullable BlockPos collectSecondBogeyPos(CarriageContraption contraption) {
        Map<BlockPos, StructureTemplate.StructureBlockInfo> infos = contraption.getBlocks();
        BlockPos posCache = BlockPos.ZERO;
        while (true) {
            posCache = posCache.relative(contraption.getAssemblyDirection());
            if (!infos.containsKey(posCache)) break;
            StructureTemplate.StructureBlockInfo info = infos.get(posCache);
            if (info.state().getBlock() instanceof AbstractBogeyBlock)
                return posCache;
        }
        return null;
    }
}
