package joshie.harvest.town.data;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.quests.data.QuestData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public abstract class TownData<Q extends QuestData> {
    protected Map<ResourceLocation, TownBuilding> buildings = new HashMap<>();
    protected LinkedList<BuildingStage> building = new LinkedList<>();
    protected final Set<ResourceLocation> inhabitants = new HashSet<>();
    protected BlockPos townCentre;
    protected UUID uuid;

    /** Overriden to actually return what we should **/
    public abstract Q getQuests();

    public UUID getID() {
        return uuid;
    }

    public TownData setUUID(UUID UUID) {
        uuid = UUID;
        return this;
    }

    /** Building currently being worked on **/
    public BuildingStage getCurrentlyBuilding() {
        return building.size() > 0 ? building.getFirst() : null;
    }

    /** If this building is being built currently **/
    public boolean isBuilding(BuildingImpl building) {
        if (building == null) return this.building.size() > 0;
        return this.building.contains(new BuildingStage(building, BlockPos.ORIGIN, Rotation.NONE));
    }

    public boolean hasBuilding(ResourceLocation building) {
        return buildings.get(building) != null;
    }

    public boolean hasBuilding(Building building) {
        return buildings.get(((BuildingImpl)building).getRegistryName()) != null;
    }

    public boolean hasBuildings(ResourceLocation[] buildings) {
        for (ResourceLocation building: buildings) {
            if (this.buildings.get(building) == null) return false;
        }

        return true;
    }

    public BlockPos getCoordinatesFor(BuildingLocation location) {
        if (location == null) return null;
        TownBuilding building = buildings.get(location.getResource());
        if (building == null) return null;
        return building.getRealCoordinatesFor(location.getLocation());
    }

    public Set<ResourceLocation> getInhabitants() {
        return inhabitants;
    }

    public Collection<TownBuilding> getBuildings() {
        return buildings.values();
    }

    public BlockPos getTownCentre() {
        return townCentre;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        uuid = NBTHelper.readUUID("Town", nbt);
        townCentre = NBTHelper.readBlockPos("TownCentre", nbt);
        NBTHelper.readMap("TownBuildingList", TownBuilding.class, buildings, nbt);
        NBTHelper.readList("CurrentlyBuilding", BuildingStage.class, building, nbt);
        for (TownBuilding building: buildings.values()) {
            inhabitants.addAll(building.building.getInhabitants());
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTHelper.writeBlockPos("TownCentre", nbt, townCentre);
        NBTHelper.writeUUID("Town", nbt, uuid);
        NBTHelper.writeMap("TownBuildingList", nbt, buildings);
        NBTHelper.writeList("CurrentlyBuilding", nbt, building);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TownData townData = (TownData) o;
        return uuid != null ? uuid.equals(townData.uuid) : townData.uuid == null;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
