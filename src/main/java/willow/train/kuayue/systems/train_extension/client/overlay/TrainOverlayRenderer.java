package willow.train.kuayue.systems.train_extension.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.RemovedGuiUtils;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;

public class TrainOverlayRenderer {
    private static int hoverTicks = 0;
    private static boolean stateChanged = false;

    public static IGuiOverlay OVERLAY = TrainOverlayRenderer::render;

    public static boolean visible = false;
    public static ItemStack icon = ItemStack.EMPTY;
    public static Component message = Component.empty();

    public static void setVisible(boolean visible) {
        TrainOverlayRenderer.visible = visible;
    }

    public static void setShowInfo(ItemStack icon, Component message) {
        if(icon == null || message == null) return;

        TrainOverlayRenderer.icon = icon;
        TrainOverlayRenderer.message = message;
    }

    public static void clearShowInfo() {
        TrainOverlayRenderer.visible = false;
        TrainOverlayRenderer.icon = ItemStack.EMPTY;
        TrainOverlayRenderer.message = Component.empty();
    }

    public static void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTicks, int width, int height) {
        if (!visible || message.equals(Component.empty())) {
            hoverTicks = 0;
            return;
        }

        PoseStack poseStack = guiGraphics.pose();


        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        if(stateChanged) {
            hoverTicks = 1;
            stateChanged = false;
        } else {
            hoverTicks++;
        }

        Component component = icon.equals(ItemStack.EMPTY) ?
                message :
                Component.literal("    ").append(message);

        poseStack.pushPose();

        CClient cfg = AllConfigs.client();
        int posX = width / 2 + cfg.overlayOffsetX.get();
        int posY = height / 2 + cfg.overlayOffsetY.get();

        posX = Math.min(posX, width - mc.font.width(component) - 20);
        posY = Math.min(posY, height - 28);

        float fade = Mth.clamp((hoverTicks + partialTicks) / 24f, 0, 1);
        Boolean useCustom = cfg.overlayCustomColor.get();
        Color colorBackground = useCustom ? new Color(cfg.overlayBackgroundColor.get())
                : Theme.c(Theme.Key.VANILLA_TOOLTIP_BACKGROUND)
                .scaleAlpha(.75f);
        Color colorBorderTop = useCustom ? new Color(cfg.overlayBorderColorTop.get())
                : Theme.c(Theme.Key.VANILLA_TOOLTIP_BORDER, true)
                .copy();
        Color colorBorderBot = useCustom ? new Color(cfg.overlayBorderColorBot.get())
                : Theme.c(Theme.Key.VANILLA_TOOLTIP_BORDER, false)
                .copy();

        if (fade < 1) {
            poseStack.translate(Math.pow(1 - fade, 3) * Math.signum(cfg.overlayOffsetX.get() + .5f) * 8, 0, 0);
            colorBackground.scaleAlpha(fade);
            colorBorderTop.scaleAlpha(fade);
            colorBorderBot.scaleAlpha(fade);
        }

        List<Component> tooltip = new ArrayList<>();
        tooltip.add(component);

        RemovedGuiUtils.drawHoveringText(guiGraphics, tooltip, posX, posY, width, height, -1, colorBackground.getRGB(),
                colorBorderTop.getRGB(), colorBorderBot.getRGB(), mc.font);

        GuiGameElement.of(icon)
                .at(posX + 10, posY - 16, 450)
                .render(guiGraphics);

        poseStack.popPose();
    }
}
