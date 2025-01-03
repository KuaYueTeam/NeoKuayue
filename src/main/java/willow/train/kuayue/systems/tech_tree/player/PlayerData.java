package willow.train.kuayue.systems.tech_tree.player;

import kasuga.lib.core.base.NbtSerializable;
import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.openjdk.nashorn.internal.objects.annotations.Getter;
import willow.train.kuayue.mixins.mixin.AccessorPlayerAdvancement;
import willow.train.kuayue.systems.tech_tree.NodeLocation;
import willow.train.kuayue.systems.tech_tree.json.HideContext;
import willow.train.kuayue.systems.tech_tree.json.OnUnlockContext;
import willow.train.kuayue.systems.tech_tree.server.TechTree;
import willow.train.kuayue.systems.tech_tree.server.TechTreeGroup;
import willow.train.kuayue.systems.tech_tree.server.TechTreeManager;
import willow.train.kuayue.systems.tech_tree.server.TechTreeNode;

import javax.annotation.Nullable;
import java.util.*;

public class PlayerData implements NbtSerializable {
    public final UUID playerID;
    public final Set<NodeLocation> unlocked;
    public final Set<ResourceLocation> unlockedGroups;
    public final Set<ResourceLocation> visibleGroups;
    public final Set<NodeLocation> visibleNodes;

    public PlayerData(UUID uuid) {
        this.playerID = uuid;
        unlocked = new HashSet<>();
        visibleGroups = new HashSet<>();
        unlockedGroups = new HashSet<>();
        visibleNodes = new HashSet<>();
    }

    public PlayerData(Player player) {
        this(player.getUUID());
    }

    public void read(CompoundTag nbt) {
        ListTag nodes = nbt.getList("nodes", Tag.TAG_STRING);
        unlocked.clear();
        nodes.forEach(tag -> unlocked.add(new NodeLocation(tag.getAsString())));

        ListTag uGrp = nbt.getList("unlock_groups", Tag.TAG_STRING);
        unlockedGroups.clear();
        uGrp.forEach(tag -> unlockedGroups.add(new ResourceLocation(tag.getAsString())));

        ListTag vGrp = nbt.getList("visible_groups", Tag.TAG_STRING);
        visibleGroups.clear();
        vGrp.forEach(tag -> visibleGroups.add(new ResourceLocation(tag.getAsString())));

        ListTag vNode = nbt.getList("visible_nodes", Tag.TAG_STRING);
        visibleNodes.clear();;
        vNode.forEach(node -> visibleNodes.add(new NodeLocation(node.getAsString())));
    }

    public void write(CompoundTag nbt) {
        ListTag nodes = new ListTag();
        unlocked.forEach(loc -> nodes.add(StringTag.valueOf(loc.toString())));
        nbt.put("nodes", nodes);

        ListTag uGrp = new ListTag();
        unlockedGroups.forEach(grp -> uGrp.add(StringTag.valueOf(grp.toString())));
        nbt.put("unlock_groups", uGrp);

        ListTag vGrp = new ListTag();
        visibleGroups.forEach(grp -> vGrp.add(StringTag.valueOf(grp.toString())));
        nbt.put("visible_groups", vGrp);

        ListTag vNode = new ListTag();
        visibleNodes.forEach(node -> vNode.add(StringTag.valueOf(node.toString())));
        nbt.put("visible_nodes", vNode);
    }

    public boolean canUnlock(ServerPlayer player, TechTreeGroup group) {
        if (!canBeSeen(player, group.getHideContext()) &&
                !visibleGroups.contains(group.getId())) return false;
        for (TechTreeNode node : group.getPrev()) {
            if (!unlocked.contains(node.getLocation())) return false;
        }
        return true;
    }

    public boolean canUnlock(ServerPlayer player, TechTreeNode node) {
        if (!canBeSeen(player, node.getHideContext()) &&
                !visibleNodes.contains(node.getLocation())) return false;
        for (TechTreeNode n : node.getPrev()) {
            if (!unlocked.contains(n.getLocation())) return false;
        }
        return true;
    }

    public boolean unlock(ServerLevel level, ServerPlayer player, TechTreeNode node) {
        if (!(canUnlock(player, node) &&
                checkItems(player.getInventory(), node.getItemConsume()) &&
                checkExpAndLevel(player, node.getExpAndLevel()))) return false;
        consumeExpAndLevel(player, node.getExpAndLevel());
        consumePlayerItem(player, level, node.getItemConsume());
        forceUnlock(level, player, node);
        return true;
    }

    public void forceUnlock(ServerLevel level, ServerPlayer player, TechTreeNode node) {
        unlocked.add(node.getLocation());
        rewardPlayer(level, player, node.getUnlockContext(), node.getGroup().getTree());
        givePlayerItem(player, level, node.getBlueprints());
        givePlayerItem(player, level, node.getItemReward());
        checkVisible(node.group.getTree(), player);
    }

