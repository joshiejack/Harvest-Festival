package harvestmoon.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BuildingCodeGenerator {
    private static final HashMap<String, String> names = new HashMap();

    private World world;
    private int x1, y1, z1, x2, y2, z2;

    public BuildingCodeGenerator(World world, int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd) {
        this.world = world;
        this.x1 = xStart < xEnd ? xStart : xEnd;
        this.x2 = xStart < xEnd ? xEnd : xStart;
        this.y1 = yStart < yEnd ? yStart : yEnd;
        this.y2 = yStart < yEnd ? yEnd : yStart;
        this.z1 = zStart < zEnd ? zStart : zEnd;
        this.z2 = zStart < zEnd ? zEnd : zStart;
    }

    public ArrayList<String> getArea(boolean air) {
        ArrayList<String> ret = new ArrayList();
        for (int x = 0; x <= x2 - x1; x++) {
            for (int y = 0; y <= y2 - y1; y++) {
                for (int z = 0; z <= z2 - z1; z++) {
                    Block block = world.getBlock(x1 + x, y1 + y, z1 + z);
                    if (!block.isAir(world, x1 + x, y1 + y, z1 + z) || air) {
                        String print = Block.blockRegistry.getNameForObject(block);
                        int meta = world.getBlockMetadata(x1 + x, y1 + y, z1 + z);
                        ret.add(x + "," + y + "," + z + "," + print + "," + meta);
                    }
                }
            }
        }

        return ret;
    }

    public void getCode(boolean air) {
        System.out.println(x2);
        
        if (!world.isRemote) {
            ArrayList<String> ret = new ArrayList();
            ret.add("START");
            int i = 0;
            for (int x = 0; x <= x2 - x1; x++) {
                for (int z = 0; z <= z2 - z1; z++) {
                    for (int y = 0; y <= y2 - y1; y++) {
                        Block block = world.getBlock(x1 + x, y1 + y, z1 + z);
                        if (!block.isAir(world, x1 + x, y1 + y, z1 + z) || air) {
                            String name = Block.blockRegistry.getNameForObject(block).replace("minecraft:", "");
                            String print = "Blocks." + name;
                            if (names.containsKey(name)) {
                                print = names.get(name);
                            }

                            int meta = world.getBlockMetadata(x1 + x, y1 + y, z1 + z);
                            ret.add("blocks[" + i + "] = " + print + ";");
                            ret.add("metas[" + i + "] = " + meta + ";");
                            ret.add("offsetX[" + i + "] = " + x + ";");
                            ret.add("offsetY[" + i + "] = " + y + ";");
                            ret.add("offsetZ[" + i + "] = " + z + ";");
                            
                            i++;
                        }
                    }
                }
            }

            ret.add("END");
            ret.add("blocks = new Block[" + i + "];");
            ret.add("metas = new int[" + i + "];");
            ret.add("offsetX = new int[" + i + "];");
            ret.add("offsetY = new int[" + i + "];");
            ret.add("offsetZ = new int[" + i + "];");

            for (String s : ret) {
                System.out.println(s);
            }
        }
    }
}
