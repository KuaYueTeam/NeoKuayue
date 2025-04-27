package willow.train.kuayue.systems.overhead_line.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlockEntity;

public class OverheadLineScissorsItem extends Item {
    public OverheadLineScissorsItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        if(pContext.getPlayer() == null){
            return InteractionResult.PASS;
        }

        if(pContext.getLevel().isClientSide()){
            return InteractionResult.SUCCESS;
        }

        Level level = pContext.getLevel();
        BlockEntity clickedBlockEntity = level.getBlockEntity(pContext.getClickedPos());
        if(!(clickedBlockEntity instanceof OverheadLineSupportBlockEntity overheadLineSupportBlockEntity)){
            return InteractionResult.PASS;
        }

        overheadLineSupportBlockEntity.removeAllConnections();

        return InteractionResult.SUCCESS;
    }
}
