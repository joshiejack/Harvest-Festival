package joshie.harvest.shops.purchasable;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.api.trees.Tree;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.knowledge.HFNotes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class PurchasableCropSeeds implements IPurchasable {
    private final String resource;
    private final Crop crop;

    public PurchasableCropSeeds(Crop crop) {
        this.crop = crop;
        this.resource = crop.getResource().toString().replace(":", "_") + "Seeds";
    }

    private boolean isCorrectSeason(Season theSeason) {
        for (Season season : crop.getSeasons()) {
            if (season == theSeason) return true;
        }

        return false;
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        CalendarDate date = HFApi.calendar.getDate(world);
        return isCorrectSeason(date.getSeason()) && crop.getRules().canDo(world, player, amount);
    }

    @Override
    public long getCost() {
        return crop.getSeedCost();
    }

    @Override
    @Nonnull
    public ItemStack getDisplayStack() {
        return crop.getSeedStack(1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        list.addAll(getDisplayStack().getTooltip(MCClientHelper.getPlayer(), false));
        list.remove(list.size() - 1); //Remove info about days
        list.add("--------------------");
        int amount = crop instanceof Tree ? ((Tree)crop).getStagesToMaturity() : crop.getStages();
        list.add(TextHelper.formatHF("crop.seeds.shop.days", amount));
        if (crop.getRegrowStage() > 0) list.add(TextHelper.formatHF("crop.seeds.shop.regrow", (crop.getStages() - crop.getRegrowStage())));
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        ItemStack seeds = crop.getSeedStack(1);
        SpawnItemHelper.addToPlayerInventory(player, seeds.copy());

        if (crop instanceof Tree) {
            HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.TREES);
        }
    }

    @Override
    public String getPurchaseableID() {
        return resource;
    }
}
