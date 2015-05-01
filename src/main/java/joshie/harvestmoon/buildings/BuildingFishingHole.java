package joshie.harvestmoon.buildings;

import java.util.ArrayList;

import joshie.harvestmoon.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableDoublePlant;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableFlower;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableLilypad;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableLog;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableSignWall;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableStairs;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableVine;
import joshie.harvestmoon.buildings.placeable.entities.PlaceableNPC;
import joshie.harvestmoon.core.helpers.TownHelper;
import joshie.harvestmoon.init.HMBuildings;
import joshie.harvestmoon.player.Town;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BuildingFishingHole extends Building {
    public BuildingFishingHole() {
        super("fishingHole");
        list = new ArrayList(162);
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 2, 0, 2));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 2, 0, 3));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 3, 0, 2));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 3, 0, 3));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 3, 0, 4));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 4, 0, 2));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 4, 0, 3));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 4, 0, 4));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 5, 0, 3));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 1, 1, 2));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 1, 1, 3));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 2, 1, 1));
        list.add(new PlaceableBlock(Blocks.sand, 0, 2, 1, 2));
        list.add(new PlaceableBlock(Blocks.sand, 0, 2, 1, 3));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 2, 1, 4));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 3, 1, 1));
        list.add(new PlaceableBlock(Blocks.sand, 0, 3, 1, 2));
        list.add(new PlaceableBlock(Blocks.sand, 0, 3, 1, 3));
        list.add(new PlaceableBlock(Blocks.sand, 0, 3, 1, 4));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 3, 1, 5));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 4, 1, 1));
        list.add(new PlaceableBlock(Blocks.sand, 0, 4, 1, 2));
        list.add(new PlaceableBlock(Blocks.sand, 0, 4, 1, 3));
        list.add(new PlaceableBlock(Blocks.sand, 0, 4, 1, 4));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 4, 1, 5));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 5, 1, 2));
        list.add(new PlaceableBlock(Blocks.sand, 0, 5, 1, 3));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 5, 1, 4));
        list.add(new PlaceableBlock(Blocks.sandstone, 0, 6, 1, 3));
        list.add(new PlaceableBlock(Blocks.sand, 0, 1, 2, 2));
        list.add(new PlaceableBlock(Blocks.sand, 0, 1, 2, 3));
        list.add(new PlaceableBlock(Blocks.sand, 0, 2, 2, 1));
        list.add(new PlaceableBlock(Blocks.water, 0, 2, 2, 2));
        list.add(new PlaceableBlock(Blocks.water, 0, 2, 2, 3));
        list.add(new PlaceableBlock(Blocks.sand, 0, 2, 2, 4));
        list.add(new PlaceableBlock(Blocks.sand, 0, 3, 2, 1));
        list.add(new PlaceableBlock(Blocks.water, 0, 3, 2, 2));
        list.add(new PlaceableBlock(Blocks.water, 0, 3, 2, 3));
        list.add(new PlaceableBlock(Blocks.water, 0, 3, 2, 4));
        list.add(new PlaceableBlock(Blocks.sand, 0, 3, 2, 5));
        list.add(new PlaceableBlock(Blocks.sand, 0, 4, 2, 1));
        list.add(new PlaceableBlock(Blocks.water, 0, 4, 2, 2));
        list.add(new PlaceableBlock(Blocks.water, 0, 4, 2, 3));
        list.add(new PlaceableBlock(Blocks.water, 0, 4, 2, 4));
        list.add(new PlaceableBlock(Blocks.sand, 0, 4, 2, 5));
        list.add(new PlaceableBlock(Blocks.sand, 0, 5, 2, 2));
        list.add(new PlaceableBlock(Blocks.water, 0, 5, 2, 3));
        list.add(new PlaceableBlock(Blocks.sand, 0, 5, 2, 4));
        list.add(new PlaceableBlock(Blocks.sand, 0, 6, 2, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 3, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 3, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 3, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 3, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 3, 1));
        list.add(new PlaceableBlock(Blocks.water, 0, 1, 3, 2));
        list.add(new PlaceableBlock(Blocks.water, 0, 1, 3, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 3, 4));
        list.add(new PlaceableVine(Blocks.vine, 1, 1, 3, 6));
        list.add(new PlaceableLog(Blocks.log, 1, 1, 3, 7));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 3, 0));
        list.add(new PlaceableBlock(Blocks.water, 0, 2, 3, 1));
        list.add(new PlaceableBlock(Blocks.water, 0, 2, 3, 2));
        list.add(new PlaceableBlock(Blocks.water, 0, 2, 3, 3));
        list.add(new PlaceableBlock(Blocks.water, 0, 2, 3, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 3, 5));
        list.add(new PlaceableVine(Blocks.vine, 2, 2, 3, 7));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 3, 0));
        list.add(new PlaceableBlock(Blocks.water, 0, 3, 3, 1));
        list.add(new PlaceableBlock(Blocks.water, 0, 3, 3, 2));
        list.add(new PlaceableBlock(Blocks.water, 0, 3, 3, 3));
        list.add(new PlaceableBlock(Blocks.water, 0, 3, 3, 4));
        list.add(new PlaceableBlock(Blocks.water, 0, 3, 3, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 3, 6));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 3, 0));
        list.add(new PlaceableBlock(Blocks.water, 0, 4, 3, 1));
        list.add(new PlaceableBlock(Blocks.water, 0, 4, 3, 2));
        list.add(new PlaceableBlock(Blocks.water, 0, 4, 3, 3));
        list.add(new PlaceableBlock(Blocks.water, 0, 4, 3, 4));
        list.add(new PlaceableBlock(Blocks.water, 0, 4, 3, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 3, 6));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 3, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 3, 1));
        list.add(new PlaceableBlock(Blocks.water, 0, 5, 3, 2));
        list.add(new PlaceableBlock(Blocks.water, 0, 5, 3, 3));
        list.add(new PlaceableBlock(Blocks.water, 0, 5, 3, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 3, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 3, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 3, 2));
        list.add(new PlaceableBlock(Blocks.water, 0, 6, 3, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 3, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 3, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 7, 3, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 7, 3, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 7, 3, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 7, 3, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 7, 3, 5));
        list.add(new PlaceableBlock(Blocks.fence, 0, 0, 4, 2));
        list.add(new PlaceableBlock(Blocks.reeds, 0, 0, 4, 3));
        list.add(new PlaceableBlock(Blocks.fence, 0, 0, 4, 4));
        list.add(new PlaceableBlock(Blocks.tallgrass, 1, 1, 4, 0));
        list.add(new PlaceableDoublePlant(Blocks.double_plant, 5, 1, 4, 1));
        list.add(new PlaceableBlock(Blocks.air, 0, 1, 4, 2));
        list.add(new PlaceableBlock(Blocks.air, 0, 1, 4, 3));
        list.add(new PlaceableBlock(Blocks.tallgrass, 1, 1, 4, 4));
        list.add(new PlaceableVine(Blocks.vine, 1, 1, 4, 6));
        list.add(new PlaceableLog(Blocks.log, 1, 1, 4, 7));
        list.add(new PlaceableFlower(Blocks.red_flower, 3, 2, 4, 0));
        list.add(new PlaceableBlock(Blocks.air, 0, 2, 4, 1));
        list.add(new PlaceableBlock(Blocks.air, 0, 2, 4, 2));
        list.add(new PlaceableBlock(Blocks.air, 0, 2, 4, 3));
        list.add(new PlaceableBlock(Blocks.air, 0, 2, 4, 4));
        list.add(new PlaceableDoublePlant(Blocks.double_plant, 3, 2, 4, 5));
        list.add(new PlaceableVine(Blocks.vine, 2, 2, 4, 7));
        list.add(new PlaceableBlock(Blocks.tallgrass, 1, 3, 4, 0));
        list.add(new PlaceableBlock(Blocks.air, 0, 3, 4, 1));
        list.add(new PlaceableLilypad(Blocks.waterlily, 0, 3, 4, 2));
        list.add(new PlaceableBlock(Blocks.air, 0, 3, 4, 3));
        list.add(new PlaceableBlock(Blocks.air, 0, 3, 4, 4));
        list.add(new PlaceableBlock(Blocks.air, 0, 3, 4, 5));
        list.add(new PlaceableFlower(Blocks.red_flower, 1, 3, 4, 6));
        list.add(new PlaceableDoublePlant(Blocks.double_plant, 1, 4, 4, 0));
        list.add(new PlaceableBlock(Blocks.air, 0, 4, 4, 1));
        list.add(new PlaceableBlock(Blocks.air, 0, 4, 4, 2));
        list.add(new PlaceableSignWall(Blocks.wall_sign, 4, 4, 4, 3, new String[] { "", "", "", "" } ));
        list.add(new PlaceableBlock(Blocks.air, 0, 4, 4, 4));
        list.add(new PlaceableBlock(Blocks.air, 0, 4, 4, 5));
        list.add(new PlaceableDoublePlant(Blocks.double_plant, 0, 4, 4, 6));
        list.add(new PlaceableBlock(Blocks.tallgrass, 1, 5, 4, 0));
        list.add(new PlaceableBlock(Blocks.tallgrass, 1, 5, 4, 1));
        list.add(new PlaceableSignWall(Blocks.wall_sign, 2, 5, 4, 2, new String[] { "", "", "", "" } ));
        list.add(new PlaceableBlock(Blocks.wooden_slab, 9, 5, 4, 3));
        list.add(new PlaceableSignWall(Blocks.wall_sign, 3, 5, 4, 4, new String[] { "", "", "", "" } ));
        list.add(new PlaceableFlower(Blocks.red_flower, 6, 5, 4, 5));
        list.add(new PlaceableBlock(Blocks.tallgrass, 1, 6, 4, 1));
        list.add(new PlaceableSignWall(Blocks.wall_sign, 2, 6, 4, 2, new String[] { "", "", "", "" } ));
        list.add(new PlaceableStairs(Blocks.spruce_stairs, 4, 6, 4, 3));
        list.add(new PlaceableSignWall(Blocks.wall_sign, 3, 6, 4, 4, new String[] { "", "", "", "" } ));
        list.add(new PlaceableFlower(Blocks.red_flower, 3, 6, 4, 5));
        list.add(new PlaceableBlock(Blocks.tallgrass, 2, 7, 4, 1));
        list.add(new PlaceableFlower(Blocks.red_flower, 6, 7, 4, 2));
        list.add(new PlaceableStairs(Blocks.spruce_stairs, 1, 7, 4, 3));
        list.add(new PlaceableBlock(Blocks.tallgrass, 1, 7, 4, 4));
        list.add(new PlaceableBlock(Blocks.tallgrass, 2, 7, 4, 5));
        list.add(new PlaceableBlock(Blocks.fence, 0, 0, 5, 2));
        list.add(new PlaceableBlock(Blocks.reeds, 0, 0, 5, 3));
        list.add(new PlaceableBlock(Blocks.fence, 0, 0, 5, 4));
        list.add(new PlaceableBlock(Blocks.air, 0, 1, 5, 2));
        list.add(new PlaceableBlock(Blocks.air, 0, 1, 5, 3));
        list.add(new PlaceableBlock(Blocks.wooden_slab, 1, 1, 5, 7));
        list.add(new PlaceableBlock(Blocks.air, 0, 4, 5, 2));
        list.add(new PlaceableBlock(Blocks.air, 0, 4, 5, 3));
        list.add(new PlaceableBlock(Blocks.air, 0, 5, 5, 2));
        npc_offsets.put(Town.FISHING_POND, new PlaceableNPC("", 5, 5, 3));
        list.add(new PlaceableBlock(Blocks.air, 0, 5, 5, 3));
        list.add(new PlaceableBlock(Blocks.air, 0, 5, 5, 4));
        list.add(new PlaceableBlock(Blocks.air, 0, 5, 5, 5));
        list.add(new PlaceableBlock(Blocks.air, 0, 6, 5, 2));
        list.add(new PlaceableBlock(Blocks.air, 0, 6, 5, 3));
        list.add(new PlaceableBlock(Blocks.air, 0, 6, 5, 4));
        list.add(new PlaceableBlock(Blocks.wooden_slab, 1, 0, 6, 2));
        list.add(new PlaceableBlock(Blocks.wooden_slab, 1, 0, 6, 3));
        list.add(new PlaceableBlock(Blocks.wooden_slab, 1, 0, 6, 4));
        list.add(new PlaceableBlock(Blocks.air, 0, 5, 6, 3));
        list.add(new PlaceableBlock(Blocks.air, 0, 6, 6, 3));
    }
    
    @Override
    public long getCost() {
        return 1000L;
    }
    
    @Override
    public int getWoodCount() {
        return 16;
    }
    
    @Override
    public int getStoneCount() {
        return 0;
    }
    
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return TownHelper.hasBuilding(player, HMBuildings.fishingHut);
    }
}
