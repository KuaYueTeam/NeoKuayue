package willow.train.kuayue.event.server;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import willow.train.kuayue.systems.tech_tree.player.PlayerDataManager;
import willow.train.kuayue.systems.tech_tree.server.TechTreeManager;
import willow.train.kuayue.systems.train_extension.bogey_weight.BogeyExtensionSystem;

public class ServerResourceReloadEvent {

    @SubscribeEvent
    public static void onServerResourceReload(ServerStartedEvent event) {
        ResourceManager manager = event.getServer().getServerResources().resourceManager();
        TechTreeManager.MANAGER.loadData(manager);
        PlayerDataManager.MANAGER.loadAdvancements(event.getServer().getAdvancements().getAllAdvancements());
        BogeyExtensionSystem.getInstance().loadData(manager);
    }
}