    public boolean unlock(ServerLevel level, ServerPlayer player, TechTreeGroup group) {
        if (!canUnlock(player, group)) return false;
        forceUnlock(level, player, group);
        return true;
    }

    public void forceUnlock(ServerLevel level, ServerPlayer player, TechTreeGroup group) {
        unlockedGroups.add(group.getId());
        rewardPlayer(level, player, group.getUnlockContext(), group.tree);
    }

    public boolean checkExpAndLevel(ServerPlayer player, Pair<Integer, Integer> expAndLevel) {
        int level = player.experienceLevel;
        int exp = player.totalExperience;
        return level >= expAndLevel.getSecond() && exp >= expAndLevel.getFirst();
    }

    public static void consumeExpAndLevel(ServerPlayer player, Pair<Integer, Integer> expAndLevel) {
        int expConsume = expAndLevel.getFirst();
        int level;
        int exp = player.totalExperience - expConsume;
        if (exp < 0) {
            level = 0;
            exp = 0;
        } else {
            level = expToLevel(exp);
        }
        int levelChange = level - player.experienceLevel;

        // from Player#giveExperienceLevels(int pLevels)
        net.minecraftforge.event.entity.player.PlayerXpEvent.LevelChange event = new net.minecraftforge.event.entity.player.PlayerXpEvent.LevelChange(player, levelChange);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);

