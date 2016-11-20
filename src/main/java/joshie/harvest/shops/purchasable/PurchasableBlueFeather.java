package joshie.harvest.shops.purchasable;
//TODO: Reenable in 1.0 when I readd marriage
/*
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
        list.add(stack.getDisplayName());
        list.add(TextHelper.translate("marriage"));
        if (!HFTrackers.getClientPlayerTracker().getRelationships().isEllegibleToMarry()) {
        list.add(DARK_RED + TextHelper.translate("marriage.locked"));
        } else
        list.add(GREEN + TextHelper.translate("marriage.unlocked"));
    }
} */
