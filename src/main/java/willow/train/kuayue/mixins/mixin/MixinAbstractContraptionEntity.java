package willow.train.kuayue.mixins.mixin;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import willow.train.kuayue.block.seat.SeatBlockEntity;
import willow.train.kuayue.block.seat.YZSeatBlock;
import willow.train.kuayue.initial.AllTags;
import willow.train.kuayue.systems.device.driver.seat.IContraptionSeatListenerBlock;
import willow.train.kuayue.systems.device.driver.seat.MinecraftUtil;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mixin(value = AbstractContraptionEntity.class, remap = false)
public abstract class MixinAbstractContraptionEntity {

    @Shadow(remap = false)
    protected Contraption contraption;

    @Shadow(remap = false)
    protected abstract float getStalledAngle();

    @Shadow(remap = false)
    public abstract Vec3 toGlobalVector(Vec3 localVec, float partialTicks);

    @Shadow public abstract float getYawOffset();

    @Redirect(method = "handlePlayerInteraction", at = @At(value = "INVOKE", target = "Ljava/util/List;indexOf(Ljava/lang/Object;)I"), remap = false)
    public int doIndexOf(List instance, Object o) {
        BlockPos pos = (BlockPos) o;
        StructureTemplate.StructureBlockInfo info = contraption.getBlocks().get(pos);
        if (info.state().getBlock() instanceof YZSeatBlock && info.state().is(AllTags.MULTI_SEAT_BLOCK.tag())) {
            return -1;
        }
        return instance.indexOf(o);
    }

    @Inject(method = "removePassenger", at = @At("HEAD"), remap = false)
    public void doRemovePassenger(Entity passenger, CallbackInfo ci) {
        BlockPos pos = contraption.getSeatOf(passenger.getUUID());
        if (pos == null) return;
        StructureTemplate.StructureBlockInfo info = contraption.getBlocks().get(pos);
        if(info.state().getBlock() instanceof IContraptionSeatListenerBlock listenerBlock){
            Boolean isLocalPlayer = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, ()->()->MinecraftUtil.isLocalPlayer(passenger));
            if(isLocalPlayer == null) isLocalPlayer = false;
            if(isLocalPlayer){
                listenerBlock.onCurrentPlayerStandUpOnContraption(passenger, (AbstractContraptionEntity) (Object) this);
            }
        }
        if (info.state().getBlock() instanceof YZSeatBlock yzSeatBlock) {
            if (!info.state().is(AllTags.MULTI_SEAT_BLOCK.tag())) return;
            int seatSize = yzSeatBlock.getSeatSize();
            CompoundTag tag = info.nbt();
            int index = -1;
            for (int i = 0; i < seatSize; i++) {
                if (tag.getBoolean("hasSeat" + i)) {
                    if (passenger.getUUID().equals(tag.getUUID("seat" + i))) {
                        index = i;
                        break;
                    }
                }
            }
            if (index == -1) return;
            tag.putBoolean("hasSeat" + index, false);
            tag.remove("seat" + index);
        }
    }

    /**
     * @author MegumiKasuga
     * @reason for yz seat block rendering
     */
    @Overwrite(remap = false)
    public Vec3 getPassengerPosition(Entity passenger, float partialTicks) {
        UUID id = passenger.getUUID();
        if (passenger instanceof OrientedContraptionEntity) {
            BlockPos localPos = contraption.getBearingPosOf(id);
            if (localPos != null)
                return toGlobalVector(VecHelper.getCenterOf(localPos), partialTicks)
                        .add(VecHelper.getCenterOf(BlockPos.ZERO))
                        .subtract(.5f, 1, .5f);
        }

        AABB bb = passenger.getBoundingBox();
        double ySize = bb.getYsize();
        BlockPos seat = contraption.getSeatOf(id);
        if (seat == null)
            return null;
        Vec3 transformedVector = toGlobalVector(Vec3.atLowerCornerOf(seat)
                .add(.5, passenger.getMyRidingOffset() + ySize - .15f, .5), partialTicks)
                .add(VecHelper.getCenterOf(BlockPos.ZERO))
                .subtract(0.5, ySize, 0.5);
        StructureTemplate.StructureBlockInfo info = contraption.getBlocks().get(seat);
        if (!(info.state().getBlock() instanceof YZSeatBlock seatBlock)) {
            return transformedVector;
        }
        if (!info.state().is(Objects.requireNonNull(AllTags.MULTI_SEAT_BLOCK.tag())))
            return transformedVector;
        int seatSize = seatBlock.getSeatSize();
        int index = -1; CompoundTag tag = info.nbt();
        if (passenger.level().isClientSide) {
            BlockEntity entity = contraption.getBlockEntityClientSide(seat);
            if (entity instanceof SeatBlockEntity seatBlockEntity) {
                tag = seatBlockEntity.writeSeatData();
            }
        }
        for (int i = 0; i < seatSize; i++) {
            if(tag.getBoolean("hasSeat" + i) && id.equals(tag.getUUID("seat" + i))) {
                index = i;
                break;
            }
        }
        if (index == -1) return transformedVector;
        Vec3 offset = seatBlock.getOffset(info.state(), index);
        offset = offset.yRot((float) - Math.toRadians(getStalledAngle() + getYawOffset()));
        return transformedVector.add(offset);
    }
}
