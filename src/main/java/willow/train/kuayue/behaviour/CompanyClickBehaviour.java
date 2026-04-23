package willow.train.kuayue.behaviour;

import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import willow.train.kuayue.block.panels.door.TrainDoorBlock;

public class CompanyClickBehaviour extends MovingInteractionBehaviour {

    @Override
    public boolean handlePlayerInteraction
            (Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
        Contraption contraption = contraptionEntity.getContraption();
        CompoundTag tag = contraption.getBlocks().get(localPos).nbt();
        BlockPos offset = new BlockPos(tag.getInt("px"), tag.getInt("py"), tag.getInt("pz"));
        BlockPos parentPos = localPos.offset(offset);
        if (parentPos.equals(localPos)) return true;
        return contraptionEntity.handlePlayerInteraction(player, parentPos, Direction.DOWN, activeHand);
    }

    public static void neoKuayue$doorOpen(BlockState currentState, Contraption contraption, BlockPos pos) {
        if (!(currentState.getBlock() instanceof TrainDoorBlock)) return;
        StructureTemplate.StructureBlockInfo info = contraption.getBlocks().getOrDefault(pos.above(), null);
        if (info == null) return;
        info.state().setValue(DoorBlock.OPEN, currentState.getValue(DoorBlock.OPEN));
        contraption.getBlocks().put(pos.above(), info);
    }
}
