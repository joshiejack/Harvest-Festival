package joshie.harvest.quests.player.meetings;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.block.BlockTrough.Trough;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.animals.item.ItemAnimalTool.Tool;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.quests.selection.TutorialSelection;
import joshie.harvest.tools.ToolHelper;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

import javax.annotation.Nullable;
import java.util.Set;

import static joshie.harvest.animals.item.ItemAnimalTool.Tool.BRUSH;
import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;
import static joshie.harvest.npcs.HFNPCs.BARN_OWNER;
import static joshie.harvest.npcs.HFNPCs.BUILDER;
import static joshie.harvest.quests.Quests.YULIF_MEET;

@HFQuest("tutorial.cow")
public class QuestMeetJim extends QuestQuestion {
    private static final int START = 0;
    private static final int INFO = 1;
    private static final int ACTION1 = 2;
    private static final int ACTION2 = 3;
    private static final int MILKER = 4;
    private static final int MILKING = 5;
    private boolean hasFed;
    private boolean hasBrushed;
    private boolean hasMilked;

    public QuestMeetJim() {
        super(new TutorialSelection("cow"));
        setNPCs(BUILDER, BARN_OWNER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(YULIF_MEET);
    }

    @Override
    public boolean onEntityInteract(EntityPlayer player, @Nullable ItemStack held, EnumHand hand, Entity target) {
        if (quest_stage == ACTION1 || quest_stage == ACTION2) {
            if (target instanceof EntityHarvestCow) {
                if (held != null) {
                    if (!hasFed && held.isItemEqual(HFCrops.GRASS.getCropStack(1))) {
                        hasFed = true;
                        increaseStage(player);
                        return true;
                    } else if (!hasBrushed && ToolHelper.isBrush(held)) {
                        hasBrushed = true;
                        increaseStage(player);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public Selection getSelection(EntityPlayer player, NPC npc) {
        return npc == BARN_OWNER && quest_stage <= 0 ? selection : null;
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        if (npc == BUILDER) {
            if (player.worldObj.rand.nextFloat() < 0.15F) {
                if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.BARN)) {
                    //If the barn exists yulif will tell the player to go and talk to jim
                    return getLocalized("reminder.intro");
                }

                //Yulif reminds the player that you should go and build a ranch
                //So that you can get additional animals
                return getLocalized("reminder.barn");
            } else return null;
        } else if (npc == BARN_OWNER) {
            if (!TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.BARN)) return null;
            if (isCompletedEarly) {
                return getLocalized("completed");
            } else if (quest_stage == START) {
            /*Jim tells you that he has a spare cow, and that he's happy to give you it
            How then proceeds to ask if you know how to take care of cows */
                return getLocalized("start");
            } else if (quest_stage == INFO) {
            /*Jim says oh ok then, then he starts to describe how to take care of cows
            He starts off by telling you that like chickens, cows and other large animals
            Need to be fed and loved, but they also need to be cleaned!
            He tells you you can feed them by hand with fodder, or with a trough
            He tells you to show them love simply right click them to talk to them
            He tells you to brush you simply take a brush and right click them until hearts appear
            He then informs you to go feed and brush the cow */
                return getLocalized("info");
            } else if (quest_stage == ACTION1 || quest_stage == ACTION2) {
            /*Jim reminds the player that they should be feeding, brushing and talking to a cow
            He informs the player if they lost the fodder, he'll trade for some grass
            He informs the player if they lost the brush, he'll trade for a fishing rod */
                return getLocalized("reminder.talk");
            } else if (quest_stage == MILKER) {
            /* Jim thanks the player for taking care of the cow, he then goes on to explain
            Just like chickens cows will produce a product, milk, in order to obtain milk
            You need to take a milker and right click them
            He also says that the friendlier you are with cows the more you can milk them
            And the larger the milk they produce, normally can only milk once a day
            Jim then asks the player to go and milk a cow */
                return getLocalized("milk");
            } else if (quest_stage == MILKING) {
            /* Jim tells the player that's almost it, he then mentions that just with the chicken
            You can autofeed larger animals with a trough, just simply place some fodder in it
            He then mentions that the animal ranch
            Is a great place to buy larger animals, and other things, so he suggests you build one */
                if (InventoryHelper.getHandItemIsIn(player, ITEM_STACK, HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.SMALL)) != null) {
                    return getLocalized("complete");
                }

            /* Jim reminds the player that he wanted the player to milk a cow
             He informs the player if they lost the milker, he'll trade for shears */
                return getLocalized("reminder.milk");
            }
        }

        return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (!TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.BARN)) return;
        if (isCompletedEarly || quest_stage == INFO) {
            if (isCompletedEarly) {
                complete(player);
                rewardItem(player, HFAnimals.TOOLS.getStackFromEnum(Tool.MILKER));
            } else increaseStage(player);

            rewardEntity(player, "harvestfestival.cow");
            rewardItem(player, new ItemStack(Items.LEAD));
            rewardItem(player, HFCrops.GRASS.getCropStack(64));
            rewardItem(player, HFAnimals.TOOLS.getStackFromEnum(BRUSH));
        } else if (quest_stage == MILKER) {
            increaseStage(player);
            rewardItem(player, HFAnimals.TOOLS.getStackFromEnum(Tool.MILKER));
        } else if (quest_stage == MILKING) {
            if (InventoryHelper.getHandItemIsIn(player, ITEM_STACK, HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.SMALL)) != null) {
                complete(player);
            }
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.COW_CARE);
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.ANIMAL_HAPPINESS);
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.ANIMAL_STRESS);
        rewardItem(player, HFAnimals.ANIMAL_PRODUCT.getStackOfSize(Sizeable.MILK, Size.LARGE, 3));
        rewardItem(player, HFAnimals.TROUGH.getStackFromEnum(Trough.WOOD));
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