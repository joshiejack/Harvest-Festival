package joshie.harvest.festivals.quests;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.schedule.SchedulePosition;
import joshie.harvest.api.npc.schedule.ScheduleSpeech;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.festivals.HFFestivals;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.entity.EntityNPCHuman;
import joshie.harvest.npcs.entity.ai.EntityAIPathing;
import joshie.harvest.quests.base.QuestFestival;
import joshie.harvest.quests.selection.FestivalSelection;
import joshie.harvest.town.BuildingLocations;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownBuilding;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

@HFQuest("festival.cooking")
public class QuestCookingFestival extends QuestFestival {
    private final Selection selection = new FestivalSelection("cooking");
    private final int QUESTION = 0;
    private final int INTRO = 1;
    private final int SECOND = 2;
    private final int THIRD = 3;
    private final int FOURTH = 4;
    private HashMap<UUID, BlockPos> blockPosList = new HashMap<>();

    public QuestCookingFestival() {
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
        if (quest_stage == INTRO) return "We have started the contest... with " + " " + blockPosList.size();
        if (quest_stage == SECOND) return "Today I have made a strumpfel blah blah";
        if (quest_stage == THIRD) return "Today I have made a chicken wing blah blah";
        if (quest_stage == FOURTH) return "Okay the results are now in... I've decided who I want to win";
        else return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (quest_stage == QUESTION) return;
        else if (!player.worldObj.isRemote && quest_stage == INTRO) {
            TownData town = TownHelper.getClosestTownToEntity(entity);
            TownBuilding building = town.getBuilding(HFFestivals.FESTIVAL_GROUNDS);
            BlockPos original = town.getCoordinatesFor(BuildingLocations.PARK_LAMP_BACK);
            EnumFacing rotation = building.rotation.rotate(EnumFacing.SOUTH);
            EntityAIPathing pathing = ((EntityNPCHuman)entity).getPathing();
            SchedulePosition accross6 = SchedulePosition.of(original.offset(rotation, 6));
            ScheduleSpeech speech = ScheduleSpeech.of(HFFestivals.COOKING1);
            pathing.setPath(accross6, accross6.offset(rotation.rotateY().getOpposite(), 5), accross6, speech);
            increaseStage(player);
        } else if (quest_stage == SECOND) {

        }
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
