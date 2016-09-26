package joshie.harvest.shops.purchasable;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.Text;
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
    public boolean canBuy(World world, EntityPlayer player) {
        return HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().isEllegibleToMarry();
    }

    @Override
    public ItemStack getDisplayStack() {
        return stacks[0];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        list.add(/*Text.WHITE + */stacks[0].getDisplayName());
        list.add(Text.translate("marriage"));
        if (!HFTrackers.getClientPlayerTracker().getRelationships().isEllegibleToMarry()) {
        list.add(DARK_RED + Text.translate("marriage.locked"));
        } else
        list.add(GREEN + Text.translate("marriage.unlocked"));
    }
}
