package willow.train.kuayue.block.panels.end_face;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Couple;
import kasuga.lib.KasugaLib;
import lombok.NonNull;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.block.panels.block_entity.CustomRenderedEndfaceEntity;
import willow.train.kuayue.initial.AllBlocks;
import willow.train.kuayue.initial.AllConductorTypes;
import willow.train.kuayue.systems.train_extension.conductor.Conductable;
import willow.train.kuayue.systems.train_extension.conductor.ConductorProvider;
import willow.train.kuayue.systems.train_extension.conductor.ConductorType;

public class CustomRenderedEndfaceBlock extends TrainEndfaceBlock implements IBE<CustomRenderedEndfaceEntity>, ConductorProvider {

    public final Couple<PartialModel> models;
    public final PartialModel frameModel;
    public CustomRenderedEndfaceBlock(BlockBehaviour.Properties pProperties,
                                      TrainPanelProperties.DoorType doorType,
                                    PartialModel leftModel, PartialModel rightModel, PartialModel frameModel) {
        super(pProperties, doorType);
        this.models = Couple.create(leftModel, rightModel);
        this.frameModel = frameModel;
    }

    public CustomRenderedEndfaceBlock(BlockBehaviour.Properties prop,
                                      TrainPanelProperties.DoorType doorType,
                                      ResourceLocation left, ResourceLocation right, ResourceLocation frame) {
        super(prop, doorType);
        this.models = Couple.create(new PartialModel(left), new PartialModel(right));
        this.frameModel = new PartialModel(frame);
    }

    public CustomRenderedEndfaceBlock(BlockBehaviour.Properties properties, TrainPanelProperties.DoorType doorType,
                                      String leftModel, String rightModel, String frameModel) {
        super(properties, doorType);
        this.models = Couple.create(
                leftModel == null ? null : new PartialModel(new ResourceLocation(Kuayue.MODID,"block/" + leftModel)),
                rightModel == null ? null : new PartialModel(new ResourceLocation(Kuayue.MODID,"block/" + rightModel)));
        this.frameModel = new PartialModel(new ResourceLocation(Kuayue.MODID, "block/" + frameModel));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, BlockState pState) {
        return getBlockEntityType().create(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public Class<CustomRenderedEndfaceEntity> getBlockEntityClass() {
        return CustomRenderedEndfaceEntity.class;
    }

    @Override
    public BlockEntityType<? extends CustomRenderedEndfaceEntity> getBlockEntityType() {
        return AllBlocks.CUSTOM_RENDERED_ENDFACE_ENTITY.getType();
    }

    @Override
    public @NonNull ConductorType getType() {
        return AllConductorTypes.JAN;
    }

    @Override
    public @NonNull Conductable modifyConductor(@NonNull Conductable rawConductor) {
        rawConductor.setOffset(1);
        return rawConductor;
    }
}
