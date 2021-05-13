package uk.joshiejack.harvestcore.network.mine;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.harvestcore.client.gui.GuiElevator;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketDisplayElevatorGUI extends PenguinPacket {
    private int floor;

    public PacketDisplayElevatorGUI() {}
    public PacketDisplayElevatorGUI(int floor) {
        this.floor = floor;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(floor);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        floor = buf.readInt();
    }


    @Override
    public void handlePacket(EntityPlayer player) {
        FMLCommonHandler.instance().showGuiScreen(new GuiElevator(floor));
    }
}
