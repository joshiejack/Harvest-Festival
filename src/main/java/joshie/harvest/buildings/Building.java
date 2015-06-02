package joshie.harvest.buildings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.PlacementStage;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.util.BlockAccessPreview;
import joshie.harvest.init.HFBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Building implements IBuilding {
    public static final ArrayList<Building> buildings = new ArrayList(50);
    //List of all placeable elements
    public HashMap<String, PlaceableNPC> npc_offsets = new HashMap();
    protected ArrayList<Placeable> list;
    private BlockAccessPreview preview;
    protected int offsetY;
    protected int tickTime = 20;

    //List of all placeable elements
    private String name;
    private int meta;
    
    public Building(String string) {
        this.name = string;
        this.meta = buildings.size();
        this.buildings.add(this);
    }
    
    public Building init() {
        this.preview = new BlockAccessPreview(this);
        return this;
    }

    public static Building getGroup(String string) {
        for (Building b : buildings) {
            if (b.getName().equals(string)) {
                return b;
            }
        }

        return null;
    }
    
    public ItemStack getPreview() {
        return new ItemStack(HFBlocks.preview, 1, meta);
    }
    
    public String getName() {
        return name;
    }

    public long getTickTime() {
        return tickTime;
    }

    public IBlockAccess getBlockAccess(int worldX, int worldY, int worldZ, boolean n1, boolean n2, boolean swap) {
        return preview.setCoordinatesAndDirection(worldX, worldY, worldZ, n1, n2, swap);
    }

    public int getOffsetY() {
        return offsetY;
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

    //TODO: LilyPad/Tripwire Hooks, Levers, Doors, ItemFrame Loot????????????
    public boolean generate(UUID uuid, World world, int xCoord, int yCoord, int zCoord) {
        if (!world.isRemote) {
            boolean n1 = world.rand.nextBoolean();
            boolean n2 = world.rand.nextBoolean();
            boolean swap = world.rand.nextBoolean();

            /** First loop we place solid blocks **/
            for (Placeable block : list) {
                block.place(uuid, world, xCoord, yCoord, zCoord, n1, n2, swap, PlacementStage.BLOCKS);
            }

            /** Second loop we place entities etc. **/
            for (Placeable block : list) {
                block.place(uuid, world, xCoord, yCoord, zCoord, n1, n2, swap, PlacementStage.ENTITIES);
            }

            /** Third loop we place torch/ladders etc **/
            for (Placeable block : list) {
                block.place(uuid, world, xCoord, yCoord, zCoord, n1, n2, swap, PlacementStage.TORCHES);
            }

            /** Fourth loop we place NPCs **/
            for (Placeable block : list) {
                block.place(uuid, world, xCoord, yCoord, zCoord, n1, n2, swap, PlacementStage.NPC);
            }
        }

        return true;
    }

    public boolean canBuy(World world, EntityPlayer player) {
        return false;
    }

    public long getCost() {
        return 0;
    }

    public int getWoodCount() {
        return 0;
    }

    public int getStoneCount() {
        return 0;
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
