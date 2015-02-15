package joshie.harvestmoon.gui;

import joshie.harvestmoon.npc.EntityNPC;
import joshie.harvestmoon.npc.gift.Gifts.Quality;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GuiNPCGift extends GuiNPC {
    private ItemStack gift;
    private Quality value;

    public GuiNPCGift(EntityNPC npc, EntityPlayer player) {
        super(npc, player);
        gift = player.getCurrentEquippedItem();
        value = npc.getNPC().getGiftValue(gift);
    }

    @Override
    public String getScript() {
        return npc.getNPC().getThanks(value);
    }
}
