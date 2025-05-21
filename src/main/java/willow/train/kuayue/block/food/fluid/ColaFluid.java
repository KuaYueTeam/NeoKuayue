package willow.train.kuayue.block.food.fluid;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class ColaFluid extends ForgeFlowingFluid {

    public ColaFluid(Properties properties) {
        super(properties);
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> pBuilder) {
        super.createFluidStateDefinition(pBuilder.add(LEVEL));
    }

    @Override
    public boolean isSource(FluidState pState) {
        return (pState.getType() instanceof ColaFluid) &&
                !(pState.getType() instanceof Flowing);
    }

    @Override
    public int getAmount(FluidState pState) {
        return isSource(pState) ? 8 : (Integer) pState.getValue(LEVEL);
    }

    public static class Flowing extends ColaFluid {

        public Flowing(Properties properties) {
            super(properties);
        }
    }
}
