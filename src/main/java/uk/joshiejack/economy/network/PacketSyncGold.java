package uk.joshiejack.economy.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.economy.gold.Wallet;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncGold extends PenguinPacket {
    private Wallet.Type type;
    private long balance, income, expenses;

    public PacketSyncGold() {}
    public PacketSyncGold(Wallet.Type type, long balance, long income, long expenses) {
        this.type = type;
        this.balance = balance;
        this.income = income;
        this.expenses = expenses;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeBoolean(type == Wallet.Type.SHARED);
        to.writeLong(balance);
        to.writeLong(income);
        to.writeLong(expenses);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        type = from.readBoolean() ? Wallet.Type.SHARED : Wallet.Type.PERSONAL;
        balance = from.readLong();
        income = from.readLong();
        expenses = from.readLong();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Wallet.setGold(type, balance, income, expenses);
    }
}
