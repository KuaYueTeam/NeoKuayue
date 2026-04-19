package willow.train.kuayue.initial.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.systems.editable_panel.EPScreen;

@JeiPlugin
public class KuayueJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Kuayue.MODID, "jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiScreenHandler(EPScreen.class, screen -> null);
    }
}
