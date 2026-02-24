package willow.train.kuayue.block.panels.block_entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.FormattedCharSequence;
import willow.train.kuayue.block.panels.block_entity.EditablePanelEntity;
import willow.train.kuayue.systems.editable_panel.EditableTypeConstants;
import willow.train.kuayue.systems.editable_panel.SignType;
import willow.train.kuayue.systems.editable_panel.interfaces.SignRenderLambda;

import java.awt.*;

public class EditablePanelRenderer implements BlockEntityRenderer<EditablePanelEntity> {

    private final Font font;

    public EditablePanelRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
    }

    @Override
    public void render(EditablePanelEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        for (SignType signType : EditableTypeConstants.getSignTypeMap().values()) {
            if (signType.shouldRender(pBlockEntity.getEditType())) {
                signType.getLambdaSupplier().get().render(pBlockEntity, pPartialTick, new SignRenderLambda.ClientObject(pPoseStack),
                        new SignRenderLambda.ClientObject(pBufferSource), pPackedLight ,pPackedOverlay, Minecraft.getInstance().options.forceUnicodeFont().get());
                return;
            }
        }
    }

    private void renderText(
            FormattedCharSequence[] aformattedcharsequence,
            int index,
            int pX,
            int pY,
            EditablePanelEntity pBlockEntity,
            PoseStack pPoseStack,
            MultiBufferSource pBufferSource,
            int pPackedLight) {

        FormattedCharSequence formattedcharsequence = aformattedcharsequence[index];
        this.font.drawInBatch(
                formattedcharsequence,
                pX,
                pY,
                getDarkColor(pBlockEntity) * 2,
                false,
                pPoseStack.last().pose(),
                pBufferSource,
                Font.DisplayMode.NORMAL,
                0,
                pPackedLight);
    }

    private static int getDarkColor(EditablePanelEntity pBlockEntity) {
        int i = pBlockEntity.getColor();
        Color color = new Color(i);
        int j = (int)((double) color.getRed() * 0.4D);
        int k = (int)((double) color.getGreen() * 0.4D);
        int l = (int)((double) color.getBlue() * 0.4D);
        return new Color(j, k, l).getRGB();
    }
}
