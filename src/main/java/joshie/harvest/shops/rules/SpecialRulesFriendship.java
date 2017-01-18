package joshie.harvest.shops.rules;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.api.npc.NPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SpecialRulesFriendship implements ISpecialRules<EntityPlayer> {
    private final NPC npc;
    private final int relationship;

    public SpecialRulesFriendship(NPC npc, int relationship) {
        this.npc = (NPC) npc;
        this.relationship = relationship;
    }

    @Override
    public boolean canDo(World world, EntityPlayer player, int amount) {
        return HFApi.player.getRelationsForPlayer(player).getRelationship(npc.getUUID()) >= relationship;
    }
}
