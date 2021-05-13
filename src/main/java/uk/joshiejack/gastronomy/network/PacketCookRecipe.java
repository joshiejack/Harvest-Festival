package uk.joshiejack.gastronomy.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.cooking.Cooker;
import uk.joshiejack.gastronomy.cooking.Recipe;
import uk.joshiejack.gastronomy.tile.base.TileCooking;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import uk.joshiejack.penguinlib.network.packet.PacketSyncNBTTagCompound;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;

@PenguinLoader(side = Side.SERVER)
public class PacketCookRecipe extends PacketSyncNBTTagCompound {
    private Appliance appliance;
    public PacketCookRecipe() {}
    public PacketCookRecipe(HolderMeta meta, Appliance appliance) {
        super(meta.serializeNBT());
        this.appliance = appliance;
    }
    @Override
    public void toBytes(ByteBuf to) {
        super.toBytes(to);
        to.writeByte(appliance.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf from) {
        super.fromBytes(from);
        appliance = Appliance.values()[from.readByte()];
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Recipe recipe = Recipe.RECIPE_BY_STACK.get(new HolderMeta(tag));
        if (recipe != null) {
            TileCooking cooking = Cooker.getNearbyAppliance(player, appliance);
            if (cooking != null) {
                List<IItemHandlerModifiable> inventories = Cooker.getFoodStorageAndPlayer(player);
                Cooker.takeFromFridgeOrPlayerInventory(cooking, inventories, recipe.getRequired());
                cooking.markDirty();
            }
        }
    }
}
