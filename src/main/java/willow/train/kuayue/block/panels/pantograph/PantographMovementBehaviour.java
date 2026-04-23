package willow.train.kuayue.block.panels.pantograph;

import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import kasuga.lib.core.create.device.TrainDeviceLocation;
import kasuga.lib.core.create.device.TrainDeviceManager;
import kasuga.lib.core.util.data_type.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.KuayueConfig;
import willow.train.kuayue.block.panels.pantograph.network.ClientSyncManager;
import willow.train.kuayue.block.panels.pantograph.network.ServerSyncManager;
import willow.train.kuayue.initial.AllPackets;
import willow.train.kuayue.network.s2c.ContraptionNbtUpdatePacket;
import willow.train.kuayue.systems.device.AllDeviceSystems;
import willow.train.kuayue.systems.device.driver.devices.power.PantographState;
import willow.train.kuayue.systems.device.driver.devices.power.PantographSystem;
import willow.train.kuayue.systems.overhead_line.block.line.OverheadLineRendererSystem;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlockEntity;
import willow.train.kuayue.systems.overhead_line.render.RenderCurve;
import willow.train.kuayue.utils.client.DebugDrawUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PantographMovementBehaviour implements MovementBehaviour {
    @Override
    public void startMoving(MovementContext context) {}

    WeakHashMap<MovementContext, AtomicInteger> ticker = new WeakHashMap<>();

    @Override
    public void tick(MovementContext context) {
        if(ticker.computeIfAbsent(context, k -> new AtomicInteger(0)).incrementAndGet() <=
                (context.world.isClientSide ?
                        KuayueConfig.CONFIG.getIntValue("PANTOGRAPH_FRESH_INTERVAL_TICKS_CLIENT") :
                        KuayueConfig.CONFIG.getIntValue("PANTOGRAPH_FRESH_INTERVAL_TICKS_SERVER"))) return;

        ticker.get(context).set(0);

        Pair<TrainDeviceManager, TrainDeviceLocation> locator = TrainDeviceManager.getManager(context);

        if(!(context.contraption instanceof CarriageContraption carriageContraption))
            return;

        if(locator == null || locator.getSecond() == null)
            return;

        BlockEntity blockEntity = context.contraption.getBlockEntityClientSide(context.localPos);

        if(blockEntity == null && (context.state.getBlock() instanceof EntityBlock entityBlock)) {
            blockEntity = entityBlock.newBlockEntity(context.localPos, context.state);
            if(blockEntity == null)
                return;
            blockEntity.setLevel(context.world);
            blockEntity.load(context.blockEntityData);
        }

        if(
                !(blockEntity instanceof IPantographBlockEntity pantographBlockEntity) ||
                !(blockEntity instanceof SyncedBlockEntity syncedBlockEntity)
        ) return;
        StructureTemplate.StructureBlockInfo bsInfo = context.contraption.getBlocks().get(context.localPos);
        if (bsInfo == null) return;
        if (!bsInfo.state().hasProperty(BlockStateProperties.OPEN)) return;
        boolean risenFlag = bsInfo.state().getValue(BlockStateProperties.OPEN);
        pantographBlockEntity.setRisen(risenFlag);

        PantographSystem pantographSystem = locator.getFirst().getOrCreateSystem(AllDeviceSystems.PANTOGRAPH);

        if(pantographSystem == null)
            return;

        PantographState state = pantographSystem.getOrRegisterPantographState(
                locator.getSecond(),
                carriageContraption.getAssemblyDirection() == context.state.getValue(BlockStateProperties.HORIZONTAL_FACING) ?
                        Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE
        );

        if(state.shouldUpdate(pantographBlockEntity.isRisen())) {
            pantographBlockEntity.setRisen(state.isRisen());
            CompoundTag data = new CompoundTag();
            syncedBlockEntity.writeClient(data);
            writeData(context.contraption.entity, context.localPos, data);
        }

        if(context.blockEntityData.get("overhead_line_support_cache") == null) {
            CompoundTag data = new CompoundTag();
            CurrOverheadLineCache cache = new CurrOverheadLineCache();
            cache.write(data);

            context.blockEntityData.put("overhead_line_support_cache", data);
        }

        if (!risenFlag) {
            pantographBlockEntity.tick();
            return;
        }

        if(isOverheadLineCacheNeedUpdate(context, pantographBlockEntity)) {
            updateOverheadLineCache(context, pantographBlockEntity);
        }

        CompoundTag cacheData = context.blockEntityData.getCompound("overhead_line_support_cache");
        CurrOverheadLineCache cache = Objects.requireNonNullElse(
                pantographBlockEntity.getCache(), new CurrOverheadLineCache()
        );
        cache = clientSync(context, cache);
        pantographBlockEntity.setCache(cache);
        cache.read(cacheData);
        boolean clientSide = context.world.isClientSide();
        boolean debugging = false;
        if (clientSide) {
            debugging = Minecraft.getInstance().options.renderDebug;
            if (!debugging) {
                DebugDrawUtil.clearAllDebugElements();
                DebugDrawUtil.clearAllDebugElements();
            }
        }

        //render a red box to show the current support pos
        if(cache.hasCurrSupport() && clientSide && debugging) {
            DebugDrawUtil.addDebugBox("pantograph_support_curr",
                    cache.getCurrSupportPos(),
                    1.0f,
                    0.0f,
                    0.0f,
                    0.7f);
        }

        //render a green box to show the current link pos
        if(cache.hasCurrLink() && clientSide && debugging) {
            DebugDrawUtil.addDebugBox("pantograph_link_curr",
                    cache.getCurrLink(),
                    0.0f,
                    1.0f,
                    0.0f,
                    0.7f);
        }


//        Kuayue.LOGGER.debug("Pantograph at {}", context.position);
//        Kuayue.LOGGER.debug("Current Support: {}", cache.hasCurrSupport() ? cache.getCurrSupportPos().toShortString() : "null");
//        Kuayue.LOGGER.debug("Current Point Pos: {}", cache.getCurrPointPos());
//        Kuayue.LOGGER.debug("Current Link: {}", cache.hasCurrLink() ? cache.getCurrLink().toShortString() : "null");
//        Kuayue.LOGGER.debug("Next Point Pos: {}", cache.getNextPointPos());
    }




    public void writeData(AbstractContraptionEntity contraptionEntity, BlockPos localPos, CompoundTag tag) {
        AllPackets.INTERACTION.getChannel().send(
                PacketDistributor.TRACKING_ENTITY.with(()->contraptionEntity),
                new ContraptionNbtUpdatePacket(contraptionEntity.getId(), localPos, tag)
        );
    }

    private boolean isOverheadLineCacheNeedUpdate(MovementContext context, IPantographBlockEntity be) {
        CompoundTag data = context.blockEntityData.getCompound("overhead_line_support_cache");
        CurrOverheadLineCache cache = Objects.requireNonNullElse(be.getCache(), new CurrOverheadLineCache());
        cache.read(data);
        boolean clientSide = context.world.isClientSide();
        boolean debugging = false;
        if (clientSide) {
            debugging = Minecraft.getInstance().options.renderDebug;
        }

        if(!cache.hasCurrSupport()) {
            if (clientSide && debugging) {
                DebugDrawUtil.clearAllDebugElements();
            }
            return true;
        }

        if(!cache.hasCurrLink()) {
            if (clientSide && debugging) {
                DebugDrawUtil.clearAllDebugElements();
            }
            return true;
        }

        BlockPos nextSupportPos = cache.getCurrLink();
        BlockPos currentSupportPos = cache.getCurrSupportPos();
        // 方向突然间反过来的话也要更新
        if (Vec3.atCenterOf(nextSupportPos).subtract(Vec3.atCenterOf(currentSupportPos))
                .dot(context.motion.normalize()) < 0.0f) {
            if (clientSide && debugging) {
                DebugDrawUtil.clearAllDebugElements();
            }
            return true;
        }
        Vec3 nextPointPos = cache.getNextPointPos();
        Vec3 supportVec = nextPointPos.subtract(cache.getCurrPointPos());
        Vec3 supportVecHoriz = new Vec3(supportVec.x, 0f, supportVec.z);
        Vec3 pantographPos = context.position;
        Vec3 pantographVec = pantographPos.subtract(cache.getCurrPointPos());
        Vec3 pantographVecHoriz = new Vec3(pantographVec.x, 0f, pantographVec.z);

        // double res = supportVec.dot(pantographVec) / (supportVec.length() * supportVec.length());
        float res = (float) (pantographVecHoriz.dot(supportVecHoriz) / supportVecHoriz.lengthSqr());

        if(res > 1.0) {
            if (clientSide && debugging) {
              DebugDrawUtil.clearAllDebugElements();
            }

            cache.setCurrSupportPos(nextSupportPos);
            cache.setCurrPointPos(nextPointPos);
            cache.setCurrentPoint(cache.getNextPoint());

            cache.clearCurrLink();

            cache.write(data);
            context.blockEntityData.put("overhead_line_support_cache", data);

            return true;
        } else if (res < 0.0) {
            if (clientSide && debugging) {
                DebugDrawUtil.clearAllDebugElements();
            }
            cache.clearAll();
            be.setCache(null);
                if (context.world.isClientSide) {
                  be.resetAngle();
                }

            cache.write(data);
            context.blockEntityData.put("overhead_line_support_cache", data);

            return true;
        } else if (res >= .4f && res <= .6f) {
              // NOTICE: 在这中间进行更新应该是最保险的
              if (serverNeedToSync(context)) {
                  serverSync(context, cache);
              }
          }

        getPantographHeight(context, be, pantographPos, cache, (float) (res * supportVec.length()));

        return false;
    }

    private void getPantographHeight(MovementContext context, IPantographBlockEntity be,
                                     Vec3 bePos, CurrOverheadLineCache cache, float res) {
        if (!context.world.isClientSide) return;
        IPantographAngleMapping mapping = be.getPantographAngleMapping();
        if (!mapping.readyForMap()) return;
        OverheadLineRendererSystem.OverheadLineLocator curveLocator =
                new OverheadLineRendererSystem.OverheadLineLocator(
                        context.world.dimension(),
                        cache.getCurrSupportPos(), cache.getCurrLink(),
                        cache.getCurrentPoint(), cache.getNextPoint()
                );
        OverheadLineRendererSystem.Rendering rendering =
                OverheadLineRendererSystem.OVERHEAD_LINES.get(curveLocator);
        if (rendering == null) {
             curveLocator =
                    new OverheadLineRendererSystem.OverheadLineLocator(
                            context.world.dimension(),
                            cache.getCurrLink(), cache.getCurrSupportPos(),
                            cache.getCurrentPoint(), cache.getNextPoint()
                    );
             rendering =  OverheadLineRendererSystem.OVERHEAD_LINES.get(curveLocator);
             if (rendering == null) {
                 cache.clearCache();
                 return;
             }
        }
        RenderCurve curve = rendering.curve();
        Objects.requireNonNull(curve);
        ArrayList<Vec2> vectors = Objects.requireNonNullElse(
                cache.getCachedPoints(),
                curve.getPantographAdjustVectors()
        );
        if (cache.getCachedPoints() == null) {
            // 把结点都反过来
            ArrayList<Vec2> newVectors = new ArrayList<>(vectors.size());
            for (int i = vectors.size() - 1; i >= 0; i--) {
                newVectors.add(new Vec2(vectors.get(i).x, - vectors.get(i).y));
            }
            cache.setRevertCachedPoints(newVectors);
            cache.setCachedPoints(vectors);
            float totalDeltaHeight = 0;
            for (Vec2 vec : vectors) {
                totalDeltaHeight += vec.y;
            }
            cache.setCacheTotalHeight(totalDeltaHeight);
        }

        if (Math.abs(cache.getCurrPointPos().y +
                cache.getCacheTotalHeight() - cache.getNextPointPos().y) > 0.05) {
            vectors = cache.getRevertCachedPoints();
        }
        if (vectors.isEmpty()) {
            cache.clearCache();
            return;
        }
        if (cache.getCacheVectorIndex() < 0) {
            if (vectors.size() == 1) {
                cache.setCacheProgress(0);
                cache.setCacheHeight(0);
                cache.setCacheVectorIndex(0);
            } else {
                double progressIntegral = 0;
                double heightIntegral = 0;
                Vec2 current;
                for (int i = 0; i < vectors.size(); i++) {
                    current = vectors.get(i);
                    if (progressIntegral <= res &&
                            (progressIntegral + current.x) >= res) {
                        cache.setCacheProgress(progressIntegral);
                        cache.setCacheHeight(heightIntegral);
                        cache.setCacheVectorIndex(i);
                        break;
                    }
                    progressIntegral += current.x;
                    heightIntegral += current.y;
                }
            }
        } else {
            double cacheProgress = cache.getCacheProgress();
            double cacheHeight = cache.getCacheHeight();
            int cacheVectorIndex = cache.getCacheVectorIndex();
            while (res > cacheProgress + vectors.get(cacheVectorIndex).x) {
                cacheProgress += vectors.get(cacheVectorIndex).x;
                cacheHeight += vectors.get(cacheVectorIndex).y;
                cacheVectorIndex ++;
                if (cacheVectorIndex >= vectors.size()) {
                    cache.clearCache();
                    return;
                }
            }
            cache.setCacheProgress(cacheProgress);
            cache.setCacheVectorIndex(cacheVectorIndex);
            cache.setCacheHeight(cacheHeight);
        }

        if (cache.getCacheVectorIndex() < 0) {
            cache.clearCache();
            return;
        }
        Vec2 currentVec = vectors.get(cache.getCacheVectorIndex());
        // 这里计算的是当前的高度
        float currentHeight = (float) (cache.getCurrPointPos().y() + cache.getCacheHeight() +
                currentVec.y * (res - cache.getCacheProgress()) /
                        (currentVec.x - cache.getCacheProgress()));

        // System.out.println(currentHeight);
        float velocity = (float) context.motion.length();
        // 这里计算的是预测的下一时刻的高度
//        float nextHeight = (float) (cache.getCurrPointPos().y() + cache.getCacheHeight() +
//                currentVec.y * (res + velocity - cache.getCacheProgress()) /
//                (currentVec.x + velocity - cache.getCacheProgress()));

        float deltaHeight = (float) (currentHeight - bePos.y) -
                (float) mapping.getBaseHeight() - (float) be.getYOffset();
//        float nextDeltaHeight = (float) (nextHeight - bePos.y) -
//                (float) mapping.getBaseHeight() - (float) be.getYOffset();

        // System.out.println(deltaHeight);
        double angle = mapping.getAngleByHeight(deltaHeight * 16f);
//        double nextAngle = mapping.getAngleByHeight(nextDeltaHeight * 16f);
        // System.out.println(angle);
        be.setAngle(angle);
    }

    // 嗅探，找到最近的可用挂架
    private @Nullable BlockPos getClosedSupport(MovementContext context, Vec3 forwardVec, int searchRadius) {
        BlockPos pantographPos = BlockPos.containing(context.position);
        BlockPos minPos = pantographPos.offset(-searchRadius, -searchRadius, -searchRadius);
        BlockPos maxPos = pantographPos.offset(searchRadius, searchRadius, searchRadius);

        List<BlockPos> supportPosList = new ArrayList<>();
        BlockPos.betweenClosedStream(minPos, maxPos)
                .forEach(blockPos -> {
                    if(context.world.getBlockEntity(blockPos) instanceof OverheadLineSupportBlockEntity) {
                        supportPosList.add(blockPos.immutable());
                    }
                });

        if(supportPosList.isEmpty()) {
            return null;
        }

        double dist = Double.MAX_VALUE;
        BlockPos supportPos = null;

        // only leave the most close support
        for(BlockPos blockPos : supportPosList) {
            BlockEntity blockEntity = context.world.getBlockEntity(blockPos);
            if(!(blockEntity instanceof OverheadLineSupportBlockEntity)) {
                continue;
            }
            Vec3 supportVec = Vec3.atCenterOf(blockPos).subtract(
                    Vec3.atCenterOf(pantographPos)
            );
            double currentDist = supportVec.cross(forwardVec).length() / forwardVec.length();
            if(currentDist < dist) {
                dist = currentDist;
                supportPos = blockPos;
            }
        }

        return supportPos;
    }

    private void updateOverheadLineCache(MovementContext context, IPantographBlockEntity be) {
        CompoundTag data = context.blockEntityData.getCompound("overhead_line_support_cache");
        CurrOverheadLineCache cache = Objects.requireNonNullElse(be.getCache(), new CurrOverheadLineCache());
        cache.read(data);
        boolean clientSide = context.world.isClientSide();
        boolean debugging = false;
        if (clientSide) {
            debugging = Minecraft.getInstance().options.renderDebug;
        }


        int searchRadius = 5;
        double threshold = Math.cos(Math.toRadians(10));
        Vec3 forwardVec = context.motion.normalize();

        if(forwardVec.equals(Vec3.ZERO)) {
            return;
        }

        if(!cache.hasCurrLink()) {
            if(!cache.hasCurrSupport()) {
                // 这里是嗅探逻辑

                @Nullable BlockPos supportPos = getClosedSupport(context, forwardVec, searchRadius);

                if (supportPos == null) {
                    return;
                }

                BlockEntity blockEntity = context.world.getBlockEntity(supportPos);
                if(!(blockEntity instanceof OverheadLineSupportBlockEntity supportBlockEntity)) {
                    return;
                }

                List<OverheadLineSupportBlockEntity.Connection> connectionList = new ArrayList<>(supportBlockEntity.getConnections());
                fineBestConnection(context, forwardVec, supportBlockEntity, connectionList);

                if(connectionList.isEmpty()) {
                    // if after filtering, no connection is suitable, skip
                    cache.clearAll();
                    be.setCache(null);
                    if (context.world.isClientSide) {
                        be.resetAngle();
                    }
                } else if(connectionList.size() == 1) {
                    // if after filtering, have only one connection, it's the best one
                    OverheadLineSupportBlockEntity.Connection connection = connectionList.get(0);

                    cache.setCurrSupportPos(supportPos);
                    cache.setCurrPointPos(supportBlockEntity.getConnectionPointByIndex(connection.connectionIndex()));
                    cache.setCurrLink(connection.absolutePos());
                    cache.setCurrentPoint(connection.connectionIndex());

                    BlockEntity targetBlockEntity = context.world.getBlockEntity(connection.absolutePos());
                    if(!(targetBlockEntity instanceof OverheadLineSupportBlockEntity targetSupportBlockEntity)) {
                        cache.clearAll();
                        be.setCache(null);
                        if (context.world.isClientSide) {
                            be.resetAngle();
                        }
                        return;
                    }
                    cache.setNextPointPos(targetSupportBlockEntity.getConnectionPointByIndex(connection.targetIndex()));
                    cache.setNextPoint(connection.targetIndex());
                } else {
                    // have multiple suitable connections, just set curr support. leave next empty.
                    cache.setCurrSupportPos(supportPos);
                    cache.setCurrPointPos(supportBlockEntity.getConnectionPointByIndex(
                            connectionList.get(0).connectionIndex()
                    ));
                    cache.setCurrentPoint(connectionList.get(0).connectionIndex());
                }
            } else {
                // System.out.println("Has Curr Support & No Curr Link");
                BlockPos currSupportPos = cache.getCurrSupportPos();
                BlockEntity blockEntity = context.world.getBlockEntity(currSupportPos);

                if(!(blockEntity instanceof OverheadLineSupportBlockEntity supportBlockEntity)) {
                    cache.clearAll();
                    be.setCache(null);
                    if (context.world.isClientSide) {
                        be.resetAngle();
                    }
                    return;
                }

                List< OverheadLineSupportBlockEntity.Connection> connectionList = new ArrayList<>(supportBlockEntity.getConnections());
                fineBestConnection(context, forwardVec, supportBlockEntity, connectionList);

                // all connections are out, re-select on next tick.
                if(connectionList.isEmpty()) {
                    cache.clearAll();
                    be.setCache(null);
                    if (context.world.isClientSide) {
                        be.resetAngle();
                    }
                    context.blockEntityData.remove("overhead_line_support_cache");  // 降级为嗅探模式
                    return;
                }

                if(connectionList.size() == 1) {
                    OverheadLineSupportBlockEntity.Connection connection = connectionList.get(0);

                    cache.setCurrLink(connection.absolutePos());
                    BlockEntity targetBlockEntity = context.world.getBlockEntity(connection.absolutePos());
                    if(!(targetBlockEntity instanceof OverheadLineSupportBlockEntity targetSupportBlockEntity)) {
                        cache.clearAll();
                        be.setCache(null);
                        if (context.world.isClientSide) {
                            be.resetAngle();
                        }
                        return;
                    }
                    cache.setNextPointPos(targetSupportBlockEntity.getConnectionPointByIndex(connection.targetIndex()));
                    cache.setNextPoint(connection.targetIndex());
                }
                // if size > 1, do nothing for next tick to filter again
                //DEBUG render: blue boxes are candidates
                else {
                    if (clientSide && debugging) {
                        DebugDrawUtil.clearAllDebugElements();
                        for(OverheadLineSupportBlockEntity.Connection connection : connectionList) {
                            DebugDrawUtil.addDebugBox("pantograph_support_curr",
                                    connection.absolutePos(),
                                    0.0f,
                                    0.0f,
                                    1.0f,
                                    0.7f);
                        }
                    }
                }
            }
            cache.write(data);
            context.blockEntityData.put("overhead_line_support_cache", data);
        } else {
            // 有link, 有support
            BlockPos currSupportPos = cache.getCurrSupportPos();
            BlockPos nextSupportPos = cache.getCurrLink();
            Vec3 currentSupportIndex = cache.getCurrPointPos();
            Vec3 nextSupportIndex = cache.getNextPointPos();

            Vec3 nextPointBlock = Vec3.atCenterOf(cache.getCurrLink());
            Vec3 thisPointBlock = Vec3.atCenterOf(cache.getCurrSupportPos());
            Vec3 linkVec = nextPointBlock.subtract(thisPointBlock);

            // 要把方向反过来
            if (linkVec.dot(forwardVec) < 0.0) {
                cache.setCurrLink(currSupportPos);
                cache.setCurrPointPos(nextSupportIndex);
                cache.setCurrentPoint(cache.getNextPoint());

                cache.setCurrSupportPos(nextSupportPos);
                cache.setNextPointPos(currentSupportIndex);
                cache.setNextPoint(cache.getCurrentPoint());
                cache.write(data);
                cache.clearCache();

                context.blockEntityData.put("overhead_line_support_cache", data);
            }
        }
    }

    // if list is not null, filter the most suitable connections
    // if list is null, find suitable connections from all connections
    // call this function in cycle until list size is 1 or 0
    private void fineBestConnection(
            MovementContext context,
            Vec3 forward,
            OverheadLineSupportBlockEntity entity,
            List<OverheadLineSupportBlockEntity.Connection> connectionList
    ) {
        double outerThreshold = Math.cos(Math.toRadians(45.0));
        List<OverheadLineSupportBlockEntity.Connection> connections = entity.getConnections();

        if(connections.isEmpty() || forward.equals(Vec3.ZERO)) {
            return;
        }

        if(connectionList.isEmpty()) {
            connectionList.addAll(connections);
        }

        List<OverheadLineSupportBlockEntity.Connection> toRemove = new ArrayList<>();
        for(OverheadLineSupportBlockEntity.Connection connection : connectionList) {
            BlockEntity targetEntity = context.world.getBlockEntity(connection.absolutePos());
            if(!(targetEntity instanceof OverheadLineSupportBlockEntity targetSupportEntity))
                continue;

            Vec3 connectionVec = targetSupportEntity
                    .getConnectionPointByIndex(connection.targetIndex())
                    .subtract(entity.getConnectionPointByIndex(connection.connectionIndex()));

            Vec3 connectionHoriz = new Vec3(connectionVec.x, 0, connectionVec.z).normalize();
            Vec3 forwardHoriz = new Vec3(forward.x, 0, forward.z).normalize();

            double angleCos = connectionHoriz.dot(forwardHoriz) /
                    (connectionHoriz.length() * forwardHoriz.length());

            if(angleCos < outerThreshold){
                toRemove.add(connection);
                continue;
            }

            Vec3 w = new Vec3(
                    context.position.x() - entity.getConnectionPointByIndex(connection.connectionIndex()).x(),
                    0,
                    context.position.z() - entity.getConnectionPointByIndex(connection.connectionIndex()).z()
            );

            double dist = connectionHoriz.cross(w).length() / connectionHoriz.length();
            if(dist > 1.0) {
                toRemove.add(connection);
            }
        }
        connectionList.removeAll(toRemove);
    }

    // ==================================== Network Start ====================================

    public boolean serverNeedToSync(MovementContext context) {
        if (context.world.isClientSide) return false;
        return ServerSyncManager.getInstance().needToSync(context.contraption, context.localPos);
    }

    // NOTICE: 服务端提交更新的方法
    public void serverSync(MovementContext context,
                           CurrOverheadLineCache cache) {
        // 只有在 server 才更新
        if (context.world.isClientSide) return;
        ServerSyncManager manager = ServerSyncManager.getInstance();
        if (manager.needToSync(context.contraption, context.localPos)) {
            manager.sync(
                    (ServerLevel) context.world,
                    context.localPos,
                    BlockPos.containing(context.position),
                    context.contraption,
                    cache);
        }
    }

    // NOTICE: 客户端接收更新的方法
    public CurrOverheadLineCache clientSync(MovementContext context,
                           CurrOverheadLineCache currCache) {
        // 只有在 client 才更新
        if (!context.world.isClientSide) return currCache;
        ClientSyncManager manager = ClientSyncManager.getInstance();
        CurrOverheadLineCache cache = manager.pop(context.contraption, context.localPos);
        if (cache == null || currCache == null) return currCache;
        if (Objects.equals(cache, currCache)) {
            return currCache;
        }
        return cache;
    }

    // ===================================== Network Ends ====================================
}
