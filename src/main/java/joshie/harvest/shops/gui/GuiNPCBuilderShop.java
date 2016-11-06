package joshie.harvest.shops.gui;

import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.api.shops.IPurchasableBuilder;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.shops.gui.button.ButtonListingBuilding;
import joshie.harvest.shops.gui.button.ButtonListingItem;
import net.minecraft.entity.player.EntityPlayer;

public class GuiNPCBuilderShop extends GuiNPCShop<IPurchasableBuilder> {
    public GuiNPCBuilderShop(EntityPlayer player, EntityNPC npc, boolean isSelling) {
        super(player, npc, -2, isSelling);
    }

    @Override
    public boolean isAllowedInShop(IPurchasable purchasable) {
        return purchasable instanceof IPurchasableBuilder;
    }

    @Override
    public int getMax() {
        return 10;
    }

    @Override
    public void reload() {
        contents.clear();
        for (IPurchasable purchasable: shop.getContents()) {
            if (isAllowedInShop(purchasable) && purchasable.canList(client.worldObj, client)) {
                contents.add(purchasable);
            }
        }

        contents.sort((s1, s2)-> {
                int one = isGoldOnly((IPurchasableBuilder)s1) || isWoodOnly((IPurchasableBuilder)s1) || isStoneOnly((IPurchasableBuilder)s1) ? 1 : 0;
                int two = isGoldOnly((IPurchasableBuilder)s2) || isWoodOnly((IPurchasableBuilder)s2) || isStoneOnly((IPurchasableBuilder)s2) ? 1 : 0;
                return one > two ? 1 : one == two ? 0:  -1;

        } );

        setStart(start);
    }

    private boolean isGoldOnly(IPurchasableBuilder builder) {
        return builder.getCost() != 0 && builder.getStoneCost() == 0 && builder.getLogCost() == 0;
    }

    private boolean isWoodOnly(IPurchasableBuilder builder) {
        return builder.getLogCost() != 0 && builder.getStoneCost() == 0 && builder.getCost() == 0;
    }

    private boolean isStoneOnly(IPurchasableBuilder builder) {
        return builder.getStoneCost() != 0 && builder.getLogCost() == 0 && builder.getCost() == 0;
    }

    @Override
    protected int addButton(IPurchasableBuilder purchasable, int id, int left, int top, int space) {
        if (isGoldOnly(purchasable)) return super.addButton(purchasable, id, left, top, space);
        else if (isWoodOnly(purchasable)) {
            if (space + 20 <= 200) buttonList.add(new ButtonListingItem(ButtonListingBuilding.log, purchasable.getLogCost(), this, purchasable, id, left, top));
            return 20;
        } else if (isStoneOnly(purchasable)) {
            if (space + 20 <= 200) buttonList.add(new ButtonListingItem(ButtonListingBuilding.stone, purchasable.getStoneCost(), this, purchasable, id, left, top));
            return 20;
        } else if (space + 36 <= 200) {
            buttonList.add(new ButtonListingBuilding(this, purchasable, id, left, top));
        }

        return 20;
    }
}