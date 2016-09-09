package joshie.harvest.calendar.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.calendar.render.CalendarHUD;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

@Packet(Side.CLIENT)
public class PacketEdit extends PenguinPacket {
    private boolean gold;

    public PacketEdit() {}
    public PacketEdit(boolean gold) {
        this.gold = gold;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(gold);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        gold = buf.readBoolean();
    }
    
    @Override
    public void handlePacket(EntityPlayer player) {
        if (gold) {
            CalendarHUD.editingCalendar = false;
            CalendarHUD.editingGold = true;
            MCClientHelper.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.GOLD + "Open a gui to edit the gold hud position"));
            MCClientHelper.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.GOLD + "   WASD/Arrows to move around"));
            MCClientHelper.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.GOLD + "   H to Hide/Show the textures"));
            MCClientHelper.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.GOLD + "   Enter to Finish Editing"));
        } else {
            CalendarHUD.editingCalendar = true;
            CalendarHUD.editingGold = false;
            MCClientHelper.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.GOLD + "Open a gui to edit the date hud position"));
            MCClientHelper.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.GOLD + "   WASD/Arrows to move around"));
            MCClientHelper.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.GOLD + "   H to Hide/Show the textures"));
            MCClientHelper.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.GOLD + "   Enter to Finish Editing"));
        }
    }
}