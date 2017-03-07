package joshie.harvest.quests.town.festivals.contest;

import com.google.common.collect.Lists;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.quests.town.festivals.Place;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class ContestEntries<O, E extends ContestEntry, Q extends QuestContest> {
    protected final List<E> entries = new ArrayList<>();
    protected final BuildingLocation[] locations;
    protected final NPC[] npcs;
    protected Set<UUID> selecting = new HashSet<>();
    protected List<Pair<String, Integer>> entryNames;
    protected Set<NPC> usedNPCS;

    public ContestEntries(BuildingLocation[] locations, NPC[] npcs) {
        this.locations = locations;
        this.npcs = npcs;
    }

    public void setEntryNames(List<Pair<String, Integer>> list) {
        this.entryNames = list;
    }

    public List<Pair<String, Integer>> getNames() {
        return entryNames;
    }

    public Set<UUID> getSelecting() {
        return selecting;
    }

    public E getEntry(Place place) {
        return entries.get(place.ordinal());
    }

    public NPC[] getNPCs() {
        return npcs;
    }

    public void complete(World world) {}

    @Nullable
    public E getEntryFromStall(int stall) {
        for (E entry: entries) {
            if (entry.getStall() == stall) return entry;
        }

        return null;
    }

    protected void validateExistingEntries(World world) {
        Iterator<E> it = entries.iterator();
        while (it.hasNext()) {
            E entry = it.next();
            if (entry.isInvalid(world)) {
                it.remove(); //Remove the entries as they are no longer valid
            }
        }
    }

    @Nullable
    public BuildingLocation getLocationFromNPC(@Nonnull NPC npc) {
        for (E entry: entries) {
            if (entry.getNPC() == npc) {
                return locations[entry.getStall() - 1];
            }
        }

        return null;
    }

    protected abstract void createEntry(EntityPlayer player, World world, BlockPos pos, int stall);

    @SuppressWarnings("all")
    protected  <O> O getNextEntry(EntityPlayer player, Set<O> used, O... o) {
        List<O> names = Lists.newArrayList(o);
        Collections.shuffle(names);
        for (O name: names) {
            if (!used.contains(name) && isValid(player, name)) {
                used.add(name);
                return name;
            }
        }

        return o[0];
    }

    private boolean isValid(EntityPlayer player, Object o) {
        return !(o instanceof NPC) || TownHelper.getClosestTownToEntity(player, false).hasNPC((NPC)o);
    }

    public void startContest(EntityPlayer player) {
        World world = player.worldObj;
        if (entries.size() < 4) {
            usedNPCS = new HashSet<>();
            for (int i = 1; i <= 4 && entries.size() < 4; i++) {
                if (!isEntered(i)) {
                    BlockPos pos = TownHelper.getClosestTownToEntity(player, false).getCoordinatesFor(locations[i - 1]);
                    createEntry(player, world, pos, i);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void sort(Q quest, World world) {
        entries.sort((o1, o2) -> ((Integer)o2.getScore(quest, world)).compareTo(o1.getScore(quest, world)));
    }

    public abstract List<Pair<O, Integer>> getAvailableEntries(EntityPlayer player);
    public abstract void enter(EntityPlayer player, O key, int value);

    public boolean isSelecting(EntityPlayer player) {
        return selecting.contains(EntityHelper.getPlayerUUID(player));
    }

    public boolean isEntered(EntityPlayer player) {
        UUID uuid = EntityHelper.getEntityUUID(player);
        for (E entry: entries) {
            if (uuid.equals(entry.getPlayerUUID())) return true;
        }

        return false;
    }

    protected boolean isEntered(int stall) {
        for (E entry: entries) {
            if (stall == entry.getStall()) return true;
        }

        return false;
    }

    public abstract List<Pair<String, Integer>> getAvailableEntryNames(EntityPlayer player);

    protected abstract E fromNBT(NBTTagCompound tag);

    public void readFromNBT(NBTTagCompound nbt) {
        entries.clear(); //Read in the player entries
        NBTTagList entryList = nbt.getTagList("Entries", 10);
        for (int i = 0; i < entryList.tagCount(); i++) {
            NBTTagCompound tag = entryList.getCompoundTagAt(i);
            E entry = fromNBT(tag);
            if (entry != null) {
                entries.add(entry);
            }
        }

        selecting = NBTHelper.readUUIDSet(nbt, "Selecting");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList entryList = new NBTTagList();
        for (E entry: entries) {
            entryList.appendTag(entry.toNBT());
        }

        nbt.setTag("Entries", entryList);

        NBTHelper.writeUUIDSet(nbt, "Selecting", selecting);
        return nbt;
    }
}
