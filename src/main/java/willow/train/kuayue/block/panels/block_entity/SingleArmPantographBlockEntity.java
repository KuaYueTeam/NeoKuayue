package willow.train.kuayue.block.panels.block_entity;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import willow.train.kuayue.block.panels.pantograph.*;
import willow.train.kuayue.initial.AllBlocks;
import willow.train.kuayue.initial.AllTags;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SingleArmPantographBlockEntity extends SmartBlockEntity implements IContraptionMovementBlockEntity, IPantographBlockEntity {

    @Getter
    private boolean isRisen = true;
    @Getter
    private PantographProps pantographType;
    @Getter
    private float risenSpeed;
    @Getter
    private float downPullRodAngle;
    @Getter
    private float risePullRodAngle;
    public double pullRodAngle = 170.0;
    private double targetAngle;
    @Getter
    private double transPosY = -0.5;

    @Getter
    @Setter
    private CurrOverheadLineCache cache = null;

    public SingleArmPantographBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public SingleArmPantographBlockEntity(BlockPos pos, BlockState state) {
        this(AllBlocks.HXD3D_PANTOGRAPH_ENTITY.getType(), pos, state);
        if (state.getBlock() instanceof SingleArmPantographBlock block) {
            this.pantographType = block.getPantographType();
            this.risenSpeed = block.getRisenSpeed();
            this.downPullRodAngle = block.getDownPullRodAngle();
            this.risePullRodAngle = block.getRisePullRodAngle();
            this.pullRodAngle = block.getDownPullRodAngle();
            this.targetAngle = pullRodAngle;
        }
//        if (level == null)
//            return;
//        BlockState belowBlockstate = level.getBlockState(pos.below());
//        if (!belowBlockstate.getBlock().getDescriptionId().equals("block.kuayue.hxd3d_carport_center")) {
//            this.transPosY = 0;
//        } else {
//            this.transPosY = -0.5;
//        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putBoolean("open", isRisen);
        tag.putDouble("trans_y", transPosY);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.isRisen = tag.getBoolean("open");
        this.transPosY = tag.getDouble("trans_y");
    }

    @Override
    public void setRisen(boolean risen) {
        isRisen = risen;
    }

    @Override
    public double getAngle() {
        return targetAngle;
    }

    @Override
    public IPantographAngleMapping getPantographAngleMapping() {
        return pantographType;
    }

    public void setAngle(double angle) {
        targetAngle = angle;
    }

    @Override
    public void resetAngle() {
        this.targetAngle = (double) downPullRodAngle;
    }

    public double getCenterAngleOf(double angle) {
        return Math.max(Math.min(angle, downPullRodAngle), risePullRodAngle);
    }

    @Override
    public double getYOffset() {
        return transPosY;
    }

    public Map<String, PartialModel> getPantographModel() {
        if (this.level == null)
            return null;
        BlockState state = this.level.getBlockState(this.getBlockPos());
        if (!(state.getBlock() instanceof SingleArmPantographBlock block))
            return null;
        return block.getPantographModel();
    }

    @Override
    public void tick() {
        if(level == null)
            return;
        BlockState state = level.getBlockState(this.getBlockPos());
        if (!(state.getBlock() instanceof SingleArmPantographBlock))
            return;
        this.isRisen = level.getBlockState(this.getBlockPos()).getValue(SingleArmPantographBlock.OPEN);
        BlockState belowBlockstate = level.getBlockState(this.getBlockPos().below());
        if (!belowBlockstate.is(Objects.requireNonNull(AllTags.LOCO_CARPORT.tag()))) {
            this.transPosY = 0;
        } else {
            if (!belowBlockstate.is(Objects.requireNonNull(AllTags.LOCO_CARPORT_10.tag()))) {
                this.transPosY = -0.5;
            } else {
                this.transPosY = -0.375;
            }
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void update(StructureTemplate.StructureBlockInfo info, Player player, BlockPos pos, AbstractContraptionEntity entity) {
        this.isRisen = !isRisen;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return AABB.ofSize(Vec3.atCenterOf(this.getBlockPos()), 5, 5, 5);
    }
}
