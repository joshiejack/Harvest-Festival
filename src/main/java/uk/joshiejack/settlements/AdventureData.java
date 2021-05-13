package uk.joshiejack.settlements;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.settlements.network.block.PacketSyncDailies;
import uk.joshiejack.settlements.network.town.PacketSyncTowns;
import uk.joshiejack.settlements.network.town.land.PacketCreateTown;
import uk.joshiejack.settlements.npcs.status.StatusTracker;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.settlements.quest.data.QuestData;
import uk.joshiejack.settlements.quest.data.QuestTracker;
import uk.joshiejack.settlements.quest.settings.Information;
import uk.joshiejack.settlements.util.QuestHelper;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.PenguinGroup;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;


public class AdventureData extends WorldSavedData {
    public static final String DATA_NAME = "settlements-data";
    private final Map<UUID, QuestTracker> playerQuests = Maps.newHashMap();
    private final Map<UUID, QuestTracker> teamQuests = Maps.newHashMap();
    private final Map<UUID, StatusTracker> status = Maps.newHashMap();
    private final QuestTracker globalQuests = new QuestTracker(PenguinGroup.GLOBAL);
    private final Cache<UUID, Cache<String, List<QuestData>>> allScripts = CacheBuilder.newBuilder().build(); //Clear the cache for a uuid whenever a quest in the list is completed/added
    private final Cache<UUID, List<QuestTracker>> allTrackers = CacheBuilder.newBuilder().build();
    private final Int2ObjectMap<Int2ObjectMap<TownServer>> towns = new Int2ObjectOpenHashMap<>();
    private static final List<QuestData> EMPTY = Lists.newArrayList();
    private static final List<QuestTracker> EMPTY_TRACKERS = Lists.newArrayList();
    private int day;

    public AdventureData(String name) {
        super(name);
    }

    public Collection<TownServer> getTowns(World world) {
        return getTownMap(world.provider.getDimension()).values();
    }

    public void clearCache(UUID uuid) {
        allScripts.invalidate(uuid);
    }

    private Int2ObjectMap<TownServer> getTownMap(int dim) {
        Int2ObjectMap<TownServer> map = towns.get(dim);
        if (map == null) {
            map = new Int2ObjectOpenHashMap<>();
            towns.put(dim, map);
        }

        return map;
    }

    private int getUnusedID(int dimension) {
        Int2ObjectMap<TownServer> map = getTownMap(dimension);
        for (int i = 1; i <= Short.MAX_VALUE; i++) {
            if (!map.containsKey(i)) return i;
        }

        return 0; //Null town, should never happen in theory
    }

    public Town<?> createTown(World world, EntityPlayer player) {
        TownServer town = new TownServer(getUnusedID(world.provider.getDimension()), new BlockPos(player)); //Give the town the same uuid as the team
        UUID townUUID = UUID.randomUUID(); //Randomly generated
        town.getCharter().setFoundingInformation(player.getDisplayNameString() + "Ville", player.getName(), world.getWorldTime(), townUUID);
        PenguinTeams.get(world).changeTeam(world, PlayerHelper.getUUIDForPlayer(player), townUUID); //Player joins this new team
        getTownMap(world.provider.getDimension()).put(town.getID(), town);
        AdventureDataLoader.get(world).markDirty(); //Save me bitch!
        TownFinder.getFinder(world).clearCache(); //Clear the cache for the town finder
        PenguinNetwork.sendToEveryone(new PacketCreateTown(world.provider.getDimension(), town));
        return town;
    }

    @Nonnull
    public TownServer getTownByID(int dimension, int townID) {
        Int2ObjectMap<TownServer> towns = getTownMap(dimension);
        return towns.containsKey(townID) ? towns.get(townID) : TownServer.NULL;
    }

    public Collection<StatusTracker> getStatusTrackers() {
        return status.values();
    }

