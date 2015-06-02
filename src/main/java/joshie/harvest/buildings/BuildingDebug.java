package joshie.harvest.buildings;

import java.util.ArrayList;
import java.util.UUID;

import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.PlacementStage;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.buildings.placeable.blocks.PlaceableFurnace;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.init.HFBuildings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BuildingDebug extends Building {
    public BuildingDebug() {
        super("debug");
        test();
    }
    
    public void test() {
        list = new ArrayList(19);
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 0));
        list.add(new PlaceableBlock(Blocks.dirt, 0, 1, 0, 1));
        list.add(new PlaceableBlock(Blocks.dirt, 0, 1, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 3));
        list.add(new PlaceableFurnace(Blocks.furnace, 4, 0, 1, 0));
        list.add(new PlaceableFurnace(Blocks.furnace, 4, 0, 1, 1));
        list.add(new PlaceableFurnace(Blocks.furnace, 4, 0, 1, 2));
        list.add(new PlaceableBlock(Blocks.planks, 1, 1, 1, 0));
        list.add(new PlaceableBlock(Blocks.planks, 1, 1, 1, 1));
        list.add(new PlaceableBlock(Blocks.planks, 1, 1, 1, 2));
        list.add(new PlaceableFurnace(Blocks.furnace, 3, 1, 1, 3));
        list.add(new PlaceableBlock(Blocks.planks, 1, 1, 2, 0));
        list.add(new PlaceableBlock(Blocks.planks, 1, 1, 2, 1));
        list.add(new PlaceableBlock(Blocks.planks, 1, 1, 2, 2));
        list.add(new PlaceableBlock(Blocks.planks, 1, 1, 2, 3));
    }
    
    @Override
    public boolean generate(UUID uuid, World world, int xCoord, int yCoord, int zCoord) {
        test();
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
    
    @Override
    public long getCost() {
        return 6800L;
    }
    
    @Override
    public int getWoodCount() {
        return 192;
    }
    
    @Override
    public int getStoneCount() {
        return 112;
    }
    
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return TownHelper.hasBuilding(player, HFBuildings.miningHill) && TownHelper.hasBuilding(player, HFBuildings.miningHut) && TownHelper.hasBuilding(player, HFBuildings.goddessPond);
    }
}
