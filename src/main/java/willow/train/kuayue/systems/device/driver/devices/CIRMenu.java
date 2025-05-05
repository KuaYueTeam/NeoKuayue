package willow.train.kuayue.systems.device.driver.devices;

import kasuga.lib.core.menu.base.GuiBinding;
import kasuga.lib.core.menu.base.GuiMenu;
import kasuga.lib.core.menu.base.GuiMenuType;
import kasuga.lib.core.menu.targets.Target;
import kasuga.lib.core.menu.targets.WorldRendererTarget;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.UUID;

public class CIRMenu extends GuiMenu {
    public CIRMenu(GuiMenuType<?> type) {
        super(type);
    }

    @Override
    protected GuiBinding createBinding(UUID id) {
        return
                new GuiBinding(id)
                        .execute(ResourceLocation.tryParse("kuayue:cir"))
                        .with(Target.SCREEN);
    }

    @Override
    protected void createGuiInstance() {
        super.createGuiInstance();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->{
            WorldRendererTarget.attach(this);
        });
    }

    @Override
    protected void closeGuiInstance() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->{
            WorldRendererTarget.detach(this);
        });
        super.closeGuiInstance();
    }
}
