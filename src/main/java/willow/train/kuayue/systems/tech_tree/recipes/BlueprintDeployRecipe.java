package willow.train.kuayue.systems.tech_tree.recipes;

import com.google.gson.JsonObject;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.systems.tech_tree.NodeLocation;

public class BlueprintDeployRecipe extends DeployerApplicationRecipe {

    private NodeLocation node;

    public BlueprintDeployRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(params);
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level p_77569_2_) {
        return super.matches(inv, p_77569_2_) && heldItemHasNode(inv.getItem(1));
    }


    private boolean heldItemHasNode(ItemStack stack) {
        if (!stack.hasTag()) return false;
        CompoundTag nbt = stack.getTag();
        String nodeStr = nbt.getString("node");
        NodeLocation loc = new NodeLocation(nodeStr);
        return loc.equals(node);
    }

    @Override
    public ItemStack assemble(RecipeWrapper inv) {
        return super.assemble(inv);
    }

    @Override
    public boolean shouldKeepHeldItem() {
        return true;
    }

    @Override
    public void readAdditional(JsonObject json) {
        super.readAdditional(json);
        this.node = new NodeLocation(json.get("node").getAsString());
    }

    @Override
    public void writeAdditional(JsonObject json) {
        super.writeAdditional(json);
        json.addProperty("node", this.node.toString());
    }

    @Override
    public void readAdditional(FriendlyByteBuf buffer) {
        super.readAdditional(buffer);
        this.node = NodeLocation.readFromByteBuf(buffer);
    }

    @Override
    public void writeAdditional(FriendlyByteBuf buffer) {
        super.writeAdditional(buffer);
        node.writeToByteBuf(buffer);
    }
}
