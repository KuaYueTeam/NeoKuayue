package willow.train.kuayue.utils.client;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.ContraptionHandlerClient;
import kasuga.lib.core.util.data_type.Pair;
import net.createmod.catnip.data.Couple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ContraptionAimUtil {

    /*
     * Gets the block hit result on the contraption that the player is currently aiming at within the specified max distance.
     */
    public static @Nullable Pair<AbstractContraptionEntity, BlockHitResult> getTargetContraptionBlock(Player player, double maxDistance) {
        Vec3 eyePosition = player.getEyePosition(1.0F);
        Vec3 lookVector = player.getViewVector(1.0F);

        Couple<Vec3> rayInputs = ContraptionHandlerClient.getRayInputs((LocalPlayer) player);
        Vec3 origin = rayInputs.getFirst();
        Vec3 target = rayInputs.getSecond();
        AABB aabb = new AABB(origin, target).inflate(16);


        Level level = Minecraft.getInstance().level;
        if(level == null) return null;


        List<AbstractContraptionEntity> entities = level.getEntitiesOfClass(
                AbstractContraptionEntity.class,
                player.getBoundingBox().expandTowards(lookVector.scale(maxDistance)).inflate(1.0D)
        );

        for(AbstractContraptionEntity entity : entities) {

            if(!entity.getBoundingBox().intersects(aabb)) continue;

            BlockHitResult rayTraceResult = ContraptionHandlerClient.rayTraceContraption(origin, target, entity);
            if(rayTraceResult == null) continue;

            return Pair.of(entity, rayTraceResult);
        }
        return null;
    }
}
