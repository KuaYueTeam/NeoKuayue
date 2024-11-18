package willow.train.kuayue.event.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.initial.fluid.FluidsInit;

@Mod.EventBusSubscriber(modid = Kuayue.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        ItemBlockRenderTypes.setRenderLayer(FluidsInit.STILL_COLA.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FluidsInit.FLOWING_COLA.get(), RenderType.translucent());
    }
}