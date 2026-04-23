package willow.train.kuayue.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.railwayteam.railways.content.fuel.psi.PortableFuelInterfaceBlock;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorage;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorage;
import com.simibubi.create.content.contraptions.*;
import com.simibubi.create.content.contraptions.actors.psi.PortableFluidInterfaceBlockEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.minecart.TrainCargoManager;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.content.trains.entity.*;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.math.BlockFace;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.MutablePair;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.mixins.mixin.*;

import java.util.*;

public class CarriageUtil {

    private static class RemapContext {
        final Direction assemblyDirection;
        final int bogeySpacing;
        final StructureTransform transform;
        final Map<BlockPos, BlockPos> itemVaultControllerTransform;
        final Map<BlockPos, BlockPos> fluidTankControllerTransform;
        final boolean isClientSide;

        RemapContext(Direction assemblyDirection, int bogeySpacing, boolean isClientSide) {
            this.assemblyDirection = assemblyDirection;
            this.bogeySpacing = bogeySpacing;
            this.transform = new StructureTransform(
                    BlockPos.ZERO.relative(assemblyDirection, bogeySpacing),
                    Direction.Axis.Y, Rotation.CLOCKWISE_180, Mirror.NONE
            );
            this.itemVaultControllerTransform = new HashMap<>();
            this.fluidTankControllerTransform = new HashMap<>();
            this.isClientSide = isClientSide;
        }

        BlockPos apply(BlockPos pos) {
            return transform.apply(pos);
        }
    }

    private static class RemapResult {
        HashMap<BlockPos, StructureTemplate.StructureBlockInfo> blocks;
        List<MutablePair<StructureTemplate.StructureBlockInfo, MovementContext>> actors;
        Map<BlockPos, MovingInteractionBehaviour> interactors;
        List<AABB> superglues;
        List<BlockPos> seats;
        Map<UUID, BlockFace> stabilizedSubContraptions;
        Multimap<BlockPos, StructureTemplate.StructureBlockInfo> capturedMultiblocks;
        Map<BlockPos, Entity> initialPassengers;

        Map<BlockPos, MountedItemStorage> storageItems;
        Map<BlockPos, MountedFluidStorage> storageFluids;
    }

    public static Vec3 getCarriageDirection(Carriage carriage) {
        CarriageBogey leadingBogey = carriage.leadingBogey();
        if (leadingBogey.leading().edge == null) return Vec3.ZERO;
        if (carriage.isOnTwoBogeys()) {
            CarriageBogey trailingBogey = carriage.trailingBogey();
            if (trailingBogey.leading().edge == null) return Vec3.ZERO;
            return leadingBogey.getAnchorPosition().subtract(trailingBogey.getAnchorPosition()).normalize();
        }
        Train train = carriage.train;
        Vec3 leading = leadingBogey.leading().getPosition(train.graph, leadingBogey.isUpsideDown());
        return leading.subtract(leadingBogey.getAnchorPosition()).normalize();
    }

    //对反转转向架的车厢重新映射车厢Contraption
    public static boolean remapCarriage(Carriage carriage, boolean isClientSide) {
        if(carriage == null) return false;
        CarriageContraptionEntity cce = carriage.anyAvailableEntity();
        if (cce == null) return false;
        Contraption contraption = cce.getContraption();
        if(!(contraption instanceof CarriageContraption cc)) return false;

        try {
            RemapContext context = new RemapContext(
                    cc.getAssemblyDirection(),
                    carriage.bogeySpacing,
                    isClientSide
            );

            calculateItemVaultControllerMap(cc, context);
            calculateFluidTankControllerMap(cc, context);

            RemapResult result = calculateRemapState(cc, carriage, context);

            commitRemapState(cc, carriage, result);

            postProcess(cc, cce, carriage, context);

            return true;
        } catch (Exception e) {
            Kuayue.LOGGER.error("Failed to remap carriage", e);
            e.printStackTrace();
            return false;
        }
    }

