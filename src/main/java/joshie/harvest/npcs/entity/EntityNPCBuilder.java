package joshie.harvest.npcs.entity;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.entity.ai.EntityAIBuild;
import net.minecraft.world.World;

public class EntityNPCBuilder extends EntityNPCHuman<EntityNPCBuilder> {
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
    public EntityNPCBuilder getNewEntity(EntityNPCBuilder entity) {
        return new EntityNPCBuilder(entity);
    }

    public BuildingStage getBuilding() {
        return getHomeTown().getCurrentlyBuilding();
    }

    public void finishBuilding() {
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
    }
}