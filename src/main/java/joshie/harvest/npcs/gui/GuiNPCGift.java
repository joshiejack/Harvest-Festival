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
    private final EntityNPC npc;

    public GuiNPCGift(EntityPlayer player, EntityNPC npc, EnumHand hand) {
        super(new ContainerNPCGift(player, npc, hand, -1), player, npc);
        this.gift = player.getHeldItem(hand).copy();
        this.value = npc.getNPC().getGiftValue(gift);
        this.npc = npc;
    }

    public GuiNPCGift(EntityPlayer player, EntityNPC npc, ItemStack gift) {
        super(new ContainerNPCGift(player, npc, null, -1), player, npc);
        this.gift = gift;
        this.value = npc.getNPC().getGiftValue(gift);
        this.npc = npc;
    }

    @Override
    public String getScript() {
        if (NPCHelper.INSTANCE.getGifts().isBlacklisted(player.world, player, gift)) return TextHelper.getSpeech(npc, "gift.no");
        //TODO: Reenable in 1.0 when I readd marriage
        /*
        if (ToolHelper.isBlueFeather(gift)) {
            int relationship = HFApi.player.getRelationsForPlayer(player).getRelationship(npc.getNPC().getUUID());
            if (relationship >= HFNPCs.MAX_FRIENDSHIP && npc.getNPC().isMarriageCandidate()) {
                return TextHelper.getSpeech(npc, "marriage.accept");
            } else return TextHelper.getSpeech(npc, "marriage.reject");
        } else */if (GODDESS_GIFT != null || HFTrackers.getClientPlayerTracker().getRelationships().gift(player, npc.getNPC(), value.getRelationPoints())) {
            return TextHelper.getSpeech(npc, "gift." + value.name().toLowerCase(Locale.ENGLISH));
        } else return TextHelper.getSpeech(npc, "gift.reject");
    }

    @Override
    public void endChat() {
        player.closeScreen();
        GODDESS_GIFT = null; //Reset
        npc.setTalking(null);
    }
}