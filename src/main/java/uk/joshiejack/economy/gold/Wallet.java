package uk.joshiejack.economy.gold;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumMap;

@SideOnly(Side.CLIENT)
public class Wallet {
    private static final EnumMap<Type, Wallet> WALLETS = new EnumMap<Type, Wallet>(Type.class) {{
        put(Type.PERSONAL, new Wallet());
        put(Type.SHARED, new Wallet());
    }};

    private static Wallet active = WALLETS.get(Type.PERSONAL);
    private long balance, income, expenses;

    public static Wallet getWallet(Type type) {
        return WALLETS.get(type);
    }

    public static void setActive(Type type) {
        active = getWallet(type);
    }

    public static Wallet getActive() {
        return active;
    }

    public static void setGold(Type type, long gold, long income, long expenses) {
        Wallet wallet = WALLETS.get(type);
        wallet.balance = gold;
        wallet.income = income;
        wallet.expenses = expenses;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }

    public long getExpenses() {
        return expenses;
    }

    public long getIncome() {
        return income;
    }

    public enum Type {
        PERSONAL, SHARED
    }
}
