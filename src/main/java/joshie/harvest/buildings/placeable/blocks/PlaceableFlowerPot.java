package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.UUID;


public class PlaceableFlowerPot extends PlaceableBlock {
    private boolean canCactus;

    public PlaceableFlowerPot(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    public PlaceableFlowerPot(Block block, int meta, int offsetX, int offsetY, int offsetZ, boolean canCactus) {
        super(block, meta, offsetX, offsetY, offsetZ);
        this.canCactus = canCactus;
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(UUID uuid, World world, BlockPos pos, Mirror mirror, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityFlowerPot) {
            TileEntityFlowerPot pot = (TileEntityFlowerPot) tile;
            ItemStack random = getRandomPlant(world.rand);
            pot.setFlowerPotData(random.getItem(), random.getItemDamage());
        }
    }

    private ItemStack getRandomPlant(Random rand) {
        Block block = null;
        int meta = 0;
        int what = rand.nextInt(13) + 1;
        switch (what) {
            case 1:
                block = Blocks.RED_FLOWER;
                meta = 0;
                break;
            case 2:
                block = Blocks.YELLOW_FLOWER;
                break;
            case 3:
                block = Blocks.SAPLING;
                meta = 0;
                break;
            case 4:
                block = Blocks.SAPLING;
                meta = 1;
                break;
            case 5:
                block = Blocks.SAPLING;
                meta = 2;
                break;
            case 6:
                block = Blocks.SAPLING;
                meta = 3;
                break;
            case 7:
                block = Blocks.RED_MUSHROOM;
                break;
            case 8:
                block = Blocks.BROWN_MUSHROOM;
                break;
            case 9:
                if (canCactus) {
                    block = Blocks.CACTUS;
                } else block = Blocks.SAPLING;

                meta = 5;
                break;
            case 10:
                block = Blocks.DEADBUSH;
                break;
            case 11:
                block = Blocks.TALLGRASS;
                meta = 2;
                break;
            case 12:
                block = Blocks.SAPLING;
                meta = 4;
                break;
            case 13:
                block = Blocks.SAPLING;
                meta = 5;
        }
        return new ItemStack(Item.getItemFromBlock(block), 1, meta);
    }
}