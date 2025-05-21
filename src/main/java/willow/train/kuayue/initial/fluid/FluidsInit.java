package willow.train.kuayue.initial.fluid;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.block.food.fluid.BlueBullBlock;
import willow.train.kuayue.block.food.fluid.ColaBlock;
import willow.train.kuayue.initial.AllElements;

public class FluidsInit {

//    public static final DeferredRegister<Fluid> FLUIDS =
//            DeferredRegister.create(ForgeRegistries.FLUIDS, Kuayue.MODID);
//
//    public static final DeferredRegister<Block> BLOCKS =
//            DeferredRegister.create(ForgeRegistries.BLOCKS, Kuayue.MODID);
//
//    public static final DeferredRegister<Item> ITEMS =
//            DeferredRegister.create(ForgeRegistries.ITEMS, Kuayue.MODID);
//
//    public static final RegistryObject<ColaBlock> COLA_BLOCK = BLOCKS.register("cola_block",
//            () -> new ColaBlock(FluidsInit.STILL_COLA,
//                    BlockBehaviour.Properties.copy(Blocks.WATER)));
//
//    public static final RegistryObject<BlueBullBlock> BLUE_BULL_BLOCK = BLOCKS.register("blue_bull_block",
//            () -> new BlueBullBlock(FluidsInit.STILL_BLUE_BULL,
//                    BlockBehaviour.Properties.copy(Blocks.WATER)));
//
//    public static final RegistryObject<Item> COLA_BUCKET = ITEMS.register("cola_fluid_bucket",
//            () -> new BucketItem(
//                    FluidsInit.STILL_COLA,
//                    new Item.Properties()
//                            .stacksTo(1).tab(AllElements.neoKuayueDietTab.getTab())));
//
//    public static final RegistryObject<Item> BLUE_BULL_FLUID_BUCKET = ITEMS.register("blue_bull_fluid_bucket",
//            () -> new BucketItem(
//                    FluidsInit.STILL_BLUE_BULL,
//                    new Item.Properties()
//                            .stacksTo(1).tab(AllElements.neoKuayueDietTab.getTab())));
//
//    public static final RegistryObject<FlowingFluid> STILL_COLA = FLUIDS.register("cola_still_fluid",
//            () -> new ForgeFlowingFluid.Source(FluidsInit.COLA_FLUID_PROPERTIES));
//
//    public static final RegistryObject<FlowingFluid> FLOWING_COLA = FLUIDS.register("cola_flowing_fluid",
//            () -> new ForgeFlowingFluid.Flowing(FluidsInit.COLA_FLUID_PROPERTIES));
//
//    public static final RegistryObject<FlowingFluid> STILL_BLUE_BULL = FLUIDS.register("blue_bull_fluid",
//            () -> new ForgeFlowingFluid.Source(FluidsInit.BLUE_BULL_FLUID_PROPERTIES));
//
//    public static final RegistryObject<FlowingFluid> FLOWING_BLUE_BULL = FLUIDS.register("blue_bull_fluid_flow",
//            () -> new ForgeFlowingFluid.Flowing(FluidsInit.BLUE_BULL_FLUID_PROPERTIES));
//
//    public static final ForgeFlowingFluid.Properties COLA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
//            FluidTypesInit.COLA_FLUID_TYPE, STILL_COLA, FLOWING_COLA)
//            .slopeFindDistance(2).levelDecreasePerBlock(2)
//            .block(COLA_BLOCK).bucket(COLA_BUCKET);
//
//    public static final ForgeFlowingFluid.Properties BLUE_BULL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
//            FluidTypesInit.BLUE_BULL_FLUID_TYPE, STILL_BLUE_BULL, FLOWING_BLUE_BULL)
//            .slopeFindDistance(2).levelDecreasePerBlock(2)
//            .block(BLUE_BULL_BLOCK).bucket(BLUE_BULL_FLUID_BUCKET);
//
//    public static void register(IEventBus eventBus){
//        FLUIDS.register(eventBus);
//        BLOCKS.register(eventBus);
//        ITEMS.register(eventBus);
//    }
}
