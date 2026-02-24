package willow.train.kuayue.initial.registration;

import kasuga.lib.registrations.BlockEntityRendererBuilder;
import kasuga.lib.registrations.common.BlockReg;
import kasuga.lib.registrations.common.CreativeTabReg;
import kasuga.lib.registrations.common.ItemReg;
import kasuga.lib.registrations.registry.SimpleRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import willow.train.kuayue.block.panels.slab.TrainSlabBlock;
import willow.train.kuayue.item.SlabBlockItem;

import java.util.function.Supplier;

public class SlabRegistration<T extends TrainSlabBlock> extends PanelRegistration<T> {

    public SlabRegistration(String registrationKey) {
        super(registrationKey);
        item = new ItemReg<SlabBlockItem>(registrationKey);
        item.itemType(p -> new SlabBlockItem(block.getBlock(), p));
    }
    
    public SlabRegistration<T> materialAndColor(MapColor materialColor) {
        block.materialColor(materialColor);
        return this;
    }

    public SlabRegistration<T> noOcclusion() {
        block.addProperty(BlockBehaviour.Properties::noOcclusion);
        return this;
    }

    public SlabRegistration<T> block(BlockReg.BlockBuilder<T> builder) {
        this.block.blockType(builder);
        return this;
    }

    public SlabRegistration<T> blockEntity(BlockEntityType.BlockEntitySupplier<?> provider) {
        block.withBlockEntity(block.registrationKey, provider);
        return this;
    }

    public SlabRegistration<T> blockEntityRenderer(Supplier<BlockEntityRendererBuilder> renderer) {
        block.withBlockEntityRenderer(renderer);
        return this;
    }

    public SlabRegistration<T> tab(CreativeTabReg reg) {
        this.item.tab(reg);
        return this;
    }

    public SlabRegistration<T> stackSize(int size) {
        this.item.stackTo(size);
        return this;
    }

    public SlabRegistration<T> strength(float strength, float explosionResistance) {
        this.block.addProperty(properties -> properties.strength(strength, explosionResistance));
        return this;
    }

    public SlabRegistration<T> requireCorrectTools() {
        this.block.addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops);
        return this;
    }

    public SlabRegistration<T> strengthAndTool(float strength, float explosionResistance) {
        this.block.addProperty(properties -> properties.strength(strength, explosionResistance));
        this.block.addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops);
        return this;
    }

    public SlabRegistration<T> submit(SimpleRegistry registry) {
        this.block.submit(registry);
        this.item.submit(registry);
        return this;
    }
}
