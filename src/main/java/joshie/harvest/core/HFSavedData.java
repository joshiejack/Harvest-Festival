package joshie.harvest.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import joshie.harvest.animals.AnimalTrackerServer;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.EntityHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.crops.CropTrackerServer;
import joshie.harvest.mining.MineTrackerServer;
import joshie.harvest.player.PlayerDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.UsernameCache;

public class HFSavedData extends WorldSavedData {
    public static final String DATA_NAME = HFModInfo.CAPNAME + "-Data";

    private CalendarServer calendar = new CalendarServer();
    private AnimalTrackerServer animals = new AnimalTrackerServer();
    private CropTrackerServer crops = new CropTrackerServer();
    private MineTrackerServer mines = new MineTrackerServer();
    private HashMap<UUID, PlayerDataServer> players = new HashMap();

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

    public PlayerDataServer getPlayerData(EntityPlayerMP player) {
        UUID uuid = UUIDHelper.getPlayerUUID(player);
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

    /** CAN AND WILL RETURN NULL, IF THE UUID COULD NOT BE FOUND **/
    public PlayerDataServer getPlayerData(UUID uuid) {
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

        NBTTagList tag_list_players = nbt.getTagList("PlayerTracker", 10);
        for (int i = 0; i < tag_list_players.tagCount(); i++) {
            NBTTagCompound tag = tag_list_players.getCompoundTagAt(i);
            PlayerDataServer data = new PlayerDataServer();
            data.readFromNBT(tag);
            players.put(data.getUUID(), data);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tag_calendar = new NBTTagCompound();
        calendar.writeToNBT(tag_calendar);
        nbt.setTag("Calendar", tag_calendar);

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
