package willow.train.kuayue.block.panels.door;


import com.simibubi.create.foundation.block.IBE;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import kasuga.lib.core.base.UnModeledBlockProperty;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.block.panels.base.TrainPanelShapes;
import willow.train.kuayue.block.panels.block_entity.DoubleDoorEntity;
import willow.train.kuayue.block.panels.end_face.TrainEndfaceBlock;
import willow.train.kuayue.initial.AllBlocks;

public class DoubleDoorBlock extends TrainEndfaceBlock implements IBE<DoubleDoorEntity> {

    public final Couple<PartialModel> models;
    public final PartialModel frameModel;
    public static final UnModeledBlockProperty<Boolean, BooleanProperty> POWERED = new UnModeledBlockProperty<>(BooleanProperty.create("powered"));

    public DoubleDoorBlock(Properties pProperties, Vec2 beginPos, Vec2 endPos, TrainPanelProperties.DoorType doorType,
                           String frameModel, String leftModel, String rightModel) {
        super(pProperties, beginPos, endPos, doorType);
        this.registerDefaultState(getStateDefinition().any().setValue(POWERED, false));
        this.models = Couple.create(
                leftModel == null ? null : PartialModel.of(new ResourceLocation(Kuayue.MODID,"block/" + leftModel)),
                rightModel == null ? null : PartialModel.of(new ResourceLocation(Kuayue.MODID,"block/" + rightModel)));
        this.frameModel = PartialModel.of(new ResourceLocation(Kuayue.MODID, "block/" + frameModel));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(POWERED));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, BlockState pState) {
        return getBlockEntityType().create(pPos, pState);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        boolean flag = level.hasNeighborSignal(pContext.getClickedPos());
        return super.getStateForPlacement(pContext).setValue(OPEN, flag).setValue(POWERED, flag);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return TrainPanelShapes.getDoubleDoorCloseShape(pState.getValue(FACING).getOpposite());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return TrainPanelShapes.getDoubleDoorShape(pState.getValue(FACING).getOpposite(), pState.getValue(OPEN));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean flag = level.hasNeighborSignal(pos);
        if (flag != state.getValue(POWERED)) {
            if (flag != state.getValue(OPEN)) {
                level.playSound(null, pos, flag ? SoundEvents.IRON_DOOR_OPEN :
                        SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS);
                level.gameEvent((Entity) null,
                        flag ? GameEvent.BLOCK_OPEN :
                                GameEvent.BLOCK_CLOSE,
                        pos);
            }
            level.setBlock(pos, state
                    .setValue(POWERED, Boolean.valueOf(flag))
                    .setValue(OPEN, Boolean.valueOf(flag)),
                    2);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public Class<DoubleDoorEntity> getBlockEntityClass() {
        return DoubleDoorEntity.class;
    }

    @Override
    public BlockEntityType<? extends DoubleDoorEntity> getBlockEntityType() {
        return AllBlocks.DOUBLE_DOOR_ENTITY.getType();
    }
}
