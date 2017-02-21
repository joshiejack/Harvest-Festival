package joshie.harvest.quests.town.festivals.cow;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.npcs.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class CowContestEntries {
    private static final NPC[] USABLE = new NPC[] { HFNPCs.BARN_OWNER, HFNPCs.GS_OWNER, HFNPCs.TRADER, HFNPCs.CARPENTER };
    private final BiMap<UUID, CowEntry> players;
    private final BiMap<NPC, CowEntry> npcs;
    private final Random rand = new Random();
    private CowEntry winner;

    public CowContestEntries(World world, Map<UUID, EntityHarvestCow> entries) {
        rand.setSeed(HFApi.calendar.getDate(world).hashCode());
        players = HashBiMap.create();
        npcs = HashBiMap.create();
        for (UUID uuid: entries.keySet()) {
            EntityPlayer player = EntityHelper.getPlayerFromUUID(uuid);
            if (player != null) {
                players.put(uuid, new CowEntry(player, entries.get(uuid)));
            }
        }

        //If we have less than four entries, add more
        if (players.size() < 4) {
            NPC npc = getNextNPC();
            npcs.put(npc, new CowEntry(npc, rand));
        }

        List<CowEntry> list = Lists.newArrayList(players.values());
        list.addAll(npcs.values()); //Collect all the entries
        list.sort((o1, o2) -> ((Integer)o1.getScore()).compareTo(o2.getScore()));
        winner = list.get(list.size() - 1);
    }

    private NPC getNextNPC() {
        for (NPC npc: USABLE) {
            if (!npcs.containsKey(npc)) return npc;
        }

        return NPC.NULL_NPC;
    }

    public void winner() {
        if (winner != null) {
            winner.setWinner();
        }
    }
}
