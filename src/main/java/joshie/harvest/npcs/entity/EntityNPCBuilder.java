package joshie.harvest.npcs.entity;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.entity.ai.EntityAIBuild;
import joshie.harvest.npcs.entity.ai.EntityAIWork;
import net.minecraft.world.World;

public class EntityNPCBuilder extends EntityNPCShopkeeper {
    public EntityNPCBuilder(World world, NPC npc) {
        super(world, npc);
    }

    public EntityNPCBuilder(World world) {
        super(world, HFNPCs.CARPENTER);
    }

    private EntityNPCBuilder(EntityNPCBuilder entity) {
        super(entity);
    }

    @Override
    public EntityNPCBuilder getNewEntity(EntityNPCShopkeeper entity) {
        return new EntityNPCBuilder((EntityNPCBuilder) entity);
    }

    public BuildingStage getBuilding() {
        return getHomeTown().getCurrentlyBuilding();
    }

    public void finishBuilding() {
        getHomeTown().finishBuilding();
        stepHeight = 0.7F; //Reset Step Height
    }

    @Override
    public boolean isBusy() {
        return getBuilding() != null;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        tasks.addTask(5, new EntityAIBuild(this));
        tasks.addTask(7, new EntityAIWork(this));
    }
}