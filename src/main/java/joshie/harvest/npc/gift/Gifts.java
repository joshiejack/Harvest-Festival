package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftHandler;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class Gifts implements IGiftHandler {
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (stack.getItem() == HFMining.MATERIALS && (HFMining.MATERIALS.getEnumFromStack(stack) == Material.ALEXANDRITE)) return Quality.AWESOME;
        else if (registry.isGiftType(stack, GEM, FLOWER, COOKING, SWEET)) return Quality.GOOD;
        else if (registry.isGiftType(stack, BUILDING, MONSTER, JUNK)) return Quality.BAD;
        else if (registry.isGiftType(stack, PLANT, MINERAL)) return Quality.DISLIKE;
        else if (registry.isGiftType(stack, MEAT, FISH, VEGETABLE, FRUIT, ANIMAL, KNOWLEDGE, MAGIC)) return Quality.DECENT;
        else return Quality.DECENT; //Defauly return
    }
}