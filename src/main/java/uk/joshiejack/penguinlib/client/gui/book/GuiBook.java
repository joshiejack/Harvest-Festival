package uk.joshiejack.penguinlib.client.gui.book;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.client.gui.CyclingObject;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonTextField;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.inventory.ContainerBook;
import uk.joshiejack.penguinlib.network.PacketSetBookInventory;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackRenderHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GuiBook extends GuiContainer {
    public static final Map<String, CyclingObject> cycles = Maps.newHashMap();
    private final ArrayList<Runnable> runnables = new ArrayList<>();
    private final ArrayList<String> tooltip = new ArrayList<>();
    public final ResourceLocation left;
    public final ResourceLocation right;
    private final int backgroundWidth;
    private final int backgroundHeight;
    protected boolean empty;
    private Page page;
    public int fontColor1 = 0x857754;
    public int fontColor2 = 4210752;

    public GuiBook(ResourceLocation left, ResourceLocation right, int backgroundWidth, int backgroundHeight) {
        super(new ContainerBook(0));
        this.left = left;
        this.right = right;
        this.xSize = 25 + backgroundWidth * 2;
        this.ySize = backgroundHeight;
        this.backgroundWidth = backgroundWidth;
        this.backgroundHeight = backgroundHeight;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        page = page == null ? getDefaultPage() : page;
        if (page instanceof DefaultPage) {
            page = ((DefaultPage)page).getActualPage();
        }

        buttonList.clear();
        labelList.clear();
        guiLeft = (width - xSize) / 2 + 25;
        guiTop = (height - ySize) / 2;
        if (page != null) {
            page.setGui(this);
            page.initGui(buttonList, labelList); //Load the buttons and labels for this gui
            initTabs(buttonList); //The tabs for this book
            if (page.hasBackwardsButton()) buttonList.add(page.createBackButton(buttonList));
            if (page.hasForwardsButton()) buttonList.add(page.createForwardButton(buttonList));
            inventorySlots = ContainerBook.getContainerFromID(mc.player, page.container);
            mc.player.openContainer = inventorySlots;
            PenguinNetwork.sendToServer(new PacketSetBookInventory(page.container));
            Keyboard.enableRepeatEvents(true);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (page.getFocus() != null) {
            page.getFocus().updateScreen();
        }
    }

    protected abstract void initTabs(List<GuiButton> buttonList);

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(right);
        drawTexturedModalRect(guiLeft + backgroundWidth, guiTop, 0, 0, backgroundWidth, backgroundHeight);
        mc.getTextureManager().bindTexture(left);
        drawTexturedModalRect(guiLeft, guiTop, 102, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        runnables.clear();
        tooltip.clear();
        super.drawScreen(x, y, partialTicks);
        page.drawScreen(x, y);
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT); //Reset the rendering
        drawTooltip(tooltip, x, y);
        runnables.forEach(Runnable :: run);
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
        this.initGui();
    }

    public void addTooltip(String string) {
        tooltip.add(string);
    }

    public void addTooltip(List<String> list) {
        tooltip.addAll(list);
    }

    public abstract Page getDefaultPage();

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int mouseWheel = Mouse.getDWheel();
        if (mouseWheel != 0) {
            if (mouseWheel < 0 && page.hasForwardsButton()) page.onForward();
            else if (page.hasBackwardsButton()) page.onBack();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        page.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 1 && page.hasBackwardsButton()) {
            page.onBack();
        } else {

            if (!(selectedButton instanceof ButtonTextField)) {
                page.setInFocus(null); // Clear the focus if we clicked off the field
            }
        }
    }

    public void drawStack(int x, int y, ItemStack stack, float scale) {
        StackRenderHelper.drawStack(stack, guiLeft + x, guiTop + y, scale);
    }

    private void drawTooltip(List<String> list, int x, int y) {
        if (!list.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int k = 0;

            Map<String, ItemStack> cache = Maps.newHashMap();
            for (String s : list) {
                if (s.contains("`")) {
                    String[] split = s.split("`");
                    CyclingObject cycle = cycles.get(split[0]);
                    if (cycle != null) {
                        cache.put(s, (((ItemStack) cycle.getStack(0))));
                        s = "   " + cache.get(s).getDisplayName();
                    }
                }


                int l = fontRenderer.getStringWidth(s);
                if (l > k) {
                    k = l;
                }
            }

            int j2 = x + 12;
            int k2 = y - 12;
            int i1 = 8;

            if (list.size() > 1) {
                i1 += 2 + (list.size() - 1) * 10;
            }

            if (j2 + k > width) {
                j2 -= 28 + k;
            }

            if (k2 + i1 + 6 > height) {
                k2 = height - i1 - 6;
            }

            zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int j1 = 0xEE1F1F1F;
            drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 0xEE504D4C;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < list.size(); ++i2) {
                String s1 = list.get(i2);
                if (s1.contains("`")) {
                    String[] split = s1.split("`");
                    CyclingObject cycle = cycles.get(split[0]);
                    if (cycle != null) {
                        ItemStack stack = cache.get(s1);
                        StackRenderHelper.drawStack(stack, j2, k2, 0.5F);
                        s1 = "   " + TextFormatting.GRAY + stack.getDisplayName();
                    }
                }

                fontRenderer.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0) {
                    k2 += 2;
                }

                k2 += 10;
            }

            zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
}