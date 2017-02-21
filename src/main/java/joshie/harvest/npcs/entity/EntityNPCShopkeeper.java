package joshie.harvest.npcs.entity;

import joshie.harvest.api.npc.NPC;
import net.minecraft.world.World;

@Deprecated //TODO: Remove in 0.7+
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
    public void onLivingUpdate() {
        super.onLivingUpdate();

        EntityNPCHuman human = new EntityNPCVillager(worldObj, npc);
        human.setPositionAndUpdate(posX, posY, posZ);
        setDead(); //Kill this entity
    }
}