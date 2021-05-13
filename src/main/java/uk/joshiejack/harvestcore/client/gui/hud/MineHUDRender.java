package uk.joshiejack.harvestcore.client.gui.hud;

import uk.joshiejack.harvestcore.world.mine.MineHelper;
import uk.joshiejack.seasons.date.CalendarDate;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.client.renderer.HUDRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

@SuppressWarnings("unused")
public class MineHUDRender extends HUDRenderer.HUDRenderData {
    @Override
    public ResourceLocation getTexture(Minecraft mc, Season season, CalendarDate date) {
        return MineHelper.getTierFromEntity(mc.player).getHUD();
    }

    @Override
    public String getHeader(Minecraft mc, Season season, CalendarDate date) {
        return TextFormatting.GRAY + StringHelper.format("harvestfestival.hud.mine", "" + MineHelper.getFloorFromEntity(mc.player));
    }
}
