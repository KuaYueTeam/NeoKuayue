package willow.train.kuayue.systems.editable_panel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import kasuga.lib.core.client.render.SimpleColor;
import kasuga.lib.core.client.render.texture.ImageMask;
import kasuga.lib.core.util.Envs;
import kasuga.lib.core.util.LazyRecomputable;
import kasuga.lib.registrations.common.BlockTagReg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import willow.train.kuayue.block.panels.TrainPanelBlock;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.block.panels.block_entity.EditablePanelEntity;
import willow.train.kuayue.initial.AllTags;
import willow.train.kuayue.initial.ClientInit;
import willow.train.kuayue.initial.item.EditablePanelItem;
import willow.train.kuayue.systems.editable_panel.interfaces.DefaultTextsLambda;
import willow.train.kuayue.systems.editable_panel.interfaces.SignNbtValidator;
import willow.train.kuayue.systems.editable_panel.interfaces.SignRenderLambda;
import willow.train.kuayue.systems.editable_panel.screens.CustomScreen;
import willow.train.kuayue.systems.editable_panel.widget.Label;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class EditableTypeConstants {

    // @OnlyIn(Dist.CLIENT)
    public static final LazyRecomputable<ImageMask> image = new LazyRecomputable<>(
            () -> ClientInit.noSignTexture.getImageSafe().get().getMask()
                    .rectangleUV(0, 0, 1, 1)
    );

    public static final LazyRecomputable<ImageMask> laqueredBoardLogo = new LazyRecomputable<>(
            () -> ClientInit.laqueredBoardLogo.getImageSafe().get().getMask()
                    .rectangleUV(0, 0, 1, 1)
    );

    public static final LazyRecomputable<ImageMask> laqueredBoardWhiteBg = new LazyRecomputable<>(
            () -> ClientInit.laqueredBoardWhiteBg.getImageSafe().get().getMask()
                    .rectangleUV(0, 0, 1, 1)
    );

    public static final LazyRecomputable<ImageMask> laqueredColorBoard = new LazyRecomputable<>(
            () -> ClientInit.laqueredBoardWhiteBg.getImageSafe().get().getMask()
                    .rectangleUV(0, 0, 1, 1)
    );

    public static final Integer
            YELLOW = 16776961,
            YELLOW2 = 16776960,
            RED = 15216648,
            BLUE = 22220,
            BLUE2 = 0x60A0B0,
            BLUE3 = 468326,
            BLUE4 = 0x2B4CA1,
            WHITE = 0xFFFFFF,
            BLACK = 789516;

    //    TODO 各Renderer中的render方法lambda
    public static final SignRenderLambda CARRIAGE_TYPE_RENDER = (blockEntity, partialTick, pose, bufferSource, packedLight, packedOverlay, unicode) -> {
        float factor = unicode ? 1.2f : 1f;
        PoseStack poseStack = pose.getPoseStack();
        BlockState blockstate = blockEntity.getBlockState();
        boolean revert = blockEntity.getNbt().getBoolean("revert");
        Label[] label = new Label[5];
        for (int i = 0; i < 5; i++) {
            label[i] = new Label(Component.literal(blockEntity.getNbt().getString("data" + i)));
            label[i].setColor(blockEntity.getColor());
        }
        poseStack.pushPose();

        poseStack.translate(0.5d, 0.5d, 0.5d);
        float f = -blockstate.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(f));
        poseStack.translate(0.0d, -0.3d, -0.42d);
        poseStack.translate(blockEntity.getNbt().getFloat("offset_x"),
                blockEntity.getNbt().getFloat("offset_y"), 0);

        // width 1.2，height 0.5
        // scale 0.133

        float size0 = ((float) Minecraft.getInstance().font.width(label[0].getText())) * 0.13f; // 硬座车
        float size1 = ((float) Minecraft.getInstance().font.width(label[1].getText())) * 0.08f; // YINGZUOCHE
        float size2 = ((float) Minecraft.getInstance().font.width(label[2].getText())) * 0.23f; // YZ
        float size3 = ((float) Minecraft.getInstance().font.width(label[3].getText())) * 0.12f; // 25T
        float size4 = ((float) Minecraft.getInstance().font.width(label[4].getText())) * 0.30f; // 345674

        if (revert) {
            poseStack.translate(0.4d - size1 * 0.133f * 0.5f, 0.0, 0.0);
        } else {
            poseStack.translate(-0.4d, 0.0d, 0.0d);
        }
        poseStack.scale(0.133f * 0.55f, -0.133f * 0.55f, 0.133f * 0.55f); // standard size

        poseStack.scale(0.08f, 0.08f, 1.0f);
        label[1].renderToGui(poseStack, Minecraft.getInstance().font);  // 硬座车
        poseStack.scale(12.5f, 12.5f, 1.0f);

        poseStack.translate((size1 - size0) / 2, -1.7 * (2 - factor), 0);

        poseStack.scale(0.13f, 0.18f, 1.0f);
        label[0].renderToGui(poseStack, Minecraft.getInstance().font);  // YINGZUOCHE
        poseStack.scale(7.6923076924f, 5.555555555555f, 1.0f);


        if (revert) {
            poseStack.translate((-size2 - size4 - size3 - 1), unicode ? -0.4 : 0, 0);
        } else {
            poseStack.translate(size1 * factor, unicode ? -0.4 : 0, 0);
        }

        poseStack.scale(0.23f, 0.32f, 1.0f);
        label[2].renderToGui(poseStack, Minecraft.getInstance().font);  // YZ
        poseStack.scale(4.347826086956f, 3.1250f, 1.0f);

        poseStack.translate(size2, 1.6, 0.0);

        poseStack.scale(0.12f, 0.12f, 1.0f);
        label[3].renderToGui(poseStack, Minecraft.getInstance().font);  // 25k
        poseStack.scale(8.333333333333f, 8.333333333333f, 1.0f);

        poseStack.translate(size3 + 1, -1.6, 0.0);

        poseStack.scale(0.26f, 0.32f, 1.0f);
        label[4].renderToGui(poseStack, Minecraft.getInstance().font);  // 345674

        poseStack.popPose();
    };

    public static final SignRenderLambda CARRIAGE_NO_SIGN = (blockEntity, partialTick, pose, bufferSource, packedLight, packedOverlay, unicode) -> {
        PoseStack poseStack = pose.getPoseStack();
        MultiBufferSource buffer = bufferSource.getBuffer();
        Font font = Minecraft.getInstance().font;
        BlockState blockState = blockEntity.getBlockState();
        CompoundTag nbt = blockEntity.getNbt();
        String content = nbt.getString("content");
        Label label = new Label(content);

        poseStack.pushPose();

        poseStack.translate(0.5d, -0.5d, 0.5d);
        float f = -blockState.getValue(TrainPanelBlock.FACING).toYRot();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(f));
        poseStack.translate(0.0, -0.9d, -0.43d);
        poseStack.translate(nbt.getFloat("offset_x"), nbt.getFloat("offset_y"), 0);

        // poseStack.translate(0.45d, 0.0d, 0.0d);
        poseStack.scale(1.5f, 1.5f, 1.5f);
        poseStack.translate(-0.03f, -0.55f, 0.0f);

        image.get().rectangle(new Vector3f(-.5675f, -1.575f, -.5f), ImageMask.Axis.X, ImageMask.Axis.Y, true, true, .125f, .125f);
        image.get().renderToWorld(poseStack, buffer, RenderType.text(image.get().getImage().id), false, packedLight);
        poseStack.translate(0.03f, 0.55f, 0.0f);

        poseStack.scale(0.6666666667f, 0.6666666667f, 0.6666666667f);
        poseStack.translate(0.0, 1.48d, 0.001d);

        poseStack.translate(
                -(0.125f - ((float) font.width(content) * 0.133f * 0.08f)) / 2 + 0.02, 0.0d, 0.001d);
        poseStack.translate(
                -((float) font.width(content) * 0.133f * 0.08f), 0.0d, 0.0d);
        poseStack.scale(0.133f, -0.133f, 0.133f); // standard size
        poseStack.scale(0.08f, 0.08f, 0.08f);
        label.setColor(nbt.getInt("color"));
        label.renderToGui(poseStack, font);

        poseStack.popPose();
    };

    public static final SignRenderLambda LAQUERED_BOARD_SIGN = (blockEntity, partialTick, pose, bufferSource, packedLight, packedOverlay, unicode) -> {
        // 获取方块状态和NBT数据
        BlockState blockstate = blockEntity.getBlockState();
        CompoundTag nbt = blockEntity.getNbt();

        // 创建四个文本标签
        Label leftTop = new Label(Component.literal(nbt.getString("left_top")));
        Label leftBottom = new Label(Component.literal(nbt.getString("left_bottom")));
        Label rightTop = new Label(Component.literal(nbt.getString("right_top")));
        Label rightBottom = new Label(Component.literal(nbt.getString("right_bottom")));
        String laqueredType = nbt.getString("image_type");
        Label trainNumber = new Label(Component.literal(nbt.getString("train_number")));
        String leftTrainLevelString = nbt.getString("left_train_level");
        if (leftTrainLevelString != null && leftTrainLevelString.length() >= 2)
            leftTrainLevelString = leftTrainLevelString.substring(0, 1);
        Label leftTrainLevel = new Label(Component.literal(leftTrainLevelString));
        String rightTrainLevelString = nbt.getString("right_train_level");
        if (rightTrainLevelString != null && rightTrainLevelString.length() >= 2)
            rightTrainLevelString = rightTrainLevelString.substring(0, 1);
        Label rightTrainLevel = new Label(Component.literal(rightTrainLevelString));

        float laqueredFloatX = nbt.getFloat("offset_x");
        float laqueredFloatY = nbt.getFloat("offset_y");

        int boardColor = nbt.getInt("color");
        float boardColorAlpha = nbt.getFloat("alpha");

        // 设置文本颜色
        int color = BLACK;
        leftTop.setColor(color);
        leftBottom.setColor(color);
        rightTop.setColor(color);
        rightBottom.setColor(color);

        // 获取字体渲染器
        Font font = Minecraft.getInstance().font;

        // 计算各文本宽度(用于居中布局)
        float leftTopWidth = font.width(leftTop.getText());
        float leftTopFontLength = (float) font.width(leftTop.getText()) / 9;
        float leftBottomWidth = font.width(leftBottom.getText());
        float leftBottomEngFontLength = (float) font.width(leftBottom.getText()) / 6;
        float rightTopWidth = font.width(rightTop.getText());
        float rightTopFontLength = (float) font.width(rightTop.getText()) / 9;
        float rightBottomWidth = font.width(rightBottom.getText());
        float rightBottomEngFontLength = (float) font.width(rightBottom.getText()) / 6;
        float trainNumberFontLength = (float) font.width(trainNumber.getText()) / 6;
        float leftTrainLevelFontLength = (float) font.width(leftTrainLevel.getText()) / 6;
        float rightTrainLevelFontLength = (float) font.width(rightTrainLevel.getText()) / 6;

        // 开始渲染
        PoseStack poseStack = pose.getPoseStack();
        poseStack.pushPose();

        // 移动到方块中心并应用朝向旋转
        poseStack.translate(0.5d, 0.5d, 0.5d);
        float rotation = -blockstate.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotation));

        // 调整到水牌表面位置
        poseStack.translate(0.0d, -0.3d, -0.406d);

        // 应用自定义偏移
        poseStack.translate(nbt.getFloat("offset_x"), nbt.getFloat("offset_y"), 0);


        // 渲染局标 它的原点在方块的右上角
        MultiBufferSource buffer = bufferSource.getBuffer();
        laqueredBoardLogo.get().rectangle(
                // 上一次是-0.5，y   y值越小（负数越大）则越向上.
                new Vector3f(-0.58f, -0.7025f, -0.519f),
                ImageMask.Axis.X,
                ImageMask.Axis.Y,
                true,
                true,
                .168f,
                .168f
        );
        laqueredBoardLogo.get().renderToWorld(poseStack, buffer, RenderType.text(laqueredBoardLogo.get().getImage().id), false, packedLight);

        // 渲染背景
        MultiBufferSource bgBuffer = bufferSource.getBuffer();
        laqueredBoardWhiteBg.get().rectangle(
                new Vector3f(-1.15f, -0.71f, -0.52f),
                ImageMask.Axis.X,
                ImageMask.Axis.Y,
                true,
                true,
                1.3f,
                .23f
        );
        laqueredBoardWhiteBg.get().renderToWorld(poseStack, bgBuffer, RenderType.text(laqueredBoardWhiteBg.get().getImage().id), false, packedLight);

        // 使用PoseStack 调整图片背景的位置
        // 渲染水牌上的彩条 左
        MultiBufferSource colorBoardLeftBuffer = bufferSource.getBuffer();
        MultiBufferSource colorBoardRightBuffer = bufferSource.getBuffer();
        laqueredColorBoard.get().rectangle(
                new Vector3f(-1.15f, -0.558f, -0.519f),
                ImageMask.Axis.X,
                ImageMask.Axis.Y,
                true,
                true,
                0.5f,
                0.07f
        );
        laqueredColorBoard.get().setColor(SimpleColor.fromRGBA(boardColor, boardColorAlpha));
        laqueredColorBoard.get().renderToWorld(poseStack, colorBoardLeftBuffer, RenderType.text(laqueredColorBoard.get().getImage().id), false, packedLight);

        // 渲染水牌上的彩条 右
        laqueredColorBoard.get().rectangle(
                new Vector3f(-0.35f, -0.558f, -0.519f),
                ImageMask.Axis.X,
                ImageMask.Axis.Y,
                true,
                true,
                0.5f,
                0.07f
        );
        laqueredColorBoard.get().setColor(SimpleColor.fromRGBA(boardColor, boardColorAlpha));
        laqueredColorBoard.get().renderToWorld(poseStack, colorBoardRightBuffer, RenderType.text(laqueredColorBoard.get().getImage().id), false, packedLight);

        // 设置文本基础缩放
        float baseScale = 0.4f * 0.3f;

        MultiBufferSource leftTopBuffer = bufferSource.getBuffer();
        poseStack.scale(baseScale, -baseScale, baseScale);
        poseStack.translate(0,0,-0.2f);
        // 每个中文 9 字符
        float leftTopTextHalfWidth = (float) (leftTopWidth / 2);
        poseStack.pushPose();
        float leftTopTextBaseLine = -5.2f;
        leftTopTextAutoCenter(leftTopTextBaseLine, leftTopFontLength, poseStack, leftTopWidth);
        leftTop.setColor(BLACK);
        leftTop.renderToGui(poseStack, font);
        poseStack.popPose();

        MultiBufferSource rightTopBuffer = bufferSource.getBuffer();
        poseStack.pushPose();
        float rightTopTextHalfWidth = (float) (rightTopWidth / 2);
        float rightBottomTextHalfWidth = (float) (rightBottomWidth / 2);
        float rightTopTextBaseLine = 1.5f;
        rightTopTextAutoCenter(rightTopTextBaseLine, rightTopFontLength, poseStack, rightTopWidth);
        rightTop.setColor(BLACK);
        rightTop.renderToGui(poseStack, font);
        poseStack.popPose();

        // 英文区域，始发地
        // 每个ascii 字符6.0f
        MultiBufferSource leftBottomBuffer = bufferSource.getBuffer();
        poseStack.pushPose();
        float leftBottomTextHalfWidth = (float) (leftBottomWidth / 2);
        leftBottomTextAutoCenter(leftTopTextBaseLine, leftBottomEngFontLength, poseStack, leftBottomWidth);
        leftBottom.setColor(WHITE);
        leftBottom.renderToGui(poseStack, font);
        poseStack.popPose();

        // 英文区域，目的地
        MultiBufferSource rightBottomBuffer = bufferSource.getBuffer();
        poseStack.pushPose();
        rightBottomTextAutoCenter(rightTopTextBaseLine, rightBottomEngFontLength, poseStack, rightBottomWidth);
        rightBottom.setColor(WHITE);

        rightBottom.renderToGui(poseStack, font);
        poseStack.popPose();


        MultiBufferSource trainNumberBuffer = bufferSource.getBuffer();
        float trainNumberBaseLine = -1.0f;
        poseStack.pushPose();
        if (trainNumberFontLength < 13) {
            float missingText = 13 - trainNumberFontLength;
            float v = (missingText * 0.15f) / 2;

            poseStack.translate((trainNumberBaseLine + v), -4.4f, 0.049f);
            poseStack.scale(0.038f, 0.038f, 1.0f);
        } else {
            poseStack.translate(trainNumberBaseLine, -4.4f, 0.049f);
            float k = 0.9f; // 英文行的可显示文字部分的定长
            poseStack.scale(k / (font.width(trainNumber.getText())), 0.038f, 1.0f);
        }
        trainNumber.setColor(BLACK);
        trainNumber.renderToGui(poseStack, font);
        poseStack.popPose();

