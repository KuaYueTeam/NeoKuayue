package willow.train.kuayue.utils;

import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TrainStatus;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import willow.train.kuayue.mixins.mixin.AccessorTrainStatus;

import java.util.List;

public class TrainUtil {
    public static void displayInformation(Train train, String msgKey, boolean itsAGoodThing) {
        if (train == null) return;
        TrainStatus status = train.status;
        if(status == null) return;
        List<Component> queuedMessages = ((AccessorTrainStatus) status).getQueued();

        Level level = null;
        for(Carriage carriage : train.carriages) {
            Entity entity = carriage.anyAvailableEntity();
            if (entity != null) {
                level = entity.level();
                break;
            }
        }
        if(level == null) return;

        queuedMessages.add(Components.literal(" - ").withStyle(ChatFormatting.GRAY)
                .append(Components.translatable(msgKey)).withStyle(st -> st.withColor(itsAGoodThing ? 0xD5ECC2 : 0xFFD3B4)));
        if (queuedMessages.size() > 3)
            queuedMessages.remove(0);
    }
}
