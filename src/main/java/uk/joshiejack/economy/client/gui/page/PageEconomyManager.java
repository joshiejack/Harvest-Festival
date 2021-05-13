package uk.joshiejack.economy.client.gui.page;

import uk.joshiejack.economy.client.Shipped;
import uk.joshiejack.economy.client.gui.GuiShop;
import uk.joshiejack.economy.client.gui.button.ButtonSwitchAccount;
import uk.joshiejack.economy.client.gui.button.ButtonTransfer;
import uk.joshiejack.economy.client.gui.label.LabelEconomyStats;
import uk.joshiejack.economy.client.gui.label.LabelShippingLog;
import uk.joshiejack.economy.gold.Wallet;
import uk.joshiejack.economy.shipping.Shipping;
import uk.joshiejack.harvestcore.client.gui.label.LabelShippedItem;
import uk.joshiejack.penguinlib.client.gui.CyclingStack;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBack;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonForward;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class PageEconomyManager extends PageMultiple.PageMultipleLabel<Shipping.HolderSold> {
    public static final PageEconomyManager INSTANCE = new PageEconomyManager();
    private long[] divisions = new long[] { 1, 5, 10, 100, 1000 };

    private PageEconomyManager() {
        super("economy.manager", 36);
        this.icon = new Icon(GuiShop.EXTRA, 240, 224);
    }

    @Override
    public void drawScreen(int x, int y) {
        if (empty) drawStringWithWrap(StringHelper.localize(unlocalized), gui.getGuiLeft() + 30, 24, 124);
    }

    @Override
    public void initGui(List<GuiButton> buttonList, List<GuiLabel> labelList) {
        super.initGui(buttonList, labelList);
        icon = new Icon(GuiShop.EXTRA, 240, 224);
        labelList.add(new LabelEconomyStats(gui, 25, 24));
        labelList.add(new LabelShippingLog(gui, 25, 24));
        for (int i = 0; i < divisions.length; i++) {
            buttonList.add(new ButtonTransfer(Wallet.Type.PERSONAL, divisions[i], gui, buttonList.size(), 22 + 25 * i, 75));
            buttonList.add(new ButtonTransfer(Wallet.Type.SHARED, divisions[i], gui, buttonList.size(), 22 + 25 * i, 107));
        }

        //If our team is the same as our id, remove the shared wallet
        if (Wallet.getActive() == Wallet.getWallet(Wallet.Type.PERSONAL)) {
            buttonList.add(new ButtonSwitchAccount(Wallet.Type.SHARED, gui, buttonList.size(), 25, 24));
        } else buttonList.add(new ButtonSwitchAccount(Wallet.Type.PERSONAL, gui, buttonList.size(), 25, 24));
    }

    @Override
    public ButtonBack createBackButton(List<GuiButton> buttonList) { return new ButtonBack(this, gui, buttonList.size(), 164, 138); }

    @Override
    public ButtonForward createForwardButton(List<GuiButton> buttonList) { return new ButtonForward(this, gui, buttonList.size(), 270, 138); }

    @Override
    protected LabelBook createLabel(GuiBook gui, Shipping.HolderSold entry, PageSide side, int position) {
        return new LabelShippedItem(gui, 163 + (position % 6) * 21, 28 + (side == PageSide.RIGHT ? 54 : 0) + (position / 6) * 18, new CyclingStack(NonNullList.from(ItemStack.EMPTY, entry.getStack())), entry.getValue());
    }

    @Override
    public List<Shipping.HolderSold> getList() {
        return Shipped.getShippingLog();
    }
}
