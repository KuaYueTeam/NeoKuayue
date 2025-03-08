package willow.train.kuayue.systems.overhead_line.constants;

import net.minecraft.resources.ResourceLocation;
import willow.train.kuayue.systems.overhead_line.power_network.PowerGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AllPowerGraphs {
    public static final HashMap<ResourceLocation, PowerGraph> GRAPHS = new HashMap<>();

    public static void refreshGraphs() {
        Set<ResourceLocation> toRemove = new HashSet<>();
        GRAPHS.forEach((rl, pg) -> {
            if (pg.getNodesSize() == 0) toRemove.add(rl);
        });
        toRemove.forEach(GRAPHS::remove);
    }

    public static ResourceLocation randomUUIDResourceLocation(String namespace) {
        return new ResourceLocation(namespace, UUID.randomUUID().toString());
    }
}