    private static void calculateItemVaultControllerMap(CarriageContraption cc, RemapContext context) {
        cc.getBlocks().forEach((k,v) -> {
            if (v.nbt() != null && v.nbt().contains("Controller")) {
                CompoundTag tag = v.nbt().getCompound("Controller");
                BlockPos oldController = new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z"));
                if(!oldController.equals(k) || !ItemVaultBlock.isVault(v.state())) return;

                Direction.Axis axis = v.state().getValue(ItemVaultBlock.HORIZONTAL_AXIS);

                int width = v.nbt().getInt("Size") - 1;
                int length = v.nbt().getInt("Length") - 1;
                BlockPos newController;

                if(axis == Direction.Axis.X) {
                    newController = context.apply(oldController).offset(-length, 0, -width);
                } else {
                    newController = context.apply(oldController).offset(-width, 0, -length);
                }
                context.itemVaultControllerTransform.put(oldController, newController);
            }
        });
    }

    private static void calculateFluidTankControllerMap(CarriageContraption cc, RemapContext context) {
        cc.getBlocks().forEach((k,v) -> {
            if (v.nbt() != null && v.nbt().contains("Controller")) {
                CompoundTag tag = v.nbt().getCompound("Controller");
                BlockPos oldController = new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z"));
                if(!oldController.equals(k) || !FluidTankBlock.isTank(v.state())) return;

                int width = v.nbt().getInt("Size") - 1;
                BlockPos newController = context.apply(oldController).offset(-width, 0, -width);
                context.fluidTankControllerTransform.put(oldController, newController);
            }
        });
    }

    private static RemapResult calculateRemapState(CarriageContraption cc, Carriage carriage, RemapContext context) {
        RemapResult result = new RemapResult();
        AccessorContraption accessor = (AccessorContraption) cc;

        result.blocks = remapBlocks(cc, context);
        result.actors = remapActors(cc, context);
        result.interactors = remapInteractors(cc, context);
        result.superglues = remapSuperglues(cc, context);
        result.seats = remapSeats(cc, context);
        result.stabilizedSubContraptions = remapStabilizedSubContraptions(accessor, context);
        result.capturedMultiblocks = remapCapturedMultiblocks(accessor, context, result.blocks);
        result.initialPassengers = remapInitialPassengers(accessor, context);

        MountedStorageManager manager = carriage.storage;
        if (manager != null) {
            AccessorMountedStorageManager managerAccess = (AccessorMountedStorageManager) manager;
            result.storageItems = remapStorageItems(managerAccess, context);
            result.storageFluids = remapStorageFluids(managerAccess, context);
        }

        return result;
    }

    private static void commitRemapState(CarriageContraption cc, Carriage carriage, RemapResult result) {
        AccessorContraption accessor = (AccessorContraption) cc;

        accessor.setBlocks(result.blocks);
        cc.getActors().clear();
        cc.getActors().addAll(result.actors);
        accessor.setInteractors(result.interactors);
        accessor.setSuperglue(result.superglues);
        cc.getSeats().clear();
        cc.getSeats().addAll(result.seats);
        accessor.setStabilizedSubContraptions(result.stabilizedSubContraptions);
        accessor.setCapturedMultiblocks(result.capturedMultiblocks);
        accessor.setInitialPassengers(result.initialPassengers);

        MountedStorageManager manager = carriage.storage;
        if (manager != null) {
            AccessorMountedStorageManager managerAccess = (AccessorMountedStorageManager) manager;
            managerAccess.invokeReset();
            managerAccess.getItemsBuilder().putAll(result.storageItems);
            managerAccess.getFluidsBuilder().putAll(result.storageFluids);
            managerAccess.invokeInitialize();
            if (manager instanceof TrainCargoManager cargoManager) {
                ((AccessorTrainCargoManager) cargoManager).invokeChangeDetected();
            }

            if (manager instanceof TrainCargoManager cargoManager) {
                ((AccessorTrainCargoManager) cargoManager).invokeChangeDetected();
            }
        }
    }

