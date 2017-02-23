package joshie.harvest.quests.town.festivals.contest;

import com.google.common.collect.Lists;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.quests.town.festivals.Place;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ContestEntries<E extends EntityAnimal> {
    private final List<ContestEntry> entries = new ArrayList<>();
    private final Class<E> entityClass;
    private final BuildingLocation[] locations;
    private final NPC[] npcs;
    private final String[] names;
    private Set<UUID> selecting = new HashSet<>();
    private List<Pair<String, Integer>> animalNames;

    public ContestEntries(Class<E> entity, BuildingLocation[] locations, NPC[] npcs, String[] names) {
        this.entityClass = entity;
        this.locations = locations;
        this.npcs = npcs;
        this.names = names;
    }

    public void setAnimalNames(List<Pair<String, Integer>> list) {
        this.animalNames = list;
    }

    public List<Pair<String, Integer>> getNames() {
        return animalNames;
    }

    public List<Pair<E, Integer>> getAvailableEntries(EntityPlayer player) {
        List<Pair<E, Integer>> list = Lists.newArrayList();
        for (int i = 0; i < locations.length; i++) {
            BuildingLocation location = locations[i];
            int stall = i + 1;
            if (!isEntered(stall)) {
                E animal = getClosestAnimal(player.worldObj, TownHelper.getClosestTownToEntity(player, false).getCoordinatesFor(location));
                if (animal != null) {
                    Pair<E, Integer> pair = Pair.of(animal, stall);
                    if (!list.contains(pair)) {
                        list.add(pair);
                    }
                }
            }
        }

        return list;
    }

    void enter(EntityPlayer player, E animal, int stall) {
        UUID playerUUID = EntityHelper.getPlayerUUID(player);
        UUID animalUUID = EntityHelper.getEntityUUID(animal);
        //Wipe out any entries that match the exist
        Iterator<ContestEntry> it = entries.iterator();
        while (it.hasNext()) {
            ContestEntry entry = it.next();
            if (entry.getStall() == stall || playerUUID.equals(entry.getUUID()) || animalUUID.equals(entry.getAnimal())) {
                it.remove();
            }
        }

        entries.add(new ContestEntry(playerUUID, animalUUID, stall));
        selecting.remove(playerUUID);
    }

    @SuppressWarnings("all")
    private <O> O getNextEntry(Set<O> used, O... o) {
        List<O> names = Lists.newArrayList(o);
        Collections.shuffle(names);
        for (O name: names) {
            if (!used.contains(name)) {
                used.add(name);
                return name;
            }
        }

        return o[0];
    }

    private E createEntity(World world) {
        E entity = null;
        try {
            if (entityClass != null) {
                entity = entityClass.getConstructor(new Class[]{World.class}).newInstance(world);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) { /**/}

        return entity;
    }

    @SuppressWarnings("ConstantConditions")
    //After this is called, we need to sync up the entries to the clients
    public void startContest(EntityPlayer player) {
        World world = player.worldObj;
        if (entries.size() < 4) {
            Set<String> used = new HashSet<>();
            Set<NPC> usedNPCS = new HashSet<>();
            entries.stream().forEach(e -> used.add(e.getName(world)));
            for (int i = 1; i <= 4 && entries.size() < 4; i++) {
                if (!isEntered(i)) {
                    BlockPos pos = TownHelper.getClosestTownToEntity(player, false).getCoordinatesFor(locations[i - 1]);
                    E animal = createEntity(world);
                    animal.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
                    animal.setCustomNameTag(getNextEntry(used, names));
                    world.spawnEntityInWorld(animal);
                    AnimalStats stats = EntityHelper.getStats(animal);
                    if (stats != null) {
                        stats.setProduced(5); //Forbid the animal making products
                        stats.setDead(); //Autokill the animal if it persists
                        //stats.affectHappiness(world.rand.nextInt(20000)); //Give the animal a random happiness
                    }

                    entries.add(new ContestEntry(getNextEntry(usedNPCS, npcs), EntityHelper.getEntityUUID(animal), i));
                }
            }
        }

        sort(world); //Sort the contents
    }

    public void sort(World world) {
        entries.sort((o1, o2) -> ((Integer)o2.getScore(world)).compareTo(o1.getScore(world)));
    }

    private E getClosestAnimal(World world, BlockPos pos) {
        List<E> animals = EntityHelper.getEntities(entityClass, world, pos, 5D, 5D);
        double d0 = -1.0D;
        E closest = null;
        for (E animal: animals) {
            double d1 = animal.getDistanceSq(pos);
            if ((d1 < 5D * 5D) && (d0 == -1.0D || d1 < d0))  {
                d0 = d1;
                closest = animal;
            }
        }

        return closest;
    }

    public boolean isSelecting(EntityPlayer player) {
        return selecting.contains(EntityHelper.getPlayerUUID(player));
    }

    public boolean isEntered(int stall) {
        for (ContestEntry entry: entries) {
            if (stall == entry.getStall()) return true;
        }

        return false;
    }

    public boolean isEntered(EntityPlayer player) {
        UUID uuid = EntityHelper.getEntityUUID(player);
        for (ContestEntry entry: entries) {
            if (uuid.equals(entry.getUUID())) return true;
        }

        return false;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        entries.clear(); //Read in the player entries
        NBTTagList entryList = nbt.getTagList("Entries", 10);
        for (int i = 0; i < entryList.tagCount(); i++) {
            NBTTagCompound tag = entryList.getCompoundTagAt(i);
            ContestEntry entry = ContestEntry.fromNBT(tag);
            if (entry != null) {
                entries.add(entry);
            }
        }

        selecting = NBTHelper.readUUIDSet(nbt, "Selecting");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList entryList = new NBTTagList();
        for (ContestEntry entry: entries) {
            entryList.appendTag(entry.toNBT());
        }

        nbt.setTag("Entries", entryList);
        NBTHelper.writeUUIDSet(nbt, "Selecting", selecting);
        return nbt;
    }

    public Set<UUID> getSelecting() {
        return selecting;
    }

    public NPC[] getNPCs() {
        return npcs;
    }

    public void kill(World world) {
        for (ContestEntry entry: entries) {
            if (entry.getUUID() == null) {
                E animal = EntityHelper.getAnimalFromUUID(world, entry.getAnimal());
                if (animal != null) {
                    animal.setDead();
                }
            }
        }
    }

    public ContestEntry getEntry(Place place) {
        return entries.get(place.ordinal());
    }

    @Nullable
    public ContestEntry getEntryFromStall(int stall) {
        for (ContestEntry entry: entries) {
            if (entry.getStall() == stall) return entry;
        }

        return null;
    }
}
