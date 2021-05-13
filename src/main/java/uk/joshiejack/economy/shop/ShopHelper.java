package uk.joshiejack.economy.shop;

import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.client.gui.GuiShop;
import uk.joshiejack.economy.shop.input.InputMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import javax.annotation.Nullable;
import java.util.Collection;

public class ShopHelper {
    public static void resetGUI() {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen instanceof GuiShop) {
            ((GuiShop)screen).refresh();
        }
    }

    public static void open(Collection<Department> shops, ShopTarget target, InputMethod method) {
        Department shop = getFirstShop(shops, target, Condition.CheckType.SHOP_IS_OPEN, method);
        if (shop != null) {
            shop.open(target);
        }
    }

    @Nullable
    public static Department getFirstShop(Collection<Department> shops, ShopTarget target, Condition.CheckType type, InputMethod method) {
        for (Department shop: shops) {
            if (shop.isValidFor(target, type, method)) {
                return shop;
            }
        }

        return null;
    }
}
