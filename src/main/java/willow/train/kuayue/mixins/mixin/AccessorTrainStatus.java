package willow.train.kuayue.mixins.mixin;

import com.simibubi.create.content.trains.entity.TrainStatus;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(TrainStatus.class)
public interface AccessorTrainStatus {
    @Accessor("queued")
    List<Component> getQueued();
}
