package willow.train.kuayue.behaviour;


import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class AnimationMovementBehaviour implements MovementBehaviour {

    @Nullable
    @Override
    public ItemStack canBeDisabledVia(MovementContext context) {
        return null;
    }

    @Override
    public boolean isActive(MovementContext context) {
        return true;
    }


    @Override
    public boolean mustTickWhileDisabled() {
        return true;
    }


}
