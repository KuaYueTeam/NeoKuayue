package willow.train.kuayue.mixins.mixin;

import com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.content.trains.track.ITrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import willow.train.kuayue.block.bogey.carriage.ISingleBogeyBlockEntity;

@Mixin(value = StationBlockEntity.class, remap = false)
public class SnrCompatibilityMixinStationBlockEntity {
    @Inject(method = "trackClicked", at = @At("RETURN"), remap = false)
    private void kuaYue$snrCompatibility$reCheckBogeyType(Player player, InteractionHand hand, ITrackBlock track, BlockState state, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        if(!cir.getReturnValue())
            return;
        Level level = ((StationBlockEntity)(Object) this).getLevel();
        if(level == null || level.isClientSide())
            return;

        if(
                level.getBlockEntity(pos.above()) instanceof AbstractBogeyBlockEntity bogeyBlockEntity &&
                bogeyBlockEntity instanceof ISingleBogeyBlockEntity iBe
        ) {
            if(!iBe.isBogeyStyleValid(bogeyBlockEntity.getStyle())){
                bogeyBlockEntity.setBogeyStyle(bogeyBlockEntity.getDefaultStyle());
            }
        }
    }
}
