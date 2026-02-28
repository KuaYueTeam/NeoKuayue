package willow.train.kuayue.mixins.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.entity.*;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import kasuga.lib.core.util.ComponentHelper;
import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.block.bogey.ISingleSideBogey;
import willow.train.kuayue.systems.train_extension.ExtensionHelper;
import willow.train.kuayue.systems.train_extension.conductor.Conductable;
import willow.train.kuayue.systems.train_extension.conductor.ConductorHelper;
import willow.train.kuayue.systems.train_extension.conductor.ConductorProvider;
import willow.train.kuayue.systems.train_extension.TrainAdditionalData;
import willow.train.kuayue.utils.StationMixinCache;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;

@Mixin(StationBlockEntity.class)
public abstract class MixinStationBlockEntity {

    @Shadow(remap = false)
    AbstractBogeyBlock<?>[] bogeyTypes;

    @Shadow(remap = false)
    int[] bogeyLocations;

    @Shadow
    private int bogeyCount;

    @Shadow
    abstract protected void exception(AssemblyException exception, int carriageIndex);

    @Shadow
    protected AssemblyException lastException;

    @Shadow
    @javax.annotation.Nullable
    public abstract GlobalStation getStation();

    @Shadow
    public TrackTargetingBehaviour<GlobalStation> edgePoint;

    @Shadow
    public abstract Direction getAssemblyDirection();

    @Shadow
    public abstract boolean tryDisassembleTrain(@Nullable ServerPlayer sender);

    @Redirect(method = "assemble", at = @At(value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
            remap = false)
    public boolean tryInject(List instance, Object e) {
        if (!(e instanceof Double value)) {
            StationMixinCache.instance = null;
            return instance.add(e);
        }
        if (StationMixinCache.instance == null) return instance.add(value);
        int index = StationMixinCache.instance.index();
        AbstractBogeyBlock bogeyType = StationMixinCache.instance.bogey();
        if (index == -1) instance.add(e);
        int loc = bogeyLocations[index];
        double bogeySize = StationMixinCache.instance.bogeySpacing();

        if (bogeyType instanceof ISingleSideBogey single) {
            double frontOffset = single.getFrontOffset();
            double backOffset = single.getBackOffset();
            double front = (double) loc + 0.5 + frontOffset;
            double back = (double) loc + 0.5 + backOffset;

            if (value.equals(front)) {
                return instance.add(value);
            } else if (value.equals(back)) {
                return instance.add(value);
            } else {
                return instance.add((double) loc + 0.5);
            }
        }
        return instance.add(value);
    }

    @Redirect(method = "assemble", at = @At(value = "INVOKE",
            target = "Lcom/simibubi/create/content/trains/bogey/AbstractBogeyBlock;getWheelPointSpacing()D"),
            remap = false)
    public double getIndex(AbstractBogeyBlock instance) {
        if (!(instance instanceof ISingleSideBogey)) {
            StationMixinCache.instance = null;
            return instance.getWheelPointSpacing();
        }
        int index = -1;
        for (int i = 0; i < bogeyTypes.length; i++) {
            if (instance == bogeyTypes[i]) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            StationMixinCache.instance = new StationMixinCache(index, instance,
                    instance.getWheelPointSpacing());
        } else {
            StationMixinCache.instance = null;
        }
        return instance.getWheelPointSpacing();
    }

    @Inject(method = "trackClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/trains/bogey/AbstractBogeyBlockEntity;setBogeyData(Lnet/minecraft/nbt/CompoundTag;)V"),
            remap = false)
    public void addBogeyChangeText(Player player, InteractionHand hand,
                                   ITrackBlock track, BlockState state,
                                   BlockPos pos, CallbackInfoReturnable<Boolean> cir,
                                   @Local(ordinal = 2) BlockState newBlock) {
        Level level = ((StationBlockEntity)(Object) this).getLevel();
        String descriptionId = newBlock.getBlock().getDescriptionId();
        if(level != null && descriptionId.startsWith("kuayue", 6)) {
            player.displayClientMessage(
                    Component.translatable("msg.bogey.style.changed." +
                            descriptionId), true);
        }
    }

    @Redirect(method = "assemble",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/trains/entity/Carriage;setContraption(Lnet/minecraft/world/level/Level;Lcom/simibubi/create/content/trains/entity/CarriageContraption;)V"),
            remap = false)
    public void onSetContraption(Carriage instance, Level level, CarriageContraption contraption) {
        boolean singleBogey = contraption.getSecondBogeyPos() == null;
        @Nullable Pair<ConductorProvider, Vec2> firstConductor = null;
        @Nullable Pair<ConductorProvider, Vec2> secondConductor = null;
        firstConductor = ConductorHelper.getConductorBlock(
                BlockPos.ZERO,
                contraption.getBlocks(),
                contraption.getAssemblyDirection(),
                true
        );
        if (singleBogey) {
            secondConductor =  ConductorHelper.getConductorBlock(
                    BlockPos.ZERO,
                    contraption.getBlocks(),
                    contraption.getAssemblyDirection(),
                    false
            );
        } else {
            secondConductor =  ConductorHelper.getConductorBlock(
                    BlockPos.ZERO.relative(contraption.getAssemblyDirection(),
                            instance.bogeySpacing),
                    contraption.getBlocks(),
                    contraption.getAssemblyDirection(),
                    false
            );
        }
        Conductable leading = firstConductor != null ? firstConductor.getFirst().
                getType().build(instance.train, instance, true) : null;
        Conductable trailing = secondConductor != null ? secondConductor.getFirst().
                getType().build(instance.train, instance, false) : null;
        if (leading != null) {
            leading = firstConductor.getFirst().modifyConductor(leading);
            leading.setDistanceToAnchor(firstConductor.getSecond());
        }
        if (trailing != null) {
            trailing = secondConductor.getFirst().modifyConductor(trailing);
            trailing.setDistanceToAnchor(secondConductor.getSecond());
        }
        Pair<Conductable, Conductable> conductorPair = Pair.of(leading, trailing);
        TrainAdditionalData data = Kuayue.TRAIN_EXTENSION.getOrCreate(instance.train);
        data.addConductorPair(
                contraption.getBlocks().size(),
                singleBogey ? 1 : 2,
                singleBogey ? BlockPos.ZERO :
                        BlockPos.ZERO.relative(contraption.getAssemblyDirection(),
                        instance.bogeySpacing), conductorPair
        );
        Kuayue.TRAIN_EXTENSION.syncChange(data);
        instance.setContraption(level, contraption);
    }

    @Inject(method = "assemble", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/station/StationBlockEntity;clearException()V"),
        remap = false, cancellable = true)
    public void onGetStation(UUID playerUUID, CallbackInfo ci) {
        StationBlockEntity sbe = (StationBlockEntity) (Object) this;
        if (sbe.getLevel() == null) return;
        GlobalStation station = getStation();
        if (station == null) return;
        Train train = station.getPresentTrain();
        if (train == null) return;
        int i = 0;
        for (Carriage carriage : train.carriages) {
            if (ExtensionHelper.isCarriageOverweighted(
                    train, carriage, i, carriage.isOnTwoBogeys()
            )) {
                exception(new AssemblyException(
                        ComponentHelper.translatable("msg.train_extension.crash.assemble", train.name)
                ), i + 1);
                station.nearestTrain = new WeakReference<>(null);
                Kuayue.TRAIN_EXTENSION.removeTrain(train.id);
                Kuayue.TRAIN_EXTENSION.syncRemove(train.id);
                ci.cancel();
            }
            i++;
        }
    }
}
