package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.HFQuests;
import joshie.harvest.quests.Quests;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Set;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;
import static joshie.harvest.core.helpers.InventoryHelper.SPECIAL;
import static joshie.harvest.core.helpers.InventoryHelper.SearchType.FLOWER;
import static joshie.harvest.npcs.HFNPCs.*;
import static joshie.harvest.quests.Quests.GODDESS_MEET;

@HFQuest("tutorial.carpenter")
public class QuestMeetYulif extends Quest {
    private static final ItemStack GODDESS_STACK = HFApi.npc.getStackForNPC(HFNPCs.GODDESS);
    private static final ItemStack FLOWER_GIRL_STACK = HFApi.npc.getStackForNPC(HFNPCs.FLOWER_GIRL);
    private static final int WELCOME = 0;
    private static final int LOGS = 1;
    private static final int SEED_CHAT = 2;
    private static final int FINISHED = 3;
    private boolean attempted = false;

    public QuestMeetYulif() {
        setNPCs(GODDESS, CARPENTER, FLOWER_GIRL);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(GODDESS_MEET);
    }

    private String getWoodAmount() {
        if (HFQuests.LOGS_CARPENTER == 24) return getLocalized("wood.dozentwo");
        else if (HFQuests.LOGS_CARPENTER == 12) return getLocalized("wood.dozen");
        else if (HFQuests.LOGS_CARPENTER == 64) return getLocalized("wood.stack");
        else if (HFQuests.LOGS_CARPENTER == 32) return getLocalized("wood.stackhalf");
        else if (HFQuests.LOGS_CARPENTER == 16) return getLocalized("wood.stackquarter");
        else return "" + HFQuests.LOGS_CARPENTER;
    }

    @Override
    public ItemStack getCurrentIcon(World world, EntityPlayer player) {
        if (quest_stage == LOGS || quest_stage == FINISHED) return GODDESS_STACK;
        else if (quest_stage == SEED_CHAT) return FLOWER_GIRL_STACK;
        else return super.getCurrentIcon(world, player);
    }

    @Override
    public String getDescription(World world, EntityPlayer player) {
        if (quest_stage == WELCOME) return getLocalized("description.info");
        else if (quest_stage == LOGS) return getLocalized("description.logs", getWoodAmount());
        else if (quest_stage == SEED_CHAT) return getLocalized("description.jade");
        else if (quest_stage == FINISHED) return getLocalized("description.goddess");
        else return super.getDescription(world, player);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        if (quest_stage == WELCOME && npc == HFNPCs.GODDESS) {
            return getLocalized("welcome", getWoodAmount());
        } else if (quest_stage == LOGS && npc == HFNPCs.GODDESS) {
            /*  Goddess Tells the player thank you for the logs
            She then gifts the player a blueprint tells the player that this is a blueprint
            You can use them to instruct the carpenter to build a new building
            It will display a ghost image of the building, helping you plan where to place it
            Simply right click this, and all of a sudden a builder will appear near the site
            He will start building, she also mentions that when you have multiple blueprints
            Right clicking will add them to a queue, and the builder will get around to the building eventually
            After explaining what it does, she sets you a task to build the carpenter, she also reminds you that Jade
            Will move in with yulif and she can often be found upstairs in the house, she requests that you deliver some sort of flower
            To jaded, and tells you that she carries around a bunch of seeds, but she is always on the lookout for flowers instead
            Normally she would ask for 10 flowers, but for a one off deal she is doing one flower for some seeds */
            if (InventoryHelper.getHandItemIsIn(player, ORE_DICTIONARY, "logWood", HFQuests.LOGS_CARPENTER) != null) {
                return getLocalized("thanks.build");
            }

            /* The goddess reminds you that she wants you to give her 64 logs */
            return getLocalized("reminder.wood", getWoodAmount());
        } else if (quest_stage == SEED_CHAT) {
            if (npc == HFNPCs.GODDESS) {
                /*The Goddess reminds the player that she has asked you to deliver a flower to jaded, and to do so
                  You must get the carpenter house built, she says that if you lost the blueprint, then bring the goddess
                  Another 64 logs of wood, and she will happily give you a blueprint again */
                if (attempted && InventoryHelper.getHandItemIsIn(player, ORE_DICTIONARY, "logWood", HFQuests.LOGS_CARPENTER) != null) {
                    return getLocalized("reminder.give");
                } else {
                    attempted = true;
                    return getLocalized("reminder.carpenter", getWoodAmount());
                }
            } else if (npc == HFNPCs.FLOWER_GIRL) {
                /*Jade thanks you for the flowers, she then proceeds
                  She then informs you that the goddess would like to see you again
                  She has a reward, She says to come back to see her after you have
                  revisited the goddess, as she has something to show you */
                if (InventoryHelper.getHandItemIsIn(player, SPECIAL, FLOWER, 1) != null) {
                    return getLocalized("thanks.flowers");
                }

                /* Jade tells you that it is nice to meet you, and that she's looking for some kind of flower */
                return getLocalized("reminder.flowers");
            }
        } else if (quest_stage == FINISHED && npc == HFNPCs.GODDESS) {
            /* Goddess thanks the player for helping out get the town started, She explains that she understands that you'll need something
               to get started, so she gives you 1000 gold */
            return getLocalized("thanks.finish");
        }

        return null;
    }

    /* Rewards Logic */
    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (quest_stage == WELCOME && npc == HFNPCs.GODDESS) {
            increaseStage(player);
        } else if (quest_stage == LOGS && npc == HFNPCs.GODDESS) {
            if (InventoryHelper.takeItemsIfHeld(player, ORE_DICTIONARY, "logWood", HFQuests.LOGS_CARPENTER) != null) {
                HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().learnNote(HFNotes.BLUEPRINTS);
                if (HFBuildings.CHEAT_BUILDINGS) rewardItem(player, HFBuildings.CARPENTER.getSpawner());
                else rewardItem(player, HFBuildings.CARPENTER.getBlueprint());
                increaseStage(player);
            }
        } else if (quest_stage == SEED_CHAT) {
            if (npc == HFNPCs.GODDESS) {
                if (attempted && InventoryHelper.takeItemsIfHeld(player, ORE_DICTIONARY, "logWood", HFQuests.LOGS_CARPENTER) != null) {
                    rewardItem(player, HFBuildings.CARPENTER.getBlueprint());
                } else attempted = true;
            } else if (npc == HFNPCs.FLOWER_GIRL){
                if (InventoryHelper.takeItemsIfHeld(player, SPECIAL, FLOWER, 1) != null) {
                    increaseStage(player);
                }
            }
        } else if (quest_stage == FINISHED && npc == HFNPCs.GODDESS) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.quests.completeQuestConditionally(Quests.BUILDING_CARPENTER, player);
        rewardGold(player, 1000);
    }
}