    private static void postProcess(CarriageContraption cc, CarriageContraptionEntity cce, Carriage carriage, RemapContext context) {
        cc.invalidateColliders();

        if (carriage.storage == null) {
            carriage.storage = new TrainCargoManager();
        }
        ((AccessorTrainCargoManager) carriage.storage).invokeChangeDetected();
        carriage.storage.resetIdleCargoTracker();
        cce.syncCarriage();

        if (context.isClientSide) {
            updateClientRenderData(cc, cce, context);
        }
    }
    private static Map<BlockPos, MountedItemStorage> rebuildItemMap(AccessorMountedStorageManager access, RemapContext context) {
        Map<BlockPos, MountedItemStorage> newItems = new HashMap<>();
        // 从旧的不可变 Map 中读出所有存储
        access.getAllItemStorages().forEach((oldPos, storage) -> {
            // 计算新坐标
            BlockPos newPos = context.apply(oldPos);
            newItems.put(newPos, storage);
        });
        return newItems;
    }
    private static Map<BlockPos, MountedFluidStorage> rebuildFluidMap(AccessorMountedStorageManager access, RemapContext context) {
        Map<BlockPos, MountedFluidStorage> newFluids = new HashMap<>();
        // 0.6 的流体存储通常挂在 fluidsWrapper 字段下
        access.getFluidsWrapper().storages.forEach((oldPos, storage) -> {
            // 使用你的流体控制器映射表获取新坐标
            BlockPos newPos = context.fluidTankControllerTransform.getOrDefault(oldPos, context.apply(oldPos));
            newFluids.put(newPos, storage);
        });
        return newFluids;
    }

    private static HashMap<BlockPos, StructureTemplate.StructureBlockInfo> remapBlocks(CarriageContraption cc, RemapContext context) {
        HashMap<BlockPos, StructureTemplate.StructureBlockInfo> newBlocks = new HashMap<>();
        cc.getBlocks().forEach((k,v) -> {
            BlockPos newPos = context.apply(k);
            StructureTemplate.StructureBlockInfo newInfo = StructureTransformUtil.getTransformedStructureBlockInfo(v, context.transform);

            if(newInfo.nbt() != null && newInfo.nbt().contains("Controller")) {
                handleStorageBlockNBT(newBlocks, k, newPos, newInfo, context);
            } else {
                newBlocks.put(newPos, newInfo);
            }
        });
        return newBlocks;
    }

    private static void handleStorageBlockNBT(HashMap<BlockPos, StructureTemplate.StructureBlockInfo> newBlocks,
                                              BlockPos oldPos,
                                              BlockPos newPos,
                                              StructureTemplate.StructureBlockInfo info,
                                              RemapContext context) {
        if(info.nbt() == null || !info.nbt().contains("Controller")) return;
        CompoundTag tag = info.nbt().getCompound("Controller").copy();
        BlockPos oldController = new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z"));

        BlockPos newController;
        if(ItemVaultBlock.isVault(info.state())) {
            newController = context.itemVaultControllerTransform.get(oldController);
        } else if(FluidTankBlock.isTank(info.state())) {
            newController = context.fluidTankControllerTransform.get(oldController);
        } else {
            return;
        }

        if(newController == null) {
            throw new IllegalStateException("Failed to find new controller position for storage block at " + oldPos);
        }

        tag.putInt("X", newController.getX());
        tag.putInt("Y", newController.getY());
        tag.putInt("Z", newController.getZ());

        info.nbt().put("Controller", tag);
        if(oldPos.equals(oldController)) {  //controller block itself
            //move it to new controller's transformed position
            StructureTemplate.StructureBlockInfo tankInfo = new StructureTemplate.StructureBlockInfo(
                    newController,
                    info.state().rotate(Rotation.CLOCKWISE_180),
                    info.nbt()
            );
            newBlocks.put(newController, tankInfo);
        } else if (newPos.equals(newController)) {  //the tank in new controller position
            //move it to old controller's transformed position
            StructureTemplate.StructureBlockInfo tankInfo = new StructureTemplate.StructureBlockInfo(
                    context.apply(oldController),
                    info.state().rotate(Rotation.CLOCKWISE_180),
                    info.nbt()
            );
            newBlocks.put(context.apply(oldController), tankInfo);
        } else {
            //neither the controller nor the tank at controller position
            newBlocks.put(newPos, info);
        }
    }