        player.experienceLevel = level;
        player.totalExperience = exp;
    }

    public void rewardPlayer(ServerLevel level, ServerPlayer player, @Nullable OnUnlockContext context, TechTree tree) {
        if (context == null) return;

        // items
        Set<ItemStack> reward = context.getReward();
        givePlayerItem(player, level, reward);

        //collect advancements
        Set<Advancement> rewardAdvancement = new HashSet<>();
        ResourceLocation[] advancements = context.getUnlockAdvancements();
        for (ResourceLocation location : advancements) {
            for (Advancement advancement : PlayerDataManager.MANAGER.getAdvancements()) {
                if (advancement.getId().equals(location)) {
                    rewardAdvancement.add(advancement);
                    break;
                }
            }
        }

        // give all advancements
        rewardAdvancement.forEach(adv -> {
            adv.getCriteria().forEach((s, criterion) ->
                    player.getAdvancements().award(adv, s)
            );
        });

        // unlock nodes;
        for (NodeLocation location : context.getUnlockNodes()) {
            TechTreeNode node = tree.getNodes().getOrDefault(location, null);
            if (node == null) continue;
            forceUnlock(level, player, node);
        }
        unlocked.addAll(Arrays.asList(context.getUnlockNodes()));
    }

    public static void givePlayerItem(Player player, ServerLevel level, Set<ItemStack> items) {
        items.forEach(item -> {
            if (player.addItem(item)) return;
            Vec3 eyePos = player.getEyePosition();
            // drop item;
            level.addFreshEntity(new ItemEntity(level, eyePos.x(), eyePos.y(), eyePos.z(), item));
        });
    }

    public static boolean consumePlayerItem(Player player, ServerLevel level, Set<ItemStack> items) {
        Inventory inventory = player.getInventory();
        HashSet<ItemStack> result = new HashSet<>();
        items.forEach(item -> {
            Item type = item.getItem();
            boolean flag = item.hasTag();

            // items that has nbt
            if (flag) {
                for (int i = 0; i < 36; i++) {
                    ItemStack stack = inventory.getItem(i);
                    if (stack.getItem().equals(type) &&
                        stack.hasTag() && stack.getTag().equals(item.getTag())
                    ) {
                        inventory.setItem(i, ItemStack.EMPTY);
                        result.add(item);
                        return;
                    }
                }
            }

            // items that without nbt
            int count = item.getCount();
            for (int i = 0; i < 36; i++) {
                ItemStack stack = inventory.getItem(i);
                if (!stack.getItem().equals(type)) continue;
                if (stack.getCount() > count) {
                    stack.setCount(stack.getCount() - count);
                    result.add(item);
                    return;
                } else if (stack.getCount() == count) {
                    inventory.setItem(i, ItemStack.EMPTY);
                    result.add(item);
                    return;
                } else {
                    inventory.setItem(i, ItemStack.EMPTY);
                    count -= stack.getCount();
                }
            }
        });

        return result.size() == items.size();
    }

    public boolean checkItems(Inventory inventory, Collection<ItemStack> items) {
        Collection<ItemStack> collection = new HashSet<>(items);
        collection.removeIf(stack -> {
            int count = stack.getCount();
            for (int i = 0; i < 36; i++) {
                ItemStack item = inventory.getItem(i);
                if (!item.getItem().equals(stack.getItem())) continue;
                if (count <= 0) return true;
                if (stack.hasTag() && item.hasTag()) {
                    if (stack.getTag().equals(item.getTag())) count --;
                    continue;
                }
                if (stack.hasTag() || item.hasTag()) continue;
                count -= item.getCount();
            }
            return count <= 0;
        });
        return collection.isEmpty();
    }

    public boolean canBeSeen(ServerPlayer player, @Nullable HideContext hide) {
        if (hide == null) return true;
        // check namespaces;
        HashSet<String> needNamespaces = new HashSet<>(Arrays.asList(hide.getNeedNamespaces()));
        TechTreeManager.MANAGER.getNamespaces().forEach(needNamespaces::remove);
        if (!needNamespaces.isEmpty()) return false;

        // check advancements
        PlayerAdvancements adv = player.getAdvancements();
        Map<Advancement, AdvancementProgress> allAdvancements = ((AccessorPlayerAdvancement) adv).getAdvancements();
        Set<ResourceLocation> needAdvancements = new HashSet<>(Arrays.asList(hide.getNeedAdvancements()));
        allAdvancements.forEach((a, ap) -> {
            if (needAdvancements.contains(a.getId()) && ap.isDone()) needAdvancements.remove(a.getId());
        });
        if (!needAdvancements.isEmpty()) return false;

        // check nodes;
        HashSet<NodeLocation> needNodes = new HashSet<>(Arrays.asList(hide.getNeedNodes()));
        unlocked.forEach(needNodes::remove);
        return needNodes.isEmpty();
    }

    public static int levelToExp(int level) {
        if (level < 17) {
            return level * level + 6 * level;
        } else if (level < 32) {
            return (int) (level * level * 2.5f - 40.5f * level + 360);
        } else {
            return (int) (level * level * 4.5f - 162.5 * level + 2220);
        }
    }

    public static int expToLevel(int exp) {
        int level17 = levelToExp(17);
        int level32 = levelToExp(32);
        if (exp >= level32) {
            return (int) quadraticYtoX(4.5, -162.5, 2220, exp, true);
        }
        if (exp >= level17) {
            return (int) quadraticYtoX(2.5, -40.5, 360, exp, true);
        }
        return (int) quadraticYtoX(1, 6, 0, exp, true);
    }

    public static Pair<Double, Double> quadraticYtoX(double A, double B, double C, double y) {
        double q = B * B - 4 * A * (C - y);
        assert q >= 0;
        return Pair.of((-B - Math.sqrt(q)) / (2 * A), (-B + Math.sqrt(q)) / (2 * A));
    }


    public static double quadraticYtoX(double A, double B, double C, double y, boolean right) {
        Pair<Double, Double> pair = quadraticYtoX(A, B, C, y);
        return right ? pair.getSecond() : pair.getFirst();
    }

    public void checkVisible(TechTree tree, ServerPlayer player) {
        tree.getGroups().forEach(
                (location, grp) -> {
                    if (!grp.isHide()) return;
                    if (!canBeSeen(player, grp.getHideContext())) return;
                    this.visibleGroups.add(grp.getId());
                }
        );
        tree.getNodes().forEach(
                (location, node) -> {
                    if (!node.isHide()) return;
                    if (!canBeSeen(player, node.getHideContext())) return;
                    this.visibleNodes.add(node.getLocation());
                }
        );
    }


    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeUUID(playerID);

        buf.writeInt(unlocked.size());
        unlocked.forEach(node -> {
            node.writeToByteBuf(buf);
        });

        buf.writeInt(unlockedGroups.size());
        unlockedGroups.forEach(buf::writeResourceLocation);

        buf.writeInt(visibleGroups.size());
        visibleGroups.forEach(buf::writeResourceLocation);

        buf.writeInt(visibleNodes.size());
        visibleNodes.forEach(node -> node.writeToByteBuf(buf));
    }


    public void fromNetwork(FriendlyByteBuf buf) {
        int unlockSize = buf.readInt();
        for (int i = 0; i < unlockSize; i++) {
            this.unlocked.add(NodeLocation.readFromByteBuf(buf));
        }

        int unlockGroupSize = buf.readInt();
        for (int i = 0; i < unlockGroupSize; i++) {
            this.unlockedGroups.add(buf.readResourceLocation());
        }

        int visibleGroupSize = buf.readInt();
        for (int i = 0; i < visibleGroupSize; i++) {
            this.visibleGroups.add(buf.readResourceLocation());
        }

        int visibleNodeSize = buf.readInt();
        for (int i = 0; i < visibleNodeSize; i++) {
            this.visibleNodes.add(NodeLocation.readFromByteBuf(buf));
        }
    }
}
