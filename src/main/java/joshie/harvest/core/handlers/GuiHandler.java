package joshie.harvest.core.handlers;

import joshie.harvest.cooking.gui.ContainerFridge;
import joshie.harvest.cooking.gui.GuiFridge;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.gui.ContainerNPC;
import joshie.harvest.npc.gui.ContainerNPCChat;
import joshie.harvest.npc.gui.ContainerNPCGift;
import joshie.harvest.npc.gui.ContainerNPCSelect;
import joshie.harvest.npc.gui.GuiNPCChat;
import joshie.harvest.npc.gui.GuiNPCGift;
import joshie.harvest.npc.gui.GuiNPCSelect;
import joshie.harvest.shops.gui.ContainerNPCBuilderShop;
import joshie.harvest.shops.gui.ContainerNPCShop;
import joshie.harvest.shops.gui.GuiNPCBuilderShop;
import joshie.harvest.shops.gui.GuiNPCShop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int NPC = 0;
    public static final int SHOP_MENU = 1;
    public static final int GIFT = 2;
    public static final int FRIDGE = 3;
    public static final int SHOP_BUILDER = 4;
    public static final int SHOP_OPTIONS = 5;
    public static final int SHOP_WELCOME = 6;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int entityID, int nextGui, int z) {
        switch (ID) {
            case NPC:           return new ContainerNPCChat((EntityNPC) world.getEntityByID(entityID), player.inventory, nextGui);
            case SHOP_WELCOME:  return new ContainerNPCChat((EntityNPC) world.getEntityByID(entityID), player.inventory, SHOP_OPTIONS);
            case SHOP_MENU:     return new ContainerNPCShop((EntityNPC) world.getEntityByID(entityID), player.inventory);
            case SHOP_BUILDER:  return new ContainerNPCBuilderShop((EntityNPC) world.getEntityByID(entityID), player.inventory);
            case GIFT:          return new ContainerNPCGift((EntityNPC) world.getEntityByID(entityID), player.inventory);
            case FRIDGE:        return new ContainerFridge(player.inventory, HFTrackers.getServerPlayerTracker(player).getFridge());
            case SHOP_OPTIONS:    {
                EntityNPC npc = (EntityNPC) world.getEntityByID(entityID);
                if (NPCHelper.isShopOpen(npc.getNPC(), world, player)) {
                    return new ContainerNPCSelect(npc, player.inventory);
                } else return new ContainerNPC(npc, player.inventory);
            }
            
            default:            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int entityID, int nextGui, int z) {       
        switch (ID) {
            case NPC:           return new GuiNPCChat((EntityNPC) world.getEntityByID(entityID), player, nextGui);
            case SHOP_WELCOME:  return new GuiNPCChat((EntityNPC) world.getEntityByID(entityID), player, SHOP_OPTIONS);
            case SHOP_MENU:     return new GuiNPCShop((EntityNPC) world.getEntityByID(entityID), player);
            case SHOP_BUILDER:  return new GuiNPCBuilderShop((EntityNPC) world.getEntityByID(entityID), player);
            case GIFT:          return new GuiNPCGift((EntityNPC) world.getEntityByID(entityID), player);
            case FRIDGE:        return new GuiFridge(player.inventory, HFTrackers.getClientPlayerTracker().getFridge());
            case SHOP_OPTIONS:    {
                EntityNPC npc = (EntityNPC) world.getEntityByID(entityID);
                if (NPCHelper.isShopOpen(npc.getNPC(), world, player)) {
                    return new GuiNPCSelect(npc, player, nextGui);
                } else return new GuiNPCChat(npc, player, nextGui);
            }
            default:            return null;
        }
    }
}
