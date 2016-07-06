package joshie.harvest.player.relationships;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class RelationshipHandlerNPC implements IRelatableDataHandler<NPC> {
    @Override
    public String name() {
        return "npc";
    }

    @Override
    public IRelatableDataHandler copy() {
        return new RelationshipHandlerNPC();
    }

    @Override
    public void toBytes(NPC relatable, ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, relatable.getRegistryName().toString());
    }

    @Override
    public NPC fromBytes(ByteBuf buf) {
        return NPCRegistry.REGISTRY.getObject(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
    }

    @Override
    public NPC readFromNBT(NBTTagCompound tag) {
        return NPCRegistry.REGISTRY.getObject(new ResourceLocation(tag.getString("NPC")));
    }

    @Override
    public void writeToNBT(NPC npc, NBTTagCompound tag) {
        tag.setString("NPC", npc.getRegistryName().toString());
    }
}
