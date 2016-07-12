package joshie.harvest.core.helpers;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.entity.AbstractEntityNPC;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.entity.EntityNPCShopkeeper;
import joshie.harvest.npc.entity.EntityNPCVillager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.api.npc.INPC.Location.HOME;
import static joshie.harvest.api.npc.INPC.Location.WORK;

public class NPCHelper {
    public static BlockPos getCoordinatesForLocation(EntityLivingBase entity, BuildingLocation location) {
        return TownHelper.getClosestTownToEntityOrCreate(entity).getCoordinatesFor(location);
    }

    public static BlockPos getHomeForEntity(AbstractEntityNPC entity) {
        INPC npc = entity.getNPC();
        if (npc.getLocation(HOME) == null) return null;
        return TownHelper.getClosestTownToEntityOrCreate(entity).getCoordinatesFor(npc.getLocation(HOME));
    }

    public static BlockPos getWorkForEntity(AbstractEntityNPC entity) {
        INPC npc = entity.getNPC();
        if (npc.getLocation(WORK) == null) return null;
        return TownHelper.getClosestTownToEntityOrCreate(entity).getCoordinatesFor(npc.getLocation(WORK));
    }

    public static AbstractEntityNPC getEntityForNPC(World world, NPC npc) {
        if (npc.isBuilder()) {
            return new EntityNPCBuilder(world, npc);
        } else if (npc.getShop() != null) {
            return new EntityNPCShopkeeper(world, npc);
        } else return new EntityNPCVillager(world, npc);
    }

    public static boolean isShopOpen(INPC npc, World world, EntityPlayer player) {
        IShop shop = npc.getShop();
        if (shop != null && shop.isOpen(world, player) && shop.getContents(player).size() > 0) {
            return true;
        }

        return false;
    }

    public static int getGuiIDForNPC(INPC npc, World world, EntityPlayer player, boolean isGifting) {
        if (isShopOpen(npc, world, player)) {
            return GuiHandler.SHOP_WELCOME;
        }

        return isGifting ? GuiHandler.GIFT : GuiHandler.NPC;
    }
}