package uk.joshiejack.harvestcore.client.gui.label;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.util.interfaces.DateFormatter;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;

import javax.annotation.Nonnull;

public class LabelTownInfo extends LabelBook {
    public static DateFormatter formatter = (time) -> ("Day " + TimeHelper.getElapsedDays(time)); //Gets replaced by HF formatter
    private final Town<?> town;
    private final int residents;
    private final PenguinTeam team;
    private String owner;
    private long founded;

    public LabelTownInfo(GuiBook gui, int x, int y, Town<?> town) {
        super(gui, x, y);
        this.town = town;
        this.founded = town.getCharter().getFoundingDate();
        this.residents = town.getCensus().population() + PenguinTeamsClient.members(town.getCharter().getTeamID()).size();
        this.team = PenguinTeams.getTeamFromID(gui.mc.world, town.getCharter().getTeamID());
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int r, int s) {
        GlStateManager.disableDepth();
        boolean flag = mc.fontRenderer.getUnicodeFlag();
        mc.fontRenderer.setUnicodeFlag(true);
        drawHorizontalLine(x - 6, x + 115, y + 10, 0xFFB0A483);
        drawHorizontalLine(x - 5, x + 116, y + 11, 0xFF9C8C63);
        mc.fontRenderer.drawString(TextFormatting.BOLD + "Founder: " + town.getCharter().getFounder(), x - 5, y + 12, 0x857754);
        mc.fontRenderer.drawString(TextFormatting.BOLD + "Founded: " + formatter.format(founded), x - 5, y + 22, 0x857754);
        mc.fontRenderer.drawString(TextFormatting.BOLD + "Mayor: " + town.getCharter().getMayor(), x - 5, y + 32, 0x857754);
        mc.fontRenderer.drawString(TextFormatting.BOLD + "Population: " + residents, x - 5, y + 42, 0x857754);
        //Draw the members subtitle
        mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "Villagers", x, y + 55, 0x857754);
        drawHorizontalLine(x - 6, x + 115, y + 65, 0xFFB0A483);
        drawHorizontalLine(x - 5, x + 116, y + 66, 0xFF9C8C63);
        mc.fontRenderer.setUnicodeFlag(flag);
        GlStateManager.enableDepth();
    }
}
