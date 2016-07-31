package joshie.harvest.quests.tutorial;

import joshie.harvest.api.HFRegister;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.helpers.generic.OreDictionaryHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import static joshie.harvest.npc.HFNPCs.GODDESS;
import static joshie.harvest.npc.HFNPCs.SEED_OWNER;
import static joshie.harvest.quests.QuestHelper.*;

@HFRegister(data = "tutorial.farming")
public class QuestGoddess extends Quest {   
    public QuestGoddess() {
        setNPCs(GODDESS, SEED_OWNER);
    }
    
    @Override
    public void onStageChanged(EntityPlayer player, int previous, int stage) {
        //On completion of stage 0, give the player 4 goddess flowers
        if (previous == 0) {
            rewardItem(player, new ItemStack(HFCore.FLOWERS, 4, 0));
        } else if (previous == 1) {
            rewardItem(player, HFBuildings.CARPENTER.getBlueprint());
            rewardItem(player, new ItemStack(Blocks.RED_FLOWER, 1, player.worldObj.rand.nextInt(8)));
        } else if (previous == 3) {
            ItemStack seeds = HFCrops.TURNIP.getSeedStack().copy();
            seeds.stackSize = 10;
            rewardItem(player, seeds);
        }
    }
    
    @Override
    public void claim(EntityPlayer player) {
        rewardGold(player, 1000);
        rewardItem(player, new ItemStack(HFTools.HOE));
        rewardItem(player, new ItemStack(HFTools.WATERING_CAN));
        rewardItem(player, new ItemStack(HFTools.SICKLE));
    }

    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (npc == GODDESS) {
            if (quest_stage == 0) {
                increaseStage(player);
                return "welcome"; //Greet the player, tell them to gather 64 logs for you, Give them 4 goddess flowers
            } else if (quest_stage == 1) {
                //If the player has 64 logs, take them away
                ItemStack held = player.getHeldItemMainhand();
                if (held != null) {
                    boolean isLogs = OreDictionaryHelper.isLogs(held);
                    if (isLogs && held.stackSize >= 64) {
                        takeHeldStack(player, 64);
                        increaseStage(player);
                        //Thank the player for the 64 logs that they gave you, Give them a 'Carpenter' Building, and a flower
                        //Explain to them how to use the carpenter, building and how it will spawn a npc builder
                        //Explain to them, that you need some sort of seeds, and that a girl will give you some
                        //When you build the carpenters house, Her name will be jade and she will live upstairs
                        //Also explain that she is the carpenters sister, Explain that she will only give you seeds
                        //If you give her 3 flowers
                        return "thanks";
                    }
                }
                
                return "reminder.welcome"; //Remind the player, that you need 64 logs to get them started
            } else if (quest_stage == 2 || quest_stage == 3) {
                //Remind the player, that they must talk to jade, and have three flowers on them
                //And then jade will take them, and give you some seeds
                return "reminder.seeds";
            } else if (quest_stage == 4) {
                //Thank the player for giving you some seeds, tell them that they can keep them
                //Give the player a hoe, a watering can and a sickle.
                //Explain to them that jade is a real pro when it comes to farming
                //Inform them, that they should return to her house
                //Tell them, that if they want to learn how to farm
                //Then they should talk to jade. Also explain to them
                //That if they talk to yulif, they will be able to order, different buildings for construction
                //Also explain that the only place that they can order is the supermarket
                //Explain that overtime, different buildings will open up that they can build
                //And they should ideally keep expanding the town, so that they can grow their farm
                //Explain a little backstory about why the town was destroyed, and why they needed
                //your help and funding to rebuild their work. Thank the player for their help
                //Give the player 1000 gold
                complete(player);
                return "completed";
            }
        } else if (npc == SEED_OWNER) {
            if (quest_stage == 2) {
                //I am jade, and i like flowers and i will give you seeds
                increaseStage(player);
                return "jade.hello";
            } else if (quest_stage == 3) {
                //I am jade, remind the player about wanting some pretty flowers
               //If the player has 3 flowers, take them away
                ItemStack held = player.getActiveItemStack();
                if (held != null) {
                    boolean isFlowers = OreDictionaryHelper.isFlowers(held);
                    if (isFlowers && held.stackSize >= 3) {
                        takeHeldStack(player, 3);
                        increaseStage(player);
                      //Thank the player for the flowers, Give them 10 turnip seeds
                        //Explain to them, that they can come visit them anytime
                        //They will exchange 10 flowers, for 1 turnip seed
                        //For up to a maximum of 64 turnip seeds
                        return "jade.complete";
                    }
                }
                
                return "jade.reminder";
            } else if (quest_stage == 4) {
                //Remind the player that they must return to visit
                //The harvest goddess, with the seeds in tact
                return "jade.goddess";
            }
        }
        
        return null;
    }
}
