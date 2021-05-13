package uk.joshiejack.settlements.client.gui.button;

import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.settlements.network.town.people.PacketRevokeCitizenship;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.util.Strings;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class ButtonVillager extends ButtonBook {
    private final ItemStack villager;
    private final String name;
    private final int dimension, town;
    private final UUID member;

    public ButtonVillager(GuiBook gui, int buttonId, int x, int y, int dimension, int town, UUID member) {
        super(gui, buttonId, x, y, Strings.EMPTY);
        this.width = 16;
        this.height = 16;
        this.villager = AdventureItems.NPC_SPAWNER.withPlayerSkin(member);
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(gui.mc.world, member);
        this.name = player != null ? player.getName() : UsernameCache.getLastKnownUsername(member);
        this.dimension = dimension;
        this.town = town;
        this.member = member;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            GlStateManager.enableDepth();
            boolean hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            if (hovered) {
                gui.addTooltip(TextFormatting.AQUA + name);
                UUID playerUUID = PlayerHelper.getUUIDForPlayer(mc.player);
                drawStack(villager, 0, 0, 1F);
                if (PenguinTeamsClient.getInstance().members().contains(playerUUID)) {
                    String type = member.equals(playerUUID) ? "leave" : "kick";
                    gui.addTooltip(StringHelper.format("settlements.message." + type + ".text", TextFormatting.RED));
                    gui.addTooltip(Strings.EMPTY);
                    gui.addTooltip(mc.fontRenderer.listFormattedStringToWidth(TextFormatting.RED + StringHelper.localize("settlements.message." + type + ".warning"), 200));
                    if (member.equals(playerUUID) || playerUUID.equals(PenguinTeamsClient.getInstance().getOwner())) {
                        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT); //Reset the rendering
                        GlStateManager.color(1F, 1F, 1F);
                        mc.getTextureManager().bindTexture(GuiElements.BOOK_LEFT);
                        drawTexturedModalRect(x + 6, y + 8, 58, 248, 7, 8);
                    }
                }
            } else drawStack(villager, 0, 0, 1F);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        UUID playerUUID = PlayerHelper.getUUIDForPlayer(gui.mc.player);
        if (PenguinTeamsClient.getInstance().members().contains(playerUUID) &&
                (member.equals(playerUUID) || playerUUID.equals(PenguinTeamsClient.getInstance().getOwner()))) {
            enabled = false;
            visible = false;
            PenguinNetwork.sendToServer(new PacketRevokeCitizenship(dimension, town, member));
        }
    }
}
