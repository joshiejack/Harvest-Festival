package joshie.harvest.quests.town.festivals;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestFestival;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Set;

@HFQuest("festival.cooking")
public class QuestCookingFestival extends QuestFestival {
    public QuestCookingFestival() {
        setNPCs(HFNPCs.CAFE_GRANNY, HFNPCs.CAFE_OWNER, HFNPCs.GS_OWNER, HFNPCs.MAYOR);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return false;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        return "Quest is currently active!";
    }
}
