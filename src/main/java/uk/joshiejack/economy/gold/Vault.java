package uk.joshiejack.economy.gold;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.economy.api.gold.IVault;
import uk.joshiejack.economy.EconomyConfig;
import uk.joshiejack.economy.network.PacketSyncGold;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;

import java.util.UUID;

public class Vault implements INBTSerializable<NBTTagCompound>, IVault {
    private static final long MAX = (long) EconomyConfig.maxGold * EconomyConfig.maxGoldMultiplier;
    private final Bank bank;
    private final UUID uuid;
    private long balance = 0;
    private long expenses = 0;
    private long income = 0;
    private boolean shared;

    public Vault(Bank bank, UUID uuid) {
        this.bank = bank;
        this.uuid = uuid;
    }

    public Vault personal() {
        this.shared = false;
        return this;
    }

    public Vault shared() {
        this.shared = true;
        return this;
    }

    public long getBalance() {
        return balance;
    }

    public void decreaseGold(World world, long amount) {
        if (amount < 0) amount = 0;
        expenses += amount;
        setBalance(world, balance - amount);
    }

    @Override
    public void increaseGold(World world, long amount) {
        if (amount < 0) amount = 0;
        income += amount;

        setBalance(world, balance + amount);
    }

    public void setBalance(World world, long amount) {
        balance = MathsHelper.constrainToRangeLong(amount, EconomyConfig.minGold, MAX);
        bank.markDirty(); //Mark for saving
        synchronize(world); //Sync the data to the players
    }

    public void synchronize(World world) {
        if (shared) {
            PenguinNetwork.sendToTeam(new PacketSyncGold(Wallet.Type.SHARED, balance, income, expenses), world, uuid);
        } else PenguinNetwork.sendToClient(new PacketSyncGold(Wallet.Type.PERSONAL, balance, income, expenses), PlayerHelper.getPlayerFromUUID(world, uuid));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();;
        tag.setLong("Balance", balance);
        tag.setLong("Expenses", expenses);
        tag.setLong("Income", income);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        balance = nbt.getLong("Balance");
        expenses = nbt.getLong("Expenses");
        income = nbt.getLong("Income");
    }
}
