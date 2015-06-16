package joshie.harvest.relations.data;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;

public class DataHandlerNPC implements IRelatableDataHandler {
    @Override
    public String name() {
        return "npc";
    }

    @Override
    public IRelatableDataHandler copy() {
        return new DataHandlerNPC();
    }

    private INPC npc;

    @Override
    public void toBytes(IRelatable relatable, ByteBuf buf) {
        npc = ((EntityNPC) relatable).getNPC();
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
