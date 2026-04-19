package willow.train.kuayue.systems.device;

import kasuga.lib.registrations.common.BlockEntityReg;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.systems.device.driver.combustion.InternalCombustionDriveControllerBlockEntity;
import willow.train.kuayue.systems.device.driver.combustion.InternalCombustionDriveControllerBlockEntityRenderer;
import willow.train.kuayue.systems.device.driver.seat.DoubleDriverSeatBlockEntity;
import willow.train.kuayue.systems.device.driver.seat.DoubleDriverSeatBlockRenderer;
import willow.train.kuayue.systems.device.track.entry.StationEntryBlockEntity;
import willow.train.kuayue.systems.device.track.entry.client.StationEntryBlockEntityRenderer;
import willow.train.kuayue.systems.device.track.train_station.TrainStationBlockEntity;
import willow.train.kuayue.systems.device.track.train_station.client.TrainStationBlockEntityRenderer;

public class AllDeviceBlockEntities {
//    public static BlockEntityReg<TrainStationBlockEntity> STATION_BLOCK_ENTITY =
//            new BlockEntityReg<TrainStationBlockEntity>("train_station_block_entity")
//                    .blockEntityType(TrainStationBlockEntity::new)
//                    .addBlock(AllDeviceBlocks.TRAIN_STATION_BLOCK)
//                    .withRenderer(()-> TrainStationBlockEntityRenderer::new)
//                    .submit(AllElements.testRegistry);

//    public static final BlockEntityReg<StationEntryBlockEntity> STATION_ENTRY_BLOCK_ENTITY =
//            new BlockEntityReg<StationEntryBlockEntity>("station_entry_block_entity")
//                    .blockEntityType(StationEntryBlockEntity::new)
//                    .addBlock(AllDeviceBlocks.STATION_ENTRY)
//                    .withRenderer(()-> StationEntryBlockEntityRenderer::new)
//                    .submit(AllElements.testRegistry);

    public static BlockEntityReg<InternalCombustionDriveControllerBlockEntity> INTERNAL_COMBUSTION_DRIVE_CONTROLLER_BLOCK_ENTITY =
        new BlockEntityReg<InternalCombustionDriveControllerBlockEntity>("internal_combustion_drive_controller_block_entity")
                .blockEntityType(InternalCombustionDriveControllerBlockEntity::new)
                .addBlock(AllDeviceBlocks.INTERNAL_COMBUSTION_DRIVE_CONTROLLER)
                .withRenderer(()-> InternalCombustionDriveControllerBlockEntityRenderer::new)
                .submit(AllElements.testRegistry);


    public static BlockEntityReg<DoubleDriverSeatBlockEntity> DOUBLE_DRIVER_SEAT =
            new BlockEntityReg<DoubleDriverSeatBlockEntity>("double_driver_seat")
                    .blockEntityType(DoubleDriverSeatBlockEntity::new)
                    .addBlock(AllDeviceBlocks.DOUBLE_DRIVER_SEAT)
                    .withRenderer(()-> DoubleDriverSeatBlockRenderer::new)
                    .submit(AllElements.testRegistry);

    public static void invoke() {}
}
