package uk.joshiejack.economy.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.gold.Wallet;
import uk.joshiejack.penguinlib.network.packet.PacketSetPlayerNBTBoolean;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader(side = Side.CLIENT)
public class PacketSetWalletUsed extends PacketSetPlayerNBTBoolean {

    public PacketSetWalletUsed() { super(Economy.MODID + "Settings", "SharedWallet");}
    public PacketSetWalletUsed(boolean shared) {
        super(Economy.MODID + "Settings", "SharedWallet", shared);
    }

    @Override
    public void setData(NBTTagCompound tag) {
        super.setData(tag);
        Wallet.setActive(isTrue ? Wallet.Type.SHARED : Wallet.Type.PERSONAL);
    }
}
