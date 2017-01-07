package joshie.harvest.knowledge.gui.stats.quests.page;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.button.ButtonNext;
import joshie.harvest.knowledge.gui.stats.button.ButtonPrevious;
import joshie.harvest.knowledge.gui.stats.quests.button.ButtonQuest;
import joshie.harvest.knowledge.gui.stats.quests.button.ButtonQuestNull;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class PageQuests extends BookPage<GuiStats> {
    public static final BookPage INSTANCE = new PageQuests();

    private PageQuests() {
        super("quests", "quests", new ItemStack(Items.FEATHER));
    }


    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList, List<GuiLabel> labelList) {
        super.initGui(gui, buttonList, labelList);
        World world = MCClientHelper.getWorld();
        EntityPlayer player = MCClientHelper.getPlayer();
        List<Quest> list = QuestHelper.INSTANCE.getCurrentQuests(player);
        Iterator<Quest> it = list.iterator();
        while (it.hasNext()) {
            Quest quest = it.next();
            if (!quest.isRealQuest() || quest.getDescription(world, player) == null) it.remove();
        }

        int x = 0, y = 0;
        for (int i = start * 12; i < 12 + start * 12 && i < list.size(); i++) {
            Quest quest = list.get(i);
            buttonList.add(new ButtonQuest(gui, quest, buttonList.size(), 16 + x * 144, 26 + y * 25));
            y++;

            if (y >= 6) {
                y = 0;
                x++;
            }
        }

        if (buttonList.size() == 0) buttonList.add(new ButtonQuestNull(gui, buttonList.size(), 16 + x * 144, 26 + y * 25));
        if (start < list.size() / 12) buttonList.add(new ButtonNext(gui, buttonList.size(), 273, 172));
        if (start != 0) buttonList.add(new ButtonPrevious(gui, buttonList.size(), 20, 172));
    }
}
