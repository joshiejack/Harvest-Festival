package joshie.harvestmoon.buildings;

import java.util.ArrayList;

import joshie.harvestmoon.buildings.placeable.Placeable;
import joshie.harvestmoon.buildings.placeable.Placeable.PlacementStage;
import net.minecraft.world.World;

public abstract class Building {
    public static final ArrayList<Building> buildings = new ArrayList();

    //List of all placeable elements
    protected ArrayList<Placeable> list;
    private String name;

    public String getName() {
        return name;
    }

    public Building setName(String name) {
        this.name = name;
        return this;
    }

    public Building init() {
        return this;
    }

    public boolean generate(World world, int xCoord, int yCoord, int zCoord) {
        if (!world.isRemote) {
            boolean n1 = world.rand.nextBoolean();
            boolean n2 = world.rand.nextBoolean();
            boolean swap = world.rand.nextBoolean();

           // boolean n1 = true;
           // boolean n2 = false;
           /// boolean swap = false;

            //foundation(world, x, y, z, xWidth, zWidth);
            /** First loop we place solid blocks **/
            for (Placeable block : list) {
                block.place(world, xCoord, yCoord, zCoord, n1, n2, swap, PlacementStage.BLOCKS);
            }

            /** Second loop we place torch/ladders etc **/
            for (Placeable block : list) {
                block.place(world, xCoord, yCoord, zCoord, n1, n2, swap, PlacementStage.TORCHES);
            }

            /** Third loop we place entities etc. **/
            for (Placeable block : list) {
                block.place(world, xCoord, yCoord, zCoord, n1, n2, swap, PlacementStage.ENTITIES);
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
