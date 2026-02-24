package willow.train.kuayue.initial.create;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import kasuga.lib.core.create.SimpleTrackBlock;
import kasuga.lib.registrations.create.TrackMaterialReg;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import willow.train.kuayue.initial.AllElements;

import static willow.train.kuayue.initial.create.AllLocoBogeys.testRegistry;

public class AllTrackMaterial {

    public static final SimpleTrackBlock.Builder builder =
            new SimpleTrackBlock.Builder(() -> AllCarriageBogeys.carriageBlockBundle.getElement("pk209p_bogey").getEntry().get());

    public static final SimpleTrackBlock.Builder meterBuilder =
            new SimpleTrackBlock.Builder(() -> AllCarriageBogeys.meterCarriageBlockBundle.getElement("mkz_bogey").getEntry().get());

    public static final TrackMaterialReg standardMaterial = new TrackMaterialReg("standard")
            .lang("standard_track")
            .block(() -> AllTracks.standardTrack)
            .trackParticle(new ResourceLocation("minecraft", "block/palettes/stone_types/polished/andesite_cut_polished"))
            .type(AllElements.testRegistry.asResource("standard"), builder::build)
            .customModel(
                    () -> PartialModel.of(AllElements.testRegistry.asResource("block/track/standard/tie")),
                    () -> PartialModel.of(AllElements.testRegistry.asResource("block/track/standard/segment_left")),
                    () -> PartialModel.of(AllElements.testRegistry.asResource("block/track/standard/segment_right"))
            )
            .simpleTrackModelOffset(0.755f)
            .sleeper(Blocks.ANDESITE_SLAB)
            .submit(AllElements.testRegistry);

    public static final TrackMaterialReg tielessMaterial = new TrackMaterialReg("tieless")
            .lang("tieless_track")
            .block(() -> AllTracks.tielessTrack)
            .trackParticle(new ResourceLocation("minecraft", "block/palettes/stone_types/polished/andesite_cut_polished"))
            .type(AllElements.testRegistry.asResource("tieless"), builder::build)
            .customModel(
                    () -> PartialModel.of(new ResourceLocation("kasuga_lib", "empty_model")),
                    () -> PartialModel.of(AllElements.testRegistry.asResource("block/track/standard/segment_left")),
                    () -> PartialModel.of(AllElements.testRegistry.asResource("block/track/standard/segment_right"))
            )
            .simpleTrackModelOffset(0.755f)
            .sleeper(Blocks.ANDESITE_SLAB)
            .submit(AllElements.testRegistry);

    public static final TrackMaterialReg ballastlessMaterial = new TrackMaterialReg("ballastless")
            .lang("ballastless_track")
            .block(() -> AllTracks.ballastlessTrack)
            .trackParticle(new ResourceLocation("minecraft", "block/palettes/stone_types/polished/andesite_cut_polished"))
            .type(AllElements.testRegistry.asResource("ballastless"), builder::build)
            .customModel(
                    () -> PartialModel.of(AllElements.testRegistry.asResource("block/track/ballastless/tie")),
                    () -> PartialModel.of(AllElements.testRegistry.asResource("block/track/standard/segment_left")),
                    () -> PartialModel.of(AllElements.testRegistry.asResource("block/track/standard/segment_right"))
            )
            .simpleTrackModelOffset(0.755f)
            .sleeper(Blocks.ANDESITE_SLAB)
            .submit(AllElements.testRegistry);

    public static final TrackMaterialReg meterMaterial = new TrackMaterialReg("meter")
            .lang("meter_track")
            .block(() -> AllTracks.meterTrack)
            .trackParticle(new ResourceLocation("minecraft", "block/palettes/stone_types/polished/andesite_cut_polished"))
            .type(AllElements.testRegistry.asResource("meter"), meterBuilder::build)
            .customModel(
                    () -> PartialModel.of(AllElements.testRegistry.asResource("block/track/meter/tie")),
                    () -> PartialModel.of(AllElements.testRegistry.asResource("block/track/standard/segment_left")),
                    () -> PartialModel.of(AllElements.testRegistry.asResource("block/track/standard/segment_right"))
            )
            .simpleTrackModelOffset(0.525f)
            .sleeper(Blocks.ANDESITE_SLAB)
            .submit(AllElements.testRegistry);

    public static final TrackMaterialReg guardMaterial = new TrackMaterialReg("guard")
            .lang("guard_track")
            .block(() -> AllTracks.guardTrack)
            .trackParticle(new ResourceLocation("minecraft", "block/palettes/stone_types/polished/andesite_cut_polished"))
            .type(testRegistry.asResource("guard"), builder::build)
            .customModel(
                    () -> PartialModel.of(testRegistry.asResource("block/track/guard/tie")),
                    () -> PartialModel.of(testRegistry.asResource("block/track/guard/segment_left")),
                    () -> PartialModel.of(testRegistry.asResource("block/track/guard/segment_right"))
            )
            .simpleTrackModelOffset(0.755f)
            .sleeper(Blocks.ANDESITE_SLAB)
            .submit(testRegistry);



    public static void invoke(){}
}
