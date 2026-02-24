package willow.train.kuayue.systems.train_extension.client.overlay.handler;

import com.simibubi.create.AllItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import willow.train.kuayue.systems.train_extension.client.overlay.TrainOverlayContext;
import willow.train.kuayue.systems.train_extension.client.overlay.TrainOverlayRenderer;

public class PantographOverlayHandler implements IContraptionFocusHandler {
    @Override
    public boolean handle(TrainOverlayContext context) {

        if(!context.state().hasProperty(BlockStateProperties.OPEN)) return false;

        boolean isOpen = context.state().getValue(BlockStateProperties.OPEN);
        if(isOpen) {
            TrainOverlayRenderer.setShowInfo(AllItems.GOGGLES.asStack(), Component.translatable("gui.kuayue.pantograph.open"));
        } else {
            TrainOverlayRenderer.setShowInfo(AllItems.GOGGLES.asStack(), Component.translatable("gui.kuayue.pantograph.closed"));
        }
        TrainOverlayRenderer.setVisible(true);
        return true;
    }
}
