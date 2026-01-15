package willow.train.kuayue.mixins.mixin;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.MountedFluidStorage;
import com.simibubi.create.content.contraptions.MountedStorage;
import com.simibubi.create.content.contraptions.MountedStorageManager;
import net.minecraft.core.BlockPos;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(MountedStorageManager.class)
public interface AccessorMountedStorageManager {
    @Accessor("storage")
    Map<BlockPos, MountedStorage> getStorage();

    @Accessor("storage")
    void setStorage(Map<BlockPos, MountedStorage> storage);

    @Accessor("fluidStorage")
    Map<BlockPos, MountedFluidStorage> getFluidStorage();

    @Accessor("fluidStorage")
    void setFluidStorage(Map<BlockPos, MountedFluidStorage> fluidStorage);

    @Accessor("inventory")
    Contraption.ContraptionInvWrapper getInventory();
}
