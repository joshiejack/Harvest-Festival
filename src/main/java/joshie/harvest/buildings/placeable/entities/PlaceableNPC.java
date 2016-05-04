package joshie.harvest.buildings.placeable.entities;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableNPC extends PlaceableEntity {
    private INPC npc;

    public PlaceableNPC() {
        super(BlockPos.ORIGIN);
    }

    public PlaceableNPC(String npc, BlockPos offsetPos) {
        super(offsetPos);

        this.npc = HFApi.NPC.get(npc);
    }

    public PlaceableNPC(String npc, int x, int y, int z) {
        super(new BlockPos(x, y, z));
        this.npc = HFApi.NPC.get(npc);
    }


    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.MOVEIN;
    }

    @Override
    public Entity getEntity(UUID uuid, World world, BlockPos pos, Mirror mirror, Rotation rotation) {
        EntityNPC entity = NPCHelper.getEntityForNPC(uuid, world, npc);
        entity.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        return entity;
    }

    @Override
    public String getStringFor(Entity e, BlockPos pos) {
        EntityNPC npc = (EntityNPC) e;
        return "list.add(new PlaceableNPC(\"" + npc.getNPC().getUnlocalizedName() + "\", " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "));";
    }
}