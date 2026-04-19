package willow.train.kuayue.systems.editable_panel.interfaces;

import net.minecraft.nbt.CompoundTag;

@FunctionalInterface
public interface SignNbtValidator {
    boolean validate(CompoundTag dataTag);
}