    public List<Quest> getDailies(EntityPlayer player) {
        return getTrackers(player).stream().map(QuestTracker::getDaily).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void syncDailies(EntityPlayer player) {
        List<Quest> dailies = getDailies(player);
        Map<Quest, Pair<String, String>> map = Maps.newHashMap();

        for (Quest q: dailies) {
            map.put(q, Pair.of(Scripting.getResult(q.getInterpreter(), "getTaskTitle", "Unknown"),
                    Scripting.getResult(q.getInterpreter(), "getTaskDescription", "Unknown")));
        }

        PenguinNetwork.sendToClient(new PacketSyncDailies(map), player);
    }

    public List<QuestTracker> getScriptTrackers() {
        List<QuestTracker> trackers = Lists.newArrayList();
        trackers.add(globalQuests);
        trackers.addAll(teamQuests.values());
        trackers.addAll(playerQuests.values());
        return trackers;
    }

    public StatusTracker getStatusTracker(EntityPlayer player) {
        UUID playerID = PlayerHelper.getUUIDForPlayer(player);
        if (!status.containsKey(playerID)) {
            status.put(playerID, new StatusTracker(playerID)); //Create a new map!
            markDirty(); //Save the new value
        }

        return status.get(playerID);
    }

    public void markCompleted(EntityPlayer player, Quest script) {
        getTrackerForQuest(player, script)
                .markCompleted(TimeHelper.getElapsedDays(player.world), script);
        allScripts.invalidate(PlayerHelper.getUUIDForPlayer(player)); //Clear the cache
    }

    @Nonnull
    public QuestTracker getPlayerTracker(EntityPlayer player) {
        //Create a tracker for the players if it doesn't exist yet
        UUID uuid = PlayerHelper.getUUIDForPlayer(player);
        if (!playerQuests.containsKey(uuid)) {
            playerQuests.put(uuid, new QuestTracker(PenguinGroup.PLAYER));
        }

        return playerQuests.get(uuid);
    }

    @Nonnull
    private QuestTracker getTeamTracker(EntityPlayer player) {
        return getTeamTracker(PenguinTeams.get(player.world).getTeamUUIDForPlayer(player));
    }

    @Nonnull
    public QuestTracker getTeamTracker(UUID teamUUID) {
        if (!teamQuests.containsKey(teamUUID)) {
            teamQuests.put(teamUUID, new QuestTracker(PenguinGroup.TEAM));
        }

        return teamQuests.get(teamUUID);
    }

    public QuestTracker getServerTracker() {
        return globalQuests;
    }

    @Nonnull
    public QuestTracker getTrackerForQuest(EntityPlayer player, Quest script) {
        switch (script.getSettings().getType()) {
            case PLAYER:
                return getPlayerTracker(player);
            case TEAM:
                return getTeamTracker(player);
            default:
                return globalQuests;
        }
    }

    public List<QuestTracker> getTrackers(EntityPlayer player) {
        try {
            return allTrackers.get(PlayerHelper.getUUIDForPlayer(player), () -> {
                List<QuestTracker> trackers = Lists.newArrayList();
                trackers.add(getPlayerTracker(player));
                trackers.add(getTeamTracker(player));
                trackers.add(globalQuests);
                return trackers;
            });
        } catch (Exception ignored) {
            return EMPTY_TRACKERS;
        }
    }

    public QuestData getData(EntityPlayer player, Quest quest) {
        return getTrackerForQuest(player, quest).getData(quest.getRegistryName());
    }

    public List<QuestData> getActive(EntityPlayer player, String method) {
        try {
            allScripts.invalidateAll();
            return allScripts.get(PlayerHelper.getUUIDForPlayer(player), () -> CacheBuilder.newBuilder().build()).get(method, () -> {
                List<QuestData> list = new ArrayList<>();
                getTrackers(player).forEach((t) -> list.addAll(t.getActive(method)));
                return list;
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            return EMPTY;
        }
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
        this.markDirty();
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound compound) {
        try {
            day = compound.getInteger("Day");
            QuestHelper.readUUIDtoRelationshipMap(compound.getTagList("Statuses", 10), status);
            NBTTagList list = compound.getTagList("Dimensions", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                QuestHelper.readIDToTownMap(tag.getTagList("Towns", 10), getTownMap(tag.getInteger("ID")));
            }

            //Quests
            NBTTagCompound nbt = compound.getCompoundTag("Quests");
            QuestHelper.readUUIDtoTrackerMap(PenguinGroup.PLAYER, nbt.getTagList("Player", 10), playerQuests);
            QuestHelper.readUUIDtoTrackerMap(PenguinGroup.TEAM, nbt.getTagList("Team", 10), teamQuests);
            globalQuests.deserializeNBT(nbt.getCompoundTag("Global"));

            //Reload in the triggers, so yey!
            playerQuests.values().forEach((t) -> t.setupOrRefresh(day));
            teamQuests.values().forEach((t) -> t.setupOrRefresh(day));
            globalQuests.setupOrRefresh(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        compound.setInteger("Day", day);
        compound.setTag("Statuses", QuestHelper.writeUUIDToRelationshipMap(status));
        NBTTagList list = new NBTTagList();
        towns.forEach((i, map) -> {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("ID", i);
            tag.setTag("Towns", QuestHelper.writeIDToTownMap(map));
            list.appendTag(tag);
        });

        compound.setTag("Dimensions", list);

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Player", QuestHelper.writeUUIDToTrackerMap(playerQuests));
        nbt.setTag("Team", QuestHelper.writeUUIDToTrackerMap(teamQuests));
        nbt.setTag("Global", globalQuests.serializeNBT()); //Server Quests
        compound.setTag("Quests", nbt);
        return compound;
    }

    public void sync(EntityPlayer player) {
        //Sync the daily quests to the client
        syncDailies(player);
        getStatusTracker(player).sync(player.getEntityWorld());
        towns.forEach((i, map) -> {
            PenguinNetwork.sendToClient(new PacketSyncTowns(i, getTownMap(i).values()), player);
        });
    }

    public List<Information> getInformation(EntityPlayer player) {
        return getActive(player, "display").stream().map(d -> d.toInformation(player)).filter(s -> !s.getIcon().isEmpty()).collect(Collectors.toList());
    }
}
