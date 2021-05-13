package uk.joshiejack.economy.shop.input;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.economy.api.shops.ShopInput;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ShopInputBlockState extends ShopInput<IBlockState> {
    public ShopInputBlockState(IBlockState id) {
        super(id);
    }

    public ShopInputBlockState(ByteBuf buf) {
        super(StateAdapter.fromString(ByteBufUtils.readUTF8String(buf)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean hasTag(ShopTarget target, String key, String value) {
        IBlockState state = target.world.getBlockState(target.pos);
        IProperty property = state.getBlock().getBlockState().getProperty(key);
        return property != null && state.getValue(property).toString().equals(value);
    }
}
