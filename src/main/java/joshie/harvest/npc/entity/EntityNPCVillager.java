package joshie.harvest.npc.entity;

import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import net.minecraft.world.World;

public class EntityNPCVillager extends AbstractEntityNPC<EntityNPCVillager> {
    public EntityNPCVillager(World world) {
        super(world, (NPC) HFNPCs.GODDESS);
    }

    public EntityNPCVillager(World world, NPC npc) {
        super(world, npc);
    }

    public EntityNPCVillager(EntityNPCVillager entity) {
        super(entity);
    }

    @Override
    protected EntityNPCVillager getNewEntity(EntityNPCVillager entity) {
        return new EntityNPCVillager(entity);
    }
}