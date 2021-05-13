package uk.joshiejack.settlements.client.gui.page;

import uk.joshiejack.settlements.client.gui.NPCDisplayData;
import uk.joshiejack.settlements.client.gui.button.ButtonInviteNPC;
import uk.joshiejack.settlements.client.gui.label.LabelRelationships;
import uk.joshiejack.settlements.client.town.TownClient;
import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple.PageSide.LEFT;

//TODO: Move this to harvestfestival instead as the values aren't in the settlements mod by default
public class PageRelationships extends PageMultiple.PageMultipleLabel<NPCDisplayData> {
    public static final PageRelationships INSTANCE = new PageRelationships();

    private PageRelationships() {
        super("settlements.journal.relationships", 12);
        icon = new Icon(AdventureItems.NPC_SPAWNER.getStackFromResource(NPC.all().get(0).getRegistryName()), 0, 0);
    }

    @Override
    protected void addToList(List<GuiButton> buttonList, List<GuiLabel> labelList, GuiBook gui, NPCDisplayData entry, PageSide side, int position) {
        super.addToList(buttonList, labelList, gui, entry, side, position);
        EntityPlayer player = Minecraft.getMinecraft().player;
        TownClient town = TownFinder.find(player.world, new BlockPos(player));
        if (PenguinTeamsClient.getInstance().getID().equals(town.getCharter().getTeamID()) && entry.getNPCClass().canInvite() && town.getCensus().isInvitable(entry.getRegistryName())) {
            buttonList.add(new ButtonInviteNPC(gui, entry, buttonList.size(), (side == LEFT ? 106 : 248), 20 + (position) * 25));
        }
    }

    @Override
    protected LabelBook createLabel(GuiBook gui, NPCDisplayData entry, PageSide side, int position) {
        return new LabelRelationships(gui, entry, (side == LEFT ? 21: 163), 24 + (position) * 25);
    }

    @Override
    public List<NPCDisplayData> getList() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        TownClient town = TownFinder.find(player.world, new BlockPos(player));
        List<NPCDisplayData> list = NPC.all().stream().filter((npc -> town.getCensus().hasResident(npc.getRegistryName()))).collect(Collectors.toList());
        //Add the extras to the list
        Collections.reverse(list);
        list.addAll(town.getCensus().getCustomNPCs());
        return list;
    }
}
