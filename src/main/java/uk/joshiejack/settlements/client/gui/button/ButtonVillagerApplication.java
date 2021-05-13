package uk.joshiejack.settlements.client.gui.button;

import uk.joshiejack.settlements.client.gui.GuiJournal;
import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.settlements.network.town.people.PacketAcceptCitizenship;
import uk.joshiejack.settlements.network.town.people.PacketRejectCitizenship;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
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
public class ButtonVillagerApplication extends ButtonBook {
    private final ItemStack villager;
    private final String name;
    private final int dimension, town;
    private final UUID member;

    public ButtonVillagerApplication(GuiBook gui, int buttonId, int x, int y, int dimension, int town, UUID member) {
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
            drawStack(villager, 0, 0, 1F);

            boolean hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            if (hovered) {
                gui.addTooltip(StringHelper.format("settlements.message.apply.name", TextFormatting.AQUA, name, TextFormatting.RESET));
                gui.addTooltip(StringHelper.format("settlements.message.apply.accept", TextFormatting.GREEN));
                gui.addTooltip(StringHelper.format("settlements.message.apply.reject", TextFormatting.AQUA, TextFormatting.RESET, TextFormatting.RED));
            }

            //Change the icon to an X if we are holding shift

            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT); //Reset the rendering
            if (hovered && GuiScreen.isShiftKeyDown()) {
                mc.getTextureManager().bindTexture(gui.left); //Elements
                drawTexturedModalRect(x + 7, y + 7, 58, 248, 7, 8);
            } else {
                mc.getTextureManager().bindTexture(GuiJournal.ICONS); //Elements
                drawTexturedModalRect(x, y, hovered && enabled ? 0 : 32, 64, 16, 16);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (GuiScreen.isShiftKeyDown()) {
            PenguinNetwork.sendToServer(new PacketRejectCitizenship(dimension, town, member));
        } else PenguinNetwork.sendToServer(new PacketAcceptCitizenship(dimension, town, member));

        gui.setPage(gui.getPage());
    }
}
