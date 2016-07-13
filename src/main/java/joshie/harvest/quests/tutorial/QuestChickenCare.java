package joshie.harvest.quests.tutorial;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFRegister;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.SizeableHelper;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

import static joshie.harvest.npc.HFNPCs.ANIMAL_OWNER;

@HFRegister(path = "tutorial.chicken")
public class QuestChickenCare extends Quest {
    private static final Quest TUTORIAL_COW = QuestHelper.getQuest("tutorial.cow");
    private boolean hasThrown;
    private boolean hasFed;

    public QuestChickenCare() {
        setNPCs(ANIMAL_OWNER);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TUTORIAL_COW); //Quest is unlocked when you have chickens
    }

    @Override
    public void onEntityInteract(EntityPlayer player, Entity target) {
        if (quest_stage == 1) {
            if (target instanceof EntityChicken) {
                ItemStack held = player.getActiveItemStack();
                if (held != null) {
                    if (!hasFed && held.getItem() == Items.WHEAT_SEEDS) {
                        hasFed = true;
                    }

                    if (!player.worldObj.isRemote) {
                        if (hasThrown) {
                            increaseStage(player);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRightClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {
        if (!hasThrown) {
            if (player.getRidingEntity() instanceof EntityChicken) {
                hasThrown = true;

                if (!player.worldObj.isRemote) {
                    if (hasFed) {
                        increaseStage(player);
                    }
                }
            }
        }
    }

    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == 0) {
            increaseStage(player);
            return "start"; //Jeremy tells you all about how to care for chickens
        } else if (quest_stage == 1) {
            return "care"; //Reminds you to go pick up and throw a chicken, as well as feed one seeds
        } else if (quest_stage == 2) {
            complete(player);
            return "finish";
        }

        return null;
    }

    @Override
    public void claim(EntityPlayer player) {
        ItemHelper.spawnByEntity(player, SizeableHelper.getSizeable(HFAnimals.EGG, 1, Size.LARGE));
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