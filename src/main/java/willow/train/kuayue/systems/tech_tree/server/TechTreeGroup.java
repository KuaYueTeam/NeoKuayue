package willow.train.kuayue.systems.tech_tree.server;

import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.systems.tech_tree.NodeLocation;
import willow.train.kuayue.systems.tech_tree.json.*;

import java.util.*;

@Getter
public class TechTreeGroup {

    public final TechTreeGroupData data;

    public final TechTree tree;
    private final HashMap<NodeLocation, TechTreeNode> nodes;
    private final TechTreeNode root;
    private final ArrayList<TechTreeNode> prev;

    public TechTreeGroup(TechTree tree, TechTreeGroupData data) {
        this.data = data;
        this.tree = tree;
        nodes = new HashMap<>();
        prev = new ArrayList<>();
        collectNodes();
        tree.grepNodes(nodes);
        root = nodes.getOrDefault(data.getRootLocation(), null);
    }

    protected void collectNodes() {
        data.getNodes().forEach((loc, dat) -> {
            nodes.put(loc, new TechTreeNode(this, dat));
        });
    }

    protected void addPrev(TechTreeNode node) {
        prev.add(node);
    }

    public boolean isHide() {
        return data.isHide();
    }

    public ItemStack icon() {
        return data.getIcon();
    }

    public HideContext getHideContext() {
        return data.getHide();
    }

    public OnUnlockContext getUnlockContext() {
        return data.getUnlock();
    }

    public String getIdentifier() {
        return data.identifier;
    }

    public String getNamespace() {
        return tree.getNamespace();
    }

    public boolean is(ResourceLocation location) {
        return data.getLocation().equals(location);
    }

    public boolean is(String str) {
        return data.getLocation().toString().equals(str);
    }

    public ResourceLocation getId() {
        return data.getLocation();
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(getId());
        if (data.getCoverId() == null)
            buf.writeUtf("null");
        else
            buf.writeResourceLocation(data.getCoverId());
        buf.writeUtf(data.getTitle());
        buf.writeUtf(data.getDescription());
        buf.writeItemStack(icon(), false);

        root.getLocation().writeToByteBuf(buf);
        buf.writeInt(prev.size());
        prev.forEach(node -> node.getLocation().writeToByteBuf(buf));

        buf.writeInt(nodes.size());
        nodes.forEach((location, node) -> location.writeToByteBuf(buf));
    }

    public boolean hasRing() {
        Queue<TechTreeNode> queue = new LinkedList<>();
        HashMap<TechTreeNode, Integer> degrees = new HashMap<>();
        for (TechTreeNode n : nodes.values()) {
            degrees.put(n, n.getInDegree());
            if (n.getInDegree() == 0) queue.add(n);
        }
        int counter = 0;
        while (!queue.isEmpty()) {
            TechTreeNode node = queue.poll();
            counter++;
            for (TechTreeNode nextNode : node.getNext()) {
                int deg = degrees.get(nextNode);
                deg -= 1;
                if (deg == 0) queue.add(nextNode);
                degrees.put(nextNode, deg);
            }
        }
        return counter != nodes.size();
    }

    public @Nullable UnlockCondition getUnlockCondition() {
        return getData().getUnlockCondition();
    }
}
