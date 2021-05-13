package uk.joshiejack.economy.shop.input;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.economy.api.shops.ShopInput;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.holder.Holder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ShopInputItem extends ShopInput<Holder> {
    public ShopInputItem(ItemStack stack) {
        super(Holder.getFromStack(stack));
    }

    public ShopInputItem(ByteBuf buf) {
        super(fromByteBuf(buf));
    }

    private static Holder fromByteBuf(ByteBuf buf) {
        String type = ByteBufUtils.readUTF8String(buf);
        try {
            Holder holder = Holder.TYPES.get(type).getClass().newInstance();
            holder.deserializeNBT(ByteBufUtils.readTag(buf));
            return holder;
        } catch (Exception ex) { return null; }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, id.name());
        ByteBufUtils.writeTag(buf, id.serializeNBT());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean hasTag(ShopTarget target, String key, String value) {
        return target.stack.hasTagCompound() && target.stack.getTagCompound().getString(key).equals(value);
    }

}
