package uk.joshiejack.settlements.client.gui.button;

import uk.joshiejack.settlements.client.gui.GuiJournal;
import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.settlements.network.town.people.PacketToggleCitizenship;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.people.Citizenship;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ButtonCitizenshipType extends ButtonBook {
    private final ItemStack player;
    private final int dimension, id;
    private Citizenship citizenship;

    public ButtonCitizenshipType(GuiBook gui, Town<?> town, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.enabled = PenguinTeamsClient.getInstance().getID().equals(town.getCharter().getTeamID()); //If we are this team
        this.citizenship = town.getGovernment().getCitizenship();
        this.width = 16;
        this.height = 16;
        this.dimension = gui.mc.player.world.provider.getDimension();
        this.id = town.getID();
        player = new ItemStack(AdventureItems.NPC_SPAWNER);
        player.setTagCompound(new NBTTagCompound());
        List<String> players = gui.mc.world.playerEntities.stream().map(EntityPlayer::getName).collect(Collectors.toList());
        Collections.shuffle(players);
        assert player.getTagCompound() != null;
        player.getTagCompound().setString("Player", players.get(0));
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            boolean flag = mc.fontRenderer.getUnicodeFlag();
            //
            mc.fontRenderer.drawString(citizenship.getType(), x - 110, y, gui.fontColor1); //TODO: Allow editing of town name if player is town owner
            mc.fontRenderer.setUnicodeFlag(true);
            mc.fontRenderer.drawSplitString(mc.fontRenderer.listFormattedStringToWidth(citizenship.getDescription(), 120).get(0), x - 110, y + 8,  120, gui.fontColor2);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(GuiJournal.ICONS); //Elements
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            //drawTexturedModalRect(x, y, hovered && enabled ? 16 : 0, 16, 16, 16);
            if (hovered) {
                gui.addTooltip(citizenship.getType());
                gui.addTooltip(TextFormatting.AQUA + citizenship.getName());
                gui.addTooltip(mc.fontRenderer.listFormattedStringToWidth(TextFormatting.GRAY + " " + TextFormatting.ITALIC + citizenship.getTooltip(), 128));
                GlStateManager.color(0.5F, 0.5F, 0.5F);

            }

            //Draw the x/tick

            drawStack(player, 0, 0, 1F);
            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT); //Reset the rendering /DEPTH buffer
            mc.getTextureManager().bindTexture(GuiJournal.ICONS);
            drawTexturedModalRect(x + 1, y + 1, (citizenship.ordinal() * 16), 80, 16, 16);
            //if (enacted) drawTexturedModalRect(x + 5, y + 8, 31, 248, 10, 8);
            //else drawTexturedModalRect(x + 6, y + 8, 41, 248, 7, 8);

            GlStateManager.color(1.0F, 1.0F, 1.0F);
            mc.fontRenderer.setUnicodeFlag(flag);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        citizenship = citizenship.next();
        PenguinNetwork.sendToServer(new PacketToggleCitizenship(dimension, id));
    }
}
