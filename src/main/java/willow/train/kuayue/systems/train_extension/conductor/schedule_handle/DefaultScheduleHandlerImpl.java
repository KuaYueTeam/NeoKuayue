package willow.train.kuayue.systems.train_extension.conductor.schedule_handle;

import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.nbt.CompoundTag;

public class DefaultScheduleHandlerImpl implements ScheduleHandler{

    @Override
    public void attachScheduleToCarriage(Train train, int carriageIndex) {
        if(train == null) return;

        ScheduleTracker tracker = (ScheduleTracker) train;
        tracker.neoKuayue$setScheduleOwnerIndex(carriageIndex);
    }

    @Override
    public int getScheduleOwner(Train train) {
        if(train == null) return -1;
        if(train.runtime.getSchedule() == null) return -1;
        ScheduleTracker tracker = (ScheduleTracker) train;
        return tracker.neoKuayue$getScheduleOwnerIndex();
    }

    @Override
    public void detachSchedule(Train train) {
        if(train == null) return;
        ScheduleTracker tracker = (ScheduleTracker) train;
        tracker.neoKuayue$setScheduleOwnerIndex(-1);
    }

    @Override
    public boolean isScheduleAttached(Train train) {
        if(train == null) return false;
        return getScheduleOwner(train) != -1 && train.runtime.getSchedule() != null;
    }


    @Override
    public void transferSchedule(Train from, Train to, int targetCarriageIndex) {
        if(from == null || to == null) return;

        to.runtime.read(from.runtime.write());
        attachScheduleToCarriage(to, targetCarriageIndex);
        detachSchedule(from);
    }

    @Override
    public void saveScheduleHolder(Train train, CompoundTag tag) {
        int index = ((ScheduleTracker) train).neoKuayue$getScheduleOwnerIndex();
        tag.putInt("ScheduleHolderIndex", index);
    }

    @Override
    public void readScheduleHolder(Train train, CompoundTag tag) {
        int index = tag.getInt("ScheduleHolderIndex");
        ((ScheduleTracker) train).neoKuayue$setScheduleOwnerIndex(index);
    }
}
