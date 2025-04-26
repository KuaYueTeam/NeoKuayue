package willow.train.kuayue.event.client;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.level.LevelEvent;
import willow.train.kuayue.systems.overhead_line.block.line.OverheadLineRendererSystem;

public class LevelEventListener {
    public void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) return;
        // Handle world loading logic here
    }

    public void onLevelUnload(LevelEvent.Unload event) {
    }

    public void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        OverheadLineRendererSystem.clearLevel();
    }
}
