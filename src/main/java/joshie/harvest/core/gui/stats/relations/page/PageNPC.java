package joshie.harvest.core.gui.stats.relations.page;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.gui.stats.GuiStats;
import joshie.harvest.core.gui.stats.relations.button.ButtonRelationsNPC;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import net.minecraft.client.gui.GuiButton;

import java.util.List;

public class PageNPC extends PageStats {
    public static final BookPage INSTANCE = new PageNPC();

    private PageNPC() {
        super("npc", HFNPCs.SPAWNER_NPC.getStackFromObject((NPC)HFNPCs.BUILDER));
    }

    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList) {
        super.initGui(gui, buttonList);
        int x = 0;
        int y = 0;
        int index = 0;
        for (NPC npc: NPCRegistry.REGISTRY) {
            if (npc == HFNPCs.NULL_NPC) continue;
            buttonList.add(new ButtonRelationsNPC(gui, npc, buttonList.size(), 16 + x * 144, 20 + y * 22));
            y++;

            if (y >= 7) {
                y = 0;
                x++;
            }

            index++;
            if (index >= 14) break;
        }
    }
}
