package joshie.harvest.npcs.entity;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.entity.ai.EntityAITalkingTo;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

public class EntityNPCMiner extends EntityNPCHuman<EntityNPCMiner> {
    @SuppressWarnings("unused")
    public EntityNPCMiner(World world) {
        this(world, HFNPCs.MINER);
    }

    public EntityNPCMiner(World world, NPC npc) {
        super(world, npc);
    }

    private EntityNPCMiner(EntityNPCMiner entity) {
        this(entity.worldObj, entity.npc);
    }

    @Override
    protected EntityNPCMiner getNewEntity(EntityNPCMiner entity) {
        return new EntityNPCMiner(entity);
    }

    @Override
    protected void initEntityAI() {
        ((PathNavigateGround) getNavigator()).setEnterDoors(true);
        ((PathNavigateGround) getNavigator()).setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAITalkingTo(this));
        tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(6, new EntityAIWander(this, 1.0D));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityNPC.class, 5.0F, 0.02F));
    }
}