package joshie.harvest.npcs.entity;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.npcs.entity.ai.EntityAIWork;
import net.minecraft.world.World;

public class EntityNPCShopkeeper extends EntityNPCHuman<EntityNPCShopkeeper> {
    @SuppressWarnings("unused")
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
        super.initEntityAI();
        tasks.addTask(6, new EntityAIWork(this));
    }
}