package joshie.harvest.quests.town.festivals;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.npc.schedule.ScheduleMove;
import joshie.harvest.api.npc.schedule.ScheduleSpeech;
import joshie.harvest.api.npc.schedule.ScheduleWait;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.tile.TileCookingStand;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.entity.EntityNPCHuman;
import joshie.harvest.npcs.entity.ai.EntityAIPathing;
import joshie.harvest.quests.base.QuestFestival;
import joshie.harvest.quests.selection.FestivalSelection;
import joshie.harvest.quests.town.festivals.cooking.CookingContestEntries;
import joshie.harvest.quests.town.festivals.cooking.CookingContestScript;
import joshie.harvest.town.BuildingLocations;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

@HFQuest("festival.cooking")
public class QuestCookingContest extends QuestFestival {
    private static final Script SCRIPT = new CookingContestScript();
    private final Selection selection = new FestivalSelection("cooking");
    private final int QUESTION = 0;
    private final int CONTEST = 1;
    private final int WINNER = 2;
    private Map<UUID, BlockPos> blockPosList = new HashMap<>();

    public QuestCookingContest() {
        setNPCs(HFNPCs.GS_OWNER);
    }

    public boolean isEmpty() {
        return blockPosList.size() == 0;
    }

    public boolean isFull() {
        return blockPosList.size() >= 4;
    }

    public void addStand(UUID uuid, BlockPos pos) {
        blockPosList.put(uuid, pos);
    }

    public void removeStand(BlockPos pos) {
        Iterator<Entry<UUID, BlockPos>> it = blockPosList.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue().equals(pos)) {
                it.remove();
            }
        }
    }

    @Override
    public Selection getSelection(EntityPlayer player, NPC npc) {
        if (quest_stage <= QUESTION) {
            return selection;
        } else return null;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        if (quest_stage == QUESTION) return "We are describing the contest to the player";
        if (quest_stage == CONTEST) return "We have started the contest... with " + " " + blockPosList.size();
        if (quest_stage == WINNER) return "Today I have made a strumpfel blah blah";
        else return null;
    }

    public CookingContestEntries getWinner(EntityLiving ageable) {
        return new CookingContestEntries(ageable.worldObj, blockPosList);
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (!player.worldObj.isRemote && quest_stage == CONTEST) {
            TownData town = TownHelper.getClosestTownToEntity(entity);
            BlockPos original = town.getCoordinatesFor(BuildingLocations.PARK_LAMP_BACK);
            //Schedule for Jenni
            EntityAIPathing pathing = ((EntityNPCHuman)entity).getPathing();
            ScheduleMove point1 = ScheduleMove.of(town.getCoordinatesFor(BuildingLocations.PARK_STAGE_RIGHT));
            ScheduleMove point2 = ScheduleMove.of(town.getCoordinatesFor(BuildingLocations.PARK_STAGE_LEFT));
            ScheduleSpeech speech = ScheduleSpeech.of(SCRIPT);
            pathing.setPath(point1, point2, point1, speech);

            Map<BlockPos, BlockPos> emptyStands = buildEmptyMap(town);
            Set<BlockPos> filled = new HashSet<>();
            CookingContestEntries winner = getWinner(entity);
            List<EntityNPCHuman> npcs = EntityHelper.getEntities(EntityNPCHuman.class, player.worldObj, original, 32D, 5D);
            for (EntityNPCHuman theNPC: npcs) {
                if (winner.isUUIDInContest(theNPC.getNPC().getUUID())) {
                    ItemStack stack = winner.getEntryForUUID(theNPC.getNPC().getUUID());
                    for (Entry<BlockPos, BlockPos> entry: emptyStands.entrySet()) {
                        if (!filled.contains(entry.getKey())) {
                            TileEntity tile = player.worldObj.getTileEntity(entry.getKey());
                            if (tile instanceof TileCookingStand) {
                                ((TileCookingStand)tile).setContents(stack);
                            }

                            theNPC.getPathing().setPath(ScheduleMove.of(entry.getValue().down()), ScheduleWait.of(10));
                            filled.add(entry.getKey());
                            break;
                        }
                    }
                }
            }

            increaseStage(player);
        } else if (quest_stage == WINNER) {
            complete(player); //Finish the contest
            CookingContestEntries winner = getWinner(entity);
            if (EntityHelper.getPlayerUUID(player).equals(winner.getWinnerID())) {
                rewardGold(player, winner.getPrize());
            }
        }
    }

    private Map<BlockPos, BlockPos> buildEmptyMap(TownData town) {
        Map<BlockPos, BlockPos> emptyList = new HashMap<>();
        addToEmptyListIfStandIsEmpty(emptyList, town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND1).down(), town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND1_HUMAN));
        addToEmptyListIfStandIsEmpty(emptyList, town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND2).down(), town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND2_HUMAN));
        addToEmptyListIfStandIsEmpty(emptyList, town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND3).down(), town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND3_HUMAN));
        addToEmptyListIfStandIsEmpty(emptyList, town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND4).down(), town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND4_HUMAN));
        return emptyList;
    }

    private void addToEmptyListIfStandIsEmpty(Map<BlockPos, BlockPos> emptyList, BlockPos pos, BlockPos pos2) {
        if (!blockPosList.values().contains(pos)) emptyList.put(pos, pos2);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        blockPosList = NBTHelper.readPosList("Positions", nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTHelper.writePosList("Positions", nbt, blockPosList);
        return super.writeToNBT(nbt);
    }
}
