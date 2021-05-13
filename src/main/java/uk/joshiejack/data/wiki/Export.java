package uk.joshiejack.data.wiki;

import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.penguinlib.util.helpers.forge.OreDictionaryHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;

public class Export {
    public static String getEntryName(ItemStack stack) {
        String name = stack.getDisplayName();
        if (stack.getItem() == Items.CAKE || stack.getItem() == Items.MILK_BUCKET ||
                (stack.getItem() == Items.FISH && (stack.getItemDamage() == ItemFishFood.FishType.CLOWNFISH.ordinal() ||
                        stack.getItemDamage() == ItemFishFood.FishType.PUFFERFISH.ordinal() ||
                        stack.getItemDamage() == ItemFishFood.FishType.SALMON.ordinal()))) {
            name = stack.getDisplayName() + " (Minecraft)";
        }

        if (stack.getItem() == Item.getItemFromBlock(Blocks.STONE_BUTTON)) name = "Stone Button";
        else if (stack.getItem() == Item.getItemFromBlock(Blocks.WOODEN_BUTTON)) name = "Wood Button";
        else if (stack.getItem() == Item.getItemFromBlock(Blocks.GOLD_ORE)) name = "Gold Ore (Minecraft)";
        else if (stack.getItem() == Item.getItemFromBlock(Blocks.IRON_ORE)) name = "Iron Ore (Minecraft)";
        else if (stack.getItem() == AdventureItems.BUILDING) name = name.replace("[SPAWN] ", "");
        else if (name.equals("Oak Wood")) name = "Wood";
        else if (name.contains("Water Bottle")) name = name.replace("Water Bottle", "Potion");
        else if (name.equals("Black Banner")) name = "Banner";
        else if (stack.getItem() == Items.SKULL) name = "Mob Heads";
        else if (OreDictionaryHelper.getOreNames(stack).contains("treeSapling")) name = "Saplings";
        return name;
    }
}
