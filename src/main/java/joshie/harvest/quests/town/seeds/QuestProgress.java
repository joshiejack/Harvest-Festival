package joshie.harvest.quests.town.seeds;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.town.Town;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestTown;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@HFQuest("seeds.progress")
public class QuestProgress extends QuestTown {
    public QuestProgress() {
        setNPCs(HFNPCs.GS_OWNER);
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPCEntity entity) {
        Town data = entity.getTown();
        Season season = HFApi.calendar.getDate(player.worldObj).getSeason();
        return super.isNPCUsed(player, entity) && (season == Season.SUMMER || season == Season.AUTUMN) && data.hasBuilding(HFBuildings.CAFE) && data.hasBuilding(HFBuildings.BLACKSMITH) && data.hasBuilding(HFBuildings.FISHING_HUT) && data.hasBuilding(HFBuildings.FESTIVAL_GROUNDS);
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    @Override
    public String getLocalizedScript(EntityPlayer player, NPCEntity npc) {
        return getLocalized("complete");
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean wasSneaking) {
        complete(player);
    }
}
