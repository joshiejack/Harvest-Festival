package joshie.harvest.quests.town.festivals;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.npc.schedule.ScheduleElement;
import joshie.harvest.api.npc.schedule.ScheduleMove;
import joshie.harvest.api.npc.schedule.ScheduleSpeech;
import joshie.harvest.api.npc.schedule.ScheduleWait;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.entity.EntityNPCHuman;
import joshie.harvest.npcs.entity.ai.EntityAIPathing;
import joshie.harvest.quests.base.QuestFestival;
import joshie.harvest.quests.town.festivals.cow.AnimalContestEntries;
import joshie.harvest.quests.town.festivals.cow.CowContestScript;
import joshie.harvest.quests.town.festivals.cow.CowContestSelection;
import joshie.harvest.quests.town.festivals.cow.CowSelection;
import joshie.harvest.town.BuildingLocations;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.quests.town.festivals.cow.CowSelection.getEntrants;

@HFQuest("festival.cow")
public class QuestContestCow extends QuestFestival {
    private static final Script cowJudge1 = new CowContestScript(1);
    private static final Script cowJudge2 = new CowContestScript(2);
    private static final Script cowJudge3 = new CowContestScript(3);
    private static final Script cowJudge4 = new CowContestScript(4);
    private static final Script cowFinish = new Script(new ResourceLocation(MODID, "cow_finish"));
    private static final Script cowWinner = new Script(new ResourceLocation(MODID, "cow_winner"));
    public static final int QUESTION = 0;
    public static final int EXPLAIN = 1;
    public static final int SELECT = 2;
    public static final int START = 3;

    private final Map<UUID, Pair<UUID, Integer>> players = new HashMap<>();
    private final Selection selection = new CowContestSelection();
    private final Selection cowSelection = new CowSelection();
    private long time;

    public QuestContestCow() {
        setNPCs(HFNPCs.MILKMAID);
    }

    //Only marked on the server side
    public void setEntrant(EntityPlayer player, Pair<EntityHarvestCow, Integer> entrant) {
        players.put(EntityHelper.getPlayerUUID(player), Pair.of(EntityHelper.getEntityUUID(entrant.getKey()), entrant.getValue()));
    }

    public boolean isPlayersCow(EntityHarvestCow cow) {
        UUID uuid = EntityHelper.getEntityUUID(cow);
        return players.values().contains(uuid);
    }

    @Override
    public void onQuestSelectedForDisplay(EntityPlayer player, EntityLiving entity, NPC npc) {
        time = CalendarHelper.getTime(player.worldObj);
    }

    private boolean isCorrectTime(long time) {
        return time >= 6000L && time <= 18000L;
    }

