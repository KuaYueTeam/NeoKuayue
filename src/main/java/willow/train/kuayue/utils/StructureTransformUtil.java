package willow.train.kuayue.utils;

import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class StructureTransformUtil {

    public static BlockPos getTransformedBlockPos(BlockPos pos, StructureTransform transform) {
        if(pos == null || transform == null) return null;
        return transform.apply(pos);
    }

    public static CompoundTag getTransformedBlockEntityNbt(CompoundTag nbt, StructureTransform transform) {
        if(nbt == null || transform == null) return nbt;
        if(!nbt.contains("x")) return nbt;
        BlockPos pos = new BlockPos(
                nbt.getInt("x"),
                nbt.getInt("y"),
                nbt.getInt("z")
        );
        BlockPos newPos = transform.apply(pos);
        nbt.putInt("x", newPos.getX());
        nbt.putInt("y", newPos.getY());
        nbt.putInt("z", newPos.getZ());
        return nbt;
    }

    public static StructureTemplate.StructureBlockInfo getTransformedStructureBlockInfo(StructureTemplate.StructureBlockInfo blockInfo, StructureTransform transform) {
        if(blockInfo == null || transform == null) return null;
        return new StructureTemplate.StructureBlockInfo(
                getTransformedBlockPos(blockInfo.pos, transform),
                blockInfo.state.rotate(transform.rotation),
                blockInfo.nbt
        );
    }

    public static BlockEntity getTransformedBlockEntity(BlockEntity blockEntity, StructureTransform transform) {
        if(blockEntity == null || transform == null) return null;
        if(blockEntity instanceof ITransformableBlockEntity) {
            transform.apply(blockEntity);
            return blockEntity;
        }

        CompoundTag nbt = blockEntity.saveWithFullMetadata();
        BlockPos pos = blockEntity.getBlockPos();
        BlockPos newPos = transform.apply(pos);
        nbt.putInt("x", newPos.getX());
        nbt.putInt("y", newPos.getY());
        nbt.putInt("z", newPos.getZ());

        BlockEntity newBE = blockEntity.getType().create(newPos, blockEntity.getBlockState().rotate(transform.rotation));
        return newBE;
    }
}
