package uk.joshiejack.settlements.shop.comparator;

import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.npcs.status.StatusTracker;
import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@PenguinLoader("npc_status")
public class ComparatorNPCStatus extends Comparator {
    private ResourceLocation npcKey;
    private String status;

    @Override
    public Comparator create(Row data, String id) {
        ComparatorNPCStatus comparator = new ComparatorNPCStatus();
        comparator.status = data.get("status");
        comparator.npcKey = data.isEmpty("npc") || data.get("npc").toString().contains("self") ? null : new ResourceLocation(data.get("npc"));
        return comparator;
    }

    public int getValue(@Nonnull ShopTarget target) {
        ResourceLocation npc = npcKey != null ? npcKey : target.entity instanceof EntityNPC ? ((EntityNPC)target.entity).getInfo().getRegistryName() : null;
        StatusTracker tracker = AdventureDataLoader.get(target.world).getStatusTracker(target.player);
        if (npc == null || !tracker.has(npc, status)) return 0;
        else return AdventureDataLoader.get(target.world).getStatusTracker(target.player).get(npc, status);
    }
}
