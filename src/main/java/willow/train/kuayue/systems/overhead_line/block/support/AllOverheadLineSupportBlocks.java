package willow.train.kuayue.systems.overhead_line.block.support;

import kasuga.lib.registrations.common.BlockEntityReg;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.ChunkDataEvent;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.systems.overhead_line.OverheadLineSystem;
import willow.train.kuayue.systems.overhead_line.block.support.variants.*;
import willow.train.kuayue.systems.overhead_line.wire.AllWires;

public class AllOverheadLineSupportBlocks {

    public static BlockEntityReg<OverheadLineEndWeightBlockEntity> OVERHEAD_LINE_END_WEIGHT_BLOCK_ENTITY =
            new BlockEntityReg<OverheadLineEndWeightBlockEntity>("overhead_line_end_weight_block_entity")
                    .blockEntityType(OverheadLineEndWeightBlockEntity::new)
                    .blockPredicates((r, i)->i instanceof OverheadLineSupportBlock)
                    .withRenderer(()->OverheadSupportBlockRenderer::new)
                    .submit(AllElements.testRegistry);

    public static OverheadLineSupportBlockReg<OverheadLineSupportBlock<OverheadLineSupportBlockEntity>,OverheadLineSupportBlockEntity> OVERHEAD_LINE_SUPPORT_A1 =
            new OverheadLineSupportBlockReg<>("overhead_line_support_a1")
                    .blockType(NormalOverheadLineSupportBlock::new)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueOverheadLineTab)
                    .withRenderer(()->OverheadLineSupportARenderer.A1Renderer::new)
                    .connectionPoints(
                            new Vec3(1.55, .125, 0)
                    )
                    .addAllowedWireType(
                            AllWires.OVERHEAD_LINE_WIRE,
                            AllWires.ELECTRONIC_WIRE
                    )
                    .submit(AllElements.testRegistry);

    public static OverheadLineSupportBlockReg<OverheadLineSupportBlock<OverheadLineSupportBlockEntity>,OverheadLineSupportBlockEntity> OVERHEAD_LINE_SUPPORT_A2 =
            new OverheadLineSupportBlockReg<>("overhead_line_support_a2")
                    .blockType(NormalOverheadLineSupportBlock::new)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueOverheadLineTab)
                    .withRenderer(()->OverheadLineSupportARenderer.A2Renderer::new)
                    .connectionPoints(
                            new Vec3(2.25, .125, 0)
                    )
                    .addAllowedWireType(
                            AllWires.OVERHEAD_LINE_WIRE,
                            AllWires.ELECTRONIC_WIRE
                    )
                    .submit(AllElements.testRegistry);

    public static OverheadLineSupportBlockReg<OverheadLineSupportBlock<OverheadLineSupportBlockEntity>,OverheadLineSupportBlockEntity> OVERHEAD_LINE_SUPPORT_B =
            new OverheadLineSupportBlockReg<>("overhead_line_support_b")
                    .blockType(NormalOverheadLineSupportBlock::new)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueOverheadLineTab)
                    .withRenderer(()-> OverheadLineSupportBRenderer.B1Renderer::new)
                    .connectionPoints(
                            new Vec3(1.55, .125, 0)
                    )
                    .addAllowedWireType(
                            AllWires.OVERHEAD_LINE_WIRE,
                            AllWires.ELECTRONIC_WIRE
                    )
                    .submit(AllElements.testRegistry);

    public static OverheadLineSupportBlockReg<OverheadLineSupportBlock<OverheadLineSupportBlockEntity>,OverheadLineSupportBlockEntity> OVERHEAD_LINE_SUPPORT_B2 =
            new OverheadLineSupportBlockReg<>("overhead_line_support_b2")
                    .blockType(NormalOverheadLineSupportBlock::new)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueOverheadLineTab)
                    .withRenderer(()-> OverheadLineSupportBRenderer.B2Renderer::new)
                    .connectionPoints(
                            new Vec3(2.25, .125, 0)
                    )
                    .addAllowedWireType(
                            AllWires.OVERHEAD_LINE_WIRE,
                            AllWires.ELECTRONIC_WIRE
                    )
                    .submit(AllElements.testRegistry);

    public static OverheadLineSupportBlockReg<OverheadLineSupportBlock<OverheadLineSupportBlockEntity>,OverheadLineSupportBlockEntity> OVERHEAD_LINE_SUPPORT_C =
            new OverheadLineSupportBlockReg<>("overhead_line_support_c")
                    .blockType(NormalOverheadLineSupportBlock::new)
                    .withItem((b, p)->new CustomRendererItem(b, p).withRenderer(()->OverheadLineSupportCRenderer.C1ItemRenderer::new), AllElements.testRegistry.asResource("dynamic_renderer_item/overhead_line_support_c1"))
                    .tabTo(AllElements.neoKuayueOverheadLineTab)
                    .withRenderer(()-> OverheadLineSupportCRenderer.C1Renderer::new)
                    .connectionPoints(
                            new Vec3(1.55, .125, - .52),
                            new Vec3(2.25, .125, .52)
                    )
                    .addAllowedWireType(
                            AllWires.OVERHEAD_LINE_WIRE,
                            AllWires.ELECTRONIC_WIRE
                    )
                    .shouldCustomRenderItem(true)
                    .submit(AllElements.testRegistry);

    public static OverheadLineSupportBlockReg<OverheadLineSupportBlock<OverheadLineSupportBlockEntity>,OverheadLineSupportBlockEntity> OVERHEAD_LINE_SUPPORT_C2 =
            new OverheadLineSupportBlockReg<>("overhead_line_support_c2")
                    .blockType(NormalOverheadLineSupportBlock::new)
                    .withItem((b, p)->new CustomRendererItem(b, p).withRenderer(()->OverheadLineSupportCRenderer.C2ItemRenderer::new), AllElements.testRegistry.asResource("dynamic_renderer_item/overhead_line_support_c2"))
                    .tabTo(AllElements.neoKuayueOverheadLineTab)
                    .withRenderer(()-> OverheadLineSupportCRenderer.C2Renderer::new)
                    .connectionPoints(
                            new Vec3(1.55, .125, .52),
                            new Vec3(2.25, .125, -.52)
                    )
                    .addAllowedWireType(
                            AllWires.OVERHEAD_LINE_WIRE,
                            AllWires.ELECTRONIC_WIRE
                    )
                    .shouldCustomRenderItem(true)
                    .submit(AllElements.testRegistry);

    public static OverheadLineSupportBlockReg<OverheadLineSupportBlock<OverheadLineSupportBlockEntity>,OverheadLineSupportBlockEntity> OVERHEAD_LINE_INSULATOR_A =
            new OverheadLineSupportBlockReg<>("overhead_line_insulator_a")
                    .blockType(OverheadLineSupportInsulatorBlock::new)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueOverheadLineTab)
                    .withRenderer(()-> OverheadLineInsulatorRenderer.ARenderer::new)
                    .withConnectionPointBuilder(OverheadLineInsulatorRenderer.ARenderer::getConnectionPointIf)
                    .addAllowedWireType(
                            AllWires.ELECTRONIC_WIRE
                    )
                    .submit(AllElements.testRegistry);

    public static OverheadLineSupportBlockReg<OverheadLineSupportBlock<OverheadLineSupportBlockEntity>,OverheadLineSupportBlockEntity> OVERHEAD_LINE_INSULATOR_B =
            new OverheadLineSupportBlockReg<>("overhead_line_insulator_b")
                    .blockType(OverheadLineSupportInsulatorBlock::new)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueOverheadLineTab)
                    .withRenderer(()-> OverheadLineInsulatorRenderer.BRenderer::new)
                    .withConnectionPointBuilder(OverheadLineInsulatorRenderer.BRenderer::getConnectionPointIf)
                    .addAllowedWireType(
                            AllWires.ELECTRONIC_WIRE
                    )
                    .submit(AllElements.testRegistry);

    public static OverheadLineSupportBlockReg<OverheadLineEndWeightBlock, OverheadLineEndWeightBlockEntity> OVERHEAD_LINE_END_WEIGHT =
            new OverheadLineSupportBlockReg<OverheadLineEndWeightBlock, OverheadLineEndWeightBlockEntity>("overhead_line_end_weight")
                    .blockType(OverheadLineEndWeightBlock::new)
                    .withBlockEntity(OVERHEAD_LINE_END_WEIGHT_BLOCK_ENTITY)
                    .defaultBlockItem()
                    .tabTo(AllElements.neoKuayueOverheadLineTab)
                    .withRenderer(()-> OverheadLineEndCounterWeightRenderer::new)
                    .addAllowedWireType(
                            AllWires.OVERHEAD_LINE_WIRE,
                            AllWires.ELECTRONIC_WIRE
                    )
                    .withConnectionPointBuilder(OverheadLineEndCounterWeightRenderer::getConnectionPointIf)
                    .submit(AllElements.testRegistry);
    public static void invoke(){

    }
}
