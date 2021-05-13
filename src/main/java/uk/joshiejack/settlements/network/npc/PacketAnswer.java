package uk.joshiejack.settlements.network.npc;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.Action;
import uk.joshiejack.settlements.entity.ai.action.chat.ActionAsk;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketAnswer extends PenguinPacket {
    private int npcID;
    private ResourceLocation registryName;
    private boolean isQuest;
    private int option;

    public PacketAnswer() {}
    public PacketAnswer(int npcID, ResourceLocation registryName, boolean isQuest, int option) {
        this.npcID = npcID;
        this.registryName = registryName;
        this.isQuest = isQuest;
        this.option = option;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(npcID);
        ByteBufUtils.writeUTF8String(buf, registryName.toString());
        buf.writeBoolean(isQuest);
        buf.writeByte(option);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        npcID = buf.readInt();
        registryName = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        isQuest = buf.readBoolean();
        option = buf.readByte();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Entity entity = player.world.getEntityByID(npcID);
        if (entity instanceof EntityNPC) {
            Action action = ((EntityNPC) entity).getMentalAI().getCurrent();
            (((EntityNPC)entity)).removeTalking(player);
            if (action instanceof ActionAsk) {
                ((ActionAsk)action).onGuiClosed(player, (EntityNPC) entity, option);
            }
        }
    }
}
