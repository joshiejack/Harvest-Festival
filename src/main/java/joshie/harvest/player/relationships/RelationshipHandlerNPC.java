package joshie.harvest.player.relationships;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import net.minecraft.nbt.NBTTagCompound;
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

    private INPC npc;

    @Override
    public void toBytes(IRelatable relatable, ByteBuf buf) {
        npc = (INPC) relatable;
        ByteBufUtils.writeUTF8String(buf, npc.getUnlocalizedName());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        npc = HFApi.NPC.get(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public IRelatable onMessage(boolean particles) {
        return npc;
    }

    @Override
    public IRelatable readFromNBT(NBTTagCompound tag) {
        return HFApi.NPC.get(tag.getString("NPC"));
    }

    @Override
    public void writeToNBT(IRelatable relatable, NBTTagCompound tag) {
        tag.setString("NPC", ((INPC) relatable).getUnlocalizedName());
    }
}
