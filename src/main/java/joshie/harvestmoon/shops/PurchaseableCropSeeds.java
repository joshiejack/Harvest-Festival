package joshie.harvestmoon.shops;

import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.player.PlayerDataServer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableCropSeeds implements IPurchaseable {
    private Crop crop;

    public PurchaseableCropSeeds(Crop crop) {
        this.crop = crop;
    }

    @Override
    public boolean canBuy(World world, PlayerDataServer player, CalendarDate calendar) {
        if (calendar.getSeason() != crop.getSeason()) return false;
        return calendar.getSeason() == crop.getSeason();
    }

    @Override
    public int getCost() {
        return crop.getSeedCost();
    }

    @Override
    public ItemStack getProduct() {
        return new ItemStack(HMItems.seeds, 1, crop.getCropMeta());
    }
}
