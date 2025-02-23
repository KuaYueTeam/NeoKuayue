package willow.train.kuayue.systems.overhead_line.block.support;

import kasuga.lib.registrations.common.BlockEntityReg;
import kasuga.lib.registrations.common.BlockReg;
import kasuga.lib.registrations.common.ItemReg;
import kasuga.lib.registrations.registry.SimpleRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import willow.train.kuayue.common.OptionalTrackTargetingBlockItem;
import willow.train.kuayue.systems.overhead_line.OverheadLineSystem;
import willow.train.kuayue.systems.overhead_line.types.OverheadLineType;

import java.util.function.Predicate;

public class OverheadLineSupportBlockReg<T extends OverheadLineSupportBlock> extends BlockReg<T> {
    private Predicate<OverheadLineType> allowedOverheadLineTypePredictor;
    public OverheadLineSupportBlockReg(String registrationKey) {
        super(registrationKey);
    }

    public OverheadLineSupportBlockReg<T> allowOverheadLineType(Predicate<OverheadLineType> overheadLineTypePredictor) {
        this.allowedOverheadLineTypePredictor = overheadLineTypePredictor;
        return this;
    }

    @Override
    public OverheadLineSupportBlockReg<T> submit(SimpleRegistry simpleRegistry) {
        super.submit(simpleRegistry);
        return this;
    }

    public OverheadLineSupportBlockReg<T> withTrackTargetingItem(ResourceLocation itemModelLocation) {
        var blockItem = OptionalTrackTargetingBlockItem.ofOptional(OverheadLineSystem.OVERHEAD_LINE_EDGE_POINT);
        withItem(properties -> blockItem.apply(this.getBlock(), properties), itemModelLocation);
        return this;
    }

    @Override
    public <R extends Item> OverheadLineSupportBlockReg<T> withItem(ItemReg.ItemBuilder<R> builder, ResourceLocation itemModelLocation) {
        super.withItem(builder, itemModelLocation);
        return this;
    }

    @Override
    public OverheadLineSupportBlockReg<T> defaultBlockItem() {
        super.defaultBlockItem();
        return this;
    }

    @Override
    public OverheadLineSupportBlockReg<T> withBlockEntity(BlockEntityReg<? extends BlockEntity> blockEntityReg) {
        super.withBlockEntity(blockEntityReg);
        return this;
    }

    @Override
    public OverheadLineSupportBlockReg<T> blockType(BlockBuilder<T> builder) {
        super.blockType(builder);
        return this;
    }

    public String getIdentifier() {
        return "overhead_line_support_block";
    }
}