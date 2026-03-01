package willow.train.kuayue.systems.overhead_line;

import kasuga.lib.registrations.common.BlockEntityReg;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.systems.overhead_line.block.decorating.AllOverheadLineDecoratingBlocks;
import willow.train.kuayue.systems.overhead_line.block.support.AllOverheadLineSupportBlocks;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlock;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadLineSupportBlockEntity;
import willow.train.kuayue.systems.overhead_line.block.support.OverheadSupportBlockRenderer;
import willow.train.kuayue.systems.overhead_line.client.AllOverheadLineMenuScreens;
import willow.train.kuayue.systems.overhead_line.save.OverheadLineSaved;
import willow.train.kuayue.systems.overhead_line.wire.AllWires;

public class OverheadLineSystem {

    public static BlockEntityReg<OverheadLineSupportBlockEntity> OVERHEAD_LINE_SUPPORT_BLOCK_ENTITY =
            new BlockEntityReg<OverheadLineSupportBlockEntity>("overhead_line_support_block_entity")
                    .blockEntityType(OverheadLineSupportBlockEntity::new)
                    .blockPredicates((r, i)->i instanceof OverheadLineSupportBlock)
                    .withRenderer(()->OverheadSupportBlockRenderer::new)
                    .submit(AllElements.testRegistry);
    public static void invoke(){
        //OverheadLineSupportBlockTest.invoke();
        AllOverheadLineSupportBlocks.invoke();
        AllWires.invoke();
        AllOverheadLineDecoratingBlocks.invoke();
        AllOverheadLineMenuScreens.invoke();
    }

    public OverheadLineSaved savedData = new OverheadLineSaved();
}
