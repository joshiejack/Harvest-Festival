package uk.joshiejack.settlements.client.town;

import uk.joshiejack.settlements.world.town.Town;
import net.minecraft.util.math.BlockPos;

public class TownClient extends Town<CensusClient> {
    public static final Town NULL = new TownClient(0, BlockPos.ORIGIN);

    public TownClient(int id, BlockPos centre) {
        super(id, centre);
    }

    @Override
    protected CensusClient createCensus() {
        return new CensusClient();
    }
}
