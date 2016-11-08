package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsBrandon extends Gifts {
    private boolean isAboveGold(Material material) {
        return material == Material.MYSTRIL || material == Material.MYTHIC;
    }

    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (stack.getItem() == HFMining.MATERIALS && isAboveGold(HFMining.MATERIALS.getEnumFromStack(stack))) return Quality.AWESOME;
        else if (registry.isGiftType(stack, MINERAL)) return Quality.GOOD;
        else if (InventoryHelper.isOreName(stack, "treeSapling")) return Quality.TERRIBLE;
        else if (InventoryHelper.startsWith(stack, "flower")) return Quality.BAD;
        else if (registry.isGiftType(stack, PLANT, FLOWER)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}