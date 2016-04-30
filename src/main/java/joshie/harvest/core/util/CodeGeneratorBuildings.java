package joshie.harvest.core.util;

import joshie.harvest.api.HFApi;
import joshie.harvest.buildings.placeable.PlaceableHelper;
import joshie.harvest.core.util.generic.IFaceable;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.entity.EntityNPCShopkeeper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CodeGeneratorBuildings {

    private World world;
    private BlockPos pos1, pos2;
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

    public CodeGeneratorBuildings(World world, BlockPos posStart, BlockPos posEnd) {
        this(world, posStart.getX(), posStart.getY(), posStart.getZ(), posEnd.getX(), posEnd.getY(), posEnd.getZ());
        this.pos1 = posStart;
        this.pos2 = posEnd;
    }

    public List<Entity> getEntities(Class<? extends Entity> clazz, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return world.getEntitiesWithinAABB(clazz, Blocks.STONE.getCollisionBoundingBox(state, world, pos));
    }

    public void getCode(boolean air) {
        if (!world.isRemote) {
            ArrayList<String> ret = new ArrayList<String>();
            Set<Entity> all = new HashSet<Entity>();
            int i = 0;
            for (int y = 0; y <= pos2.getY() - pos1.getY(); y++) {
                for (int x = 0; x <= pos2.getX() - pos1.getX(); x++) {
                    for (int z = 0; z <= pos2.getZ() - pos1.getZ(); z++) {
                        Set<Entity> entityList = new HashSet<Entity>();
                        entityList.addAll(getEntities(EntityPainting.class, pos1.add(x, y, z)));
                        entityList.addAll(getEntities(EntityItemFrame.class, pos1.add(x, y, z)));
                        entityList.addAll(getEntities(EntityNPC.class, pos1.add(x, y, z)));
                        entityList.addAll(getEntities(EntityNPCBuilder.class, pos1.add(x, y, z)));
                        //entityList.addAll(getEntities(EntityNPCMiner.class, pos1.add(x, y, z)));
                        entityList.addAll(getEntities(EntityNPCShopkeeper.class, pos1.add(x, y, z)));

                        Block block = world.getBlockState(pos1.add(x, y, z)).getBlock();
                        if (block == Blocks.CHEST) {
                            TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos1.add(x, y, z));
                            String name = chest.getName();
                            String field = name;
                            if (name.startsWith("npc.")) {
                                field = name.replace("npc.", "");
                                ret.add(PlaceableHelper.getPlaceableEntityString(new EntityNPC(null, world, HFApi.NPC.get(field)), new BlockPos(x, y, z)));
                            }

                            String text = "npc_offsets.put(Town." + field.toUpperCase() + ", new PlaceableNPC(\"\", " + x + ", " + y + ", " + z + "));";
                            ret.add(text);
                            String air2 = PlaceableHelper.getPlaceableBlockString(Blocks.AIR.getDefaultState(), new BlockPos(x, y, z));
                            ret.add(air2);
                            continue;
                        }

                        if ((block != Blocks.AIR || air || entityList.size() > 0) && block != Blocks.END_STONE) {
                            IBlockState state = world.getBlockState(pos1.add(x, y, z));
                            if (block == Blocks.DOUBLE_PLANT && state.getBlock().getMetaFromState(state) >= 8) continue;
                            TileEntity tile = world.getTileEntity(pos1.add(x, y, z));
                            if (tile instanceof IFaceable) {
                                ret.add(PlaceableHelper.getPlaceableIFaceableString((IFaceable) tile, state, new BlockPos(x, y, z)));
                            } else if (tile instanceof TileEntitySign) {
                                ITextComponent[] text = ((TileEntitySign) tile).signText;
                                if (block == Blocks.STANDING_SIGN) {
                                    ret.add(PlaceableHelper.getFloorSignString(text, state, new BlockPos(x, y, z)));
                                } else ret.add(PlaceableHelper.getWallSignString(text, state, new BlockPos(x, y, z)));
                            } else {
                                String text = PlaceableHelper.getPlaceableBlockString(state, new BlockPos(x, y, z));
                                text = text.replace("schematicmetablocks:blockImplicitAir", "air");
                                text = text.replace("schematicmetablocks:blockInteriorAirMarker", "air");
                                text = text.replace("schematicmetablocks:blockExplicitAir", "air");
                                ret.add(text);
                            }

                            //Entities
                            if (entityList.size() > 0) {
                                for (Entity e : entityList) {
                                    if (!all.contains(e)) {
                                        ret.add(PlaceableHelper.getPlaceableEntityString(e, new BlockPos(x, y, z)));
                                        all.add(e);
                                    }
                                }
                            }
                            i++;
                        }
                    }
                }
            }

            List<String> build = new ArrayList<String>();
            build.add("list = new ArrayList(" + i + ");");
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