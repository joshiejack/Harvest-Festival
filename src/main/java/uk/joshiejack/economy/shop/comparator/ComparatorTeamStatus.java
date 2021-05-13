package uk.joshiejack.economy.shop.comparator;

import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("team_status")
public class ComparatorTeamStatus extends Comparator {
    private String status;

    @Override
    public Comparator create(Row data, String id) {
        ComparatorTeamStatus comparator = new ComparatorTeamStatus();
        comparator.status = data.get("status");
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return PenguinTeams.getTeamForPlayer(target.player).getData().getCompoundTag("PenguinStatuses").getInteger(status);
    }
}
