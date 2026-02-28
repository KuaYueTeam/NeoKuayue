package willow.train.kuayue.item.animate_controller;

import com.google.common.collect.Lists;
import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.utils.client.ComponentTranslationTool;

import java.util.List;
import java.util.UUID;

public class AnimatableBlockControllerItem extends Item implements MenuProvider {

    public AnimatableBlockControllerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        CompoundTag tag = stack.getOrCreateTagElement("control_elements");
        // InteractionProcressor.toFullTag(tag);
        stack.getOrCreateTagElement("recent").putInt("number", 0);
        stack.getOrCreateTagElement("recent").putInt("max", getMaxNumbers(stack));
        return stack;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level pLevel, Player pPlayer) {
        super.onCraftedBy(stack, pLevel, pPlayer);
        CompoundTag tag = stack.getOrCreateTagElement("control_elements");
        // InteractionProcressor.toFullTag(tag);
        stack.getOrCreateTagElement("recent").putInt("number", 0);
        stack.getOrCreateTagElement("recent").putInt("max", getMaxNumbers(stack));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag pIsAdvanced) {
        super.appendHoverText(stack, level, components, pIsAdvanced);
        if(Screen.hasShiftDown()) {
//            components.add(Component.translatable("tooltip.kuayue.abc.binding.explain.bind_mode.title").withStyle(ChatFormatting.GOLD));
//            components.add(Component.translatable("tooltip.kuayue.abc.binding.explain.bind_mode.text_1").
//                    append(Component.literal(String.valueOf(KYConfig.getCachedMaxAbcControlDistance())).withStyle(ChatFormatting.GREEN)).
//                    append(Component.translatable("tooltip.kuayue.abc.binding.explain.bind_mode.text_2")));
//            components.add(Component.translatable("tooltip.kuayue.abc.binding.explain.use_mode.title").withStyle(ChatFormatting.GOLD));
//            components.add(Component.translatable("tooltip.kuayue.abc.binding.explain.use_mode.text"));
//            components.add(Component.translatable("tooltip.kuayue.abc.binding.explain.caution").withStyle(ChatFormatting.RED));
        } else {
            components.add(ComponentTranslationTool.HELP_WITH_LSHIFT_COMPONENT);
            CompoundTag tag = stack.getOrCreateTagElement("control_elements");
            int controllingNumber = stack.getOrCreateTagElement("recent").getInt("number");

            components.add(Component.translatable("tooltip.kuayue.abc.binding.state")
                    .append(hasId(stack) ? Component.translatable("tooltip.kuayue.abc.binding.tr").withStyle(ChatFormatting.GREEN) :
                            Component.translatable("tooltip.kuayue.abc.binding.un").withStyle(ChatFormatting.RED)));
            int counter = 0;
            components.add(Component.translatable("tooltip.kuayue.abc.title"));
//            for(String s : tag.getAllKeys()) {
//                ICustomInteractionGroup group = InteractionProcressor.INTERACTIONS.get(s);
//                components.add((Component.literal("-> ").append(group.descriptionId().copy())).withStyle(ChatFormatting.GRAY));
//                for(String key : group.interactions().keySet()) {
//                    components.add((Component.literal("--> ").append(group.getInteraction(key).descriptionKey().copy())).withStyle(counter == controllingNumber ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY));
//                    counter++;
//                }
//            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pPlayer.getItemInHand(pUsedHand).getItem() instanceof AnimatableBlockControllerItem) {
            ItemStack stack = pPlayer.getItemInHand(pUsedHand);
            if (pPlayer.isShiftKeyDown()) {
                /*
                int recent = pPlayer.getItemInHand(pUsedHand).getOrCreateTagElement("recent").getInt("number");
                int max = pPlayer.getItemInHand(pUsedHand).getOrCreateTagElement("recent").getInt("max");

                if (recent >= max - 1) recent = 0;
                else recent++;
                stack.getOrCreateTagElement("recent").putInt("number", recent);
                String[] groupAndKey = getControllingGroupAndKey(pPlayer.getItemInHand(pUsedHand));

                if (groupAndKey.length != 2) return super.use(pLevel, pPlayer, pUsedHand);

                ComponentTranslationTool.showWarning(pPlayer, true,
                        Component.translatable("tooltip.kuayue.abc.controlling"),
                        Component.translatable("tooltip.kuayue.abc." + groupAndKey[0] + "." + groupAndKey[1]));

                 */
                if(!pLevel.isClientSide)
                    NetworkHooks.openScreen((ServerPlayer) pPlayer, this);
            } else {
                if(hasId(stack)) {
                    boolean successed = onUseItem(pLevel, pPlayer, pUsedHand, stack);
                    if(successed)
                        ComponentTranslationTool.showSuccess(pPlayer, true, Component.translatable("tooltip.kuayue.abc.binding.use.success"));
                    else
                        ComponentTranslationTool.showError(pPlayer, true, Component.translatable("tooltip.kuayue.abc.binding.use.fail"));
                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static String[] getControllingGroupAndKey(ItemStack stack) {
        if(!(stack.getItem() instanceof AnimatableBlockControllerItem)) return new String[]{};
        int recentNumber = stack.getOrCreateTagElement("recent").getInt("number");
        int counter = 0;
        for(String s : stack.getOrCreateTagElement("control_elements").getAllKeys()) {
            String[] values= stack.getOrCreateTagElement("control_elements").getString(s).split(",");
            for(String value : values) {
                if(counter == recentNumber)
                    return new String[]{s, value.replaceAll(" ", "")};
                counter++;
            }
        }
        return new String[]{};
    }

    public static String getControllingKey(ItemStack stack){
        String[] groupAndKey = getControllingGroupAndKey(stack);
        if(groupAndKey.length != 2) return "";
        return groupAndKey[1];
    }

    public static boolean hasId(ItemStack stack) {
        return stack.getOrCreateTagElement("coupling").contains("contents");
    }

    public static boolean isCouplingValid(Level level, BlockPos playerPos, ItemStack stack) {
        return stack.getOrCreateTagElement("coupling").getCompound("contents").contains("basic");
    }

    public static boolean onUseItem(Level level, Player player, InteractionHand hand, ItemStack stack) {
        BlockPos playerPos = player.getOnPos();
        if(!isCouplingValid(level, playerPos, stack)) return false;
        CompoundTag nbt = stack.getOrCreateTagElement("coupling").getCompound("contents");
        CompoundTag basicTag = nbt.getCompound("basic");
        boolean isTrain = basicTag.getBoolean("train");
        UUID entityId = basicTag.getUUID("id");
        List<Entity> entities = getEntities(level, player.getPosition(0), 64,
                AllEntityTypes.CARRIAGE_CONTRAPTION.get(),
                AllEntityTypes.CONTROLLED_CONTRAPTION.get(),
                AllEntityTypes.GANTRY_CONTRAPTION.get(),
                AllEntityTypes.ORIENTED_CONTRAPTION.get()
        );
        String[] groupAndKey = AnimatableBlockControllerItem.getControllingGroupAndKey(stack);
//        ICustomInteraction interaction = InteractionProcressor.getInteraction(groupAndKey[0], groupAndKey[1]);
//        if(interaction == null) return false;
        for(String key : nbt.getAllKeys()) {
            if(key.equals("basic")) continue;
            CompoundTag subTag = nbt.getCompound(key);
            BlockPos localPos = new BlockPos(subTag.getInt("px"), subTag.getInt("py"), subTag.getInt("pz"));
            if(isTrain) {
                entityId = subTag.getUUID("entity");
                UUID trainId = basicTag.getUUID("uuid");
                Entity entity = null;
                for(Entity e : entities) {
                    if(e.getUUID().equals(entityId) &&
                            e instanceof CarriageContraptionEntity cce &&
                            cce.trainId.equals(trainId)) {
                        entity = e;
                        break;
                    }
                }
                if(!(entity instanceof CarriageContraptionEntity)) continue;
                if(!((CarriageContraptionEntity) entity).trainId.equals(trainId)) continue;
//                onContraptionLocating((AbstractContraptionEntity) entity, localPos, player, hand, interaction);
            } else {
                Entity entity = null;
                for(Entity e : entities) {
                    if(e.getUUID().equals(entityId)) {
                        entity = e;
                        break;
                    }
                }
                if(!(entity instanceof AbstractContraptionEntity)) continue;
                if(!entity.getUUID().equals(entityId)) continue;
//                onContraptionLocating((AbstractContraptionEntity) entity, localPos, player, hand, interaction);
            }
        }
        return true;
    }

    private static boolean onContraptionLocating(AbstractContraptionEntity entity, BlockPos localPos, Player player, InteractionHand hand) {// , ICustomInteraction interaction) {
        Contraption contraption = entity.getContraption();
        StructureTemplate.StructureBlockInfo info = contraption.getBlocks().get(localPos);
        BlockEntity be = contraption.getBlockEntityClientSide(localPos);
//        if(interaction.canActive(entity.level, player, hand, entity, localPos, info.state, be)) {
//            entity.handlePlayerInteraction(player, localPos, Direction.DOWN, hand);
//            return true;
//        }
        return false;
    }

    public static List<Entity> getEntities(Level level, Vec3 centrePos, int radius, EntityType<?>... types) {
        List<Entity> result = Lists.newArrayList();
        for(EntityType<?> type : types) {
            result.addAll(
                    level.getEntities(type.create(level), AABB.ofSize(centrePos,2 * radius,2 * radius,2 * radius))
            );
        }
        return result;
    }

    public static void loadCoupling(ItemStack controller, ItemStack coupling) {
        if(coupling.is(Items.AIR)) {
            controller.removeTagKey("coupling");
        }
//        if(!coupling.is(ResourceItemInit.AutomaticControlCoupling.get())) return;
        CompoundTag tag = coupling.getOrCreateTagElement("contents");
        if(tag.isEmpty()) return;
        controller.getOrCreateTagElement("coupling").put("contents", tag);
    }

    public static ItemStack generateCoupling(ItemStack controller) {
        CompoundTag tag = controller.getOrCreateTagElement("coupling");
        if(tag.isEmpty()) return Items.AIR.getDefaultInstance();
//        ItemStack coupling = ResourceItemInit.AutomaticControlCoupling.get().getDefaultInstance();
//        CompoundTag nbt = coupling.getOrCreateTag();
//        nbt.put("contents", tag);
//        return coupling;
        return ItemStack.EMPTY;
    }

    public static ItemStack generateCoupling(CompoundTag tag) {
        CompoundTag t = tag.getCompound("coupling").getCompound("contents");
        if(t.isEmpty()) return Items.AIR.getDefaultInstance();
//        ItemStack coupling = ResourceItemInit.AutomaticControlCoupling.get().getDefaultInstance();
//        CompoundTag nbt = coupling.getOrCreateTag();
//        nbt.put("contents", t);
//        return coupling;
        return ItemStack.EMPTY;
    }

    public static void setNumber(ItemStack stack, int number) {
        if(!(stack.getItem() instanceof AnimatableBlockControllerItem)) return;
        stack.getOrCreateTagElement("recent").putInt("number", number);
    }

    private int getMaxNumbers(ItemStack stack) {
        int result = 0;
        for(String s : stack.getOrCreateTagElement("control_elements").getAllKeys()) {
            String value = stack.getOrCreateTagElement("control_elements").getString(s);
            result += value.split(",").length;
        }
        return result;
    }

    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
//        return new AnimatableBlockControllerMenu(pContainerId, pPlayerInventory, ByteBufUtil.defaultBuf());
        return null;
    }
}
