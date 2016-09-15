package joshie.harvest.shops.purchaseable;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.WHITE;

public class PurchaseableCropSeeds implements IPurchaseable {
    private final String resource;
    private final Crop crop;

    public PurchaseableCropSeeds(Crop crop) {
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
    public boolean canBuy(World world, EntityPlayer player) {
        CalendarDate date = HFApi.calendar.getDate(world);
        if (!isCorrectSeason(date.getSeason())) return false;
        if (!crop.canPurchase()) return false;
        return CalendarHelper.haveYearsPassed(world, player, crop.getPurchaseYear());
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return canBuy(world, player);
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
        list.add(WHITE + crop.getSeedsName());
        for (Season season : crop.getSeasons()) {
            list.add(season.getDisplayName());
        }
    }

    @Override
    public boolean onPurchased(EntityPlayer player) {
        SpawnItemHelper.addToPlayerInventory(player, crop.getSeedStack(1).copy());
        return false;
    }

    @Override
    public String getPurchaseableID() {
        return resource;
    }
}
