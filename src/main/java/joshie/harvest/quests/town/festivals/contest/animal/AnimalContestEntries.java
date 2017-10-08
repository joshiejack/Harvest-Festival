package joshie.harvest.quests.town.festivals.contest.animal;

import com.google.common.collect.Lists;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.quests.base.QuestAnimalContest;
import joshie.harvest.quests.town.festivals.contest.ContestEntries;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AnimalContestEntries<E extends EntityAnimal> extends ContestEntries<E, AnimalContestEntry, QuestAnimalContest> {
    private final Class<E> entityClass;
    private final String[] names;
    private Set<String> used;

    public AnimalContestEntries(Class<E> entity, BlockPos[] locations, NPC[] npcs, String[] names) {
        super(locations, npcs);
        this.entityClass = entity;
        this.names = names;
    }

    @Override
    public List<Pair<E, Integer>> getAvailableEntries(EntityPlayer player) {
        //Validate the existing entries
        validateExistingEntries(player.world);

        //Grab the list of closest
        List<Pair<E, Integer>> list = Lists.newArrayList();
        for (int i = 0; i < locations.length; i++) {
            BlockPos location = locations[i];
            int stall = i + 1;
            if (!isEntered(stall)) {
                BlockPos target = HFApi.towns.getTownForEntity(player).getCoordinatesFromOffset(HFBuildings.FESTIVAL_GROUNDS, location);
                E animal = getClosestAnimal(player.world, target);
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

    @Override
    public void enter(EntityPlayer player, E animal, int stall) {
        UUID playerUUID = EntityHelper.getPlayerUUID(player);
        UUID animalUUID = EntityHelper.getEntityUUID(animal);
        //Wipe out any entries that match the exist
        Iterator<AnimalContestEntry> it = entries.iterator();
        while (it.hasNext()) {
            AnimalContestEntry entry = it.next();
            if (entry.getStall() == stall || playerUUID.equals(entry.getPlayerUUID()) || animalUUID.equals(entry.getAnimalUUID())) {
                it.remove();
            }
        }

        entries.add(new AnimalContestEntry(playerUUID, animalUUID, stall));
        selecting.remove(playerUUID);
    }

    @Override
    public List<Pair<String, Integer>> getAvailableEntryNames(EntityPlayer player) {
        List<Pair<String, Integer>> list = new ArrayList<>();
        for (Pair<E, Integer> pair: getAvailableEntries(player)) {
            list.add(Pair.of(pair.getKey().getName(), pair.getValue()));
        }

        return list;
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

    @Override
    protected void createEntry(EntityPlayer player, World world, BlockPos pos, int stall) {
        E animal = createEntity(world);
        animal.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
        animal.setCustomNameTag(getNextEntry(player, used, names));
        world.spawnEntity(animal);
        AnimalStats stats = EntityHelper.getStats(animal);
        if (stats != null) {
            animal.setEntityInvulnerable(true);
            stats.setProduced(5); //Forbid the animal making products
            stats.setDead(); //Autokill the animal the next day if it somehow persists
            stats.affectHappiness(world.rand.nextInt(20000)); //Give the animal a random happiness
        }

        entries.add(new AnimalContestEntry(getNextEntry(player, usedNPCS, npcs), EntityHelper.getEntityUUID(animal), stall));
    }

    @SuppressWarnings("ConstantConditions")
    //After this is called, we need to sync up the entries to the clients
    @Override
    public void startContest(EntityPlayer player) {
        World world = player.world;
        if (entries.size() < 4) {
            used = new HashSet<>();
            entries.stream().forEach(e -> used.add(e.getName(world)));
        }

        super.startContest(player);
    }

    @Override
    protected AnimalContestEntry fromNBT(NBTTagCompound tag) {
        return AnimalContestEntry.fromNBT(tag);
    }

    private E getClosestAnimal(World world, BlockPos pos) {
        List<E> animals = EntityHelper.getEntities(entityClass, world, pos, 5D, 5D);
        double d0 = -1.0D;
        E closest = null;
        for (E animal: animals) {
            if (animal.isDead || animal.isAirBorne) continue;
            double d1 = animal.getDistanceSq(pos);
            if ((d1 < 5D * 5D) && (d0 == -1.0D || d1 < d0))  {
                d0 = d1;
                closest = animal;
            }
        }

        return closest;
    }

    @Override
    public void complete(World world) {
        entries.stream().filter(entry -> entry.getPlayerUUID() == null).forEach(entry -> {
            E animal = EntityHelper.getAnimalFromUUID(world, entry.getAnimalUUID());
            if (animal != null) {
                animal.setDead();
            }
        });
    }
}
