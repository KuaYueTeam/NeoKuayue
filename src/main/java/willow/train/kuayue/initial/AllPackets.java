package willow.train.kuayue.initial;

import kasuga.lib.core.network.Packet;
import kasuga.lib.registrations.common.ChannelReg;
import willow.train.kuayue.network.c2s.ColorTemplateC2SPacket;
import willow.train.kuayue.network.c2s.NbtC2SPacket;
import willow.train.kuayue.network.s2c.ColorTemplateS2CPacket;
import willow.train.kuayue.network.s2c.ContraptionTagChangedPacket;

import java.util.LinkedList;

public class AllPackets {
    public static final String KUAYUE_NETWORK_VERSION = "v0.4.0";

    public static final ChannelReg CHANNEL = new ChannelReg("kuayue_channel")
            .brand(KUAYUE_NETWORK_VERSION)
            .loadPacket(ContraptionTagChangedPacket.class, ContraptionTagChangedPacket::new)
            .loadPacket(NbtC2SPacket.class, NbtC2SPacket::new)
            .submit(AllElements.testRegistry);

    public static final ChannelReg TEMPLATE = new ChannelReg("kuayue_template_channel")
            .brand(KUAYUE_NETWORK_VERSION)
            .loadPacket(ColorTemplateS2CPacket.class, ColorTemplateS2CPacket::new)
            .loadPacket(ColorTemplateC2SPacket.class, ColorTemplateC2SPacket::new)
            .submit(AllElements.testRegistry);

    public static void invoke() {}
}