    private static List<MutablePair<StructureTemplate.StructureBlockInfo, MovementContext>> remapActors(CarriageContraption cc, RemapContext context) {
        List<MutablePair<StructureTemplate.StructureBlockInfo, MovementContext>> newActors = new ArrayList<>(cc.getActors().size());
        for(MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> actor : cc.getActors()) {
            StructureTemplate.StructureBlockInfo newInfo = StructureTransformUtil.getTransformedStructureBlockInfo(actor.getLeft(), context.transform);

            MovementContext movementContext = actor.getRight();
            movementContext.localPos = context.apply(movementContext.localPos);
            movementContext.state = movementContext.state.rotate(Rotation.CLOCKWISE_180);
            movementContext.blockEntityData =  StructureTransformUtil.getTransformedBlockEntityNbt(movementContext.blockEntityData, context.transform);

            newActors.add(MutablePair.of(newInfo, movementContext));
        }
        return newActors;
    }

    private static Map<BlockPos, MovingInteractionBehaviour> remapInteractors(CarriageContraption cc, RemapContext context) {
        Map<BlockPos, MovingInteractionBehaviour> newInteractors = new HashMap<>();
        cc.getInteractors().forEach((k,v) -> {
            BlockPos newPos = context.apply(k);
            newInteractors.put(newPos, v);
        });
        return newInteractors;
    }

    private static List<AABB> remapSuperglues(CarriageContraption cc, RemapContext context) {
        List<AABB> newSuperglues = new ArrayList<>();
        ((AccessorContraption) cc).getSuperglue().forEach(superglue -> {
            BlockPos start = new BlockPos(- (int) superglue.minX + 1, (int) superglue.minY, - (int) superglue.minZ + 1)
                    .relative(context.assemblyDirection, context.bogeySpacing);
            BlockPos end = new BlockPos(- (int) superglue.maxX + 1, (int) superglue.maxY, - (int) superglue.maxZ + 1)
                    .relative(context.assemblyDirection, context.bogeySpacing);
            newSuperglues.add(new AABB(start, end));
        });
        return newSuperglues;
    }

    private static List<BlockPos> remapSeats(CarriageContraption cc, RemapContext context) {
        List<BlockPos> newSeats = new ArrayList<>();
        cc.getSeats().forEach(seatPos -> {
            BlockPos newPos = context.apply(seatPos);
            newSeats.add(newPos);
        });
        return newSeats;
    }

    private static Map<UUID, BlockFace> remapStabilizedSubContraptions(AccessorContraption accessor, RemapContext context) {
        Map<UUID, BlockFace> newStabilizedSubContraptions = new HashMap<>();
        accessor.getStabilizedSubContraptions().forEach((k,v) -> {
            BlockPos newPos = context.apply(v.getPos());
            Direction newDirection = v.getOppositeFace();

            newStabilizedSubContraptions.put(k, new BlockFace(newPos, newDirection));
        });
        return newStabilizedSubContraptions;
    }

