package willow.train.kuayue.systems.device.driver.combustion;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import kasuga.lib.core.util.data_type.Pair;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.render.BlockEntityRenderHelper;
import kasuga.lib.core.create.device.TrainDeviceLocation;
import kasuga.lib.core.create.device.TrainDeviceManager;
import kasuga.lib.core.menu.base.GuiMenu;
import kasuga.lib.core.menu.locator.ContraptionBlockMenuLocator;
import kasuga.lib.core.menu.locator.GuiMenuHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.systems.device.AllDeviceSystems;
import willow.train.kuayue.systems.device.AllDevicesMenus;
import willow.train.kuayue.systems.device.IEntityTrackingMovementBehavior;
import willow.train.kuayue.systems.device.driver.seat.AnchorPoint;
import willow.train.kuayue.systems.device.driver.seat.InteractiveBehaviour;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class InternalCombustionDriveControllerMovementBehavior
        implements MovementBehaviour, IEntityTrackingMovementBehavior, InteractiveBehaviour {
    protected HashMap<MovementContext, GuiMenuHolder> MENUS = new HashMap<>();

    @Override
    public void startMoving(MovementContext context) {
        MovementBehaviour.super.startMoving(context);
        if(context.world.isClientSide()) {
            return;
        }
        Pair<TrainDeviceManager, TrainDeviceLocation> location = TrainDeviceManager.getManager(context);
        if(location == null || location.getSecond() == null)
            return;
        // location.getFirst().getOrCreateSystem();
    }

    @Override
    public void tick(MovementContext context) {
        if(!MENUS.containsKey(context)){
            ContraptionBlockMenuLocator locator = ContraptionBlockMenuLocator.fromLocator(context);
            locator.withLevel(context.world);
            GuiMenuHolder holder = new GuiMenuHolder.Builder()
                    .with(AllDevicesMenus.LKJ2000, (menu)->{
                        if(context.world.isClientSide()) {
                            menu.withMovementContext(context);
                        }
                        if(context.contraption.entity instanceof CarriageContraptionEntity carriageContraption){
                            menu.provideTrainData(new TrainDataHandler(Create.RAILWAYS.trains.get(carriageContraption.trainId)));
                        }
                    })
                    .with(AllDevicesMenus.CIR)
                    .with(AllDevicesMenus.INTERNAL_COMBUSTION_DRIVING_MENU, (menu)->{
                        if(!context.world.isClientSide) {
                            Pair<TrainDeviceManager, TrainDeviceLocation> pair = TrainDeviceManager.getManager(context);
                            menu.providePower(pair.getFirst().getOrCreateSystem(AllDeviceSystems.POWER), pair.getSecond());
                        }
                    })
                    .locatedAt(locator)
                    .build();
            holder.enable(context.world);
            MENUS.put(context, holder);
        }
        if(MENUS.containsKey(context) && !context.world.isClientSide()) {
            ((ContraptionBlockMenuLocator) MENUS.get(context).getLocator()).update(context);
        }
    }


    @Override
    public void onEntityLeave(MovementContext behaviour) {
        if(!MENUS.containsKey(behaviour))
            return;
        GuiMenuHolder holder = MENUS.remove(behaviour);
        holder.disable();
    }

    @Override
    public boolean mustTickWhileDisabled() {
        return true;
    }

    private static BlockPos getLightPos(@Nullable Matrix4f lightTransform, BlockPos contraptionPos) {
        // Copied from Create mod
        if (lightTransform != null) {
            Vector4f lightVec = new Vector4f((float)contraptionPos.getX() + 0.5F, (float)contraptionPos.getY() + 0.5F, (float)contraptionPos.getZ() + 0.5F, 1.0F);
            lightVec.mul(lightTransform);
            return new BlockPos((int)lightVec.x(), (int)lightVec.y(), (int)lightVec.z());
        } else {
            return contraptionPos;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        if(!MENUS.containsKey(context))
            return;
        GuiMenuHolder holder = MENUS.get(context);
        PoseStack ms = matrices.getModelViewProjection();
        ms.pushPose();
        ms.translate(context.localPos.getX(), context.localPos.getY(), context.localPos.getZ());
        InternalCombustionDriveControllerBlockEntityRenderer.renderCommon(
                context.state,
                holder,
                matrices.getModelViewProjection(),
                buffer,
                LevelRenderer.getLightColor(
                        context.world,
                        BlockPos.containing(context.position)
                ),
                OverlayTexture.NO_OVERLAY,
                AnimationTickHolder.getPartialTicks()
        );
        ms.popPose();
    }

    @Override
    public List<MenuEntry> getMenusOf(MovementContext context) {
        return List.of(
                new MenuEntry(
                        Component.literal("LKJ2000"),
                        AllElements.testRegistry.asResource("lkj2000"),
                        ()->MENUS.containsKey(context),
                        ()->MENUS.get(context).getMenu(0).orElseThrow(),
                        new Vec2(-1, 1),
                        new Vec2(0.3f,0.3f),
                        new Vec2(256,252)
                ) {{
                    setScreenAnchor(AnchorPoint.TOP_RIGHT);
                    setSelfAnchor(AnchorPoint.TOP_RIGHT);
                }},
                new MenuEntry(
                        Component.literal("CIR"),
                        AllElements.testRegistry.asResource("cir"),
                        ()->MENUS.containsKey(context),
                        ()->MENUS.get(context).getMenu(1).orElseThrow(),
                        new Vec2(-1, 256 * 0.3f + 2),
                        new Vec2(0.3f,0.3f),
                        new Vec2(328,252)
                ) {{
                    setScreenAnchor(AnchorPoint.TOP_RIGHT);
                    setSelfAnchor(AnchorPoint.TOP_RIGHT);
                }},
                new MenuEntry(
                        Component.literal("Train Control"),
                        AllElements.testRegistry.asResource("control"),
                        ()->MENUS.containsKey(context),
                        ()->MENUS.get(context).getMenu(2).orElseThrow(),
                        new Vec2(0, -4),
                        new Vec2(0.075f,0.075f),
                        new Vec2(5080,680)
                ) {{
                    setScreenAnchor(AnchorPoint.BOTTOM_CENTER);
                    setSelfAnchor(AnchorPoint.BOTTOM_CENTER);
                    setFixed(true);
                }}
        );
    }
}
