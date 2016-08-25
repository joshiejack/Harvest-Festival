package joshie.harvest.npc.entity;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.entity.ai.TaskHeadToBlock;
import joshie.harvest.town.TownDataServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityNPCBuilder extends EntityNPCShopkeeper {
    private TaskHeadToBlock go = new TaskHeadToBlock();
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
    }

    @Override
    public EntityNPCBuilder getNewEntity(EntityNPCShopkeeper entity) {
        return new EntityNPCBuilder((EntityNPCBuilder) entity);
    }

    public boolean isBuilding() {
        return homeTown != null && homeTown.getCurrentlyBuilding() != null;
    }

    @Override
    protected void updateAITasks() {
        if (homeTown == null) homeTown = TownHelper.getClosestTownToEntity(this);
        BuildingStage building = homeTown != null ? homeTown.getCurrentlyBuilding() : null;
        if (building == null) {
            super.updateAITasks();
        } else {
            if (!worldObj.isRemote) {
                if (tick % building.getTickTime() == 0) {
                    if (building.build(worldObj)) resetSpawnHome();
                    setTask(go.setLocation(building.next()));
                    if (building.isFinished()) {
                        ((TownDataServer)homeTown).finishBuilding(worldObj);
                        headTowards = null;
                    }
                }

                tick++;
            }
        }
    }
}