package willow.train.kuayue.systems.editable_panel.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import kasuga.lib.core.client.render.texture.ImageMask;
import kasuga.lib.core.util.LazyRecomputable;
import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.royawesome.jlibnoise.module.combiner.Min;
import org.checkerframework.checker.units.qual.C;
import willow.train.kuayue.initial.ClientInit;
import willow.train.kuayue.systems.editable_panel.screens.ColorScreen;

import java.io.IOException;
import java.util.Map;

public class OffsetEditor extends AbstractWidget {
    private float minX, maxX, minY, maxY, cursorX, cursorY, defaultX, defaultY;

    private static final int touchPanelX = 5, touchPanelY = 5, touchPanelA = 86;
    public static final LazyRecomputable<ImageMask> editor =
            new LazyRecomputable<>(() -> {
               try {
                   return ClientInit.offsetEditor.getImage().get().getMask();
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
            });

    double cacheX = -1, cacheY = -1;

    public static final LazyRecomputable<ImageMask> largeBg =
            new LazyRecomputable<>(() -> editor.get().copyWithOp(imageMask -> imageMask));

    private final LazyRecomputable<ImageMask> resetBtnBg =
            new LazyRecomputable<>(() -> editor.get().copyWithOp(m ->
                    m.rectangleUV(48f/192f, 112f/128f, 64f/192f, 1f))
            );

    private final LazyRecomputable<ImageMask> cancelBtnBg =
            new LazyRecomputable<>(() -> editor.get().copyWithOp(m ->
                    m.rectangleUV(64f/192f, 112f/128f, 80f/192f, 1f))
            );

    private final LazyRecomputable<ImageMask> acceptBtnBg =
            new LazyRecomputable<>(() -> editor.get().copyWithOp(m ->
                    m.rectangleUV(80f/192f, 112f/128f, 96f/192f, 1f))
            );

    private final LazyRecomputable<ImageMask> mainBg =
            new LazyRecomputable<>(() -> editor.get().copyWithOp(m ->
                    m.rectangleUV(0, 0, 161f/192f, 96f/128f)
            ));

    private final LazyRecomputable<ImageMask> cursor =
            new LazyRecomputable<>(() -> editor.get().copyWithOp(m ->
                    m.rectangleUV(32f/192f, 112f/128f, 39f/192f, 120f/128f)
            ));

    private final LazyRecomputable<ImageMask> logo =
            new LazyRecomputable<>(() -> editor.get().copyWithOp(m ->
                    m.rectangleUV(32f/192f, 96f/128f, 48f/192f, 112f/128f)
            ));

    private final ImageButton resetBtn, cancelBtn, acceptBtn, editorBtn;

    public OffsetEditor(int x, int y, Component title) {
        super(x, y, 162, 97, title);
        resetBtn = new ImageButton(resetBtnBg, x + 100, y + 75, 16, 16, Component.empty(), b -> {
            this.cursorX = this.defaultX;
            this.cursorY = this.defaultY;
        });
        cancelBtn = new ImageButton(cancelBtnBg, x + 120, y + 75, 16, 16, Component.empty(), b -> {});
        acceptBtn = new ImageButton(acceptBtnBg, x + 140, y + 75, 16, 16, Component.empty(), b -> {});
        editorBtn = new ImageButton(logo, 0, 0, 16, 16, Component.empty(), b -> {});
    }

    public OffsetEditor(int x, int y, Component title, float minX, float maxX, float minY, float maxY, float defaultX, float defaultY) {
        this(x, y, title);
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.cursorX = defaultX;
        this.cursorY = defaultY;
        this.defaultX = defaultX;
        this.defaultY = defaultY;
    }

    public void setX(int x) {
        resetBtn.setX(x + 99);
        cancelBtn.setX(x + 119);
        acceptBtn.setX(x + 139);
        this.x = x;
    }

    public void setY(int y) {
        resetBtn.setY(y + 74);
        cancelBtn.setY(y + 74);
        acceptBtn.setY(y + 74);
        this.y = y;
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public void setRanges(float minX, float minY, float maxX, float maxY, float defaultX, float defaultY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.defaultX = defaultX;
        this.defaultY = defaultY;
        cursorX = defaultX;
        cursorY = defaultY;
    }

    public void reset() {
        this.cursorX = defaultX;
        this.cursorY = defaultY;
    }

    public void setCursorPosition(float px, float py) {
        cursorX = Math.max(this.minX, Math.min(this.maxX, px));
        cursorY = Math.max(this.minY, Math.min(this.maxY, py));
    }

    public Pair<Float, Float> getCursorPosition() {
        float x = Math.max(this.minX, Math.min(this.maxX, cursorX));
        float y = Math.max(this.minY, Math.min(this.maxY, cursorY));
        return Pair.of(x, y);
    }

    @Override
    public void renderButton(PoseStack pose, int pMouseX, int pMouseY, float pPartialTick) {
        mainBg.get().rectangle(new Vector3f(this.x, this.y, 0),
                ImageMask.Axis.X, ImageMask.Axis.Y, true, true, this.width, this.height);
        mainBg.get().renderToGui();

        resetBtn.renderOnlyOnClicked(pose, pMouseX, pMouseY, pPartialTick);
        cancelBtn.renderOnlyOnClicked(pose, pMouseX, pMouseY, pPartialTick);
        acceptBtn.renderOnlyOnClicked(pose, pMouseX, pMouseY, pPartialTick);

        Pair<Float, Float> pxpy = cursorToPos();
        cursor.get().rectangle(new Vector3f(pxpy.getFirst() - 3, pxpy.getSecond() - 3, 0),
                ImageMask.Axis.X, ImageMask.Axis.Y, true, true, 7, 7);
        cursor.get().renderToGui();

        Font font = Minecraft.getInstance().font;
        font.draw(pose, this.getMessage(), this.x + 98, this.y + 5, 0xff000000);
        drawAllCenteredString(pose, font, Component.literal("x:"),
                this.x + 103, this.y + 29, 0xff000000);
        drawAllCenteredString(pose, font, Component.literal(String.valueOf(ColorScreen.front3(this.cursorX))),
                this.x + 133, this.y + 29, 0xffffffff);
        drawAllCenteredString(pose, font, Component.literal("y:"),
                this.x + 103, this.y + 53, 0xff000000);
        drawAllCenteredString(pose, font, Component.literal(String.valueOf(ColorScreen.front3(this.cursorY))),
                this.x + 133, this.y + 53, 0xffffffff);
    }

    private Pair<Float, Float> cursorToPos() {
        float leftX = Math.min(this.minX, this.maxX);
        float upperY = Math.max(this.minY, this.maxY);
        float cx = (Math.max(this.minX, Math.min(this.maxX, this.cursorX)) - leftX) / Math.abs(this.maxX - this.minX);
        float cy = (upperY - Math.max(this.minY, Math.min(this.maxY, this.cursorY))) / Math.abs(this.maxY - this.minY);
        float a = touchPanelA - 8;
        float px = Math.round(cx * a) + this.x + touchPanelX + 4,
                py = Math.round(cy * a) + this.y + touchPanelY + 4;
        return Pair.of(px, py);
    }

    private void posToCursor(double mouseX, double mouseY) {
        double px = mouseX, py = mouseY;
        float a = touchPanelA - 8;
        py -= 4 + touchPanelY + this.y;
        px -= 4 + touchPanelX + this.x;
        px /= a;
        py /= a;
        px *= Math.abs(this.maxX - this.minX);
        py *= Math.abs(this.maxY - this.minY);
        this.cursorX = (float) (px + Math.min(this.minX, this.maxX));
        this.cursorY = (float) (Math.max(this.minY, this.maxY) - py);
    }

    public void drawAllCenteredString(PoseStack poseStack, Font font, Component component, int x, int y, int color) {
        FormattedCharSequence formattedcharsequence = component.getVisualOrderText();
        font.draw(poseStack, formattedcharsequence, (float)(x - font.width(formattedcharsequence) / 2), (float)y, color);
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return resetBtn.mouseClicked(pMouseX, pMouseY, pButton) |
                cancelBtn.mouseClicked(pMouseX, pMouseY, pButton) |
                acceptBtn.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        return resetBtn.mouseReleased(pMouseX, pMouseY, pButton) |
                cancelBtn.mouseReleased(pMouseX, pMouseY, pButton) |
                acceptBtn.mouseReleased(pMouseX, pMouseY, pButton);
    }

    public void onEditorBtnClick(OnClick<ImageButton> clicker) {
        this.editorBtn.setOnClick(clicker);
    }

    public void onCancelBtnClick(OnClick<ImageButton> clicker) {
        this.cancelBtn.setOnClick(clicker);
    }

    public void onAcceptBtnClick(OnClick<ImageButton> clicker) {
        this.acceptBtn.setOnClick(clicker);
    }

    public ImageButton getEditorBtn() {
        return editorBtn;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (!this.visible) {
            return false;
        }

        if (Screen.hasShiftDown()) {
            if (cacheX < 0 || cacheY < 0) {
                cacheX = pMouseX;
                cacheY = pMouseY;
            }
            if (Math.abs(pDragX) > Math.abs(pDragY))
                posToCursor(pMouseX + pDragX, cacheY);
            else
                posToCursor(cacheX, pMouseY + pDragY);
            return true;
        } else {
            cacheX = -1;
            cacheY = -1;
        }
        if (inCursorPanelRange(pMouseX, pMouseY)) {
            posToCursor(pMouseX + pDragX, pMouseY + pDragY);
            return true;
        }
        return false;
    }

    public boolean inCursorPanelRange(double mouseX, double mouseY) {
        return mouseX > this.x + touchPanelX && mouseX < this.x + touchPanelX + touchPanelA &&
                mouseY > this.y + touchPanelY && mouseY < this.y + touchPanelY + touchPanelA;
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public void onRelease(double pMouseX, double pMouseY) {
        super.onRelease(pMouseX, pMouseY);
        cacheX = -1;
        cacheY = -1;
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        return super.charTyped(pCodePoint, pModifiers);
    }
}
