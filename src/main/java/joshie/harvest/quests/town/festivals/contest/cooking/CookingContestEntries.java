package joshie.harvest.quests.town.festivals.contest.cooking;

import com.google.common.collect.Lists;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.tile.TilePlate;
import joshie.harvest.quests.town.festivals.QuestContestCooking;
import joshie.harvest.quests.town.festivals.contest.ContestEntries;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CookingContestEntries extends ContestEntries<ItemStack, CookingContestEntry, QuestContestCooking> {
    public CookingContestEntries(BlockPos[] locations, NPC[] npcs) {
        super(locations, npcs);
    }

    @Override
    protected void createEntry(EntityPlayer player, World world, BlockPos pos, int stall) {
        TileEntity tile = world.getTileEntity(pos);
        ItemStack meal = HFCooking.MEAL.getRandomMealFromUtensil(QuestContestCooking.getCategory());
        if (tile instanceof TilePlate) {
            TilePlate plate = ((TilePlate) tile);
            plate.setContents(meal.copy());
        }

        entries.add(new CookingContestEntry(getNextEntry(player, usedNPCS, npcs), pos, meal, stall));
    }

    @Override
    public List<Pair<ItemStack, Integer>> getAvailableEntries(EntityPlayer player) {
        //Validate the existing entries
        validateExistingEntries(player.world);
        List<Pair<ItemStack, Integer>> list = Lists.newArrayList();
        for (int i = 0; i < locations.length; i++) {
            BlockPos location = locations[i];
            int stall = i + 1;
            if (!isEntered(stall)) {
                BlockPos target = HFApi.towns.getTownForEntity(player).getCoordinatesFromOffset(HFBuildings.FESTIVAL_GROUNDS, location);
                if (target != null) {
                    TileEntity tile = player.world.getTileEntity(target);
                    if (tile instanceof TilePlate) {
                        ItemStack stack = ((TilePlate) tile).getContents();
                        if (!stack.isEmpty()) {
                            Pair<ItemStack, Integer> pair = Pair.of(stack, stall);
                            if (!list.contains(pair)) list.add(pair);
                        }
                    }
                }
            }
        }

        return list;
    }

    @Override
    public List<Pair<String, Integer>> getAvailableEntryNames(EntityPlayer player) {
        List<Pair<String, Integer>> list = new ArrayList<>();
        for (Pair<ItemStack, Integer> pair : getAvailableEntries(player)) {
            list.add(Pair.of(pair.getKey().getDisplayName(), pair.getValue()));
        }

        return list;
    }

    @Override
    public void enter(EntityPlayer player, @Nonnull ItemStack stack, int stall) {
        UUID playerUUID = EntityHelper.getPlayerUUID(player);
        //Wipe out any entries that match the exist
        entries.removeIf(entry -> entry.getStall() == stall || playerUUID.equals(entry.getPlayerUUID()));

        BlockPos target = HFApi.towns.getTownForEntity(player).getCoordinatesFromOffset(HFBuildings.FESTIVAL_GROUNDS, locations[stall - 1]);
        entries.add(new CookingContestEntry(playerUUID, target, stack, stall));
        selecting.remove(playerUUID);
    }

    @Override
    protected CookingContestEntry fromNBT(NBTTagCompound tag) {
        return CookingContestEntry.fromNBT(tag);
    }
}