package joshie.harvestmoon.handlers;

import joshie.harvestmoon.blocks.TileFridge;
import joshie.harvestmoon.entities.EntityNPC;
import joshie.harvestmoon.gui.ContainerCooking;
import joshie.harvestmoon.gui.ContainerNPC;
import joshie.harvestmoon.gui.GuiCooking;
import joshie.harvestmoon.gui.GuiNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int COOKING = 0;
    public static final int NPC = 1;

    //Returns the closest fridge, to the coordinates
    public TileFridge getClosestFridge(World world, int x, int y, int z) {
        for (int x2 = x - 3; x2 <= x + 3; x2++) {
            for (int z2 = z - 3; z2 <= z + 3; z2++) {
                TileEntity tile = world.getTileEntity(x2, y, z2);
                if (tile instanceof TileFridge) {
                    return (TileFridge) tile;
                }
            }
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case COOKING:   return new ContainerCooking(getClosestFridge(world, x, y, z), player.inventory);
            case NPC:       return new ContainerNPC((EntityNPC) world.getEntityByID(x), player.inventory);
            default:        return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case COOKING:   return new GuiCooking(getClosestFridge(world, x, y, z), player.inventory);
            case NPC:       return new GuiNPC((EntityNPC) world.getEntityByID(x), player);
            default:        return null;
        }
    }
}
