package willow.train.kuayue.systems.overhead_line.constants;

import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.resources.ResourceLocation;
import willow.train.kuayue.systems.overhead_line.types.PowerIOType;
import willow.train.kuayue.systems.overhead_line.types.PowerNodeType;
import willow.train.kuayue.systems.overhead_line.power_network.PowerPackage;

import java.util.HashMap;
import java.util.function.Function;

public class AllPowerNodeTypes {
    private static final HashMap<ResourceLocation, PowerNodeType> MAP = new HashMap<>();
    public static final PowerNodeType JOINT = jumpPoint(new ResourceLocation("kuayue", "joint"), 256, 256);

    public static PowerNodeType register(ResourceLocation location, PowerIOType io, float maxVoltage, float maxCurrent,
                                         Pair<Function<PowerPackage, PowerPackage>, Function<PowerPackage, PowerPackage>> func) {
        PowerNodeType type = new PowerNodeType(location, io, maxVoltage, maxCurrent, func);
        return register(type);
    }

    public static PowerNodeType register(ResourceLocation location, PowerIOType io, float maxVoltage, float maxCurrent,
                                         Function<PowerPackage, PowerPackage> supplierFunc,
                                         Function<PowerPackage, PowerPackage> consumerFunc) {
        return register(location, io, maxVoltage, maxCurrent, Pair.of(supplierFunc, consumerFunc));
    }

    public static PowerNodeType jumpPoint(ResourceLocation location, float maxV, float maxC) {
        if (MAP.containsKey(location)) return MAP.get(location);
        PowerNodeType type = new PowerNodeType(location, maxV, maxC);
        MAP.put(location, type);
        return type;
    }

    public static PowerNodeType pureSource(ResourceLocation location, float maxV, float maxC,
                                       Function<PowerPackage, PowerPackage> supplierFunc) {
        return register(location, PowerIOType.SOURCE, maxV, maxC, supplierFunc, PowerNodeType.defFunc);
    }

    public static PowerNodeType pureConsumer(ResourceLocation location, float maxV, float maxC,
                                             Function<PowerPackage, PowerPackage> consumerFunc) {
        return register(location, PowerIOType.CONSUME, maxV, maxC, PowerNodeType.defFunc, consumerFunc);
    }

    public static PowerNodeType register(PowerNodeType type) {
        MAP.put(type.getLocation(), type);
        return type;
    }

    public static boolean contains(PowerNodeType type) {
        return MAP.containsValue(type);
    }

    public static boolean contains(ResourceLocation location) {
        return MAP.containsKey(location);
    }

    public static PowerNodeType get(ResourceLocation location) {
        return MAP.getOrDefault(location, null);
    }
}
