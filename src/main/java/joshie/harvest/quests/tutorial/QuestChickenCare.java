package joshie.harvest.quests.tutorial;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.SizeableHelper;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.core.lib.SizeableMeta;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.quests.Quest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

import static joshie.harvest.core.helpers.QuestHelper.completeQuest;
public class QuestChickenCare extends Quest {
    private boolean hasThrown;
    private boolean hasFed;

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

                        HFTrackers.markDirty();
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

                    HFTrackers.markDirty();
                }
            }
        }
    }

    @Override
    public boolean canStart(EntityPlayer player, HashSet<IQuest> active, HashSet<IQuest> finished) {
        if (!super.canStart(player, active, finished)) return false;
        else {
            return finished.contains(HFApi.quests.get("tutorial.cow")); //Quest is unlocked when you have chickens
        }
    }

    @Override
    public INPC[] getNPCs() {
        return new INPC[]{HFNPCs.ANIMAL_OWNER};
    }

    @Override
    public String getScript(EntityPlayer player, EntityNPC npc) {
        if (quest_stage == 0) {
            increaseStage(player);
            return getLocalized("start"); //Jeremy tells you all about how to care for chickens
        } else if (quest_stage == 1) {
            return getLocalized("care"); //Reminds you to go pick up and throw a chicken, as well as feed one seeds
        } else if (quest_stage == 2) {
            completeQuest(player, this);
            return getLocalized("finish");
        }

        return null;
    }

    @Override
    public void claim(EntityPlayer player) {
        ItemHelper.spawnByEntity(player, SizeableHelper.getSizeable(SizeableMeta.EGG, 1, Size.LARGE));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        hasThrown = nbt.getBoolean("HasThrown");
        hasFed = nbt.getBoolean("HasFed");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("HasThrown", hasThrown);
        nbt.setBoolean("HasFed", hasFed);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(hasThrown);
        buf.writeBoolean(hasFed);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        hasThrown = buf.readBoolean();
        hasFed = buf.readBoolean();
    }
}