package joshie.harvestmoon.gui;

import joshie.harvestmoon.helpers.GiftingHelper;
import joshie.harvestmoon.npc.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GuiNPCGift extends GuiNPC {
    private ItemStack gift;
    private int value;

    public GuiNPCGift(EntityNPC npc, EntityPlayer player) {
        super(npc, player);
        gift = player.getCurrentEquippedItem();
        value = GiftingHelper.getValue(npc.getNPC(), player, gift);
    }

    @Override
    public String getScript() {
        return npc.getNPC().getThanks(value);
    }
}
