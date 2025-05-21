package willow.train.kuayue.systems.device;

import kasuga.lib.core.menu.base.GuiMenuType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import willow.train.kuayue.systems.device.driver.combustion.InternalCombustionDrivingMenu;
import willow.train.kuayue.systems.device.driver.devices.CIRMenu;
import willow.train.kuayue.systems.device.driver.devices.LKJ2000Menu;

public class AllDevicesMenus {
    public static final GuiMenuType<LKJ2000Menu> LKJ2000 = GuiMenuType.createType(LKJ2000Menu::new);
    public static final GuiMenuType<CIRMenu> CIR = GuiMenuType.createType(CIRMenu::new);

    public static final GuiMenuType<InternalCombustionDrivingMenu> INTERNAL_COMBUSTION_DRIVING_MENU = GuiMenuType.createType(InternalCombustionDrivingMenu::new);
}
