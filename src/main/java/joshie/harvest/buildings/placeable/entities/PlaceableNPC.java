package joshie.harvest.buildings.placeable.entities;

import com.google.gson.annotations.Expose;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.NPCRegistry;
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
        NPC inpc = NPCRegistry.REGISTRY.getValue(new ResourceLocation(npc)); if (inpc == null) return null;
        EntityNPC entity = NPCHelper.getEntityForNPC(world, inpc);
        entity.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        entity.resetSpawnHome();
        return entity;
    }

    @Override
    public PlaceableNPC getCopyFromEntity(Entity e, int x, int y, int z) {
        EntityNPC npc = (EntityNPC) e;
        return new PlaceableNPC("", npc.getNPC().getRegistryName().toString(), x, y, z);
    }
}