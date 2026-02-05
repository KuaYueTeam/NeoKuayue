package willow.train.kuayue.systems.train_extension.client.overlay.handler;

import com.simibubi.create.AllItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import willow.train.kuayue.systems.train_extension.client.overlay.TrainOverlayContext;
import willow.train.kuayue.systems.train_extension.client.overlay.TrainOverlayRenderer;
import willow.train.kuayue.systems.train_extension.conductor.ConductorHelper;

public class CouplerOverlayHandler implements IContraptionFocusHandler {
    @Override
    public boolean handle(TrainOverlayContext context) {

        if(!AllItems.WRENCH.isIn(context.player().getMainHandItem())) return false;

        BlockPos localPos = context.localPos();
        Direction assemblyDirection = context.cc().getAssemblyDirection();
        int coord = assemblyDirection.getAxis() == Direction.Axis.X ? localPos.getX() : localPos.getZ();
        boolean isLeading = coord * assemblyDirection.getAxisDirection().getStep() < 0;
        boolean canDivide = ConductorHelper.canDivideTrain(context.train(), context.cce().carriageIndex, isLeading);

        if(canDivide) {
            TrainOverlayRenderer.setShowInfo(AllItems.WRENCH.asStack(), Component.translatable("gui.kuayue.coupler.can_divide"));
        } else {
            TrainOverlayRenderer.setShowInfo(AllItems.WRENCH.asStack(), Component.translatable("gui.kuayue.coupler.cannot_divide"));
        }
        TrainOverlayRenderer.setVisible(true);
        return true;
    }
}
