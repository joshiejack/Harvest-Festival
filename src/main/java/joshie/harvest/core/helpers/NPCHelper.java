package joshie.harvest.core.helpers;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.entity.EntityNPCShopkeeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NPCHelper {
    public static BlockPos getHomeForEntity(EntityNPC entity) {
        INPC npc = entity.getNPC();
        if (npc.getHome() == null) return null;
        return TownHelper.getClosestTownToEntityOrCreate(entity).getCoordinatesFor(npc.getHome());
    }

    public static EntityNPC getEntityForNPC(World world, NPC npc) {
        if (npc.isBuilder()) {
            return new EntityNPCBuilder(world, npc);
        } else if (npc.getShop() != null) {
            return new EntityNPCShopkeeper(world, npc);
        } else return new EntityNPC(world, npc);
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