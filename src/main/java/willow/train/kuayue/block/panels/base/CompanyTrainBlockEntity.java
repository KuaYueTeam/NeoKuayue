package willow.train.kuayue.block.panels.base;

import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.initial.AllBlocks;
import willow.train.kuayue.utils.DirectionUtil;

public class CompanyTrainBlockEntity extends BlockEntity {
    private BlockPos parentPos = BlockPos.ZERO;
    private Direction direction = Direction.EAST;
    public CompanyTrainBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("px", parentPos.getX());
        pTag.putInt("py", parentPos.getY());
        pTag.putInt("pz", parentPos.getZ());
        NBTHelper.writeEnum(pTag, "direction", direction);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.parentPos = new BlockPos(pTag.getInt("px"), pTag.getInt("py"), pTag.getInt("pz"));
        this.direction = NBTHelper.readEnum(pTag, "direction", Direction.class);
    }

    public CompanyTrainBlockEntity(BlockPos pos, BlockState state) {
        this(AllBlocks.COMPANY_TRAIN_BLOCK_ENTITY.getType(), pos, state);
    }

    public Direction getOldDirection() {
        return direction;
    }

    public void updateDirection(Direction newDirection) {
        if (direction == newDirection) return;
        this.parentPos = DirectionUtil.rotate(this.parentPos, direction, newDirection);
        this.direction = newDirection;
        this.setChanged();
        if (hasLevel() && !level.isClientSide) {
            ((ServerLevel) level).getChunkSource().blockChanged(this.getParentPos());
        }
    }

    public void setParentPos(BlockPos pos, Direction direction) {
        this.parentPos = pos;
        this.direction = direction;
        this.setChanged();
        if (hasLevel() && !level.isClientSide) {
            ((ServerLevel) level).getChunkSource().blockChanged(this.getBlockPos());
        }
    }

    public BlockPos getParentPos() {
        return parentPos;
    }
}
