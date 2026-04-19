package willow.train.kuayue.systems.editable_panel.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import willow.train.kuayue.mixins.mixin.client.AccessorEditBox;

public class TransparentEditBox extends EditBox {
    private float scaleX = 1.0f, scaleY = 1.0f;
    public TransparentEditBox(Font pFont, int pX, int pY, int pWidth, int pHeight, Component pMessage, String value, int color) {
        super(pFont, pX, pY, pWidth, pHeight, null, pMessage);
        setBordered(false);
        setValue(value);
        setTextColor(color);
    }

    public TransparentEditBox(Font pFont, int pX, int pY, int pWidth, int pHeight, float scaleX,
                              float scaleY, Component pMessage, String value, int color) {
        super(pFont, pX, pY, pWidth, pHeight, null, pMessage);
        setBordered(false);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        setValue(value);
        setTextColor(color);
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scale) {
        this.scaleX = scale;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getScaleY() {
        return scaleY;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (!this.visible) return;
        float x = this.getX() - scaleX * this.getX();
        float y = this.getY() - scaleY * this.getY();
        PoseStack pPoseStack = guiGraphics.pose();
        pPoseStack.translate(x, y, 0);
        pPoseStack.scale(scaleX, scaleY, 1f);
        this.isHovered = pMouseX >= this.getX() && pMouseY >= this.getY() &&
                pMouseX < this.getX() + this.width * scaleX && pMouseY < this.getY() + this.height * scaleY;
        this.renderButton(guiGraphics, pMouseX, pMouseY, pPartialTick);
        pPoseStack.scale(1 / scaleX, 1 / scaleY, 1f);
        pPoseStack.translate(- x, - y, 0);
    }

    public void renderButton(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        AccessorEditBox accessor = (AccessorEditBox) this;
        if (this.isVisible()) {
            if (accessor.getBordered()) {
                int i = this.isFocused() ? -1 : -6250336;
                guiGraphics.fill(this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1, this.getY() + this.height + 1, i);
                guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, -16777216);
            }

            int textColor = accessor.getIsEditable() ? accessor.getTextColor() : accessor.getTextColorUneditable();
            int headToCursor = accessor.getCursorPos() - accessor.getDisplayPos();
            int headToHighlight = accessor.getHighlightPos() - accessor.getDisplayPos();
            String displayedText = accessor.getFont().plainSubstrByWidth(accessor.getValue().substring(accessor.getDisplayPos()), this.getInnerWidth());
            boolean cursorInBox = headToCursor >= 0 && headToCursor <= displayedText.length();  // 游标是否在框内
            boolean shouldRenderCursor = this.isFocused() && accessor.getFrame() / 6 % 2 == 0 && cursorInBox;  // 是否应当渲染游标
            int startX = accessor.getBordered() ? this.getX() + 4 : this.getX();
            int startY = accessor.getBordered() ? this.getY() + (this.height - 8) / 2 : this.getY();
            int lengthBeforeCursor = startX;
            if (headToHighlight > displayedText.length()) {
                headToHighlight = displayedText.length();
            }
            String textBeforeCursor = cursorInBox ? displayedText.substring(0, headToCursor) : displayedText;
            if (!displayedText.isEmpty()) {
                lengthBeforeCursor = guiGraphics.drawString(accessor.getFont(), accessor.getFormatter().apply(textBeforeCursor, accessor.getDisplayPos()), startX, startY, textColor, false);
            }

            boolean cursorBeforeEnd = accessor.getCursorPos() < accessor.getValue().length() || accessor.getValue().length() >= accessor.getMaxLength();
            int positionAfterCursor = lengthBeforeCursor;
            if (!cursorInBox) {
                positionAfterCursor = headToCursor > 0 ? startX + this.width : startX;
            } else if (cursorBeforeEnd) {
                positionAfterCursor = lengthBeforeCursor - 1;
                --lengthBeforeCursor;
            }

            if (!displayedText.isEmpty() && cursorInBox && headToCursor < displayedText.length()) {
                guiGraphics.drawString(accessor.getFont(), accessor.getFormatter().apply(displayedText.substring(headToCursor), accessor.getCursorPos()), lengthBeforeCursor, startY, textColor, false);
            }

            if (!cursorBeforeEnd && accessor.getSuggestion() != null) {
                guiGraphics.drawString(accessor.getFont(), accessor.getSuggestion(), (positionAfterCursor - 1), startY, -8355712, false);
            }

            if (shouldRenderCursor) {
                if (cursorBeforeEnd) {
                    guiGraphics.fill(positionAfterCursor, startY - 1, positionAfterCursor + 1, startY + 1 + 9, -3092272);
                } else {
                    guiGraphics.drawString(accessor.getFont(), "_", positionAfterCursor, startY, textColor, false);
                }
            }
            if (headToHighlight != headToCursor) {
                int lengthBeforeHighlight = startX + (int)(accessor.getFont().width(displayedText.substring(0, headToHighlight)) * scaleX);
                this.renderHighlight(startX + (int)(accessor.getFont().width(textBeforeCursor) * scaleX), startY - 1,
                        lengthBeforeHighlight, startY + (int)(height * scaleY));
            }
        }
    }

    private void renderHighlight(int pStartX, int pStartY, int pEndX, int pEndY) {
        if (pStartX < pEndX) {
            int i = pStartX;
            pStartX = pEndX;
            pEndX = i;
        }

        if (pStartY < pEndY) {
            int j = pStartY;
            pStartY = pEndY;
            pEndY = j;
        }

        if (pEndX > this.getX() + this.width * scaleX) {
            pEndX = this.getX() + (int)(this.width * scaleX);
        }

        if (pStartX > this.getX() + this.width * scaleX) {
            pStartX = this.getX() + (int)(this.width * scaleX);
        }

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(0.0F, 0.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        bufferbuilder.vertex((double)pStartX, (double)pEndY, 0.0D).endVertex();
        bufferbuilder.vertex((double)pEndX, (double)pEndY, 0.0D).endVertex();
        bufferbuilder.vertex((double)pEndX, (double)pStartY, 0.0D).endVertex();
        bufferbuilder.vertex((double)pStartX, (double)pStartY, 0.0D).endVertex();
        tesselator.end();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableColorLogicOp();
        RenderSystem.enableBlend();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        AccessorEditBox accessor = (AccessorEditBox) this;
        if (!this.isVisible()) {
            return false;
        } else {
            boolean flag = pMouseX >= this.getX() && pMouseY >= this.getY() &&
                    pMouseX < this.getX() + this.width * scaleX &&
                    pMouseY < this.getY() + this.height * scaleY;
            if (accessor.getCanLoseFocus()) {
                this.setFocused(flag);
            }

            if (this.isFocused() && flag && pButton == 0) {
                int position = (int) Math.round((pMouseX - this.getX()) / scaleX);
                String textBeforePos = accessor.getFont().plainSubstrByWidth(accessor.getValue(), position);
                setCursorPosition(textBeforePos.length());
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return pMouseX >= this.getX() && pMouseY >= this.getY() &&
                pMouseX < this.getX() + this.width * scaleX &&
                pMouseY < this.getY() + this.height * scaleY;
    }

    public void setY(int y) {
        super.setY(y);
    }
}
