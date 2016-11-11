package joshie.harvest.shops.purchasable;

import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.DARK_RED;
import static net.minecraft.util.text.TextFormatting.GREEN;

public class PurchasableBlueFeather extends Purchasable {
    public PurchasableBlueFeather(int cost, ItemStack stack) {
        super(cost, stack);
    }

    @Override
    public int getStock() {
        return 1;
    }
    
    @Override
    public boolean canBuy(World world, EntityPlayer player, int amount) {
        return amount == 1 && HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().isEllegibleToMarry();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        list.add(/*Text.WHITE + */stack.getDisplayName());
        list.add(TextHelper.translate("marriage"));
        if (!HFTrackers.getClientPlayerTracker().getRelationships().isEllegibleToMarry()) {
        list.add(DARK_RED + TextHelper.translate("marriage.locked"));
        } else
        list.add(GREEN + TextHelper.translate("marriage.unlocked"));
    }
}
