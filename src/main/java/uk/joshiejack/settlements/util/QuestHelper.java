package uk.joshiejack.settlements.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.npcs.status.StatusTracker;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.settlements.quest.data.QuestData;
import uk.joshiejack.settlements.quest.data.QuestTracker;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.util.PenguinGroup;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class QuestHelper {
    public static List<QuestData> getActive(EntityPlayer player, String method) {
        return AdventureDataLoader.get(player.world).getActive(player, method);
    }

    public static QuestData getData(EntityPlayer player, Quest script) {
        return AdventureDataLoader.get(player.world).getData(player, script);
    }

    public static void readIDToTownMap(NBTTagList list, Int2ObjectMap<TownServer> map) {
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            int id = tag.getInteger("ID");
            BlockPos centre = BlockPos.fromLong(tag.getLong("Centre"));
            TownServer town = new TownServer(id, centre);
            town.deserializeNBT(tag.getCompoundTag("Data"));
            map.put(id, town);
        }
    }

    public static NBTTagList writeIDToTownMap(Int2ObjectMap<TownServer> map) {
        NBTTagList list = new NBTTagList();
        map.forEach((key, value) -> {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("ID", key);
            tag.setLong("Centre", value.getCentre().toLong());
            tag.setTag("Data", value.serializeNBT());
            list.appendTag(tag);
        });

        return list;
    }

    public static void readUUIDtoRelationshipMap(NBTTagList list, Map<UUID, StatusTracker> map) {
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(tag.getString("UUID"));
            StatusTracker tracker = new StatusTracker(uuid);
            tracker.deserializeNBT(tag.getCompoundTag("Data"));
            map.put(uuid, tracker);
        }
    }

    public static NBTTagList writeUUIDToRelationshipMap(Map<UUID, StatusTracker> map) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<UUID, StatusTracker> tracker: map.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("UUID", tracker.getKey().toString());
            tag.setTag("Data", tracker.getValue().serializeNBT());
            list.appendTag(tag);
        }

        return list;
    }

    public static NBTTagList writeUUIDToTrackerMap(Map<UUID, QuestTracker> map) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<UUID, QuestTracker> tracker: map.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("UUID", tracker.getKey().toString());
            tag.setTag("Data", tracker.getValue().serializeNBT());
            list.appendTag(tag);
        }

        return list;
    }

    public static void readUUIDtoTrackerMap(PenguinGroup type, NBTTagList list, Map<UUID, QuestTracker> map) {
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(tag.getString("UUID"));
            QuestTracker tracker = new QuestTracker(type);
            tracker.deserializeNBT(tag.getCompoundTag("Data"));
            map.put(uuid, tracker);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static NBTTagList writeQuestMap(Map<ResourceLocation, QuestData> map) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<ResourceLocation, QuestData> entry: map.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Quest", entry.getKey().toString());
            tag.setTag("Data", entry.getValue().serializeNBT());
            list.appendTag(tag);
        }

        return list;
    }

    public static void readQuestMap(NBTTagList list, Map<ResourceLocation, QuestData> map) {
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            Quest quest = Quest.REGISTRY.get(new ResourceLocation(tag.getString("Quest")));
            if (quest != null) {
                QuestData storage = new QuestData(quest);
                storage.deserializeNBT(tag.getCompoundTag("Data"));
                map.put(quest.getRegistryName(), storage);
            }
        }
    }

}