//        leftTrainLevel
        MultiBufferSource leftTrainLevelBuffer = bufferSource.getBuffer();
        poseStack.pushPose();
        poseStack.translate(-0.2685 - leftTrainLevelFontLength / 2, -5.75f, 0.049f);
        poseStack.scale(0.05f, 0.055f, 1.0f);
        leftTrainLevel.setColor(BLACK);
        leftTrainLevel.renderToGui(poseStack, font);
        poseStack.popPose();

//                rightTrainLevel
        MultiBufferSource rightTrainLevelBuffer = bufferSource.getBuffer();
        poseStack.pushPose();
        poseStack.translate(1.5 - rightTrainLevelFontLength / 2, -5.75f, 0.049f);
        poseStack.scale(0.05f, 0.055f, 1.0f);
        rightTrainLevel.setColor(BLACK);
        rightTrainLevel.renderToGui(poseStack, font);
        poseStack.popPose();

        // 结束渲染
        poseStack.popPose();
    };

    private static void rightBottomTextAutoCenter(float rightBottomTextBaseLine, float rightBottomEngFontLength, PoseStack poseStack, float tempWidth) {

        if (rightBottomEngFontLength < 13) {
            float missingText = 13 - rightBottomEngFontLength;
            float v = (missingText * 0.3f) / 2;
            poseStack.translate(rightBottomTextBaseLine + v, -4.55f, 0.049f);
            poseStack.scale(0.048f, 0.048f, 1.0f);
        } else {
            poseStack.translate((rightBottomTextBaseLine), -4.55f, 0.049f);
            float k = 3.8f; // 英文行的可显示文字部分的定长
            poseStack.scale(k / tempWidth, 0.048f, 1.0f);
        }
    }

    /**
     * 水牌左下英文始发地的自动文字居中计算
     *
     * @param leftTopTextBaseLine
     * @param leftBottomFontLength
     * @param poseStack
     * @param leftBottomTextHalfWidth
     */
    private static void leftBottomTextAutoCenter(float leftBottomTextBaseLine, float leftBottomFontLength, PoseStack poseStack, float tempWidth) {

        if (leftBottomFontLength < 13) {
            float missingText = 13 - leftBottomFontLength;
            float v = (missingText * 0.3f) / 2;
            poseStack.translate(leftBottomTextBaseLine + v, -4.55f, 0.0599999f);
            poseStack.scale(0.048f, 0.048f, 1.0f);
        } else {
            poseStack.translate((leftBottomTextBaseLine), -4.55f, 0.0599999f);
            float k = 3.8f; // 英文行的可显示文字部分的定长
            poseStack.scale(k / tempWidth, 0.048f, 1.0f);
        }
    }

    /**
     * @param rightTopTextBaseLine
     * @param rightTopFontLength
     * @param poseStack
     * @param tempWidth            文字宽度
     */
    private static void rightTopTextAutoCenter(float rightTopTextBaseLine, float rightTopFontLength, PoseStack poseStack, float tempWidth) {
        // 一个中文0.8f
        if (rightTopFontLength <= 4) {
            float missingText = 4 - rightTopFontLength;
            float v = (missingText * 1.f) / 2;
            poseStack.translate(rightTopTextBaseLine + v, -5.6f, 0.0599999f);
            // 文字缩放使用等比
            poseStack.scale(0.098f, 0.098f, 1.0f);
        } else {
            // 如果4中文及以上, 直接使其贴边
            poseStack.translate(rightTopTextBaseLine, -5.6f, 0.0599999f);
            float k = 3.8f; // 中文行的可显示文字部分的定长
            poseStack.scale(k / tempWidth, 0.098f, 1.0f);
        }
    }

    /**
     * @param baseLine
     * @param leftTopFontLength    文字数量（通过文字width / 9得出）
     * @param poseStack
     * @param leftTopTextHalfWidth
     */
    private static void leftTopTextAutoCenter(float baseLine, float leftTopFontLength, PoseStack poseStack, float tempWidth) {
        // 如果文字数量少于4个
        if (leftTopFontLength <= 4) {
            // 计算缺少的文字数量
            float missingText = 4 - leftTopFontLength;
            // 将缺失文字数量的宽度除以2来得到单边文字宽度
            float v = (missingText * 1.1f) / 2;
            // 应用偏移
            poseStack.translate(baseLine + v, -5.6f, 0.0599999f);
            // 文字缩放使用等比
            poseStack.scale(0.098f, 0.098f, 1.0f);
        } else {
            // 如果4中文及以上
            // 得出多余文字的总宽度
            poseStack.translate((baseLine), -5.6f, 0.0599999f);
            float k = 3.8f; // 中文行的可显示文字部分的定长
            poseStack.scale(k / tempWidth, 0.098f, 1.0f);
        }
    }

    public static final SignRenderLambda TRAIN_SPEED_SIGN = (blockEntity, partialTick, pose, bufferSource, packedLight, packedOverlay, unicode) -> {
        BlockState blockState = blockEntity.getBlockState();
        PoseStack poseStack = pose.getPoseStack();
        CompoundTag nbt = blockEntity.getNbt();

        float x_offset = nbt.getFloat("offset_x");
        float y_offset = nbt.getFloat("offset_y");

        String str = nbt.getString("content");
        Label label = new Label(str);

        float text_length = Minecraft.getInstance().font.width(str);
        float text_height = Minecraft.getInstance().font.lineHeight;

        poseStack.pushPose();
        poseStack.translate(0.5d, 0.5d, 0.5d);
        float f = -blockState.getValue(TrainPanelBlock.FACING).getOpposite().toYRot();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(f));

        poseStack.translate(0.0d, 0.1d, 0.501d);
        poseStack.translate(x_offset, y_offset, 0);
        poseStack.scale(1.0f, -1.0f, 1.0f);
        poseStack.scale(.2f, .2f, 1.0f); // standard_size
        poseStack.scale(.1f, .1f, 1.0f);
        poseStack.translate(-text_length / 2, -text_height / 2, 0);

        label.setColor(nbt.getInt("color"));
        label.render(poseStack, 0, 0, partialTick);
        poseStack.popPose();
    };

    public static final DefaultTextsLambda CARRIAGE_TYPE_SIGN_MESSAGES = new DefaultTextsLambda() {
        @Override
        public void defaultTextComponent(BlockEntity blockEntity, BlockState blockState, CompoundTag nbt) {
            nbt.putString("data0", "硬座车");
            nbt.putString("data1", "YINGZUOCHE");
            nbt.putString("data2", "YZ");
            nbt.putString("data3", "25B");
            nbt.putString("data4", "345676");
            nbt.putFloat("offset_x", 0f);
            nbt.putFloat("offset_y", 0f);
        }
    };

    public static final DefaultTextsLambda CARRIAGE_NO_SIGN_MESSAGES = new DefaultTextsLambda() {
        @Override
        public void defaultTextComponent(BlockEntity blockEntity, BlockState blockState, CompoundTag nbt) {
            Component content = Component.literal("1");

            nbt.putInt("color", RED);
            nbt.putString("content", content.getString());
            nbt.putFloat("offset_x", 0);
            nbt.putFloat("offset_y", 0);
        }
    };

    public static final DefaultTextsLambda LAQUERED_BOARD_MESSAGES = new DefaultTextsLambda() {
        // 编写空水牌创建时的默认文本

        @Override
        public void defaultTextComponent(BlockEntity blockEntity, BlockState blockState, CompoundTag nbt) {
//            int type = 0; // 当前处于的种类
            // todo 已有nbt数据的也会因此处的默认文本而覆盖吗？
            // 左侧上方文字 - 如"北京"
            nbt.putString("left_top", "上海");
            // 左侧下方文字 - 如拼音"BEIJING"
            nbt.putString("left_bottom", "SHANGHAI");
            // 右侧上方文字 - 如"上海"
            nbt.putString("right_top", "北京");
            // 右侧下方文字 - 如拼音"SHANGHAI"
            nbt.putString("right_bottom", "BEIJING");
            // 图片标识(保留字段，可用于不同样式水牌)
            nbt.putString("image_type", "default");
            // X轴偏移量
            nbt.putFloat("offset_x", 0f);
            // Y轴偏移量
            nbt.putFloat("offset_y", 0f);
            // 车次
            nbt.putString("train_number", "1462/1");
            // 等级左
            nbt.putString("left_train_level", "普");
            nbt.putString("right_train_level", "快");

            // 彩条颜色 默认不设置，因为创建时color nbt会根据下厢板的不同而不同
            nbt.putFloat("alpha", 1.f);
        }
    };

    public static final DefaultTextsLambda TRAIN_SPEED_SIGN_MESSAGES = new DefaultTextsLambda() {
        @Override
        public void defaultTextComponent(BlockEntity blockEntity, BlockState blockState, CompoundTag nbt) {
            Component content = Component.literal("120 km/h");

            nbt.putInt("color", 0xffffff);
            nbt.putString("content", content.getString());
            nbt.putFloat("offset_x", 0);
            nbt.putFloat("offset_y", 0);
        }
    };

    public static final SignNbtValidator CARRIAGE_TYPE_NBT_VALIDATOR = dataTag -> {
        Set<String> allowedFields = Set.of(
                "color", "data0", "data1", "data2", "data3", "data4", "revert", "offset_x", "offset_y"
        );
        if (!hasOnlyAllowedFields(dataTag, allowedFields)) return false;
        if (!isValidColor(dataTag)) return false;
        for (int i = 0; i <= 4; i++) {
            if (!isValidStringField(dataTag, "data" + i, 100)) return false;
        }
        if (!isValidRevert(dataTag)) return false;
        return isValidOffset(dataTag);
    };

    public static final SignNbtValidator TRAIN_SPEED_NBT_VALIDATOR = dataTag -> {
        Set<String> allowedFields = Set.of("color", "content", "offset_x", "offset_y");
        if (!hasOnlyAllowedFields(dataTag, allowedFields)) return false;
        if (!isValidColor(dataTag)) return false;
        if (!isValidStringField(dataTag, "content", 100)) return false;
        return isValidOffset(dataTag);
    };

    public static final SignNbtValidator CARRIAGE_NO_SIGN_NBT_VALIDATOR = TRAIN_SPEED_NBT_VALIDATOR;

    public static final SignNbtValidator LAQUERED_BOARD_NBT_VALIDATOR = dataTag -> {
        Set<String> allowedFields = Set.of(
                "color", "offset_x", "offset_y",
                "left_top", "right_top", "left_bottom", "right_bottom",
                "image_type", "train_number", "left_train_level", "right_train_level", "alpha"
        );
        if (!hasOnlyAllowedFields(dataTag, allowedFields)) return false;
        if (!isValidColor(dataTag)) return false;
        if (!isValidOffset(dataTag)) return false;
        if (!isValidStringField(dataTag, "left_top", 100)) return false;
        if (!isValidStringField(dataTag, "right_top", 100)) return false;
        if (!isValidStringField(dataTag, "left_bottom", 100)) return false;
        if (!isValidStringField(dataTag, "right_bottom", 100)) return false;
        if (!isValidStringField(dataTag, "image_type", 100)) return false;
        if (!isValidStringField(dataTag, "train_number", 100)) return false;
        if (!isValidStringField(dataTag, "left_train_level", 100)) return false;
        if (!isValidStringField(dataTag, "right_train_level", 100)) return false;
        return isValidAlpha(dataTag);
    };

    private static boolean hasOnlyAllowedFields(CompoundTag dataTag, Set<String> allowedFields) {
        for (String key : dataTag.getAllKeys()) {
            if (!allowedFields.contains(key)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidColor(CompoundTag dataTag) {
        if (!dataTag.contains("color")) return true;
        if (!dataTag.contains("color", CompoundTag.TAG_INT)) return false;
        int color = dataTag.getInt("color");
        return color >= 0 && color <= 0xFFFFFF;
    }

    private static boolean isValidStringField(CompoundTag dataTag, String key, int maxLength) {
        if (!dataTag.contains(key)) return true;
        if (!dataTag.contains(key, CompoundTag.TAG_STRING)) return false;
        return dataTag.getString(key).length() <= maxLength;
    }

    private static boolean isValidRevert(CompoundTag dataTag) {
        if (!dataTag.contains("revert")) return true;
        if (!dataTag.contains("revert", CompoundTag.TAG_BYTE)) return false;
        byte revert = dataTag.getByte("revert");
        return revert == 0 || revert == 1;
    }

    private static boolean isValidOffset(CompoundTag dataTag) {
        if (dataTag.contains("offset_x")) {
            if (!dataTag.contains("offset_x", CompoundTag.TAG_FLOAT)) return false;
            float offsetX = dataTag.getFloat("offset_x");
            if (!Float.isFinite(offsetX) || Math.abs(offsetX) > 10.0f) return false;
        }
        if (dataTag.contains("offset_y")) {
            if (!dataTag.contains("offset_y", CompoundTag.TAG_FLOAT)) return false;
            float offsetY = dataTag.getFloat("offset_y");
            if (!Float.isFinite(offsetY) || Math.abs(offsetY) > 10.0f) return false;
        }
        return true;
    }

    private static boolean isValidAlpha(CompoundTag dataTag) {
        if (!dataTag.contains("alpha")) return true;
        if (!dataTag.contains("alpha", CompoundTag.TAG_FLOAT)) return false;
        float alpha = dataTag.getFloat("alpha");
        return Float.isFinite(alpha) && alpha >= 0.0f && alpha <= 1.0f;
    }

//    TODO 各 Screen类中方法实现类，CarriageTypeSign被挪到Screen类中。
/*
    public static final IEditScreenMethods CARRIAGE_NO_SIGN_METHODS = new IEditScreenMethods() {
        @Override
        public void init(EditablePanelEditScreen screen, EditablePanelEntity entity) {
            System.out.println("车厢编号");
        }

        @Override
        public void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {

        }
    };

    public static final IEditScreenMethods LAQUERED_BOARD_METHODS = new IEditScreenMethods() {
        @Override
        public void init(EditablePanelEditScreen screen, EditablePanelEntity entity) {
            System.out.println("水牌");
        }

        @Override
        public void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {

        }
    };

    public static final IEditScreenMethods TRAIN_SPEED_SIGN_METHODS = new IEditScreenMethods() {
        @Override
        public void init(EditablePanelEditScreen screen, EditablePanelEntity entity) {
            System.out.println("车厢类型");
        }

        @Override
        public void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {

        }
    };

 */

    private static final Map<ResourceLocation, PanelColorType> signColorMap = new HashMap<>();
    private static final Map<ResourceLocation, SignType> signTypeMap = new HashMap<>();

    public static Map<ResourceLocation, PanelColorType> getSignColorMap() {
        return signColorMap;
    }

    public static Map<ResourceLocation, SignType> getSignTypeMap() {
        return signTypeMap;
    }

    public static int getColorByTag(BlockState state) {
        for (PanelColorType colorType : EditableTypeConstants.getSignColorMap().values()) {
            if (state.is(Objects.requireNonNull(colorType.blockTag.tag())))
                return colorType.signColor;
        }
        return YELLOW;
    }

    // 通过tag获取PanelColorType对象
    public static @Nullable PanelColorType getColorTypeByTag(BlockState state) {
        // 遍历所有PanelColorType类对象，每个对象中均包含车厢板类型（blockTag）- 标识颜色（signColor）的映射关系。
        for (PanelColorType colorType : EditableTypeConstants.getSignColorMap().values()) {
            // 当传入的blockstate中包含当前循环的PanelColorType类对象中的blockTag时，直接返回包含对应标识颜色的PanelColorType类对象。
            if (state.is(Objects.requireNonNull(colorType.blockTag.tag())))
                return colorType;
        }
        return null;
    }

    public static PanelColorType signColorRegister(String locationKey, BlockTagReg blockTag, Integer signColor) {

        PanelColorType panelColorType = new PanelColorType(new ResourceLocation(locationKey), blockTag, signColor);

        EditableTypeConstants.getSignColorMap()
                .put(new ResourceLocation("color_map_" + locationKey), panelColorType);

        return panelColorType;
    }

    public static SignType signLambdaRegister(String locationKey,
                                              TrainPanelProperties.EditType editType,
                                              Supplier<Supplier<SignRenderLambda>> supplier,
                                              Supplier<DefaultTextsLambda> defaultTextSupplier,
                                              Supplier<SignType.CustomScreenSupplier<EditablePanelEditMenu, CustomScreen<EditablePanelEditMenu, EditablePanelEntity>>> screenMethodsSupplier,
                                              SignNbtValidator nbtValidator) {

        SignType signType = new SignType(locationKey, editType, supplier, defaultTextSupplier,
                Envs.isClient() ? screenMethodsSupplier.get() : null, nbtValidator);

        EditableTypeConstants.getSignTypeMap().put(new ResourceLocation(locationKey), signType);

        return signType;
    }

    public static SignType getSignTypeByKey(ResourceLocation location) {
        return EditableTypeConstants.getSignTypeMap().get(location);
    }

    // 根据自定义类型返回对应包含自定义类型（EditType）- 默认字段lambda映射关系的SignType对象
    public static SignType getSignTypeByEditType(TrainPanelProperties.EditType type) {
        // 遍历所有SignType类对象，当传入的EditType等于当前循环中SignType类对象的EditType时，返回该SignType对象。
        for (SignType signType : EditableTypeConstants.getSignTypeMap().values()) {
            if (signType.shouldRender(type))
                return signType;
        }
        return null;
    }

    // 根据手持的物品与车厢板类型返回自定义类型
    public static TrainPanelProperties.EditType getPanelEditType(BlockState state, Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).is(EditablePanelItem.COLORED_BRUSH.getItem())) {
            if (state.is(Objects.requireNonNull(AllTags.BOTTOM_PANEL.tag())))
                return TrainPanelProperties.EditType.TYPE;
            if (state.is(Objects.requireNonNull(AllTags.FLOOR.tag())))
                return TrainPanelProperties.EditType.SPEED;
        }
        if (player.getItemInHand(hand).is(EditablePanelItem.LAQUERED_BOARD.getItem()) && state.is(Objects.requireNonNull(AllTags.BOTTOM_PANEL.tag()))) {
            return TrainPanelProperties.EditType.LAQUERED;
        }
        if (player.getItemInHand(hand).is(EditablePanelItem.STICKER.getItem()) && state.is(Objects.requireNonNull(AllTags.UPPER_PANEL.tag()))) {
            return TrainPanelProperties.EditType.NUM;
        }
        return TrainPanelProperties.EditType.NONE;
    }
}
