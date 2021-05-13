package uk.joshiejack.settlements.client.gui.page;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.entity.player.EntityPlayer;
import uk.joshiejack.settlements.client.gui.GuiJournal;
import uk.joshiejack.settlements.client.gui.button.*;
import uk.joshiejack.settlements.client.gui.label.LabelTownLaws;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.people.Citizenship;
import uk.joshiejack.settlements.world.town.people.Ordinance;
import uk.joshiejack.harvestcore.client.gui.label.LabelTownInfo;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBack;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonForward;
import uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;

import java.util.List;
import java.util.UUID;

public class PageTownManager extends PageMultiple.PageMultipleButton<Ordinance> {
    public static final PageTownManager INSTANCE = new PageTownManager();
    public Town<?> town;

    private PageTownManager() {
        super("settlements.journal.town_manager", 6);
        this.icon = new Icon(GuiJournal.ICONS, 0, 16);
        this.setSingleSided();
    }

    @Override
    public void initGui(List<GuiButton> buttonList, List<GuiLabel> labelList) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        town = TownFinder.find(player.world, player.getPosition());
        labelList.add(new LabelTownInfo(gui, 25, 24, town));
        labelList.add(new LabelTownLaws(gui, 164, 20, town));
        super.initGui(buttonList, labelList);
        int i = 0;
        for (UUID member: PenguinTeamsClient.members(town.getCharter().getTeamID())) {
            buttonList.add(new ButtonVillager(gui, buttonList.size(), 24 + (i % 7) * 18, 95 + (i / 7) * 18, player.world.provider.getDimension(), town.getID(), member));
            i++;
        }

        for (UUID member: town.getGovernment().getApplications()) {
            buttonList.add(new ButtonVillagerApplication(gui, buttonList.size(), 24 + (i % 7) * 18, 95 + (i / 7) * 18, player.world.provider.getDimension(), town.getID(), member));
            i++;
        }

        //If the town has no owner
        if (!town.getCharter().hasMayor()) {
            buttonList.add(new ButtonClaimTown(player.world.provider.getDimension(), town.getID(), gui, buttonList.size(), 24 + (i % 7) * 18, 95 + (i / 7) * 18));
        } else if (town.getGovernment().getCitizenship() != Citizenship.CLOSED && !PenguinTeamsClient.members(town.getCharter().getTeamID()).contains(PlayerHelper.getUUIDForPlayer(player))) { //AND NOT ALREADY A MEMBER
            buttonList.add(new ButtonRequestCitizenship(town.getGovernment().getCitizenship(), player.world.provider.getDimension(), town.getID(), gui, buttonList.size(), 24 + (i % 7) * 18, 95 + (i / 7) * 18));
        }

        //If we're the owner
        if (PlayerHelper.getUUIDForPlayer(player).equals(PenguinTeamsClient.getInstance().getOwner())) {
            buttonList.add(new ButtonRenameTown(gui, player.world.provider.getDimension(), town, buttonList.size(), 20, 24));
        }

        buttonList.add(new ButtonCitizenshipType(gui, town, buttonList.size(), 273, 28));
    }

    @Override
    public ButtonBack createBackButton(List<GuiButton> buttonList) { return new ButtonBack(this, gui, buttonList.size(), 164, 168); }

    @Override
    public ButtonForward createForwardButton(List<GuiButton> buttonList) { return new ButtonForward(this, gui, buttonList.size(), 270, 168); }

    @Override
    protected ButtonBook createButton(GuiBook gui, Ordinance entry, PageSide side, int id, int position) {
        return new ButtonOrdinance(gui, town, entry,  id, 273, 48 + (position) * 20);
    }

    @Override
    public List<Ordinance> getList() {
        return Lists.newArrayList(Ordinance.values());
    }
}
