package uk.joshiejack.settlements.network.book;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.settlements.client.gui.page.PageQuests;
import uk.joshiejack.settlements.quest.settings.Information;
import uk.joshiejack.penguinlib.network.packet.PacketOpenUniversalGuide;
import uk.joshiejack.penguinlib.util.PenguinGroup;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import java.util.List;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncInformation extends PacketOpenUniversalGuide {
    private List<Information> list;

    public PacketSyncInformation() {}
    public PacketSyncInformation(List<Information> list) {
        this.list = list;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(list.size());
        list.forEach(info -> {
            buf.writeByte(info.getGroup().ordinal());
            ByteBufUtils.writeUTF8String(buf, info.getName());
            ByteBufUtils.writeItemStack(buf, info.getIcon());
            ByteBufUtils.writeUTF8String(buf, info.getDescription());
        });
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        list = Lists.newArrayList();
        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            list.add(new Information(PenguinGroup.values()[buf.readByte()], ByteBufUtils.readUTF8String(buf), ByteBufUtils.readItemStack(buf), ByteBufUtils.readUTF8String(buf)));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        PageQuests.INSTANCE.setInformations(list);
        super.handlePacket(player); //Perform the underlying gui opening
    }
}