    private static Multimap<BlockPos, StructureTemplate.StructureBlockInfo> remapCapturedMultiblocks(AccessorContraption accessor, RemapContext context, Map<BlockPos, StructureTemplate.StructureBlockInfo> newBlocks) {
        Multimap<BlockPos, StructureTemplate.StructureBlockInfo> newCapturedMultiblocks = ArrayListMultimap.create();
        accessor.getCapturedMultiblocks().forEach((k,v) -> {
            BlockPos newPos = context.apply(k);
            // multiblocks.info are referencing the same info in blocks
            BlockPos newInfoPos = context.apply(v.pos());
            StructureTemplate.StructureBlockInfo newInfo = newBlocks.get(newInfoPos);

            newCapturedMultiblocks.put(newPos, newInfo);
        });
        return newCapturedMultiblocks;
    }

    private static Map<BlockPos, Entity> remapInitialPassengers(AccessorContraption accessor, RemapContext context) {
        Map<BlockPos, Entity> newInitialPassengers = new HashMap<>();
        accessor.getInitialPassengers().forEach((k,v) -> {
            BlockPos newPos = context.apply(k);
            newInitialPassengers.put(newPos, v);
        });
        return newInitialPassengers;
    }

    private static Map<BlockPos, MountedItemStorage> remapStorageItems(AccessorMountedStorageManager managerAccess, RemapContext context) {
        Map<BlockPos, MountedItemStorage> newStorage = new HashMap<>();
        managerAccess.getAllItemStorages().forEach((k, v) -> {
            BlockPos newPos = context.apply(k);
            newStorage.put(newPos, v);
        });
        return newStorage;
    }
    private static Map<BlockPos, MountedFluidStorage> remapStorageFluids(AccessorMountedStorageManager managerAccess, RemapContext context) {
        Map<BlockPos, MountedFluidStorage> newStorage = new HashMap<>();

        // 从包装器里拿存储清单
        managerAccess.getFluidsWrapper().storages.forEach((k, v) -> {
            BlockPos newPos = context.fluidTankControllerTransform.getOrDefault(k, context.apply(k));
            newStorage.put(newPos, v);
        });

        return newStorage;
    }

    private static void updateClientRenderData(CarriageContraption cc, CarriageContraptionEntity cce, RemapContext context) {
        // 1. 直接操作 blocks，这是 0.6 唯一确定存在的数据源
        Map<BlockPos, StructureTemplate.StructureBlockInfo> blocks = cc.getBlocks();
        Map<BlockPos, StructureTemplate.StructureBlockInfo> newNBTBlocks = new HashMap<>();

        blocks.forEach((pos, info) -> {
            if (info.nbt() != null && info.nbt().contains("TankContent")) {
                // 这是一个流体坦克
                CompoundTag nbt = info.nbt().copy();
                int width = nbt.getInt("Size") - 1;

                // 计算这个坦克对应的重映射后的 Controller 坐标
                BlockPos oldController = NbtUtils.readBlockPos(nbt.getCompound("Controller"));
                BlockPos newController = context.fluidTankControllerTransform.get(oldController);

                if (newController != null) {
                    // 更新 NBT 里的控制器坐标引用
                    nbt.put("Controller", NbtUtils.writeBlockPos(newController));

                    // 创建带有新 NBT 的 info
                    StructureTemplate.StructureBlockInfo newInfo = new StructureTemplate.StructureBlockInfo(info.pos(), info.state(), nbt);
                    newNBTBlocks.put(pos, newInfo);
                }
            }
        });

        // 把改好的 NBT 塞回 cc
        newNBTBlocks.forEach((pos, info) -> blocks.put(pos, info));

        // 2. 这里的 storage 同样使用 Builder 模式重载（避开直接 set）
        MountedStorageManager storage = cc.getStorage();
        if (storage instanceof TrainCargoManager cargoManager) {
            AccessorMountedStorageManager access = (AccessorMountedStorageManager) (Object) cargoManager;
            access.invokeReset();
            // 重新填充你的 result.storageItems 和 result.storageFluids
            access.getItemsBuilder().putAll(rebuildItemMap(access, context));
            access.getFluidsBuilder().putAll(rebuildFluidMap(access, context));
            access.invokeInitialize();
        }

        // 3. 关键：强制 Contraption 彻底丢弃旧模型和旧 BE，根据 blocks 里的新 NBT 重生
        reloadContraptionRender(cc, cce);
    }

