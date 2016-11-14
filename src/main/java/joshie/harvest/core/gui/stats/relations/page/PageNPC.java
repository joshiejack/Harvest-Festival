package joshie.harvest.core.gui.stats.relations.page;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.gui.stats.GuiStats;
import joshie.harvest.core.gui.stats.button.ButtonNext;
import joshie.harvest.core.gui.stats.button.ButtonPrevious;
import joshie.harvest.core.gui.stats.relations.button.ButtonRelationsNPC;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;

import java.util.List;

public class PageNPC extends PageStats {
    public static final BookPage INSTANCE = new PageNPC();

    private PageNPC() {
        super("npc", HFNPCs.SPAWNER_NPC.getStackFromObject((NPC)HFNPCs.BUILDER));
    }

    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList, List<GuiLabel> labelList) {
        super.initGui(gui, buttonList, labelList);
        int x = 0;
        int y = 0;
        List<NPC> list = NPCRegistry.REGISTRY.getValues();
        for (int i = 1 + start * 14; i < 15 + start * 14 && i < list.size(); i++) {
            NPC npc = list.get(i);
            buttonList.add(new ButtonRelationsNPC(gui, npc, buttonList.size(), 16 + x * 144, 20 + y * 22));
            y++;

            if (y >= 7) {
                y = 0;
                x++;
            }
        }

        if (start < (NPCRegistry.REGISTRY.getValues().size() - 1) / 14) buttonList.add(new ButtonNext(gui, buttonList.size(), 273, 172));
        if (start != 0) buttonList.add(new ButtonPrevious(gui, buttonList.size(), 20, 172));
    }
}
