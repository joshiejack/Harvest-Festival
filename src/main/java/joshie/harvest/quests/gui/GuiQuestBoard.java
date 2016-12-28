package joshie.harvest.quests.gui;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.base.gui.GuiBaseContainer;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class GuiQuestBoard extends GuiBaseContainer {
    private World world;
    private Quest quest;

    public GuiQuestBoard(EntityPlayer player, InventoryPlayer playerInv) {
        super(new ContainerQuestBoard(player, playerInv), "sign", 56);
        this.quest = TownHelper.getClosestTownToEntity(player).getDailyQuest();
        this.world = MCClientHelper.getWorld();
    }

    @Override
    public void drawForeground(int x, int y) {
        if (quest != null) {
            fontRendererObj.drawSplitString(quest.getDescription(world, null), 42, 38, 142, 4210752);
        }
    }
}
