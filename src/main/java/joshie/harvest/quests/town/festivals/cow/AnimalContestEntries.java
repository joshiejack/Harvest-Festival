package joshie.harvest.quests.town.festivals.cow;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.quests.town.festivals.Place;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.Map.Entry;

public class AnimalContestEntries {
    private final List<AnimalContestEntry> entries = new ArrayList<>();
    private TIntObjectMap<AnimalContestEntry> stalls = new TIntObjectHashMap<>();
    private final Set<NPC> used = new HashSet<>();
    private final NPC[] usable;

    public AnimalContestEntries(World world, Map<UUID, Pair<UUID, Integer>> map, NPC... npcs) {
        usable = npcs; //Set the list up
        for (Entry<UUID, Pair<UUID, Integer>> entry: map.entrySet()) {
            EntityPlayer player = EntityHelper.getPlayerFromUUID(entry.getKey());
            EntityAnimal animal = EntityHelper.getAnimalFromUUID(world, entry.getValue().getKey());
            if (player != null && animal != null) {
                AnimalContestEntry contestEntry = new AnimalContestEntry(player, animal);
                entries.add(contestEntry);
                stalls.put(entry.getValue().getValue(), contestEntry);
            }
        }

        //If we have less than four entries, add more
        Random rand = new Random(HFApi.calendar.getDate(world).hashCode());
        if (entries.size() < 4) {
            NPC npc = getNextNPC();
            int stall = getNextStall();
            if (stall >= 1) {
                AnimalContestEntry contestEntry = new AnimalContestEntry(npc, rand);
                entries.add(contestEntry);
                stalls.put(stall, contestEntry);
            }
        }

        entries.sort((o1, o2) -> ((Integer)o1.getScore()).compareTo(o2.getScore()));
    }

    private Integer getNextStall() {
        for (int i = 1; i <= 4; i++) {
            if (!stalls.containsKey(i)) return i;
        }

        return 0;
    }

    private NPC getNextNPC() {
        for (NPC npc: usable) {
            if (!used.contains(npc)) {
                used.add(npc);
                return npc;
            }
        }

        return NPC.NULL_NPC;
    }

    public Set<NPC> getNPCs() {
        return used;
    }

    public AnimalContestEntry getEntry(int id) {
        return entries.get(id);
    }

    public AnimalContestEntry getEntry(Place place) {
        return entries.get(entries.size() - 1 - place.ordinal());
    }
}
