package willow.train.kuayue.block.bogey.carriage;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.content.trains.entity.TravellingPoint;
import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.track.TrackMaterial;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import willow.train.kuayue.block.panels.base.TrainPanelShapes;
import willow.train.kuayue.initial.create.AllCarriageBogeys;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AndesiteCarriageBogeyBlock extends AbstractBogeyBlock<AndesiteCarriageBogeyEntity>
        implements IBE<AndesiteCarriageBogeyEntity>,
        ProperWaterloggedBlock
         {

    private boolean halfHeightTop = false;

    public AndesiteCarriageBogeyBlock(Properties pProperties, BogeySizes.BogeySize size) {
        super(pProperties, size);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    public AndesiteCarriageBogeyBlock(Properties pProperties, BogeySizes.BogeySize size, boolean halfHeightTop) {
        super(pProperties, size);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
        this.halfHeightTop = halfHeightTop;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (!this.halfHeightTop)
            return super.getShape(pState, pLevel, pPos, pContext);
        return TrainPanelShapes.HALF_HEIGHT_TOP_AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public TrackMaterial.TrackType getTrackType(BogeyStyle style) {
        return TrackMaterial.ANDESITE.trackType;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public double getWheelPointSpacing() {
        return 2.5d;
    }

    @Override
    public double getWheelRadius() {
        return 0.915;
    }

    @Override
    protected Vec3 getConnectorAnchorOffset() {
        return new Vec3(0, 7 / 32f, 1);
    }

    @Override
    public BogeyStyle getDefaultStyle() {
        return AllCarriageBogeys.AndesiteCarriageBogeyGroup.getStyle();
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return AllBlocks.RAILWAY_CASING.asStack();
    }

    @Override
    public Class<AndesiteCarriageBogeyEntity> getBlockEntityClass() {
        return AndesiteCarriageBogeyEntity.class;
    }

    @Override
    public BlockEntityType<? extends AndesiteCarriageBogeyEntity> getBlockEntityType() {
        return AllCarriageBogeys.andesiteCarriageBogeyEntity.getType();
    }

    @Override
    public BlockState getRotatedBlockState(BlockState state, Direction targetedFace) {
        return state;
    }

    private final List<Property<?>> properties_to_copy =
            ImmutableList.<Property<?>>builder().addAll(super.propertiesToCopy()).build();

    @Override
    public List<Property<?>> propertiesToCopy() {
        return properties_to_copy;
    }

    @Override
    public Set<TrackMaterial.TrackType> getValidPathfindingTypes(BogeyStyle style) {
        return Set.of(
                getTrackType(style)
        );
    }

    @Override
    public boolean isOnIncompatibleTrack(Carriage carriage, boolean leading) {
        TravellingPoint point = leading ? carriage.getLeadingPoint() : carriage.getTrailingPoint();
        CarriageBogey bogey = leading ? carriage.leadingBogey() : carriage.trailingBogey();
        TrackEdge currentEdge = point.edge;
        if (currentEdge == null)
            return false;
        return !getValidPathfindingTypes(bogey.getStyle()).contains(currentEdge.getTrackMaterial().trackType);
    }
}
