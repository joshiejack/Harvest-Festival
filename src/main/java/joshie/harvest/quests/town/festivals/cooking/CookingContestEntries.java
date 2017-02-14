package joshie.harvest.quests.town.festivals.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.NPCHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.Map.Entry;

public class CookingContestEntries {
    private final Random rand = new Random();
    private final Map<UUID, CookingContestEntry> entries;
    private final Object[] localizations;
    private UUID winner;
    private long prize;

    @SuppressWarnings("ConstantConditions")
    public CookingContestEntries(World world, Map<UUID, BlockPos> blockPosList) {
        rand.setSeed(HFApi.calendar.getDate(world).hashCode());
        entries = new HashMap<>();
        for (Entry<UUID, BlockPos> entry: blockPosList.entrySet()) {
            CookingContestEntry contestEntry = new CookingContestEntry(world, entry.getValue());
            if (contestEntry.getMeal() != null)
            entries.put(entry.getKey(), contestEntry);
        }

        if (entries.size() < 4) { addEntries(HFNPCs.FLOWER_GIRL, HFNPCs.MILKMAID, HFNPCs.TRADER, HFNPCs.CARPENTER);}
        List<UUID> sorted = new ArrayList<>(entries.keySet());
        sorted.sort((o1, o2) -> ((Integer)entries.get(o1).getScore()).compareTo(entries.get(o2).getScore()));
        List<Object> localized = new ArrayList<>();
        for (int i = 1; i < sorted.size(); i++) {
            UUID uuid = sorted.get(i); //Get the winners and remove the last place
            localized.add(getNameFromUUID(uuid));
            localized.add(entries.get(uuid).getMeal().getDisplayName());
            if (i >= sorted.size() - 1) winner = uuid;
        }

        prize = 500 + (rand.nextInt(50) * 50);
        localized.add(prize); //Forgot to add the prize values
        if (localized.size() < 7) localized.add("MISSING ENTRY???");
        localizations = localized.toArray();
    }

    public boolean isUUIDInContest(UUID uuid) {
        return entries.containsKey(uuid);
    }

    public ItemStack getEntryForUUID(UUID uuid) {
        return entries.get(uuid).getMeal();
    }

    /** Keeps adding entries until we hit for **/
    private void addEntries(NPC... npcs) {
        for (NPC npc: npcs) {
            if (entries.size() >= 4) break; //No more
            CookingContestEntry entry = new CookingContestEntry(rand);
            entries.put(npc.getUUID(), entry);
        }
    }

    private String getNameFromUUID(UUID uuid) {
        EntityPlayer player = EntityHelper.getPlayerFromUUID(uuid);
        if (player != null) return player.getName();
        else {
            NPC npc = NPCHelper.getNPCFromUUID(uuid);
            if (npc != null) return npc.getLocalizedName();
            else return "Unknown";
        }
    }

    public long getPrize() {
        return prize;
    }

    public UUID getWinnerID() {
        return winner;
    }

    public Object[] getLocalization() {
        return localizations;
    }
}
