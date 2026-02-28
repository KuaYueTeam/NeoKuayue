package willow.train.kuayue.mixins.mixin;

import com.google.common.collect.Multimap;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.MountedStorageManager;
import net.createmod.catnip.math.BlockFace;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(Contraption.class)
public interface AccessorContraption {

    @Accessor("blocks")
    public Map<BlockPos, StructureTemplate.StructureBlockInfo> getBlocks();

    @Accessor("blocks")
    public void setBlocks(Map<BlockPos, StructureTemplate.StructureBlockInfo> blocks);

    @Accessor("interactors")
    public void setInteractors(Map<BlockPos, MovingInteractionBehaviour> interactors);

    @Accessor("superglue")
    public List<AABB> getSuperglue();

    @Accessor("superglue")
    public void setSuperglue(List<AABB> superglue);

    @Accessor("stabilizedSubContraptions")
    public Map<UUID, BlockFace> getStabilizedSubContraptions();

    @Accessor("stabilizedSubContraptions")
    public void setStabilizedSubContraptions(Map<UUID, BlockFace> stabilizedSubContraptions);

    @Accessor("capturedMultiblocks")
    public Multimap<BlockPos, StructureTemplate.StructureBlockInfo> getCapturedMultiblocks();

    @Accessor("capturedMultiblocks")
    public void setCapturedMultiblocks(Multimap<BlockPos, StructureTemplate.StructureBlockInfo> capturedMultiblocks);

    @Accessor("initialPassengers")
    public Map<BlockPos, Entity> getInitialPassengers();

    @Accessor("initialPassengers")
    public void setInitialPassengers(Map<BlockPos, Entity> initialPassengers);

    @Accessor("storage")
    public MountedStorageManager getStorage();
}