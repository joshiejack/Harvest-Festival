package joshie.harvest.npc.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Side.SERVER)
public class PacketInfo extends PenguinPacket {
    private int npcID;

    public PacketInfo() {}
    public PacketInfo(EntityNPC npc) {
        this.npcID = npc.getEntityId();
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeInt(npcID);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        npcID = from.readInt();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handlePacket(EntityPlayer player) {
        EntityNPC npc = (EntityNPC) player.worldObj.getEntityByID(npcID);
        if (npc != null) {
            if(npc.getNPC().onClickedInfoButton(player)) {
                player.openGui(HarvestFestival.instance, GuiHandler.NPC_INFO, player.worldObj, npcID, -1, -1);
            }
        }
    }
}
