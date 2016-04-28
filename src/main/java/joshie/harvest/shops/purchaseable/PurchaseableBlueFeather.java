package joshie.harvest.shops.purchaseable;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class PurchaseableBlueFeather extends Purchaseable {    
    public PurchaseableBlueFeather(int cost, ItemStack stack) {
        super(cost, stack);
    }
    
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return HFTrackers.getPlayerTracker(player).getRelationships().isEllegibleToMarry();
    }

    @Override
    public ItemStack getDisplayStack() {
        return stacks[0];
    }

    @Override
    public void addTooltip(List<String> list) {
        list.add(/*Text.WHITE + */stacks[0].getDisplayName());
        list.add(Translate.translate("marriage"));
        if (!HFTrackers.getPlayerTracker(MCClientHelper.getPlayer()).getRelationships().isEllegibleToMarry()) {
        list.add(Text.DARK_RED + Translate.translate("marriage.locked"));
        } else
        list.add(Text.LIME + Translate.translate("marriage.unlocked"));
    }
}
