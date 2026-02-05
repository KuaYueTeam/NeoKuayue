package willow.train.kuayue.initial;

import com.simibubi.create.Create;
import kasuga.lib.core.util.Envs;
import kasuga.lib.registrations.common.CreativeTabReg;
import kasuga.lib.registrations.registry.CreateRegistry;
import net.minecraftforge.common.MinecraftForge;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.block.panels.pantograph.network.ClientSyncManager;
import willow.train.kuayue.block.panels.pantograph.network.ServerSyncManager;
import willow.train.kuayue.event.both.OnFinalizeSetup;
import willow.train.kuayue.event.both.PlayerDataEvent;
import willow.train.kuayue.event.client.*;
import willow.train.kuayue.event.server.ColorTemplateEvents;
import willow.train.kuayue.event.server.PlayerJumpEvents;
import willow.train.kuayue.event.server.ServerResourceReloadEvent;
import willow.train.kuayue.event.server.TrainCouplerTickEvents;
import willow.train.kuayue.initial.create.*;
import willow.train.kuayue.initial.fluid.AllFluids;
import willow.train.kuayue.initial.food.AllFoods;
import willow.train.kuayue.initial.material.AllMaterials;
import willow.train.kuayue.initial.ore.AllOres;
import willow.train.kuayue.initial.ore.FeaturesInit;
import willow.train.kuayue.systems.blueprint_distributor.DistributionEvent;
import willow.train.kuayue.systems.device.AllDeviceItems;
import willow.train.kuayue.initial.recipe.AllRecipes;
import willow.train.kuayue.systems.device.EntityTrackingListener;
import willow.train.kuayue.systems.overhead_line.OverheadLineSystem;
import willow.train.kuayue.systems.overhead_line.block.line.OverheadLineRendererSystem;
import willow.train.kuayue.systems.overhead_line.block.support.AllOverheadLineSupportBlocks;
import willow.train.kuayue.systems.train_extension.TrainExtensionConstants;

public class AllElements {

    public static final CreateRegistry testRegistry = new CreateRegistry(Kuayue.MODID, Kuayue.BUS);

    public static final CreateRegistry createRegistry = new CreateRegistry(Create.ID, Kuayue.BUS);

    public static final CreativeTabReg neoKuayueMainTab = new CreativeTabReg("kuayue_main")
            .icon(() -> AllBlocks.CR_LOGO.itemInstance().getDefaultInstance())
            .submit(testRegistry);

    public static final CreativeTabReg neoKuayueLocoTab = new CreativeTabReg("kuayue_loco")
            .icon(() -> AllItems.LOCO_LOGOS.getItem().getDefaultInstance())
            .submit(testRegistry);

    public static final CreativeTabReg neoKuayueCarriageTab = new CreativeTabReg("kuayue_carriage")
            .icon(() -> AllItems.SERIES_25_LOGOS.getItem().getDefaultInstance())
            .submit(testRegistry);

    public static final CreativeTabReg neoKuayueDietTab = new CreativeTabReg("kuayue_diet")
            .icon(() -> AllItems.CA_25T.getItem().getDefaultInstance())
            .submit(testRegistry);

    public static final CreativeTabReg neoKuayueDeviceTab = new CreativeTabReg("kuayue_device")
            .icon(() -> AllDeviceItems.ITEM_LOGO.getItem().getDefaultInstance())
            .submit(testRegistry);

    public static final CreativeTabReg neoKuayueGridTab = new CreativeTabReg("kuayue_grid")
            .icon(() -> AllOverheadLineSupportBlocks.OVERHEAD_LINE_SUPPORT_A1.itemInstance().getDefaultInstance())
            .submit(testRegistry);
    public static final CreativeTabReg neoKuayueMaterialTab = new CreativeTabReg("kuayue_materials")
            .icon(() -> AllItems.CIRCUIT_MOTHERBOARD.getItem().getDefaultInstance())
            .submit(testRegistry);

