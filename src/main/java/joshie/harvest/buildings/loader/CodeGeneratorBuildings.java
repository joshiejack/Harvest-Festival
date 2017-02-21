package joshie.harvest.buildings.loader;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.PlaceableHelper;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.buildings.placeable.blocks.PlaceableChest;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.npcs.entity.EntityNPCBuilder;
import joshie.harvest.npcs.entity.EntityNPCTrader;
import joshie.harvest.npcs.entity.EntityNPCVillager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static joshie.harvest.core.lib.HFModInfo.MODID;

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

    @SuppressWarnings("unchecked")
    public ArrayList<Entity> getEntities(Class clazz, int x, int y, int z) {
        return (ArrayList<Entity>) world.getEntitiesWithinAABB(clazz, new AxisAlignedBB(new BlockPos(x, y, z)));
    }

    private void buildList(ArrayList<Placeable> ret, int x, int y, int z, Set<Entity> all) {
        Set<Entity> entityList = new HashSet<>();
        entityList.addAll(getEntities(EntityPainting.class, x1 + x, y1 + y, z1 + z));
        entityList.addAll(getEntities(EntityItemFrame.class, x1 + x, y1 + y, z1 + z));
        entityList.addAll(getEntities(EntityNPCVillager.class, x1 + x, y1 + y, z1 + z));
        entityList.addAll(getEntities(EntityNPCBuilder.class, x1 + x, y1 + y, z1 + z));
        entityList.addAll(getEntities(EntityNPCTrader.class, x1 + x, y1 + y, z1 + z));

        BlockPos position = new BlockPos(x1 + x, y1 + y, z1 + z);
        IBlockState state = world.getBlockState(position);
        Block block = state.getBlock();
        if (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST) {
            TileEntityChest chest = (TileEntityChest) world.getTileEntity(new BlockPos(x1 + x, y1 + y, z1 + z));
            if (chest != null) {
                String name = chest.getName();
                if (name.startsWith("npc.")) {
                    name = name.replace("npc.", "");
                    NPC npc = NPC.REGISTRY.getValue(new ResourceLocation(MODID, name));
                    String npcField = npc == null ? "" : npc.getRegistryName().toString();
                    ret.add(new PlaceableNPC(name, npcField, x, y, z));
                    ret.add(new PlaceableBlock(Blocks.AIR.getDefaultState(), x, y, z));
                    return;
                } else if (name.startsWith("loot.")) {
                    ret.add(new PlaceableChest(name.replace("loot.", "chests/"), state, x, y, z));
                    return;
                }
            }
        }

        if ((block != Blocks.AIR  || entityList.size() > 0) && block != Blocks.END_STONE) {
            int meta = state.getBlock().getMetaFromState(state);
            if ((block == Blocks.DOUBLE_PLANT || block instanceof BlockDoor) && meta >= 8) return;
            TileEntity tile = world.getTileEntity(position);
            if (tile instanceof TileEntitySign) {
                ITextComponent[] text = ((TileEntitySign) tile).signText;
                if (block == Blocks.STANDING_SIGN) {
                    ret.add(PlaceableHelper.getFloorSignString(text, state, new BlockPos(x, y, z)));
                } else ret.add(PlaceableHelper.getWallSignString(text, state, new BlockPos(x, y, z)));
            } else {
                Placeable text = PlaceableHelper.getPlaceableBlockString(world, state, position, x, y, z);
                ret.add(text);
            }

            //Entities
            if (entityList.size() > 0) {
                for (Entity e : entityList) {
                    if (!all.contains(e)) {
                        ret.add(PlaceableHelper.getPlaceableEntityString(e, x, y, z));
                        all.add(e);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void getCode() {
        if (!world.isRemote) {
            ArrayList<Placeable> ret = new ArrayList<>();
            Set<Entity> all = new HashSet();
            for (int y = 0; y <= y2 - y1; y++) {
                for (int x = 0; x <= x2 - x1; x++) {
                    if (x % 2 == 0) {
                        for (int z = 0; z <= z2 - z1; z++) buildList(ret, x, y, z, all);
                    } else for (int z = z2 - z1; z >= 0; z--) buildList(ret, x, y, z, all);
                }
            }

            HFTemplate template = new HFTemplate(ret);
            try {
                String json = HFBuildings.getGson().toJson(template);
                PrintWriter writer = new PrintWriter("building.json", "UTF-8");
                writer.write(json);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}