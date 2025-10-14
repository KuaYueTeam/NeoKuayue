package willow.train.kuayue.systems.editable_panel.screens;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import kasuga.lib.core.client.render.SimpleColor;
import kasuga.lib.core.client.render.texture.ImageMask;
import kasuga.lib.core.network.C2SPacket;
import kasuga.lib.core.util.LazyRecomputable;
import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.ModList;
import org.joml.Vector3f;
import willow.train.kuayue.block.panels.block_entity.EditablePanelEntity;
import willow.train.kuayue.initial.AllPackets;
import willow.train.kuayue.initial.ClientInit;
import willow.train.kuayue.network.c2s.DiscardChangeC2SPacket;
import willow.train.kuayue.network.c2s.NbtC2SPacket;
import willow.train.kuayue.systems.editable_panel.EditablePanelEditMenu;
import willow.train.kuayue.systems.editable_panel.widget.*;

/**
 * 水牌的GUI呈现
 * GUI开发的第一步就是要有一个UI。MC的GUI分为Screen，即窗口；是GUI的总呈现器。一个这些Widget与玩家的交互
 *
 * @author 童话的爱
 * @since 2025-03-29
 */
// 这里AbstractContainerScreen后面的泛型要填你的menu类。
public class LaqueredScreen
        extends CustomScreen<EditablePanelEditMenu, EditablePanelEntity> {

    private int color;
    public boolean revert;
    public Label titleLabel;
    public ImageButton mirrorBtn, cancelBtn, confirmBtn;
    public EditBar editBar;
    public ColorScreenBundles colorEditor;
    private OffsetEditor offsetEditor;
    private float bgX = 0, bgY = 0, scale = 1.0f;
    private boolean showSub, hasJei, showBg;

    private final LazyRecomputable<ImageMask> cancelBtnImage =
            new LazyRecomputable<>(() -> GetShareTemplateScreen.cancelImage.get().copyWithOp(p -> p));

    private final LazyRecomputable<ImageMask> acceptBtnImage =
            new LazyRecomputable<>(() -> GetShareTemplateScreen.acceptImage.get().copyWithOp(p -> p));

    private final LazyRecomputable<ImageMask> mirrorBtnImage =
            new LazyRecomputable<>(() -> ColorTemplateScreen.buttons.get()
                    .copyWithOp(p -> p.rectangleUV(.125f, .375f, .25f, .5f)));

    public static final LazyRecomputable<ImageMask> laqueredBoardLogo = new LazyRecomputable<>(
            () -> ClientInit.laqueredBoardLogo.getImageSafe().get().getMask()
                    .rectangleUV(0, 0, 1, 1)
    );

    public static final LazyRecomputable<ImageMask> laqueredBoardWhiteBg = new LazyRecomputable<>(
            () -> ClientInit.laqueredBoardWhiteBg.getImageSafe().get().getMask()
                    .rectangleUV(48f / 128f, 64f / 128f, 64f / 128f, 80f / 128f)
    );

    public static final LazyRecomputable<ImageMask> leftLaqueredColorBoard = new LazyRecomputable<>(
            () -> ClientInit.laqueredBoardWhiteBg.getImageSafe().get().getMask()
                    .rectangleUV(0, 0, 1, 1)
    );
    public static final LazyRecomputable<ImageMask> rightLaqueredColorBoard = new LazyRecomputable<>(
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

    private static SimpleColor colorBarColor = SimpleColor.BLACK;


    public LaqueredScreen(AbstractContainerScreen<EditablePanelEditMenu> editablePanelEditMenuAbstractContainerScreen, CompoundTag compoundTag) {
        super(editablePanelEditMenuAbstractContainerScreen, compoundTag);

        setBlockEntity(editablePanelEditMenuAbstractContainerScreen.getMenu().getEditablePanelEntity());
        editBar = new EditBar(0, 0, Component.empty(), "");
        // 检测是否加了jei mod，以防止打开screen时被右侧栏占用
        this.hasJei = ModList.get().isLoaded("jei");
    }


    /**
     * 初始化screen 时添加一个label 组件
     * 该init方法会在GUI最终开始显示之前调用。
     * 有一些参数(或方法)在Screen构造的时候是不存在(或不可用)的，那么若要调用这些参数，就都要写在init()方法里
     */
    @Override
    public void init() {
        showBg = true;
        Minecraft instance = Minecraft.getInstance();
        if (instance.screen == null) return;
        int sW = instance.screen.width;
        int sH = instance.screen.height;
// 获取窗口的 GUI 缩放尺寸（更准确，考虑 UI 缩放）
        Window window = instance.getWindow();
        int guiScaledWidth = window.getGuiScaledWidth();
        int guiScaledHeight = window.getGuiScaledHeight();

        // 获取 ImageMask 背景
        ImageMask imageMask = laqueredBoardWhiteBg.get();
        ImageMask logoIm = laqueredBoardLogo.get();
        ImageMask leftColorBoard = leftLaqueredColorBoard.get();
        ImageMask rightColorBoard = rightLaqueredColorBoard.get();


        // 初始化颜色编辑器组件
        colorEditorInit();
        // 初始化按钮组件
        buttonsInit();

        Font font = instance.font;
        CompoundTag nbt = getNbt();
        // 获取可编辑面板实体对象
        EditablePanelEntity entity = getScreen().getMenu().getEditablePanelEntity();
        // 获取实体的颜色并赋值给类成员变量 color
        color = entity.getColor();
        // 存储从 NBT 数据中读取的文本值
        String[] values = new String[7];
        values[0] = nbt.getString("left_top");
        values[1] = nbt.getString("left_bottom");
        values[2] = nbt.getString("right_top");
        values[3] = nbt.getString("right_bottom");
        values[4] = nbt.getString("train_number");
        values[5] = nbt.getString("left_train_level");
        values[6] = nbt.getString("right_train_level");

        innerInit(values, color, font, revert);


        cancelBtn.setOnClick((w, x, y) -> {
            AllPackets.CHANNEL.sendToServer(new DiscardChangeC2SPacket(entity.getBlockPos()));
            this.close();
        });

        confirmBtn.setOnClick((w, x, y) -> {
            BlockPos pos = entity.getBlockPos();
            nbt.putInt("color", this.color);
            Pair<Float, Float> offset = offsetEditor.getCursorPosition();
            nbt.putFloat("offset_x", offset.getFirst());
            nbt.putFloat("offset_y", offset.getSecond());
            TransparentEditBox[] boxes = new TransparentEditBox[7];
            int counter = 0;
            for (Renderable widget : getCustomWidgets()) {
                if (widget instanceof TransparentEditBox box) {
                    boxes[counter] = box;
                    counter++;
                }
            }

            nbt.putString("left_top", boxes[0].getValue());
            nbt.putString("left_bottom", boxes[1].getValue());
            nbt.putString("right_top", boxes[2].getValue());
            nbt.putString("right_bottom", boxes[3].getValue());
            nbt.putString("train_number", boxes[4].getValue());
            nbt.putString("left_train_level", boxes[5].getValue());
            nbt.putString("right_train_level", boxes[6].getValue());

            CompoundTag tag = new CompoundTag();
            tag.put("data", nbt);
            entity.load(tag);
            entity.markUpdated();
            AllPackets.CHANNEL.sendToServer(new NbtC2SPacket(pos, tag));
            this.close();
        });
    }

    private void innerInit(String[] values, int color, Font font, boolean revert) {
        // 设置文本缩放因子
        float textScaleFactor = 11f;
        float textScaleFactorForEnglish = 16f;

// 获取窗口和字体信息
        Minecraft minecraft = Minecraft.getInstance();
        Window window = minecraft.getWindow();
// 动态获取当前GUI缩放后的尺寸
        double guiScale = window.getGuiScale();
        int guiScaledWidth = window.getGuiScaledWidth();
        int guiScaledHeight = window.getGuiScaledHeight();
        float heightMarginPixel = guiScaledHeight * 0.015f;

// 计算文本显示尺寸（考虑缩放因子）
        int width0 = font.width(values[0]);
        float size0 = width0 * textScaleFactor;
        double v0TextCount = (width0 / guiScale) / 9;
        int width1 = font.width(values[1]);
        float size1 = width1 * textScaleFactorForEnglish;
        double v1TextCount = (width1 / guiScale) / 6;
        int width2 = font.width(values[2]);
        float size2 = width2 * textScaleFactor;
        double v2TextCount = (width2 / guiScale) / 9;
        int width3 = font.width(values[3]);
        float size3 = width3 * textScaleFactorForEnglish;
        double v3TextCount = (width3 / guiScale) / 6;

        int width4 = font.width(values[4]);
        float size4 = width4 * textScaleFactor;
        double v4TextCount = (width4 / guiScale) / 9;

        int width5 = font.width(values[5]);
        float size5 = width5 * textScaleFactor;
        double v5TextCount = (width5 / guiScale) / 9;

        int width6 = font.width(values[6]);
        float size6 = width6 * textScaleFactor;
        double v6TextCount = (width6 / guiScale) / 9;


        float bgImageXStarter = (float) (guiScaledWidth * 0.15); // 偏移至从左到右第25%的位置
        float bgImageYStarter = (float) (guiScaledHeight * 0.3); // 偏移至从下到上第35%的位置
        float imageBgWidth = (float) (guiScaledWidth * 0.70); // 宽度为窗口宽度的70%，
        float imageBgHeight = (float) (guiScaledHeight * 0.3); // 高度为窗口高度的40%
//        float colorBarHeightStart = (bgImageYStarter + imageBgHeight * 0.70f); // 高度为   // todo 可能会到上面去
        float colorBarHeightStart = (bgImageYStarter + imageBgHeight * 0.70f); // 高度为   // todo 可能会到上面去
        float colorBarHeightEnd = (bgImageYStarter + imageBgHeight) * 1f; // 高度为
        float leftColorBarXStarter = (float) (guiScaledWidth * 0.15); // 偏移至从左到右第25%的位置
        float colorBarWidth = (float) (imageBgWidth * 0.4); // 彩条宽度为水牌宽度的20%
        float colorBarHeight = (float) (imageBgHeight * 0.25); // 彩条高度为水牌高的23%
        float leftColorBarXEnd = (float) ((guiScaledWidth * 0.15) + (imageBgWidth * 0.4)); // 0.4+0.4 -> 0.8
        float rightColorBarXEnd = (float) ((guiScaledWidth * 0.15) + imageBgWidth); // 偏移至从左到右第25%的位置
        // 使logo的大小是水牌背景高度的20%
        double logoWidthHalf = imageBgHeight * 0.6;
        float rightColorBarXStarter = (float) ((imageBgWidth * 0.6) + (imageBgWidth * 0.2133));
        // 偏移至从左到右第25%的位置
        float logoXStarter = (float) ((guiScaledWidth / 2) - (logoWidthHalf / 2)); //
        float logoYStarter = (float) ((bgImageYStarter * 0.925) + ((imageBgHeight / 2) - (logoWidthHalf / 2))); //

        // 中间基线
        float middleLineY = (float) (bgImageYStarter + (imageBgHeight / 2));
        float middleLineX = (float) (guiScaledWidth / 2);

        // 计算得出文字应在当前屏幕x y 时的文字比例（修改文字缩放参数）
        double nonEngScaleNumber = (bgImageXStarter / 100) * 0.5;
        float resizedTextScaleFactor = (float) ((textScaleFactor * nonEngScaleNumber));
        double engLineScaleNumber = (bgImageXStarter / 100) * 0.15;
        float resizedTextScaleFactorForEnglish = (float) (textScaleFactorForEnglish * engLineScaleNumber);

        // 为了解决文字变长时锚点不在固定的中间位置问题，需先将锚点x定位到水牌纸面中央，然后再根据文字长度 / 2 反向（给反方向的x值，负值）偏移锚点
        float leftMoJiMargin = 0f;
        if (v0TextCount <= 2) { // 0.5 一个字？  font.width(values[0]) 4中文 36float 一个字9f
            // 左边距中文计算公式
//            leftMoJiMargin = (float) Math.max (允许的最小值, ((最大字符数 - 当前字符数 ) / 除以2即留白的一半) * (画面缩放宽度 * 的百分之25));
            leftMoJiMargin = (float) Math.max(0, ((2 - v0TextCount) / 2) * (guiScaledWidth * 0.15));
        }

        float leftEngMoJiMargin = 0f;
        if (v1TextCount <= 13) {
            // todo 2025-05-20 英文文字的边距计算 目前左边距计算扔有偏差（在多字和少字均仍有瑕疵），超出13字后不可自动缩放
            leftEngMoJiMargin = (float) Math.max(0, ((13 - v1TextCount * 2) / 2) * (guiScaledWidth * 0.02));
        }
        float rightMoJiMargin = 0f;
        if (v2TextCount <= 2) {
            rightMoJiMargin = (float) Math.max(0, ((2 - v2TextCount) / 2) * (guiScaledWidth * 0.15));
        }
        float rightEngMoJiMargin = 0f;
        if (v3TextCount <= 13) {
            rightEngMoJiMargin = (float) Math.max(0, ((13 - v3TextCount * 2) / 2) * (guiScaledWidth * 0.02));
        }

        int lineFontMaxWidth = (int) (guiScaledWidth * 0.28);
        addCustomWidget(new TransparentEditBox(font,
                // 使其根据文本内容大小偏移
                (int) (leftMoJiMargin + bgImageXStarter),
                (int) (bgImageYStarter + heightMarginPixel),
                (int) width0,
                font.lineHeight,
                v0TextCount < 2.5 ? resizedTextScaleFactor : (float) lineFontMaxWidth / width0,
                resizedTextScaleFactor,
                Component.empty(),
                values[0],
                BLACK)
        );
        addCustomWidget(new TransparentEditBox(font,
                (int) (leftEngMoJiMargin + bgImageXStarter),
                (int) (colorBarHeightStart + heightMarginPixel),
                width1,
                font.lineHeight,
                v1TextCount <= 6 ? resizedTextScaleFactorForEnglish : (float) lineFontMaxWidth / width1,
                resizedTextScaleFactorForEnglish,
                Component.empty(),
                values[1],
                WHITE));
        // 目的地布局（values[2]和values[3]）
        addCustomWidget(new TransparentEditBox(font,
                // imageBgWidth * 0.6 用于将文字偏移到logo右边
                (int) (rightMoJiMargin + (bgImageXStarter + (imageBgWidth * 0.6))),
                (int) (bgImageYStarter + heightMarginPixel),
                width2,
                font.lineHeight,
                v2TextCount < 2.5 ? resizedTextScaleFactor : (float) lineFontMaxWidth / width2,
                resizedTextScaleFactor,
                Component.empty(),
                values[2],
                BLACK));

        addCustomWidget(new TransparentEditBox(font,
                (int) (rightEngMoJiMargin + (bgImageXStarter + (imageBgWidth * 0.6))),
                (int) (colorBarHeightStart + heightMarginPixel),
                width3,
                font.lineHeight,
                v3TextCount <= 6 ? resizedTextScaleFactorForEnglish : (float) lineFontMaxWidth / width3,
                resizedTextScaleFactorForEnglish,
                Component.empty(),
                values[3],
                WHITE));

        int trainNumberMaxLength = (int) (guiScaledWidth * 0.085);
        addCustomWidget(new TransparentEditBox(font,
                (int) (((guiScaledWidth * 0.95) / 2) - width4 / 2),
                (int) (colorBarHeightStart + heightMarginPixel),
                width4,
                font.lineHeight,
                v4TextCount <= 1.5 ? resizedTextScaleFactorForEnglish : (float) trainNumberMaxLength / width4,
                resizedTextScaleFactorForEnglish,
                Component.empty(),
                values[4],
                BLACK));

// 左侧logo文字
        addCustomWidget(new TransparentEditBox(font,
                (int) logoXStarter - 25,
                (int) (logoYStarter - 15),
                width5,
                font.lineHeight,
                v5TextCount <= 2.3 ? resizedTextScaleFactorForEnglish : (float) lineFontMaxWidth / width5,
                resizedTextScaleFactorForEnglish,
                Component.empty(),
                values[5],
                BLACK));

        // 右侧logo文字
        addCustomWidget(new TransparentEditBox(font,
                (int) ((int) logoXStarter + (logoWidthHalf) - 5.5),
                (int) logoYStarter - 15,
                width6,
                font.lineHeight,
                v6TextCount <= 2.3 ? resizedTextScaleFactorForEnglish : (float) lineFontMaxWidth / width6,
                resizedTextScaleFactorForEnglish,
                Component.empty(),
                values[6],
                BLACK));
    }

    /**
     * 根据文字长度计算动态偏移量
     *
     * @param text 待测量的文字
     * @param font
     * @return 水平偏移量（像素值）
     */
    @Deprecated
    public static float calculateDynamicMargin(String text, Font font, float bgWidth) {
        // 基础参数（可配置化） 当中文那行是 北京 时，则边距应为80f 以使其在中间
        final float BASE_MARGIN = 80f;    // 基础偏移量
        final float LENGTH_THRESHOLD = 4f; // 长度临界点
        final float PIXEL_PER_CHAR = 1f;  // 每字符补偿系数

        // 获取有效长度（考虑Unicode组合字符）
        int codePointCount = font.width(text) / 9;
        float lengthFactor = codePointCount / LENGTH_THRESHOLD;

        // 动态计算规则 当codePointCount / LENGTH_THRESHOLD 大于1则代表超出 LENGTH_THRESHOLD 中规定的文字长度限制，否则未超出限制
        if (lengthFactor > 1.0f) {
            // 超长文本负偏移公式：-baseMargin - (extraLength * compensation)
            return -BASE_MARGIN - ((codePointCount - LENGTH_THRESHOLD));
        } else {
            // 短文本正偏移公式：baseMargin + (remainingSpace * compensation)
            return BASE_MARGIN + ((LENGTH_THRESHOLD - codePointCount) * 0.6f);
        }
    }

    public void buttonsInit() {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        Window window = minecraft.getWindow();
// 动态获取当前GUI缩放后的尺寸
        int guiScaledWidth = window.getGuiScaledWidth();
        int guiScaledHeight = window.getGuiScaledHeight();

        float bgImageXStarter = (float) (guiScaledWidth * 0.15); // 偏移至从左到右第25%的位置
        float bgImageYStarter = (float) (guiScaledHeight * 0.3); // 偏移至从下到上第35%的位置
        float imageBgWidth = (float) (guiScaledWidth * 0.70); // 宽度为窗口宽度的70%，
        float imageBgHeight = (float) (guiScaledHeight * 0.3); // 高度为窗口高度的40%

        int offsetButtonX = (int) (bgImageXStarter);
        int offsetButtonY = (int) (bgImageYStarter + imageBgHeight + 16);
        titleLabel = new Label(Component.translatable("clip_board.laquered"));
        int width = font.width(titleLabel.getPlainText());
        titleLabel.setPosition((guiScaledWidth / 2) - (width / 2), bgImageYStarter - 20);
        addCustomWidget(titleLabel);

        mirrorBtn = new ImageButton(mirrorBtnImage, (16), offsetButtonY, 16, 16, Component.empty(), b -> {
            revert = !revert;
            refresh();
        });

        offsetEditor = new OffsetEditor((int) (bgImageXStarter + (16 * 2)), offsetButtonY, Component.literal("offset"),
                -.5f, .5f, -.5f, .5f, 0f, 0f);
        offsetEditor.setPosition((Minecraft.getInstance().screen.width - offsetEditor.getWidth()) / 2,
                (Minecraft.getInstance().screen.height - offsetEditor.getHeight()) / 2);
        offsetEditor.visible = false;

        cancelBtn = new ImageButton(cancelBtnImage, (int) (bgImageXStarter + imageBgWidth - (16)), offsetButtonY, 16, 16, Component.empty(), b -> {
        });

        confirmBtn = new ImageButton(acceptBtnImage, (int) (bgImageXStarter + imageBgWidth - (16 * 2)), offsetButtonY, 16, 16, Component.empty(), b -> {
        });

        editBar.onCancelClick((w, x, y) -> editBar.visible = false);
        editBar.visible = false;
        offsetEditor.onCancelBtnClick(((widget, mouseX, mouseY) -> {
            setBoardWidgetVisible(true);
            offsetEditor.visible = false;
            showBg = true;
        }));
        offsetEditor.onEditorBtnClick((widget, mouseX, mouseY) -> {
            setBoardWidgetVisible(false);
            offsetEditor.visible = true;
            showBg = false;
            offsetEditor.setCursorPosition(getNbt().getFloat("offset_x"), getNbt().getFloat("offset_y"));
        });
        offsetEditor.onAcceptBtnClick((widget, mouseX, mouseY) -> {
            setBoardWidgetVisible(true);
            Pair<Float, Float> offset = offsetEditor.getCursorPosition();
            getNbt().putFloat("offset_x", offset.getFirst());
            getNbt().putFloat("offset_y", offset.getSecond());
            getBlockEntity().saveNbt(this.getNbt());
            offsetEditor.visible = false;
            showBg = true;
        });

        addCustomWidget(cancelBtn);
        addCustomWidget(confirmBtn);
        addCustomWidget(editBar);
        ImageButton editorBtn = offsetEditor.getEditorBtn();
        editorBtn.setPos((int) (bgImageXStarter + imageBgWidth - (17 * 3)), offsetButtonY);
        addCustomWidget(editorBtn);
        ImageButton colorBtn = colorEditor.getColorBtn();
        colorBtn.setPos((int) (bgImageXStarter + (10)), offsetButtonY);
        addCustomWidget(colorBtn);
        ImageButton templateBtn = colorEditor.getTemplateBtn();
        templateBtn.setPos((int) (bgImageXStarter + (30)), offsetButtonY);
        addCustomWidget(templateBtn);
        addCustomWidget(offsetEditor);
    }

    private void refresh() {
        if (editBar.visible) return;
        String[] values = new String[7];
        int counter = 0;
        int focus = -1;
        int focusIndex = -1;
        for (Renderable w : getCustomWidgets()) {
            if (!(w instanceof TransparentEditBox box)) continue;
            values[counter] = box.getValue();
            if (box.isFocused()) {
                focus = counter;
                focusIndex = box.getCursorPosition();
            }
            counter++;
        }
        clearWidgets();

        addCustomWidget(cancelBtn);
        addCustomWidget(confirmBtn);
        addCustomWidget(titleLabel);
        addCustomWidget(editBar);
        addCustomWidget(mirrorBtn);
        addCustomWidget(colorEditor);
        addCustomWidget(colorEditor.getColorBtn());
        addCustomWidget(colorEditor.getTemplateBtn());
        addCustomWidget(offsetEditor.getEditorBtn());
        addCustomWidget(offsetEditor);

        clearLabels();
        Font font = Minecraft.getInstance().font;
        CompoundTag nbt = getNbt();
        int color = getScreen().getMenu().getEditablePanelEntity().getColor();
        innerInit(values, color, font, revert);
        if (focus > -1 && focusIndex > -1) {
            Renderable w = getCustomWidgets().get(focus);
            if (!(w instanceof TransparentEditBox box)) return;
            box.setFocused(true);
            box.setCursorPosition(focusIndex);
        }
    }

    public void colorEditorInit() {
        colorEditor = new ColorScreenBundles();
        colorEditor.init();
        colorEditor.setOpen((selector, template, now) -> {
            selector.setRgb(this.color);
            setBoardWidgetVisible(false);
            showBg = false;
        });
        colorEditor.setCancel((selector, template, now) -> {
            setBoardWidgetVisible(true);
            showBg = true;
        });
        colorEditor.setSuccess((selector, template, now) -> {
            if (now == template) {
                this.color = template.getChosenBox().getTemplate().getColor();
//                this.setTextColor(color);
                colorBarColor = SimpleColor.fromRGBAInt(template.getChosenBox().getTemplate().getColor());
                setBoardWidgetVisible(true);
            } else {
                this.color = selector.getColor().getRGB();
//                setTextColor(color);
                colorBarColor = SimpleColor.fromRGBAInt(template.getChosenBox().getTemplate().getColor());
                setBoardWidgetVisible(true);
            }
            showBg = true;
        });
        colorEditor.visible = false;
        addCustomWidget(colorEditor);
    }

    /**
     * 渲染背景
     * (一般来说，是GUI从开始显示到最终关闭都会存在的静态渲染对象)
     *
     * @param guiGraphics
     * @param mouseX
     * @param mouseY
     * @param partialTick
     **/
    @Override
    public void renderBackGround(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft instance = Minecraft.getInstance();
        if (instance.screen == null) return;
        int sW = instance.screen.width;
        int sH = instance.screen.height;
        // 获取窗口的 GUI 缩放尺寸（更准确，考虑 UI 缩放）
        Window window = instance.getWindow();
        int guiScaledWidth = window.getGuiScaledWidth();
        int guiScaledHeight = window.getGuiScaledHeight();

        // 获取 ImageMask 背景
        ImageMask imageMask = laqueredBoardWhiteBg.get();
        ImageMask logoIm = laqueredBoardLogo.get();
        ImageMask leftColorBoard = leftLaqueredColorBoard.get();

        // starter ender 都是锚点，width是宽度，height是高度
        // 确定水牌图片背景的左上角x百分比  0.25-> 0.15   0.35 -> 0.3
        float bgImageXStarter = (float) (guiScaledWidth * 0.15); // 偏移至从左到右第25%的位置
        float bgImageYStarter = (float) (guiScaledHeight * 0.3); // 偏移至从下到上第35%的位置
        float imageBgWidth = (float) (guiScaledWidth * 0.70); // 宽度为窗口宽度的70%，
        float imageBgHeight = (float) (guiScaledHeight * 0.3); // 高度为窗口高度的40%
        float colorBarHeightStart = (bgImageYStarter + imageBgHeight * 0.70f); // 高度为   // todo 可能会到上面去
        float colorBarHeightEnd = (bgImageYStarter + imageBgHeight) * 1f; // 高度为
        float leftColorBarXStarter = (float) (guiScaledWidth * 0.15); // 偏移至从左到右第25%的位置
        float colorBarWidth = (float) (imageBgWidth * 0.4); // 彩条宽度为水牌宽度的20%
        float colorBarHeight = (float) (imageBgHeight * 0.25); // 彩条高度为水牌高的23%
        float leftColorBarXEnd = (float) ((guiScaledWidth * 0.15) + (imageBgWidth * 0.4)); // 0.4+0.4 -> 0.8
        float rightColorBarXEnd = (float) ((guiScaledWidth * 0.15) + imageBgWidth); // 偏移至从左到右第25%的位置
        // 使logo的大小是水牌背景高度的20%
        double logoWidthHalf = imageBgHeight * 0.6;
        float rightColorBarXStarter = (float) ((imageBgWidth * 0.6) + (imageBgWidth * 0.2133));

        // 偏移至从左到右第25%的位置
        float logoXStarter = (float) ((guiScaledWidth / 2) - (logoWidthHalf / 2)); //
        float logoYStarter = (float) (bgImageYStarter * 0.90 + ((imageBgHeight / 2) - (logoWidthHalf / 2))); //

        // 确保背景的 x 和 y 原点与 values[0] 对齐
        imageMask.rectangle(
                new Vector3f(bgImageXStarter, bgImageYStarter, 0),  // 使用与 values[0] 相同的原点
                ImageMask.Axis.X,
                ImageMask.Axis.Y,
                true,
                true,
                imageBgWidth,
                imageBgHeight
        );
        logoIm.rectangle(
                new Vector3f(logoXStarter,
                        logoYStarter,
                        1),
                ImageMask.Axis.X,
                ImageMask.Axis.Y,
                true,
                true,
                (float) logoWidthHalf,
                (float) logoWidthHalf
        );

        leftColorBoard.rectangle(

                new Vector3f(leftColorBarXStarter,

                        colorBarHeightStart,
                        1),  // 使用与 values[0] 相同的原点
                ImageMask.Axis.X,
                ImageMask.Axis.Y,
                true,
                true,
                colorBarWidth,
                colorBarHeight
        );
        leftColorBoard.setColor(
                SimpleColor.fromRGBAInt(color)
        );
        ImageMask rightColorBoard = rightLaqueredColorBoard.get();
        rightColorBoard.rectangle(
                new Vector3f(rightColorBarXStarter, colorBarHeightStart, 1),  // 使用与 values[0] 相同的原点
                ImageMask.Axis.X,
                ImageMask.Axis.Y,
                true,
                true,
                colorBarWidth,
                colorBarHeight
        );
        rightColorBoard.setColor(SimpleColor.fromRGBAInt(color));
        // 渲染背景
        if (showBg) {
            imageMask.renderToGui();
            rightColorBoard.renderToGui();
            leftColorBoard.renderToGui();
            logoIm.renderToGui();
        }
        // 添加半透明黑色背景覆盖整个屏幕
        guiGraphics.fill(0, 0, sW, sH, 0x80000000);
    }

    @Override
    public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }

    /**
     * Screen的核心渲染方法，用于控制其他所有渲染方法的工作
     *
     * @param guiGraphics
     * @param mouseX
     * @param mouseY
     * @param partial
     */
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        super.render(guiGraphics, mouseX, mouseY, partial);

        if (offsetEditor == null) return;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int btn) {
        for (Renderable widget : getCustomWidgets()) {
            if (!(widget instanceof GuiEventListener listener)) continue;
            if (!listener.isMouseOver(mouseX, mouseY)) continue;
            if (listener instanceof AbstractWidget widget1 && !widget1.visible) continue;
            if (listener instanceof ColorScreen cs && !cs.getVisible()) continue;
            if (listener instanceof GetShareTemplateScreen screen && !screen.isVisible()) continue;
            if (widget instanceof TransparentEditBox box) {
                editBar.setPosition(box.getX() + ((int) ((float) box.getWidth() * box.getScaleX()) - 200) / 2,
                        box.getY() + (int) ((float) box.getHeight() * box.getScaleY()) + 2);
                editBar.setText(box.getValue());
                editBar.onAcceptClick(
                        (w, x, y) -> {
                            box.setValue(editBar.getText());
                            editBar.visible = false;
                            refresh();
                            getBlockEntity().saveNbt(this.getNbt());
                        }
                );
                editBar.visible = true;
                editBar.setFocused(true);
                return true;
            }
            listener.mouseClicked(mouseX, mouseY, btn);
            return true;
        }
        return false;
    }


    /**
     * 该screen 关闭时调用
     */
//    @Override
//    public void onClose() {
//        super.onClose();
//    }
    @Override
    public boolean charTyped(char code, int modifier) {
        return super.charTyped(code, modifier);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void setButtonsVisible(boolean visible) {
        colorEditor.getTemplateBtn().visible = visible;
        colorEditor.getColorBtn().visible = visible;
        this.cancelBtn.visible = visible;
        this.confirmBtn.visible = visible;
        this.titleLabel.visible = visible;
        this.mirrorBtn.visible = visible;
        editBar.visible = false;
        this.offsetEditor.getEditorBtn().visible = visible;
        editBar.setFocused(false);
    }

    public void setBoardWidgetVisible(boolean visible) {
        getCustomWidgets().forEach(w -> {
            if (w instanceof TransparentEditBox box) box.visible = visible;
        });
        setButtonsVisible(visible);
    }

    public void setTextColor(int color) {
        getCustomWidgets().forEach(w -> {
            if (w instanceof TransparentEditBox box) box.setTextColor(color);
        });
    }
}
