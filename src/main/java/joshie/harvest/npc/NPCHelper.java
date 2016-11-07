package joshie.harvest.npc;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.npc.entity.*;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.shops.Shop;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static joshie.harvest.api.npc.INPC.Location.HOME;

public class NPCHelper {
    public static BlockPos getCoordinatesForLocation(EntityLivingBase entity, BuildingLocation location) {
        return TownHelper.getClosestTownToEntity(entity).getCoordinatesFor(location);
    }

    public static BlockPos getHomeForEntity(EntityNPC entity) {
        INPC npc = entity.getNPC();
        if (npc.getLocation(HOME) == null) return null;
        return TownHelper.getClosestTownToEntity(entity).getCoordinatesFor(npc.getLocation(HOME));
    }

    @SuppressWarnings("unchecked")
    public static <N extends EntityNPC> N getEntityForNPC(World world, NPC npc) {
        if (npc.isBuilder()) {
            return (N) new EntityNPCBuilder(world, npc);
        } else if (npc.getShop() != null) {
            return (N) new EntityNPCShopkeeper(world, npc);
        } if (npc == HFNPCs.GODDESS) {
            return (N) new EntityNPCGoddess(world, npc);
        } else return (N) new EntityNPCVillager(world, npc);
    }

    private static boolean canPlayerOpenShop(NPC npc, Shop shop, EntityPlayer player) {
        return (!player.worldObj.isRemote && HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getRelationships().hasMet(npc.getUUID()) || player.worldObj.isRemote) && (shop.canBuyFromShop(player) || shop.canSellToShop(player));
    }

    public static boolean isShopOpen(NPC npc, World world, @Nullable EntityPlayer player) {
        Shop shop = npc.getShop();
        if (shop != null && shop.isOpen(world, player) && (player == null || (canPlayerOpenShop(npc, shop, player)))) {
            return (player != null && (shop.getContents().size() > 0)) || player == null;
        }

        return false;
    }

    public static boolean isShopPreparingToOpen(NPC npc, World world) {
        Shop shop = npc.getShop();
        return shop != null && shop.isPreparingToOpen(world);
    }

    public static int getGuiIDForNPC(EntityNPC npc, World world, EntityPlayer player) {
        return !npc.isBusy() && isShopOpen(npc.getNPC(), world, player) ? GuiHandler.SHOP_WELCOME: GuiHandler.NPC;
    }
}