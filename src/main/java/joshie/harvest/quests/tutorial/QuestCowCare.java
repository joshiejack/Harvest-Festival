package joshie.harvest.quests.tutorial;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.api.HFQuest;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.SizeableHelper;
import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;

import static joshie.harvest.animals.item.ItemAnimalTool.Tool.BRUSH;
import static joshie.harvest.animals.item.ItemAnimalTool.Tool.MILKER;
import static joshie.harvest.npc.HFNPCs.ANIMAL_OWNER;
import static joshie.harvest.npc.HFNPCs.GODDESS;

@HFQuest(data = "tutorial.cow")
public class QuestCowCare extends Quest {
    private static final joshie.harvest.api.quests.Quest TUTORIAL_FARMING = QuestHelper.getQuest("tutorial.farming");
    private boolean hasCollected;
    private boolean hasFed;
    private boolean hasBrushed;
    private boolean hasMilked;

    public QuestCowCare() {
        setNPCs(ANIMAL_OWNER, GODDESS);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<joshie.harvest.api.quests.Quest> active, Set<joshie.harvest.api.quests.Quest> finished) {
        return finished.contains(TUTORIAL_FARMING);
    }

    @Override
    public void onEntityInteract(EntityPlayer player, Entity target) {
        if (quest_stage == 2) {
            if (target instanceof EntityHarvestCow) {
                EntityHarvestCow cow = (EntityHarvestCow) target;
                ItemStack held = player.getActiveItemStack();
                if (held != null) {
                    boolean hasChanged = false;
                    if (!hasFed && held.getItem() == Items.WHEAT) {
                        hasFed = true;
                        hasChanged = true;
                    } else if (!hasBrushed && ToolHelper.isBrush(held)) {
                        hasBrushed = true;
                        hasChanged = true;
                    } else if (!hasMilked && ToolHelper.isMilker(held)) {
                        if (cow.getData().canProduce()) {
                            hasMilked = true;
                            hasChanged = true;
                        }
                    }

                    //If something changed, let the server know
                    if (hasChanged) {
                        if (!player.worldObj.isRemote) {
                            //If all three are completed, increase the stage
                            if (hasFed && hasBrushed && hasMilked) {
                                increaseStage(player);
                            }

                            HFTrackers.markDirty(target.getEntityWorld());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClosedChat(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (!hasCollected && quest_stage == 2 && npc == ANIMAL_OWNER) {
            hasCollected = true;
            ItemHelper.addToPlayerInventory(player, HFAnimals.TOOLS.getStackFromEnum(MILKER));
            ItemHelper.addToPlayerInventory(player, HFAnimals.TOOLS.getStackFromEnum(BRUSH));
        }
    }


    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == 0) {
            if (npc == GODDESS) {
                increaseStage(player);
                return "start"; //Goddess tells you to go and talk to jeremy
            }
        } else if (quest_stage == 1) {
            if (npc == GODDESS) {
                return "go"; //Goddess reminds you you should be talking to jeremy
            } else {
                increaseStage(player);
                return "care"; //Jeremy tells you how caring for cows works
            }
        } else if (quest_stage == 2) {
            if (npc == ANIMAL_OWNER) {
                return "reminder";
            }
        } else if (quest_stage == 3) { //Jeremy expects you to have brushed 1 cow, fed 1 cow and to have milked 1 cow
            if (npc == ANIMAL_OWNER) {
                complete(player);
                return "finish";
            }
        }

        return null;
    }

    @Override
    public void claim(EntityPlayer player) {
        ItemHelper.spawnByEntity(player, SizeableHelper.getSizeable(HFAnimals.MILK, 1, Size.LARGE));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        hasCollected = nbt.getBoolean("HasCollected");
        hasFed = nbt.getBoolean("HasFed");
        hasBrushed = nbt.getBoolean("HasBrushed");
        hasMilked = nbt.getBoolean("HasMilked");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("HasCollected", hasCollected);
        nbt.setBoolean("HasFed", hasFed);
        nbt.setBoolean("HasBrushed", hasBrushed);
        nbt.setBoolean("HasMilked", hasMilked);
        return nbt;
    }
}