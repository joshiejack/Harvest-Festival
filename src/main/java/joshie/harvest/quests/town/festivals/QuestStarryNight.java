package joshie.harvest.quests.town.festivals;

import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.quests.base.QuestFestivalTimed;
import joshie.harvest.quests.town.festivals.starry.StarryNightData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

@HFQuest("festival.starry.night")
public class QuestStarryNight extends QuestFestivalTimed {
    private final Map<UUID, StarryNightData> data = new HashMap<>();

    public QuestStarryNight() {}

    @Override //If the npc is a marriage candidate, we can process them for this festival
    public boolean isNPCUsed(EntityPlayer player, NPCEntity entity) {
        return entity.getNPC().getAge() == Age.ADULT && !getDataForPlayer(player).isFinished();
    }

    private StarryNightData getDataForPlayer(EntityPlayer player) {
        UUID uuid = EntityHelper.getPlayerUUID(player);
        if (data.containsKey(uuid)) return data.get(uuid);
        else {
            StarryNightData starry = new StarryNightData();
            data.put(uuid, starry);
            return starry;
        }
    }

    @Override
    protected boolean isCorrectTime(long time) {
        return time >= 13000L && time <= 22000L;
    }

    @Nullable
    public Selection getSelection(EntityPlayer player, NPC npc) {
        return getDataForPlayer(player).getSelection();
    }

    @Override
    @Nullable
    protected String getLocalizedScript(EntityPlayer player, NPC npc) {
        return getDataForPlayer(player).getLocalizedScript(npc);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onChatClosed(EntityPlayer player, NPC npc) {
        if(getDataForPlayer(player).isChatting()) syncData(player);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList list = nbt.getTagList("Data", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(tag.getString("UUID"));
            StarryNightData starry = StarryNightData.fromNBT(tag.getCompoundTag("Data"));
            data.put(uuid, starry);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList list = new NBTTagList();
        for (Entry<UUID, StarryNightData> entry: data.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("UUID", entry.getKey().toString());
            tag.setTag("Data", entry.getValue().toNBT());
            list.appendTag(tag);
        }

        nbt.setTag("Data", list);
        return nbt;
    }
}
