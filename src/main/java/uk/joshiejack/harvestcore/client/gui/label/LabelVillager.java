package uk.joshiejack.harvestcore.client.gui.label;

import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class LabelVillager extends LabelBook {
    private final long birthday;
    private final ItemStack villager;
    private final String name;

    public LabelVillager(GuiBook gui, int x, int y, UUID member) {
        super(gui, x, y);
        this.width = 16;
        this.height = 16;
        this.villager = AdventureItems.NPC_SPAWNER.withPlayerSkin(member);
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(gui.mc.world, member);
        this.name = player != null ? player.getName() : UsernameCache.getLastKnownUsername(member);
        if (player != null) {
            this.birthday = player.getEntityData().getCompoundTag(PenguinLib.MOD_ID).getLong("Spawnday");
        } else {
            this.birthday = -1;
        }
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            drawStack(villager, 0, 0, 1F);

            boolean hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            if (hovered) {
                gui.addTooltip(TextFormatting.WHITE + name);
                if (birthday != -1) {
                    gui.addTooltip(LabelTownInfo.formatter.format(birthday));
                }
            }

            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT); //Reset the rendering
        }
    }
}
