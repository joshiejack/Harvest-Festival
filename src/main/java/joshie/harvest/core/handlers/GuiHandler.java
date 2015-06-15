package joshie.harvest.core.handlers;

import joshie.harvest.cooking.gui.ContainerFridge;
import joshie.harvest.cooking.gui.GuiFridge;
import joshie.harvest.core.helpers.PlayerHelper;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.gui.ContainerNPC;
import joshie.harvest.npc.gui.ContainerNPCBuilderShop;
import joshie.harvest.npc.gui.ContainerNPCGift;
import joshie.harvest.npc.gui.ContainerNPCShop;
import joshie.harvest.npc.gui.GuiNPC;
import joshie.harvest.npc.gui.GuiNPCBuilderShop;
import joshie.harvest.npc.gui.GuiNPCGift;
import joshie.harvest.npc.gui.GuiNPCShop;
import net.minecraft.entity.player.EntityPlayer;
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
