package harvestmoon.handlers;

import harvestmoon.blocks.TileFridge;
import harvestmoon.gui.ContainerCooking;
import harvestmoon.gui.GuiCooking;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {    
    public static final int COOKING = 0;
    
    //Returns the closest fridge, to the coordinates
    public TileFridge getClosestFridge(World world, int x, int y, int z) {
        for(int x2 = x - 3; x2 <= x + 3; x2++) {
            for(int z2 = z - 3; z2 <= z + 3; z2++) {
                TileEntity tile = world.getTileEntity(x2, y, z2);
                if(tile instanceof TileFridge) {
                    return (TileFridge)tile;
                }
            }
        }
        
        return null;
    }
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == COOKING) return new ContainerCooking(getClosestFridge(world, x, y, z), player.inventory);
        
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == COOKING) return new GuiCooking(getClosestFridge(world, x, y, z), player.inventory);
        
        return null;
    }
}
