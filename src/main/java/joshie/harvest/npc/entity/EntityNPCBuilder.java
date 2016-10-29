package joshie.harvest.npc.entity;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.entity.ai.EntityAIBuild;
import joshie.harvest.npc.entity.ai.EntityAIWork;
import joshie.harvest.town.data.TownDataServer;
import joshie.harvest.town.TownHelper;
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
        if (homeTown == null) {
            homeTown = TownHelper.getClosestTownToEntity(this);
        }

        return homeTown.getCurrentlyBuilding();
    }

    public void finishBuilding() {
        ((TownDataServer)homeTown).finishBuilding(worldObj);
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