package uk.joshiejack.economy.shop.input;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.economy.api.shops.ShopInput;
import uk.joshiejack.economy.api.shops.ShopTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ShopInputEntity extends ShopInput<ResourceLocation> {
    public ShopInputEntity(Entity entity) {
        super(entity instanceof EntityPlayer ? new ResourceLocation("player") : EntityList.getKey(entity));
    }

    public ShopInputEntity(ResourceLocation resource) {
        super(resource);
    }

    public ShopInputEntity(ByteBuf buf) {
        super(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
    }

    @Override
    public String getName(ShopTarget target) {
        return target.entity.getName();
    }

    @Override
    public boolean hasTag(ShopTarget target, String key, String value) {
        return target.entity.writeToNBT(new NBTTagCompound()).getString(key).equals(value);
    }
}
