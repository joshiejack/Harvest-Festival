package joshie.harvest.npc.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.gui.GuiNPCGift;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Side.CLIENT)
public class PacketGoddessGift extends PenguinPacket {
    private int npcID;
    private ItemStack stack;

    public PacketGoddessGift() {}
    public PacketGoddessGift(EntityNPC npc, ItemStack stack) {
        this.npcID = npc.getEntityId();
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeInt(npcID);
        ByteBufUtils.writeItemStack(to, stack);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        npcID = from.readInt();
        stack = ByteBufUtils.readItemStack(from);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handlePacket(EntityPlayer player) {
        EntityNPC npc = (EntityNPC) player.worldObj.getEntityByID(npcID);
        if (npc != null) {
            if (npc.isEntityAlive()) {
                if (npc.getNPC() == HFNPCs.GODDESS) {
                    GuiNPCGift.GODDESS_GIFT = stack;
                    player.openGui(HarvestFestival.instance, GuiHandler.GIFT_GODDESS, player.worldObj, npcID, -1, EnumHand.MAIN_HAND.ordinal());
                }

                npc.setTalking(player);
            }
        }
    }
}
