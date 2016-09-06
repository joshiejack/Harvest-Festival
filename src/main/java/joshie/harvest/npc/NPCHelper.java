package joshie.harvest.npc;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.town.TownHelper;
import joshie.harvest.npc.entity.*;
import joshie.harvest.shops.Shop;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.api.npc.INPC.Location.HOME;
import static joshie.harvest.api.npc.INPC.Location.WORK;

public class NPCHelper {
    public static BlockPos getCoordinatesForLocation(EntityLivingBase entity, BuildingLocation location) {
        return TownHelper.getClosestTownToEntity(entity).getCoordinatesFor(location);
    }

    public static BlockPos getHomeForEntity(EntityNPC entity) {
        INPC npc = entity.getNPC();
        if (npc.getLocation(HOME) == null) return null;
        return TownHelper.getClosestTownToEntity(entity).getCoordinatesFor(npc.getLocation(HOME));
    }

    public static BlockPos getWorkForEntity(EntityNPC entity) {
        INPC npc = entity.getNPC();
        if (npc.getLocation(WORK) == null) return null;
        return TownHelper.getClosestTownToEntity(entity).getCoordinatesFor(npc.getLocation(WORK));
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

    public static boolean isShopOpen(NPC npc, World world, EntityPlayer player) {
        Shop shop = npc.getShop();
        if (shop != null && shop.isOpen(world, player) && shop.getContents(player).size() > 0) {
            return true;
        }

        return false;
    }

    public static int getGuiIDForNPC(NPC npc, World world, EntityPlayer player, boolean isGifting) {
        return isGifting? GuiHandler.GIFT : isShopOpen(npc, world, player) ? GuiHandler.SHOP_WELCOME: GuiHandler.NPC;
    }
}