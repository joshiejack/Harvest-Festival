package joshie.harvestmoon.buildings;

import java.util.ArrayList;

import joshie.harvestmoon.buildings.data.EntityData;
import joshie.harvestmoon.buildings.meta.MetaHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.world.World;

public abstract class Building {
    public static final ArrayList<Building> buildings = new ArrayList();

    //Data for all the blocks
    protected Block[] blocks;
    protected int[] metas;
    protected int[] offsetX;
    protected int[] offsetY;
    protected int[] offsetZ;
    protected ArrayList<EntityData>[] entities;
    private String name;
    protected ArrayList tempArray;

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
            
            //boolean n1 = true;
            //boolean n2 = true;
            //boolean swap = false;

            //foundation(world, x, y, z, xWidth, zWidth);
            /** First loop we place solid blocks **/
            for (int i = 0; i < offsetX.length; i++) {
                int y = offsetY[i];
                int x = n1 ? -offsetX[i] : offsetX[i];
                int z = n2 ? -offsetZ[i] : offsetZ[i];
                if (swap) {
                    int xClone = x; //Create a copy of X
                    x = z; //Set x to z
                    z = xClone; //Set z to the old value of x
                }

                Block block = blocks[i];
                if (block instanceof BlockTorch) continue;
                int meta = MetaHelper.convert(block, metas[i], n1, n2, swap);

                if (meta == 0) {
                    world.setBlock(xCoord + x, yCoord + y, zCoord + z, block);
                } else {
                    world.setBlock(xCoord + x, yCoord + y, zCoord + z, block, meta, 2);
                }

                //TODO: Adjust certain blocks for metadata
            }

            /** Second loop we place entities and special blocks like torches **/
            for (int i = 0; i < offsetX.length; i++) {
                int y = offsetY[i];
                int x = n1 ? -offsetX[i] : offsetX[i];
                int z = n2 ? -offsetZ[i] : offsetZ[i];
                if (swap) {
                    int xClone = x; //Create a copy of X
                    x = z; //Set x to z
                    z = xClone; //Set z to the old value of x
                }

                //TODO: Adjust certain blocks for metadata
                Block block = blocks[i];
                if (block instanceof BlockTorch) {
                    int meta = MetaHelper.convert(block, metas[i], n1, n2, swap);
                    if (meta == 0) {
                        world.setBlock(xCoord + x, yCoord + y, zCoord + z, block);
                    } else {
                        world.setBlock(xCoord + x, yCoord + y, zCoord + z, block, meta, 2);
                    }
                }

                //Adjust the entities position accordingly, and then spawn them.
                if (entities != null) {
                    ArrayList<EntityData> entityList = entities[i];
                    if (entityList != null && entityList.size() > 0) {
                        for (EntityData d : entityList) {
                            world.spawnEntityInWorld(d.getEntity(world, xCoord + x, yCoord + y, zCoord + z));
                        }
                    }
                }
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
