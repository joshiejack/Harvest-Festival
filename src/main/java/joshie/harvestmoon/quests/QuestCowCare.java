package joshie.harvestmoon.quests;

import static joshie.harvestmoon.core.helpers.AnimalHelper.canProduceProduct;
import static joshie.harvestmoon.core.helpers.QuestHelper.completeQuest;
import static joshie.harvestmoon.core.helpers.ServerHelper.markDirty;
import io.netty.buffer.ByteBuf;

import java.util.HashSet;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.core.ISizeable.Size;
import joshie.harvestmoon.api.npc.INPC;
import joshie.harvestmoon.api.quest.IQuest;
import joshie.harvestmoon.core.helpers.SizeableHelper;
import joshie.harvestmoon.core.helpers.ToolHelper;
import joshie.harvestmoon.core.helpers.generic.ItemHelper;
import joshie.harvestmoon.core.lib.SizeableMeta;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.items.ItemGeneral;
import joshie.harvestmoon.npc.EntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class QuestCowCare extends Quest {
    private boolean hasCollected;
    private boolean hasFed;
    private boolean hasBrushed;
    private boolean hasMilked;

    @Override
    public void onEntityInteract(EntityPlayer player, Entity target) {
        if (quest_stage == 2) {
            if (target instanceof EntityCow) {
                ItemStack held = player.getCurrentEquippedItem();
                if (held != null) {
                    boolean hasChanged = false;
                    if (!hasFed && held.getItem() == Items.wheat) {
                        hasFed = true;
                        hasChanged = true;
                    } else if (!hasBrushed && ToolHelper.isBrush(held)) {
                        hasBrushed = true;
                        hasChanged = true;
                    } else if (!hasMilked && ToolHelper.isMilker(held)) {
                        if (canProduceProduct((EntityCow) target)) {
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

                            markDirty();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClosedChat(EntityPlayer player, EntityNPC npc) {
        if (!hasCollected && quest_stage == 2 && npc.getNPC() == HMNPCs.animal_owner) {
            hasCollected = true;
            ItemHelper.addToPlayerInventory(player, new ItemStack(HMItems.general, 1, ItemGeneral.MILKER));
            ItemHelper.addToPlayerInventory(player, new ItemStack(HMItems.general, 1, ItemGeneral.BRUSH));
        }
    }

    @Override
    public boolean canStart(EntityPlayer player, HashSet<IQuest> active, HashSet<IQuest> finished) {
        if (!super.canStart(player, active, finished)) return false;
        else {
            return finished.contains(HMApi.QUESTS.get("tutorial.tomatoes")); //This quest is unlocked when we have completed tomato quest
        }
    }

    @Override
    public INPC[] getNPCs() {
        return new INPC[] { HMNPCs.animal_owner, HMNPCs.goddess };
    }

    @Override
    public String getScript(EntityPlayer player, EntityNPC npc) {
        if (quest_stage == 0) {
            if (npc.getNPC() == HMNPCs.goddess) {
                increaseStage(player);
                return getLocalized("start"); //Goddess tells you to go and talk to jeremy
            }
        } else if (quest_stage == 1) {
            if (npc.getNPC() == HMNPCs.goddess) {
                return getLocalized("go"); //Goddess reminds you you should be talking to jeremy
            } else {
                increaseStage(player);
                return getLocalized("care"); //Jeremy tells you how caring for cows works
            }
        } else if (quest_stage == 2) {
            if (npc.getNPC() == HMNPCs.animal_owner) {
                return getLocalized("reminder");
            }
        } else if (quest_stage == 3) { //Jeremy expects you to have brushed 1 cow, fed 1 cow and to have milked 1 cow
            if (npc.getNPC() == HMNPCs.animal_owner) {
                completeQuest(player, this);
                return getLocalized("finish");
            }
        }

        return null;
    }

    @Override
    public void claim(EntityPlayerMP player) {
        ItemHelper.spawnByEntity(player, SizeableHelper.getSizeable(48000, SizeableMeta.MILK, Size.LARGE));
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
