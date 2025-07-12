package willow.train.kuayue.event.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import java.util.HashMap;
import java.util.Map;

/**
 * The ItemTooltipConfig class is designed to manage the configuration of item tooltips.
 * It stores mappings between item IDs, translation keys, and text colors, and provides
 * methods to retrieve and create tooltip components based on these mappings.
 *
 * <p>This class plays a role in conjunction with event handlers related to item tooltips,
 * This class works in conjunction with the event handlers related to item tooltips,
 * and is associated with the {@link willow.train.kuayue.event.client.TooltipEvent#onItemTooltipCheckItemType}.
 * When an item tooltip event is triggered, the relevant event handler can use this class
 * to obtain the appropriate tooltip information for the item and add it to the tooltip list.</p>
 *
 * <p>You should use this class when you need to customize the tooltips for different items
 * and support multi - language display. By adding new item IDs, translation keys, and colors
 * in the static block, you can easily expand the configuration of item tooltips.</p>
 */
public class ItemTooltipConfig {
    private static final Map<String, TooltipInfo> itemTooltipMap = new HashMap<>();

    static {
        // Use translation keys instead of specific tooltip texts
        addItemTooltip("ss8_head", "tip.kuayue.ss8_head", ChatFormatting.GREEN);
        addItemTooltip("ss3_head", "tip.kuayue.ss3_head", ChatFormatting.GREEN);
    }

    private static void addItemTooltip(String itemId, String translationKey, ChatFormatting color) {
        itemTooltipMap.put(itemId, new TooltipInfo(translationKey, color));
    }

    public static TooltipInfo getTooltipInfo(String itemId) {
        return itemTooltipMap.get(itemId);
    }

    public static class TooltipInfo {
        private final String translationKey;
        private final ChatFormatting color;

        public TooltipInfo(String translationKey, ChatFormatting color) {
            this.translationKey = translationKey;
            this.color = color;
        }

        public MutableComponent createTooltipComponent() {
            return Component.translatable(translationKey).setStyle(Style.EMPTY.withColor(color));
        }
    }
}