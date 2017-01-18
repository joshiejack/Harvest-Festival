package joshie.harvest.npcs;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.gift.IGiftHandler.Quality;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.npcs.entity.*;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.shops.HFShops;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.shops.gui.ShopSelection;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;

import static joshie.harvest.api.npc.NPC.Location.HOME;

public class NPCHelper {
    private static final HashMap<Shop, ShopSelection> selections = new HashMap<>();

    public static BlockPos getCoordinatesForLocation(EntityLivingBase entity, BuildingLocation location) {
        return TownHelper.getClosestTownToEntity(entity).getCoordinatesFor(location);
    }

    public static Selection getShopSelection(World worldObj, NPC npc) {
        Shop shop = npc.getShop(worldObj);
        ShopSelection selection = selections.get(shop);
        if (selection == null) {
            selection = new ShopSelection(shop);
            selections.put(shop, selection);
        }

        return selection;
    }

    public static BlockPos getHomeForEntity(EntityNPC entity) {
        NPC npc = entity.getNPC();
        if (npc.getLocation(HOME) == null) return null;
        return TownHelper.getClosestTownToEntity(entity).getCoordinatesFor(npc.getLocation(HOME));
    }

    @SuppressWarnings("unchecked")
    public static <N extends EntityNPC> N getEntityForNPC(World world, NPC npc) {
        if (npc == HFNPCs.TRADER) {
            return (N) new EntityNPCTrader(world, npc);
        } if (npc == HFNPCs.BUILDER) {
            return (N) new EntityNPCBuilder(world, npc);
        } else if (npc.isShopkeeper()) {
            return (N) new EntityNPCShopkeeper(world, npc);
        } if (npc == HFNPCs.GODDESS) {
            return (N) new EntityNPCGoddess(world, npc);
        } else return (N) new EntityNPCVillager(world, npc);
    }

    private static boolean canPlayerOpenShop(NPC npc, Shop shop, EntityPlayer player) {
        return (!player.worldObj.isRemote && HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getRelationships().hasMet(npc.getUUID()) || player.worldObj.isRemote) && (shop.canBuyFromShop(player) || shop.canSellToShop(player));
    }

    public static boolean isShopOpen(EntityNPC npc, World world, @Nullable EntityPlayer player) {
        Shop shop = npc.getNPC().getShop(world);
        if (shop != null && isShopOpen(world, npc, player, shop) && (player == null || (canPlayerOpenShop(npc.getNPC(), shop, player)))) {
            return (player != null && (shop.getContents().size() > 0)) || player == null;
        }

        return false;
    }

    public static boolean isShopPreparingToOpen(EntityNPC npc, World world) {
        Shop shop = npc.getNPC().getShop(world);
        return shop != null && shop.getOpeningHandler().isPreparingToOpen(world, npc, shop);
    }

    public static int getGuiIDForNPC(EntityNPC npc, World world, EntityPlayer player) {
        return !npc.isBusy() && isShopOpen(npc, world, player) ? GuiHandler.SHOP_WELCOME: GuiHandler.NPC;
    }

    public static Quality getGiftValue(NPC npc, ItemStack stack) {
        if (stack.getItem() == HFCooking.MEAL && !stack.hasTagCompound()) return Quality.DISLIKE;
        else return npc.getGiftValue(stack);
    }

    @SuppressWarnings("unchecked")
    public static boolean isShopOpen(World world, EntityAgeable npc, @Nullable EntityPlayer player, Shop shop) {
        if (HFShops.TWENTY_FOUR_HOUR_SHOPPING) return true;
        return shop.getOpeningHandler().isOpen(world, npc, player, shop);
    }
}