package willow.train.kuayue.mixins.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntityRenderer;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import willow.train.kuayue.block.bogey.loco.renderer.QJMainRenderer;

@Mixin(value = CarriageContraptionEntityRenderer.class, remap = false)
public class MixinCarriageContraptionEntityRenderer {

    @Inject(method = "translateBogey", at = @At("TAIL"), remap = false)
    private static void injectDataToBogeyData(
            PoseStack ms, CarriageBogey bogey, int bogeySpacing,
            float viewYRot, float viewXRot, float partialTicks,
            CallbackInfo ci) {

        // 在 1.20.1 中，使用 typeData 来传递运行数据
        CompoundTag data = bogey.bogeyData;

        if (bogey.carriage != null && bogey.carriage.train != null) {
            // 将速度同步进去，QJ 的连杆需要这个值
            data.putDouble("TrainSpeed", bogey.carriage.train.speed);

            // 如果你需要逆转机逻辑，也可以同步
            // data.putFloat("Reversing", bogey.carriage.train.throttle);
        }
    }
}
