package joshie.harvest.npcs.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.GOOD;

@SuppressWarnings("unused")
public class GiftsJamie extends Gifts {
    public GiftsJamie() {
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.ALEXANDRITE), Quality.AWESOME);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.PINK_DIAMOND), Quality.AWESOME);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.MYTHIC), Quality.GOOD);
        categoryRegistry.put(MONSTER, Quality.GOOD);
        categoryRegistry.put(FLOWER, Quality.DECENT);
        categoryRegistry.put(COOKING, Quality.DECENT);
        categoryRegistry.put(SWEET, Quality.DECENT);
    }


    @Override
    public Quality getQuality(ItemStack stack) {
        long sell = HFApi.shipping.getSellValue(stack);
        Quality quality = super.getQuality(stack);
        if (quality == Quality.GOOD || quality == Quality.AWESOME) {
            return quality;
        } else {
            if (sell <= 1) return Quality.TERRIBLE;
            if (sell <= 80) return Quality.BAD;
            if (sell <= 150) return Quality.DISLIKE;
            if (sell <= 300) return Quality.DECENT;
            return sell >= 1000 ? Quality.AWESOME : GOOD;
        }
    }
}