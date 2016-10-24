package joshie.harvest.town;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.town.packet.PacketNewBuilding;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class TownData {
    public static final String CARPENTER_DOWNSTAIRS = "carpenter.downstairs";
    public static final String CARPENTER_DOOR = "carpenter.door";
    public static final String JADE_HOME = "jade";
    public static final String TIBERIUS_HOME = "tiberius";
    public static final String FENN_HOME = "fenn";
    public static final String CLOCKWORKER_DOOR = "clockworker.door";
    public static final String CANDICE_HOME = "candice";
    public static final String MARKET_TILL = "market.till";
    public static final String MARKET_STOREFRONT = "market.customer";
    public static final String MARKET_STAIRS_TOP = "market.stairs";
    public static final String MARKET_DOOR = "market.entrance";
    public static final String MARKET_TABLES = "market.tables";
    public static final String MARKET_BACKYARD = "market.backyard";
    public static final String MARKET_GARDEN = "market.garden";
    public static final String MARKET_BASEMENT = "market.basement";
    public static final String GIRAFI_HOME = "girafi";
    public static final String JENNI_HOME = "jenni";
    public static final String JAMIE_HOME = "jamie";
    public static final String CLOE_HOME = "cloe";
    public static final String ABI_HOME = "abi";
    public static final String TOWNHALL_ADULT_BEDROOM = "townhall.bedroom.adult";
    public static final String TOWNHALL_TEEN_BEDROOM = "townhall.bedroom.teen";
    public static final String TOWNHALL_CHILD_BEDROOM = "townhall.bedroom.child";
    public static final String TOWNHALL_ENTRANCE = "townhall.door";
    public static final String TOWNHALL_CENTRE = "townhall.entrance";
    public static final String TOWNHALL_LEFT_WING = "townhall.wing.left";
    public static final String TOWNHALL_RIGHT_WING = "townhall.wing.right";
    public static final String TOWNHALL_FRONT_OF_STAGE = "townhall.stage.front";
    public static final String TOWNHALL_LEFT_OF_STAGE = "townhall.stage.left";
    public static final String TOWNHALL_RIGHT_OF_STAGE = "townhall.stage.right";
    public static final String LIARA_HOME = "liara";
    public static final String KATLIN_HOME = "katlin";
    public static final String CAFE_FRONT = "cafe.entrance";
    public static final String CAFE_TILL = "cafe.till";
    public static final String CAFE_DOOR = "cafe.door";
    public static final String CAFE_STAIRS = "cafe.stairs";
    public static final String CAFE_CUSTOMER = "cafe.customer";
    public static final String MINE_ENTRANCE = "mine.front";
    public static final String MINE_STAIRS = "mine.stairs";
    public static final String MINE_RIGHT = "mine.right";
    public static final String MINE_BACK = "mine.back";
    public static final String MINER_FRONT = "miner.door";
    public static final String MINER_HALL = "miner.hall";
    public static final String MINER_GRAVEL = "miner.gravel";
    public static final String BRANDON_HOME = "brandon";
    public static final String ASHLEE_HOME = "ashlee";
    public static final String POULTRY_FRONT = "poultry.front";
    public static final String POULTRY_DOOR = "poultry.door";
    public static final String FISHING_POND = "pond.pier";
    public static final String FISHING_POND_LEFT = "pond.left";
    public static final String FISHING_POND_RIGHT = "pond.right";
    public static final String FISHING_POND_BACK = "pond.back";
    public static final String JACOB_HOME = "jacob";
    public static final String FISHER_RIGHT = "fisher.right";
    public static final String FISHER_LEFT = "fisher.left";
    public static final String FISHER_DOOR = "fisher.door";
    public static final String FISHER_UPSTAIRS = "fisher.upstairs";
    public static final String THOMAS = "thomas";
    public static final String CHURCH_FRONT = "church.door";
    public static final String CHURCH_LEFT = "church.left";
    public static final String CHURCH_RIGHT = "church.right";
    public static final String CHURCH_PEW_FRONT_RIGHT = "church.pew.right.front";
    public static final String CHURCH_PEW_FRONT_LEFT = "church.pew.left.front";
    public static final String CHURCH_PEW_CENTRE = "church.pew.centre";
    public static final String CHURCH_PEW_BACK_RIGHT = "church.pew.right.back";
    public static final String CHURCH_PEW_BACK_LEFT = "church.pew.left.back";
    public static final String DANIEL_HOME = "daniel";
    public static final String BLACKSMITH_FRONT = "blacksmith.entrance";
    public static final String BLACKSMITH_DOOR = "blacksmith.door";
    public static final String JIM_HOME = "jim";
    public static final String BARN_LEFT = "barn.left";
    public static final String BARN_RIGHT = "barn.right";
    public static final String BARN_DOOR = "barn.door";
    public static final String GODDESS_BACK = "goddess.back";
    public static final String GODDESS_BACK_LEFT = "goddess.left.back";
    public static final String GODDESS_BACK_RIGHT = "goddess.right.back";
    public static final String GODDESS_MIDDLE_RIGHT = "goddess.right.middle";
    public static final String GODDESS_MIDDLE_LEFT = "goddess.left.middle";
    public static final String GODDESS_FRONT_RIGHT = "goddess.right";
    public static final String GODDESS_FRONT_LEFT = "goddess.left";
    public static final String GODDESS_HOME = "goddess.front";
    public static final String GODDESS_WATER = "goddess.water";
    //END NPC NAME OF LOCATIONS **/
    
    protected final Map<ResourceLocation, TownBuilding> buildings = new HashMap<>();
    protected LinkedList<BuildingStage> building = new LinkedList<>();
    protected BlockPos townCentre;
    protected UUID uuid;

    public UUID getID() {
        return uuid;
    }

    public BuildingStage getCurrentlyBuilding() {
        return building.size() > 0 ? building.getFirst() : null;
    }

    public boolean isBuilding(BuildingImpl building) {
        if (building == null) return this.building.size() > 0;
        return this.building.contains(new BuildingStage(building, BlockPos.ORIGIN, Rotation.NONE));
    }
    
    public void addBuilding(World world, BuildingImpl building, Rotation rotation, BlockPos pos) {
        TownBuilding newBuilding = new TownBuilding(building, rotation, pos);
        buildings.put(BuildingRegistry.REGISTRY.getKey(building), newBuilding);
        PacketHandler.sendToDimension(world.provider.getDimension(), new PacketNewBuilding(uuid, newBuilding));
        HFTrackers.markDirty(world);
    }

    public void addBuilding(TownBuilding building) {
        buildings.put(BuildingRegistry.REGISTRY.getKey(building.building), building);
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

    public void newDay(World world) {}

    public BlockPos getCoordinatesFor(BuildingLocation location) {
        if (buildings == null || location == null) return null;
        TownBuilding building = buildings.get(location.getResource());
        if (building == null) return null;
        return building.getRealCoordinatesFor(location.getLocation());
    }

    public Rotation getFacingFor(ResourceLocation resource) {
        TownBuilding building = buildings.get(resource);
        if (building == null) return null;
        return building.getFacing();
    }

    public BlockPos getTownCentre() {
        return townCentre;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        uuid = NBTHelper.readUUID("Town", nbt);
        townCentre = NBTHelper.readBlockPos("TownCentre", nbt);
        NBTTagList list = nbt.getTagList("TownBuildingList", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            TownBuilding building = new TownBuilding();
            building.readFromNBT(tag);
            if (building.building != null) {
                buildings.put(BuildingRegistry.REGISTRY.getKey(building.building), building);
            }
        }

        //Currently Building
        NBTTagList currently = nbt.getTagList("CurrentlyBuilding", 10);
        for (int i = 0; i < currently.tagCount(); i++) {
            NBTTagCompound tag = currently.getCompoundTagAt(i);
            building.add(BuildingStage.readFromNBT(tag));
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTHelper.writeBlockPos("TownCentre", nbt, townCentre);
        NBTHelper.writeUUID("Town", nbt, uuid);
        NBTTagList list = new NBTTagList();
        for (ResourceLocation name : buildings.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            buildings.get(name).writeToNBT(tag);
            list.appendTag(tag);
        }

        nbt.setTag("TownBuildingList", list);

        //Currently
        NBTTagList currently = new NBTTagList();
        for (BuildingStage stage: building) {
            NBTTagCompound tag = new NBTTagCompound();
            stage.writeToNBT(tag);
            currently.appendTag(tag);
        }

        nbt.setTag("CurrentlyBuilding", currently);
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

    public TownData setUUID(UUID UUID) {
        uuid = UUID;
        return this;
    }


}