    @Override
    public Selection getSelection(EntityPlayer player, NPC npc) {
        if (quest_stage == SELECT) {
            return getEntrants(player).size() > 0 ? cowSelection : null;
        } else if (quest_stage != QUESTION || !isCorrectTime(time)) return null;
        else return selection;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        if (quest_stage == QUESTION && isCorrectTime(time)) return getLocalized("help"); //"Hello pretty! Welcome to the Cow Festival, how may I help you?";
        else if (quest_stage == EXPLAIN) return getLocalized("expain"); //"Just bring one of your cows, and put them inside one of the stalls. Then talk to me, to select which cow. You can then wait for any other players to join. Once ready come talk to me again to start the judging. I'll walk around and judge each cow.";
        else if (quest_stage == SELECT) {
            return getEntrants(player).size() == 0 ? getLocalized("none") : getLocalized("selected"); //"There are no cows in the stalls! Come back when you have some to pick" : "Alrighty then, pick one of the following cows to enter them!";
        } else if (quest_stage == START) {
            return getLocalized("start"); //"Well then let's get the judging started! Don't get in my way or it may delay the process";
        } else return null;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (quest_stage == EXPLAIN) quest_stage = QUESTION;
        else if (!player.worldObj.isRemote && quest_stage == START) {
            executeFestival(player, ((EntityNPCHuman)entity).getPathing());
            increaseStage(player); //Go up another level so we don't keep calling this
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected void executeFestival(EntityPlayer player, EntityAIPathing pathing) {
        TownData town = TownHelper.getClosestTownToEntity(player, false);
        List<EntityHarvestCow> spawned = new ArrayList<>();
        spawnCowIfNotExists(player.worldObj, town.getCoordinatesFor(BuildingLocations.PARK_COW_1), spawned);
        spawnCowIfNotExists(player.worldObj, town.getCoordinatesFor(BuildingLocations.PARK_COW_2), spawned);
        spawnCowIfNotExists(player.worldObj, town.getCoordinatesFor(BuildingLocations.PARK_COW_3), spawned);
        spawnCowIfNotExists(player.worldObj, town.getCoordinatesFor(BuildingLocations.PARK_COW_4), spawned);
        ScheduleMove cow1 = ScheduleMove.of(town.getCoordinatesFor(BuildingLocations.PARK_COW_1));
        ScheduleMove cow2 = ScheduleMove.of(town.getCoordinatesFor(BuildingLocations.PARK_COW_2));
        ScheduleMove cow3 = ScheduleMove.of(town.getCoordinatesFor(BuildingLocations.PARK_COW_3));
        ScheduleMove cow4 = ScheduleMove.of(town.getCoordinatesFor(BuildingLocations.PARK_COW_4));
        ScheduleMove judge = ScheduleMove.of(town.getCoordinatesFor(BuildingLocations.PARK_COW_JUDGE));
        ScheduleWinner winner = new ScheduleWinner(player.worldObj, this, spawned);
        pathing.setPath(cow1, ScheduleSpeech.of(cowJudge1), cow2, ScheduleSpeech.of(cowJudge2),
                cow3, ScheduleSpeech.of(cowJudge3), cow4, ScheduleSpeech.of(cowJudge4), ScheduleWait.of(1),
                ScheduleSpeech.of(cowFinish), judge, ScheduleSpeech.of(cowWinner), winner);
    }

    private void spawnCowIfNotExists(World world, BlockPos pos, List<EntityHarvestCow> spawned) {
        EntityHarvestCow cow = CowSelection.getClosestCow(world, pos);
        if (cow == null) {
            cow = new EntityHarvestCow(world);
            cow.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ()); //TODO: Add locations that are inside the pen for spawning
            world.spawnEntityInWorld(cow); //We create a cow
            spawned.add(cow); //So we can remove later
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        players.clear(); //Reset the data on reading
        NBTTagList list = nbt.getTagList("Entries", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            UUID player = UUID.fromString(tag.getString("Player"));
            UUID animal = UUID.fromString(tag.getString("Animal"));
            Integer stall = tag.getInteger("Stall");
            players.put(player, Pair.of(animal, stall));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList list = new NBTTagList();
        for (Entry<UUID, Pair<UUID, Integer>> entry: players.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Player", entry.getKey().toString());
            tag.setString("Animal", entry.getValue().getKey().toString());
            tag.setInteger("Stall", entry.getValue().getValue());
            list.appendTag(tag);
        }

        nbt.setTag("Entries", list);
        return nbt;
    }

    public static class ScheduleWinner extends ScheduleElement {
        private static final ItemStack[] REWARDS = new ItemStack[] {
                HFCooking.MEAL.getCreativeStack(Meal.MILK_STRAWBERRY),
                HFCooking.MEAL.getCreativeStack(Meal.MILK_HOT),
                new ItemStack(Items.WHEAT)
        };

        private final AnimalContestEntries entries;
        private final List<EntityHarvestCow> spawned;

        public ScheduleWinner(World world, QuestContestCow quest, List<EntityHarvestCow> spawned) {
            this.entries = getEntries(world, quest);
            this.spawned = spawned;
        }

        public static AnimalContestEntries getEntries(World world, QuestContestCow quest) {
            return new AnimalContestEntries(world, quest.players, HFNPCs.BARN_OWNER, HFNPCs.GS_OWNER, HFNPCs.TRADER, HFNPCs.CARPENTER);
        }

        public void execute(EntityAgeable npc) {
            super.execute(npc);
            for (Place place: Place.VALUES) {
                entries.getEntry(place).reward(place, entries.getNPCs(), REWARDS);
            }

            spawned.stream().forEach(EntityHarvestCow::setDead);
        }
    }
}
