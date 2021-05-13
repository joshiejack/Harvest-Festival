package uk.joshiejack.economy.api.shops;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.Objects;

public abstract class ShopInput<T> {
    protected T id;

    protected ShopInput(T id) {
        this.id = id;
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, id.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopInput<?> input = (ShopInput<?>) o;
        return Objects.equals(id, input.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getName(ShopTarget target) {
        return id.toString();
    }

    public abstract boolean hasTag(ShopTarget target, String key, String value);
}
