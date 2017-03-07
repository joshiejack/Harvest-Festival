package joshie.harvest.quests.town.tasks;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestDaily;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;

@HFQuest("collect.materials")
public class QuestCollectWoodStone extends QuestDaily {
    public QuestCollectWoodStone() {
        super(HFNPCs.CARPENTER);
    }

    private boolean wood;
    private int amount = 1;

    @Override
    public String getDescription(World world, @Nullable EntityPlayer player) {
        if (player != null) return getLocalized("desc", amount, (wood ? getLocalized("wood") : getLocalized("stone")));
        else {
            rand.setSeed(HFApi.calendar.getDate(world).hashCode());
            wood = rand.nextBoolean();
            amount = 8 * (1 + rand.nextInt(8));
            return getLocalized("task", amount, (wood ? getLocalized("wood") : getLocalized("stone")), (wood ? getLocalized("axe") : getLocalized("pick")), amount * 10);
        }
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPCEntity entity) {
        return super.isNPCUsed(player, entity) && InventoryHelper.getHandItemIsIn(player, ORE_DICTIONARY, (wood ? "logWood" : "stone"), amount) != null;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        return TextHelper.getRandomSpeech(entity.getNPC(), "harvestfestival.quest.collect.materials.complete", 32);
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean wasSneaking) {
        if (InventoryHelper.takeItemsIfHeld(player, ORE_DICTIONARY, (wood ? "logWood" : "stone"), amount) != null) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.player.getRelationsForPlayer(player).affectRelationship(HFNPCs.CARPENTER, 500);
        rewardGold(player, 10 * amount);
        rewardItem(player, (wood ? HFTools.AXE.getStack(ToolTier.COPPER) : HFTools.HAMMER.getStack(ToolTier.COPPER)));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        amount = nbt.getByte("Amount");
        wood = nbt.getBoolean("Wood");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("Amount", (byte) amount);
        nbt.setBoolean("Wood", wood);
        return super.writeToNBT(nbt);
    }
}
