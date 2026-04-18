package willow.train.kuayue.systems.editable_panel;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.block.panels.block_entity.EditablePanelEntity;
import willow.train.kuayue.initial.AllMenuScreens;

public class EditablePanelEditMenu extends AbstractContainerMenu {

    EditablePanelEntity editablePanelEntity;

    public EditablePanelEditMenu(@Nullable MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);

        this.addDataSlot(
                new DataSlot() {
                    @Override
                    public int get() {
                        return 0;
                    }

                    @Override
                    public void set(int pValue) {}
                });
    }

    public EditablePanelEditMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()),
                new SimpleContainerData(2));
    }

    public EditablePanelEditMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(AllMenuScreens.EDITABLE_PANEL.getMenuType(), containerId);
        this.editablePanelEntity = (EditablePanelEntity) entity;
    }

    public EditablePanelEditMenu setEditablePanelEntity(EditablePanelEntity editablePanelEntity) {
        this.editablePanelEntity = editablePanelEntity;
        return this;
    }

    public EditablePanelEntity getEditablePanelEntity() {
        return editablePanelEntity;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    public boolean updatePanelNbt(CompoundTag nbt, ServerPlayer player) {
        if (this.editablePanelEntity == null) {
            Kuayue.LOGGER.warn("EditablePanelEntity is null for player {}", player.getName().getString());
            return false;
        }

        if (!this.stillValid(player)) {
            Kuayue.LOGGER.warn("Player {} attempted to update panel NBT but menu is no longer valid",
                    player.getName().getString());
            return false;
        }

        BlockPos panelPos = this.editablePanelEntity.getBlockPos();
        double distanceSq = player.distanceToSqr(Vec3.atCenterOf(panelPos));
        if (distanceSq > 64.0) {
            Kuayue.LOGGER.warn("Player {} is too far from panel at {} (distance: {})",
                    player.getName().getString(), panelPos, Math.sqrt(distanceSq));
            return false;
        }

        ServerLevel playerLevel = player.getLevel();
        if (!playerLevel.equals(this.editablePanelEntity.getLevel())) {
            Kuayue.LOGGER.warn("Player {} and panel are in different dimensions",
                    player.getName().getString());
            return false;
        }

        if (!isValidEditablePanelNbt(nbt)) {
            Kuayue.LOGGER.warn("Invalid NBT data received from player {}",
                    player.getName().getString());
            return false;
        }

        if (!canPlayerEditPanel(player, panelPos, playerLevel)) {
            Kuayue.LOGGER.warn("Player {} does not have permission to edit panel at {}",
                    player.getName().getString(), panelPos);
            return false;
        }

        try {
            this.editablePanelEntity.load(nbt);
            this.editablePanelEntity.setChanged();
            playerLevel.getChunkSource().blockChanged(panelPos);

            Kuayue.LOGGER.debug("Successfully updated panel NBT at {} for player {}",
                    panelPos, player.getName().getString());
            return true;

        } catch (Exception e) {
            Kuayue.LOGGER.error("Error updating panel NBT for player {}: {}",
                    player.getName().getString(), e.getMessage());
            return false;
        }
    }

    private boolean isValidEditablePanelNbt(CompoundTag nbt) {
        if (!nbt.contains("data", CompoundTag.TAG_COMPOUND)) {
            return false;
        }

        if (this.editablePanelEntity == null) {
            return false;
        }

        CompoundTag dataTag = nbt.getCompound("data");
        TrainPanelProperties.EditType editType = this.editablePanelEntity.getEditType();
        SignType signType = EditableTypeConstants.getSignTypeByEditType(editType);
        if (signType == null) {
            Kuayue.LOGGER.warn("No SignType validator found for edit type: {}", editType);
            return false;
        }

        boolean valid = signType.validateNbt(dataTag);
        if (!valid) {
            Kuayue.LOGGER.warn("Invalid data payload for edit type {}", editType);
        }
        return valid;
    }

    private boolean canPlayerEditPanel(ServerPlayer player, BlockPos pos, ServerLevel level) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof EditablePanelEntity)) {
            return false;
        }
        //可选添加其他权限检查
        return true;
    }
}
