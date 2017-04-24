package joshie.harvest.core.handlers;

import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.gui.ContainerFridge;
import joshie.harvest.cooking.gui.GuiCookbook;
import joshie.harvest.cooking.gui.GuiFridge;
import joshie.harvest.cooking.tile.TileFridge;
import joshie.harvest.core.base.gui.ContainerNull;
import joshie.harvest.core.gui.ContainerBasket;
import joshie.harvest.core.gui.GuiBasket;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.knowledge.gui.calendar.GuiCalendar;
import joshie.harvest.knowledge.gui.letter.GuiLetter;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.npcs.gui.*;
import joshie.harvest.quests.gui.GuiQuestBoard;
import joshie.harvest.shops.gui.ContainerNPCShop;
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
    public static final int STATS_BOOK = 4;
    public static final int SHOP_OPTIONS = 5;
    public static final int SHOP_WELCOME = 6;
    public static final int COOKBOOK = 7;
    public static final int NPC_INFO = 8;
    public static final int SHOP_MENU_SELL = 9;
    public static final int GIFT_GODDESS = 10;
    public static final int QUEST_BOARD = 11;
    public static final int FORCED_NPC = 12;
    public static final int MAILBOX = 13;
    public static final int SELECTION = 14;
    public static final int CALENDAR_GUI = 15;
    public static final int BASKET = 16;

    //Open no gui after this one
    public static final int NEXT_NONE = -1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int entityID, int nextGui, int hand) {

        switch (ID) {
            case SELECTION:
            case NPC:           return new ContainerNPCChat(player, (EntityNPC) world.getEntityByID(entityID), nextGui, false);
            case NPC_INFO:
            case SHOP_OPTIONS:  return new ContainerNPCChat(player, (EntityNPC) world.getEntityByID(entityID), nextGui, true);
            case SHOP_WELCOME:  return new ContainerNPCChat(player, (EntityNPC) world.getEntityByID(entityID), SHOP_OPTIONS, true);
            case SHOP_MENU:
            case SHOP_MENU_SELL:
                HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.SHOPPING);
                return new ContainerNPCShop(player, (EntityNPC) world.getEntityByID(entityID));
            case GIFT:          return new ContainerNPCGift(player, (EntityNPC) world.getEntityByID(entityID), EnumHand.values()[hand], nextGui);
            case FRIDGE:        return new ContainerFridge(player, player.inventory, (TileFridge) world.getTileEntity(new BlockPos(entityID, nextGui, hand)));
            case MAILBOX:
            case GIFT_GODDESS:
            case FORCED_NPC:
            case QUEST_BOARD:   return new ContainerNull();
            case BASKET:        return new ContainerBasket(player.inventory, player.getHeldItem(EnumHand.values()[hand]));
            default:            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int entityID, int nextGui, int hand) {
        switch (ID) {
            case FORCED_NPC: {
                return new GuiNPCMask(player, (EntityNPC) world.getEntityByID(entityID), nextGui);
            }
            case SELECTION:
            case NPC: {
                EntityNPC npc = (EntityNPC) world.getEntityByID(entityID);
                if (hand != -1) return new GuiNPCSelect(player, npc, nextGui, hand);
                return new GuiNPCChat(player, (EntityNPC) world.getEntityByID(entityID), nextGui, false, false);
            }
            case NPC_INFO: {
                EntityNPC npc = (EntityNPC) world.getEntityByID(entityID);
                if (hand != -1) return new GuiNPCSelect(player, npc, nextGui, hand);
                return new GuiNPCChat(player, (EntityNPC) world.getEntityByID(entityID), nextGui, true, true);
            }
            case SHOP_WELCOME:  return new GuiNPCChat(player, (EntityNPC) world.getEntityByID(entityID), SHOP_OPTIONS, false, true);
            case SHOP_MENU_SELL:
            case SHOP_MENU:
                HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.SHOPPING);
                return new GuiNPCShop(player, (EntityNPC) world.getEntityByID(entityID), NEXT_NONE, ID == SHOP_MENU_SELL);
            case GIFT:          return new GuiNPCGift(player, (EntityNPC) world.getEntityByID(entityID), EnumHand.values()[hand]);
            case GIFT_GODDESS:  return new GuiNPCGift(player, (EntityNPC) world.getEntityByID(entityID), GuiNPCGift.GODDESS_GIFT);
            case FRIDGE:        return new GuiFridge(player, player.inventory, (TileFridge) world.getTileEntity(new BlockPos(entityID, nextGui, hand)));
            case QUEST_BOARD:   return new GuiQuestBoard(new BlockPos(entityID, nextGui, hand), player);
            case MAILBOX:       return new GuiLetter(player);
            case COOKBOOK:      return new GuiCookbook();
            case STATS_BOOK:    return new GuiStats();
            case CALENDAR_GUI:      return new GuiCalendar(player);
            case SHOP_OPTIONS:    {
                EntityNPC npc = (EntityNPC) world.getEntityByID(entityID);
                if (NPCHelper.isShopOpen(npc, world, player)) {
                    return new GuiNPCSelect(player, npc, nextGui, NEXT_NONE);
                } else return new GuiNPCChat(player, npc, nextGui, false, true);
            }

            case BASKET:        return new GuiBasket(player.inventory, player.getHeldItem( EnumHand.values()[hand]));
            default:            return null;
        }
    }
}
