package joshie.harvest.npc.gui;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftHandler.Quality;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.core.util.Text;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.AbstractEntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class GuiNPCGift extends GuiNPCChat {
    private ItemStack gift;
    private Quality value;

    public GuiNPCGift(AbstractEntityNPC npc, EntityPlayer player, EnumHand hand) {
        super(npc, player, -1);
        gift = player.getHeldItem(hand).copy();
        value = npc.getNPC().getGiftValue(gift);
    }

    @Override
    public String getScript() {
        if (ToolHelper.isBlueFeather(gift)) {
            int relationship = HFApi.player.getRelationshipHelper().getRelationship(player, npc.getNPC());
            if (relationship >= HFNPCs.MARRIAGE_REQUIREMENT && npc.getNPC().isMarriageCandidate()) {
                return Text.getSpeech(npc, "marriage.accept");
            } else return Text.getSpeech(npc, "marriage.reject");
        } else if (HFTrackers.getClientPlayerTracker().getRelationships().gift(player, npc.getRelatable(), value.getRelationPoints())) {
            return Text.getSpeech(npc, "gift." + value.name().toLowerCase());
        } else return Text.getSpeech(npc, "gift.reject");
    }

    @Override
    public void endChat() {
        player.closeScreen();

        if (nextGui != -1) {
            player.openGui(HarvestFestival.instance, nextGui, player.worldObj, npc.getEntityId(), 0, -1);
        }
    }
}