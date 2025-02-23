package willow.train.kuayue.initial;

import kasuga.lib.core.util.Envs;
import kasuga.lib.registrations.common.CreativeTabReg;
import kasuga.lib.registrations.registry.CreateRegistry;
import net.minecraftforge.common.MinecraftForge;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.event.both.PlayerDataEvent;
import willow.train.kuayue.event.client.CarriageInventoryEvents;
import willow.train.kuayue.event.client.ClientPassengerEvent;
import willow.train.kuayue.event.client.ClientTickScheduler;
import willow.train.kuayue.event.client.RenderArrowEvent;
import willow.train.kuayue.event.server.ColorTemplateEvents;
import willow.train.kuayue.event.server.PlayerJumpEvents;
import willow.train.kuayue.event.server.ServerResourceReloadEvent;
import willow.train.kuayue.initial.create.*;
import willow.train.kuayue.initial.fluid.AllFluids;
import willow.train.kuayue.initial.fluid.FluidTypesInit;
import willow.train.kuayue.initial.fluid.FluidsInit;
import willow.train.kuayue.initial.food.AllFoods;
import willow.train.kuayue.initial.material.AllMaterials;
import willow.train.kuayue.initial.ore.AllOres;
import willow.train.kuayue.initial.ore.FeaturesInit;
import willow.train.kuayue.systems.device.AllDeviceItems;
import willow.train.kuayue.initial.recipe.AllRecipes;
import willow.train.kuayue.systems.device.EntityTrackingListener;
import willow.train.kuayue.systems.overhead_line.OverheadLineSystem;

public class AllElements {

    public static final CreateRegistry testRegistry = new CreateRegistry(Kuayue.MODID, Kuayue.BUS);

    public static final CreativeTabReg neoKuayueMainTab = new CreativeTabReg("main")
            .icon(() -> AllBlocks.CR_LOGO.itemInstance().getDefaultInstance())
            .submit(testRegistry);

    public static final CreativeTabReg neoKuayueLocoTab = new CreativeTabReg("loco")
            .icon(() -> AllItems.LOCO_LOGOS.getItem().getDefaultInstance())
            .submit(testRegistry);

    public static final CreativeTabReg neoKuayueCarriageTab = new CreativeTabReg("carriage")
            .icon(() -> AllItems.SERIES_25_LOGOS.getItem().getDefaultInstance())
            .submit(testRegistry);

    public static final CreativeTabReg neoKuayueDietTab = new CreativeTabReg("diet")
            .icon(() -> AllItems.CA_25T.getItem().getDefaultInstance())
            .submit(testRegistry);

    public static final CreativeTabReg neoKuayueDeviceTab = new CreativeTabReg("device")
            .icon(() -> AllDeviceItems.ITEM_LOGO.getItem().getDefaultInstance())
            .submit(testRegistry);
    public static final CreativeTabReg neoKuayueMaterialTab = new CreativeTabReg("materials")
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
        OverheadLineSystem.invoke();
//        FluidsInit.register(testRegistry.eventBus);
//        FluidTypesInit.register(testRegistry.eventBus);
        FeaturesInit.register(testRegistry.eventBus);
        AllFluids.invoke();
        if (Envs.isClient()) {
            ClientInit.invoke();
            Kuayue.BUS.addListener(ClientInit::registerHUDOverlays);
            MinecraftForge.EVENT_BUS.addListener(RenderArrowEvent::renderBlockBounds);
            MinecraftForge.EVENT_BUS.addListener(ColorTemplateEvents::unloadEvent);
            MinecraftForge.EVENT_BUS.addListener(ColorTemplateEvents::saveEvent);
            MinecraftForge.EVENT_BUS.addListener(ColorTemplateEvents::loadEvent);
            MinecraftForge.EVENT_BUS.addListener(PlayerJumpEvents::playerJumpEvent);
            MinecraftForge.EVENT_BUS.addListener(ClientPassengerEvent::onMountEvent);
            MinecraftForge.EVENT_BUS.addListener(ClientTickScheduler::onClientEarlyTick);
            // MinecraftForge.EVENT_BUS.addListener(RenderPrePosedBlockEvent::renderBlock);
            MinecraftForge.EVENT_BUS.register(new CarriageInventoryEvents());
        }
        MinecraftForge.EVENT_BUS.addListener(ServerResourceReloadEvent::onServerResourceReload);
        MinecraftForge.EVENT_BUS.addListener(PlayerDataEvent::onPlayerLogin);
        MinecraftForge.EVENT_BUS.addListener(PlayerDataEvent::onPlayerLogout);
        MinecraftForge.EVENT_BUS.addListener(PlayerDataEvent::onLevelLoad);
        MinecraftForge.EVENT_BUS.addListener(PlayerDataEvent::onLevelSave);
        MinecraftForge.EVENT_BUS.addListener(PlayerDataEvent::addCustomTrades);
        MinecraftForge.EVENT_BUS.addListener(EntityTrackingListener::onEntityUnload);
        testRegistry.submit();
    }
}
