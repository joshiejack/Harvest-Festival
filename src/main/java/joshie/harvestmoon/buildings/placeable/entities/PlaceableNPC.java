package joshie.harvestmoon.buildings.placeable.entities;

import java.util.UUID;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.npc.INPC;
import joshie.harvestmoon.core.helpers.NPCHelper;
import joshie.harvestmoon.npc.EntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class PlaceableNPC extends PlaceableEntity {
    private INPC npc;

    public PlaceableNPC() {
        super(0, 0, 0);
    }
    
    public PlaceableNPC(String npc, int offsetX, int offsetY, int offsetZ) {
        super(offsetX, offsetY, offsetZ);

        this.npc = HMApi.NPC.get(npc);
    }


    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.NPC;
    }

    @Override
    public Entity getEntity(UUID uuid, World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        EntityNPC entity = NPCHelper.getEntityForNPC(uuid, world, npc);
        entity.setPosition(x + 0.5, y + 0.5, z + 0.5);
        return entity;
    }

    @Override
    public String getStringFor(Entity e, int x, int y, int z) {
        EntityNPC npc = (EntityNPC) e;
        return "list.add(new PlaceableNPC(\"" + npc.getNPC().getUnlocalizedName() + "\", " + x + ", " + y + ", " + z + "));";
    }
}
