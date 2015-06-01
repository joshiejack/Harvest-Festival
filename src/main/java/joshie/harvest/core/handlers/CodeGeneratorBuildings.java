package joshie.harvest.core.handlers;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import joshie.harvest.api.HFApi;
import joshie.harvest.buildings.placeable.PlaceableHelper;
import joshie.harvest.core.util.generic.IFaceable;
import joshie.harvest.npc.EntityNPC;
import joshie.harvest.npc.EntityNPCBuilder;
import joshie.harvest.npc.EntityNPCMiner;
import joshie.harvest.npc.EntityNPCShopkeeper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class CodeGeneratorBuildings {

    private World world;
    private int x1, y1, z1, x2, y2, z2;

    public CodeGeneratorBuildings(World world, int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd) {
        this.world = world;
        this.x1 = xStart < xEnd ? xStart : xEnd;
        this.x2 = xStart < xEnd ? xEnd : xStart;
        this.y1 = yStart < yEnd ? yStart : yEnd;
        this.y2 = yStart < yEnd ? yEnd : yStart;
        this.z1 = zStart < zEnd ? zStart : zEnd;
        this.z2 = zStart < zEnd ? zEnd : zStart;
    }

    public ArrayList<Entity> getEntities(Class clazz, int x, int y, int z) {
        return (ArrayList<Entity>) world.getEntitiesWithinAABB(clazz, Blocks.stone.getCollisionBoundingBoxFromPool(world, x, y, z));
    }

    public void getCode(boolean air) {
        if (!world.isRemote) {
            ArrayList<String> ret = new ArrayList();
            StringBuilder entities = new StringBuilder();
            Set all = new HashSet();
            boolean hasAFrame = false;
            int i = 0;
            for (int y = 0; y <= y2 - y1; y++) {
                for (int x = 0; x <= x2 - x1; x++) {
                    for (int z = 0; z <= z2 - z1; z++) {
                        Set<Entity> entityList = new HashSet();
                        entityList.addAll(getEntities(EntityPainting.class, x1 + x, y1 + y, z1 + z));
                        entityList.addAll(getEntities(EntityItemFrame.class, x1 + x, y1 + y, z1 + z));
                        entityList.addAll(getEntities(EntityNPC.class, x1 + x, y1 + y, z1 + z));
                        entityList.addAll(getEntities(EntityNPCBuilder.class, x1 + x, y1 + y, z1 + z));
                        entityList.addAll(getEntities(EntityNPCMiner.class, x1 + x, y1 + y, z1 + z));
                        entityList.addAll(getEntities(EntityNPCShopkeeper.class, x1 + x, y1 + y, z1 + z));

                        Block block = world.getBlock(x1 + x, y1 + y, z1 + z);
                        if (block == Blocks.chest) {
                            TileEntityChest chest = (TileEntityChest) world.getTileEntity(x1 + x, y1 + y, z1 + z);
                            String name = chest.getInventoryName();
                            String field = name;
                            if (name.startsWith("npc.")) {
                                field = name.replace("npc.", "");
                                ret.add(PlaceableHelper.getPlaceableEntityString(new EntityNPC(null, world, HFApi.NPC.get(field)), x, y, z));
                            }
                            
                            String text = "npc_offsets.put(Town." + field.toUpperCase() + ", new PlaceableNPC(\"\", " + x + ", " + y + ", " + z + "));";
                            ret.add(text);
                            String air2 = PlaceableHelper.getPlaceableBlockString(Blocks.air, 0, x, y, z);
                            ret.add(air2);
                            continue;
                        }
                        
                        if ((block != Blocks.air || air || entityList.size() > 0) && block != Blocks.end_stone) {
                            int meta = world.getBlockMetadata(x1 + x, y1 + y, z1 + z);
                            if (block == Blocks.double_plant && meta >= 8) continue;
                            TileEntity tile = world.getTileEntity(x1 + x, y1 + y, z1 + z);
                            if (tile instanceof IFaceable) {
                                ret.add(PlaceableHelper.getPlaceableIFaceableString((IFaceable) tile, block, meta, x, y, z));
                            } else if (tile instanceof TileEntitySign) {
                                String[] text = ((TileEntitySign) tile).signText;
                                if (block == Blocks.standing_sign) {
                                    ret.add(PlaceableHelper.getFloorSignString(text, block, meta, x, y, z));
                                } else ret.add(PlaceableHelper.getWallSignString(text, block, meta, x, y, z));
                            } else {
                                String text = PlaceableHelper.getPlaceableBlockString(block, meta, x, y, z);
                                text = text.replace("schematicmetablocks:blockImplicitAir", "air");
                                text = text.replace("schematicmetablocks:blockInteriorAirMarker", "air");
                                text = text.replace("schematicmetablocks:blockExplicitAir", "air");
                                ret.add(text);
                            }

                            //Entities
                            if (entityList.size() > 0) {
                                int l = 0;
                                for (Entity e : entityList) {
                                    if (!all.contains(e)) {
                                        ret.add(PlaceableHelper.getPlaceableEntityString(e, x, y, z));
                                        all.add(e);
                                    }

                                    l++;
                                }
                            }

                            i++;
                        }
                    }
                }
            }

            ArrayList<String> build = new ArrayList();
            build.add("list = new ArrayList("+ i + ");");
            build.addAll(ret);

            try {
                PrintWriter writer = new PrintWriter("building-generator.log", "UTF-8");
                for (String s : build) {
                    writer.println(s);
                }

                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
