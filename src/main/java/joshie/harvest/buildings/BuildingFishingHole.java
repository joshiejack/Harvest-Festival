package joshie.harvest.buildings;

import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.blocks.*;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.player.town.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BuildingFishingHole extends Building {
    public BuildingFishingHole() {
        super("fishingHole");
        list = new ArrayList<Placeable>(162);
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 2, 0, 2));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 2, 0, 3));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 3, 0, 2));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 3, 0, 3));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 3, 0, 4));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 4, 0, 2));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 4, 0, 3));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 4, 0, 4));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 5, 0, 3));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 1, 1, 2));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 1, 1, 3));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 2, 1, 1));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 2, 1, 2));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 2, 1, 3));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 2, 1, 4));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 3, 1, 1));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 3, 1, 2));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 3, 1, 3));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 3, 1, 4));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 3, 1, 5));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 4, 1, 1));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 4, 1, 2));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 4, 1, 3));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 4, 1, 4));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 4, 1, 5));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 5, 1, 2));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 5, 1, 3));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 5, 1, 4));
        list.add(new PlaceableBlock(Blocks.SANDSTONE, 0, 6, 1, 3));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 1, 2, 2));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 1, 2, 3));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 2, 2, 1));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 2, 2, 2));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 2, 2, 3));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 2, 2, 4));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 3, 2, 1));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 3, 2, 2));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 3, 2, 3));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 3, 2, 4));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 3, 2, 5));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 4, 2, 1));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 4, 2, 2));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 4, 2, 3));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 4, 2, 4));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 4, 2, 5));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 5, 2, 2));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 5, 2, 3));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 5, 2, 4));
        list.add(new PlaceableBlock(Blocks.SAND, 0, 6, 2, 3));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 0, 3, 2));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 0, 3, 3));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 0, 3, 4));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 1, 3, 0));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 1, 3, 1));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 1, 3, 2));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 1, 3, 3));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 1, 3, 4));
        list.add(new PlaceableDecorative(Blocks.VINE, 1, 1, 3, 6));
        list.add(new PlaceableBlock(Blocks.LOG, 1, 1, 3, 7));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 2, 3, 0));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 2, 3, 1));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 2, 3, 2));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 2, 3, 3));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 2, 3, 4));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 2, 3, 5));
        list.add(new PlaceableDecorative(Blocks.VINE, 2, 2, 3, 7));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 3, 3, 0));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 3, 3, 1));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 3, 3, 2));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 3, 3, 3));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 3, 3, 4));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 3, 3, 5));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 3, 3, 6));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 4, 3, 0));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 4, 3, 1));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 4, 3, 2));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 4, 3, 3));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 4, 3, 4));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 4, 3, 5));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 4, 3, 6));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 5, 3, 0));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 5, 3, 1));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 5, 3, 2));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 5, 3, 3));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 5, 3, 4));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 5, 3, 5));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 6, 3, 1));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 6, 3, 2));
        list.add(new PlaceableBlock(Blocks.WATER, 0, 6, 3, 3));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 6, 3, 4));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 6, 3, 5));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 7, 3, 1));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 7, 3, 2));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 7, 3, 3));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 7, 3, 4));
        list.add(new PlaceableBlock(Blocks.GRASS, 0, 7, 3, 5));
        list.add(new PlaceableBlock(Blocks.OAK_FENCE, 0, 0, 4, 2));
        list.add(new PlaceableBlock(Blocks.REEDS, 0, 0, 4, 3));
        list.add(new PlaceableBlock(Blocks.OAK_FENCE, 0, 0, 4, 4));
        list.add(new PlaceableBlock(Blocks.TALLGRASS, 1, 1, 4, 0));
        list.add(new PlaceableDoublePlant(Blocks.DOUBLE_PLANT, 5, 1, 4, 1));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 1, 4, 2));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 1, 4, 3));
        list.add(new PlaceableBlock(Blocks.TALLGRASS, 1, 1, 4, 4));
        list.add(new PlaceableDecorative(Blocks.VINE, 1, 1, 4, 6));
        list.add(new PlaceableBlock(Blocks.LOG, 1, 1, 4, 7));
        list.add(new PlaceableDecorative(Blocks.RED_FLOWER, 3, 2, 4, 0));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 2, 4, 1));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 2, 4, 2));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 2, 4, 3));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 2, 4, 4));
        list.add(new PlaceableDoublePlant(Blocks.DOUBLE_PLANT, 3, 2, 4, 5));
        list.add(new PlaceableDecorative(Blocks.VINE, 2, 2, 4, 7));
        list.add(new PlaceableBlock(Blocks.TALLGRASS, 1, 3, 4, 0));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 3, 4, 1));
        list.add(new PlaceableDecorative(Blocks.WATERLILY, 0, 3, 4, 2));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 3, 4, 3));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 3, 4, 4));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 3, 4, 5));
        list.add(new PlaceableDecorative(Blocks.RED_FLOWER, 1, 3, 4, 6));
        list.add(new PlaceableDoublePlant(Blocks.DOUBLE_PLANT, 1, 4, 4, 0));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 4, 4, 1));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 4, 4, 2));
        list.add(new PlaceableSignWall(Blocks.WALL_SIGN, 4, 4, 4, 3, new String[]{"", "", "", ""}));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 4, 4, 4));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 4, 4, 5));
        list.add(new PlaceableDoublePlant(Blocks.DOUBLE_PLANT, 0, 4, 4, 6));
        list.add(new PlaceableBlock(Blocks.TALLGRASS, 1, 5, 4, 0));
        list.add(new PlaceableBlock(Blocks.TALLGRASS, 1, 5, 4, 1));
        list.add(new PlaceableSignWall(Blocks.WALL_SIGN, 2, 5, 4, 2, new String[]{"", "", "", ""}));
        list.add(new PlaceableBlock(Blocks.WOODEN_SLAB, 9, 5, 4, 3));
        list.add(new PlaceableSignWall(Blocks.WALL_SIGN, 3, 5, 4, 4, new String[]{"", "", "", ""}));
        list.add(new PlaceableDecorative(Blocks.RED_FLOWER, 6, 5, 4, 5));
        list.add(new PlaceableBlock(Blocks.TALLGRASS, 1, 6, 4, 1));
        list.add(new PlaceableSignWall(Blocks.WALL_SIGN, 2, 6, 4, 2, new String[]{"", "", "", ""}));
        list.add(new PlaceableBlock(Blocks.SPRUCE_STAIRS, 4, 6, 4, 3));
        list.add(new PlaceableSignWall(Blocks.WALL_SIGN, 3, 6, 4, 4, new String[]{"", "", "", ""}));
        list.add(new PlaceableDecorative(Blocks.RED_FLOWER, 3, 6, 4, 5));
        list.add(new PlaceableBlock(Blocks.TALLGRASS, 2, 7, 4, 1));
        list.add(new PlaceableDecorative(Blocks.RED_FLOWER, 6, 7, 4, 2));
        list.add(new PlaceableBlock(Blocks.SPRUCE_STAIRS, 1, 7, 4, 3));
        list.add(new PlaceableBlock(Blocks.TALLGRASS, 1, 7, 4, 4));
        list.add(new PlaceableBlock(Blocks.TALLGRASS, 2, 7, 4, 5));
        list.add(new PlaceableBlock(Blocks.OAK_FENCE, 0, 0, 5, 2));
        list.add(new PlaceableBlock(Blocks.REEDS, 0, 0, 5, 3));
        list.add(new PlaceableBlock(Blocks.OAK_FENCE, 0, 0, 5, 4));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 1, 5, 2));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 1, 5, 3));
        list.add(new PlaceableBlock(Blocks.WOODEN_SLAB, 1, 1, 5, 7));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 4, 5, 2));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 4, 5, 3));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 5, 5, 2));
        npc_offsets.put(TownData.FISHING_POND, new PlaceableNPC("", new BlockPos(5, 5, 3)));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 5, 5, 3));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 5, 5, 4));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 5, 5, 5));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 6, 5, 2));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 6, 5, 3));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 6, 5, 4));
        list.add(new PlaceableBlock(Blocks.WOODEN_SLAB, 1, 0, 6, 2));
        list.add(new PlaceableBlock(Blocks.WOODEN_SLAB, 1, 0, 6, 3));
        list.add(new PlaceableBlock(Blocks.WOODEN_SLAB, 1, 0, 6, 4));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 5, 6, 3));
        list.add(new PlaceableBlock(Blocks.AIR, 0, 6, 6, 3));
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
        return HFTrackers.getPlayerTracker(player).getTown().hasBuilding(HFBuildings.fishingHut);
    }
}