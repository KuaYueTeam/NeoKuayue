package willow.train.kuayue.initial.registration;

import kasuga.lib.registrations.BlockEntityRendererBuilder;
import kasuga.lib.registrations.common.BlockReg;
import kasuga.lib.registrations.common.CreativeTabReg;
import kasuga.lib.registrations.common.ItemReg;
import kasuga.lib.registrations.registry.SimpleRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import willow.train.kuayue.block.panels.SkirtBlock;
import willow.train.kuayue.item.SkirtBlockItem;

import java.util.function.Supplier;

public class SkirtRegistration<T extends SkirtBlock> extends PanelRegistration<T> {

    public SkirtRegistration(String registrationKey) {
        super(registrationKey);
        item = new ItemReg<SkirtBlockItem>(registrationKey);
        item.itemType(p -> new SkirtBlockItem(block.getBlock(), p));
    }
    
    public SkirtRegistration<T> materialAndColor(MapColor materialColor) {
        block.materialColor(materialColor);
        return this;
    }

    public SkirtRegistration<T> noOcclusion() {
        block.addProperty(BlockBehaviour.Properties::noOcclusion);
        return this;
    }

    public SkirtRegistration<T> block(BlockReg.BlockBuilder<T> builder) {
        this.block.blockType(builder);
        return this;
    }

    public SkirtRegistration<T> blockEntity(BlockEntityType.BlockEntitySupplier<?> provider) {
        block.withBlockEntity(block.registrationKey, provider);
        return this;
    }

    public SkirtRegistration<T> blockEntityRenderer(Supplier<BlockEntityRendererBuilder> renderer) {
        block.withBlockEntityRenderer(renderer);
        return this;
    }

    public SkirtRegistration<T> tab(CreativeTabReg reg) {
        this.item.tab(reg);
        return this;
    }

    public SkirtRegistration<T> stackSize(int size) {
        this.item.stackTo(size);
        return this;
    }

    public SkirtRegistration<T> strength(float strength, float explosionResistance) {
        this.block.addProperty(properties -> properties.strength(strength, explosionResistance));
        return this;
    }

    public SkirtRegistration<T> requireCorrectTools() {
        this.block.addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops);
        return this;
    }

    public SkirtRegistration<T> strengthAndTool(float strength, float explosionResistance) {
        this.block.addProperty(properties -> properties.strength(strength, explosionResistance));
        this.block.addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops);
        return this;
    }

    public SkirtRegistration<T> submit(SimpleRegistry registry) {
        this.block.submit(registry);
        this.item.submit(registry);
        return this;
    }
}
