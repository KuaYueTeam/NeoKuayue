package willow.train.kuayue.mixins.mixin;

import com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorage;
import com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorageWrapper;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorage;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageWrapper;
import com.simibubi.create.content.contraptions.MountedStorageManager;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(value = MountedStorageManager.class, remap = false)
public interface AccessorMountedStorageManager {

    // --- 物品存储相关 ---
    @Invoker("reset")
    void invokeReset();

    @Invoker("initialize")
    void invokeInitialize();
    @Accessor("allItemStorages")
    ImmutableMap<BlockPos, MountedItemStorage> getAllItemStorages();

    // 注意：如果是 Builder (HashMap)，在组装时可用
    @Accessor("itemsBuilder")
    Map<BlockPos, MountedItemStorage> getItemsBuilder();

    @Accessor("items")
    MountedItemStorageWrapper getItemsWrapper();


    // --- 流体存储相关 ---

    @Accessor("fluidsBuilder")
    Map<BlockPos, MountedFluidStorage> getFluidsBuilder();

    @Accessor("fluids")
    MountedFluidStorageWrapper getFluidsWrapper();



    // --- 构建状态相关 ---

    @Accessor("syncCooldown")
    int getSyncCooldown();

    @Accessor("syncCooldown")
    void setSyncCooldown(int cooldown);
}