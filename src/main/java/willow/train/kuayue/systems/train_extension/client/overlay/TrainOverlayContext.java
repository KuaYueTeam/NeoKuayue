package willow.train.kuayue.systems.train_extension.client.overlay;

import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record TrainOverlayContext(
        Player player,
        Level level,
        ItemStack heldItem,
        Train train,
        Carriage carriage,
        CarriageContraptionEntity cce,
        CarriageContraption cc,
        BlockPos localPos,
        BlockState state
) {
}
