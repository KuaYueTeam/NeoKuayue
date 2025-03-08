package willow.train.kuayue.systems.overhead_line.tree;

import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import willow.train.kuayue.systems.overhead_line.power_network.PowerGraph;
import willow.train.kuayue.systems.overhead_line.power_network.PowerNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class GraphTree {
    private final GraphTreeNode head;
    private final List<GraphTreeNode> nodes;

    public GraphTree(GraphTreeNode head) {
        this.head = head;
        nodes = new LinkedList<>();
    }

    public GraphTree(PowerNode node) {
        this.head = new GraphTreeNode(node);
        this.nodes = new LinkedList<>();
    }

    public GraphTreeNode getHead() {
        return head;
    }

    public List<GraphTreeNode> getNodes() {
        return nodes;
    }

    public void genTreeFromHead() {
        this.nodes.clear();
        ArrayList<PowerNode> powerNodes = new ArrayList<>(64);
        ArrayList<Integer> route = new ArrayList<>(20);
        GraphTreeNode cache = head;
        this.nodes.add(head);
        GraphTreeNode gtn;
        do {
            powerNodes.clear();
            powerNodes.addAll(cache.element.getConnectedNode());
            for (PowerNode node : powerNodes) {
                gtn = new GraphTreeNode(node);
                if (nodes.contains(gtn)) continue;
                gtn.prev = cache;
                this.nodes.add(gtn);
                cache.next.add(gtn);
            }
            if (cache.next.isEmpty()) {
                int j = route.get(route.size() - 1);
                while (j + 1 >= cache.prev.next.size()) {
                    route.remove(route.size() - 1);
                    if (route.isEmpty()) return;
                    cache = cache.prev;
                    j = route.get(route.size() - 1);
                }
                cache = cache.prev.next.get(j + 1);
                route.set(route.size() - 1, j + 1);
            } else {
                route.add(0);
                cache = cache.next.get(0);
            }
        } while (!route.isEmpty());
    }

    public void fillPosCollection(Collection<Pair<BlockPos, Integer>> collection) {
        nodes.forEach(a -> collection.add(a.element.getPositionPair()));
    }

    public void fillElementsCollection(Collection<PowerNode> collection) {
        nodes.forEach(a -> collection.add(a.element));
    }

    public PowerGraph fillPowerGraph(ResourceLocation location) {
        PowerGraph graph = new PowerGraph(location);
        nodes.forEach(a -> graph.addNode(a.element));
        graph.refreshEdgePoints();
        return graph;
    }

    // public ArrayList<GraphTreeNode> serialize() {
        // if (nodes.isEmpty()) genTreeFromHead();

    // }
}
