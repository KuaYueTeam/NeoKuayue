package willow.train.kuayue.systems.overhead_line.constants;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class Utils {

    public static ResourceLocation INVALID = new ResourceLocation("kuayue", "invalid");

    public static void writeBlockPos(BlockPos pos, CompoundTag nbt, String name) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        nbt.put(name, tag);
    }

    public static BlockPos readBlockPos(CompoundTag nbt, String name) {
        CompoundTag tag = nbt.getCompound(name);
        return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }

    public static void writeResourceLocation(ResourceLocation location, CompoundTag nbt, String name) {
        nbt.putString(name, location.toString());
    }

    public static ResourceLocation readResourceLocation(CompoundTag nbt, String name) {
        if (!nbt.contains(name)) return INVALID;
        return new ResourceLocation(nbt.getString(name));
    }
}
