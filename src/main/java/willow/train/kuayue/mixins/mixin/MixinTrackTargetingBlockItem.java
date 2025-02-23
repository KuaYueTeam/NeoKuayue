package willow.train.kuayue.mixins.mixin;

import com.simibubi.create.content.trains.track.TrackTargetingBlockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import willow.train.kuayue.common.OptionalTrackTargetingBlockItem;

@Mixin(value = TrackTargetingBlockItem.class, remap = false)
public class MixinTrackTargetingBlockItem {
    @Inject(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/BlockItem;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"
            )
    )
    public void onPlace(UseOnContext pContext, CallbackInfoReturnable<InteractionResult> cir){
        if((Object)this instanceof OptionalTrackTargetingBlockItem) {
            CompoundTag tag = pContext.getItemInHand().getTag();
            tag.getCompound("BlockEntityTag").putBoolean("TrackPresent", true);
        }
    }
}
