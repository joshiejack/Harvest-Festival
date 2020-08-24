package joshie.harvest.shops.gui.button;

import joshie.harvest.api.shops.IPurchaseableMaterials;
import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.shops.gui.GuiNPCShop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class ButtonListingItem extends ButtonListing<IPurchaseableMaterials> {
    private final IRequirement requirement;

    public ButtonListingItem(IRequirement requirement, GuiNPCShop shop, IPurchaseableMaterials purchasable, int buttonId, int x, int y) {
        super(shop, purchasable, buttonId, x, y);
        this.requirement = requirement;
    }

    @Override
    protected void drawForeground(Minecraft mc, FontRenderer fontrenderer, int j) {
        StackRenderHelper.drawStack(purchasable.getDisplayStack(), x + 2, y + 1, 1F);
        drawString(fontrenderer, displayString, x + 20, y + (height - 8) / 2, j);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        //Draw the cost
        String cost = shop.getCostAsString(requirement.getCost());
        int width = fontrenderer.getStringWidth(cost);
        StackRenderHelper.drawStack(requirement.getIcon(), x + 188 - width, y + 1, 1F);
        drawString(fontrenderer, cost, x + 180 - width, y + (height - 8) / 2, j);
    }
}
