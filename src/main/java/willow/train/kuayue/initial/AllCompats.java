package willow.train.kuayue.initial;

import willow.train.kuayue.initial.compat.railways.RailwayCompat;
import willow.train.kuayue.initial.compat.railways.RailwayCompatImpl;

import java.util.Optional;
import java.util.function.Supplier;

public class AllCompats {
    public static Optional<RailwayCompat> RAILWAYS =
            ((Optional<Supplier<Supplier<RailwayCompat>>>) (AllCompatMods.isRailwaysPresent() ? Optional.of((Supplier<Supplier<RailwayCompat>>) () -> RailwayCompatImpl::new) : Optional.empty()))
                    .map(Supplier::get).map(Supplier::get);

    public static void invoke() {}
}
