package uk.joshiejack.furniture.network;

import uk.joshiejack.furniture.client.gui.GuiBasket;
import uk.joshiejack.furniture.client.gui.GuiTelevision;
import uk.joshiejack.furniture.inventory.ContainerBasket;
import uk.joshiejack.furniture.tile.TileTelevision;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class FurnitureGuiHandler implements IGuiHandler {
    public static final int BASKET = 1;

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == BASKET) {
            return new ContainerBasket(player.inventory, player.getHeldItem(EnumHand.values()[z]));
        } else return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == BASKET) {
            return new GuiBasket(new ContainerBasket(player.inventory, player.getHeldItem(EnumHand.values()[z])));
        } else {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof TileTelevision) {
                return new GuiTelevision((TileTelevision) tile);
            }

            return null;
        }
    }
}
