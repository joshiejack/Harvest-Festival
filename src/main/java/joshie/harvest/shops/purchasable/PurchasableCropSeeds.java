package joshie.harvest.shops.purchasable;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class PurchasableCropSeeds implements IPurchasable {
    private final String resource;
    private final Crop crop;

    public PurchasableCropSeeds(Crop crop) {
        this.crop = crop;
        this.resource = crop.getRegistryName().toString().replace(":", "_") + "Seeds";
    }

    private boolean isCorrectSeason(Season theSeason) {
        for (Season season : crop.getSeasons()) {
            if (season == theSeason) return true;
        }

        return false;
    }

    @Override
    public boolean canDo(World world, EntityPlayer player, int amount) {
        CalendarDate date = HFApi.calendar.getDate(world);
        if (!isCorrectSeason(date.getSeason())) return false;
        if (!crop.canPurchase()) return false;
        if (crop.getPurchaseYear() > 0 && !CalendarHelper.haveYearsPassed(world, player, crop.getPurchaseYear())) return false;
        return crop.getRules().canDo(world, player, amount);
    }

    @Override
    public long getCost() {
        return crop.getSeedCost();
    }

    @Override
    public ItemStack getDisplayStack() {
        return crop.getSeedStack(1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        list.addAll(getDisplayStack().getTooltip(MCClientHelper.getPlayer(), false));
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        ItemStack seeds = crop.getSeedStack(1);
        SpawnItemHelper.addToPlayerInventory(player, seeds.copy());
    }

    @Override
    public String getPurchaseableID() {
        return resource;
    }
}
