package willow.train.kuayue;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.systems.train_extension.TrainExtensionSystem;
import willow.train.kuayue.systems.device.graph.KuaYueRailwayManager;
import willow.train.kuayue.systems.overhead_line.OverheadLineSystem;
import willow.train.kuayue.systems.tech_tree.server.TechTreeManager;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Kuayue.MODID)
public class Kuayue {
    public static final String MODID = "kuayue";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static IEventBus BUS = FMLJavaModLoadingContext.get().getModEventBus();
  
    public static KuaYueRailwayManager RAILWAY = new KuaYueRailwayManager();
    public static final LocalFileEnv LOCAL_FILE = new LocalFileEnv("./kuayue");
    public static final TechTreeManager TECH_TREE = TechTreeManager.MANAGER;
    public static final String TECH_TREE_VERSION = "1.0.0";

    public static final OverheadLineSystem OVERHEAD = new OverheadLineSystem();
    public static final TrainExtensionSystem TRAIN_EXTENSION = new TrainExtensionSystem();

    public Kuayue() {
        BUS.register(this);
        AllElements.invoke();
        KuayueConfig.invoke();
    }
}
