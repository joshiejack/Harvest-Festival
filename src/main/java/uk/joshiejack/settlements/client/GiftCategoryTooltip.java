package uk.joshiejack.settlements.client;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.npcs.gifts.GiftCategory;
import uk.joshiejack.settlements.npcs.gifts.GiftQuality;
import uk.joshiejack.settlements.npcs.gifts.GiftRegistry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Settlements.MODID, value = Side.CLIENT)
public class GiftCategoryTooltip {
    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {
        if (GiftRegistry.CATEGORY_REGISTRY == null) return;
        GiftCategory none = GiftCategory.get("none");
        GiftCategory category = GiftRegistry.CATEGORY_REGISTRY.getValue(event.getItemStack());
        if (category != none && event.getFlags().isAdvanced()) {
            event.getToolTip().add(TextFormatting.AQUA + "Gifts");
            if (GuiScreen.isShiftKeyDown()) {
                event.getToolTip().add(TextFormatting.WHITE + "Overrides");
            } else {
                event.getToolTip().add(TextFormatting.GREEN +  "Category   " + TextFormatting.RESET + category.name());
                event.getToolTip().add(TextFormatting.YELLOW + "Quality      " + colorFromQuality(category.quality()) + category.quality().name());
            }

            int overrides = 0;
            for (NPC npc: NPC.all()) {
                GiftQuality quality = GiftRegistry.getQualityForNPC(npc, event.getItemStack());
                if (quality != category.quality()) {
                    overrides++;
                    if (GuiScreen.isShiftKeyDown()) {
                        event.getToolTip().add(TextFormatting.WHITE + npc.getLocalizedName() + colorFromQuality(quality) +" " + quality.name());
                    }
                }
            }

            if (!GuiScreen.isShiftKeyDown()) {
                event.getToolTip().add(TextFormatting.WHITE + "Overrides  " + TextFormatting.RESET + overrides);
                event.getToolTip().add(TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + "Hold shift to show");
            }
        }
    }

    private static TextFormatting colorFromQuality(GiftQuality quality) {
        if (quality.value() >= 1000) return TextFormatting.DARK_GREEN;
        else if (quality.value() >= 500) return TextFormatting.GREEN;
        else if (quality.value() >= 200) return TextFormatting.GRAY;
        else if (quality.value() >= -200) return TextFormatting.YELLOW;
        else if (quality.value() >= -400) return TextFormatting.GOLD;
        else return TextFormatting.RED;
    }
}