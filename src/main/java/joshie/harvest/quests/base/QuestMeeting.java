package joshie.harvest.quests.base;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class QuestMeeting extends Quest {
    private final Building building;

    public QuestMeeting(Building building, INPC npc) {
        this.building = building;
        setNPCs(npc);
    }

    @Override
    public boolean isRealQuest() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        TownData data = TownHelper.getClosestTownToEntity(entity);
        if (data.hasBuilding(building)) {
            return getLocalized("text");
        } else return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc, boolean wasSneaking) {
        TownData data = TownHelper.getClosestTownToEntity(entity);
        if (data.hasBuilding(building)) {
            complete(player);
        }
    }
}
