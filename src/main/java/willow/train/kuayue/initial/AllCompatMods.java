package willow.train.kuayue.initial;

import net.minecraftforge.fml.loading.FMLLoader;

public class AllCompatMods {
    public static boolean isRailwaysPresent() {
        return FMLLoader.getLoadingModList().getModFileById("railways") != null;
    }
}
