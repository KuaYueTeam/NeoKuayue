package willow.train.kuayue.systems.overhead_line.types;

import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import willow.train.kuayue.systems.overhead_line.power_network.PowerPackage;

import java.util.function.Consumer;
import java.util.function.Function;

public class PowerNodeType {
    private final ResourceLocation location;
    private final PowerIOType io;
    public final float maxVoltage, maxCurrent;
    private final Pair<Function<PowerPackage, PowerPackage>, Function<PowerPackage, PowerPackage>> energyFunctions;
    public static final Function<PowerPackage, PowerPackage> defFunc = (input) -> input;
    private final CompoundTag data;

    public PowerNodeType(ResourceLocation location, PowerIOType io, float maxVoltage, float maxCurrent, Pair<Function<PowerPackage, PowerPackage>, Function<PowerPackage, PowerPackage>> functions) {
        this.location = location;
        this.io = io;
        this.energyFunctions = functions;
        this.maxCurrent = maxCurrent;
        this.maxVoltage = maxVoltage;
        this.data = new CompoundTag();
    }

    public PowerNodeType(ResourceLocation location, PowerIOType io, float maxVoltage, float maxCurrent, Function<PowerPackage, PowerPackage> supplierFunction, Function<PowerPackage, PowerPackage> consumingFunction) {
        this.location = location;
        this.io = io;
        this.maxVoltage = maxVoltage;
        this.maxCurrent = maxCurrent;
        this.energyFunctions = Pair.of(supplierFunction, consumingFunction);
        this.data = new CompoundTag();
    }

    public PowerNodeType(ResourceLocation location, float maxVoltage, float maxCurrent) {
        this.location = location;
        this.io = PowerIOType.JUMP;
        this.maxCurrent = maxCurrent;
        this.maxVoltage = maxVoltage;
        this.energyFunctions = Pair.of(defFunc, defFunc);
        this.data = new CompoundTag();
    }

    public CompoundTag getAdditionalData() {
        return data;
    }

    public void addAdditionalData(Consumer<CompoundTag> func) {
        func.accept(data);
    }

    public Function<PowerPackage, PowerPackage> getSupplierFunc() {
        return energyFunctions.getFirst();
    }

    public PowerPackage applySupplier(PowerPackage input) {
        return getSupplierFunc().apply(input);
    }

    public Function<PowerPackage, PowerPackage> getConsumingFunc() {
        return energyFunctions.getSecond();
    }

    public float getMaxVoltage() {
        return maxVoltage;
    }

    public float getMaxCurrent() {
        return maxCurrent;
    }

    public PowerPackage handlePackage(PowerPackage input) {
        return switch (io) {
            case SOURCE -> getSupplierFunc().apply(input);
            case NONE -> PowerPackage.EMPTY;
            case JUMP -> input;
            case CONSUME -> getConsumingFunc().apply(input);
            case CONSUME_SUPPLY -> getSupplierFunc().apply(getConsumingFunc().apply(input));
            case SUPPLY_CONSUME -> getConsumingFunc().apply(getSupplierFunc().apply(input));
        };
    }

    public PowerPackage applyConsumer(PowerPackage input) {
        return getConsumingFunc().apply(input);
    }

    public Function<PowerPackage, PowerPackage> getFunc(boolean first) {
        return (Function<PowerPackage, PowerPackage>) energyFunctions.get(first);
    }

    public ResourceLocation getLocation() {
        return location;
    }
}
