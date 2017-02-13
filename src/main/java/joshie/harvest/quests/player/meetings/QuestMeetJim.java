package joshie.harvest.quests.player.meetings;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.block.BlockTrough.Trough;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.animals.item.ItemAnimalTool.Tool;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestMeetingTutorial;
import joshie.harvest.quests.selection.TutorialSelection;
import joshie.harvest.tools.ToolHelper;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static joshie.harvest.animals.item.ItemAnimalTool.Tool.BRUSH;
import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;

@HFQuest("tutorial.cow")
public class QuestMeetJim extends QuestMeetingTutorial {
    private static final ItemStack COW = HFAnimals.ANIMAL.getStackFromEnum(Spawner.COW);
    private static final ItemStack MILK_ITEM = HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.SMALL);
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
        super(new TutorialSelection("cow"), HFBuildings.BARN, HFNPCs.BARN_OWNER);
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
    public String getDescription(World world, EntityPlayer player) {
        if (TownHelper.getClosestTownToEntity(player).hasBuilding(HFBuildings.CARPENTER) && HFApi.quests.hasCompleted(Quests.JADE_MEET, player)) {
            if (quest_stage == START) return hasBuilding(player) ? getLocalized("description.talk") : getLocalized("description.build");
            else if (quest_stage == ACTION1 || quest_stage == ACTION2) return getLocalized("description.brush");
            else if (quest_stage == MILKING) return getLocalized("description.milk");
        }

        return null;
    }

    @Override
    public ItemStack getCurrentIcon(World world, EntityPlayer player) {
        if (!hasBuilding(player)) return buildingStack;
        else if (quest_stage == START) return primary;
        else if (quest_stage == ACTION1 || quest_stage == ACTION2) return COW;
        else if (quest_stage == MILKING) return MILK_ITEM;
        else return super.getCurrentIcon(world, player);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, NPC npc) {
        if (isCompletedEarly()) {
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

        return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPC npc) {
        if (quest_stage == INFO || isCompletedEarly()) {
            increaseStage(player);
            rewardEntity(player, "harvestfestival.cow");
            rewardItem(player, new ItemStack(Items.LEAD));
            rewardItem(player, HFCrops.GRASS.getCropStack(16));
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
        if (isCompletedEarly()) {
            rewardEntity(player, "harvestfestival.cow");
            rewardItem(player, HFAnimals.TOOLS.getStackFromEnum(Tool.MILKER));
            rewardItem(player, new ItemStack(Items.LEAD));
            rewardItem(player, HFCrops.GRASS.getCropStack(16));
            rewardItem(player, HFAnimals.TOOLS.getStackFromEnum(BRUSH));
        }

        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.COW_CARE);
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.ANIMAL_HAPPINESS);
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.ANIMAL_STRESS);
        rewardItem(player, HFAnimals.ANIMAL_PRODUCT.getStackOfSize(Sizeable.MILK, Size.LARGE, 1));
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