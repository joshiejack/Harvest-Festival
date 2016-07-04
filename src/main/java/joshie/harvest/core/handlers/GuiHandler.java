package joshie.harvest.core.handlers;

import joshie.harvest.cooking.gui.ContainerFridge;
import joshie.harvest.cooking.gui.GuiFridge;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.npc.entity.AbstractEntityNPC;
import joshie.harvest.npc.gui.*;
import joshie.harvest.shops.gui.ContainerNPCBuilderShop;
import joshie.harvest.shops.gui.ContainerNPCShop;
import joshie.harvest.shops.gui.GuiNPCBuilderShop;
import joshie.harvest.shops.gui.GuiNPCShop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int NPC = 0;
    public static final int SHOP_MENU = 1;
    public static final int GIFT = 2;
    public static final int FRIDGE = 3;
    public static final int SHOP_BUILDER = 4;
    public static final int SHOP_OPTIONS = 5;
    public static final int SHOP_WELCOME = 6;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int entityID, int nextGui, int hand) {
        switch (ID) {
            case NPC:           return new ContainerNPCChat((AbstractEntityNPC) world.getEntityByID(entityID), player.inventory, nextGui);
            case SHOP_WELCOME:  return new ContainerNPCChat((AbstractEntityNPC) world.getEntityByID(entityID), player.inventory, SHOP_OPTIONS);
            case SHOP_MENU:     return new ContainerNPCShop((AbstractEntityNPC) world.getEntityByID(entityID), player.inventory);
            case SHOP_BUILDER:  return new ContainerNPCBuilderShop((AbstractEntityNPC) world.getEntityByID(entityID), player.inventory);
            case GIFT:          return new ContainerNPCGift((AbstractEntityNPC) world.getEntityByID(entityID), player.inventory, EnumHand.values()[hand]);
            case FRIDGE:        return new ContainerFridge(player.inventory, HFTrackers.getServerPlayerTracker(player).getFridge());
            case SHOP_OPTIONS:    {
                AbstractEntityNPC npc = (AbstractEntityNPC) world.getEntityByID(entityID);
                if (NPCHelper.isShopOpen(npc.getNPC(), world, player)) {
                    return new ContainerNPCSelect(npc, player.inventory);
                } else return new ContainerNPC(npc, player.inventory);
            }
            
            default:            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int entityID, int nextGui, int hand) {
        switch (ID) {
            case NPC:           return new GuiNPCChat((AbstractEntityNPC) world.getEntityByID(entityID), player, nextGui);
            case SHOP_WELCOME:  return new GuiNPCChat((AbstractEntityNPC) world.getEntityByID(entityID), player, SHOP_OPTIONS);
            case SHOP_MENU:     return new GuiNPCShop((AbstractEntityNPC) world.getEntityByID(entityID), player);
            case SHOP_BUILDER:  return new GuiNPCBuilderShop((AbstractEntityNPC) world.getEntityByID(entityID), player);
            case GIFT:          return new GuiNPCGift((AbstractEntityNPC) world.getEntityByID(entityID), player, EnumHand.values()[hand]);
            case FRIDGE:        return new GuiFridge(player.inventory, HFTrackers.getClientPlayerTracker().getFridge());
            case SHOP_OPTIONS:    {
                AbstractEntityNPC npc = (AbstractEntityNPC) world.getEntityByID(entityID);
                if (NPCHelper.isShopOpen(npc.getNPC(), world, player)) {
                    return new GuiNPCSelect(npc, player, nextGui);
                } else return new GuiNPCChat(npc, player, nextGui);
            }
            default:            return null;
        }
    }
}
