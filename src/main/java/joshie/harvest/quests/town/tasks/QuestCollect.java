package joshie.harvest.quests.town.tasks;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.player.RelationshipType;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestDaily;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nullable;
import java.util.Random;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;

@HFQuest("collect.crops")
public class QuestCollect extends QuestDaily {
    private Random rand = new Random();

    public QuestCollect() {
        setNPCs(HFNPCs.CAFE_GRANNY);
        setTownQuest();
    }

    @Override
    public String getDescription(World world, @Nullable EntityPlayer player) {
        rand.setSeed(HFApi.calendar.getDate(world).hashCode());
        int amount = 1 + rand.nextInt(10);
        Crop crop = Crop.REGISTRY.getValues().get(rand.nextInt(Crop.REGISTRY.getValues().size()));
        if (player != null) return "Bring katlin " + amount + " " + crop.getLocalizedName(true);
        else {
            return "I'm looking for an ingredient for something I'd love to cook. Could you bring me \n" + amount + " " + crop.getLocalizedName(true) + " please.\n- Katlin\n\n- Free Meal\n- Katlin will be grateful";
        }
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        rand.setSeed(HFApi.calendar.getDate(player.worldObj).hashCode());
        int amount = 1 + rand.nextInt(10);
        Crop crop = Crop.REGISTRY.getValues().get(rand.nextInt(Crop.REGISTRY.getValues().size()));
        return "Bring me " + amount + " " + crop.getLocalizedName(true);
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc, boolean wasSneaking) {
        rand.setSeed(HFApi.calendar.getDate(player.worldObj).hashCode());
        int amount = 1 + rand.nextInt(10);
        Crop crop = Crop.REGISTRY.getValues().get(rand.nextInt(Crop.REGISTRY.getValues().size()));
        String name = "crop" + WordUtils.capitalizeFully(crop.getRegistryName().getResourcePath(), '_').replace("_", "");
        if (InventoryHelper.hasInInventory(player, ORE_DICTIONARY, name, amount)) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.player.getRelationsForPlayer(player).affectRelationship(RelationshipType.NPC, HFNPCs.CAFE_GRANNY.getUUID(), 500);
        ItemStack stack = HFCooking.MEAL.getCreativeStack(HFCooking.MEAL, Meal.values()[player.worldObj.rand.nextInt(Meal.values().length)]);
        if (player.worldObj.rand.nextInt(10) == 0) {
            stack.stackSize = 10;
        }

        rewardItem(player, stack);
    }
}
