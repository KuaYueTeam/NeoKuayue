package willow.train.kuayue.common;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

public class OptionalBehaviour<T extends BlockEntityBehaviour> extends BlockEntityBehaviour{
    protected static HashMap<BehaviourType<?>, BehaviourType<?>> conversionMap = new HashMap<>();
    private boolean initialized;

    public static <T extends BlockEntityBehaviour> OptionalBehaviour<T> create(String behaviourName, SmartBlockEntity smartBlockEntity, Supplier<T> behaviourSupplier, BehaviourType<?> originalType){
        BehaviourType<?> converted = conversionMap.computeIfAbsent(originalType, type -> new BehaviourType<>());
        return new OptionalBehaviour<>(behaviourName,smartBlockEntity, converted, behaviourSupplier);
    }

    public static <V extends TrackEdgePoint> OptionalBehaviour<TrackTargetingBehaviour<V>> createTrackTargetingBehaviour(SmartBlockEntity smartBlockEntity, EdgePointType<V> trackEdgePointType){
        return create(
                "Track",
                smartBlockEntity,
                () -> new TrackTargetingBehaviour<>(smartBlockEntity, trackEdgePointType),
                TrackTargetingBehaviour.TYPE
        );
    }

    private final String behaviourName;
    private final Supplier<T> behaviour;
    private T presentBehaviour;
    private final BehaviourType<?> type;

    public OptionalBehaviour(String behaviourName,SmartBlockEntity blockEntity, BehaviourType<?> type, Supplier<T> behaviour) {
        super(blockEntity);
        this.behaviourName = behaviourName;
        this.behaviour = behaviour;
        this.type = type;
    }


    @Override
    public BehaviourType<?> getType() {
        return type;
    }

    public T get() {
        return presentBehaviour;
    }

    @Override
    public void initialize() {
        this.initialized = true;
        if(presentBehaviour != null){
            presentBehaviour.initialize();
        }
    }

    @Override
    public void tick() {
        if(presentBehaviour != null){
            presentBehaviour.tick();
        }
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        if(presentBehaviour == null) {
            if(nbt.getBoolean(behaviourName + "Present")){
                presentBehaviour = behaviour.get();
                if(initialized) {
                    presentBehaviour.initialize();
                }
                presentBehaviour.read(nbt, clientPacket);
            }
        }
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        if(presentBehaviour != null){
            nbt.putBoolean(behaviourName + "Present", true);
            presentBehaviour.write(nbt, clientPacket);
        } else nbt.putBoolean(behaviourName + "Present", false);
    }

    @Override
    public boolean isSafeNBT() {
        return presentBehaviour == null || presentBehaviour.isSafeNBT();
    }

    public Optional<T> getOptional() {
        return Optional.ofNullable(presentBehaviour);
    }


    @Override
    public ItemRequirement getRequiredItems() {
        return presentBehaviour == null ? ItemRequirement.NONE : presentBehaviour.getRequiredItems();
    }

    public void onBlockChanged(BlockState oldState) {
        if (presentBehaviour != null) {
            presentBehaviour.onBlockChanged(oldState);
        }
    }

    public void onNeighborChanged(BlockPos neighborPos) {
        if (presentBehaviour != null) {
            presentBehaviour.onNeighborChanged(neighborPos);
        }
    }

    public void unload() {
        if (presentBehaviour != null) {
            presentBehaviour.unload();
        }
    }

    public void destroy() {
        if (presentBehaviour != null) {
            presentBehaviour.destroy();
        }
    }
}
