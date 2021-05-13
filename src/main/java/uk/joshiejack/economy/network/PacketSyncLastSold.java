package uk.joshiejack.economy.network;

import uk.joshiejack.economy.client.Shipped;
import uk.joshiejack.economy.shipping.Shipping;
import uk.joshiejack.penguinlib.network.packet.PacketSyncNBTTagCompound;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Set;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncLastSold extends PacketSyncNBTTagCompound {
    public PacketSyncLastSold() {}
    public PacketSyncLastSold(Set<Shipping.HolderSold> set) {
        super(NBTHelper.writeHolderCollectionToTag(set));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Shipped.getShippingLog().clear();
        Shipped.getShippingLog().addAll(NBTHelper.readHolderCollectionFromTag(tag));
    }
}
