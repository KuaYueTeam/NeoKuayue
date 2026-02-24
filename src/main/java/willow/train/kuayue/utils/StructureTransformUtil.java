package willow.train.kuayue.utils;

import com.simibubi.create.content.contraptions.StructureTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class StructureTransformUtil {

    public static BlockPos getTransformedBlockPos(BlockPos pos, StructureTransform transform) {
        if(pos == null || transform == null) return null;
        return transform.apply(pos);
    }

    public static CompoundTag getTransformedBlockEntityNbt(CompoundTag nbt, StructureTransform transform) {
        if(nbt == null || transform == null) return nbt;
        if(!nbt.contains("x")) return nbt;
        CompoundTag newNbt = nbt.copy();
        BlockPos pos = new BlockPos(
                nbt.getInt("x"),
                nbt.getInt("y"),
                nbt.getInt("z")
        );
        BlockPos newPos = transform.apply(pos);
        newNbt.putInt("x", newPos.getX());
        newNbt.putInt("y", newPos.getY());
        newNbt.putInt("z", newPos.getZ());
        return nbt;
    }

    public static StructureTemplate.StructureBlockInfo getTransformedStructureBlockInfo(StructureTemplate.StructureBlockInfo blockInfo, StructureTransform transform) {
        if (blockInfo == null || transform == null) return null;

        BlockPos newPos = transform.apply(blockInfo.pos());
        BlockState newState = blockInfo.state().rotate(transform.rotation);

        // 处理 NBT
        CompoundTag nbt = blockInfo.nbt();
        if (nbt != null) {
            CompoundTag newNbt = nbt.copy();
            // 修正 BE 基础坐标
            newNbt.putInt("x", newPos.getX());
            newNbt.putInt("y", newPos.getY());
            newNbt.putInt("z", newPos.getZ());

            if (newNbt.contains("Controller")) {
                BlockPos oldController = NbtUtils.readBlockPos(newNbt.getCompound("Controller"));
                BlockPos newController = transform.apply(oldController);
                newNbt.put("Controller", NbtUtils.writeBlockPos(newController));
            }

            if (newNbt.contains("ConnectedPos")) {
                newNbt.remove("ConnectedPos");
                newNbt.remove("Distance");
            }

            return new StructureTemplate.StructureBlockInfo(newPos, newState, newNbt);
        }

        return new StructureTemplate.StructureBlockInfo(newPos, newState, null);
    }

    public static BlockEntity getTransformedBlockEntity(BlockEntity blockEntity, StructureTransform transform) {
        if (blockEntity == null || transform == null) return null;

        // 1. 获取最原始的数据快照 (NBT)
        CompoundTag nbt = blockEntity.saveWithFullMetadata();

        // 2. 物理坐标变换 (x, y, z)
        BlockPos oldPos = blockEntity.getBlockPos();
        BlockPos newPos = transform.apply(oldPos);

        // 3. 计算旋转后的方块状态 (BlockState)
        BlockState newState = blockEntity.getBlockState().rotate(transform.rotation);

        // 4. 深度处理 NBT (处理坦克控制器、PFI 连接等)
        CompoundTag transformedNbt = nbt.copy();
        transformedNbt.putInt("x", newPos.getX());
        transformedNbt.putInt("y", newPos.getY());
        transformedNbt.putInt("z", newPos.getZ());

        if (transformedNbt.contains("Controller")) {
            BlockPos oldController = NbtUtils.readBlockPos(transformedNbt.getCompound("Controller"));
            BlockPos newController = transform.apply(oldController);
            transformedNbt.put("Controller", NbtUtils.writeBlockPos(newController));
        }

        // 5. 【核心步骤】根据旋转后的状态创建全新的 BE 实例
        BlockEntity newBE = blockEntity.getType().create(newPos, newState);

        if (newBE != null) {
            // 6. 将变换后的数据灌入新实例
            newBE.load(transformedNbt);
            return newBE;
        }

        return null;
    }
}
