package joshie.harvest.quests.town.tasks;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.town.Town;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestDaily;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;

@HFQuest("collect.materials")
public class QuestCollectWoodStone extends QuestDaily {
    private boolean silver;
    private boolean wood;
    private int amount = 1;

    public QuestCollectWoodStone() {
        super(HFNPCs.CARPENTER);
    }

    @Override
    public String getDescription(World world, @Nullable EntityPlayer player) {
        if (player != null) return getLocalized("desc", amount, (wood ? getLocalized("wood") : getLocalized("stone")));
        else return getLocalized("task", amount, (wood ? getLocalized("wood") : getLocalized("stone")),
                (wood ? (silver ? getLocalized("axe.silver") : getLocalized("axe")) :
                        (silver ? getLocalized("pick.silver") : getLocalized("pick"))), (wood ? amount * 10 : amount * 20));
    }

    @Override
    public void onSelectedAsDailyQuest(Town town, World world, BlockPos pos) {
        rand.setSeed(HFApi.calendar.getDate(world).hashCode());
        wood = rand.nextBoolean();
        amount = (8 * (1 + rand.nextInt(16))) + rand.nextInt(16);
        if (amount >= 128 && rand.nextInt(30) == 0) {
            silver = true;
            amount *= 4.5;
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
        HFApi.player.getRelationsForPlayer(player).affectRelationship(HFNPCs.CARPENTER, 2500);
        rewardGold(player, (wood ? amount * 10 : amount * 20));
        rewardItem(player, (wood ? HFTools.AXE.getStack((silver ? ToolTier.SILVER : ToolTier.COPPER)) : HFTools.HAMMER.getStack((silver ? ToolTier.SILVER : ToolTier.COPPER))));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        amount = nbt.getShort("Amount");
        wood = nbt.getBoolean("Wood");
        silver = nbt.getBoolean("Silver");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setShort("Amount", (short) amount);
        nbt.setBoolean("Wood", wood);
        nbt.setBoolean("Silver", silver);
        return super.writeToNBT(nbt);
    }
}
