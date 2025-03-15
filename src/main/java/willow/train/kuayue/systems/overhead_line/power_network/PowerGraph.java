package willow.train.kuayue.systems.overhead_line.power_network;

import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import willow.train.kuayue.systems.overhead_line.constants.AllPowerGraphs;
import willow.train.kuayue.systems.overhead_line.tree.GraphTree;
import willow.train.kuayue.systems.overhead_line.types.OverheadLineType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PowerGraph {

    public final ResourceLocation name;
    private final HashMap<Pair<BlockPos, Integer>, PowerNode> nodes;

    /**
        edge -> 边缘结点，这里的定义是是只有一条边的结点(不包括没有任何边的头结点)
        这种结点在断裂其边时不需要关心其它结点的状态，只需要处理其本身的变化。
        同时，它在进行额外连接时也可以转化为不稳定结点。
    */
    public final HashSet<PowerNode> edgeNodes;
    private PowerNode head;

    public PowerGraph(ResourceLocation name, PowerNode firstNode) {
        this(name);
        this.head = firstNode;
        nodes.put(firstNode.getPositionPair(), firstNode);
    }

    public PowerGraph(ResourceLocation name) {
        this.name = name;
        this.nodes = new HashMap<>();
        this.edgeNodes = new HashSet<>();
        this.head = null;
    }

    public void addNode(PowerNode node) {
        if (nodes.isEmpty() && head == null) head = node;
        this.nodes.put(node.getPositionPair(), node);
    }

    public PowerNode getNode(BlockPos pos, Integer index) {
        return getNode(Pair.of(pos, index));
    }

    public PowerNode getNode(Pair<BlockPos, Integer> posPair) {
        return nodes.getOrDefault(posPair, null);
    }


    public boolean containsNode(BlockPos blockPos, Integer index) {
        return containsNode(Pair.of(blockPos, index));
    }

    public boolean containsNode(Pair<BlockPos, Integer> pos) {
        return nodes.containsKey(pos);
    }

    public boolean connect(BlockPos prevPos, Integer prevIndex, BlockPos selfPos, Integer selfIndex, OverheadLineType type) {
        return connect(Pair.of(prevPos, prevIndex), Pair.of(selfPos, selfIndex), type);
    }

    public boolean connect(BlockPos prevPos, Integer prevIndex, PowerNode node, OverheadLineType type) {
        return connect(Pair.of(prevPos, prevIndex), node, type);
    }

    public boolean connect(Pair<BlockPos, Integer> posPair, Pair<BlockPos, Integer> selfPair,
                           OverheadLineType type) {
        if (!nodes.containsKey(selfPair)) return false;
        return connect(posPair, nodes.get(selfPair), type);
    }

    public boolean connect(Pair<BlockPos, Integer> posPair, PowerNode node, OverheadLineType type) {
        if (!nodes.containsKey(posPair)) {
            if (this.head == null) {
                this.head = node;
                this.nodes.put(node.getPositionPair(), node);
                return true;
            }
            return false;
        }
        PowerNode prev = getNode(posPair);
        node.connect(prev, type);
        int prevSize = prev.getConnections().size();
        if (prevSize == 1) {
            edgeNodes.add(prev);
        } else {
            edgeNodes.remove(prev);
        }
        nodes.put(node.getPositionPair(), node);
        GraphTree tree = new GraphTree(node);
        tree.genTreeFromHead();
        tree.getNodes().forEach(
                treeNode -> {
                    nodes.put(treeNode.element.getPositionPair(), treeNode.element);
                    if (treeNode.element.getConnectedNode().size() == 1)
                        edgeNodes.add(treeNode.element);
                }
        );
        return true;
    }

    public boolean removeEdge(Pair<BlockPos, Integer> left, Pair<BlockPos, Integer> right) {
        if (!(containsNode(left) && containsNode(right))) return false;
        PowerNode leftNode = nodes.get(left), rightNode = nodes.get(right);
        if (!leftNode.disConnect(rightNode)) return false;
        if (leftNode.getConnections().isEmpty() || rightNode.getConnections().isEmpty()) return true;
        splitGraph().forEach(a -> AllPowerGraphs.GRAPHS.put(a.name, a));
        return true;
    }

    public boolean removeNode(Pair<BlockPos, Integer> posPair) {
        if (!nodes.containsKey(posPair)) return false;
        PowerNode node = getNode(posPair);
        for (PowerNode n : node.getConnectedNode()) node.disConnect(n);
        this.nodes.remove(posPair);
        GraphTree tree = new GraphTree(head);
        tree.genTreeFromHead();
        if (tree.getNodes().size() == this.nodes.size())
            return true;
        splitGraph().forEach(a -> AllPowerGraphs.GRAPHS.put(a.name, a));
        return true;
    }

    public Set<PowerGraph> splitGraph() {
        GraphTree tree = new GraphTree(this.head);
        tree.genTreeFromHead();
        if (tree.getNodes().size() == this.nodes.size()) return Set.of();
        HashMap<Pair<BlockPos, Integer>, PowerNode> cache = new HashMap<>(nodes);
        Set<Pair<BlockPos, Integer>> posPairCache = new HashSet<>();
        HashSet<PowerGraph> result = new HashSet<>();
        for(;;) {
            tree.fillPosCollection(posPairCache);
            posPairCache.forEach(cache::remove);
            posPairCache.clear();
            cache.forEach(nodes::remove);
            if (cache.isEmpty()) break;
            tree = new GraphTree(cache.values().stream().findAny().get());
            tree.genTreeFromHead();
            result.add(tree.fillPowerGraph(AllPowerGraphs.randomUUIDResourceLocation(this.name.getNamespace())));
        }
        refreshEdgePoints();
        return result;
    }

    public int getNodesSize() {
        return nodes.size();
    }

    public void updateBlockInfo() {}

    public void refreshEdgePoints() {
        edgeNodes.clear();
        nodes.forEach((a, b) -> {
            if (b.getConnections().size() == 1) edgeNodes.add(b);
        });
    }
}
