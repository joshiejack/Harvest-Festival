package uk.joshiejack.economy.client.gui.button;

import com.google.common.collect.Lists;
import uk.joshiejack.economy.client.gui.GuiShop;
import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.economy.shop.MaterialCost;
import uk.joshiejack.penguinlib.client.gui.CyclingStack;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class ButtonListingBuilding extends ButtonListing {
    public static final ItemStack log = new ItemStack(Blocks.LOG);
    public static final ItemStack stone = new ItemStack(Blocks.STONE);
    private final NonNullList<CyclingStack> theStacks;

    @SuppressWarnings("unchecked")
    public ButtonListingBuilding(GuiShop shop, Listing purchasable, int buttonId, int x, int y) {
        super(shop, purchasable, buttonId, x, y);
        theStacks = NonNullList.create();
        List<MaterialCost> costs = sublisting.getMaterials();
        for (MaterialCost requirement: costs) {
            NonNullList<ItemStack> stacks = NonNullList.withSize(requirement.getStacks().size(), ItemStack.EMPTY);
            for (int i = 0; i < stacks.size(); i++) {
                stacks.set(i, StackHelper.toStack(requirement.getStacks().get(i), requirement.getCost()));
            }

            theStacks.add(new CyclingStack(stacks));
        }
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);

        //Draw the requirements
        int index = 0;
        for (CyclingStack stack: theStacks) {
            int x = this.x  + 135 - (index * 18);
            int y = this.y + 3;
            ItemStack render = stack.getStack(index);
            if(mouseX >= x && mouseY >= y && mouseX < x + 14 && mouseY < y + 14) {
                shop.addTooltip(Lists.newArrayList(render.getTooltip(shop.mc.player, ITooltipFlag.TooltipFlags.NORMAL)));
            }

            StackRenderHelper.drawStack(render, x, y, 0.75F);
            index++;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void drawForeground(Minecraft mc, FontRenderer fontrenderer, int j) {
        //Names
        drawString(fontrenderer, StringUtils.left(displayString, 18), x + 20, y + (height - 8) / 2, j);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        StackRenderHelper.drawStack(sublisting.getDisplayStack(), x + 2, y + 1, 1F);
        //Draw the cost
        long goldCost = listing.getGoldCost(shop.target.player, shop.stock);
        if (goldCost > 0) {
            String cost = shop.getCostAsString(goldCost);
            int width = fontrenderer.getStringWidth(cost);
            mc.renderEngine.bindTexture(GuiShop.EXTRA);
            drawTexturedModalRect(x + 184, (y + (height - 8) / 2) - 2, 244, 244, 12, 12);
            drawString(fontrenderer, cost, x + 180 - width, y + (height - 8) / 2, j);
        }
    }
}
