package willow.train.kuayue.mixins.mixin.compat.railways;

import com.railwayteam.railways.content.coupling.TrainUtils;
import com.simibubi.create.content.trains.entity.Train;
import net.createmod.catnip.data.Couple;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.systems.train_extension.TrainAdditionalData;
import willow.train.kuayue.systems.train_extension.TrainExtensionSystem;
import willow.train.kuayue.systems.train_extension.conductor.Conductable;
import willow.train.kuayue.systems.train_extension.conductor.ConductorHelper;
import willow.train.kuayue.systems.train_extension.conductor.ConductorLocation;

@Mixin(TrainUtils.class)
public class MixinRailwaysTrainUtils {

    private static int divideIndex = 0;

    @Inject(method = "splitTrain", at = @At(value = "HEAD"), remap = false)
    private static void onSplitTrain(Train train, int numberOffEnd, CallbackInfoReturnable<Train> cir) {
        divideIndex = train.carriages.size() - numberOffEnd - 1;
    }

    @Inject(method = "splitTrain", at = @At(value = "TAIL"), remap = false)
    private static void afterSplitTrain(Train train, int numberOffEnd, CallbackInfoReturnable<Train> cir) {
        Train newTrain = cir.getReturnValue();
        if(newTrain != null) {
            ConductorHelper.divideTrainExtensionData(train, newTrain, divideIndex);
            TrainAdditionalData locoData = Kuayue.TRAIN_EXTENSION.get(train.id);
            TrainAdditionalData carriageData = Kuayue.TRAIN_EXTENSION.get(newTrain.id);
            if(locoData == null || carriageData == null) {
                return;
            }
            Conductable locoTail = locoData.getConductorAt(
                    new ConductorLocation(train.id, divideIndex, false)
            );
            Conductable carriageHead = carriageData.getConductorAt(
                    new ConductorLocation(newTrain.id, 0, true)
            );
            if(locoTail != null && carriageHead != null) {
                Kuayue.TRAIN_EXTENSION.conductorsCoolingDown.put(
                        Couple.create(locoTail.getLoc(), carriageHead.getLoc()),
                        new TrainExtensionSystem.ConductorCDInfo(locoTail, carriageHead)
                );
            }

        }
    }

    @Inject(
            method = "combineTrains(Lcom/simibubi/create/content/trains/entity/Train;Lcom/simibubi/create/content/trains/entity/Train;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/Level;I)Lcom/simibubi/create/content/trains/entity/Train;",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/trains/GlobalRailwayManager;removeTrain(Ljava/util/UUID;)V"),
            remap = false
    )
    private static void combineTrains(Train frontTrain, Train backTrain, Vec3 itemDropPos, Level itemDropLevel, int carriageSpacing, CallbackInfoReturnable<Train> cir) {
        ConductorHelper.mergeTrainExtensionData(frontTrain, backTrain, false, false);
    }
}
