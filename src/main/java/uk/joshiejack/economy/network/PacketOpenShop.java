package uk.joshiejack.economy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.client.gui.GuiShop;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.input.ShopInputBlockState;
import uk.joshiejack.economy.shop.input.ShopInputEntity;
import uk.joshiejack.economy.shop.input.ShopInputItem;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader(side = Side.CLIENT)
public class PacketOpenShop extends AbstractPacketSyncDepartment {
    private ShopTarget target;
    private int entityID;

    public PacketOpenShop() {}
    public PacketOpenShop(Department department, ShopTarget target) {
        super(department);
        this.target = target;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeLong(target.pos.toLong());
        buf.writeInt(target.entity.getEntityId());
        ByteBufUtils.writeItemStack(buf, target.stack);
        if (target.input instanceof ShopInputBlockState) buf.writeByte(0);
        else if (target.input instanceof ShopInputEntity) buf.writeByte(1);
        else buf.writeByte(2);
        target.input.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        target = new ShopTarget();
        target.pos = BlockPos.fromLong(buf.readLong());
        entityID = buf.readInt();
        target.stack = ByteBufUtils.readItemStack(buf);
        byte type = buf.readByte();
        target.input = (type == 0 ? new ShopInputBlockState(buf) : type == 1 ? new ShopInputEntity(buf) : new ShopInputItem(buf));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handlePacket(EntityPlayer player) {
        target.world = player.world;
        target.entity = player.world.getEntityByID(entityID);
        target.player = player;
        Minecraft.getMinecraft().displayGuiScreen(new GuiShop(department, target));
    }
}