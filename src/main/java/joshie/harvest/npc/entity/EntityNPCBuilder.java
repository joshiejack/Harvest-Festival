package joshie.harvest.npc.entity;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.npc.entity.ai.EntityAINPC;
import joshie.harvest.npc.entity.ai.TaskHeadToBlock;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityNPCBuilder extends EntityNPCShopkeeper {
    private TaskHeadToBlock go = new TaskHeadToBlock();
    private BuildingStage building;
    public BlockPos headTowards = null;
    private int tick;

    public EntityNPCBuilder(UUID owning_player, EntityNPCBuilder entity) {
        super(owning_player, entity);
        building = entity.building;
    }

    public EntityNPCBuilder(World world) {
        super(world);
    }

    public EntityNPCBuilder(UUID owning_player, World world, INPC npc) {
        super(owning_player, world, npc);
    }

    public boolean isBuilding() {
        return building != null;
    }

    @Override
    protected void updateTasks() {
        if (building == null) {
            super.updateTasks();
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

    @Override
    protected void initEntityAI() {
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);

        if (owning_player != null) {
            tasks.addTask(0, new EntityAISwimming(this));
            tasks.addTask(1, new EntityAIAvoidEntity<EntityZombie>(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
            tasks.addTask(1, new EntityAITalkingTo(this));
            tasks.addTask(2, new EntityAINPC(this));
            tasks.addTask(3, new EntityAILookAtPlayer(this));
            tasks.addTask(3, new EntityAITeleportHome(this));
            tasks.addTask(4, new EntityAIRestrictOpenDoor(this));
            tasks.addTask(5, new EntityAIOpenDoor(this, true));
            tasks.addTask(8, new EntityAIPlay(this, 0.32D));
            tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
            tasks.addTask(9, new EntityAIWatchClosest(this, EntityNPC.class, 5.0F, 0.02F));
            tasks.addTask(9, new EntityAIWander(this, 0.6D));
            tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        }
    }

    public boolean startBuilding(IBuilding building, BlockPos pos, Mirror mirror, Rotation rotation, UUID uuid) {
        if (!worldObj.isRemote) {
            this.building = new BuildingStage(uuid, building, pos, mirror, rotation);
        }

        return false;
    }

    @Override
    public void setDead() {
        if (!worldObj.isRemote && npc.respawns()) {
            EntityNPCBuilder clone = new EntityNPCBuilder(owning_player, this);
            clone.building = building;
            worldObj.spawnEntityInWorld(clone);
        }

        isDead = true;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("CurrentlyBuilding")) {
            building = new BuildingStage();
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