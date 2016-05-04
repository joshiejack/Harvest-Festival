package joshie.harvest.buildings;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Building implements IBuilding {
    public static final ArrayList<IBuilding> buildings = new ArrayList<IBuilding>(50);
    //List of all placeable elements
    public HashMap<String, PlaceableNPC> npc_offsets = new HashMap<String, PlaceableNPC>();
    protected ArrayList<Placeable> list;
    protected int offsetY;
    protected int tickTime = 20;

    //List of all placeable elements
    private String name;
    private int meta;

    public Building(String string) {
        this.name = string;
        this.meta = buildings.size();
        buildings.add(this);
    }

    @Override
    public IBuilding addBlocks() {
        return this;
    }

    public static IBuilding getGroup(String string) {
        for (IBuilding b : buildings) {
            if (b.getName().equals(string)) {
                return b;
            }
        }

        return null;
    }

    public ItemStack getPreview() {
        return new ItemStack(HFBlocks.PREVIEW.get(this));
    }

    @Override
    public PlaceableNPC getNPCOffset(String npc_location) {
        return npc_offsets.get(npc_location);
    }

    public String getName() {
        return name;
    }

    public long getTickTime() {
        return tickTime;
    }

    //TODO PREVIEW
    public IBlockAccess getBlockAccess(int worldX, int worldY, int worldZ, boolean n1, boolean n2, boolean swap) {
        return null;
    }

    @Override
    public int getOffsetY() {
        return offsetY;
    }

    public int getSize() {
        return list.size();
    }

    public Placeable get(int index) {
        return list.get(index);
    }

    @Override
    public List<Placeable> getList() {
        return list;
    }

    public EnumActionResult generate(UUID uuid, World world, BlockPos pos) {
        if (!world.isRemote) {
            Mirror mirror = Mirror.values()[world.rand.nextInt(3)];
            Rotation rotation = Rotation.values()[world.rand.nextInt(4)];
            for (Placeable placeable: list) placeable.place(uuid, world, pos, mirror, rotation, ConstructionStage.BUILD);
            for (Placeable placeable: list) placeable.place(uuid, world, pos, mirror, rotation, ConstructionStage.PAINT);
            for (Placeable placeable: list) placeable.place(uuid, world, pos, mirror, rotation, ConstructionStage.DECORATE);
            for (Placeable placeable: list) placeable.place(uuid, world, pos, mirror, rotation, ConstructionStage.MOVEIN);
        }

        return EnumActionResult.SUCCESS;
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
        return (o instanceof String) && name.equals(o);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}