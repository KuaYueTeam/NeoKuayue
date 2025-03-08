package willow.train.kuayue.systems.overhead_line.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public interface IPowerBlock {
    ResourceLocation getPowerNodeType(Integer index);
    Vec3 getNode(Integer index);
}
