package joshie.harvest.quests.town.festivals.cooking;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.tile.TileCookingStand;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.town.BuildingLocations;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CookingContestEntries {
    private static final NPC[] USABLE = new NPC[] { HFNPCs.FLOWER_GIRL, HFNPCs.MILKMAID, HFNPCs.TRADER, HFNPCs.CARPENTER };
    private final Random rand = new Random();
    private final BiMap<UUID, CookingContestEntry> players;
    private final BiMap<NPC, CookingContestEntry> npcs;
    private final Object[] localizations;
    private final Utensil utensil;
    private EntityPlayer winner;
    private long prize;

    @SuppressWarnings("ConstantConditions")
    public CookingContestEntries(World world, Entity entity, Utensil category) {
        rand.setSeed(HFApi.calendar.getDate(world).hashCode());
        utensil = category;
        TownData data = TownHelper.getClosestTownToEntity(entity, false);
        players = HashBiMap.create();
        npcs = HashBiMap.create();
        for (BlockPos pos: getStandLocations(data)) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileCookingStand) {
                ItemStack stack = ((TileCookingStand)tile).getContents();
                UUID uuid = ((TileCookingStand)tile).getUUID();
                EntityPlayer player = EntityHelper.getPlayerFromUUID(uuid);
                if (stack != null && player != null) {
                    players.put(uuid, new CookingContestEntry(player, stack));
                } else {
                    NPC npc = getNextNPC();
                    npcs.put(npc, new CookingContestEntry(npc, rand));
                }
            }
        }

        List<CookingContestEntry> list = Lists.newArrayList(players.values());
        list.addAll(npcs.values()); //Collect all the entries
        list.sort((o1, o2) -> ((Integer)o1.getScore(category)).compareTo(o2.getScore(category)));


        List<Object> localized = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            CookingContestEntry entry = list.get(i); //Get the winners and remove the last place
            localized.add(entry.getDisplayName());
            localized.add(entry.getMeal().getDisplayName());
            if (i >= list.size() - 1) {
                winner = entry.getPlayer();
            }
        }

        prize = 500 + (rand.nextInt(50) * 50);
        localized.add(prize); //Forgot to add the prize values
        if (localized.size() < 7) localized.add("MISSING ENTRY???");
        localizations = localized.toArray();
    }

    public boolean isWinner(EntityPlayer player) {
        return winner == player;
    }

    public ItemStack getEntryForNPC(NPC npc) {
        return npcs.get(npc).getMeal();
    }

    public boolean isEntered(NPC npc) {
        return npcs.containsKey(npc);
    }

    private NPC getNextNPC() {
        for (NPC npc: USABLE) {
            if (!npcs.containsKey(npc)) return npc;
        }

        return NPC.NULL_NPC;
    }

    @SuppressWarnings("ConstantConditions")
    public BlockPos[] getStandLocations(TownData town) {
        BlockPos[] positions = new BlockPos[4];
        positions[0] = town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND1).down();
        positions[1] = town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND2).down();
        positions[2] = town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND3).down();
        positions[3] = town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND4).down();
        return positions;
    }

    @SuppressWarnings("ConstantConditions")
    public BlockPos[] getWalkLocations(TownData town) {
        BlockPos[] positions = new BlockPos[4];
        positions[0] = town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND1).down();
        positions[1] = town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND2).down();
        positions[2] = town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND3).down();
        positions[3] = town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND4).down();
        return positions;
    }

    private String getNameFromEntry(UUID uuid) {
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

    public Object[] getLocalization() {
        return localizations;
    }
}
