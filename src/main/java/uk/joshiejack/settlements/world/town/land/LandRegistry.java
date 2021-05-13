package uk.joshiejack.settlements.world.town.land;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.building.BuildingUpgrades;
import uk.joshiejack.settlements.AdventureConfig;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.util.helpers.minecraft.BlockPosHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static uk.joshiejack.settlements.world.town.land.Interaction.FOOTPRINT;

public class LandRegistry implements INBTSerializable<NBTTagCompound> {
    private final Multimap<Building, TownBuilding> buildings = HashMultimap.create();
    private final Cache<BlockPos, TownBuilding> closest = CacheBuilder.newBuilder().build();
    private final EnumMap<Interaction, List<BlockPos>> footprint = new EnumMap<>(Interaction.class);
    private final Town<?> town;

    public LandRegistry(Town<?> town) {
        this.town = town;
    }

    public void addBuilding(World world, TownBuilding townBuilding) {
        buildings.get(townBuilding.getBuilding()).add(townBuilding);
        if (BuildingUpgrades.overrides(townBuilding.getBuilding())) { //Remove the overidden building?
            Building original = BuildingUpgrades.getTargetForUpgrade(townBuilding.getBuilding());
            buildings.get(original).removeIf(t -> t.getPosition().equals(townBuilding.getPosition()));
        }

        onBuildingsChanged(); //Reload them bitch
        TownFinder.getFinder(world).clearCache(); //Reset the cache for the towns
    }

    public void removeBuilding(World world, TownBuilding townBuilding) {
        buildings.get(townBuilding.getBuilding()).remove(townBuilding);
        onBuildingsChanged(); //Reload them bitch
        TownFinder.getFinder(world).clearCache(); //Reset the cache for the towns
    }

    public void onBuildingsChanged() {
        closest.invalidateAll();
        footprint.clear();
        town.getCensus().onBuildingsChanged(buildings);
    }

    public void onNewDay(World world) {
        buildings.values().forEach(b -> b.newDay(world, town));
    }

    public int getBuildingCount(Building building) {
        return buildings.get(building).size();
    }

    public int uniqueBuildingsCount() {
        return buildings.size();
    }

    @Nonnull
    public TownBuilding getBuildingLocation(Building building) {
        return buildings.containsKey(building) ? buildings.get(building).iterator().next() : TownBuilding.NULL;
    }


    public Pair<BlockPos, Rotation> getWaypoint(String waypoint) {
        for (Map.Entry<Building, TownBuilding> entry : buildings.entries()) {
            if (entry.getKey().hasWaypoint(waypoint)) {
                return Pair.of(BlockPosHelper.getTransformedPosition(entry.getKey().getWaypoint(waypoint), entry.getValue().getPosition(), entry.getValue().getRotation()), entry.getValue().getRotation());
            }
        }

        return Pair.of(town.getCentre(), Rotation.NONE);
    }

    @Nonnull
    public TownBuilding getClosestBuilding(World world, BlockPos target) {
        try {
            return closest.get(target, () -> {
                TownBuilding closest = TownBuilding.NULL;
                double distance = Double.MAX_VALUE;
                for (TownBuilding location : buildings.values()) {
                    //BlockPos buildingPos = location.getCentre();
                    for (BlockPos buildingPos : location.getFootprint(world, FOOTPRINT)) {
                        double between = target.getDistance(buildingPos.getX(), buildingPos.getY(), buildingPos.getZ());
                        if (between < distance) {
                            closest = location;
                            distance = between;
                        }
                    }
                }

                return closest;
            });
        } catch (ExecutionException e) {
            return null;
        }
    }

    public double getDistance(BlockPos pos) {
        double distance = Double.MAX_VALUE;
        for (TownBuilding building : buildings.values()) {
            double placementDistance = building.getCentre().getDistance(pos.getX(), pos.getY(), pos.getZ());
            if (placementDistance < AdventureConfig.townDistance && placementDistance < distance) {
                distance = placementDistance;
            }
        }

        return distance;
    }

    public List<BlockPos> getFootprints(World world, Interaction interaction) {
        if (!footprint.containsKey(interaction)) {
            List<BlockPos> list = Lists.newArrayList();
            buildings.values().forEach(b -> list.addAll(b.getFootprint(world, interaction)));
            footprint.put(interaction, list);
            return list;
        } else return footprint.get(interaction);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        NBTTagList list = tag.getTagList("Buildings", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            Building building = Building.REGISTRY.get(new ResourceLocation(nbt.getString("Building")));
            BlockPos pos = BlockPos.fromLong(nbt.getLong("Pos"));
            Rotation rotation = Rotation.values()[nbt.getByte("Rotation")];
            TownBuilding townBuilding = new TownBuilding(building, pos, rotation);
            if (nbt.getBoolean("Built")) townBuilding.setBuilt();
            buildings.get(building).add(townBuilding);
        }

        onBuildingsChanged();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList buildingList = new NBTTagList();
        buildings.values().forEach((b) -> {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("Building", b.getBuilding().getRegistryName().toString());
            nbt.setLong("Pos", b.getPosition().toLong());
            nbt.setByte("Rotation", (byte) b.getRotation().ordinal());
            nbt.setBoolean("Built", b.isBuilt());
            buildingList.appendTag(nbt);
        });

        tag.setTag("Buildings", buildingList);
        return tag;
    }
}


