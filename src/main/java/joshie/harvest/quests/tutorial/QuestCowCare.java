package joshie.harvest.quests.tutorial;

import io.netty.buffer.ByteBuf;
import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.SizeableHelper;
import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.quests.Quest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashSet;

import static joshie.harvest.animals.item.ItemAnimalTool.Tool.BRUSH;
import static joshie.harvest.animals.item.ItemAnimalTool.Tool.MILKER;
import static joshie.harvest.quests.QuestHelper.completeQuest;

public class QuestCowCare extends Quest {
    private boolean hasCollected;
    private boolean hasFed;
    private boolean hasBrushed;
    private boolean hasMilked;

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
    public void onClosedChat(EntityPlayer player, EntityNPC npc) {
        if (!hasCollected && quest_stage == 2 && npc.getNPC() == HFNPCs.ANIMAL_OWNER) {
            hasCollected = true;
            ItemHelper.addToPlayerInventory(player, HFAnimals.TOOLS.getStackFromEnum(MILKER));
            ItemHelper.addToPlayerInventory(player, HFAnimals.TOOLS.getStackFromEnum(BRUSH));
        }
    }

    @Override
    public boolean canStart(EntityPlayer player, HashSet<IQuest> active, HashSet<IQuest> finished) {
        if (!super.canStart(player, active, finished)) return false;
        else {
            return finished.contains(HFApi.quests.get("tutorial.tomatoes")); //This quest is unlocked when we have completed tomato quest
        }
    }

    @Override
    public INPC[] getNPCs() {
        return new INPC[]{HFNPCs.ANIMAL_OWNER, HFNPCs.GODDESS};
    }

    @Override
    public String getScript(EntityPlayer player, EntityNPC npc) {
        if (quest_stage == 0) {
            if (npc.getNPC() == HFNPCs.GODDESS) {
                increaseStage(player);
                return getLocalized("start"); //Goddess tells you to go and talk to jeremy
            }
        } else if (quest_stage == 1) {
            if (npc.getNPC() == HFNPCs.GODDESS) {
                return getLocalized("go"); //Goddess reminds you you should be talking to jeremy
            } else {
                increaseStage(player);
                return getLocalized("care"); //Jeremy tells you how caring for cows works
            }
        } else if (quest_stage == 2) {
            if (npc.getNPC() == HFNPCs.ANIMAL_OWNER) {
                return getLocalized("reminder");
            }
        } else if (quest_stage == 3) { //Jeremy expects you to have brushed 1 cow, fed 1 cow and to have milked 1 cow
            if (npc.getNPC() == HFNPCs.ANIMAL_OWNER) {
                completeQuest(player, this);
                return getLocalized("finish");
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
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("HasCollected", hasCollected);
        nbt.setBoolean("HasFed", hasFed);
        nbt.setBoolean("HasBrushed", hasBrushed);
        nbt.setBoolean("HasMilked", hasMilked);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(hasCollected);
        buf.writeBoolean(hasFed);
        buf.writeBoolean(hasBrushed);
        buf.writeBoolean(hasMilked);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        hasCollected = buf.readBoolean();
        hasFed = buf.readBoolean();
        hasBrushed = buf.readBoolean();
        hasMilked = buf.readBoolean();
    }
}