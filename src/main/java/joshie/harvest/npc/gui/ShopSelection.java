package joshie.harvest.npc.gui;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Quest.Selection;
import joshie.harvest.core.handlers.GuiHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class ShopSelection extends Selection {
    public ShopSelection() {
        super("harvestfestival.shop.general.options", "harvestfestival.shop.general.options.shop", "harvestfestival.shop.general.options.chat");
    }

    /** Called when the option is selected **/
    @Override
    public Result onSelected(EntityPlayer player, EntityLiving entity, INPC npc, Quest quest, int option) {
        if (option == 1) {
            if (npc.isBuilder()) player.openGui(HarvestFestival.instance, GuiHandler.SHOP_BUILDER, player.worldObj, entity.getEntityId(), 0, 0);
            else player.openGui(HarvestFestival.instance, GuiHandler.SHOP_MENU, player.worldObj, entity.getEntityId(), 0, 0);
            return Result.DEFAULT;
        }

        return Result.ALLOW;
    }
}
