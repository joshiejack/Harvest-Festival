package joshie.harvestmoon.quests;

import static joshie.harvestmoon.HarvestMoon.handler;
import static joshie.harvestmoon.helpers.QuestHelper.completeQuest;
import io.netty.buffer.ByteBuf;

import java.util.HashSet;

import joshie.harvestmoon.entities.EntityNPC;
import joshie.harvestmoon.entities.NPC;
import joshie.harvestmoon.helpers.SizeableHelper;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.init.HMQuests;
import joshie.harvestmoon.lib.SizeableMeta;
import joshie.harvestmoon.lib.SizeableMeta.Size;
import joshie.lib.helpers.ItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class QuestChickenCare extends Quest {
    private boolean hasThrown;
    private boolean hasFed;

    @Override
    public void onEntityInteract(EntityPlayer player, Entity target) {
        if (quest_stage == 1) {
            if (target instanceof EntityChicken) {
                ItemStack held = player.getCurrentEquippedItem();
                if (held != null) {
                    boolean hasChanged = false;
                    if (!hasFed && held.getItem() == Items.wheat_seeds) {
                        hasFed = true;
                        hasChanged = true;
                    }

                    if (!player.worldObj.isRemote) {
                        if (hasThrown) {
                            increaseStage(player);
                        }

                        handler.getServer().markDirty();
                    }
                }
            }
        }
    }

    @Override
    public void onRightClickBlock(EntityPlayer player, World world, int x, int y, int z, int side) {
        if (!hasThrown) {
            if (player.riddenByEntity instanceof EntityChicken) {
                hasThrown = true;

                if (!player.worldObj.isRemote) {
                    if (hasFed) {
                        increaseStage(player);
                    }

                    handler.getServer().markDirty();
                }
            }
        }
    }

    @Override
    public boolean canStart(EntityPlayer player, HashSet<Quest> active, HashSet<Quest> finished) {
        if (!super.canStart(player, active, finished)) return false;
        else {
            return finished.contains(HMQuests.get("tutorial.cow")); //Quest is unlocked when you have chickens
        }
    }

    @Override
    public NPC[] getNPCs() {
        return new NPC[] { HMNPCs.animal_owner };
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
    public void claim(EntityPlayerMP player) {
        ItemHelper.spawnByEntity(player, SizeableHelper.getSizeable(48000, SizeableMeta.EGG, Size.LARGE));
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
