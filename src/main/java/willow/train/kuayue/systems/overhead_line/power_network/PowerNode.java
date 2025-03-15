package willow.train.kuayue.systems.overhead_line.power_network;

import kasuga.lib.core.base.NbtSerializable;
import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import willow.train.kuayue.systems.overhead_line.constants.AllPowerNodeTypes;
import willow.train.kuayue.systems.overhead_line.constants.Utils;
import willow.train.kuayue.systems.overhead_line.types.OverheadLineType;
import willow.train.kuayue.systems.overhead_line.types.PowerNodeType;

import java.util.ArrayList;
import java.util.List;

public class PowerNode implements NbtSerializable {
    private final Pair<BlockPos, Integer> position;

    public final PowerNodeType type;
    private final List<PowerEdge> connection;

    public PowerNode(BlockPos pos, int index, ResourceLocation type) {
        this.position = Pair.of(pos, index);
        this.type = AllPowerNodeTypes.get(type);
        connection = new ArrayList<>();
    }

    public PowerNode(BlockPos pos, int index, PowerNodeType type) {
        this.position = Pair.of(pos, index);
        this.type = type;
        connection = new ArrayList<>();
    }

    public BlockPos getPos() {
        return position.getFirst();
    }

    public int getIndex() {return position.getSecond();}

    public Pair<BlockPos, Integer> getPositionPair() {return position;}

    public PowerNodeType getType() {
        return type;
    }

    public PowerPackage consume(PowerPackage input) {
        return type.applyConsumer(input);
    }

    public PowerPackage apply(PowerPackage input) {
        return type.applySupplier(input);
    }

    public boolean connectedTo(PowerNode node) {
        if (node == this) return true;
        for (PowerEdge edge : connection) {
            if (edge.hasNode(node)) return true;
        }
        return false;
    }

    public PowerEdge findConnectionWith(PowerNode node) {
        for (PowerEdge edge : connection) {
            if (edge.hasNode(node)) return edge;
        }
        return null;
    }

    public List<PowerNode> getConnectedNode() {
        ArrayList<PowerNode> result = new ArrayList<>(this.connection.size());
        connection.forEach(edge -> result.add(edge.getNextSide(this)));
        return result;
    }

    public void connect(PowerNode node, OverheadLineType type) {
        PowerEdge edge = new PowerEdge(type, this, node);
        connection.add(edge);
        node.connection.add(edge);
    }

    public boolean disConnect(PowerNode node) {
        PowerEdge edge = findConnectionWith(node);
        if (edge == null) return false;
        node.connection.remove(edge);
        this.connection.remove(edge);
        return true;
    }

    public List<PowerEdge> getConnections() {
        return connection;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PowerNode node)) return false;
        return this.position.getFirst().equals(node.position.getFirst())
                && this.position.getSecond().equals(node.position.getSecond());
    }

    @Override
    public void write(CompoundTag compoundTag) {
        Utils.writeBlockPos(position.getFirst(), compoundTag, "pos");
        compoundTag.putInt("index", position.getSecond());
        Utils.writeResourceLocation(type.getLocation(), compoundTag, "type");
    }

    @Override
    public void read(CompoundTag compoundTag) {}

    public static PowerNode readFromNbt(CompoundTag nbt) {
        BlockPos pos = Utils.readBlockPos(nbt, "pos");
        int index = nbt.getInt("index");
        ResourceLocation type = Utils.readResourceLocation(nbt, "type");
        return new PowerNode(pos, index, type);
    }
}
