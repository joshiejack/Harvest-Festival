package uk.joshiejack.economy.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.economy.Economy;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSetPlayerNBT;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketSwitchWalletUsed extends PacketSetPlayerNBT {
    private boolean shared;

    public PacketSwitchWalletUsed() { super(Economy.MODID + "Settings");}
    public PacketSwitchWalletUsed(boolean shared) {
        super(Economy.MODID + "Settings");
        this.shared = shared;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(shared);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        shared = buf.readBoolean();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        super.handlePacket(player);
        PenguinNetwork.sendToClient(new PacketSetWalletUsed(shared), player);
    }

    @Override
    public void setData(NBTTagCompound tag) {
        tag.setBoolean("SharedWallet", shared); //Set this data, YESSSSSSSSSSSSS
    }
}
