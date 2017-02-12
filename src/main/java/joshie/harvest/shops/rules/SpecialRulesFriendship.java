package joshie.harvest.shops.rules;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.api.npc.NPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SpecialRulesFriendship implements ISpecialRules {
    private final NPC npc;
    private final int relationship;

    public SpecialRulesFriendship(NPC npc, int relationship) {
        this.npc = npc;
        this.relationship = relationship;
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        return HFApi.player.getRelationsForPlayer(player).getRelationship(npc) >= relationship;
    }
}
