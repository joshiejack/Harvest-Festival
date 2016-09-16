package joshie.harvest.quests.trade;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.calendar.CalendarClient;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.quests.Quests;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;
import java.util.Set;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM;
import static joshie.harvest.npc.HFNPCs.GODDESS;

@HFQuest("trade.weather")
public class QuestGoddessWeather extends QuestTrade {
    public QuestGoddessWeather() {
        setNPCs(GODDESS);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.TUTORIAL_CARPENTER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (InventoryHelper.getHandItemIsIn(player, ITEM, Items.COMPASS) != null) {
            return getLocalized(HFTrackers.<CalendarClient>getCalendar(player.worldObj).getTomorrowsWeather().name().toLowerCase(Locale.ENGLISH) + (player.worldObj.rand.nextInt(3) + 1));
        } else return null;
    }
}
