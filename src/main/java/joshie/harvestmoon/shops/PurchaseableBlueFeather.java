package joshie.harvestmoon.shops;

import java.util.List;

import joshie.harvestmoon.core.helpers.PlayerHelper;
import joshie.harvestmoon.core.util.Translate;
import joshie.harvestmoon.core.util.generic.Text;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableBlueFeather extends Purchaseable {    
    public PurchaseableBlueFeather(int cost, ItemStack stack) {
        super(cost, stack);
    }
    
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return PlayerHelper.isElligibleToMarry(player);
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
