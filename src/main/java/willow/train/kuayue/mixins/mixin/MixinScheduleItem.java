package willow.train.kuayue.mixins.mixin;

import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.schedule.ScheduleItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import willow.train.kuayue.systems.train_extension.conductor.schedule_handle.ScheduleHandlerProvider;

@Mixin(value = ScheduleItem.class, remap = false)
public class MixinScheduleItem {
    @Inject(method = "handScheduleTo", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/schedule/ScheduleRuntime;setSchedule(Lcom/simibubi/create/content/trains/schedule/Schedule;Z)V"))
    private void store(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (pInteractionTarget.getRootVehicle() instanceof CarriageContraptionEntity cce) {
            Carriage carriage = cce.getCarriage();
            Train train = carriage.train;
            ScheduleHandlerProvider.get().attachScheduleToCarriage(train, train.carriages.indexOf(carriage));
        }
    }
}
