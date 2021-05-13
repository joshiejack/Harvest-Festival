package uk.joshiejack.settlements.network.npc;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.client.gui.GuiNPC;
import uk.joshiejack.settlements.client.gui.NPCButtons;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.Action;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.List;

public class PacketButtonLoad<A extends Action> extends PenguinPacket {
    private List<NPCButtons.ButtonData> list;
    protected A action;
    protected int npcID;

    public PacketButtonLoad() {}
    public PacketButtonLoad(EntityPlayer player, EntityNPC npc, A action) {
        this.npcID = npc.getEntityId();
        this.action = action;
        this.list = NPCButtons.getForDisplay(npc, player);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, action.getType());
        ByteBufUtils.writeTag(buf, action.serializeNBT());
        buf.writeInt(npcID);
        buf.writeByte(list.size());
        list.forEach(b-> b.toBytes(buf));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fromBytes(ByteBuf buf) {
        action = (A) Action.createOfType(ByteBufUtils.readUTF8String(buf));
        action.deserializeNBT(ByteBufUtils.readTag(buf));
        npcID = buf.readInt();
        list = Lists.newArrayList();
        int count = buf.readByte();
        for (int i = 0; i < count; i++) {
            list.add(new NPCButtons.ButtonData(buf));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        GuiNPC.setButtons(list);
    }
}
