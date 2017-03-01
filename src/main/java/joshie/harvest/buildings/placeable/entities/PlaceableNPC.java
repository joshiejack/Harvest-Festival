package joshie.harvest.buildings.placeable.entities;

import com.google.gson.annotations.Expose;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableNPC extends PlaceableEntity {
    @Expose
    private String homeString;
    @Expose
    private String npc;

    public PlaceableNPC() {}
    public PlaceableNPC(String homeString, String npc, int x, int y, int z) {
        this.homeString = homeString;
        this.npc = npc;
        this.pos = new BlockPos(x, y, z);
    }

    public String getHomeString() {
        return homeString;
    }

    public String getNPC() {
        return npc;
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.MOVEIN;
    }

    @Override
    public Entity getEntity(World world, BlockPos pos, Rotation rotation) {
        if (npc == null || npc.equals("")) return null;
        NPC inpc = NPC.REGISTRY.get(new ResourceLocation(npc)); if (inpc == null) return null;
        EntityNPC entity = NPCHelper.getEntityForNPC(world, inpc);
        entity.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        entity.resetSpawnHome();
        return entity;
    }

    @Override
    public PlaceableNPC getCopyFromEntity(Entity e, int x, int y, int z) {
        EntityNPC npc = (EntityNPC) e;
        return new PlaceableNPC("", npc.getNPC().getResource().toString(), x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceableNPC that = (PlaceableNPC) o;
        return homeString != null ? homeString.equals(that.homeString) : that.homeString == null && (npc != null ? npc.equals(that.npc) : that.npc == null);
    }

    @Override
    public int hashCode() {
        int result = homeString != null ? homeString.hashCode() : 0;
        result = 31 * result + (npc != null ? npc.hashCode() : 0);
        return result;
    }
}