package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.blocks.tiles.TileCooking;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;

public class PacketSyncCooking extends AbstractPacketOrientation {
    private boolean isCooking, hasResult;
    private int iIngredient;
    private ArrayList<ItemStack> ingredients;
    private ItemStack result;

    public PacketSyncCooking() {}

    public PacketSyncCooking(int dim, BlockPos pos, EnumFacing dir, boolean isCooking, ArrayList<ItemStack> ingredients, ItemStack result) {
        super(dim, pos, dir);
        this.isCooking = isCooking;
        this.hasResult = result != null;
        this.iIngredient = ingredients.size();
        this.ingredients = ingredients;
        this.result = result;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isCooking);
        buf.writeBoolean(hasResult);
        buf.writeInt(iIngredient);
        if (iIngredient > 0) {
            for (int i = 0; i < iIngredient; i++) {
                ByteBufUtils.writeItemStack(buf, ingredients.get(i));
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
        ingredients = new ArrayList<ItemStack>(20);
        if (iIngredient > 0) {
            for (int i = 0; i < iIngredient; i++) {
                ingredients.add(ByteBufUtils.readItemStack(buf));
            }
        }

        if (hasResult) {
            result = ByteBufUtils.readItemStack(buf);
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        super.handlePacket(player);
        TileEntity tile = MCClientHelper.getTile(this);
        if (tile instanceof TileCooking) {
            ((TileCooking) tile).setFromPacket(isCooking, ingredients, result);
        }
    }
}