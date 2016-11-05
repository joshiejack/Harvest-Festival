package joshie.harvest.shops.gui;

import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.StackHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.gui.GuiNPCBase;
import joshie.harvest.player.stats.StatsClient;
import joshie.harvest.shops.Shop;
import joshie.harvest.shops.gui.button.ButtonArrow;
import joshie.harvest.shops.gui.button.ButtonListing;
import joshie.harvest.shops.gui.button.ButtonListingSell;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiNPCShop extends GuiNPCBase {
    static final ResourceLocation SHOP_BACKGROUND = new ResourceLocation(HFModInfo.MODID, "textures/gui/shop.png");
    public static final ResourceLocation SHOP_EXTRA = new ResourceLocation(HFModInfo.MODID, "textures/gui/shop_extra.png");
    protected final List<IPurchasable> contents = new ArrayList<>();
    protected final StatsClient stats;
    private final EntityPlayer client;
    protected final Shop shop;
    protected final boolean selling;
    protected int start;
    private int maxSize;

    public GuiNPCShop(EntityPlayer player, EntityNPC npc, int nextGui, boolean isSelling) {
        super(player, npc, EnumHand.MAIN_HAND, nextGui);
        client = player;
        shop = npc.getNPC().getShop();
        if (shop == null || !shop.isOpen(player.worldObj, player)) player.closeScreen();
        stats = HFTrackers.getClientPlayerTracker().getStats();
        selling = isSelling;
    }

    @Override
    public void initGui() {
        super.initGui();
        reload();
        setStart(0);
    }

    @SuppressWarnings("all")
    public void reload() {
        contents.clear();
        for (IPurchasable purchasable: shop.getContents()) {
            if (purchasable.canList(client.worldObj, client)) {
                contents.add(purchasable);
            }
        }

        setStart(start);
    }

    public int getStart() {
        return start;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public Shop getShop() {
        return shop;
    }

    public void setStart(int i) {
        buttonList.clear();
        //Up arrow
        buttonList.add(new ButtonArrow(this, -1, 225, 0, guiLeft + 232, guiTop + 60) {
            @Override
            protected void updateVisiblity() {
                visible = shop.getStart() != 0;
            }
        });

        //Down arrow
        buttonList.add(new ButtonArrow(this, +1, 242, 1, guiLeft + 232, guiTop + 210) {
            @Override
            protected void updateVisiblity() {
                visible = shop.getStart() < shop.getMaxSize();
            }
        });

        maxSize = contents.size() - getMax();
        start = Math.max(0, Math.min(maxSize, i));
        int id = start;
        int position = 0;
        int pPosition = 0;
        Iterator<IPurchasable> it = contents.iterator();
        while (it.hasNext() && id < start + 10) {
            IPurchasable purchasable = it.next();
            if (pPosition >= start && purchasable.canList(client.worldObj, client)) {
                if (purchasable.getCost() < 0) buttonList.add(new ButtonListingSell(this, purchasable, id + 2, guiLeft + 28, 38 + guiTop + (20 * position)));
                else buttonList.add(new ButtonListing(this, purchasable, id + 2, guiLeft + 28, 38 + guiTop + (20 * position)));
                position++;
                id++;
            }

            pPosition++;
        }
    }

    protected void drawResizableBackground(int x, int y) {
        mc.renderEngine.bindTexture(SHOP_BACKGROUND);
        if (buttonList.size() < 10 && (selling || npc.getNPC() != HFNPCs.BUILDER)) {
            drawTexturedModalRect(x, y, 0, 0, xSize,  40 + (37 * (buttonList.size())));
            drawTexturedModalRect(x, y + 40 + (37 * (buttonList.size())), 0, 228, xSize, 28);
        } else drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    public void drawBackground(int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        drawResizableBackground(x, y);
        ShopFontRenderer.render(this, x + 20, guiTop + 17, shop.getLocalizedName(), false);
        drawCoinage(x, guiTop + 19, stats.getGold());

        drawPlayerInventory();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void drawPlayerInventory() {
        if (selling) ShopFontRenderer.render(this, guiLeft + 250, guiTop + 27, "SELLING", false);
        else ShopFontRenderer.render(this, guiLeft + 250, guiTop + 27, "BUYING", false);
        mc.renderEngine.bindTexture(SHOP_EXTRA);
        drawTexturedModalRect(guiLeft + 240, guiTop + 40, 0, 62, 100, 194);

        int x2 = 0, y2 = 0;
        boolean first = true;
        ShopFontRenderer.render(this, guiLeft + 240, guiTop + 44, "Inventory", false);
        for (ItemStack stack: MCClientHelper.getPlayer().inventory.mainInventory) {
            if (stack != null) {
                StackHelper.drawStack(stack, guiLeft + 253 + y2, guiTop + 61 + x2 * 18, 1F);
            }
            x2++;
            if (x2 >= 9) {
                x2 = 0;
                if (first) {
                    y2 += 22;
                    first = false;
                } else {
                    y2 += 18;
                }
            }
        }
    }


    public String getCostAsString(long cost) {
        if (cost < 0) cost = -cost;
        if (cost < 1000) return "" + cost;
        long remainder = cost % 1000;
        int decimal = remainder == 0 ? 0 : remainder % 100 == 0 ? 1: remainder %10 == 0 ? 2: 3;
        int exp = (int) (Math.log(cost) / Math.log(1000));
        return String.format("%." + decimal + "f%c", cost / Math.pow(1000, exp), "kMGTPE".charAt(exp-1));
    }

    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    private void drawCoinage(int x, int y, long gold) {
        String formatted = String.valueOf(formatter.format(gold));
        ShopFontRenderer.render(this, x + 220, y, formatted, true);
        GlStateManager.disableDepth();
        mc.renderEngine.bindTexture(HFModInfo.ELEMENTS);
        mc.ingameGUI.drawTexturedModalRect((x + 224), y - 1, 244, 0, 12, 12);
        GlStateManager.enableDepth();
    }

    @Override
    public void drawForeground(int x, int y) {}

    @Override
    public void drawOverlay(int x, int y) {}

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        if (key == 208 || character == 's') {
            setStart(start + 2);
        }

        if (key == 200 || character == 'w') {
            setStart(start - 2);
        }

        super.keyTyped(character, key);
    }

    public void scroll(int amount) {
        setStart(start + amount);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        if (mouseWheel != 0) {
            if (mouseWheel < 0) setStart(start + 1);
            else setStart(start - 1);
        }
    }

    public int getMax() {
        return 10;
    }
}