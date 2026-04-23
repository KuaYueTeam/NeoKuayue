package willow.train.kuayue.systems.device.driver.seat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.foundation.render.BlockEntityRenderHelper;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import javax.annotation.Nullable;

public class DoubleDriverSeatBlockMovementBehaviour implements MovementBehaviour {
    private static BlockPos getLightPos(@Nullable Matrix4f lightTransform, BlockPos contraptionPos) {
        if (lightTransform != null) {
            Vector4f lightVec = new Vector4f(
                (float)contraptionPos.getX() + 0.5F, 
                (float)contraptionPos.getY() + 0.5F, 
                (float)contraptionPos.getZ() + 0.5F, 
                1.0F
            );
            lightVec.mul(lightTransform);
            return new BlockPos((int)lightVec.x(), (int)lightVec.y(), (int)lightVec.z());
        } else {
            return contraptionPos;
        }
    }

    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
            ContraptionMatrices matrices, MultiBufferSource buffer) {
        PoseStack ms = matrices.getModelViewProjection();
        ms.pushPose();
        
        // 设置渲染位置
        ms.translate(context.localPos.getX(), context.localPos.getY(), context.localPos.getZ());
        
        // 渲染模型
        DoubleDriverSeatBlockRenderer.renderCommon(
            context.state,
            matrices.getModelViewProjection(),
            buffer,
                LevelRenderer.getLightColor(
                        context.world,
                        BlockPos.containing(context.position)
                ),
            OverlayTexture.NO_OVERLAY
        );
        
        ms.popPose();
    }
}
