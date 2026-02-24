package willow.train.kuayue.behaviour;

import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.network.PacketDistributor;
import willow.train.kuayue.block.seat.YZSeatBlock;
import willow.train.kuayue.initial.AllPackets;
import willow.train.kuayue.initial.AllTags;
import willow.train.kuayue.network.s2c.ContraptionNbtUpdatePacket;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class SeatClickBehaviour extends MovingInteractionBehaviour {

    @Override
    public boolean handlePlayerInteraction
            (Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
        if(contraptionEntity.level().isClientSide)
            return true; // Update at server side only
        Contraption contraption = contraptionEntity.getContraption();
        StructureTemplate.StructureBlockInfo info = contraption.getBlocks().get(localPos);
        boolean dirty = false;
        CompoundTag tag = info.nbt();
        BlockState state = info.state();
        Block block = state.getBlock();
        if (!(block instanceof YZSeatBlock seatBlock)) return true;
        if (!state.is(Objects.requireNonNull(AllTags.MULTI_SEAT_BLOCK.tag()))) return true;
        int seatSize = seatBlock.getSeatSize();
        if (seatSize == 0) return true;
        UUID[] passengers = new UUID[seatSize];
        UUID id = player.getUUID();
        int seatIndex = contraption.getSeats().indexOf(localPos);
        Collection<Integer> seat = contraption.getSeatMapping().values();
        if (!seat.contains((Integer) seatIndex)) {
            for (int i = 0; i < seatSize; i++) {
                tag.putBoolean("hasSeat" + i, false);
                tag.remove("seat" + i);
            }
            dirty = true;
        }
        int orgIndex = -1;
        for (int i = 0; i < seatSize; i++) {
            boolean hasPassenger = tag.getBoolean("hasSeat" + i);
            passengers[i] = hasPassenger ? tag.getUUID("seat" + i) : null;
            if (id.equals(passengers[i])) orgIndex = i;
        }
        int index = -1, original = (orgIndex == -1 || orgIndex >= seatSize - 1) ? 0 : orgIndex + 1;
        if (orgIndex == -1 || orgIndex >= seatSize - 1) {
            for (int i = 0; i < seatSize; i++) {
                if (passengers[i] == null) {
                    index = i;
                    break;
                }
            }
        } else {
            int j;
            for (int i = 0; i < seatSize - 1; i++) {
                j = (original + i) % seatSize;
                if (passengers[j] == null) {
                    index = j;
                    break;
                }
            }
        }
        if (index == -1) {
            if (dirty)
                writeData(contraptionEntity, contraption, localPos, info, tag, player, seatIndex);
            return true;
        }
        tag.putUUID("seat" + index, player.getUUID());
        tag.putBoolean("hasSeat" + index, true);
        if (orgIndex > -1) {
            tag.remove("seat" + orgIndex);
            tag.putBoolean("hasSeat" + orgIndex, false);
        }

        writeData(contraptionEntity, contraption, localPos, info, tag, player, seatIndex);

        return true;
    }

    public void writeData(AbstractContraptionEntity contraptionEntity, Contraption contraption,
                          BlockPos localPos, StructureTemplate.StructureBlockInfo info,
                          CompoundTag tag, Player player, int seatIndex) {
        contraption.getBlocks().put(localPos, info);
        contraptionEntity.addSittingPassenger(player, seatIndex);
        if (!contraptionEntity.level().isClientSide) {
            AllPackets.INTERACTION.getChannel().send(
                    PacketDistributor.TRACKING_ENTITY.with(()->contraptionEntity),
                    new ContraptionNbtUpdatePacket(contraptionEntity.getId(), localPos, tag)
            );
        }
    }
}
