package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.gathering.HFGathering;
import joshie.harvest.gathering.block.BlockNature.NaturalBlock;
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
        stackRegistry.register(Items.CLOCK, Quality.AWESOME);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.DOUGHNUT), Quality.AWESOME);
        stackRegistry.register(Ore.of("dustRedstone"), Quality.GOOD);
        stackRegistry.register(Ore.of("ingotGold"), Quality.GOOD);
        categoryRegistry.put(MAGIC, Quality.GOOD);
        categoryRegistry.put(KNOWLEDGE, Quality.GOOD);
        categoryRegistry.put(MONSTER, Quality.GOOD);
        stackRegistry.register(Items.RABBIT_FOOT, Quality.GOOD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.RICE_BAMBOO), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.SPINACH_BOILED), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.SOUP_HERB), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.SALAD_HERB), Quality.DISLIKE);
        categoryRegistry.put(FLOWER, Quality.BAD);
        stackRegistry.register(HFGathering.NATURE.getStackFromEnum(NaturalBlock.BAMBOO), Quality.BAD);
        stackRegistry.register(Ore.of("cropSpinach"), Quality.BAD);
        stackRegistry.register(Ore.of("cropCabbage"), Quality.BAD);
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
