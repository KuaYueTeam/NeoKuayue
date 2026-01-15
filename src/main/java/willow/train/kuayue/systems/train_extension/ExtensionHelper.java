package willow.train.kuayue.systems.train_extension;

import com.simibubi.create.content.contraptions.minecart.TrainCargoManager;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import kasuga.lib.core.util.data_type.Pair;
import lombok.NonNull;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.KuayueConfig;
import willow.train.kuayue.mixins.mixin.AccessorCarriageBogey;
import willow.train.kuayue.systems.train_extension.bogey_weight.BogeyAdditionalData;
import willow.train.kuayue.systems.train_extension.bogey_weight.BogeyExtensionSystem;
import willow.train.kuayue.systems.train_extension.conductor.ConductorHelper;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ExtensionHelper {

    public static BogeyAdditionalData getDataForBogey(Carriage carriage, boolean leading) {
        Pair<BogeyStyle, BogeySizes.BogeySize> styleAndSize = getStyleAndSize(carriage.bogeys.get(leading));
        if (!Kuayue.TRAIN_EXTENSION.BOGEY_EXTENSION.hasBogeyData(styleAndSize.getFirst(),  styleAndSize.getSecond()))
            return BogeyExtensionSystem.getDefault();
        return Kuayue.TRAIN_EXTENSION.BOGEY_EXTENSION.getBogeyDataOrDefault(styleAndSize.getFirst(), styleAndSize.getSecond());
    }

    public static Pair<BogeyStyle, BogeySizes.BogeySize> getStyleAndSize(CarriageBogey bogey) {
        AbstractBogeyBlock<?> block = ((AccessorCarriageBogey) bogey).type();
        return Pair.of(bogey.getStyle(), block.getSize());
    }

    public static boolean isCarriageOverweighted(Train train,
                                                 Carriage carriage,
                                                 int carriageIndex,
                                                 boolean hasSecondBogey) {
        if (!KuayueConfig.CONFIG.getBoolValue("BOGEY_WEIGHT_SYS_ENABLE")) return false;
        UUID trainId = train.id;
        BogeyAdditionalData firstBogey = ExtensionHelper.getDataForBogey(carriage, true);
        BogeyAdditionalData secondBogey =
                hasSecondBogey ? ExtensionHelper.getDataForBogey(carriage, false) : null;
        TrainAdditionalData tad = Kuayue.TRAIN_EXTENSION.get(trainId);
        if (tad == null) return false;
        CarriageAdditionalData cad = tad.getCarriages().get(carriageIndex);
        float weight = getDynamicWeight(carriage, cad.blockCount);
        if (secondBogey == null) {
            return weight > firstBogey.maxWeight();
        }
        return weight > firstBogey.maxWeight() + secondBogey.maxWeight();
    }

    public static Pair<Float, Integer> getCarriageWeight(
            Carriage carriage,
            @NonNull Map<BlockPos, StructureTemplate.StructureBlockInfo> blocks
    ) {
        int blockCount = blocks.size();
        TrainCargoManager cargo = carriage.storage;
        if (cargo == null) return Pair.of(0f, blockCount);
        return Pair.of(getCarriageFluidWeight(cargo) + getCarriageFluidWeight(cargo), blockCount);
    }

    public static float getCarriageItemWeight(
            TrainCargoManager cargo) {
        float weight = 0;
        IItemHandlerModifiable items = cargo.getItems();
        if (items == null) return weight;
        int slotSize = items.getSlots();
        for (int i = 0; i < slotSize; i++) {
            ItemStack stack = items.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            weight += ((float) stack.getCount()) * .6f;
        }
        return weight;
    }

    public static float getCarriageFluidWeight(
            TrainCargoManager cargo) {
        float weight = 0;
        IFluidHandler fluids = cargo.getFluids();
        if (fluids == null) return weight;
        int fluidSlotSize =  fluids.getTanks();
        for (int i = 0; i < fluidSlotSize; i++) {
            FluidStack fluid = fluids.getFluidInTank(i);
            weight += ((float) fluid.getAmount()) * .4f;
        }
        return weight;
    }

    public static float getDynamicWeight(Carriage carriage, int blockCount) {
        TrainCargoManager cargo = carriage.storage;
        if (cargo == null) return (float) blockCount;
        return (float) blockCount +
                getCarriageItemWeight(cargo) +
                getCarriageFluidWeight(cargo);
    }

    public static void gentlyCrash(Train train, float crashSpeed) {
        train.navigation.cancelNavigation();
        if (train.derailed)
            return;
        train.speed = crashSpeed;
        if (train.getCurrentStation() != null) {
            train.leaveStation();
        }

        for (Carriage carriage : train.carriages)
            carriage.forEachPresentEntity(e -> e.getIndirectPassengers()
                    .forEach(entity -> {
                        if (!(entity instanceof Player p))
                            return;
                        Optional<UUID> controllingPlayer = e.getControllingPlayer();
                        if (controllingPlayer.isPresent() && controllingPlayer.get()
                                .equals(p.getUUID()))
                            return;
                        AllAdvancements.TRAIN_CRASH.awardTo(p);
                    }));

        if (train.backwardsDriver != null)
            AllAdvancements.TRAIN_CRASH_BACKWARDS.awardTo(train.backwardsDriver);
    }
}
