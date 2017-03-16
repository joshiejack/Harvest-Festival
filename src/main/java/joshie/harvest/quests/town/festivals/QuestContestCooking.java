package joshie.harvest.quests.town.festivals;

import com.google.common.collect.Lists;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.npc.task.TaskSpeech;
import joshie.harvest.api.npc.task.TaskWait;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.town.Town;
import joshie.harvest.calendar.HFFestivals;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.other.HFScript;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.quests.town.festivals.contest.ContestJudgingScript;
import joshie.harvest.quests.town.festivals.contest.ContestTaskWinner;
import joshie.harvest.quests.town.festivals.contest.ContestWinningScript;
import joshie.harvest.quests.town.festivals.contest.QuestContest;
import joshie.harvest.quests.town.festivals.contest.cooking.CookingContestEntries;
import joshie.harvest.quests.town.festivals.contest.cooking.CookingContestEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static joshie.harvest.npcs.HFNPCs.GS_OWNER;
import static joshie.harvest.town.BuildingLocations.*;

@HFQuest("festival.cooking")
public class QuestContestCooking extends QuestContest<CookingContestEntries> {
    private static final String PREFIX = "cooking";
    private static final BuildingLocation[] LOCATIONS = new BuildingLocation[] { PARK_COW_STALL_1, PARK_COW_STALL_2, PARK_CENTRE, PARK_COW_STALL_4 };
    private static final NPC[] NPCS = new NPC[] { HFNPCs.FLOWER_GIRL, HFNPCs.MILKMAID, HFNPCs.TRADER, HFNPCs.CARPENTER, HFNPCs.DAUGHTER_ADULT, HFNPCs.CLOCKMAKER, HFNPCs.BLACKSMITH, HFNPCs.FISHERMAN, HFNPCs.POULTRY, HFNPCs.PRIEST, HFNPCs.BARN_OWNER };
    private static final Script FINISH = new HFScript(PREFIX + "_finish");
    private static final Script JUDGE_1 = new ContestJudgingScript(PREFIX, 1).setNPC(GS_OWNER);
    private static final Script JUDGE_2 = new ContestJudgingScript(PREFIX, 2).setNPC(GS_OWNER);
    private static final Script JUDGE_3 = new ContestJudgingScript(PREFIX, 3).setNPC(GS_OWNER);
    private static final Script JUDGE_4 = new ContestJudgingScript(PREFIX, 4).setNPC(GS_OWNER);
    private static final Script WINNER = new ContestWinningScript(PREFIX).setNPC(GS_OWNER);
    private Utensil category;

    public QuestContestCooking() {
        super(GS_OWNER, PREFIX);
        List<Utensil> utensils = Lists.newArrayList(Utensil.REGISTRY.values());
        Collections.shuffle(utensils);
        category = utensils.get(0);
    }

    @Override
    protected CookingContestEntries createEntries() {
        return new CookingContestEntries(LOCATIONS, NPCS);
    }

    public Utensil getCategory() {
        return category;
    }
    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        if (quest_stage == START) return getLocalized("selected");
        if (quest_stage == CONTINUE) return getLocalized("judging");
        if (isCorrectTime(time)) {
            if (quest_stage == EXPLAIN) return getLocalized("explain");
            if (entries.isSelecting(player)) {
                return entries.getNames().size() > 0 ? getLocalized("select") : getLocalized("none");
            }

            if (!entries.isEntered(player)) return getLocalized("help", TextHelper.localize(getCategory().getLocalizedName()));
            if (entries.isEntered(player)) return getLocalized("start");
        }

        return player.worldObj.rand.nextInt(4) == 0 ? getLocalized("wait") : null;
    }


    @Override
    public void reward(World world, Place place) {
        CookingContestEntry entry = entries.getEntry(place);
        entry.reward(world, place, entries.getNPCs(), getReward(place));
        if (entry.getPlayer() != null) {
            EntityPlayer player = entry.getPlayer();
            switch (place) {
                case FIRST:
                    HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getTracking().setHasWonCookingContest();
                    rewardGold(player, 5000);
                    break;
                case SECOND:
                    rewardGold(player, 2500);
                    break;
                case THIRD:
                    rewardGold(player, 500);
                    break;
            }
        }
    }

    @Override
    public ItemStack getReward(Place place) {
        switch (place) {
            case FIRST:
                return HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.FLOUR, 8);
            case SECOND:
                return HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.WINE, 1);
            case THIRD:
                return HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.FLOUR, 8);
            default:
                return new ItemStack(Items.WHEAT);
        }
    }

    @Override
    public void execute(Town town, EntityPlayer player, NPCEntity npc) {
        npc.setPath(move(PARK_COW_1), TaskSpeech.of(QuestContestCooking.JUDGE_1), move(PARK_COW_2), TaskSpeech.of(QuestContestCooking.JUDGE_2),
                move(PARK_COW_3), TaskSpeech.of(QuestContestCooking.JUDGE_3), move(PARK_COW_4), TaskSpeech.of(QuestContestCooking.JUDGE_4), TaskWait.of(1),
                TaskSpeech.of(QuestContestCooking.FINISH), move(PARK_COW_JUDGE), TaskSpeech.of(QuestContestCooking.WINNER), new ContestTaskWinner(HFFestivals.COW_FESTIVAL));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Utensil")) {
            category = Utensil.REGISTRY.get(new ResourceLocation(nbt.getString("Utensil")));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (category != null) {
            nbt.setString("Utensil", category.getResource().toString());
        }

        return nbt;
    }
}
