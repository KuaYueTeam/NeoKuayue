package willow.train.kuayue.systems.train_extension;

import com.simibubi.create.content.trains.entity.Train;
import kasuga.lib.core.base.Saved;
import lombok.Getter;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.initial.AllPackets;
import willow.train.kuayue.network.s2c.TrainExtensionChangePacket;
import willow.train.kuayue.network.s2c.TrainExtensionRemovePacket;
import willow.train.kuayue.network.s2c.TrainExtensionSyncPacket;
import willow.train.kuayue.systems.train_extension.bogey_weight.BogeyExtensionSystem;
import willow.train.kuayue.systems.train_extension.conductor.Conductable;
import willow.train.kuayue.systems.train_extension.conductor.ConductorHelper;
import willow.train.kuayue.systems.train_extension.conductor.ConductorLocation;
import willow.train.kuayue.systems.train_extension.conductor.ConductorType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TrainExtensionSystem extends SavedData {

    public final String resourceKey = "train_extension_store";

    public final Saved<TrainExtensionSystem> distSaving =
            new Saved<>(resourceKey, TrainExtensionSystem::getInstance, TrainExtensionSystem::load);

    private final HashMap<ResourceLocation, ConductorType> types;

    @Getter
    private final HashMap<UUID, TrainAdditionalData> data;

    public final BogeyExtensionSystem BOGEY_EXTENSION;

    public final HashSet<UUID> trainsToRemove;

    public final HashSet<ConductorHelper.TrainMergeRequest> trainsToMerge;
    public final HashSet<Train> newlyMerged;

    public static class ConductorCDInfo {
        public final Conductable conductorA;
        public final Conductable conductorB;
        public int checkInterval;

        public ConductorCDInfo(Conductable conductorA, Conductable conductorB) {
            this.conductorA = conductorA;
            this.conductorB = conductorB;
            this.checkInterval = 0;
        }
    }

    public final ConcurrentHashMap<Couple<ConductorLocation>, ConductorCDInfo> conductorsCoolingDown;

    public TrainExtensionSystem() {
        this.data = new HashMap<>();
        this.types = new HashMap<>();
        BOGEY_EXTENSION = new BogeyExtensionSystem();
        trainsToRemove = new HashSet<>();
        trainsToMerge = new HashSet<>();
        newlyMerged = new HashSet<>();
        conductorsCoolingDown = new ConcurrentHashMap<>();
    }

    public void broadcastToClients(ServerLevel level, BlockPos pos) {
        AllPackets.CHANNEL.boardcastToClients(new TrainExtensionSyncPacket(this), level, pos);
    }

    public void serverSync(ServerPlayer player) {
        AllPackets.CHANNEL.sendToClient(new TrainExtensionSyncPacket(this), player);
    }

    public void clientSync(HashMap<UUID, TrainAdditionalData> data) {
        this.data.putAll(data);
    }

    public void removeTrain(UUID trainId) {
        trainsToRemove.add(trainId);
    }

    public void cancelRemoveTrain(UUID trainId) {
        trainsToRemove.remove(trainId);
    }

    public static TrainExtensionSystem getInstance() {
        return Kuayue.TRAIN_EXTENSION;
    }

    public void register(ConductorType type) {
        this.types.put(type.id(), type);
    }

    public @Nullable ConductorType getType(ResourceLocation id) {
        return this.types.get(id);
    }

    public boolean hasType(ResourceLocation id) {
        return this.types.containsKey(id);
    }

    public @Nullable TrainAdditionalData get(UUID id) {
        return this.data.get(id);
    }

    public @NotNull TrainAdditionalData getOrCreate(Train train) {
        return this.data.computeIfAbsent(train.id, id -> {
            return new TrainAdditionalData(train);
        });
    }

    public void add(TrainAdditionalData data) {
        this.data.put(data.getTrain(), data);
    }

    public void syncChange(TrainAdditionalData data) {
        TrainExtensionChangePacket packet = new TrainExtensionChangePacket(data);
        AllPackets.CHANNEL.getChannel().send(PacketDistributor.ALL.noArg(), packet);
    }

    public void remove(UUID trainId) {
        this.data.remove(trainId);
    }

    public void syncRemove(UUID trainId) {
        TrainExtensionRemovePacket packet = new TrainExtensionRemovePacket(trainId);
        AllPackets.CHANNEL.getChannel().send(PacketDistributor.ALL.noArg(), packet);
    }

    public boolean contains(UUID trainId) {
        return this.data.containsKey(trainId);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        int i = 0;
        for (Map.Entry<UUID, TrainAdditionalData> entry : this.data.entrySet()) {
            CompoundTag nbt = new CompoundTag();
            TrainAdditionalData data = entry.getValue();
            data.write(nbt);
            tag.put("data" + i, nbt);
            i++;
        }

        ListTag cooldownList = new ListTag();
        for (ConductorCDInfo info : this.conductorsCoolingDown.values()) {
            CompoundTag cdTag = new CompoundTag();

            CompoundTag locATag = new CompoundTag();
            info.conductorA.getLoc().write(locATag);
            cdTag.put("locA", locATag);

            CompoundTag locBTag = new CompoundTag();
            info.conductorB.getLoc().write(locBTag);
            cdTag.put("locB", locBTag);

            cdTag.putInt("checkInterval", info.checkInterval);

            cooldownList.add(cdTag);
        }
        tag.put("cooldowns", cooldownList);

        return tag;
    }

    public void clearData() {
        data.clear();
    }

    public static TrainExtensionSystem load(@NotNull CompoundTag nbt) {
        TrainExtensionSystem sys = Kuayue.TRAIN_EXTENSION;
        sys.clearData();
        int i = 0;
        while (nbt.contains("data" + i)) {
            CompoundTag tag = nbt.getCompound("data" + i);
            i++;
            TrainAdditionalData data = new TrainAdditionalData(tag);
            sys.data.put(data.getTrain(), data);
        }

        sys.conductorsCoolingDown.clear();
        if (nbt.contains("cooldowns")) {
            ListTag cooldownList = nbt.getList("cooldowns", Tag.TAG_COMPOUND);
            for (Tag t : cooldownList) {
                CompoundTag cdTag = (CompoundTag) t;

                ConductorLocation locA = new ConductorLocation(cdTag.getCompound("locA"));
                ConductorLocation locB = new ConductorLocation(cdTag.getCompound("locB"));
                int checkInterval = cdTag.getInt("checkInterval");

                TrainAdditionalData dataA = sys.get(locA.getTrainId());
                TrainAdditionalData dataB = sys.get(locB.getTrainId());

                if (dataA != null && dataB != null) {
                    Conductable condA = dataA.getConductorAt(locA);
                    Conductable condB = dataB.getConductorAt(locB);

                    if (condA != null && condB != null) {
                        ConductorCDInfo info = new ConductorCDInfo(condA, condB);
                        info.checkInterval = checkInterval;
                        sys.conductorsCoolingDown.put(Couple.create(locA, locB), info);
                    }
                }
            }
        }

        return sys;
    }
}
