package joshie.harvest.core.handlers;

import joshie.harvest.cooking.gui.ContainerFridge;
import joshie.harvest.cooking.gui.GuiCookbook;
import joshie.harvest.cooking.gui.GuiFridge;
import joshie.harvest.cooking.tile.TileFridge;
import joshie.harvest.npc.NPCHelper;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.gui.*;
import joshie.harvest.shops.gui.ContainerNPCShop;
import joshie.harvest.shops.gui.GuiNPCBuilderShop;
import joshie.harvest.shops.gui.GuiNPCShop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
    public static final int COOKBOOK = 7;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int entityID, int nextGui, int hand) {
        switch (ID) {
            case NPC: {
                EntityNPC npc = (EntityNPC) world.getEntityByID(entityID);
                if (hand != -1) return new ContainerNPCChat(player, npc, EnumHand.MAIN_HAND, nextGui);
                return new ContainerNPCChat(player, (EntityNPC) world.getEntityByID(entityID), EnumHand.MAIN_HAND, nextGui);
            }

            case SHOP_WELCOME:  return new ContainerNPCChat(player, (EntityNPC) world.getEntityByID(entityID), EnumHand.MAIN_HAND, SHOP_OPTIONS);
            case SHOP_OPTIONS:  return new ContainerNPCChat(player, (EntityNPC) world.getEntityByID(entityID), EnumHand.MAIN_HAND, nextGui);
            case SHOP_MENU:
            case SHOP_BUILDER:  return new ContainerNPCShop((EntityNPC) world.getEntityByID(entityID));
            case GIFT:          return new ContainerNPCGift(player, (EntityNPC) world.getEntityByID(entityID), EnumHand.values()[hand], -1);
            case FRIDGE:        return new ContainerFridge(player, player.inventory, (TileFridge) world.getTileEntity(new BlockPos(entityID, nextGui, hand)));
            default:            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int entityID, int nextGui, int hand) {
        switch (ID) {
            case NPC: {
                EntityNPC npc = (EntityNPC) world.getEntityByID(entityID);
                if (hand != -1) return new GuiNPCSelect(player, npc, nextGui, hand);
                return new GuiNPCChat(player, (EntityNPC) world.getEntityByID(entityID), EnumHand.MAIN_HAND, nextGui);
            }

            case SHOP_WELCOME:  return new GuiNPCChat(player, (EntityNPC) world.getEntityByID(entityID), EnumHand.MAIN_HAND, SHOP_OPTIONS);
            case SHOP_MENU:     return new GuiNPCShop(player, (EntityNPC) world.getEntityByID(entityID), -2);
            case SHOP_BUILDER:  return new GuiNPCBuilderShop(player, (EntityNPC) world.getEntityByID(entityID));
            case GIFT:          return new GuiNPCGift(player, (EntityNPC) world.getEntityByID(entityID), EnumHand.values()[hand]);
            case FRIDGE:        return new GuiFridge(player, player.inventory, (TileFridge) world.getTileEntity(new BlockPos(entityID, nextGui, hand)));
            case COOKBOOK:      return new GuiCookbook();
            case SHOP_OPTIONS:    {
                EntityNPC npc = (EntityNPC) world.getEntityByID(entityID);
                if (NPCHelper.isShopOpen(npc.getNPC(), world, player)) {
                    return new GuiNPCSelect(player, npc, nextGui, -1);
                } else return new GuiNPCChat(player, npc, EnumHand.MAIN_HAND, nextGui);
            }

            default:            return null;
        }
    }
}
