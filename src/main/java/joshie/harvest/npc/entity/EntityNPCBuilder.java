package joshie.harvest.npc.entity;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.entity.ai.TaskHeadToBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityNPCBuilder extends EntityNPCShopkeeper {
    private TaskHeadToBlock go = new TaskHeadToBlock();
    private BuildingStage building;
    public BlockPos headTowards = null;
    private int tick;

    public EntityNPCBuilder(World world, INPC npc) {
        super(world, (NPC) npc);
    }

    public EntityNPCBuilder(World world) {
        super(world, (NPC) HFNPCs.BUILDER);
    }

    public EntityNPCBuilder(EntityNPCBuilder entity) {
        super(entity);
        building = entity.building;
    }

    @Override
    public EntityNPCBuilder getNewEntity(EntityNPCShopkeeper entity) {
        return new EntityNPCBuilder((EntityNPCBuilder) entity);
    }

    public boolean isBuilding() {
        return building != null;
    }

    @Override
    protected void updateAITasks() {
        if (building == null) {
            super.updateAITasks();
        } else {
            if (!worldObj.isRemote) {
                if (tick % building.getTickTime() == 0) {
                    building.build(worldObj);
                    setTask(go.setLocation(building.next()));
                    if (building.isFinished()) {
                        building = null;
                        headTowards = null;
                    }
                }

                tick++;
            }
        }
    }

    public boolean startBuilding(Building building, BlockPos pos, Mirror mirror, Rotation rotation) {
        if (!worldObj.isRemote) {
            this.building = new BuildingStage(this, building, pos, mirror, rotation);
        }

        return false;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("CurrentlyBuilding")) {
            building = new BuildingStage(this);
            building.readFromNBT(nbt);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        if (building != null) {
            building.writeToNBT(nbt);
        }
    }
}