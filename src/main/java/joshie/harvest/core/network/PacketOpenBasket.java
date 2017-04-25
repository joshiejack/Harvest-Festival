package joshie.harvest.core.network;


import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.core.entity.EntityBasket;
import joshie.harvest.core.handlers.BasketHandler;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.network.Packet.Side;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Side.SERVER)
public class PacketOpenBasket extends PenguinPacket {
    public PacketOpenBasket() {}
    @Override
    public void toBytes(ByteBuf to) {}

    @Override
    public void fromBytes(ByteBuf from) {}

    @Override
    public void handlePacket(EntityPlayer player) {
        EntityBasket basket = BasketHandler.getWearingBasket(player);
        if (basket != null) {
            player.openGui(HarvestFestival.instance, GuiHandler.BASKET_ENTITY, player.world, 0, 0, 0);
        }
    }
}