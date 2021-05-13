package uk.joshiejack.harvestcore.registry.letter;

import uk.joshiejack.economy.api.EconomyAPI;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LetterGold extends Letter {
    private long amount;

    public LetterGold(ResourceLocation resource, String text, boolean repeatable, int delay, long amount) {
        super(resource, text, repeatable, delay);
        this.amount = amount;
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    @Override
    public void renderLetter(GuiScreen gui, FontRenderer font, int x, int y, int mouseX, int mouseY) {
        super.renderLetter(gui, font, x, y, mouseX, mouseY);
        //StackRenderHelper.drawStack(getStack(), 45, 100, 5F);
        //TODO: DRAW GOLD AMOUNT!!!!!!!!!!!!
    }

    @Override
    public void accept(EntityPlayer player) {
        EconomyAPI.instance.getVaultForPlayer(player.world, player).increaseGold(player.world, amount);
    }
}
