package willow.train.kuayue.systems.overhead_line.constants;

import net.minecraft.resources.ResourceLocation;
import willow.train.kuayue.systems.overhead_line.types.OverheadLineType;

import java.util.HashMap;

public class AllOverheadLineTypes {

    private static final HashMap<ResourceLocation, OverheadLineType> MAP = new HashMap<>();

    public static OverheadLineType register(ResourceLocation name, float maxVoltage, float maxCurrent, float maxLength) {
        OverheadLineType type = new OverheadLineType(name, maxVoltage, maxCurrent, maxLength);
        MAP.put(name, type);
        return type;
    }

    public static boolean contains(ResourceLocation location) {
        return MAP.containsKey(location);
    }

    public static OverheadLineType getType(ResourceLocation location) {
        return MAP.getOrDefault(location, null);
    }
}
