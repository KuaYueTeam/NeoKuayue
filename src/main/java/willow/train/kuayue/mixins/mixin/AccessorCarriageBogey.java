package willow.train.kuayue.mixins.mixin;

import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.content.trains.entity.TravellingPoint;
import net.createmod.catnip.data.Couple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CarriageBogey.class)
public interface AccessorCarriageBogey {

    @Accessor("type")
    public AbstractBogeyBlock<?> type();

    @Accessor("isLeading")
    public boolean isLeading();

    @Accessor("isLeading")
    public void setLeading(boolean leading);

    @Accessor("points")
    public Couple<TravellingPoint> getPoints();

    @Accessor("points")
    public void setPoints(Couple<TravellingPoint> points);
}
