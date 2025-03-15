package willow.train.kuayue.systems.overhead_line.block.support;

import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import willow.train.kuayue.common.OptionalBehaviour;
import willow.train.kuayue.systems.overhead_line.OverheadLineSystem;

import java.util.List;

public class OverheadLineSupportBlockEntity extends SmartBlockEntity {
    public OverheadLineSupportBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public OverheadLineSupportBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(OverheadLineSystem.OVERHEAD_LINE_SUPPORT_BLOCK_ENTITY.getType(), blockPos, blockState);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {

    }
}
