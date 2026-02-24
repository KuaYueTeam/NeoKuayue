package willow.train.kuayue.systems.train_extension.bogey_weight;

import com.google.gson.JsonObject;
import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class BogeyAdditionalData {

    public final BogeyStyle style;
    public final BogeySizes.BogeySize size;

    public int axleCount;
    public float maxWeightPerAxle;

    public BogeyAdditionalData(BogeyStyle style, BogeySizes.BogeySize size, int axleCount, float maxWeightPerAxle) {
        this.style = style;
        this.size = size;
        this.axleCount = axleCount < 1 ? 2 : axleCount;
        this.maxWeightPerAxle = maxWeightPerAxle <= 0f ? Float.MAX_VALUE : maxWeightPerAxle;
    }

    public BogeyAdditionalData(BogeyStyle style, BogeySizes.BogeySize size, JsonObject obj) {
        this.style = style;
        this.size = size;
        axleCount = getAxleCount(obj, "axle_count", 2);
        maxWeightPerAxle = getWeightPerAxle(obj, "max_weight_per_axle", Float.MAX_VALUE);
    }

    public static int getAxleCount(JsonObject obj, String key, int defaultValue) {
        int result = obj.has(key) ? obj.get(key).getAsInt() : defaultValue;
        return result <= 0 ? defaultValue : result;
    }

    public static float getWeightPerAxle(JsonObject obj, String key, float defaultValue) {
        float result = obj.has(key) ? obj.get(key).getAsFloat() : defaultValue;
        return result <= 0f ? defaultValue : result;
    }

    public BogeyAdditionalData(CompoundTag nbt) {
        this.style = AllBogeyStyles.BOGEY_STYLES.get(new ResourceLocation(nbt.getString("style")));
        Objects.requireNonNull(style);
        ResourceLocation sizeRl = new ResourceLocation(nbt.getString("size"));
        BogeySizes.BogeySize s1 = null;
        for (BogeySizes.BogeySize s : style.validSizes()) {
            if (s.id().equals(sizeRl)) {
                s1 = s;
                break;
            }
        }
        Objects.requireNonNull(s1);
        this.size = s1;
        this.axleCount = nbt.getInt("axle_count");
        this.maxWeightPerAxle = nbt.getFloat("max_weight_per_axle");
    }

    public void write(CompoundTag nbt) {
        nbt.putString("style", style.id.toString());
        nbt.putString("size", size.id().toString());
        nbt.putInt("axle_count", axleCount);
        nbt.putFloat("max_weight_per_axle", maxWeightPerAxle);
    }

    public float maxWeight() {
        return maxWeightPerAxle * (float) axleCount;
    }
}
