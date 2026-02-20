package willow.train.kuayue.systems.device;

import kasuga.lib.registrations.common.BlockReg;
import net.minecraft.world.level.material.Material;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.systems.device.driver.seat.DoubleDriverSeatBlock;
import willow.train.kuayue.systems.device.track.entry.StationEntryBlock;
import willow.train.kuayue.systems.device.track.train_station.TrainStationBlock;
import willow.train.kuayue.systems.device.driver.combustion.InternalCombustionDriveControllerBlock;

public class AllDeviceBlocks {

//    public static BlockReg<TrainStationBlock> TRAIN_STATION_BLOCK =
//            new BlockReg<TrainStationBlock>("train_station")
//                .blockType(TrainStationBlock::new)
//                .material(Material.METAL)
//                .withItem(AllDeviceEdgePoints.TRAIN_STATION.getBlockItemFactory(), null)
//                .tabTo(AllElements.neoKuayueDeviceTab)
//                .submit(AllElements.testRegistry);

//    public static BlockReg<StationEntryBlock> STATION_ENTRY =
//            new BlockReg<StationEntryBlock>("station_entry")
//                    .blockType(StationEntryBlock::new)
//                    .material(Material.METAL)
//                    .withItem(AllDeviceEdgePoints.STATION_ENTRY.getBlockItemFactory(), null)
//                    .tabTo(AllElements.neoKuayueDeviceTab)
//                    .submit(AllElements.testRegistry);


    public static BlockReg<InternalCombustionDriveControllerBlock> INTERNAL_COMBUSTION_DRIVE_CONTROLLER =
            new BlockReg<InternalCombustionDriveControllerBlock>("internal_combustion_drive_controller")
                .blockType(InternalCombustionDriveControllerBlock::new)
                .material(Material.METAL)
                .defaultBlockItem()
                .tabTo(AllElements.neoKuayueDeviceTab)
                .submit(AllElements.testRegistry);

    public static BlockReg<DoubleDriverSeatBlock> DOUBLE_DRIVER_SEAT =
            new BlockReg<DoubleDriverSeatBlock>("double_driver_seat")
                    .blockType(DoubleDriverSeatBlock::new)
                    .material(Material.METAL)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueDeviceTab)
                    .submit(AllElements.testRegistry);

    public static void invoke(){
        AllDeviceBlockEntities.invoke();
        AllDeviceEdgePoints.invoke();
        AllDevicesMovementBehaviors.invoke();
        AllDeviceSystems.invoke();
    }
}
