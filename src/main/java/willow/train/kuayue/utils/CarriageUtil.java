package willow.train.kuayue.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.simibubi.create.content.contraptions.*;
import com.simibubi.create.content.contraptions.actors.psi.PortableFluidInterfaceBlockEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;
import com.simibubi.create.content.contraptions.minecart.TrainCargoManager;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.content.trains.entity.*;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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

        Map<BlockPos, MountedStorage> storageItems;
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
            managerAccess.setStorage(result.storageItems);
            managerAccess.setFluidStorage(result.storageFluids);
            manager.createHandlers();
            ((AccessorTrainCargoManager) manager).invokeChangeDetected();
            carriage.storage.resetIdleCargoTracker();
        }
    }

    private static void postProcess(CarriageContraption cc, CarriageContraptionEntity cce, Carriage carriage, RemapContext context) {
        cc.invalidateColliders();

        if (carriage.storage == null) {
            carriage.storage = new TrainCargoManager();
        }
        carriage.storage.createHandlers();
        ((AccessorTrainCargoManager) carriage.storage).invokeChangeDetected();
        carriage.storage.resetIdleCargoTracker();
        cce.syncCarriage();

        if (context.isClientSide) {
            updateClientRenderData(cc, cce, context);
        }
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

    private static Map<BlockPos, MountedStorage> remapStorageItems(AccessorMountedStorageManager managerAccess, RemapContext context) {
        Map<BlockPos, MountedStorage> newStorage = new HashMap<>();
        managerAccess.getStorage().forEach((k, v) -> {
            BlockPos newPos = context.apply(k);
            newStorage.put(newPos, v);
        });
        return newStorage;
    }

    private static Map<BlockPos, MountedFluidStorage> remapStorageFluids(AccessorMountedStorageManager managerAccess, RemapContext context) {
        Map<BlockPos, MountedFluidStorage> newFluidStorage = new HashMap<>();
        managerAccess.getFluidStorage().forEach((k, v) -> {
            BlockPos newPos = context.fluidTankControllerTransform.get(k);
            newFluidStorage.put(newPos, v);
        });
        return newFluidStorage;
    }

    private static void updateClientRenderData(CarriageContraption cc, CarriageContraptionEntity cce, RemapContext context) {
        cc.presentBlockEntities.forEach((k, v) -> {
            if(v instanceof FluidTankBlockEntity ft && ft.isController()) {
                updateFluidTankRenderData(ft, k, cc, context);
            }
        });

        MountedStorageManager storage = ((AccessorContraption) cc).getStorage();
        Map<BlockPos, MountedFluidStorage> newFluidStorage = new HashMap<>();
        ((AccessorMountedStorageManager) storage).getFluidStorage().forEach((k,v) -> {
            BlockPos newPos = context.fluidTankControllerTransform.get(k);
            newFluidStorage.put(newPos, v);
        });
        ((AccessorMountedStorageManager) storage).setFluidStorage(newFluidStorage);

        reloadContraptionRender(cc, cce);
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

    private static void reloadContraptionRender(CarriageContraption cc, CarriageContraptionEntity cce) {
        CompoundTag tag = cc.writeNBT(false);
        cc.modelData.clear();
        cc.presentBlockEntities.clear();
        cc.maybeInstancedBlockEntities.clear();
        cc.specialRenderedBlockEntities.clear();

        cc.readNBT(cce.level(), tag, false);
        ContraptionRenderDispatcher.invalidate(cc);

        ((AccessorContraption) cc).getStorage().bindTanks(cc.presentBlockEntities);
        for (BlockEntity be : cc.presentBlockEntities.values()) {
            if(be instanceof PortableFluidInterfaceBlockEntity pfi) {
                ((AccessorPortableFluidInterfaceBlockEntity) pfi).invokeStopTransferring();
                pfi.startTransferringTo(cc, 0);
            }
        }
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
