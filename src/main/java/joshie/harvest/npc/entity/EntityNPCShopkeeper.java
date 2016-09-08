package joshie.harvest.npc.entity;

import joshie.harvest.npc.NPC;
import joshie.harvest.npc.entity.ai.*;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

public class EntityNPCShopkeeper extends EntityNPCHuman<EntityNPCShopkeeper> {
    public EntityNPCShopkeeper(World world) {
        super(world);
    }

    public EntityNPCShopkeeper(World world, NPC npc) {
        super(world, npc);
    }

    public EntityNPCShopkeeper(EntityNPCShopkeeper entity) {
        super(entity);
    }

    @Override
    public EntityNPCShopkeeper getNewEntity(EntityNPCShopkeeper entity) {
        return new EntityNPCShopkeeper(entity);
    }

    @Override
    protected void initEntityAI() {
        ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAITalkingTo(this));
        tasks.addTask(1, new EntityAILookAtPlayer(this));
        tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(6, new EntityAISchedule(this));
        tasks.addTask(7, new EntityAIWork(this));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityNPC.class, 5.0F, 0.02F));
        tasks.addTask(9, new EntityAIWander(this, 0.6D));
        tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }
}