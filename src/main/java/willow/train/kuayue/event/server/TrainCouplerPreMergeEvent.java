package willow.train.kuayue.event.server;

import kasuga.lib.core.util.data_type.Pair;
import lombok.Getter;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import willow.train.kuayue.systems.train_extension.conductor.Conductable;
import willow.train.kuayue.systems.train_extension.conductor.ConductorHelper;

@Getter
@Cancelable
public class TrainCouplerPreMergeEvent extends Event {
    public final ConductorHelper.MergeEventContext mergeEventContext;
    public final Pair<Pair<Conductable, Vec3>, Pair<Conductable, Vec3>> conductors;
    public final float locoBeforeSpeed;
    public final float carriageBeforeSpeed;

    public TrainCouplerPreMergeEvent(
            ConductorHelper.MergeEventContext mergeEventContext,
            Pair<Pair<Conductable, Vec3>, Pair<Conductable, Vec3>> conductors,
            float locoBeforeSpeed,
            float carriageBeforeSpeed) {
        this.mergeEventContext = mergeEventContext;
        this.conductors = conductors;
        this.locoBeforeSpeed = locoBeforeSpeed;
        this.carriageBeforeSpeed = carriageBeforeSpeed;
    }
}
