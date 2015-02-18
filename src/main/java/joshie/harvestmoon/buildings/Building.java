package joshie.harvestmoon.buildings;

import java.util.ArrayList;

import joshie.harvestmoon.blocks.render.PreviewBlock;
import joshie.harvestmoon.buildings.placeable.Placeable;
import joshie.harvestmoon.buildings.placeable.Placeable.PlacementStage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Building {
    //List of all placeable elements
    protected ArrayList<Placeable> list;
    private PreviewBlock preview;
    private int index;

    public IBlockAccess getBlockAccess(int worldX, int worldY, int worldZ, boolean n1, boolean n2, boolean swap) {
        return preview.setCoordinatesAndDirection(worldX, worldY, worldZ, n1, n2, swap);
    }
    
    public int getInt() {
        return index;
    }

    public void init(int index) {
        preview = new PreviewBlock(list);
        this.index = index;
    }

    public int getSize() {
        return list.size();
    }

    public Placeable get(int index) {
        return list.get(index);
    }

    public ArrayList<Placeable> getList() {
        return list;
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

            /** Third loop we place torch/ladders etc **/
            for (Placeable block : list) {
                block.place(world, xCoord, yCoord, zCoord, n1, n2, swap, PlacementStage.TORCHES);
            }

            /** Fourth loop we place NPCs **/
            for (Placeable block : list) {
                block.place(world, xCoord, yCoord, zCoord, n1, n2, swap, PlacementStage.NPC);
            }
        }

        return true;
    }
}
