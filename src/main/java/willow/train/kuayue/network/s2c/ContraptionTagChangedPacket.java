package willow.train.kuayue.network.s2c;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import kasuga.lib.core.network.S2CPacket;
import kasuga.lib.registrations.common.ChannelReg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class ContraptionTagChangedPacket extends S2CPacket {
    public final BlockPos localPos;
    public final CompoundTag nbt;
    public final int entity;

    public ContraptionTagChangedPacket(FriendlyByteBuf buf){
        super(buf);
        localPos = buf.readBlockPos();
        nbt = buf.readNbt();
        entity = buf.readInt();
    }

    public ContraptionTagChangedPacket(BlockPos localPos, CompoundTag nbt, int entity) {
        this.localPos = localPos;
        this.nbt = nbt;
        this.entity = entity;
    }

    public static void packet(ChannelReg channel, BlockPos localPos, AbstractContraptionEntity ace, CompoundTag nbt, StructureTemplate.StructureBlockInfo info) {
        Contraption contraption = ace.getContraption();
        if (ace.level().isClientSide) return;
        contraption.getBlocks().put(localPos, new StructureTemplate.StructureBlockInfo(localPos, info.state(), nbt));
        ContraptionTagChangedPacket packet = new ContraptionTagChangedPacket(localPos, nbt, ace.getId());
        channel.boardcastToClients(packet, (ServerLevel) ace.level(), ace.getOnPos());
    }

    @Override
    public void handle(Minecraft minecraft) {
        ClientLevel level = minecraft.level;
        if (level == null) return;
        Entity e = level.getEntity(entity);
        if (!(e instanceof AbstractContraptionEntity ace)) return;
        StructureTemplate.StructureBlockInfo info = ace.getContraption().getBlocks().get(localPos);
        Contraption contraption = ace.getContraption();
        StructureTemplate.StructureBlockInfo newInfo = new StructureTemplate.StructureBlockInfo(localPos, info.state(), nbt);

// 更新数据
        contraption.getBlocks().put(localPos, newInfo);

// 触发 0.6 的更新流
        contraption.invalidateColliders();
    }

    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(localPos);
        friendlyByteBuf.writeNbt(nbt);
        friendlyByteBuf.writeInt(entity);
    }
}
