package willow.train.kuayue.block.panels.end_face;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import willow.train.kuayue.block.panels.base.EndFaceShapes;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.block.panels.block_entity.CustomRenderedEndfaceEntity;
import willow.train.kuayue.initial.AllBlocks;

public class MeterCarriageEndFaceBlock extends CustomRenderedEndfaceBlock {

    private final boolean isOutside;

    public MeterCarriageEndFaceBlock(Properties properties, TrainPanelProperties.DoorType doorType, boolean isOutside) {
        super(properties, doorType, (PartialModel) null, null, null);
        this.isOutside = isOutside;
    }

    public MeterCarriageEndFaceBlock(Properties properties, TrainPanelProperties.DoorType doorType,
                                     String leftModel, String rightModel, String frameModel, boolean isOutside) {
        super(properties, doorType, leftModel, rightModel, frameModel);
        this.isOutside = isOutside;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (this.isOutside)
            return EndFaceShapes.getEndFaceShape(
                            pState.getValue(FACING).getOpposite(),
                            DOOR_TYPE, pState.getValue(OPEN)).move(0, 0.5, 0);
        return EndFaceShapes.getInsideEndFaceCloseShape(
                        pState.getValue(FACING).getOpposite()).move(0, 0.5, 0);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (this.isOutside)
            return EndFaceShapes.getEndFaceShape(
                    pState.getValue(FACING).getOpposite(),
                    DOOR_TYPE, pState.getValue(OPEN)).move(0, 0.5, 0);
        return EndFaceShapes.getInsideEndFaceShape(
                pState.getValue(FACING).getOpposite(),
                pState.getValue(OPEN)).move(0, 0.5, 0);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        if (this.DOOR_TYPE == TrainPanelProperties.DoorType.NO_DOOR)
            return RenderShape.MODEL;
        return super.getRenderShape(pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (this.DOOR_TYPE == TrainPanelProperties.DoorType.NO_DOOR)
            return InteractionResult.PASS;
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public BlockEntityType<? extends CustomRenderedEndfaceEntity> getBlockEntityType() {
        return AllBlocks.SINGLE_SLIDING_DOOR_ENTITY.getType();
    }
}
