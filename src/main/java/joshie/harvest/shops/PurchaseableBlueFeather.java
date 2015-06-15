package joshie.harvest.shops;

import java.util.List;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableBlueFeather extends Purchaseable {    
    public PurchaseableBlueFeather(int cost, ItemStack stack) {
        super(cost, stack);
    }
    
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return HarvestFestival.proxy.getPlayerTracker(player).getRelationships().isEllegibleToMarry();
    }

    @Override
    public ItemStack getDisplayStack() {
        return stacks[0];
    }

    @Override
    public void addTooltip(List list) {
        list.add(Text.WHITE + stacks[0].getDisplayName());
        list.add(Translate.translate("marriage"));
    }
}
