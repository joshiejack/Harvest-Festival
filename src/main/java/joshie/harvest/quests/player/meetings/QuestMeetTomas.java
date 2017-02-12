package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestMeeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@HFQuest("meeting.tomas")
public class QuestMeetTomas extends QuestMeeting {
    public QuestMeetTomas() {
        super(HFBuildings.CHURCH, HFNPCs.PRIEST);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getDescription(World world, EntityPlayer player) {
        if (hasBuilding(player)) return getLocalized("description");
        else if (building.getRules().canDo(world, player, 1)) return getLocalized("build");
        else return null;
    }
}
