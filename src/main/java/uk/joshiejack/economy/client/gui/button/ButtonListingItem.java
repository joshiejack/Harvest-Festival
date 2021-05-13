package uk.joshiejack.economy.client.gui.button;

import uk.joshiejack.economy.client.gui.GuiShop;
import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.economy.shop.MaterialCost;
import uk.joshiejack.penguinlib.client.gui.CyclingStack;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ButtonListingItem extends ButtonListing {
    private final CyclingStack stack;
    private final MaterialCost requirement;
    private final int offset;

    public ButtonListingItem(MaterialCost requirement, GuiShop shop, Listing purchasable, int buttonId, int x, int y) {
        super(shop, purchasable, buttonId, x, y);
        this.requirement = requirement;
        NonNullList<ItemStack> stacks = NonNullList.withSize(requirement.getStacks().size(), ItemStack.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
            stacks.set(i, StackHelper.toStack(requirement.getStacks().get(i), requirement.getCost()));
        }

        this.stack = new CyclingStack(stacks);
        this.offset = shop.mc.world.rand.nextInt(stacks.size());
    }

    @Override
    protected void drawForeground(Minecraft mc, FontRenderer fontrenderer, int j) {
        StackRenderHelper.drawStack(listing.getSubListing(shop.stock).getDisplayStack(), x + 2, y + 1, 1F);
        drawString(fontrenderer, displayString, x + 20, y + (height - 8) / 2, j);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        //Draw the cost
        String cost = shop.getCostAsString(requirement.getCost());
        int width = fontrenderer.getStringWidth(cost);
        StackRenderHelper.drawStack(stack.getStack(offset), x + 188 - width, y + 1, 1F);
    }
}
