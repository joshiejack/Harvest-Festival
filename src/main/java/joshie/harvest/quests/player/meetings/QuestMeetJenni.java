package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.quests.Quests;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Set;

import static joshie.harvest.api.calendar.Season.AUTUMN;
import static joshie.harvest.api.calendar.Season.SUMMER;
import static joshie.harvest.npcs.HFNPCs.GS_OWNER;
import static joshie.harvest.quests.Quests.JADE_MEET;

@HFQuest("tutorial.supermarket")
public class QuestMeetJenni extends Quest {
    private static final ItemStack SUPERMARKET = HFBuildings.SUPERMARKET.getSpawner();
    private static final int START = 0;

    public QuestMeetJenni() {
        setNPCs(GS_OWNER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(JADE_MEET);
    }

    @Override
    public String getDescription(World world, EntityPlayer player) {
        if (!TownHelper.getClosestTownToEntity(player).hasBuilding(HFBuildings.SUPERMARKET)) return getLocalized("description.build");
        else return getLocalized("description.visit");
    }

    @Override
    public ItemStack getCurrentIcon(World world, EntityPlayer player) {
        if (!TownHelper.getClosestTownToEntity(player).hasBuilding(HFBuildings.SUPERMARKET)) return SUPERMARKET;
        else return super.getCurrentIcon(world, player);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        if (!TownHelper.getClosestTownToEntity(player).hasBuilding(HFBuildings.SUPERMARKET)) return null;
        //Jenni says hey there I'm the owner, welcome to the supermarket, here you can buy all kinds of things
        //From seeds to chocolate, If you need anything, just ask me
        //She then says we're open every weekday except wednesday from 9am to 5pm
        //We're also open on saturdays but only from 11am to 3pm
        //She says you're our first customer so here's a free gift
        return getLocalized("welcome.owner");
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (TownHelper.getClosestTownToEntity(player).hasBuilding(HFBuildings.SUPERMARKET)) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.quests.completeQuestConditionally(Quests.BUILDING_SUPERMARKET, player);
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.SUPERMARKET);
        Season season = HFApi.calendar.getDate(player.worldObj).getSeason();
        if (season == SUMMER) rewardItem(player, HFCrops.TOMATO.getSeedStack(4));
        else if (season == AUTUMN) rewardItem(player, HFCrops.EGGPLANT.getSeedStack(4));
        else rewardItem(player, HFCrops.CUCUMBER.getSeedStack(4));
    }
}