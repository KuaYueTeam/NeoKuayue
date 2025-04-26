package willow.train.kuayue.systems.overhead_line.block.line;

import kasuga.lib.core.client.model.anim_model.AnimModel;
import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.mozilla.javascript.ast.Block;
import willow.train.kuayue.mixins.mixin.LevelRendererAccessor;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlockEntity;
import willow.train.kuayue.systems.overhead_line.render.CachedCurveRenderer;
import willow.train.kuayue.systems.overhead_line.render.RenderCurve;
import willow.train.kuayue.systems.overhead_line.types.OverheadLineType;
import willow.train.kuayue.systems.overhead_line.wire.OverheadLineRendererUtils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static net.minecraftforge.client.event.RenderLevelStageEvent.Stage.*;

public class OverheadLineRendererSystem {

    public static final HashMap<OverheadLineType, OverheadLineRenderer> RENDERERS = new HashMap<>();

    public static void registerRenderer(OverheadLineType wireType, Supplier<Supplier<OverheadLineRenderer>> renderer) {
        RENDERERS.put(wireType, renderer.get().get());
    }

    public static OverheadLineRenderer getRendererFor(OverheadLineType type) {
        if(!RENDERERS.containsKey(type)){
            throw new IllegalArgumentException("Unknown wire type: " + type);
        }
        return RENDERERS.get(type);
    }

    public static void clearLevel() {
        OVERHEAD_LINES.clear();
        LOCK.clear();
    }


    public static record OverheadLineLocator(
            ResourceKey<Level> level,
            BlockPos fromPosition,
            BlockPos toPosition,
            int fromIndex,
            int toIndex
    ) {
        public static OverheadLineLocator createFromConnection(
                ResourceKey<Level> level,
                BlockPos $fromPosition,
                OverheadLineSupportBlockEntity.Connection connection
        ) {

            BlockPos $targetPosition = connection.absolutePos();

            int $fromIndex = connection.connectionIndex();
            int $toIndex =  connection.targetIndex();

            if(PositionComparator.compareBlockPosition($fromPosition, $targetPosition) < 0) {
                BlockPos $tmp = $fromPosition;
                $fromPosition = $targetPosition;
                $targetPosition = $tmp;

                int $tmpI = $fromIndex;
                $fromIndex = $toIndex;
                $toIndex = $tmpI;
            }

            return new OverheadLineLocator(
                level,
                $fromPosition,
                $targetPosition,
                $fromIndex,
                $toIndex
            );
        }
    }

    public record Rendering(
            RenderCurve curve,
            AnimModel model,
            AABB boundingBox
    ){ }


    public static HashMap<OverheadLineLocator, Rendering> OVERHEAD_LINES = new HashMap<>();

    public static HashMap<OverheadLineLocator, Pair<Boolean, Boolean>> LOCK = new HashMap<>();

    public static void registerOverheadLine(OverheadLineSupportBlockEntity blockEntity, OverheadLineSupportBlockEntity.Connection connection){
        OverheadLineLocator locator = OverheadLineLocator.createFromConnection(blockEntity.getLevel().dimension(), blockEntity.getBlockPos(), connection);
        boolean isFirst = locator.fromPosition() == blockEntity.getBlockPos();
        if(LOCK.containsKey(locator)) {
            LOCK.put(locator, replace(LOCK.get(locator), isFirst, true));
            return;
        }

        OverheadLineRenderer renderer = OverheadLineRendererSystem.getRendererFor(connection.type());

        RenderCurve curve = renderer.getRenderCurveFor(
                blockEntity.getLevel(),
                blockEntity.getConnectionPointByIndex(connection.connectionIndex()),
                new Vec3(connection.toPosition())
        );
        OVERHEAD_LINES.put(locator, new Rendering(curve, renderer.getModel(), curve.getMatrix().getBoundingBox()));
        LOCK.put(locator, replace(Pair.of(false, false), isFirst, true));
    }

    private static Pair<Boolean, Boolean> replace(Pair<Boolean, Boolean> source, boolean isFirst, boolean data) {
        return isFirst ? Pair.of(source.getFirst(), data) : Pair.of(data, source.getSecond());
    }

    public static void removeOverheadLine(OverheadLineSupportBlockEntity blockEntity, OverheadLineSupportBlockEntity.Connection connection){
        OverheadLineLocator locator = OverheadLineLocator.createFromConnection(blockEntity.getLevel().dimension(), blockEntity.getBlockPos(), connection);
        boolean isFirst = locator.fromPosition() == blockEntity.getBlockPos();
        if(!LOCK.containsKey(locator)) {
            OVERHEAD_LINES.remove(locator);
            return;
        }
        Pair<Boolean, Boolean> newLock = replace(LOCK.get(locator), isFirst, false);

        if(!newLock.getFirst() && !newLock.getSecond()) {
            OVERHEAD_LINES.remove(locator);
            LOCK.remove(locator);
            return;
        }

        LOCK.put(locator, newLock);
    }

    public static void onRenderLevelLast(RenderLevelStageEvent event) {

        ClientLevel level = ((LevelRendererAccessor) event.getLevelRenderer()).getLevel();

        MultiBufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        if(event.getStage() != AFTER_SKY)
            return;
        Frustum frustum = event.getFrustum();

        Vec3 vec3 = event.getCamera().getPosition();
        double d0 = vec3.x();
        double d1 = vec3.y();
        double d2 = vec3.z();

        event.getPoseStack().pushPose();
        event.getPoseStack().translate(-d0, -d1, -d2);

        OVERHEAD_LINES.forEach((position, rendering)->{
            if(position.level() != level.dimension())
                return;
            AABB boundingBox = rendering.boundingBox();
            if(boundingBox == null || !frustum.isVisible(boundingBox))
                return;
            RenderCurve curve = rendering.curve();
            if(curve == null)
                return;
            event.getPoseStack().pushPose();
            CachedCurveRenderer.render(
                    rendering.model(),
                    curve,
                    event.getPoseStack(),
                    bufferSource,
                    OverlayTexture.NO_OVERLAY
            );
            event.getPoseStack().popPose();
        });

        event.getPoseStack().popPose();
    }

}
