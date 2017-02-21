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
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.entity.EntityNPCHuman;
import joshie.harvest.npcs.entity.ai.EntityAIPathing;
import joshie.harvest.quests.base.QuestFestival;
import joshie.harvest.quests.town.festivals.cow.CowContestEntries;
import joshie.harvest.quests.town.festivals.cow.CowContestScript;
import joshie.harvest.quests.town.festivals.cow.CowContestSelection;
import joshie.harvest.quests.town.festivals.cow.CowSelection;
import joshie.harvest.town.BuildingLocations;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.quests.town.festivals.cow.CowSelection.getEntrants;

@HFQuest("festival.cow")
public class QuestContestCow extends QuestFestival {
    private static final Script cowJudge = new CowContestScript();
    private static final Script cowFinish = new Script(new ResourceLocation(MODID, "cow_finish"));
    private static final Script cowWinner = new Script(new ResourceLocation(MODID, "cow_winner"));
    public static final int QUESTION = 0;
    public static final int EXPLAIN = 1;
    public static final int SELECT = 2;
    public static final int START = 3;
    private final Map<UUID, EntityHarvestCow> players = new HashMap<>();
    private final Selection selection = new CowContestSelection();
    private final Selection cowSelection = new CowSelection();
    private long time;

    public QuestContestCow() {
        setNPCs(HFNPCs.MILKMAID);
    }

    //Only marked on the server side
    public void setEntrant(EntityPlayer player, EntityHarvestCow entrant) {
        players.put(EntityHelper.getPlayerUUID(player), entrant);
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
        if (quest_stage == QUESTION && isCorrectTime(time)) return "Hello pretty! Welcome to the Cow Festival, how may I help you?";
        else if (quest_stage == EXPLAIN) return "Just bring one of your cows, and put them inside one of the stalls. Then talk to me, to select which cow. You can then wait for any other players to join. Once ready come talk to me again to start the judging. I'll walk around and judge each cow.";
        else if (quest_stage == SELECT) {
            return getEntrants(player).size() == 0 ? "There are no cows in the stalls! Come back when you have some to pick" : "Alrighty then, pick one of the following cows to enter them!";
        } else if (quest_stage == START) {
            return "Well then let's get the judging started! Don't get in my way or it may delay the process";
        } else return null;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (quest_stage == EXPLAIN) quest_stage = QUESTION;
        else if (!player.worldObj.isRemote && quest_stage == START) {
            EntityAIPathing pathing = ((EntityNPCHuman)entity).getPathing();
            TownData town = TownHelper.getClosestTownToEntity(entity, false);
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
            ScheduleWinner winner = new ScheduleWinner(player.worldObj, players, spawned);
            pathing.setPath(cow1, ScheduleSpeech.of(cowJudge), cow2, ScheduleSpeech.of(cowJudge),
                    cow3, ScheduleSpeech.of(cowJudge), cow4, ScheduleSpeech.of(cowJudge), ScheduleWait.of(1),
                    ScheduleSpeech.of(cowFinish), judge, ScheduleSpeech.of(cowWinner), winner);
            increaseStage(player); //Go up another level so we don't keep calling this
        }
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

    private static class ScheduleWinner extends ScheduleElement {
        private final CowContestEntries entries;
        private final List<EntityHarvestCow> spawned;

        public ScheduleWinner(World world, Map<UUID, EntityHarvestCow> players, List<EntityHarvestCow> spawned) {
            this.entries = new CowContestEntries(world, players);
            this.spawned = spawned;
        }

        public void execute(EntityAgeable npc) {
            super.execute(npc);
            entries.winner(); //Kill all those in the spawned list
            spawned.stream().forEach(EntityHarvestCow::setDead);
        }
    }
}