    private static void reloadContraptionRender(CarriageContraption cc, CarriageContraptionEntity cce) {
        CompoundTag tag = cc.writeNBT(false);
        cc.readNBT(cce.level(), tag, false);
        cce.syncCarriage();
        cc.getBlocks().forEach((pos, info) -> {
            if (info.state().getBlock() instanceof PortableFuelInterfaceBlock) {
                BlockEntity be = cc.getBlockEntityClientSide(pos);
                if (be instanceof PortableFluidInterfaceBlockEntity pfi) {
                    ((AccessorPortableFluidInterfaceBlockEntity) pfi).invokeStopTransferring();
                    pfi.startTransferringTo(cc, 0);
                }
            }
        });
    }

    private static void updateFluidTankRenderData(FluidTankBlockEntity ft, BlockPos pos, CarriageContraption cc, RemapContext context) {
        CompoundTag tag = new CompoundTag();
        ft.write(tag, false);

        CompoundTag tankContent = tag.getCompound("TankContent");
        int width = tag.getInt("Size") - 1;
        BlockPos newPos = context.apply(pos).offset(-width, 0, -width);

        StructureTemplate.StructureBlockInfo info = cc.getBlocks().get(newPos);
        if(info == null || info.nbt() == null) return;
        info.nbt().put("TankContent", tankContent);
    }
    

    public static boolean isDoubleEnded(List<Carriage> carriages) {
        for(Carriage carriage : carriages) {
            CarriageContraptionEntity cce = carriage.anyAvailableEntity();
            if(cce == null) continue;
            Contraption contraption = cce.getContraption();
            if(!(contraption instanceof CarriageContraption cc)) continue;
            if(cc.hasBackwardControls()) return true;
        }
        return false;
    }

    public static void reverseBogeys(Carriage carriage) {

        if (!carriage.isOnTwoBogeys()) {
            CarriageBogey bogey =  carriage.bogeys.getFirst();
            Couple<TravellingPoint> points = ((AccessorCarriageBogey) bogey).getPoints();

            //翻转Point内的属性
            for(boolean originalFirstPoint : Iterate.trueAndFalse) {
                points.get(originalFirstPoint).reverse(carriage.train.graph);
            }
            //交换bogey的points
            ((AccessorCarriageBogey) bogey).setPoints(Couple.create(points.getSecond(), points.getFirst()));

            carriage.bogeys.setFirst(bogey);
            return;
        }

        Couple<CarriageBogey> newBogeys = carriage.bogeys;

        for(boolean originalFirstBogey : Iterate.trueAndFalse) {
            CarriageBogey bogey = carriage.bogeys.get(originalFirstBogey);
            Couple<TravellingPoint> points = ((AccessorCarriageBogey) bogey).getPoints();

            for(boolean originalFirstPoint : Iterate.trueAndFalse) {
                points.get(originalFirstPoint).reverse(carriage.train.graph);
            }

            boolean isLeading = ((AccessorCarriageBogey) bogey).isLeading();
            ((AccessorCarriageBogey) bogey).setLeading(!isLeading);

            //交换bogey的points
            ((AccessorCarriageBogey) bogey).setPoints(Couple.create(points.getSecond(), points.getFirst()));
        }

        carriage.bogeys = Couple.create(
                carriage.bogeys.getSecond(),
                carriage.bogeys.getFirst()
        );

        CarriageContraptionEntity cce = carriage.anyAvailableEntity();
        if (cce == null) return;
        cce.setCarriage(carriage);
    }
}
