package willow.train.kuayue.systems.device.driver.seat;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;
import willow.train.kuayue.block.seat.M1SeatBlock;
import willow.train.kuayue.block.seat.SeatBlockEntity;
import willow.train.kuayue.systems.device.AllDeviceBlockEntities;

import java.util.List;
import java.util.stream.Collectors;

public class DoubleDriverSeatBlock extends M1SeatBlock implements IContraptionSeatListenerBlock, IDynamicSitBlock, IContraptionActionSeatBlock
{
    public static final EnumProperty<DriverSeatActionType> LEFT_STATE = EnumProperty.create("left_state", DriverSeatActionType.class);
    public static final EnumProperty<DriverSeatActionType> RIGHT_STATE = EnumProperty.create("right_state", DriverSeatActionType.class);

    public DoubleDriverSeatBlock(Properties pProperties)
    {
        super(pProperties.noOcclusion(), 2, (state, index)->{
            return Vec3.ZERO;
        });
        registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(LEFT_STATE, DriverSeatActionType.SIT_DOWN)
                        .setValue(RIGHT_STATE, DriverSeatActionType.SIT_DOWN)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(LEFT_STATE, RIGHT_STATE);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntityType<? extends SeatBlockEntity> getBlockEntityType() {
        return AllDeviceBlockEntities.DOUBLE_DRIVER_SEAT.getType();
    }

    @Override
    public Vec3 getOffset(BlockState state, int index) {
        return getConditionalOffset(state, index);
    }

    private Vec3 getConditionalOffset(BlockState state, int index) {
        Direction facing = state.getValue(FACING);
        DriverSeatActionType left = state.getValue(LEFT_STATE);
        DriverSeatActionType right = state.getValue(RIGHT_STATE);
        DriverSeatActionType action = index == 0 ? left : right;
        List<Vec3> offsets;
        if(action == DriverSeatActionType.SIT_DOWN || action == DriverSeatActionType.STAND){
            offsets = switch (facing) {
                case NORTH -> List.of(
                        new Vec3(0.64, 0, 0),
                        new Vec3(-0.64, 0, 0)
                );
                case SOUTH -> List.of(
                        new Vec3(-0.64, 0, 0),
                        new Vec3(0.64, 0, 0)
                );
                case EAST -> List.of(
                        new Vec3(0, 0, 0.64),
                        new Vec3(0, 0, -0.64)
                );
                case WEST -> List.of(
                        new Vec3(0, 0, -0.64),
                        new Vec3(0, 0, 0.64)
                );
                default -> List.of();
            };
        } else if(action == DriverSeatActionType.WATCHING_DOOR){
            offsets = switch (facing) {
                case NORTH -> List.of(
                        new Vec3(1.6, 0, 0),
                        new Vec3(-1.6, 0, 0)
                );
                case SOUTH -> List.of(
                        new Vec3(-1.6, 0, 0),
                        new Vec3(1.6, 0, 0)
                );
                case EAST -> List.of(
                        new Vec3(0, 0, 1.6),
                        new Vec3(0, 0, -1.6)
                );
                case WEST -> List.of(
                        new Vec3(0, 0, -1.6),
                        new Vec3(0, 0, 1.6)
                );
                default -> List.of();
            };
        } else {
            offsets = List.of();
        }


        if(index < 0 || index >= offsets.size())
            return Vec3.ZERO;
        return offsets.get(index);
    }

    @Override
    public void onCurrentPlayerSitDownOnContraption(Entity passenger, AbstractContraptionEntity contraptionEntity) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                ()->()->MinecraftUtil.createDriverInteractiveScreen(contraptionEntity, contraptionEntity.getContraption())
        );
    }

    @Override
    public void onCurrentPlayerStandUpOnContraption(Entity passenger, AbstractContraptionEntity contraptionEntity) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                ()-> MinecraftUtil::closeCurrentDriverInteractiveScreen
        );
    }


    @Override
    public boolean isSitDown(BlockState state, int i) {
        var status = i == 0 ? state.getValue(LEFT_STATE) : state.getValue(RIGHT_STATE);
        return status == DriverSeatActionType.SIT_DOWN;
    }

    @Override
    public void onAction(ServerPlayer player, Contraption contraption, BlockPos blockPos, StructureTemplate.StructureBlockInfo info, DriverSeatActionType actionType) {
        int index = -1;
        for(int i=0;i<2;i++){
            if(info.nbt.getBoolean("hasSeat"+i)){
                index = i;
                break;
            }
        }
        if(index == -1)
            return;
        BlockState state = info.state;
        if(index == 0){
            state = state.setValue(LEFT_STATE, actionType);
        } else {
            state = state.setValue(RIGHT_STATE, actionType);
        }
        info = new StructureTemplate.StructureBlockInfo(blockPos, state, info.nbt);
        contraption.entity.setBlock(blockPos, info);
    }
        public @NotNull BlockState rotate(BlockState pState, Rotation pRotation) {
        Rotation opposite = Rotation.NONE;
        switch (pRotation) {
            case CLOCKWISE_90 ->  opposite = Rotation.COUNTERCLOCKWISE_90;
            case COUNTERCLOCKWISE_90 ->   opposite = Rotation.CLOCKWISE_90;
            case CLOCKWISE_180 -> opposite = Rotation.CLOCKWISE_180;
        }
        return pState.setValue(FACING, opposite.rotate(pState.getValue(FACING)));
    }
}
