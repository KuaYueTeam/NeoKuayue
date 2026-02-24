package willow.train.kuayue.systems.train_extension.bogey_weight;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import kasuga.lib.core.util.data_type.Pair;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.initial.AllPackets;
import willow.train.kuayue.network.s2c.BogeyExtensionSyncPacket;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BogeyExtensionSystem implements ResourceManagerReloadListener {

    @Getter
    private final HashMap<Pair<BogeyStyle, BogeySizes.BogeySize>, BogeyAdditionalData> bogeyData;

    public static final BogeyAdditionalData DEFAULT = new BogeyAdditionalData(null, null, 2, Integer.MAX_VALUE);

    public static BogeyAdditionalData getDefault() {
        return DEFAULT;
    }

    public BogeyExtensionSystem() {
        bogeyData = new HashMap<>();
    }

    public void serverSync(ServerPlayer player) {
        AllPackets.CHANNEL.sendToClient(new BogeyExtensionSyncPacket(this), player);
    }

    public void clientSync(HashSet<BogeyAdditionalData> bogeyData) {
        bogeyData.forEach(d -> {
            this.bogeyData.put(Pair.of(d.style, d.size), d);
        });
    }

    public static BogeyExtensionSystem getInstance() {
        return Kuayue.TRAIN_EXTENSION.BOGEY_EXTENSION;
    }

    public boolean hasBogeyData(BogeyStyle style, BogeySizes.BogeySize size) {
        return bogeyData.containsKey(Pair.of(style, size));
    }

    public BogeyAdditionalData getBogeyData(BogeyStyle style, BogeySizes.BogeySize size) {
        return bogeyData.get(Pair.of(style, size));
    }

    public BogeyAdditionalData getBogeyDataOrDefault(BogeyStyle style, BogeySizes.BogeySize size) {
        return bogeyData.getOrDefault(Pair.of(style, size), DEFAULT);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        bogeyData.clear();
    }

    public void loadData(ResourceManager resourceManager) {
        bogeyData.clear();
        Map<ResourceLocation, Resource> resources =
                resourceManager.listResources("bogey_extension", rl -> rl.getPath().endsWith(".json"));
        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            try {
                JsonElement element = JsonParser.parseReader(entry.getValue().openAsReader());
                if (!element.isJsonObject()) continue;
                JsonObject obj =  element.getAsJsonObject();
                for (Map.Entry<String, JsonElement> coupleEntry : obj.entrySet()) {
                    if (!coupleEntry.getValue().isJsonObject()) continue;
                    JsonObject innerObj =  coupleEntry.getValue().getAsJsonObject();
                    ResourceLocation rl = new ResourceLocation(coupleEntry.getKey());
                    BogeyStyle style = AllBogeyStyles.BOGEY_STYLES.get(rl);
                    if (style == null) continue;
                    for (Map.Entry<String, JsonElement> sizeEntry : innerObj.entrySet()) {
                        JsonElement ele = sizeEntry.getValue();
                        if (!ele.isJsonObject()) continue;
                        JsonObject o = ele.getAsJsonObject();
                        ResourceLocation sizeLoc = new ResourceLocation(sizeEntry.getKey());
                        Set<BogeySizes.BogeySize> sizes = style.validSizes();
                        BogeySizes.BogeySize size = null;
                        for (BogeySizes.BogeySize s : sizes) {
                            if (s.id().equals(sizeLoc)) {
                                size = s;
                                break;
                            }
                        }
                        if (size == null) continue;
                        bogeyData.put(Pair.of(style, size), new BogeyAdditionalData(style, size, o));
                    }

                }
            } catch (JsonParseException | IOException e) {
                Kuayue.LOGGER.error("Failed to parse bogey extension data {}", entry.getKey(), e);
            }
        }
    }
}
