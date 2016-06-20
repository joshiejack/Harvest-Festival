package joshie.harvest.player.relationships;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class RelationshipHandlerNPC implements IRelatableDataHandler {
    @Override
    public String name() {
        return "npc";
    }

    @Override
    public IRelatableDataHandler copy() {
        return new RelationshipHandlerNPC();
    }

    private NPC npc;

    @Override
    public void toBytes(IRelatable relatable, ByteBuf buf) {
        npc = (NPC) relatable;
        ByteBufUtils.writeUTF8String(buf, npc.getRegistryName().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        npc = NPCRegistry.REGISTRY.getObject(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
    }

    @Override
    public IRelatable onMessage(boolean particles) {
        return npc;
    }

    @Override
    public IRelatable readFromNBT(NBTTagCompound tag) {
        return NPCRegistry.REGISTRY.getObject(new ResourceLocation(tag.getString("NPC")));
    }

    @Override
    public void writeToNBT(IRelatable relatable, NBTTagCompound tag) {
        tag.setString("NPC", ((NPC) relatable).getRegistryName().toString());
    }
}
