package willow.train.kuayue.item.animate_controller.executor;

import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import kasuga.lib.core.base.NbtSerializable;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import willow.train.kuayue.item.animate_controller.sensor.AnimateSensorType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class AnimateExecutor implements NbtSerializable {

    public final String name;

    public final AnimateExecutorType type;
    private final List<BlockPos> pos;
    private boolean lock;

    public AnimateExecutor(String name, AnimateExecutorType type) {
        this.name = name;
        this.type = type;
        this.pos = new ArrayList<>();
    }

    public AnimateExecutor(CompoundTag nbt) {
        this.name = nbt.getString("name");
        this.type = AllExecutors.getExecutorType(new ResourceLocation(nbt.getString("type")));
        CompoundTag posTag = nbt.getCompound("pos");
        int size = posTag.getInt("size");
        this.pos = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ListTag tag = posTag.getList(String.valueOf(i), Tag.TAG_INT);
            Vec3i vec3i = NBTHelper.readVec3i(tag);
            pos.add(new BlockPos(vec3i));
        }
    }

    public void addBlock(BlockPos pos) {
        this.pos.add(pos);
    }

    public void removeBlock(BlockPos pos) {
        this.pos.remove(pos);
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isLock() {
        return lock;
    }

    public void execute(Level level, @Nullable AnimateSensorType.PlayerData playerData, @Nullable MovementContext movementContext) {
        if (lock) return;
        type.execute(this, level, playerData, movementContext);
    }

    @Override
    public void write(CompoundTag compoundTag) {
        compoundTag.putString("name", this.name);
        compoundTag.putString("type", this.type.identifier.toString());
        compoundTag.putBoolean("lock", lock);
        CompoundTag posTag = new CompoundTag();
        posTag.putInt("size", pos.size());
        int i = 0;
        for (BlockPos p : pos) {
            ListTag listTag = NBTHelper.writeVec3i(p);
            posTag.put(String.valueOf(i), listTag);
            i++;
        }
        compoundTag.put("pos", posTag);
    }

    @Override
    public void read(CompoundTag compoundTag) {}
}
