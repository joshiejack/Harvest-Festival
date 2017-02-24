package joshie.harvest.quests.town.tasks;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestDaily;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nullable;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;

@HFQuest("collect.crops")
public class QuestCollect extends QuestDaily {
    public QuestCollect() {
        setNPCs(HFNPCs.CAFE_GRANNY);
        setTownQuest();
    }

    private Crop crop = HFCrops.TURNIP;
    private int amount = 1;

    @Override
    public String getDescription(World world, @Nullable EntityPlayer player) {
        if (player != null) return getLocalized("desc", amount, crop.getLocalizedName(true));
        else {
            rand.setSeed(HFApi.calendar.getDate(world).hashCode());
            amount = 1 + rand.nextInt(10);
            crop = getValidCrop(Crop.REGISTRY.getValues().get(rand.nextInt(Crop.REGISTRY.getValues().size())));
            return getLocalized("task", amount, crop.getLocalizedName(true));
        }
    }

    private Crop getValidCrop(Crop crop) {
        return crop == HFCrops.GRASS || crop == HFCrops.TUTORIAL || crop.getIngredient() == null ? HFCrops.TURNIP : crop;
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPCEntity entity) {
        if (!super.isNPCUsed(player, entity)) return false;
        String name = "crop" + WordUtils.capitalizeFully(crop.getRegistryName().getResourcePath(), '_').replace("_", "");
        return InventoryHelper.getHandItemIsIn(player, ORE_DICTIONARY, name, amount) != null;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        return TextHelper.getRandomSpeech(entity.getNPC(), "harvestfestival.quest.collect.crops.complete", 32);
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean wasSneaking) {
        String name = "crop" + WordUtils.capitalizeFully(crop.getRegistryName().getResourcePath(), '_').replace("_", "");
        if (InventoryHelper.takeItemsIfHeld(player, ORE_DICTIONARY, name, amount) != null) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.player.getRelationsForPlayer(player).affectRelationship(HFNPCs.CAFE_GRANNY, 500);
        ItemStack stack = HFCooking.MEAL.getCreativeStack(Meal.values()[player.worldObj.rand.nextInt(Meal.values().length)]);
        if (player.worldObj.rand.nextInt(10) == 0) {
            stack.stackSize = 10;
        }

        rewardItem(player, stack);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        amount = nbt.getByte("TargetAmount");
        crop = Crop.REGISTRY.getValue(new ResourceLocation(nbt.getString("TargetCrop")));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("TargetAmount", (byte) amount);
        nbt.setString("TargetCrop", crop.getRegistryName().toString());
        return super.writeToNBT(nbt);
    }
}
