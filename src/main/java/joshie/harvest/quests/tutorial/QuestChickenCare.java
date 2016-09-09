package joshie.harvest.quests.tutorial;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.entity.EntityHarvestChicken;
import joshie.harvest.animals.item.ItemAnimalTool.Tool;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.lib.HFQuests;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.quests.TutorialSelection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Set;

import static joshie.harvest.animals.block.BlockTray.Tray.NEST_EMPTY;
import static joshie.harvest.animals.item.ItemAnimalTool.Tool.CHICKEN_FEED;
import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;
import static joshie.harvest.npc.HFNPCs.GODDESS;

@HFQuest("tutorial.chicken")
public class QuestChickenCare extends QuestQuestion {
    private static final int INTRO = 0;
    private static final int THROW = 1;
    private static final int ACTION1 = 2;
    private static final int ACTION2 = 3;
    private static final int EGG = 4;
    private static final int FINAL = 5;
    private static final ItemStack CHEST = new ItemStack(Blocks.CHEST);
    private boolean attempted;
    private boolean hasThrown;
    private boolean hasFed;

    public QuestChickenCare() {
        super(new TutorialSelection("chicken"));
        setNPCs(GODDESS);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.TUTORIAL_CROPS);
    }

    @Override
    public void onEntityInteract(EntityPlayer player, @Nullable ItemStack held, EnumHand hand, Entity target) {
        if (!hasFed && (quest_stage == ACTION1 || quest_stage == ACTION2)) {
            if (target instanceof EntityChicken) {
                if (held != null) {
                    if (InventoryHelper.ITEM_STACK.matches(held, HFAnimals.TOOLS.getStackFromEnum(Tool.CHICKEN_FEED))) {
                        hasFed = true;
                    }

                    increaseStage(player);
                }
            }
        }
    }

    @Override
    public void onRightClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {
        if (!hasThrown && (quest_stage == ACTION1 || quest_stage == ACTION2)) {
            for (Entity entity: player.getPassengers()) {
                if (entity instanceof EntityHarvestChicken) {
                    hasThrown = true; //You have now thrown!
                    increaseStage(player);
                }
            }
        }
    }

    @Override
    public Selection getSelection(EntityPlayer player, INPC npc) {
        return quest_stage <= 0 && InventoryHelper.getHandItemIsIn(player, ITEM_STACK, HFCrops.TURNIP.getCropStack(1)) != null? selection : null;
    }

    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (isCompletedEarly) {
            return "completed";
        } else if (quest_stage == INTRO && InventoryHelper.getHandItemIsIn(player, ITEM_STACK, HFCrops.TURNIP.getCropStack(1)) != null) {
            /*The goddess welcomes you and sees that you have a turnip
            She thanks you for growing them for her, she explains that has a wonderful gift
            One you have never seen before, She explains she has a chicken she would like to give you
            She then proceeds to ask if you know how to care for chickens */
            return "start";
        } else if (quest_stage == THROW) {
            /*Now that the goddess knows that you do not how to take care of chicken she starts off on a rant
            She explains that in order to care four chickens, you must feed them
            She tells you that you can feed them by hand, or place chicken feed in a feeding tray
            And they will feed themselves, She also tells you that they need to be loved,
            She explains the best way for a chicken to feel loved is when you pick it up
            You can do this by right clicking it, and to put it down, right click the ground
            She explains you can also make it love you when feed it by hand
            She explains that doing this will make the chicken like you more, and in doing so
            She asks the player to go feed by hand, and throw the chicken (giving the player feed) */
            return "throw";
        } else if (quest_stage == ACTION1 || quest_stage == ACTION2) {
            if (!attempted) {
                if (InventoryHelper.getHeldItem(player) instanceof ItemSeeds) {
                    //Goddess thanks the player for the seeds and then gives them 1 chicken feed
                    return "reminder.seeds";
                } else if (InventoryHelper.getHeldItem(player) instanceof ItemEgg) {
                    /*Goddess thanks the player for the egg, she then informs the player
                    That she will give them another chicken */
                    return "reminder.chicken";
                }
            }

            /*The goddess Reminds you to go pick up and throw a chicken, as well as feed one chicken feed
            She allow informs the player that if they ran out of feed, she will happily trade for more
            She also explains that she will trade a vanilla egg for a chicken if yours happens to die
            If the player gives them seeds */
            attempted = true;
            return "reminder.throw";
        } else if (quest_stage == EGG) {
            /*The goddess congratulates you on performing the task, she then goes on to say that
            Over time the chicken will eventually produce bigger and better eggs that you can sell for more money
            She also explains that for chickens to lay eggs they need a nesting box
            Chickens will lay their eggs in here and you can then collect them and ship them off
            The goddess now asks the player to return when they have one egg from the special chickens */
            attempted = false;
            return "egg";
        } else if (quest_stage == FINAL) {
            if (HFAnimals.EGG.matches(player.getHeldItemMainhand())) {
                /*The goddess thanks the player for their time and gives them a reward of a large egg
                She explains this is a valuable egg from the best of chickens, you'll have to take care
                Of yours properly if you wish to look after it. She also heard that yulif had a spare cow
                And that you should go talk to him if you want it */
                return "complete";
            } else if (attempted && InventoryHelper.getHandItemIsIn(player, ITEM_STACK, CHEST) != null) {
                //Thanks the player for the hay, and reminds them to get her an egg
                return "reminder.nest";
            }

            /*The goddess reminds you that she wants an egg from one of the special chickens
            She also tells that if you lost the nest, bring her a hay bale */
            attempted = true;
            return "reminder.egg";
        }

        return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (isCompletedEarly) {
            complete(player);
            rewardEntity(player, "harvestfestival.chicken");
            rewardItem(player, new ItemStack(HFAnimals.TOOLS, 16, CHICKEN_FEED.ordinal()));
            rewardItem(player, HFAnimals.TRAY.getStackFromEnum(NEST_EMPTY));
            rewardItem(player, HFAnimals.EGG.getStack(Size.LARGE));
        } else if (quest_stage == THROW) {
            increaseStage(player);
            rewardEntity(player, "harvestfestival.chicken");
            rewardItem(player, new ItemStack(HFAnimals.TOOLS, 16, CHICKEN_FEED.ordinal()));
        } else if (quest_stage == ACTION1 || quest_stage == ACTION2) {
            if (!attempted) {
                if (InventoryHelper.getHeldItem(player) instanceof ItemSeeds) {
                    takeHeldStack(player, 1);
                    rewardItem(player, HFAnimals.TOOLS.getStackFromEnum(CHICKEN_FEED));
                } else if (InventoryHelper.getHeldItem(player) instanceof ItemEgg) {
                    takeHeldStack(player, 1);
                    rewardEntity(player, "harvestfestival.chicken");
                }
            }

            attempted = true;
        } else if (quest_stage == EGG) {
            attempted = false;
            increaseStage(player);
            rewardItem(player, HFAnimals.TRAY.getStackFromEnum(NEST_EMPTY));
        } else if (quest_stage == FINAL) {
            if (HFAnimals.EGG.matches(player.getHeldItemMainhand())) {
                complete(player);
                rewardItem(player, HFAnimals.EGG.getStack(Size.LARGE));
            } else if (attempted && InventoryHelper.takeItemsIfHeld(player, ITEM_STACK, CHEST) != null) {
                rewardItem(player, HFAnimals.TRAY.getStackFromEnum(NEST_EMPTY));
            }

            attempted = true;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        hasThrown = nbt.getBoolean("HasThrown");
        hasFed = nbt.getBoolean("HasFed");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("HasThrown", hasThrown);
        nbt.setBoolean("HasFed", hasFed);
        return nbt;
    }
}