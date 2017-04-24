package joshie.harvest.quests.town.tasks;

import com.google.common.collect.Lists;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.town.Town;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemFish.Fish;
import joshie.harvest.fishing.loot.SetWeight;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestDaily;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;
import static joshie.harvest.fishing.item.ItemFish.FISH_LOCATIONS;

@HFQuest("collect.fish")
public class QuestCollectFish extends QuestDaily {
    private static final ItemStack rod = HFFishing.FISHING_ROD.getStack(ToolTier.BASIC);
    private ItemStack fish = HFFishing.FISH.getStackFromEnum(Fish.COD);
    private long reward = 1L;

    public QuestCollectFish() {
        super(HFNPCs.FISHERMAN);
    }

    @Override
    public String getDescription(World world, @Nullable EntityPlayer player) {
        if (player != null) return getLocalized("desc", fish.getCount(), fish.getDisplayName());
        else return getLocalized("task", fish.getCount(), fish.getDisplayName(), reward);
    }

    @Override
    public void onSelectedAsDailyQuest(Town town, World world, BlockPos pos) {
        rand.setSeed(HFApi.calendar.getDate(world).hashCode());
        int amount = 1 + rand.nextInt(3);
        List<Fish> list = Lists.newArrayList(FISH_LOCATIONS.get(HFApi.calendar.getDate(world).getSeason()));
        Fish fishy = list.get(rand.nextInt(list.size()));
        fish = SetWeight.applyFishSizeData(rand, rod, HFFishing.FISH.getStackFromEnum(fishy, amount));
        reward = HFApi.shipping.getSellValue(fish) * 10;
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPCEntity entity) {
        return super.isNPCUsed(player, entity) && InventoryHelper.getHandItemIsIn(player, ITEM_STACK, fish, fish.getCount()) != null;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        return TextHelper.getRandomSpeech(entity.getNPC(), "harvestfestival.quest.collect.fish.complete", 32);
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean wasSneaking) {
        if (InventoryHelper.takeItemsIfHeld(player, ITEM_STACK, fish, fish.getCount()) != null) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.player.getRelationsForPlayer(player).affectRelationship(HFNPCs.FISHERMAN, 2500);
        rewardGold(player, reward);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Stack")) fish = new ItemStack(nbt.getCompoundTag("Stack"));
        reward = nbt.getLong("Reward");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Stack", fish.writeToNBT(new NBTTagCompound()));
        nbt.setLong("Reward", reward);
        return super.writeToNBT(nbt);
    }
}
