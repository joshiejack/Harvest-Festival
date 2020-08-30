package joshie.harvest.api.npc.greeting;

import joshie.harvest.api.npc.IInfoButton;
import joshie.harvest.api.npc.NPC;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GreetingShop implements IInfoButton {
    private static final ResourceLocation ICONS = new ResourceLocation("harvestfestival", "textures/gui/icons.png");
    protected final String text;

    public GreetingShop(String text) {
        this.text = text;
    }

    public GreetingShop(ResourceLocation resourceLocation) {
        this.text = resourceLocation.getNamespace() + ".npc." + resourceLocation.getPath() + ".shop";
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, NPC npc) {
        return I18n.translateToLocal(text);
    }

    @Override
    public void drawIcon(GuiScreen gui, int x, int y) {
        gui.mc.renderEngine.bindTexture(ICONS);
        gui.drawTexturedModalRect(x, y, 16, 0, 16, 16);
    }

    @SideOnly(Side.CLIENT)
    public String getTooltip() {
        return "harvestfestival.npc.tooltip.clock";
    }
}