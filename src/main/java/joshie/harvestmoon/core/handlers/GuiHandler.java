package joshie.harvestmoon.core.handlers;

import joshie.harvestmoon.blocks.tiles.TileRuralChest;
import joshie.harvestmoon.core.gui.ContainerFridge;
import joshie.harvestmoon.core.gui.ContainerNPC;
import joshie.harvestmoon.core.gui.ContainerNPCBuilderShop;
import joshie.harvestmoon.core.gui.ContainerNPCGift;
import joshie.harvestmoon.core.gui.ContainerNPCShop;
import joshie.harvestmoon.core.gui.ContainerRuralChest;
import joshie.harvestmoon.core.gui.GuiNPCBuilderShop;
import joshie.harvestmoon.core.gui.GuiFridge;
import joshie.harvestmoon.core.gui.GuiNPC;
import joshie.harvestmoon.core.gui.GuiNPCGift;
import joshie.harvestmoon.core.gui.GuiNPCShop;
import joshie.harvestmoon.core.gui.GuiRuralChest;
import joshie.harvestmoon.core.helpers.PlayerHelper;
import joshie.harvestmoon.npc.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int NPC = 0;
    public static final int SHOP = 1;
    public static final int GIFT = 2;
    public static final int FRIDGE = 3;
    public static final int SHOP_BUILDER = 4;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null) {
            if (tile instanceof TileRuralChest)     return new ContainerRuralChest((TileRuralChest) tile, player.inventory);
        }

        switch (ID) {
            case NPC:           return new ContainerNPC((EntityNPC) world.getEntityByID(x), player.inventory);
            case SHOP:          return new ContainerNPCShop((EntityNPC) world.getEntityByID(x), player.inventory);
            case SHOP_BUILDER:  return new ContainerNPCBuilderShop((EntityNPC) world.getEntityByID(x), player.inventory);
            case GIFT:          return new ContainerNPCGift((EntityNPC) world.getEntityByID(x), player.inventory);
            case FRIDGE:        return new ContainerFridge(player.inventory, PlayerHelper.getFridge(player));
            default:            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null) {
            if (tile instanceof TileRuralChest)      return new GuiRuralChest(player.inventory, (TileRuralChest) tile);
        }
        
        switch (ID) {
            case NPC:           return new GuiNPC((EntityNPC) world.getEntityByID(x), player);
            case SHOP:          return new GuiNPCShop((EntityNPC) world.getEntityByID(x), player);
            case SHOP_BUILDER:  return new GuiNPCBuilderShop((EntityNPC) world.getEntityByID(x), player);
            case GIFT:          return new GuiNPCGift((EntityNPC) world.getEntityByID(x), player);
            case FRIDGE:        return new GuiFridge(player.inventory, PlayerHelper.getFridge(player));
            default:            return null;
        }
    }
}
