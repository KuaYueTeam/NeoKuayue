package willow.train.kuayue.systems.overhead_line.item;

import com.mojang.datafixers.util.Either;
import com.simibubi.create.content.trains.track.TrackTargetingBlockItem;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistries;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlockEntity;
import willow.train.kuayue.utils.client.ComponentTranslationTool;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

public class OverheadLineItem extends Item {
    public OverheadLineItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pPlayer.level.isClientSide()) {
            if (pPlayer.isShiftKeyDown()) {
                ComponentTranslationTool.showWarning(pPlayer, "overhead_line_target_remove", true);
                return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
            }
        }else {
            // @TODO: play sound
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(pContext.getPlayer() == null){
            return InteractionResult.PASS;
        }
        if(pContext.getLevel().isClientSide()){
            return InteractionResult.SUCCESS;
        }
        Player player = pContext.getPlayer();
        ItemStack item = pContext.getItemInHand();
        CompoundTag tags = item.getTag();
        if(player.isShiftKeyDown()) {
            if(tags != null && tags.contains("target")) {
                tags.remove("target");
                ComponentTranslationTool.showWarning(player, "overhead_line_target_remove", true);
            }
        } else {
            if(tags != null && tags.contains("target", Tag.TAG_COMPOUND)) {
                CompoundTag target = tags.getCompound("target");
                int previousIndex = tags.getInt("index");
                if(this.connectStacks(pContext,player ,pContext.getClickedPos(), target, previousIndex)) {
                    ComponentTranslationTool.showSuccess(player, "overhead_line_target_connected", true);
                    tags.remove("target");
                }
            } else {
                this.addTargetIntoStack(pContext, player, pContext.getClickedPos(), item);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void addTargetIntoStack(UseOnContext pContext, Player player, BlockPos clickedPos, ItemStack item) {
        Level level = pContext.getLevel();
        BlockEntity clickedBlockEntity = level.getBlockEntity(clickedPos);
        if(!(clickedBlockEntity instanceof OverheadLineSupportBlockEntity overheadLineSupportBlockEntity)){
            return;
        }

        if(overheadLineSupportBlockEntity.getConnectionPoints().isEmpty()){
            ComponentTranslationTool.showError(player, "overhead_line_target_unavailable", true);
            return;
        }

        CompoundTag tag = item.getOrCreateTag();

        tag.put("target", NbtUtils.writeBlockPos(clickedPos));
        tag.putInt("index", overheadLineSupportBlockEntity.getConnectionIndexOf(player.getEyePosition()));

        ComponentTranslationTool.showSuccess(player, "overhead_line_target_set", true);
    }

    private boolean connectStacks(UseOnContext pContext, Player player, BlockPos clickedPos, CompoundTag target, int previousIndex){
        Level level = pContext.getLevel();

        BlockPos targetPos = NbtUtils.readBlockPos(target);

        if(Objects.equals(clickedPos, targetPos)) {
            ComponentTranslationTool.showError(player, "overhead_line_same_support", true);
            return false;
        }

        BlockEntity clickedBlockEntity = level.getBlockEntity(clickedPos);
        BlockEntity targetBlockEntity = level.getBlockEntity(targetPos);

        if(
                !(clickedBlockEntity instanceof OverheadLineSupportBlockEntity clickedSupport) ||
                !(targetBlockEntity instanceof OverheadLineSupportBlockEntity targetSupport)
        ){
            ComponentTranslationTool.showError(player, "overhead_line_incompatible_block", true);
            return false;
        }
        Optional<String> canConnectFromClicked = clickedSupport.checkConnectable(targetSupport);
        Optional<String> canConnectFromSupport = targetSupport.checkConnectable(clickedSupport);

        if(canConnectFromClicked.isPresent() || canConnectFromSupport.isPresent()) {
            Component errorDescription;
            if(
                    canConnectFromClicked.isPresent() && canConnectFromSupport.isPresent() &&
                    canConnectFromSupport.get().equals(canConnectFromClicked.get())
            ) {
                errorDescription = ComponentTranslationTool.translatable(
                        canConnectFromSupport.get(),
                        ComponentTranslationTool.translatable("overhead_line_point_both")
                );
            } else {
                errorDescription = canConnectFromSupport.map(string -> ComponentTranslationTool.translatable(
                        string,
                        ComponentTranslationTool.translatable("overhead_line_point_first")
                )).orElseGet(() -> ComponentTranslationTool.translatable(
                        canConnectFromClicked.get(),
                        ComponentTranslationTool.translatable("overhead_line_point_last")
                ));
            }
            ComponentTranslationTool.showError(player, true, errorDescription);
            return false;
        }

        ResourceLocation itemType = ForgeRegistries.ITEMS.getKey(this);

        int index = clickedSupport.getConnectionIndexOf(player.getEyePosition());


        if(clickedSupport.getConnectionPoints().isEmpty()){
            ComponentTranslationTool.showError(player, "overhead_line_target_unavailable", true);
            return false;
        }

        if(clickedSupport.hasConnectionTo(targetSupport) || targetSupport.hasConnectionTo(clickedSupport)) {
            ComponentTranslationTool.showError(player, "overhead_line_target_duplicated", true);
            return false;
        }

        clickedSupport.addConnection(targetPos, itemType, index, previousIndex, targetSupport);
        targetSupport.addConnection(clickedPos, itemType, previousIndex, index, clickedSupport);
        return true;
    }
}
