package willow.train.kuayue.initial.ore;

import kasuga.lib.registrations.common.BlockReg;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import willow.train.kuayue.initial.AllElements;

public class AllOres {

    public static final BlockReg<DropExperienceBlock> SALT_ORE =
            new BlockReg<DropExperienceBlock>("salt_ore")
                    .blockType(props ->
                            new DropExperienceBlock(props, UniformInt.of(3, 7)))
                    .materialColor(MapColor.STONE)
                    .addProperty(properties -> properties.strength(1.5f, 6.0F))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<DropExperienceBlock> DEEPSLATE_SALT_ORE =
            new BlockReg<DropExperienceBlock>("deepslate_salt_ore")
                    .blockType(props ->
                            new DropExperienceBlock(props, UniformInt.of(3, 7)))
                    .materialColor(MapColor.STONE)
                    .addProperty(properties -> properties.strength(1.5f, 6.0F))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    public static final BlockReg<Block> SALT_BLOCK =
            new BlockReg<Block>("salt_block")
                    .blockType(Block::new)
                    .materialColor(MapColor.STONE)
                    .addProperty(properties -> properties.sound(SoundType.TUFF))
                    .addProperty(properties -> properties.strength(1.0f, 3.0F))
                    .addProperty(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueMainTab)
                    .submit(AllElements.testRegistry);

    // 自mc 1.20.1 后强制使用数据驱动的方式注册矿物，下述代码无效也不应在之后的版本使用
//    @Deprecated
//    public static final OreReg<DropExperienceBlock> SALT_ORE_REG =
//            new OreReg<DropExperienceBlock>("salt_ore")
//            .addOreReplaceTarget(SALT_ORE)
//            .addDeepSlateReplaceTarget(DEEPSLATE_SALT_ORE)
//            .setOreCountPerChunk(90)
//            .setOreQuantityPerGroup(9)
//            .setOreDistributionType(PlacedFeatureReg.DistributionType.TRIANGLE)
//            .setOreAnchorAbsolute(80, -64)
//            .submit(AllElements.testRegistry);

    public static void invoke() {}
}
