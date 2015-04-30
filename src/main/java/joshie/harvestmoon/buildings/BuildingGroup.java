package joshie.harvestmoon.buildings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import joshie.harvestmoon.api.buildings.IBuildingGroup;
import joshie.harvestmoon.buildings.placeable.Placeable;
import joshie.harvestmoon.buildings.placeable.Placeable.PlacementStage;
import joshie.harvestmoon.buildings.placeable.entities.PlaceableNPC;
import joshie.harvestmoon.core.util.BlockAccessPreview;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BuildingGroup implements IBuildingGroup {
    public static final ArrayList<BuildingGroup> groups = new ArrayList(50);
    //List of all placeable elements
    public HashMap<String, PlaceableNPC> npc_offsets = new HashMap();
    protected ArrayList<Placeable> list;
    private BlockAccessPreview preview;
    private BuildingGroup group;
    protected int offsetY;
    protected int tickTime = 20;

    //List of all placeable elements
    private String name;
    private int meta;
    
    public BuildingGroup(String string) {
        this.name = string;
        this.meta = groups.size();
        this.groups.add(this);
        this.preview = new BlockAccessPreview(this, list);
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
    
    public String getName() {
        return name;
    }

    public long getTickTime() {
        return tickTime;
    }

    public IBlockAccess getBlockAccess(int worldX, int worldY, int worldZ, boolean n1, boolean n2, boolean swap) {
        return preview.setCoordinatesAndDirection(worldX, worldY, worldZ, n1, n2, swap);
    }

    public BuildingGroup getGroup() {
        return group;
    }

    public int getInt() {
        return meta;
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

    //TODO: LilyPad/Tripwire Hooks, Levers, Doors, Furnaces, Ladders, ItemFrame Loot????????????
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

    @Override
    public boolean equals(Object o) {
        return name.equals(o);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
