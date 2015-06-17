package joshie.harvest.npc.entity;

import java.util.ArrayList;
import java.util.UUID;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.blocks.BlockStone;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.buildings.placeable.Placeable.PlacementStage;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class EntityNPCMiner extends EntityNPC {
    private boolean isMining = false;
    private int mineX;
    private int mineY;
    private int mineZ;
    private int mineLevel;
    private ArrayList<PlaceableBlock> instructions = new ArrayList(500);
    private ArrayList<PlaceableBlock> completed = new ArrayList(500);
    private int index;

    public EntityNPCMiner(UUID owning_player, EntityNPCMiner entity) {
        super(owning_player, entity);
        isMining = entity.isMining;
        mineX = entity.mineX;
        mineY = entity.mineY;
        mineZ = entity.mineZ;
        mineLevel = entity.mineLevel;
        instructions = entity.instructions;
        index = entity.index;
    }

    public EntityNPCMiner(World world) {
        super(world);
    }

    public EntityNPCMiner(UUID owning_player, World world, INPC npc) {
        super(owning_player, world, npc);
    }

    /*
    @Override
    public boolean interact(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            WorldLocation location = TownHelper.getLocationFor(UUIDHelper.getPlayerUUID(player), HFBuildings.miningHill, Town.MINE_ENTRANCE);
            if (location != null) {
                MineHelper.getServerTracker().addToMine(worldObj, location.x, location.y, location.z, this, player.getDisplayName());
            }
        }
        
        return true;
    } */

    public void startBuild(World world, int xCoord, int yCoord, int zCoord, int level) {
        isMining = true;
        mineX = xCoord;
        mineY = yCoord;
        mineZ = zCoord;
        mineLevel = level;
        boolean longX = world.rand.nextBoolean();
        int mineXSize = world.rand.nextInt(longX ? 9 : 5) + 2;
        int mineZSize = world.rand.nextInt(longX ? 5 : 9) + 2;
        instructions = new ArrayList(500);
        completed = new ArrayList(500);
        index = 0;
        /* Build the list */

        //Ground floor
        for (int x = -mineXSize; x <= mineXSize; x++) {
            for (int z = -mineZSize; z <= mineZSize; z++) {
                instructions.add(new PlaceableBlock(HFBlocks.dirt, 0, x, -8 - (level * 5), z)); //Add the block to this list
            }
        }

        //Walls
        for (int x = -mineXSize - 1; x <= mineXSize + 1; x++) {
            for (int y = -8; y < -3; y++) {
                instructions.add(new PlaceableBlock(HFBlocks.stone, BlockStone.getRandomMeta(world.rand), x, y - (level * 5), -mineZSize - 1));
                instructions.add(new PlaceableBlock(HFBlocks.stone, BlockStone.getRandomMeta(world.rand), x, y - (level * 5), mineZSize + 1));
            }
        }

        //Walls Two
        for (int z = -mineZSize - 1; z <= mineZSize + 1; z++) {
            for (int y = -8; y < -3; y++) {
                instructions.add(new PlaceableBlock(HFBlocks.stone, BlockStone.getRandomMeta(world.rand), -mineXSize - 1, y - (level * 5), z));
                instructions.add(new PlaceableBlock(HFBlocks.stone, BlockStone.getRandomMeta(world.rand), mineXSize + 1, y - (level * 5), z));
            }
        }

        //Ceiling
        for (int x = -mineXSize; x <= mineXSize; x++) {
            for (int z = -mineZSize; z <= mineZSize; z++) {
                instructions.add(new PlaceableBlock(HFBlocks.stone, BlockStone.getRandomMeta(world.rand), x, -4 - (level * 5), z)); //Add the block to this list
            }
        }

        //Clear out the centre
        for (int x = -mineXSize; x <= mineXSize; x++) {
            for (int z = -mineZSize; z <= mineZSize; z++) {
                for (int y = -7; y < -4; y++) {
                    instructions.add(new PlaceableBlock(Blocks.air, 0, x, y - (level * 5), z)); //Add the block to this list
                }
            }
        }
    }

    private boolean canPlaceBlock(int x, int y, int z) {
        if (worldObj.canBlockSeeTheSky(x, y, z)) {
            return false;
        }

        return true;
    }

    @Override
    protected void updateAITick() {
        if (!isMining) {
            super.updateAITick();
        } else {
            if (!worldObj.isRemote) {
                //LETS BUILD
                if (index >= instructions.size()) {
                    isMining = false;
                    HFTrackers.getMineTracker().completeMine(worldObj, mineX, mineY, mineZ, completed);
                    completed = new ArrayList(500);
                    instructions = new ArrayList(500);
                    HFTrackers.getMineTracker().newDay();
                    HFTrackers.markDirty();
                } else {
                    PlaceableBlock block = instructions.get(index);
                    Material material = worldObj.getBlock(mineX + block.getX(), mineY + block.getY(), mineZ + block.getZ()).getMaterial();
                    boolean sideCanSee = false;
                    int x = mineX + block.getX();
                    int y = mineY + block.getY();
                    int z = mineZ + block.getZ();
                    if (!canPlaceBlock(x, y, z)) sideCanSee = true;
                    else if (!canPlaceBlock(x + 1, y, z)) sideCanSee = true;
                    else if (!canPlaceBlock(x - 1, y, z)) sideCanSee = true;
                    else if (!canPlaceBlock(x, y, z + 1)) sideCanSee = true;
                    else if (!canPlaceBlock(x, y, z - 1)) sideCanSee = true;
                    else if (!canPlaceBlock(x, y + 1, z)) sideCanSee = true;
                    else if (!canPlaceBlock(x + 1, y + 1, z)) sideCanSee = true;
                    else if (!canPlaceBlock(x - 1, y + 1, z)) sideCanSee = true;
                    else if (!canPlaceBlock(x, y + 1, z + 1)) sideCanSee = true;
                    else if (!canPlaceBlock(x, y + 1, z - 1)) sideCanSee = true;

                    if (!sideCanSee) {
                        block.place(owning_player, worldObj, mineX, mineY, mineZ, false, false, false, PlacementStage.BLOCKS);
                        completed.add(block);
                    }

                    index++;
                }
            }
        }
    }

    @Override
    public void setDead() {
        if (!worldObj.isRemote && npc.respawns()) {
            EntityNPCMiner clone = new EntityNPCMiner(owning_player, this);
            worldObj.spawnEntityInWorld(clone);
        }

        isDead = true;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        isMining = nbt.getBoolean("IsMining");
        mineX = nbt.getInteger("MineX");
        mineY = nbt.getInteger("MineY");
        mineZ = nbt.getInteger("MineZ");
        mineLevel = nbt.getInteger("MineLevel");
        index = nbt.getInteger("Position");

        NBTTagList placeable = nbt.getTagList("Farmland", 10);
        for (int i = 0; i < placeable.tagCount(); i++) {
            NBTTagCompound tag = placeable.getCompoundTagAt(i);
            PlaceableBlock p = new PlaceableBlock();
            p.readFromNBT(tag);
            instructions.add(p);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("IsMining", isMining);
        nbt.setInteger("MineX", mineX);
        nbt.setInteger("MineY", mineY);
        nbt.setInteger("MineZ", mineZ);
        nbt.setInteger("MineLevel", mineLevel);
        nbt.setInteger("Position", index);
        NBTTagList placeable = new NBTTagList();
        for (PlaceableBlock block : instructions) {
            NBTTagCompound tag = new NBTTagCompound();
            block.writeToNBT(tag);
            placeable.appendTag(tag);
        }

        nbt.setTag("Instructions", placeable);
    }
}
