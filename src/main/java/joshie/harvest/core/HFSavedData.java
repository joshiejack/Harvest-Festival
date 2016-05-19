package joshie.harvest.core;

import joshie.harvest.animals.AnimalTrackerServer;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.EntityHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.crops.CropTrackerServer;
import joshie.harvest.mining.MineTrackerServer;
import joshie.harvest.npc.town.TownTrackerServer;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.UsernameCache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HFSavedData extends WorldSavedData {
    public static final String DATA_NAME = HFModInfo.CAPNAME + "-Data";

    private CalendarServer calendar = new CalendarServer();
    private AnimalTrackerServer animals = new AnimalTrackerServer();
    private CropTrackerServer crops = new CropTrackerServer();
    private MineTrackerServer mines = new MineTrackerServer();
    private HashMap<UUID, PlayerTrackerServer> players = new HashMap<>();
    private TownTrackerServer towns = new TownTrackerServer();

    public HFSavedData(String string) {
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

    public Collection<PlayerTrackerServer> getPlayerData() {
        return players.values();
    }

    public TownTrackerServer getTownTracker() {
        return towns;
    }

    public PlayerTrackerServer getPlayerData(EntityPlayerMP player) {
        UUID uuid = UUIDHelper.getPlayerUUID(player);
        if (players.containsKey(uuid)) {
            return players.get(uuid);
        } else {
            //If this UUID was not found, Search the username cache for this players username
            String name = player.getGameProfile().getName();
            for (Map.Entry<UUID, String> entry : UsernameCache.getMap().entrySet()) {
                if (entry.getValue().equals(name)) {
                    uuid = entry.getKey();
                    break;
                }
            }

            if (players.containsKey(uuid)) {
                return players.get(uuid);
            } else {
                PlayerTrackerServer data = new PlayerTrackerServer(player);
                players.put(uuid, data);

                markDirty();
                return players.get(uuid);
            }
        }
    }

    /**
     * CAN AND WILL RETURN NULL, IF THE UUID COULD NOT BE FOUND
     **/
    public PlayerTrackerServer getPlayerData(UUID uuid) {
        if (players.containsKey(uuid)) {
            return players.get(uuid);
        } else {
            EntityPlayer player = EntityHelper.getPlayerFromUUID(uuid);
            if (player == null) return null;
            else return getPlayerData((EntityPlayerMP) player);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        calendar.readFromNBT(nbt.getCompoundTag("Calendar"));
        crops.readFromNBT(nbt.getCompoundTag("CropTracker"));
        mines.readFromNBT(nbt.getCompoundTag("MineTracker"));
        towns.readFromNBT(nbt.getCompoundTag("TownTracker"));

        NBTTagList tag_list_players = nbt.getTagList("PlayerTracker", 10);
        for (int i = 0; i < tag_list_players.tagCount(); i++) {
            NBTTagCompound tag = tag_list_players.getCompoundTagAt(i);
            PlayerTrackerServer data = new PlayerTrackerServer();
            boolean success = false;
            try {
                data.readFromNBT(tag);
                success = true;

            } catch (Exception e) {
                success = false;
            }

            //Only add non failed loads
            if (success) {
                players.put(data.getUUID(), data);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tag_calendar = new NBTTagCompound();
        calendar.writeToNBT(tag_calendar);
        nbt.setTag("Calendar", tag_calendar);

        NBTTagCompound tag_crops = new NBTTagCompound();
        crops.writeToNBT(tag_crops);
        nbt.setTag("CropTracker", tag_crops);

        NBTTagCompound tag_mines = new NBTTagCompound();
        mines.writeToNBT(tag_mines);
        nbt.setTag("MineTracker", tag_mines);

        NBTTagCompound tag_towns = new NBTTagCompound();
        towns.writeToNBT(tag_towns);
        nbt.setTag("TownTracker", tag_towns);

        NBTTagList tag_list_players = new NBTTagList();
        for (Map.Entry<UUID, PlayerTrackerServer> entry : players.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                NBTTagCompound tag = new NBTTagCompound();
                entry.getValue().writeToNBT(tag);
                tag_list_players.appendTag(tag);
            }
        }

        nbt.setTag("PlayerTracker", tag_list_players);
        return nbt;
    }
}