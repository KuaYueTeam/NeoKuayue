package willow.train.kuayue.behaviour;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.TrapdoorMovingInteraction;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.initial.AllPackets;
import willow.train.kuayue.network.s2c.TrainDividePacket;
import willow.train.kuayue.systems.train_extension.conductor.ConductorHelper;
import willow.train.kuayue.utils.client.ComponentTranslationTool;

import java.util.UUID;

public class EndfaceMovementBehaviour extends TrapdoorMovingInteraction {
    @Override
    public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
        if(AllItems.WRENCH.isIn(player.getItemInHand(activeHand))) {
            if(player.level().isClientSide) return true;

            return handleWrenchInteraction(player, activeHand, localPos, contraptionEntity);
        }

        return super.handlePlayerInteraction(player, activeHand, localPos, contraptionEntity);
    }

    private boolean handleWrenchInteraction(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
        if(!(contraptionEntity instanceof CarriageContraptionEntity cce) ||
                !(contraptionEntity.getContraption() instanceof CarriageContraption cc)) return false;

        Direction assemblyDirection = cc.getAssemblyDirection();
        int coord = assemblyDirection.getAxis() == Direction.Axis.X ? localPos.getX() : localPos.getZ();
        boolean isLeading = coord * assemblyDirection.getAxisDirection().getStep() < 0;

        int carriageIndex = cce.carriageIndex;
        Train train = cce.getCarriage().train;
        if(train == null) return false;

        boolean canDivide = ConductorHelper.canDivideTrain(train, carriageIndex, isLeading);
        carriageIndex = isLeading ? carriageIndex - 1 : carriageIndex;

        if (canDivide) {
            UUID newTrainId = UUID.randomUUID();
            ConductorHelper.divideTrains(train, newTrainId, carriageIndex, false);
            ConductorHelper.TrainDivideRequest request = new ConductorHelper.TrainDivideRequest(train, newTrainId, carriageIndex);
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if(server != null) {
                server.getPlayerList().getPlayers().forEach(p -> {
                    AllPackets.CHANNEL.sendToClient(
                            new TrainDividePacket(request),
                            p
                    );
                });
            } else {
                Kuayue.LOGGER.debug("Failed to send TrainDividePacket: MinecraftServer is null");
            }

            Vec3 effectPos = cce.toGlobalVector(VecHelper.getCenterOf(localPos), 1);
            ConductorHelper.playEffects(effectPos, cce);

            ComponentTranslationTool.showSuccess(player, "coupler.divide_success", true);
        } else {
            ComponentTranslationTool.showError(player, "coupler.cannot_divide", true);
        }

        return true;
    }
}
