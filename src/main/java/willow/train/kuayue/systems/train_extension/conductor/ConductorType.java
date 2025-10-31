package willow.train.kuayue.systems.train_extension.conductor;

import com.simibubi.create.content.trains.GlobalRailwayManager;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.function.TriFunction;

public class ConductorType {

    private final ResourceLocation id;
    private final PropertyDispatch.QuadFunction<ConductorType, Train, Carriage, Boolean, Conductable> constructor;
    private final TriFunction<ConductorType, GlobalRailwayManager, CompoundTag, Conductable> deserializer;

    public static final ConductorType DUMMY = new ConductorType(
            new ResourceLocation("dummy"),
            Conductor::new,
            Conductor::new
    );

    public ConductorType(ResourceLocation id,
                         PropertyDispatch.QuadFunction<ConductorType, Train, Carriage, Boolean, Conductable> constructor,
                         TriFunction<ConductorType, GlobalRailwayManager, CompoundTag, Conductable> deserializer) {
        this.id = id;
        this.constructor = constructor;
        this.deserializer = deserializer;
    }

    public Conductable build(Train train, Carriage carriage, boolean isLeading) {
        return constructor.apply(this, train, carriage, isLeading);
    }

    public Conductable build(GlobalRailwayManager railway, CompoundTag nbt) {
        return deserializer.apply(this, railway, nbt);
    }

    public ResourceLocation id() {
        return id;
    }
}
