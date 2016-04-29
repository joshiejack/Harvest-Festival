package joshie.harvest.buildings;

import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.PlacementStage;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

/** This data is used by the BuilderNPC, 
 * to know their current progress through a building project **/
public class BuildingStage {
    public UUID owner;
    public Building building;
    public boolean n1, n2, swap;
    public PlacementStage stage;
    public int index;
    public BlockPos pos;

    public BuildingStage() {
    }

    public BuildingStage(UUID uuid, Building building, BlockPos pos, boolean n1, boolean n2, boolean swap) {
        this.owner = uuid;
        this.building = building;
        this.n1 = n1;
        this.n2 = n2;
        this.swap = swap;
        this.stage = PlacementStage.BLOCKS;
        this.index = 0;
        this.pos = pos.add(0, building.getOffsetY(), 0);
    }

    public BuildingStage build(World world) {
        if (index >= building.getSize()) {
            if (stage == PlacementStage.BLOCKS) {
                stage = PlacementStage.TORCHES;
                index = 0;
            } else if (stage == PlacementStage.TORCHES) {
                stage = PlacementStage.ENTITIES;
                index = 0;
            } else if (stage == PlacementStage.ENTITIES) {
                stage = PlacementStage.NPC;
                index = 0;
            } else if (stage == PlacementStage.NPC) {
                stage = PlacementStage.FINISHED;
                index = 0;

                HFTrackers.getPlayerTracker(owner).getTown().addBuilding(world, this);
            }
        } else {
            while (index < building.getSize()) {
                Placeable block = building.get(index);
                IBlockState state = world.getBlockState(pos);
                if (block.place(owner, world, pos, state, n1, n2, swap, stage)) {
                    index++;
                    return this;
                }

                index++;
            }
        }

        return this;
    }

    public long getTickTime() {
        return building.getTickTime();
    }

    public boolean isFinished() {
        return stage == PlacementStage.FINISHED;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        building = Building.getGroup(nbt.getString("CurrentlyBuilding"));
        n1 = nbt.getBoolean("North1");
        n2 = nbt.getBoolean("North2");
        swap = nbt.getBoolean("Swap");
        pos = new BlockPos(nbt.getInteger("BuildingX"), nbt.getInteger("BuildingY"), nbt.getInteger("BuildingZ"));

        if (nbt.hasKey("Owner-UUIDMost")) {
            index = nbt.getInteger("Index");
            stage = PlacementStage.values()[nbt.getInteger("Stage")];
            owner = new UUID(nbt.getLong("Owner-UUIDMost"), nbt.getLong("Owner-UUIDLeast"));
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("CurrentlyBuilding", building.getName());
        nbt.setBoolean("North1", n1);
        nbt.setBoolean("North2", n2);
        nbt.setBoolean("Swap", swap);
        nbt.setInteger("BuildingX", pos.getX());
        nbt.setInteger("BuildingY", pos.getY());
        nbt.setInteger("BuildingZ", pos.getZ());

        if (owner != null) {
            nbt.setInteger("Stage", stage.ordinal());
            nbt.setInteger("Index", index);
            nbt.setLong("Owner-UUIDMost", owner.getMostSignificantBits());
            nbt.setLong("Owner-UUIDLeast", owner.getLeastSignificantBits());
        }
    }
}