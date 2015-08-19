package joshie.harvest.shops.purchaseable;

import java.util.List;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableStone extends Purchaseable {    
    public PurchaseableStone(int cost, ItemStack stack) {
        super(cost, stack);
    }

    @Override
    public void addTooltip(List list) {
        for (ItemStack stack: stacks) {
        list.add(Text.WHITE + stack.getDisplayName());
		list.add(Translate.translate("tooltip.dirt"));
        }
    }
}
