package willow.train.kuayue.block.panels.block_entity.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.block.panels.block_entity.CustomRenderedEndfaceEntity;

public class CustomRenderedEndFaceRenderer implements BlockEntityRenderer<CustomRenderedEndfaceEntity> {
    public static float STEP = 0.025f;

    public CustomRenderedEndFaceRenderer(BlockEntityRendererProvider.Context pContext) {}

    @Override
    public void render(
            CustomRenderedEndfaceEntity pBlockEntity,
            float pPartialTick,
            PoseStack pose,
            MultiBufferSource pBufferSource,
            int pPackedLight,
            int pPackedOverlay) {
        if (pBlockEntity == null || pBlockEntity.getBlockState() == null) return;
        if (pBlockEntity.getLevel() == null) return;
        boolean is_rotate = pBlockEntity.isRotateDoor();
        BlockState blockState = pBlockEntity.getBlockState();
        boolean isOpened = pBlockEntity.isOpen();
        Couple<PartialModel> doorModels = pBlockEntity.getModels();
        if(doorModels == null) return;
        PartialModel left = doorModels.get(true);
        PartialModel right = doorModels.get(false);
        PartialModel frame = pBlockEntity.getFrameModel();
        float f = -blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite().toYRot() - 90;
        SuperByteBuffer leftBuffer = left == null ? null : CachedBuffers.partial(left, blockState);
        SuperByteBuffer rightBuffer = right == null ? null : CachedBuffers.partial(right, blockState);
        pose.pushPose();
        pose.translate(0.5f, -1f, 0.5f);
        pose.mulPose(Axis.YP.rotationDegrees(f + 90f));
        // pose.translate(0, 0, -1);
        if (frame != null) {
            pose.translate(-.5, 2, -.5);
            SuperByteBuffer frameBuffer = CachedBuffers.partial(frame, blockState);
            frameBuffer.renderInto(pose, pBufferSource.getBuffer(RenderType.cutout()));
            pose.translate(.5, -2, .5);
        }
        if (pBlockEntity.type == TrainPanelProperties.DoorType.NO_DOOR) {
            pose.popPose();
            return;
        }
        pose.translate(0, 1, 0);
        pose.mulPose(Axis.YN.rotationDegrees(-90f));
        if (pBlockEntity.type == TrainPanelProperties.DoorType.SLIDE_3) {
            if (leftBuffer != null)
                leftBuffer.translate(-.6f, .13f, .375f);
            if (rightBuffer != null)
                rightBuffer.translate(-.6f, .13f, -.375f);
        } else if (pBlockEntity.type == TrainPanelProperties.DoorType.SLIDE_2) {
            if (leftBuffer != null)
                leftBuffer.translate(-.425f, .13f, .375f);
            if (rightBuffer != null)
                rightBuffer.translate(-.425f, .13f, -.375f);
        } else if (!pBlockEntity.isSingleSided()) {
            if (leftBuffer != null)
                leftBuffer.translate(-.6f, 0, .375f);
            if (rightBuffer != null)
                rightBuffer.translate(-.6f, 0, -.375f);
        } else {
            if (leftBuffer != null) {
                leftBuffer.translate(-.6f, .13f, .5f);
                leftBuffer.rotateY(90);
            }
            if (rightBuffer != null) {
                rightBuffer.translate(-.6f, .13f, -.5f);
                rightBuffer.rotateY(-90);
            }
        }
        float level1 = .4f;
        if(isOpened && pBlockEntity.counter < 1) {
            pBlockEntity.counter += STEP;
        } else if (!isOpened && pBlockEntity.counter > 0) {
            pBlockEntity.counter -= STEP;
        } else if (isOpened) {
            pBlockEntity.counter = 1f;
        } else {
            pBlockEntity.counter = 0f;
        }
        if(!is_rotate) {
            double level2 = .4;
            if (pBlockEntity.type == TrainPanelProperties.DoorType.SLIDE_2) {
                if (leftBuffer != null)
                    leftBuffer.translate(0, 0, (Math.cos((pBlockEntity.counter + 1) * Math.PI) + 1) * level2 / 2);
                if (rightBuffer != null)
                    rightBuffer.translate(0, 0, - (Math.cos((pBlockEntity.counter + 1) * Math.PI) + 1) * level2 / 2);
            } else {
                double distance_1 = .2;
                if (pBlockEntity.counter < level1) {
                    double ctk = (pBlockEntity.counter) / level1;
                    if (leftBuffer != null)
                        leftBuffer.translate(ctk * distance_1, 0, 0);
                    if (rightBuffer != null)
                        rightBuffer.translate(ctk * distance_1, 0, 0);
                } else {
                    double ctk = (pBlockEntity.counter - level1) / (1 - level1);
                    if (leftBuffer != null)
                        leftBuffer.translate(distance_1, 0, ctk * level2);
                    if (rightBuffer != null)
                        rightBuffer.translate(distance_1, 0, -ctk * level2);
                }
            }
        } else {
            float sgn = pBlockEntity.isSingleSided() ? -1 : 1;
            float rotation = (float) (Math.cos((pBlockEntity.counter + 1) * Math.PI) + 1) *
                    (pBlockEntity.isSingleSided() ? 50f : 45f);
            if (leftBuffer != null)
                leftBuffer.rotateY(rotation * sgn);
            if (rightBuffer != null)
                rightBuffer.rotateY(- rotation * sgn);
        }
        if (leftBuffer != null)
            leftBuffer.renderInto(pose, pBufferSource.getBuffer(RenderType.cutout()));
        if (rightBuffer != null)
            rightBuffer.renderInto(pose, pBufferSource.getBuffer(RenderType.cutout()));
        pose.popPose();
    }
}
