package willow.train.kuayue.common;

import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBlockItem;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class OptionalTrackTargetingBlockItem extends TrackTargetingBlockItem {
    public OptionalTrackTargetingBlockItem(Block pBlock, Properties pProperties, EdgePointType<?> type) {
        super(pBlock, pProperties, type);
    }

    public static <T extends Block> NonNullBiFunction<? super T, Properties, OptionalTrackTargetingBlockItem> ofOptional(EdgePointType<?> type) {
        return (b, p) -> {
            return new OptionalTrackTargetingBlockItem(b, p, type);
        };
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(
                pContext.getPlayer() == null ||
                        (
                                !pContext.getPlayer().isShiftKeyDown() &&
                                !pContext.getItemInHand().hasTag() &&
                                !(pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() instanceof ITrackBlock)
                        )
        ){
            // Copied from BlockItem#useOn
            InteractionResult interactionresult = this.place(new BlockPlaceContext(pContext));
            if (!interactionresult.consumesAction() && this.isEdible()) {
                InteractionResult useResult = this.use(pContext.getLevel(), pContext.getPlayer(), pContext.getHand()).getResult();
                return useResult == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : useResult;
            } else {
                return interactionresult;
            }
        }
        return super.useOn(pContext);
    }
}
