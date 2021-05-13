package uk.joshiejack.furniture.client.gui;

import net.minecraft.util.math.BlockPos;
import uk.joshiejack.furniture.network.PacketSetTVChannel;
import uk.joshiejack.furniture.television.TVChannel;
import uk.joshiejack.penguinlib.client.gui.GuiChatter;
import uk.joshiejack.penguinlib.network.PenguinNetwork;

public class GuiTVChatter extends GuiChatter {
    private final BlockPos pos;

    public GuiTVChatter(BlockPos pos, String text) {
        super(text);
        this.pos = pos;
    }

    @Override
    public void onGuiClosed() {
        PenguinNetwork.sendToServer(new PacketSetTVChannel(pos, TVChannel.OFF));
    }
}
