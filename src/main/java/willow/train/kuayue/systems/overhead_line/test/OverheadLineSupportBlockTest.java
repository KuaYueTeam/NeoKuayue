package willow.train.kuayue.systems.overhead_line.test;

import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.systems.overhead_line.OverheadLineSystem;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlock;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlockEntity;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlockReg;

public class OverheadLineSupportBlockTest {
    public static OverheadLineSupportBlockReg<OverheadLineSupportBlock> BLOCK =
            new OverheadLineSupportBlockReg<>("overhead_line_support_block")
                    .blockType(OverheadLineSupportBlock::new)
                    .withBlockEntity(OverheadLineSystem.OVERHEAD_LINE_SUPPORT_BLOCK_ENTITY)
                    .defaultBlockItem()
                    .submit(AllElements.testRegistry);

    public static void invoke(){}
}
