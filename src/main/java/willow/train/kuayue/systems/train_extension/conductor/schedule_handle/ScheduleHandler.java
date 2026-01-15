package willow.train.kuayue.systems.train_extension.conductor.schedule_handle;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.schedule.Schedule;
import com.simibubi.create.content.trains.schedule.ScheduleRuntime;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import willow.train.kuayue.mixins.mixin.AccessorMountedStorageManager;
import willow.train.kuayue.mixins.mixin.AccessorTrainCargoManager;
import willow.train.kuayue.systems.train_extension.conductor.ConductorHelper;
import willow.train.kuayue.utils.TrainUtil;

public interface ScheduleHandler {

    void attachScheduleToCarriage(Train train, int carriageIndex);
    // @Return: the owner index of the schedule, -1 if no owner
    int getScheduleOwner(Train train);
    void detachSchedule(Train train);
    boolean isScheduleAttached(Train train);

    void transferSchedule(Train from, Train to, int targetCarriageIndex);

    default void handleMerge(ConductorHelper.MergeContext context) {
        if(context.isClientSide()) return;
        Train loco = context.getLoco();
        Train carriages = context.getCarriages();

        if(loco == null || carriages == null) return;

        int carriagesOwnerIndex = getScheduleOwner(carriages);

        if (carriagesOwnerIndex < 0) return;    //no need to do anything
        else {
            if(isScheduleAttached(loco)) {
                //both have schedule, keep loco's
                boolean saved = detachAndEjectSchedule(carriages, carriages.carriages.get(0).anyAvailableEntity().level());
                if(saved) {
                    TrainUtil.displayInformation(loco, "msg.kuayue.coupler.schedule_saved", true);
                } else {
                    TrainUtil.displayInformation(loco, "msg.kuayue.coupler.schedule_ejected", true);
                }
            } else {
                //only carriages have schedule, transfer to loco
                int targetIndex;
                if(!context.isLocoHead() && context.isCarriageTail()) { // tail - tail
                    targetIndex = loco.carriages.size() + carriages.carriages.size() - carriagesOwnerIndex;
                } else if(context.isLocoHead() && !context.isCarriageTail()) {  // head - head
                    targetIndex = carriages.carriages.size() - carriagesOwnerIndex - 1;
                } else {
                    // loco tail - carriages head
                    targetIndex = loco.carriages.size() + carriagesOwnerIndex;
                }
                transferSchedule(carriages, loco, targetIndex);
            }
        }
    };
    default void handleDivide(ConductorHelper.DivideContext context) {
        if(context.isClientSide()) return;
        Train original = context.getTrain();
        if(original == null || original.runtime.getSchedule() == null) return;

        Train newTrain = Create.RAILWAYS.trains.get(context.getNewTrain());
        if(newTrain == null) return;

        int splitIndex = context.getCarriageIndex();
        int originalOwnerIndex = getScheduleOwner(original);
        if(originalOwnerIndex < 0) return;    //no need to do anything

        if(originalOwnerIndex > splitIndex) {
            int targetIndex = originalOwnerIndex - splitIndex - 1;
            transferSchedule(original, newTrain, targetIndex);
            if(original.runtime.state == ScheduleRuntime.State.IN_TRANSIT) {
                newTrain.runtime.state = ScheduleRuntime.State.PRE_TRANSIT;
            }
            original.runtime.discardSchedule();
        }
    };

    default void saveScheduleHolder(Train train, CompoundTag tag) {};
    default void readScheduleHolder(Train train, CompoundTag tag) {};

    /**
     * detach时刻表
     * 先尝试将时刻表存储到车厢中
     * 如果车厢没有存储空间或者存储失败，则弹出到世界
     * @return 是否成功将时刻表存储到车厢中（false表示弹出到世界）
     */
    default boolean detachAndEjectSchedule(Train train, Level level) {
        if(train.runtime == null || train.runtime.getSchedule() == null) {
            return false;
        }

        int ownerIndex = getScheduleOwner(train);
        if (ownerIndex < 0 || ownerIndex >= train.carriages.size()) {
            ejectToWorld(train, level, 0);
            detachSchedule(train);
            return false;
        }

        Carriage carriage = train.carriages.get(ownerIndex);
        if (carriage == null || carriage.storage == null) {
            ejectToWorld(train, level, ownerIndex);
            detachSchedule(train);
            return false;
        }

        ItemStack scheduleItem = createScheduleItem(train.runtime.getSchedule());
        ItemStack remaining = ItemHandlerHelper.insertItem(
                ((AccessorMountedStorageManager) carriage.storage).getInventory(),
                scheduleItem,
                false
        );
        detachSchedule(train);

        if (!remaining.isEmpty()) {
            ejectItemToWorld(train, level, remaining, ownerIndex);
            return false;
        }
        return true;
    }

    default ItemStack createScheduleItem(Schedule schedule) {
        ItemStack stack = new ItemStack(com.simibubi.create.AllItems.SCHEDULE.get());
        CompoundTag tag = new CompoundTag();
        tag.put("Schedule", schedule.write());
        stack.setTag(tag);
        return stack;
    }

    default void ejectToWorld(Train train, Level level, int carriageIndex) {
        if (train.runtime == null || train.runtime.getSchedule() == null) {
            return;
        }

        ItemStack scheduleItem = createScheduleItem(train.runtime.getSchedule());
        ejectItemToWorld(train, level, scheduleItem, carriageIndex);
    }

    default void ejectItemToWorld(Train train, Level level, ItemStack stack, int carriageIndex) {
        Carriage carriage = carriageIndex >= 0 && carriageIndex < train.carriages.size()
                ? train.carriages.get(carriageIndex)
                : train.carriages.get(0);

        if (carriage == null || carriage.getDimensionalIfPresent(level.dimension()) == null) {
            return;
        }

        var dimensional = carriage.getDimensionalIfPresent(level.dimension());
        if (dimensional == null || dimensional.entity == null) {
            return;
        }

        net.minecraft.world.entity.item.ItemEntity itemEntity = new net.minecraft.world.entity.item.ItemEntity(
                level,
                dimensional.entity.get().getX(),
                dimensional.entity.get().getY() + 1.0,
                dimensional.entity.get().getZ(),
                stack
        );

        itemEntity.setDeltaMovement(
                (level.random.nextFloat() - 0.5) * 0.1,
                0.2,
                (level.random.nextFloat() - 0.5) * 0.1
        );

        level.addFreshEntity(itemEntity);
    }
}
