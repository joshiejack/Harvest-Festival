package joshie.harvest.npc.greeting;

import joshie.harvest.api.npc.IInfoButton;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.quests.Quests;
import joshie.harvest.town.TownHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.lib.HFModInfo.ICONS;

public class GreetingSupermarket implements IInfoButton {
    private final String text;
    private final String text2;

    public GreetingSupermarket(ResourceLocation resourceLocation) {
        this.text = resourceLocation.getResourceDomain() + ".npc." + resourceLocation.getResourcePath() + ".shop";
        this.text2 = resourceLocation.getResourceDomain() + ".npc." + resourceLocation.getResourcePath() + ".shop.wednesday";
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        return TownHelper.getClosestTownToEntity(ageable).getQuests().getFinished().contains(Quests.OPEN_WEDNESDAYS) ? TextHelper.localize(text2) : TextHelper.localize(text);
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
