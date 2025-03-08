package willow.train.kuayue.systems.overhead_line.types;

import net.minecraft.resources.ResourceLocation;

public class OverheadLineType {
    private final ResourceLocation name;
    private final float maxVoltage, maxCurrent, maxLength;

    public OverheadLineType(ResourceLocation name, float maxVoltage, float maxCurrent, float maxLength) {
        this.name = name;
        this.maxVoltage = maxVoltage;
        this.maxCurrent = maxCurrent;
        this.maxLength = maxLength;
    }

    public ResourceLocation getName() {
        return name;
    }

    public float getMaxCurrent() {
        return maxCurrent;
    }

    public float getMaxVoltage() {
        return maxVoltage;
    }

    public float getMaxLength() {return maxLength;}

    public float getMaxPower() {
        return maxCurrent * maxVoltage;
    }


}
