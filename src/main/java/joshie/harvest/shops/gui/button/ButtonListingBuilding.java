package joshie.harvest.shops.gui.button;

import joshie.harvest.api.shops.IPurchaseableMaterials;
import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.core.helpers.StackHelper;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.shops.gui.GuiNPCShop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

public class ButtonListingBuilding extends ButtonListing<IPurchaseableMaterials> {
    public static final ItemStack log = new ItemStack(Blocks.LOG);
    public static final ItemStack stone = new ItemStack(Blocks.STONE);
    private final ItemStack[] theStacks;

    public ButtonListingBuilding(GuiNPCShop shop, IPurchaseableMaterials purchasable, int buttonId, int x, int y) {
        super(shop, purchasable, buttonId, x, y);
        theStacks = new ItemStack[purchasable.getRequirements().length];
        for (int i = 0; i < theStacks.length; i++) {
            IRequirement requirement = purchasable.getRequirements()[i];
            theStacks[i] = StackHelper.toStack(requirement.getIcon(), requirement.getCost());
        }
    }

    @Override
    public void drawForeground(Minecraft mc, FontRenderer fontrenderer, int j) {
        //Names
        drawString(fontrenderer, StringUtils.left(displayString, 18), xPosition + 20, yPosition + (height - 8) / 2, j);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        StackRenderHelper.drawStack(purchasable.getDisplayStack(), xPosition + 2, yPosition + 1, 1F);
        //Draw the cost
        if (purchasable.getCost() > 0) {
            String cost = shop.getCostAsString(purchasable.getCost());
            int width = fontrenderer.getStringWidth(cost);
            mc.renderEngine.bindTexture(HFModInfo.ELEMENTS);
            drawTexturedModalRect(xPosition + 184, (yPosition + (height - 8) / 2) - 2, 244, 0, 12, 12);
            drawString(fontrenderer, cost, xPosition + 180 - width, yPosition + (height - 8) / 2, j);
        }

        //Draw the requirements
        int index = 0;
        for (int i = theStacks.length - 1; i >= 0; i--) {
            StackRenderHelper.drawStack(theStacks[i], xPosition + 135 - (index * 18), yPosition + 3, 0.75F);
            index++;
        }
    }
}
