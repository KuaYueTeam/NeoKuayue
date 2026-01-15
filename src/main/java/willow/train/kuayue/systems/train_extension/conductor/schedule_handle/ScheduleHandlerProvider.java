package willow.train.kuayue.systems.train_extension.conductor.schedule_handle;

import willow.train.kuayue.initial.AllCompats;

public class ScheduleHandlerProvider {
    private static ScheduleHandler HANDLER;

    public static void init() {
        if(AllCompats.RAILWAYS.isPresent()) {
            HANDLER = AllCompats.RAILWAYS.get();
        } else {
            HANDLER = new DefaultScheduleHandlerImpl();
        }
    }

    public static ScheduleHandler get() {
        if(HANDLER == null) {
            init();
        }
        return HANDLER;
    }
}
