package joshie.harvestmoon.buildings;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.item.ItemStack;
import joshie.harvestmoon.api.buildings.IBuildingGroup;
import joshie.harvestmoon.init.HMBlocks;

public class BuildingGroup implements IBuildingGroup {
    public static final ArrayList<BuildingGroup> groups = new ArrayList(50);
    private ArrayList<Building> buildings = new ArrayList();

    //List of all placeable elements
    private String name;
    private int meta;

    public static BuildingGroup register(String string, Building... buildingsList) {
        BuildingGroup toRegister = new BuildingGroup().setName(string);
        toRegister.meta = groups.size();
        
        int index = 0;
        for (Building building : buildingsList) {
            building.init(toRegister, index);
            toRegister.buildings.add(building);
            index++;
        }

        BuildingGroup.groups.add(toRegister);
        return toRegister;
    }

    public static BuildingGroup getGroup(String string) {
        for (BuildingGroup b : groups) {
            if (b.getName().equals(string)) {
                return b;
            }
        }

        return null;
    }
    
    public ItemStack getPreview() {
        return new ItemStack(HMBlocks.preview, 1, meta);
    }
    
    public Building getBuilding(int key) {
        return buildings.get(key);
    }

    /** Returns a random building key for this building**/
    public int random(Random rand) {
        if (buildings.size() == 0 || buildings.size() == 1) return 0;
        return rand.nextInt(buildings.size());
    }

    /** Returns a random building in this group **/
    public Building getRandom() {
        if (buildings.size() == 0) return null;
        else return buildings.get(new Random().nextInt(buildings.size()));
    }

    public String getName() {
        return name;
    }

    public BuildingGroup setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return name.equals(o);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
