package willow.train.kuayue.systems.tech_tree.player;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;
import java.util.UUID;

@Getter
@OnlyIn(Dist.CLIENT)
public class ClientPlayerData {

    private static PlayerData data = null;

    public static void updateDataFromNetwork(FriendlyByteBuf buf) {
        if (Minecraft.getInstance().player == null) return;
        // if (!buf.readUUID().equals(Minecraft.getInstance().player.getUUID())) return;
        UUID playerId = buf.readUUID();
        if (data == null) {
            data = new PlayerData(playerId);
        } else {
            data.clearUnlock();
        }
        data.fromNetwork(buf);
    }

    public static Optional<PlayerData> getData() {
        return Optional.ofNullable(data);
    }
}
