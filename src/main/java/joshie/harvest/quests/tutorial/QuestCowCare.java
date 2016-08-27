package joshie.harvest.quests.tutorial;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.api.HFQuest;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.SizeableHelper;
import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.quests.QuestQuestion;
import joshie.harvest.quests.TutorialSelection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;

import static joshie.harvest.animals.item.ItemAnimalTool.Tool.BRUSH;
import static joshie.harvest.animals.item.ItemAnimalTool.Tool.MILKER;
import static joshie.harvest.core.lib.HFQuests.TUTORIAL_CHICKEN;
import static joshie.harvest.npc.HFNPCs.BUILDER;

@HFQuest(data = "tutorial.cow")
public class QuestCowCare extends QuestQuestion {
    private boolean attempted;
    private boolean hasFed;
    private boolean hasBrushed;
    private boolean hasMilked;

    public QuestCowCare() {
        super(new TutorialSelection("cow"));
        setNPCs(BUILDER);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TUTORIAL_CHICKEN);
    }

    @Override
    public void onEntityInteract(EntityPlayer player, Entity target) {
        if (quest_stage == 2) {
            if (target instanceof EntityHarvestCow) {
                ItemStack held = player.getActiveItemStack();
                if (held != null) {
                    if (!hasFed && held.isItemEqual(HFCrops.GRASS.getCropStack())) {
                        hasFed = true;
                        if(!player.worldObj.isRemote) increaseStage(player);
                    } else if (!hasBrushed && ToolHelper.isBrush(held)) {
                        hasBrushed = true;
                        if(!player.worldObj.isRemote) increaseStage(player);
                    }
                }
            }
        } else if (quest_stage == 5) {
            if (target instanceof EntityHarvestCow) {
                EntityHarvestCow cow = (EntityHarvestCow) target;
                ItemStack held = player.getActiveItemStack();
                if (held != null) {
                    if (!hasMilked && ToolHelper.isMilker(held)) {
                        if (cow.getData().canProduce()) {
                            hasMilked = true;
                            if(!player.worldObj.isRemote) increaseStage(player);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == 0) {
            //Yulif tells you that he has a spare cow, and that he's happy to give you it
            //How then proceeds to ask if you know how to take care of cows
            return "question";
        } else if (quest_stage == 1) {
            increaseStage(player);
            //Yulif says oh ok then, then he starts to describe how to take care of cows
            //He starts off by telling you that like chickens, cows and other large animals
            //Need to be fed and loved, but they also need to be cleaned!
            //He tells you you can feed them by hand with fodder, or with a trough
            //He tells you to show them love simply right click them to talk to them
            //He tells you to brush you simply take a brush and right click them until hearts appear
            //He then informs you to go feed and brush the cow
            return "info";
        } else if (quest_stage == 2 || quest_stage == 3) {
            ItemStack held = player.getHeldItemMainhand();
            if (attempted && held != null) {
                if (held.getItem() instanceof ItemFishingRod) {
                    takeHeldStack(player, 1);
                    rewardItem(player, HFAnimals.TOOLS.getStackFromEnum(BRUSH));
                    //Yulif thanks the player for the brush and then reminds them to brush
                    //the cow and feed it by hand
                    return "reminder.brush";
                } else if (held.getItem() == Items.WHEAT) {
                    takeHeldStack(player, 1);
                    rewardItem(player, HFCrops.GRASS.getCropStack());
                    //Yulif thanks the player for the wheat, gives fodder and thanks the player
                    return "reminder.fodder";
                }
            }

            //Yulif reminds the player that they should be feeding, brushing and talking to a cow
            //He informs the player if they lost the fodder, he'll trade for some wheat
            //He informs the player if they lost the brush, he'll trade for a fishing rod
            attempted = true;
            return "reminder.talk";
        } else if (quest_stage == 4) { //Jeremy expects you to have brushed 1 cow, fed 1 cow and to have milked 1 cow
            //Yulif thanks the player for taking care of the cow, he then goes on to explain
            //Just like chickens cows will produce a product, milk, in order to obtain milk
            //You need to take a milker and right click them
            //He also says that the friendlier you are with cows the more you can milk them
            //And the larger the milk they produce, normally can only milk once a day
            //Yulif then asks the player to go and milk a cow
            attempted = false;
            increaseStage(player);
            return "milk";
        } else if (quest_stage == 5) {
            ItemStack held = player.getHeldItemMainhand();
            if (attempted && held != null) {
                if (held.getItem() instanceof ItemShears) {
                    takeHeldStack(player, 1);
                    rewardItem(player, HFAnimals.TOOLS.getStackFromEnum(MILKER));
                    //Yulif thanks the player for the shears, and gives them a milk, reminding them to go milk a cow
                    return "reminder.brush";
                }
            }

            //Yulif reminds the player that he wanted the player to milk a cow
            //He informs the player if they lost the milker, he'll trade for shears
            attempted = true;
            return "reminder.milk";
        } else if (quest_stage == 6) {
            //Yulif tells the player that's almost it, he then mentions that just with the chicken
            //You can autofeed larger animals with a trough, just simply place some fodder in it
            //He then mentions that the animal ranch
            //Is a great place to buy larger animals, and other things, so he suggests you build one
            complete(player);
            return "complete";
        }

        return null;
    }

    @Override
    public void onStageChanged(EntityPlayer player, int previous, int stage) {
        if (previous == 1) { //Gives the player the basic tools to help them
            rewardEntity(player, "harvestfestival.cow");
            rewardItem(player, new ItemStack(Items.LEAD));
            rewardItem(player, new ItemStack(HFCrops.GRASS.getCropStack().getItem(), 16, HFCrops.GRASS.getCropStack().getItemDamage()));
            rewardItem(player, HFAnimals.TOOLS.getStackFromEnum(BRUSH));
        } else if (previous == 5) {
            rewardItem(player, HFAnimals.TOOLS.getStackFromEnum(MILKER));
        }
    }

    @Override
    public void claim(EntityPlayer player) {
        if (quest_stage == 0) {
            rewardEntity(player, "harvestfestival.cow");
            rewardItem(player, new ItemStack(Items.LEAD));
            rewardItem(player, new ItemStack(HFCrops.GRASS.getCropStack().getItem(), 16, HFCrops.GRASS.getCropStack().getItemDamage()));
            rewardItem(player, HFAnimals.TOOLS.getStackFromEnum(BRUSH));
            rewardItem(player, HFAnimals.TOOLS.getStackFromEnum(MILKER));
        }

        rewardItem(player, SizeableHelper.getSizeable(HFAnimals.MILK, 1, Size.LARGE));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        hasFed = nbt.getBoolean("HasFed");
        hasBrushed = nbt.getBoolean("HasBrushed");
        hasMilked = nbt.getBoolean("HasMilked");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("HasFed", hasFed);
        nbt.setBoolean("HasBrushed", hasBrushed);
        nbt.setBoolean("HasMilked", hasMilked);
        return nbt;
    }
}