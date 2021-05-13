package uk.joshiejack.settlements.network.npc;

import uk.joshiejack.settlements.client.gui.GuiNPCAsk;
import uk.joshiejack.settlements.client.gui.GuiNPCChat;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.chat.ActionSay;
import uk.joshiejack.penguinlib.client.gui.Chatter;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@PenguinLoader(side = Side.CLIENT)
public class PacketSay extends PacketButtonLoad<ActionSay> {
    public PacketSay() {}
    public PacketSay(EntityPlayer player, EntityNPC npc, ActionSay action) {
        super(player, npc, action);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handlePacket(EntityPlayer player) {
        super.handlePacket(player); //Super first!
        Entity entity = player.world.getEntityByID(npcID);
        if (entity instanceof EntityNPC) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiNPCChat((EntityNPC)entity, new Chatter(GuiNPCAsk.modify(action.registryName, action.isQuest, action.text)), action.formatting));
        }
    }
}
