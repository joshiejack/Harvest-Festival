package joshie.lib.helpers;

import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

public class PowerHelper {
    public static Object[] getNextEnergyHandler(ForgeDirection from, World worldObj, int xCoord, int yCoord, int zCoord) {
        ForgeDirection facing = ForgeDirection.UNKNOWN;
        IEnergyHandler handler = null;
        Collections.shuffle(handlers);
        for (int i = 0; i < handlers.size(); i++) {
            Object[] obj = handlers.get(i);
            int x = (Integer) obj[0];
            int y = (Integer) obj[1];
            int z = (Integer) obj[2];
            ForgeDirection direction = (ForgeDirection) obj[3];

            if (from != direction) {
                if (PowerHelper.isEnergyHandler(worldObj, xCoord + x, yCoord + y, zCoord + z) != null) {
                    handler = PowerHelper.isEnergyHandler(worldObj, xCoord + x, yCoord + y, zCoord + z);
                    facing = direction;
                }
            }
        }

        return new Object[] { handler, facing };
    }

    public static IEnergyHandler isEnergyHandler(World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof IEnergyHandler) return (IEnergyHandler) tile;

        return null;
    }

    public static ArrayList<Object[]> handlers = new ArrayList();
    static {
        handlers.add(new Object[] { -1, 0, 0, ForgeDirection.WEST });
        handlers.add(new Object[] { 0, 0, 1, ForgeDirection.SOUTH });
        handlers.add(new Object[] { 0, 1, 0, ForgeDirection.UP });
        handlers.add(new Object[] { 0, 0, -1, ForgeDirection.NORTH });
        handlers.add(new Object[] { 1, 0, 0, ForgeDirection.EAST });
        handlers.add(new Object[] { 0, -1, 0, ForgeDirection.DOWN });
    }
}
