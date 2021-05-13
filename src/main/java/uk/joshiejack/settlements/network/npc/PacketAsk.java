package uk.joshiejack.settlements.network.npc;

import uk.joshiejack.settlements.client.gui.GuiNPCAsk;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.chat.ActionAsk;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@PenguinLoader(side = Side.CLIENT)
public class PacketAsk extends PacketButtonLoad<ActionAsk> {
    public PacketAsk() {}
    public PacketAsk(EntityPlayer player, EntityNPC npc, ActionAsk action) {
        super(player, npc, action);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handlePacket(EntityPlayer player) {
        super.handlePacket(player);
        Entity entity = player.world.getEntityByID(npcID);
        if (entity instanceof EntityNPC) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiNPCAsk((EntityNPC)entity, action.registryName, action.isQuest, action.question, action.answers, action.formatting));
        }
    }
}
