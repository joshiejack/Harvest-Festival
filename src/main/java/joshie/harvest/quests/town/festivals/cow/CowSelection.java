package joshie.harvest.quests.town.festivals.cow;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.quests.town.festivals.QuestContestCow;
import joshie.harvest.town.BuildingLocations;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class CowSelection extends Selection<QuestContestCow> {
    public static List<EntityHarvestCow> getEntrants(EntityPlayer player) {
        List<EntityHarvestCow> cows = new ArrayList<>();
        TownData town = TownHelper.getClosestTownToEntity(player, false);
        addCowToList(player.worldObj, town, BuildingLocations.PARK_COW_1, cows);
        addCowToList(player.worldObj, town, BuildingLocations.PARK_COW_2, cows);
        addCowToList(player.worldObj, town, BuildingLocations.PARK_COW_3, cows);
        addCowToList(player.worldObj, town, BuildingLocations.PARK_COW_4, cows);
        return cows;
    }

    @Nullable
    public static EntityHarvestCow getClosestCow(World world, BlockPos pos) {
        List<EntityHarvestCow> cows = EntityHelper.getEntities(EntityHarvestCow.class, world, pos, 5D, 5D);
        double d0 = -1.0D;
        EntityHarvestCow closest = null;
        for (EntityHarvestCow cow: cows) {
            double d1 = cow.getDistanceSq(pos);
            if ((d1 < 5D * 5D) && (d0 == -1.0D || d1 < d0))  {
                d0 = d1;
                closest = cow;
            }
        }

        return closest;
    }

    private static void addCowToList(World world, TownData town, BuildingLocation location, List<EntityHarvestCow> cows) {
        EntityHarvestCow closest = getClosestCow(world, town.getCoordinatesFor(location));
        if (closest != null && !cows.contains(closest)) {
            cows.add(closest);
        }
    }

    @Override
    public String[] getText(@Nonnull EntityPlayer player) {
       List<EntityHarvestCow> cows = getEntrants(player);
        int max = Math.min(4, cows.size());
        if (max <= 0) return new String[] { "No cows in the stalls." };
        else {
            String[] string = new String[max];
            for (int i = 0; i < max; i++) {
                string[i] = "@" + cows.get(i).getName();
            }

            return string;
        }
    }

    @Override
    public Result onSelected(EntityPlayer player, EntityLiving entity, NPC npc, @Nullable QuestContestCow quest, int option) {
        if (quest == null) return Result.DEFAULT;
        List<EntityHarvestCow> cows = getEntrants(player);
        int index = option - 1;
        if (index >= cows.size()) return Result.DENY;
        else {
            quest.setEntrant(player, cows.get(index));
            quest.setStage(QuestContestCow.START);
            quest.syncData(player);
            return Result.ALLOW;
        }
    }
}
