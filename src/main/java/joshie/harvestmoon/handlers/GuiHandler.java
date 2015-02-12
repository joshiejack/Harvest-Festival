package joshie.harvestmoon.handlers;

import joshie.harvestmoon.gui.ContainerNPC;
import joshie.harvestmoon.gui.ContainerNPCShop;
import joshie.harvestmoon.gui.GuiNPC;
import joshie.harvestmoon.gui.GuiNPCShop;
import joshie.harvestmoon.npc.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int NPC = 0;
    public static final int SHOP = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case NPC:       return new ContainerNPC((EntityNPC) world.getEntityByID(x), player.inventory);
            case SHOP:       return new ContainerNPCShop((EntityNPC) world.getEntityByID(x), player.inventory);
            default:        return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case NPC:       return new GuiNPC((EntityNPC) world.getEntityByID(x), player);
            case SHOP:       return new GuiNPCShop((EntityNPC) world.getEntityByID(x), player);
            default:        return null;
        }
    }
}
