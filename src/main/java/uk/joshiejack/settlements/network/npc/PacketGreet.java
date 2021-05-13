package uk.joshiejack.settlements.network.npc;

import uk.joshiejack.settlements.client.gui.GuiNPCChat;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.chat.ActionGreet;
import uk.joshiejack.penguinlib.client.gui.Chatter;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@PenguinLoader(side = Side.CLIENT)
public class PacketGreet extends PacketButtonLoad<ActionGreet> {
    public PacketGreet() {}
    public PacketGreet(EntityPlayer player, EntityNPC npc, ActionGreet action) {
        super(player, npc, action);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handlePacket(EntityPlayer player) {
        super.handlePacket(player);
        Entity entity = player.world.getEntityByID(npcID);
        if (entity instanceof EntityNPC) {
            EntityNPC npc = (EntityNPC) entity;
            Minecraft.getMinecraft().displayGuiScreen(new GuiNPCChat(npc, new Chatter(npc.getInfo().getGreeting(player.world.rand)), player.getDisplayNameString(), entity.getName()));
        }
    }
}
