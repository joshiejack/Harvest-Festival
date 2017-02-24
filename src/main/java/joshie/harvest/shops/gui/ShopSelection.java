package joshie.harvest.shops.gui;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.core.handlers.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class ShopSelection extends Selection {
    private final Shop shop;

    public ShopSelection(Shop shop) {
        this.shop = shop;
        if (shop.canBuyFromShop(null) && shop.canSellToShop(null)) {
            setLines("harvestfestival.shop.general.options", "harvestfestival.shop.general.options.shop", "harvestfestival.shop.general.options.sell", "harvestfestival.shop.general.options.chat");
        } else if (shop.canBuyFromShop(null)) setLines("harvestfestival.shop.general.options", "harvestfestival.shop.general.options.shop", "harvestfestival.shop.general.options.chat");
        else if (shop.canSellToShop(null)) setLines("harvestfestival.shop.general.options", "harvestfestival.shop.general.options.sell", "harvestfestival.shop.general.options.chat");
    }

    /** Called when the option is selected **/
    @Override
    public Result onSelected(EntityPlayer player, NPCEntity entity, Quest quest, int option) {
        //If we are able to buy from this shop
        if(shop.canBuyFromShop(player) && option == 1) {
            player.openGui(HarvestFestival.instance, GuiHandler.SHOP_MENU, player.worldObj, entity.getAsEntity().getEntityId(), 0, 0);
            return Result.DEFAULT;
        } else if ((!shop.canBuyFromShop(player) && shop.canSellToShop(player) && option == 1) || (shop.canBuyFromShop(player) && shop.canSellToShop(player) && option == 2)) {
            player.openGui(HarvestFestival.instance, GuiHandler.SHOP_MENU_SELL, player.worldObj, entity.getAsEntity().getEntityId(), 0, 0);
            return Result.DEFAULT;
        } else return Result.ALLOW;
    }
}
