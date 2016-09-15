package joshie.harvest.npc.entity;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.entity.ai.*;
import joshie.harvest.town.TownDataServer;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

public class EntityNPCBuilder extends EntityNPCShopkeeper {
    public EntityNPCBuilder(World world, INPC npc) {
        super(world, (NPC) npc);
    }

    public EntityNPCBuilder(World world) {
        super(world, (NPC) HFNPCs.BUILDER);
    }

    public EntityNPCBuilder(EntityNPCBuilder entity) {
        super(entity);
    }

    @Override
    public EntityNPCBuilder getNewEntity(EntityNPCShopkeeper entity) {
        return new EntityNPCBuilder((EntityNPCBuilder) entity);
    }

    public BuildingStage getBuilding() {
        if (homeTown == null) homeTown = TownHelper.getClosestTownToEntity(this);
        return homeTown != null ? homeTown.getCurrentlyBuilding() : null;
    }

    public void finishBuilding() {
        ((TownDataServer)homeTown).finishBuilding(worldObj);
    }

    @Override
    public boolean isBusy() {
        return getBuilding() != null;
    }

    @Override
    protected void initEntityAI() {
        ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAITalkingTo(this));
        tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(5, new EntityAIBuild(this));
        tasks.addTask(6, new EntityAISchedule(this));
        tasks.addTask(7, new EntityAIWork(this));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityNPC.class, 5.0F, 0.02F));
        tasks.addTask(9, new EntityAIWander(this, 0.6D));
        tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }
}