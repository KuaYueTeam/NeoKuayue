package willow.train.kuayue.initial.compat.railways;

import willow.train.kuayue.systems.train_extension.conductor.schedule_handle.ScheduleHandler;

public interface RailwayCompat extends ScheduleHandler {
    void registerConductors();
}