    public static void invoke() {
        AllTags.invoke();
        willow.train.kuayue.initial.AllBlocks.invoke();
        AllTrackMaterial.invoke();
        AllTracks.invoke();
        AllLocoBogeys.invoke();
        AllCarriageBogeys.invoke();
        AllBehaviours.invoke();
        AllPackets.invoke();
        AllMaterials.invoke();
        AllEditableTypes.invoke();
        AllMenuScreens.invoke();
        AllItems.invoke();
        AllFoods.invoke();
        AllRecipes.invoke();
        AllEntities.invoke();
        AllOres.invoke();
        AllSounds.invoke();
        AllConductorTypes.invoke();
        OverheadLineSystem.invoke();
//        FluidsInit.register(testRegistry.eventBus);
//        FluidTypesInit.register(testRegistry.eventBus);
        FeaturesInit.register(testRegistry.eventBus);
        AllFluids.invoke();
        if (Envs.isClient()) {
            ClientInit.invoke();
            Kuayue.BUS.addListener(ClientInit::registerHUDOverlays);
            Kuayue.BUS.addListener(DistributionEvent::onClientStarted);
            MinecraftForge.EVENT_BUS.addListener(RenderArrowEvent::renderBlockBounds);
            MinecraftForge.EVENT_BUS.addListener(OverheadLineRendererSystem::onRenderLevelLast);
            MinecraftForge.EVENT_BUS.addListener(ColorTemplateEvents::unloadEvent);
            MinecraftForge.EVENT_BUS.addListener(ColorTemplateEvents::saveEvent);
            MinecraftForge.EVENT_BUS.addListener(ColorTemplateEvents::loadEvent);
            MinecraftForge.EVENT_BUS.addListener(ClientPassengerEvent::onMountEvent);
            MinecraftForge.EVENT_BUS.addListener(ClientRenderTickManager::renderClientTick);
            MinecraftForge.EVENT_BUS.addListener(ClientTickScheduler::onClientEarlyTick);
            // MinecraftForge.EVENT_BUS.addListener(RenderPrePosedBlockEvent::renderBlock);
            MinecraftForge.EVENT_BUS.register(new CarriageInventoryEvents());
            MinecraftForge.EVENT_BUS.addListener(ClientSyncManager::clientTick);
            MinecraftForge.EVENT_BUS.addListener(TrainClientTickEvents::clientTick);
        }
        Kuayue.BUS.addListener(OnFinalizeSetup::onCommonSetup);
        MinecraftForge.EVENT_BUS.addListener(PlayerJumpEvents::playerJumpEvent);
        MinecraftForge.EVENT_BUS.addListener(ServerResourceReloadEvent::onServerResourceReload);
        MinecraftForge.EVENT_BUS.addListener(PlayerDataEvent::onPlayerLogin);
        MinecraftForge.EVENT_BUS.addListener(PlayerDataEvent::onPlayerLogout);
        MinecraftForge.EVENT_BUS.addListener(PlayerDataEvent::onLevelLoad);
        MinecraftForge.EVENT_BUS.addListener(PlayerDataEvent::onLevelUnload);
        MinecraftForge.EVENT_BUS.addListener(PlayerDataEvent::onLevelSave);
        MinecraftForge.EVENT_BUS.addListener(PlayerDataEvent::addCustomTrades);
        MinecraftForge.EVENT_BUS.addListener(EntityTrackingListener::onEntityUnload);
        MinecraftForge.EVENT_BUS.addListener(ServerSyncManager::serverTick);
        MinecraftForge.EVENT_BUS.addListener(ServerSyncManager::onLevelUnload);
        MinecraftForge.EVENT_BUS.addListener(TrainCouplerTickEvents::serverTick);
        MinecraftForge.EVENT_BUS.addListener(TrainExtensionConstants::onLevelUnloaded);
        testRegistry.submit();
        createRegistry.submit();
    }
}
