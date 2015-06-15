package joshie.harvest.relations.data;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.relations.IDataHandler;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;

public class DataHandlerNPC implements IDataHandler {
    @Override
    public String name() {
        return "npc";
    }

    @Override
    public IDataHandler copy() {
        return new DataHandlerNPC();
    }

    private INPC npc;
    private boolean displayParticles;

    @Override
    public void toBytes(IRelatable relatable, ByteBuf buf, Object... data) {
        npc = ((EntityNPC) relatable).getNPC();
        ByteBufUtils.writeUTF8String(buf, npc.getUnlocalizedName());
        buf.writeBoolean((Boolean) data[0]);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        npc = HFApi.NPC.get(ByteBufUtils.readUTF8String(buf));
        displayParticles = buf.readBoolean();
    }

    @Override
    public IRelatable onMessage() {
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
