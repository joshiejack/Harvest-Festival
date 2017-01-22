package joshie.harvest.npcs.gui;

import joshie.harvest.api.npc.gift.IGiftHandler.Quality;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.Locale;

public class GuiNPCGift extends GuiNPCChat {
    public static ItemStack GODDESS_GIFT;
    private final ItemStack gift;
    private final Quality value;

    public GuiNPCGift(EntityPlayer player, EntityNPC npc, EnumHand hand) {
        super(player, npc, hand, -1, false);
        gift = player.getHeldItem(hand).copy();
        value = npc.getNPC().getGiftValue(gift);
    }

    public GuiNPCGift(EntityPlayer player, EntityNPC npc, EnumHand hand, ItemStack gift) {
        super(player, npc, hand, -1, false);
        this.gift = gift;
        this.value = npc.getNPC().getGiftValue(gift);
    }

    @Override
    public String getScript() {
        if (NPCHelper.INSTANCE.getGifts().isBlacklisted(gift)) return TextHelper.getSpeech(npc, "gift.no");
        //TODO: Reenable in 1.0 when I readd marriage
        /*
        if (ToolHelper.isBlueFeather(gift)) {
            int relationship = HFApi.player.getRelationsForPlayer(player).getRelationship(npc.getNPC().getUUID());
            if (relationship >= HFNPCs.MAX_FRIENDSHIP && npc.getNPC().isMarriageCandidate()) {
                return TextHelper.getSpeech(npc, "marriage.accept");
            } else return TextHelper.getSpeech(npc, "marriage.reject");
        } else */if (GODDESS_GIFT != null || HFTrackers.getClientPlayerTracker().getRelationships().gift(player, npc.getNPC().getUUID(), value.getRelationPoints())) {
            return TextHelper.getSpeech(npc, "gift." + value.name().toLowerCase(Locale.ENGLISH));
        } else return TextHelper.getSpeech(npc, "gift.reject");
    }

    @Override
    public void endChat() {
        player.closeScreen();
        GODDESS_GIFT = null; //Reset
    }
}