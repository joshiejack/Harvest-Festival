package joshie.harvest.npcs.gift;

import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.AWESOME;
import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.GOOD;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsTiberius extends Gifts {
    public GiftsTiberius() {
        categoryRegistry.put(MAGIC, Quality.GOOD);
        categoryRegistry.put(KNOWLEDGE, Quality.GOOD);
        categoryRegistry.put(MONSTER, Quality.GOOD);
        categoryRegistry.put(SWEET, Quality.BAD);
        categoryRegistry.put(FLOWER, Quality.BAD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.DOUGHNUT), Quality.GOOD);
        stackRegistry.register(new ItemStack(Items.DYE, 1, 9), Quality.TERRIBLE);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.SAND_ROSE), Quality.TERRIBLE);
        stackRegistry.register(HFCore.FLOWERS.getStackFromEnum(FlowerType.PINKCAT), Quality.TERRIBLE);
        stackRegistry.register(new ItemStack(Blocks.RED_FLOWER, 1, 2), Quality.TERRIBLE);
        stackRegistry.register(new ItemStack(Blocks.DOUBLE_PLANT, 1, 1), Quality.TERRIBLE);
    }


    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() instanceof ItemPotion) {
            for (PotionEffect effect: PotionUtils.getEffectsFromStack(stack)) {
                if (effect.getPotion() == MobEffects.REGENERATION) return AWESOME;
            }

            return GOOD;
        }  else return super.getQuality(stack);
    }
}
