package willow.train.kuayue.block.panels.window;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;

public class LevelWindowBlock extends TrainOpenableWindowBlock implements IWrenchable {
    public LevelWindowBlock(Properties pProperties) {
        super(pProperties, -1, 2);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.EAST)
                .setValue(HINGE, DoorHingeSide.LEFT)
                .setValue(OPEN, false)
                .setValue(TrainPanelProperties.TYPE, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(TrainPanelProperties.TYPE));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.getItemInHand(pHand).is(AllItems.WRENCH.get()))
            return InteractionResult.PASS;
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockState rotated = state.cycle(TrainPanelProperties.TYPE);
        if (!rotated.canSurvive(world, context.getClickedPos()))
            return InteractionResult.PASS;

        KineticBlockEntity.switchToBlockState(world, context.getClickedPos(), updateAfterWrenched(rotated, context));

        if (world.getBlockState(context.getClickedPos()) != state)
            IWrenchable.playRotateSound(world, context.getClickedPos());

        return InteractionResult.SUCCESS;
    }
}
