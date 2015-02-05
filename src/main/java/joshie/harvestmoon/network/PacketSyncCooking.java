package joshie.harvestmoon.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import joshie.harvestmoon.blocks.tiles.TileCooking;
import joshie.lib.helpers.ClientHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncCooking extends AbstractPacketOrientation implements IMessageHandler<PacketSyncCooking, IMessage> {
    private boolean isCooking, hasResult;
    private int iIngredient, iSeasoning;
    private ArrayList<ItemStack> ingredients, seasonings;
    private ItemStack result;

    public PacketSyncCooking() {}

    public PacketSyncCooking(int dim, int x, int y, int z, ForgeDirection dir, boolean isCooking, ArrayList<ItemStack> ingredients, ArrayList<ItemStack> seasonings, ItemStack result) {
        super(dim, x, y, z, dir);
        this.isCooking = isCooking;
        this.hasResult = result != null;
        this.iIngredient = ingredients.size();
        this.iSeasoning = seasonings.size();
        this.ingredients = ingredients;
        this.seasonings = seasonings;
        this.result = result;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isCooking);
        buf.writeBoolean(hasResult);
        buf.writeInt(iIngredient);
        buf.writeInt(iSeasoning);
        if (iIngredient > 0) {
            for (int i = 0; i < iIngredient; i++) {
                ByteBufUtils.writeItemStack(buf, ingredients.get(i));
            }
        }

        if (iSeasoning > 0) {
            for (int i = 0; i < iSeasoning; i++) {
                ByteBufUtils.writeItemStack(buf, seasonings.get(i));
            }
        }

        if (hasResult) {
            ByteBufUtils.writeItemStack(buf, result);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        isCooking = buf.readBoolean();
        hasResult = buf.readBoolean();
        iIngredient = buf.readInt();
        iSeasoning = buf.readInt();
        ingredients = new ArrayList();
        seasonings = new ArrayList();
        if (iIngredient > 0) {
            for (int i = 0; i < iIngredient; i++) {
                ingredients.add(ByteBufUtils.readItemStack(buf));
            }
        }

        if (iSeasoning > 0) {
            for (int i = 0; i < iSeasoning; i++) {
                seasonings.add(ByteBufUtils.readItemStack(buf));
            }
        }

        if (hasResult) {
            result = ByteBufUtils.readItemStack(buf);
        }
    }

    @Override
    public IMessage onMessage(PacketSyncCooking message, MessageContext ctx) {
        super.onMessage(message, ctx);
        TileEntity tile = ClientHelper.getTile(message);
        if (tile instanceof TileCooking) {
            ((TileCooking) tile).setFromPacket(message.isCooking, message.ingredients, message.seasonings, message.result);
        }

        return null;
    }
}