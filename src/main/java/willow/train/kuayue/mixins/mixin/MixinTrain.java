package willow.train.kuayue.mixins.mixin;

import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import willow.train.kuayue.event.server.TrainCrashEvent;
import willow.train.kuayue.systems.train_extension.ExtensionHelper;
import willow.train.kuayue.systems.train_extension.TrainExtensionConstants;
import willow.train.kuayue.systems.train_extension.conductor.ConductorHelper;
import willow.train.kuayue.systems.train_extension.conductor.schedule_handle.ScheduleHandlerProvider;
import willow.train.kuayue.systems.train_extension.conductor.schedule_handle.ScheduleTracker;

import java.util.Map;
import java.util.UUID;

@Mixin(Train.class)
public abstract class MixinTrain implements ScheduleTracker {

    @Unique
    private int neoKuayue$scheduleHolderIndex = -1;

    @Shadow
    protected abstract void collideWithOtherTrains(Level level, Carriage carriage);

    @Shadow
    public abstract Pair<Train, Vec3> findCollidingTrain(Level level, Vec3 start, Vec3 end, ResourceKey<Level> dimension);

//    @Redirect(method = "tick", at=@At(value = "INVOKE",
//            target = "Lcom/simibubi/create/content/trains/entity/Train;collideWithOtherTrains(Lnet/minecraft/world/level/Level;Lcom/simibubi/create/content/trains/entity/Carriage;)V"),
//            remap = false)
//    public void doCollideWithOtherTrains(Train instance, Level level, Carriage carriage) {
//        if (instance.derailed)
//            return;
//
//        TravellingPoint trailingPoint = carriage.getTrailingPoint();
//        TravellingPoint leadingPoint = carriage.getLeadingPoint();
//
//        if (leadingPoint.node1 == null || trailingPoint.node1 == null)
//            return;
//        ResourceKey<Level> dimension = leadingPoint.node1.getLocation().dimension;
//        if (!dimension.equals(trailingPoint.node1.getLocation().dimension))
//            return;
//
//        Vec3 start = (instance.speed < 0 ? trailingPoint : leadingPoint).getPosition(instance.graph);
//        Vec3 end = (instance.speed < 0 ? leadingPoint : trailingPoint).getPosition(instance.graph);
//
//        Pair<Train, Vec3> collision = instance.findCollidingTrain(level, start, end, dimension);
//        if (collision == null)
//            return;
//
//        Train train = collision.getFirst();
//
//        double combinedSpeed = Math.abs(instance.speed) + Math.abs(train.speed);
//        if (combinedSpeed > .2f) {
//            Vec3 v = collision.getSecond();
//            level.explode(null, v.x, v.y, v.z, (float) Math.min(3 * combinedSpeed, 5), Explosion.BlockInteraction.NONE);
//        }
//
//        kasuga.lib.core.util.data_type.Pair<Float, Float> speedAfterCrash = ConductorHelper.momentumExchange(instance, train, 0.8f);
//        if (speedAfterCrash == null) {
//            instance.crash();
//            train.crash();
//            return;
//        }
//        ExtensionHelper.gentlyCrash(instance, speedAfterCrash.getFirst());
//        ExtensionHelper.gentlyCrash(train, speedAfterCrash.getSecond());
//    }

    @Redirect(method = "collideWithOtherTrains", at = @At(
            value = "INVOKE", target = "Lcom/simibubi/create/content/trains/entity/Train;findCollidingTrain(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/resources/ResourceKey;)Lcom/simibubi/create/foundation/utility/Pair;"),
    remap = false)
    public Pair<Train, Vec3> doFindCollidingTrain(Train instance, Level otherLeading, Vec3 otherTrailing, Vec3 otherDimension, ResourceKey<Level> start2) {
        Pair<Train, Vec3> pair = findCollidingTrain(otherLeading, otherTrailing, otherDimension, start2);
        if (pair == null) return null;
        TrainExtensionConstants.colliedTrains.put(instance, pair.getFirst());
        return pair;
    }

    @Inject(method = "collideWithOtherTrains", at = @At(
            value = "INVOKE", target = "Lcom/simibubi/create/content/trains/entity/Train;crash()V"),
    remap = false, cancellable = true)
    public void onCrash(Level level, Carriage carriage, CallbackInfo ci) {
        Train train = carriage.train;
        Train other = TrainExtensionConstants.colliedTrains.remove(train);
        if (other == null) return;

        float trainSpeed = (float) train.speed;
        float otherSpeed = (float) other.speed;
        float deltaSpeed = (float) Math.abs(train.speed - other.speed);
        float reverseCoefficient = Math.max(Math.min(
                0.1f / deltaSpeed, 1f), 0f);
        System.out.println("collide speed: " + deltaSpeed +
                ", rC: " + reverseCoefficient);
        kasuga.lib.core.util.data_type.Pair<Float, Float> speedAfterCrash =
                ConductorHelper.momentumExchange(train, other, reverseCoefficient);
        if (speedAfterCrash == null) {
            train.crash();
            other.crash();
            return;
        }
        ExtensionHelper.gentlyCrash(train, speedAfterCrash.getSecond());
        ExtensionHelper.gentlyCrash(other, speedAfterCrash.getFirst());

        MinecraftForge.EVENT_BUS.post(new TrainCrashEvent(
                train.id,
                other.id,
                deltaSpeed,
                reverseCoefficient,
                kasuga.lib.core.util.data_type.Pair.of(trainSpeed, otherSpeed),
                speedAfterCrash
        ));

        ci.cancel();
    }

    @Inject(method = "write", at = @At("RETURN"), remap = false)
    public void afterWrite(DimensionPalette dimensions, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = cir.getReturnValue();

        ScheduleHandlerProvider.get().saveScheduleHolder((Train) (Object) this, tag);
    }

    @Inject(method = "read", at = @At("RETURN"), remap = false)
    private static void afterRead(CompoundTag tag, Map<UUID, TrackGraph> trackNetworks, DimensionPalette dimensions, CallbackInfoReturnable<Train> cir) {
        Train train = cir.getReturnValue();

        ScheduleHandlerProvider.get().readScheduleHolder(train, tag);
    }

    @Override
    public void neoKuayue$setScheduleOwnerIndex(int index) {
        this.neoKuayue$scheduleHolderIndex = index;
    }

    @Override
    public int neoKuayue$getScheduleOwnerIndex() {
        return this.neoKuayue$scheduleHolderIndex;
    }
}
