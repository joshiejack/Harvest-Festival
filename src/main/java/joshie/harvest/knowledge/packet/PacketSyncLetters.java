package joshie.harvest.knowledge.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.core.Letter;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.knowledge.letter.LetterDataClient;
import joshie.harvest.quests.packet.PacketSharedSync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.HashSet;
import java.util.Set;

@Packet(Side.CLIENT)
public class PacketSyncLetters extends PacketSharedSync {
    private Set<ResourceLocation> set;

    public PacketSyncLetters(){}
    public PacketSyncLetters(Set<Letter> set) {
        this.set = new HashSet<>();
        for (Letter letter: set) {
            this.set.add(letter.getResource());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("Letters", NBTHelper.writeResourceSet(set));
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        set = NBTHelper.readResourceSet(ByteBufUtils.readTag(buf), "Letters");
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        this.<LetterDataClient>getLetterDataFromPlayer(player).setLetters(set);
    }
}
