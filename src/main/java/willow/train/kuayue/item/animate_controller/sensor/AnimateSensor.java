package willow.train.kuayue.item.animate_controller.sensor;

import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import kasuga.lib.core.base.NbtSerializable;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.logging.Level;

public class AnimateSensor implements NbtSerializable {

    public final String name;

    public final AnimateSensorType type;

    public final BlockPos pos;
    public final CombineType combineType;
    private boolean lock;
    private float output;
    public AnimateSensor(String name, AnimateSensorType type, CombineType combineType, BlockPos pos) {
        this.name = name;
        this.type = type;
        this.pos = pos;
        this.combineType = combineType;
    }

    public AnimateSensor(CompoundTag nbt) {
        this.name = nbt.getString("name");
        this.type = AllSensors.getSensorType(new ResourceLocation(nbt.getString("type")));
        this.combineType = CombineType.fromString(nbt.getString("combine"));
        this.lock = nbt.getBoolean("lock");
        this.output = nbt.getFloat("output");
        Vec3i vec = NBTHelper.readVec3i(nbt.getList("pos", Tag.TAG_INT));
        this.pos = new BlockPos(vec);
    }

    @Override
    public void write(CompoundTag compoundTag) {
        compoundTag.putString("name", name);
        compoundTag.putString("type", type.identifier.toString());
        compoundTag.putString("combine", combineType.getSerializedName());
        compoundTag.putBoolean("lock", lock);
        compoundTag.putFloat("output", output);
        compoundTag.put("pos", NBTHelper.writeVec3i(this.pos));
    }

    public float getOutput() {
        return output;
    }

    public void setOutput(float output) {
        this.output = output;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isLocked() {
        return lock;
    }

    public void tick(Level level, BlockState blockState, @Nullable AnimateSensorType.PlayerData playerData,
                     @Nullable MovementContext context) {
        if (lock) return;
        this.output = type.tick(this, level, blockState, this.pos, playerData, context, context != null);
    }

    @Override
    public void read(CompoundTag compoundTag) {}

    public enum CombineType implements StringRepresentable {
        AND, OR;

        @Override
        public String getSerializedName() {
            return switch (this) {
                case AND -> "and";
                case OR -> "or";
            };
        }

        public static CombineType fromString(String str) {
            if (str.equals("and")) return AND;
            return OR;
        }
    }
}
