package willow.train.kuayue.behaviour;

import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static willow.train.kuayue.block.panels.TrainPanelBlock.FACING;
import static willow.train.kuayue.block.panels.carport.DF11GChimneyBlock.LIT;

public class ChimneyMovementBehaviour implements MovementBehaviour {

    protected double posSpeedDiff = 0.0F;

    protected boolean isStopped = true;


    @Override
    public void startMoving(MovementContext context) {
        MovementBehaviour.super.startMoving(context);
    }

    @Override
    public void onSpeedChanged(MovementContext context, Vec3 oldMotion, Vec3 motion) {
        MovementBehaviour.super.onSpeedChanged(context, oldMotion, motion);
        double speed = motion.length();
        double oldSpeed = oldMotion.length();
        double speedDiff = speed - oldSpeed;
        posSpeedDiff = speedDiff > 0 ? speedDiff : 0;
        isStopped = Mth.equal(motion.length(), 0);
    }

    @Override
    public void tick(MovementContext context) {

        Level pLevel = context.world;
        BlockState pState = context.state;
        if (pLevel == null || !pLevel.isClientSide || context.position == null
                || !pState.getValue(CampfireBlock.LIT) || context.disabled)
            return;

        RandomSource pRandom = pLevel.random;
        Direction direction = pState.getValue(FACING);

        float density = 0.5F;
        if (isStopped) {
            density = 0.1F;
            posSpeedDiff = 0.0F;
        }

        if (pState.getValue(LIT)) {
            if (pRandom.nextFloat() < (density + posSpeedDiff * 10)) {
                if (pRandom.nextFloat() < 0.5F) {
                    pLevel.addParticle(
                            ParticleTypes.LARGE_SMOKE,
                            context.position.x(),
                            context.position.y() + 0.70D,
                            context.position.z(),
                            (direction == Direction.EAST || direction == Direction.WEST) ?
                                    (double)(0.05F + pRandom.nextFloat() / 10.0F) : 0.0F,
                            0.2F,
                            (direction == Direction.SOUTH || direction == Direction.NORTH) ?
                                    (double)(0.05F + pRandom.nextFloat() / 10.0F) : 0.0F);
                } else {
                    pLevel.addParticle(
                            ParticleTypes.LARGE_SMOKE,
                            context.position.x(),
                            context.position.y() + 0.70D,
                            context.position.z(),
                            (direction == Direction.EAST || direction == Direction.WEST) ?
                                    -(double)(0.05F + pRandom.nextFloat() / 10.0F) : 0.0F,
                            0.2F,
                            (direction == Direction.SOUTH || direction == Direction.NORTH) ?
                                    -(double)(0.05F + pRandom.nextFloat() / 10.0F) : 0.0F);
                }
            }
        }
    }
}
