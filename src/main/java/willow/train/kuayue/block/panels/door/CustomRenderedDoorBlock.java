package willow.train.kuayue.block.panels.door;


import com.simibubi.create.foundation.block.IBE;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.block.panels.TrainPanelBlock;
import willow.train.kuayue.block.panels.base.TrainPanelShapes;
import willow.train.kuayue.block.panels.block_entity.CustomRenderedDoorEntity;
import willow.train.kuayue.initial.AllBlocks;
import willow.train.kuayue.initial.AllElements;

public class CustomRenderedDoorBlock extends TrainDoorBlock implements IBE<CustomRenderedDoorEntity> {
    boolean isSlideDoor;
    final Couple<PartialModel> leftDoorModels;
    final Couple<PartialModel> rightDoorModels;
    final Vec3 offset;
    final Vec3 openoffset;
    final RenderShape renderShape;
    public CustomRenderedDoorBlock(BlockBehaviour.Properties pProperties,
                                   Couple<ResourceLocation> leftDoorModels,
                                   Couple<ResourceLocation> rightDoorModels,
                                   RenderShape renderShape, boolean isSlideDoor) {
        super(pProperties);
        this.leftDoorModels = Couple.create(block(leftDoorModels.get(true).getPath()), block(leftDoorModels.get(false).getPath()));
        this.rightDoorModels = Couple.create(block(rightDoorModels.get(true).getPath()), block(rightDoorModels.get(false).getPath()));
        this.renderShape = renderShape;
        this.offset = Vec3.ZERO;
        this.openoffset = Vec3.ZERO;
        this.isSlideDoor = isSlideDoor;
    }

    public CustomRenderedDoorBlock(BlockBehaviour.Properties pProperties,
                                   CustomRenderedDoorBlock modelFrom,
                                   RenderShape renderShape, boolean isSlideDoor) {
        super(pProperties);
        this.leftDoorModels = modelFrom.leftDoorModels;
        this.rightDoorModels = modelFrom.rightDoorModels;
        this.renderShape = renderShape;
        this.offset = modelFrom.offset;
        this.openoffset = modelFrom.offset;
        this.isSlideDoor = isSlideDoor;
    }

    public CustomRenderedDoorBlock(BlockBehaviour.Properties properties,
                                   Couple<ResourceLocation> leftDoorModels,
                                   Couple<ResourceLocation> rightDoorModels,
                                   Vec3 offset,
                                   Vec3 openoffset,
                                   RenderShape renderShape, boolean isSlideDoor) {
        super(properties);
        this.leftDoorModels = Couple.create(block(leftDoorModels.get(true).getPath()), block(leftDoorModels.get(false).getPath()));
        this.rightDoorModels = Couple.create(block(rightDoorModels.get(true).getPath()), block(rightDoorModels.get(false).getPath()));
        this.renderShape = renderShape;
        this.offset = offset;
        this.openoffset = openoffset;
        this.isSlideDoor = isSlideDoor;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, BlockState pState) {
        return getBlockEntityType().create(pPos, pState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if(!isSlideDoor)
            return super.getShape(pState, pLevel, pPos, pContext);
        return TrainPanelShapes.getSlidingDoorShape(pState.getValue(TrainPanelBlock.FACING).getOpposite(), pState.getValue(HINGE), pState.getValue(OPEN));
    }

    public Couple<PartialModel> getLeftDoorModels() {
        return leftDoorModels;
    }

    public Couple<PartialModel> getRightDoorModels() {
        return rightDoorModels;
    }

    public Vec3 getOffset() {
        return offset;
    }

    public Vec3 getOpenOffset() {
        return openoffset;
    }

    private static PartialModel block(String path) {
        return PartialModel.of(AllElements.testRegistry.asResource("block/" + path));
    }

    @Override
    public boolean isPathfindable(
            BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return true;
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public Class<CustomRenderedDoorEntity> getBlockEntityClass() {
        return CustomRenderedDoorEntity.class;
    }

    @Override
    public BlockEntityType<CustomRenderedDoorEntity> getBlockEntityType() {
        return AllBlocks.CUSTOM_RENDERED_DOOR_ENTITY.getType();
    }

    @Override
    public BlockState generateCompanyState(Direction direction, DoorHingeSide hingeSide, boolean open) {
        if (!isSlideDoor)
            return super.generateCompanyState(direction, hingeSide, open);
        return AllBlocks.COMPANY_SLIDING_DOOR.instance().defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, direction)
                .setValue(BlockStateProperties.DOOR_HINGE, hingeSide)
                .setValue(BlockStateProperties.OPEN, open);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return renderShape;
    }

    public boolean isSlideDoor() {
        return isSlideDoor;
    }
}
