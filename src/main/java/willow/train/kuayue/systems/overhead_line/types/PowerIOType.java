package willow.train.kuayue.systems.overhead_line.types;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PowerIOType implements StringRepresentable {
    SOURCE, CONSUME_SUPPLY, SUPPLY_CONSUME, CONSUME, NONE, JUMP;

    @Override
    public @NotNull String getSerializedName() {
        return switch (this) {
            case SOURCE -> "source";
            case CONSUME_SUPPLY -> "consume_supply";
            case SUPPLY_CONSUME -> "supply_consume";
            case CONSUME -> "consume";
            case NONE -> "none";
            case JUMP -> "jump";
        };
    }

    public PowerIOType getType(String input) {
        return switch (input) {
            case "source" -> SOURCE;
            case "consume_supply" -> CONSUME_SUPPLY;
            case "supply_consume" -> SUPPLY_CONSUME;
            case "consume" -> CONSUME;
            case "jump" -> JUMP;
            default -> NONE;
        };
    }

    public boolean isSupplier() {
        return this == SOURCE || this == CONSUME_SUPPLY || this == SUPPLY_CONSUME;
    }

    public boolean isConsumer() {
        return this == CONSUME || this == CONSUME_SUPPLY || this == SUPPLY_CONSUME;
    }

    public boolean isMixed() {
        return this == CONSUME_SUPPLY || this == SUPPLY_CONSUME;
    }
}
