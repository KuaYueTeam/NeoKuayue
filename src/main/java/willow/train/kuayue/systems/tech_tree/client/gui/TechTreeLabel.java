package willow.train.kuayue.systems.tech_tree.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import kasuga.lib.core.client.render.SimpleColor;
import kasuga.lib.core.client.render.texture.ImageMask;
import kasuga.lib.core.util.LazyRecomputable;
import kasuga.lib.core.util.data_type.Vec2i;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import willow.train.kuayue.initial.ClientInit;
import willow.train.kuayue.systems.editable_panel.widget.ImageButton;
import willow.train.kuayue.systems.tech_tree.NodeType;
import willow.train.kuayue.systems.tech_tree.client.ClientTechTree;
import willow.train.kuayue.systems.tech_tree.client.ClientTechTreeNode;

@Getter
public class TechTreeLabel extends ImageButton {

    private final ClientTechTreeNode node;

    private static final LazyRecomputable<ImageMask> smallBgMask = LazyRecomputable.of(
            () -> new ImageMask(ClientInit.blueprintButtons.getImageSafe().get())
                    .rectangleUV(0, 16f / 128f, 20f / 128f, 36f / 128f)
    );

    private static final LazyRecomputable<ImageMask> smallDarkBgMask = LazyRecomputable.of(
            () -> new ImageMask(ClientInit.blueprintButtons.getImageSafe().get())
                    .rectangleUV(0, 36f / 128f, 20f / 128f, 56f / 128f)
    );

    private static final LazyRecomputable<ImageMask> smallGoldBgMask = LazyRecomputable.of(
            () -> new ImageMask(ClientInit.blueprintButtons.getImageSafe().get())
                    .rectangleUV(96f / 128, 16f / 128, 116f / 128, 36f / 128)
    );

    private static final LazyRecomputable<ImageMask> largeBgMask = LazyRecomputable.of(
            () -> new ImageMask(ClientInit.blueprintButtons.getImageSafe().get())
                    .rectangleUV(32f / 128f, 0, 56f / 128f, 24f / 128f)
    );

    private static final LazyRecomputable<ImageMask> finishDotMask = LazyRecomputable.of(
            () -> new ImageMask(ClientInit.blueprintButtons.getImageSafe().get())
                    .rectangleUV(25f / 128f, 25f / 128f, 32f / 128f, 32f / 128f)
    );

    private boolean finished, required;

    protected TechTreeLabel(LazyRecomputable<ImageMask> foreground, LazyRecomputable<ImageMask> bg, ClientTechTreeNode node,
                         int x, int y, int width, int height, Component tooltip) {
        super(foreground, bg, x, y, width, height, tooltip, b -> {});
        this.node = node;
        finished = false;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
        if (this.finished) required = false;
    }

    public void setRequired(boolean required) {
        this.required = required;
        if (this.required) finished = false;
    }

    public static TechTreeLabel smallLabel(ClientTechTreeNode node, int x, int y, Component tooltip) {
        return new TechTreeLabel(node.getType() == NodeType.EPIC ?
                LazyRecomputable.of(() -> smallGoldBgMask.get().copyWithOp(o -> o)) :
                LazyRecomputable.of(() -> smallBgMask.get().copyWithOp(o -> o)),
                LazyRecomputable.of(() -> smallDarkBgMask.get().copyWithOp(o -> o)),
                node, x, y, 20, 20, tooltip);
    }

    public static TechTreeLabel largeLabel(ClientTechTreeNode node, int x, int y, Component tooltip) {
        return new TechTreeLabel(null, LazyRecomputable.of(() -> largeBgMask.get().copyWithOp(o -> o)),
                node, x, y, 24, 24, tooltip);
    }

    public Vec2i getCenterPos() {
        return new Vec2i(x + 10, y + 10);
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int mouseX, int mouseY, float partialTicks) {
        super.renderButton(pPoseStack, mouseX, mouseY, partialTicks);

        // render item
        ItemStack logo = node.getLogo();
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer renderer = mc.getItemRenderer();
        renderer.blitOffset = 100.0F;
        renderer.renderAndDecorateItem(logo, x + (this.width - 16) / 2, y + (this.height - 16) / 2);
        renderer.renderGuiItemDecorations(mc.font, logo, x + (this.width - 16) / 2, y + (this.height - 16) / 2);
        renderer.blitOffset = 0.0F;

        if (finished || required) {
            finishDotMask.get().rectangle(new Vector3f(this.x + this.width - 7, this.y + this.height - 7, 0),
                    ImageMask.Axis.X, ImageMask.Axis.Y, true, true, 7, 7);
            if (required) {
                finishDotMask.get().setColor(SimpleColor.fromRGBInt(0xff0000));
            } else {
                finishDotMask.get().setColor(SimpleColor.fromRGBInt(0xffffff));
            }
            finishDotMask.get().renderToGui();
        }
    }
}
