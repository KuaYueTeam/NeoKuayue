package willow.train.kuayue.block.panels.block_entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import willow.train.kuayue.block.panels.block_entity.SingleArmPantographBlockEntity;
import willow.train.kuayue.block.panels.pantograph.IPantographModel;
import willow.train.kuayue.block.panels.pantograph.PantographProps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SingleArmPantographRenderer implements
        BlockEntityRenderer<SingleArmPantographBlockEntity>, IPantographModel {

    public static float STEP_FAST = 0.20f;
    public static float STEP_SLOW = 0.05f;

    public SingleArmPantographRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SingleArmPantographBlockEntity pBlockEntity,
                       float pPartialTick, @NotNull PoseStack pose,
                       @NotNull MultiBufferSource buffer, int light, int overlay) {

        BlockState blockState = pBlockEntity.getBlockState();
        boolean risen = pBlockEntity.isRisen();
        float risenSpeed = pBlockEntity.getRisenSpeed();
        float downPullRodAngle = pBlockEntity.getDownPullRodAngle();
        float risePullRodAngle = pBlockEntity.getRisePullRodAngle();
        double transPosY = pBlockEntity.getTransPosY();
        // 获取玩家朝向
        Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        // 各部件固定参数
        PantographProps pantographProps = pBlockEntity.getPantographType();

        if (!pantographProps.isMapping()) {
            pantographProps.heightListInit(pantographProps, risePullRodAngle, downPullRodAngle);
//            System.out.println(pantographProps.getHeightList().toString());
        }

//        List<PantographProps.PantographMapping> heightList = pantographProps.getHeightList();
//
//        Random random = new Random();
//        double randomNumber =
//                random.nextDouble(heightList.get(0).getBowHeight() -
//                        heightList.get(heightList.size() - 1).getBowHeight()) +
//                        heightList.get(heightList.size() - 1).getBowHeight();
//        double pantographAngleByList =
//                pantographProps.getPantographAngleByList(heightList, randomNumber);
//        System.out.println("randomNumber: " + randomNumber + ".angleByList: " + pantographAngleByList);

        // 建模转缓存
        Map<String, PartialModel> pantographModel = pBlockEntity.getPantographModel();
        if (pantographModel == null)
            return;
        PartialModel baseModel = pantographModel.get(BASE_MODEL);
        PartialModel largeArmModel = pantographModel.get(LARGE_ARM_MODEL);
        PartialModel pullRodModel = pantographModel.get(PULL_ROD_MODEL);
        PartialModel smallArmModel = pantographModel.get(SMALL_ARM_MODEL);
        PartialModel bowHeadModel = pantographModel.get(BOW_HEAD_MODEL);

        SuperByteBuffer baseBuffer =
                baseModel == null ? null : CachedBuffers.partial(baseModel, blockState).light(light);
        SuperByteBuffer largeArmBuffer =
                largeArmModel == null ? null : CachedBuffers.partial(largeArmModel, blockState).light(light);
        SuperByteBuffer pullRodBuffer =
                pullRodModel == null ? null : CachedBuffers.partial(pullRodModel, blockState).light(light);
        SuperByteBuffer smallArmBuffer =
                smallArmModel == null ? null : CachedBuffers.partial(smallArmModel, blockState).light(light);
        SuperByteBuffer bowHeadBuffer =
                bowHeadModel == null ? null : CachedBuffers.partial(bowHeadModel, blockState).light(light);

        pose.pushPose();
        pose.translate(0, transPosY, 0);

        if (facing == Direction.NORTH)
            pose.translate(1f, 0, 0);
        if (facing == Direction.SOUTH)
            pose.translate(0, 0, 1f);
        if (facing == Direction.WEST)
            pose.translate(1f, 0, 1f);
        pose.mulPose(Axis.YP.rotationDegrees(facing.toYRot() + 90f));

        // 升降弓拉杆角度变化
//        if (risen && pBlockEntity.pullRodAngle > risePullRodAngle) {
//            pBlockEntity.pullRodAngle -= STEP_SLOW * risenSpeed;
//        } else if (!risen && pBlockEntity.pullRodAngle < downPullRodAngle) {
//            pBlockEntity.pullRodAngle += STEP_SLOW * risenSpeed;
//        } else if (risen) {
//            pBlockEntity.pullRodAngle = risePullRodAngle;
//        } else {
//            pBlockEntity.pullRodAngle = downPullRodAngle;
//        }
        float speedThreshold = 155;

        double targetAngle = pBlockEntity.getAngle();
        // 这地方的算法改成向给定的值靠拢
        if (pBlockEntity.getCache() != null) {
            //pBlockEntity.pullRodAngle = targetAngle.getFirst() + (targetAngle.getSecond() - targetAngle.getFirst()) * pPartialTick;
            if(risen && pBlockEntity.pullRodAngle < targetAngle) {
                pBlockEntity.pullRodAngle += STEP_FAST * risenSpeed * 1.1f;
                if(pBlockEntity.pullRodAngle > targetAngle) {
                    pBlockEntity.pullRodAngle = targetAngle;
                }
            } else if(risen && pBlockEntity.pullRodAngle > targetAngle) {
                pBlockEntity.pullRodAngle -= STEP_FAST * risenSpeed * 1.1f;
                if(pBlockEntity.pullRodAngle < targetAngle) {
                    pBlockEntity.pullRodAngle = targetAngle;
                }
            } else if(!risen && pBlockEntity.pullRodAngle < downPullRodAngle) {
                pBlockEntity.pullRodAngle += STEP_FAST * risenSpeed;
                if(pBlockEntity.pullRodAngle > downPullRodAngle) {
                    pBlockEntity.pullRodAngle = downPullRodAngle;
                }
            } else if(!risen && pBlockEntity.pullRodAngle > downPullRodAngle) {
                pBlockEntity.pullRodAngle -= STEP_FAST * risenSpeed;
                if(pBlockEntity.pullRodAngle > downPullRodAngle) {
                    pBlockEntity.pullRodAngle = downPullRodAngle;
                }
            }
        } else {
            if (risen && pBlockEntity.pullRodAngle > speedThreshold) {
                pBlockEntity.pullRodAngle -= STEP_FAST * risenSpeed;
            } else if (risen && pBlockEntity.pullRodAngle <= speedThreshold &&
                    pBlockEntity.pullRodAngle > risePullRodAngle) {
                pBlockEntity.pullRodAngle -= STEP_SLOW * risenSpeed;
            } else if (!risen && pBlockEntity.pullRodAngle < speedThreshold) {
                pBlockEntity.pullRodAngle += STEP_SLOW * risenSpeed;
            } else if (!risen && pBlockEntity.pullRodAngle >= speedThreshold &&
                    pBlockEntity.pullRodAngle < downPullRodAngle) {
                pBlockEntity.pullRodAngle += STEP_FAST * risenSpeed;
            } else if (risen) {
                pBlockEntity.pullRodAngle = risePullRodAngle;
            } else {
                pBlockEntity.pullRodAngle = downPullRodAngle;
            }
        }

        // 随动角度与坐标
        HashMap<String, Double> pantoModelMap = IPantographModel.getPantoModelMapByType(pantographProps, pBlockEntity.pullRodAngle);
        Double largeArmAngle = pantoModelMap.get(LARGE_ARM_ANGLE);
        Double smallArmPosX = pantoModelMap.get(SMALL_ARM_POS_X);
        Double smallArmPosY = pantoModelMap.get(SMALL_ARM_POS_Y);
        Double smallArmAngle = pantoModelMap.get(SMALL_ARM_ANGLE);
        Double bowHeadPosX = pantoModelMap.get(BOW_HEAD_POS_X);
        Double bowHeadPosY = pantoModelMap.get(BOW_HEAD_POS_Y);

        // 渲染
        if (baseBuffer != null)
            baseBuffer.renderInto(pose, buffer.getBuffer(RenderType.cutout()));

        pose.translate(0, 0.25f, 0.28125f);

        if (largeArmBuffer != null)
            largeArmBuffer.translateZ((float) (8.8 / 16.0)).rotateX((float) -largeArmAngle)
                    .renderInto(pose, buffer.getBuffer(RenderType.cutout()));
        if (pullRodBuffer != null)
            pullRodBuffer.rotateX((float) -pBlockEntity.pullRodAngle)
                    .renderInto(pose, buffer.getBuffer(RenderType.cutout()));
        if (smallArmBuffer != null)
            smallArmBuffer.translateY((float) (smallArmPosY / 16.0)).translateZ((float) (smallArmPosX / 16.0)).rotateX((float) -smallArmAngle)
                    .renderInto(pose, buffer.getBuffer(RenderType.cutout()));
        if (bowHeadBuffer != null)
            bowHeadBuffer.translateY((float) (bowHeadPosY / 16.0)).translateZ((float) (bowHeadPosX / 16.0))
                    .renderInto(pose, buffer.getBuffer(RenderType.cutout()));

        pose.popPose();

        if(Minecraft.getInstance().options.renderDebug){
            pose.pushPose();
            pose.translate(0f,1f,0f);
            pose.scale(-0.02f,-0.02f,-0.02f);
            pose.translate(0, 10, 0);
            Minecraft.getInstance().font.drawInBatch(
                    String.format("isRisen: %s", pBlockEntity.isRisen() ? "true" : "false"),
                    0,
                    0,
                    0xffffff,
                    false,
                    pose.last().pose(),
                    buffer,
                    Font.DisplayMode.NORMAL,
                    0,
                    light
            );
            pose.translate(0, 10, 0);
            Minecraft.getInstance().font.drawInBatch(
                    String.format("hasCache: %s", pBlockEntity.getCache() != null ? "true" : "false"),
                    0,
                    0,
                    0xffffff,
                    false,
                    pose.last().pose(),
                    buffer,
                    Font.DisplayMode.NORMAL,
                    0,
                    light
            );
            pose.translate(0, 10, 0);
            Minecraft.getInstance().font.drawInBatch(
                    String.format("PullRodAngle: %.3f, targetAngle: %.3f",pBlockEntity.pullRodAngle, targetAngle),
                    0,
                    0,
                    0xffffff,
                    false,
                    pose.last().pose(),
                    buffer,
                    Font.DisplayMode.NORMAL,
                    0,
                    light
            );
            pose.popPose();
        }
    }
}
