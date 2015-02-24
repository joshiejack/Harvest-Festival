package joshie.harvestmoon.core.gui;

import static joshie.harvestmoon.core.helpers.RelationsHelper.getRelationshipValue;
import joshie.harvestmoon.core.helpers.ToolHelper;
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
        if (ToolHelper.isBlueFeather(gift)) {
            int relationship = getRelationshipValue(npc, player);
            if (relationship >= joshie.harvestmoon.core.config.NPC.MARRIAGE_REQUIREMENT && npc.getNPC().isMarriageCandidate()) {
                return npc.getNPC().getAcceptProposal();
            } else return npc.getNPC().getRejectProposal();
        } else return npc.getNPC().getThanks(value);
    }
}
