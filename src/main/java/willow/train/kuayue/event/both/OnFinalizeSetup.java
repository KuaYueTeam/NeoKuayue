package willow.train.kuayue.event.both;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlockEntity;
import willow.train.kuayue.systems.train_extension.client.overlay.TrainOverlayRegistry;
import willow.train.kuayue.systems.train_extension.conductor.registry.ConductorCandidateRegistry;

public class OnFinalizeSetup {
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        OverheadLineSupportBlockEntity.applyRegistration();
        ConductorCandidateRegistry.invoke();
        TrainOverlayRegistry.invoke();
    }
}
