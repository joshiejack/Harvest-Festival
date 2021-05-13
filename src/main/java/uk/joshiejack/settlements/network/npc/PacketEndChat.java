package uk.joshiejack.settlements.network.npc;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.Action;
import uk.joshiejack.settlements.entity.ai.action.ActionChat;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketEndChat extends PenguinPacket {
    private int npcID;

    public PacketEndChat() {}
    public PacketEndChat(int npcID) {
        this.npcID = npcID;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(npcID);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        npcID = buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Entity entity = player.world.getEntityByID(npcID);
        if (entity instanceof EntityNPC) {
            Action action = ((EntityNPC) entity).getMentalAI().getCurrent();
            if (action instanceof ActionChat) {
                (((EntityNPC)entity)).removeTalking(player);
                ((ActionChat)action).onGuiClosed(player, (EntityNPC) entity);
            }
        }
    }
}
