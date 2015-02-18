package joshie.harvestmoon.buildings.placeable.entities;

import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.npc.EntityNPC;
import joshie.harvestmoon.npc.NPC;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class PlaceableNPC extends PlaceableEntity {
    private NPC npc;

    public PlaceableNPC() {
        super(0, 0, 0);
    }
    
    public PlaceableNPC(String npc, int offsetX, int offsetY, int offsetZ) {
        super(offsetX, offsetY, offsetZ);

        this.npc = HMNPCs.get(npc);
    }


    @Override
    protected boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.NPC;
    }

    @Override
    public Entity getEntity(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        EntityNPC entity = npc.getEntity(world, x, y, z);
        entity.setPosition(x + 0.5, y + 0.5, z + 0.5);
        return entity;
    }

    @Override
    public String getStringFor(Entity e, int x, int y, int z) {
        EntityNPC npc = (EntityNPC) e;
        return "list.add(new PlaceableNPC(\"" + npc.getNPC().getUnlocalizedName() + "\", " + x + ", " + y + ", " + z + "));";
    }
}
