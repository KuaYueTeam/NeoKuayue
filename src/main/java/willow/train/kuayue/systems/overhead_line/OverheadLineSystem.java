package willow.train.kuayue.systems.overhead_line;

import com.simibubi.create.content.trains.graph.EdgePointType;
import kasuga.lib.core.create.boundary.BoundarySegmentRegistry;
import kasuga.lib.registrations.common.BlockEntityReg;
import net.minecraft.resources.ResourceLocation;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineEdgePoint;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlock;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlockEntity;
import willow.train.kuayue.systems.overhead_line.test.OverheadLineSupportBlockTest;
import willow.train.kuayue.systems.overhead_line.track.OverheadLineRange;

public class OverheadLineSystem {
    public static EdgePointType<OverheadLineEdgePoint> OVERHEAD_LINE_EDGE_POINT = EdgePointType.register(
            AllElements.testRegistry.asResource("overhead_line_edge_point"),
            OverheadLineEdgePoint::new
    );
    public static ResourceLocation OVERHEAD_LINE = new ResourceLocation("kasuga_lib","overhead_line");

    public static BlockEntityReg<OverheadLineSupportBlockEntity> OVERHEAD_LINE_SUPPORT_BLOCK_ENTITY =
            new BlockEntityReg<OverheadLineSupportBlockEntity>("overhead_line_support_block_entity")
                    .blockEntityType(OverheadLineSupportBlockEntity::new)
                    .blockPredicates((r, i)->i instanceof OverheadLineSupportBlock)
                    .submit(AllElements.testRegistry);
    public static void invoke(){
        BoundarySegmentRegistry.register(
                OVERHEAD_LINE,
                OVERHEAD_LINE_EDGE_POINT,
                OverheadLineRange::new
        );
        OverheadLineSupportBlockTest.invoke();
    }
}
