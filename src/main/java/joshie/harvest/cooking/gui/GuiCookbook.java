package joshie.harvest.cooking.gui;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuiCookbook extends GuiScreen {
    public static final ResourceLocation LEFT_GUI = new ResourceLocation(HFModInfo.MODID, "textures/gui/book_cooking_left.png");
    public static final ResourceLocation RIGHT_GUI = new ResourceLocation(HFModInfo.MODID, "textures/gui/book_cooking_right.png");
    public static final PageUtensilList MASTER = new PageUtensilList();
    public static final Set<IngredientStack> ingredients = new HashSet<>();
    private final ArrayList<Runnable> runnables = new ArrayList<>();
    private static final int imageWidth = 154;
    private static final int imageHeight = 202;
    static final int MAX_UTENSILS_DISPLAY = 5;

    private static Page page;
    private int centreX;
    private int centreY;

    @Override
    public void initGui() {
        super.initGui();
        setPage(page == null ? MASTER : page);
        ingredients.clear();
        //Add the player inventory
        for (ItemStack stack: MCClientHelper.getPlayer().inventory.mainInventory) {
            if (stack != null) {
                Ingredient ingredient = CookingAPI.INSTANCE.getCookingComponents(stack);
                if (ingredient != null) {
                    ingredients.add(new IngredientStack(ingredient, stack.stackSize));
                }
            }
        }

        ingredients.remove(null); //Remove any nulls
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        centreX = (width / 2) - imageWidth;
        centreY = (height - imageHeight) / 2;
        int mouseX = x - centreX;
        int mouseY = y - centreY;
        mc.getTextureManager().bindTexture(RIGHT_GUI);
        drawTexturedModalRect(centreX + imageWidth, centreY, 0, 0, imageWidth, imageHeight);
        mc.getTextureManager().bindTexture(LEFT_GUI);
        drawTexturedModalRect(centreX, centreY, 102, 0, imageWidth, imageHeight);
        runnables.clear();
        page.draw(mouseX, mouseY);
        //Draw the utensil buttons
        if (page.getUtensil() != null) {
            //Draw the background buttons
            GlStateManager.color(1F, 1F, 1F);
            boolean hoverX = mouseX >= 307 && mouseX <= 333;
            for (int i = 0; i < MAX_UTENSILS_DISPLAY; i++) {
                Utensil tool = Utensil.getUtensilFromIndex(i);
                if (PageRecipeList.get(tool).hasRecipes()) {
                    mc.getTextureManager().bindTexture(LEFT_GUI);
                    boolean hoverY = mouseY >= 16 + i * 36 && mouseY <= 50 + i * 36;
                    int theY = page.getUtensil() == tool ? 64 : hoverX && hoverY ? 32: 0;
                    drawTexture(308, 16 + i * 36, 0, theY, 26, 32);
                    drawStack(308, 25 + i * 36, PageRecipeList.get(tool).getItem(), 1F);
                }
            }
        }

        mc.getTextureManager().bindTexture(LEFT_GUI);
        //Draw the back button
        if (page.getOwner() != page) {
            GlStateManager.color(1F, 1F, 1F);
            int buttonY = mouseX >= 24 && mouseX <= 39 && mouseY >= 168 && mouseY <= 178 ? 246 : 235;
            drawTexture(24, 168, 16, buttonY, 15, 10);
        }

        runnables.forEach(Runnable :: run);
    }

    @Override
    protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
        super.mouseClicked(x, y, mouseButton);
        int mouseX = x - centreX;
        int mouseY = y - centreY;
        if (page.mouseClicked(mouseX, mouseY)) return;
        //Draw the utensil buttons
        if (page.getUtensil() != null) {
            boolean hoverX = mouseX >= 307 && mouseX <= 333;
            for (int i = 0; i < MAX_UTENSILS_DISPLAY; i++) {
                Utensil tool = Utensil.getUtensilFromIndex(i);
                if (PageRecipeList.get(tool).hasRecipes()) {
                    boolean hoverY = mouseY >= 16 + i * 36 && mouseY <= 50 + i * 36;
                    if (hoverX && hoverY) {
                        setPage(PageRecipeList.get(tool));
                        return; //Don't continue
                    }
                }
            }
        }


        if (page.getOwner() != page && mouseX >= 24 && mouseX <= 39 && mouseY >= 168 && mouseY <= 178 || mouseButton == 1) {
            setPage(page.getOwner());
        }
    }

    public void addRunnable(Runnable r) {
        runnables.add(r);
    }

    boolean setPage(Page page) {
        GuiCookbook.page = page.initGui(this);
        return true;
    }

    void drawString(int x, int y, String text) {
        fontRendererObj.drawSplitString(text, centreX + x, centreY + y, 120, 4210752);
    }

    void drawStack(int x, int y, ItemStack stack, float scale) {
        StackRenderHelper.drawStack(stack, centreX + x, centreY + y, scale);
    }

    void drawTexture(int x, int y, int startX, int startY, int widthX, int heightY) {
        drawTexturedModalRect(centreX + x, centreY + y, startX, startY, widthX, heightY);
    }

    void drawBox(int x, int y, int width, int length, int color) {
        drawRect(centreX + x, centreY + y, centreX + x + width, centreY + y + length, color);
    }

    public void drawIngredientTooltip(List<ItemStack> stacks, int mouseX, int mouseY) {
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        int k = (int)(stacks.size() >= 6 ? 6 * 15.5: stacks.size() * 15.5);
        int j2 = mouseX + centreX + 12;
        int k2 = mouseY + centreY + 6;
        int i1 = 8;

        if (stacks.size() > 1) {
            i1 += 2 + (stacks.size() - 1) * 10;
        }

        if (j2 + k > this.width) {
            j2 -= 28 + k;
        }

        i1 = ((stacks.size() / 6) + 1) * 16;
        this.zLevel = 300.0F;
        itemRender.zLevel = 300.0F;
        int j1 = 0xEEB0A483;
        this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
        this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
        this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
        this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
        this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
        int k1 = 0xEE79725A;
        int l1 = 0xEE79725A;
        this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
        this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
        this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
        this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

        for (int i2 = 0; i2 < stacks.size(); ++i2) {
            if (i2 == 0) {
                k2 += 2;
            }

            k2 += 10;
        }

        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();

        //Draw the stacks
        int i = 0, j = 0;
        for (ItemStack stack: stacks) {
            drawStack(mouseX + 10 + (i * 16), mouseY + 6 + (j * 16), stack, 1F);

            i++;
            if (i > 5) {
                i = 0;
                j++;
            }
        }
    }
}
