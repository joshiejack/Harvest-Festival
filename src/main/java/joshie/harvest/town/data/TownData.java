package joshie.harvest.town.data;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.quests.data.QuestData;
import joshie.harvest.shops.data.ShopData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public abstract class TownData<Q extends QuestData> {
    protected Map<ResourceLocation, TownBuilding> buildings = new HashMap<>();
    protected LinkedList<BuildingStage> building = new LinkedList<>();
    protected final Set<ResourceLocation> inhabitants = new HashSet<>();
    protected final ShopData shops = new ShopData();
    protected CalendarDate birthday;
    protected Quest dailyQuest;
    protected BlockPos townCentre;
    protected UUID uuid;

    /** Overriden to actually return what we should **/
    public abstract Q getQuests();

    public Quest getDailyQuest() {
        return dailyQuest;
    }

    public CalendarDate getBirthday() {
        return birthday;
    }

    public UUID getID() {
        return uuid;
    }

    public ShopData getShops() {
        return shops;
    }

    public TownBuilding getBuilding(Building building) {
        return buildings.get(building.getRegistryName());
    }

    /** Building currently being worked on **/
    public BuildingStage getCurrentlyBuilding() {
        return building.size() > 0 ? building.getFirst() : null;
    }

    /** If this building is being built currently **/
    public boolean isBuilding(Building building) {
        if (building == null) return this.building.size() > 0;
        return this.building.contains(new BuildingStage(building, BlockPos.ORIGIN, Rotation.NONE));
    }

    public boolean hasBuilding(ResourceLocation building) {
        return buildings.get(building) != null;
    }

    public boolean hasBuilding(Building building) {
        return buildings.get(building.getRegistryName()) != null;
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
        if (nbt.hasKey("Created")) birthday = CalendarDate.fromNBT(nbt.getCompoundTag("Created"));
        else birthday = new CalendarDate(1, Season.SPRING, 1);
        shops.readFromNBT(nbt);
        uuid = NBTHelper.readUUID("Town", nbt);
        townCentre = NBTHelper.readBlockPos("TownCentre", nbt);
        NBTHelper.readMap("TownBuildingList", TownBuilding.class, buildings, nbt);
        NBTHelper.readList("CurrentlyBuilding", BuildingStage.class, building, nbt);
        for (TownBuilding building: buildings.values()) {
            inhabitants.addAll(building.building.getInhabitants());
        }

        if (nbt.hasKey("DailyQuest")) dailyQuest = Quest.REGISTRY.getValue(new ResourceLocation(nbt.getString("DailyQuest")));
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Created", birthday.toNBT());
        shops.writeToNBT(nbt);
        NBTHelper.writeBlockPos("TownCentre", nbt, townCentre);
        NBTHelper.writeUUID("Town", nbt, uuid);
        NBTHelper.writeMap("TownBuildingList", nbt, buildings);
        NBTHelper.writeList("CurrentlyBuilding", nbt, building);
        if (dailyQuest != null) nbt.setString("DailyQuest", dailyQuest.getRegistryName().toString());
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
