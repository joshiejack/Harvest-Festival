package uk.joshiejack.settlements.world.town.people;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.AdventureConfig;
import uk.joshiejack.settlements.npcs.HomeOverrides;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Spawner {
    private final TownServer town;

    public Spawner(TownServer town) {
        this.town = town;
    }

    private List<Worker> all() {
        List<Worker> list = Lists.newArrayList(NPC.all());
        list.addAll(town.getCensus().getWorkers());
        return list;
    }

    public static class TempWorker implements Worker {
        private final Pair<ResourceLocation, NBTTagCompound> custom;

        public TempWorker(Pair<ResourceLocation, NBTTagCompound> custom) {
            this.custom = custom;
        }

        @Override
        public String getOccupation() {
            return custom.getRight().hasKey("Occupation") ? custom.getRight().getString("Occupation") : NPC.getNPCFromRegistry(custom.getLeft()).getOccupation();
        }
    }

    public interface Worker {
        String getOccupation();
    }

    public EntityNPC byOccupation(World world, String occupation, BlockPos origin) {
        return getNearbyNPCs(world).stream().filter(e -> e.getInfo().getOccupation().equals(occupation)).findFirst()
                .orElseGet(() -> create(world, all().stream().filter(n -> n.getOccupation().equals(occupation)).findFirst().orElse(NPC.NULL_NPC), origin));
    }

    private EntityNPC create(World world, Worker worker, BlockPos origin) {
        if (worker instanceof NPC) return create(world, (NPC) worker, ((NPC)worker).getRegistryName(), new NBTTagCompound(), origin);
        else {
            TempWorker temp = (TempWorker) worker;
            ResourceLocation uniqueID = temp.custom.getRight().hasKey("UniqueID") ? new ResourceLocation(temp.custom.getRight().getString("UniqueID")) : temp.custom.getLeft();
            return create(world, NPC.getNPCFromRegistry(temp.custom.getLeft()), uniqueID, temp.custom.getRight(), origin);
        }
    }

    public EntityNPC getNPC(World world, NPC npc, ResourceLocation search, @Nullable NBTTagCompound custom, BlockPos origin) {
        return getNearbyNPCs(world).stream().filter(e -> e.getBaseNPC() == npc && e.getInfo().getRegistryName().equals(search)).findFirst().orElseGet(() -> create(world, npc, search, custom, origin));
    }

    private EntityNPC create(World world, NPC npc, ResourceLocation search, @Nullable NBTTagCompound custom, BlockPos origin) {
        EntityNPC newNPC = new EntityNPC(world, npc, custom);
        newNPC.getTown();//Set the town by calling the getter
        BlockPos home = town.getLandRegistry().getWaypoint(HomeOverrides.get(search)).getKey();
        BlockPos target = town.equals(TownServer.NULL) || !origin.equals(town.getCentre()) ? origin : home;
        newNPC.setLocationAndAngles(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5, 1F, 1F);
        world.spawnEntity(newNPC);
        return newNPC;
    }

    public List<EntityNPC> getNearbyNPCs(World world) {
        //Remove duplicates
        Set<ResourceLocation> exists = Sets.newHashSet();
        List<EntityNPC> nearby = EntityHelper.getEntities(EntityNPC.class, world, town.getCentre(), AdventureConfig.townDistance, 128D).stream()
                .filter(e -> !e.isDead && e.getTown() == town.getID()).collect(Collectors.toList());
        List<EntityNPC> remaining = Lists.newArrayList();
        for (EntityNPC e : nearby) {
            if (!exists.contains(e.getInfo().getRegistryName())) {
                exists.add(e.getInfo().getRegistryName());
                remaining.add(e);
            } else {
                e.setDead();
            }
        }

        return remaining;
    }
}
