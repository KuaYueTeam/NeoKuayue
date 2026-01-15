package willow.train.kuayue.initial.compat.railways;

import com.railwayteam.railways.content.buffer.headstock.CopycatHeadstockBlock;
import com.railwayteam.railways.content.buffer.headstock.HeadstockBlock;
import com.railwayteam.railways.content.buffer.headstock.HeadstockStyle;
import com.railwayteam.railways.content.buffer.single_deco.LinkPinBlock;
import com.railwayteam.railways.mixin_interfaces.IIndexedSchedule;
import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.world.level.block.Block;
import willow.train.kuayue.systems.train_extension.conductor.providers.*;
import willow.train.kuayue.systems.train_extension.conductor.registry.ConductorCandidateRegistry;

public class RailwayCompatImpl implements RailwayCompat{
    public RailwayCompatImpl(){
    }
    public RailwayCompatImpl(RailwayCompatImpl r){}

    @Override
    public void registerConductors() {
        //link
        ConductorCandidateRegistry.registerBlockState(
                state -> {
                    Block block = state.getBlock();
                    if(block instanceof LinkPinBlock && state.getValue(LinkPinBlock.STYLE).equals(LinkPinBlock.Style.LINK)) return true;
                    if(block instanceof HeadstockBlock && state.getValue(HeadstockBlock.STYLE).equals(HeadstockStyle.LINK)) return true;
                    if(block instanceof CopycatHeadstockBlock && state.getValue(CopycatHeadstockBlock.STYLE).equals(HeadstockStyle.LINK)) return true;
                    return false;
                },
                LinkConductorProvider.INSTANCE
        );

        //linkless
        ConductorCandidateRegistry.registerBlockState(
                state -> {
                    Block block = state.getBlock();
                    if(block instanceof LinkPinBlock && state.getValue(LinkPinBlock.STYLE).equals(LinkPinBlock.Style.LINKLESS)) return true;
                    if(block instanceof HeadstockBlock && state.getValue(HeadstockBlock.STYLE).equals(HeadstockStyle.LINKLESS)) return true;
                    if(block instanceof CopycatHeadstockBlock && state.getValue(CopycatHeadstockBlock.STYLE).equals(HeadstockStyle.LINKLESS)) return true;
                    return false;
                },
                LinklessConductorProvider.INSTANCE
        );

        //jan
        ConductorCandidateRegistry.registerBlockState(
                state -> {
                    Block block = state.getBlock();
                    if(block instanceof LinkPinBlock && (
                            state.getValue(LinkPinBlock.STYLE).equals(LinkPinBlock.Style.KNUCKLE) ||
                            state.getValue(LinkPinBlock.STYLE).equals(LinkPinBlock.Style.KNUCKLE_SPLIT)
                            )) return true;
                    if(block instanceof HeadstockBlock && (
                            state.getValue(HeadstockBlock.STYLE).equals(HeadstockStyle.KNUCKLE) ||
                            state.getValue(HeadstockBlock.STYLE).equals(HeadstockStyle.KNUCKLE_SPLIT)
                            )) return true;
                    if(block instanceof CopycatHeadstockBlock && (
                            state.getValue(CopycatHeadstockBlock.STYLE).equals(HeadstockStyle.KNUCKLE) ||
                            state.getValue(CopycatHeadstockBlock.STYLE).equals(HeadstockStyle.KNUCKLE_SPLIT)
                    )) return true;
                    return false;
                },
                JanConductorProvider.INSTANCE
        );

        //screw link
        ConductorCandidateRegistry.registerBlockState(
                state -> {
                    Block block = state.getBlock();
                    if(block instanceof LinkPinBlock && state.getValue(LinkPinBlock.STYLE).equals(LinkPinBlock.Style.SCREWLINK)) return true;
                    if(block instanceof HeadstockBlock && state.getValue(HeadstockBlock.STYLE).equals(HeadstockStyle.SCREWLINK)) return true;
                    if(block instanceof CopycatHeadstockBlock && state.getValue(CopycatHeadstockBlock.STYLE).equals(HeadstockStyle.SCREWLINK)) return true;
                    return false;
                },
                ScrewLinkConductorProvider.INSTANCE
        );
    }

    @Override
    public void attachScheduleToCarriage(Train train, int carriageIndex) {
        if(train == null) return;

        IIndexedSchedule indexedSchedule = getIndexedSchedule(train);
        if(indexedSchedule != null) {
            indexedSchedule.railways$setIndex(carriageIndex);
        }
    }

    @Override
    public int getScheduleOwner(Train train) {
        if(train == null) return -1;

        IIndexedSchedule indexedSchedule = getIndexedSchedule(train);
        if(indexedSchedule != null) {
            return indexedSchedule.railways$getIndex();
        }
        return -1;
    }

    @Override
    public void detachSchedule(Train train) {
        if(train == null) return;

        IIndexedSchedule indexedSchedule = getIndexedSchedule(train);
        if(indexedSchedule != null) {
            indexedSchedule.railways$setIndex(-1);
        }
    }

    @Override
    public boolean isScheduleAttached(Train train) {
        return getScheduleOwner(train) != -1;
    }

    @Override
    public void transferSchedule(Train from, Train to, int targetCarriageIndex) {
        if(from == null || to == null) return;

        to.runtime.read(from.runtime.write());
        attachScheduleToCarriage(to, targetCarriageIndex);
        detachSchedule(from);
    }

    private IIndexedSchedule getIndexedSchedule(Train train) {
        if (train instanceof IIndexedSchedule indexedSchedule) {
            return indexedSchedule;
        }
        return null;
    }
}
