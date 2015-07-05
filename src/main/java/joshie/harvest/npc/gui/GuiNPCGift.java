package joshie.harvest.npc.gui;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.config.NPC;
import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.gift.Gifts.Quality;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GuiNPCGift extends GuiNPCChat {
    private ItemStack gift;
    private Quality value;

    public GuiNPCGift(EntityNPC npc, EntityPlayer player) {
        super(npc, player, -1);
        gift = player.getCurrentEquippedItem();
        value = npc.getNPC().getGiftValue(gift);
    }

    @Override
    public String getScript() {
        if (ToolHelper.isBlueFeather(gift)) {
            int relationship = HFApi.RELATIONS.getAdjustedRelationshipValue(player, npc.getNPC());
            if (relationship >= NPC.ADJUSTED_MARRIAGE_REQUIREMENT && npc.getNPC().isMarriageCandidate()) {
                return npc.getNPC().getAcceptProposal();
            } else return npc.getNPC().getRejectProposal();
        } else return npc.getNPC().getThanks(value);
    }
}
