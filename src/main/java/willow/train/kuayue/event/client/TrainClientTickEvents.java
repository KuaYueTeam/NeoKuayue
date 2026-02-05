package willow.train.kuayue.event.client;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import willow.train.kuayue.systems.train_extension.client.overlay.TrainOverlayContext;
import willow.train.kuayue.systems.train_extension.client.overlay.TrainOverlayRegistry;
import willow.train.kuayue.systems.train_extension.client.overlay.TrainOverlayRenderer;
import willow.train.kuayue.utils.client.ContraptionAimUtil;

public class TrainClientTickEvents {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        updateGuiState();
    }

    public static void updateGuiState() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if(player == null || mc.level == null) {
            return;
        }

        Pair<AbstractContraptionEntity, BlockHitResult> hitResultPair = ContraptionAimUtil.getTargetContraptionBlock(player, 5.0D);
        if(hitResultPair == null || !(hitResultPair.getFirst() instanceof CarriageContraptionEntity cce)) {
            TrainOverlayRenderer.setVisible(false);
            return;
        }

        Carriage carriage = cce.getCarriage();
        if(carriage == null) {
            TrainOverlayRenderer.setVisible(false);
            return;
        }

        Train train = carriage.train;
        if(train == null) {
            TrainOverlayRenderer.setVisible(false);
            return;
        }

        if(!(cce.getContraption() instanceof CarriageContraption cc)) {
            TrainOverlayRenderer.setVisible(false);
            return;
        }

        BlockPos localPos = hitResultPair.getSecond().getBlockPos();
        if(cc.getBlocks().get(localPos) == null) {
            TrainOverlayRenderer.setVisible(false);
            return;
        }

        BlockState blockState = cc.getBlocks().get(localPos).state();

        TrainOverlayContext context = new TrainOverlayContext(
                player,
                mc.level,
                player.getMainHandItem(),
                train,
                carriage,
                cce,
                cc,
                localPos,
                blockState
        );

        boolean handled = TrainOverlayRegistry.process(context);

        if(!handled) {
            TrainOverlayRenderer.clearShowInfo();
            TrainOverlayRenderer.setVisible(false);
        }
    }
}
