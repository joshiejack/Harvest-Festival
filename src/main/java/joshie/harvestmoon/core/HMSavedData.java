package joshie.harvestmoon.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import joshie.harvestmoon.animals.AnimalTrackerServer;
import joshie.harvestmoon.calendar.CalendarServer;
import joshie.harvestmoon.core.helpers.generic.EntityHelper;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.crops.CropTrackerServer;
import joshie.harvestmoon.mining.MineTrackerServer;
import joshie.harvestmoon.player.PlayerDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.UsernameCache;

public class HMSavedData extends WorldSavedData {
    public static final String DATA_NAME = HMModInfo.CAPNAME + "-Data";

    private CalendarServer calendar = new CalendarServer();
    private AnimalTrackerServer animals = new AnimalTrackerServer();
    private CropTrackerServer crops = new CropTrackerServer();
    private MineTrackerServer mines = new MineTrackerServer();
    private HashMap<UUID, PlayerDataServer> players = new HashMap();

    public HMSavedData(String string) {
        super(string);
    }

    public AnimalTrackerServer getAnimalTracker() {
        return animals;
    }

    public CalendarServer getCalendar() {
        return calendar;
    }

    public CropTrackerServer getCropTracker() {
        return crops;
    }

    public MineTrackerServer getMineTracker() {
        return mines;
    }

    public PlayerDataServer getPlayerData(EntityPlayerMP player) {
        UUID uuid = player.getPersistentID();
        if (players.containsKey(uuid)) {
            return players.get(uuid);
        } else {
            //If this UUID was not found, Search the username cache for this players username
            String name = player.getCommandSenderName();
            for (Map.Entry<UUID, String> entry : UsernameCache.getMap().entrySet()) {
                if (entry.getValue().equals(name)) {
                    uuid = entry.getKey();
                    break;
                }
            }

            if (players.containsKey(uuid)) {
                return players.get(uuid);
            } else {
                PlayerDataServer data = new PlayerDataServer(player);
                players.put(uuid, data);

                markDirty();
                return players.get(uuid);
            }
        }
    }

    public PlayerDataServer getPlayerData(UUID uuid) {
        if (players.containsKey(uuid)) {
            return players.get(uuid);
        } else {
            EntityPlayer player = EntityHelper.getPlayerFromUUID(uuid);
            if (player == null) return null;
            for (Entry<UUID, String> entry : UsernameCache.getMap().entrySet()) {
                if (entry.getValue().equals(player.getDisplayName())) {
                    if (players.containsKey(entry.getKey())) {
                        return players.get(entry.getKey());
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        calendar.readFromNBT(nbt.getCompoundTag("Calendar"));
        animals.readFromNBT(nbt.getCompoundTag("AnimalTracker"));
        crops.readFromNBT(nbt.getCompoundTag("CropTracker"));
        mines.readFromNBT(nbt.getCompoundTag("MineTracker"));

        NBTTagList tag_list_players = nbt.getTagList("PlayerTracker", 10);
        for (int i = 0; i < tag_list_players.tagCount(); i++) {
            NBTTagCompound tag = tag_list_players.getCompoundTagAt(i);
            PlayerDataServer data = new PlayerDataServer();
            data.readFromNBT(tag);
            UUID uuid = new UUID(tag.getLong("UUIDMost"), tag.getLong("UUIDLeast"));
            players.put(uuid, data);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tag_calendar = new NBTTagCompound();
        calendar.writeToNBT(tag_calendar);
        nbt.setTag("Calendar", tag_calendar);

        NBTTagCompound tag_animals = new NBTTagCompound();
        animals.writeToNBT(tag_animals);
        nbt.setTag("AnimalTracker", tag_animals);

        NBTTagCompound tag_crops = new NBTTagCompound();
        crops.writeToNBT(tag_crops);
        nbt.setTag("CropTracker", tag_crops);

        NBTTagCompound tag_mines = new NBTTagCompound();
        mines.writeToNBT(tag_mines);
        nbt.setTag("MineTracker", tag_mines);

        NBTTagList tag_list_players = new NBTTagList();
        for (Map.Entry<UUID, PlayerDataServer> entry : players.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                NBTTagCompound tag = new NBTTagCompound();
                entry.getValue().writeToNBT(tag);
                tag_list_players.appendTag(tag);
            }
        }

        nbt.setTag("PlayerTracker", tag_list_players);
    }
}
