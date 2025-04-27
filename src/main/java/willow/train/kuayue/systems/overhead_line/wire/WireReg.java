package willow.train.kuayue.systems.overhead_line.wire;

import kasuga.lib.registrations.Reg;
import kasuga.lib.registrations.common.ItemReg;
import kasuga.lib.registrations.registry.SimpleRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.apache.http.impl.conn.Wire;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.systems.overhead_line.block.line.OverheadLineRenderer;
import willow.train.kuayue.systems.overhead_line.item.OverheadLineItem;
import willow.train.kuayue.systems.overhead_line.types.OverheadLineType;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WireReg extends Reg {

    protected static HashMap<ResourceLocation, OverheadLineType> wireRegMap = new HashMap<>();

    protected static HashMap<OverheadLineType, ResourceLocation> reverseWireRegMap = new HashMap<>();

    protected OverheadLineType wireType;

    protected float maxVoltage = Float.MAX_VALUE, maxCurrent = Float.MAX_VALUE, maxLength = Float.MAX_VALUE;

    protected Supplier<Supplier<OverheadLineRenderer>> renderer;

    protected ItemReg<? extends OverheadLineItem> itemReg;

    public WireReg(String registrationKey) {
        super(registrationKey);
        itemReg = new ItemReg<OverheadLineItem>(registrationKey)
                .tab(AllElements.neoKuayueOverheadLineTab)
                .itemType(OverheadLineItem::new);
    }

    public WireReg useItemConfiguration(Consumer<ItemReg<? extends OverheadLineItem>> itemConsumer) {
        itemConsumer.accept(itemReg);
        return this;
    }

    @Override
    public WireReg submit(SimpleRegistry simpleRegistry) {
        ResourceLocation name = simpleRegistry.asResource(this.registrationKey);
        wireType = new OverheadLineType(name, maxVoltage, maxCurrent, maxLength);
        wireRegMap.put(name, wireType);
        reverseWireRegMap.put(wireType, name);
        this.itemReg.submit(simpleRegistry);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            OverheadLineRendererUtils.registerRenderer(wireType, renderer);
        });
        return this;
    }

    public OverheadLineType getWireType() {
        return wireType;
    }

    public OverheadLineItem getItem(){
        return this.itemReg.getItem();
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    public WireReg maxVoltage(float maxVoltage) {
        this.maxVoltage = maxVoltage;
        return this;
    }

    public WireReg maxCurrent(float maxCurrent) {
        this.maxCurrent = maxCurrent;
        return this;
    }

    public WireReg maxLength(float maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public WireReg renderer(Supplier<Supplier<OverheadLineRenderer>> renderer) {
        this.renderer = renderer;
        return this;
    }

    public static OverheadLineType get(ResourceLocation resourceLocation) {
        return wireRegMap.get(resourceLocation);
    }

    public static ResourceLocation getName(OverheadLineType wireType) {
        if(!reverseWireRegMap.containsKey(wireType)) {
            throw new IllegalArgumentException("Unknown wire type: " + wireType);
        }
        return reverseWireRegMap.get(wireType);
    }
}
