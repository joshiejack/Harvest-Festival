package joshie.harvest.cooking.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.recipe.MealImpl;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Side.SERVER)
public class PacketSelectRecipe extends PenguinPacket {
    private MealImpl recipe;

    public PacketSelectRecipe() {}
    public PacketSelectRecipe(MealImpl recipe) {
        this.recipe = recipe;
    }

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeUTF8String(to, recipe.getRegistryName().toString());
    }

    @Override
    public void fromBytes(ByteBuf from) {
        recipe = CookingAPI.REGISTRY.getValue(new ResourceLocation(ByteBufUtils.readUTF8String(from)));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        CookingHelper.tryPlaceIngredients(player, recipe);
    }
}
