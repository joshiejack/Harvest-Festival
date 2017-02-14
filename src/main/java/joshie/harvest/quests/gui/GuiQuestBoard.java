package joshie.harvest.quests.gui;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.base.gui.ContainerNull;
import joshie.harvest.core.base.gui.GuiBaseContainer;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringEscapeUtils;

public class GuiQuestBoard extends GuiBaseContainer {
    private World world;
    private Quest quest;
    private BlockPos pos;

    public GuiQuestBoard(BlockPos pos, EntityPlayer player) {
        super(new ContainerNull(), "sign", 34);
        this.quest = TownHelper.getClosestTownToEntity(player, false).getDailyQuest();
        this.world = MCClientHelper.getWorld();
        this.pos = pos;
        this.xSize = 226;
    }

    @Override
    public void initGui() {
        super.initGui();
        if (quest != null && !TownHelper.getClosestTownToEntity(MCClientHelper.getPlayer(), false).getQuests().getCurrent().contains(quest)) {
            buttonList.add(new GuiButtonStartQuest(pos, quest, 0, guiLeft + 78, guiTop + 152));
        }
    }

    @Override
    public void drawForeground(int x, int y) {
        if (quest != null) {
            fontRendererObj.drawSplitString(StringEscapeUtils.unescapeJava(quest.getDescription(world, null)), 42, 38, 142, 4210752);
        } else fontRendererObj.drawSplitString(StringEscapeUtils.unescapeJava(TextHelper.translate("quest.none")), 42, 38, 142, 4210752);
    }
}
