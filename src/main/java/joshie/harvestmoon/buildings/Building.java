package joshie.harvestmoon.buildings;

import java.util.ArrayList;
import java.util.Random;

import joshie.harvestmoon.buildings.placeable.Placeable;
import joshie.harvestmoon.buildings.placeable.Placeable.PlacementStage;
import net.minecraft.world.World;

public class Building {
    public static final ArrayList<Building> buildings = new ArrayList(50);
    private ArrayList<Building> subTypes = new ArrayList();

    //List of all placeable elements
    protected ArrayList<Placeable> list;
    private String name;
    private boolean isVariant;

    public static Building register(String string, Building... buildings) {
        Building toRegister = new Building().setName(string).add(buildings);
        Building.buildings.add(toRegister);
        return toRegister;
    }

    public ArrayList<Placeable> getSubList(int subType) {
        return subTypes.get(subType).list;
    }

    public Building add(Building... buildings) {
        for (Building building : buildings) {
            subTypes.add(building);
        }

        return this;
    }

    /** Returns a random sub building **/
    public int random(Random rand) {
        if (subTypes.size() == 0 || subTypes.size() == 1) return 0;
        return rand.nextInt(subTypes.size());
    }

    public Building getRandom() {
        if (subTypes.size() == 0) return null;
        else return subTypes.get(new Random().nextInt(subTypes.size()));
    }

    public boolean isVariant() {
        return isVariant;
    }

    public Building setIsVariant() {
        isVariant = true;
        return this;
    }

    public String getName() {
        return name;
    }

    public Building setName(String name) {
        this.name = name;
        return this;
    }

    public static Building getBuilding(String string) {
        for (Building b : buildings) {
            if (b.getName().equals(string)) {
                return b;
            }
        }

        return null;
    }

    //TODO: LilyPad/Tripwire Hooks????????????
    public boolean generate(World world, int xCoord, int yCoord, int zCoord) {
        if (!world.isRemote) {
            boolean n1 = world.rand.nextBoolean();
            boolean n2 = world.rand.nextBoolean();
            boolean swap = world.rand.nextBoolean();

            /** First loop we place solid blocks **/
            for (Placeable block : list) {
                block.place(world, xCoord, yCoord, zCoord, n1, n2, swap, PlacementStage.BLOCKS);
            }

            /** Second loop we place entities etc. **/
            for (Placeable block : list) {
                block.place(world, xCoord, yCoord, zCoord, n1, n2, swap, PlacementStage.ENTITIES);
            }

            /** First loop we place torch/ladders etc **/
            for (Placeable block : list) {
                block.place(world, xCoord, yCoord, zCoord, n1, n2, swap, PlacementStage.TORCHES);
            }
        }

        return true;
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
