package joshie.harvest.player;

import joshie.harvest.api.player.IPlayerHelper;
import joshie.harvest.api.player.IPlayerStats;
import joshie.harvest.api.player.IRelations;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import net.minecraft.entity.player.EntityPlayer;

@HFApiImplementation
public class PlayerHelper implements IPlayerHelper {
    public static final PlayerHelper INSTANCE = new PlayerHelper();

    @Override
    public IPlayerStats getStatsForPlayer(EntityPlayer player) {
        return HFTrackers.getPlayerTrackerFromPlayer(player).getStats();
    }

    @Override
    public IRelations getRelationsForPlayer(EntityPlayer player) {
        return HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships();
    }
}
