package joshie.harvest.core.helpers;

import java.util.UUID;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.entity.EntityNPCMiner;
import joshie.harvest.npc.entity.EntityNPCShopkeeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class NPCHelper {
    public static WorldLocation getHomeForEntity(EntityNPC entity) {
        UUID owner_uuid = entity.owning_player;
        INPC npc = entity.getNPC();
        if (npc.getHomeGroup() == null || npc.getHomeLocation() == null) return null;
        return TownHelper.getLocationFor(owner_uuid, npc.getHomeGroup(), npc.getHomeLocation());
    }
    
    public static EntityNPC getEntityForNPC(UUID owning_player, World world, INPC npc) {
        if (npc.isBuilder()) {
            return new EntityNPCBuilder(owning_player, world, npc);
        } else if (npc.isMiner()) {
            return new EntityNPCMiner(owning_player, world, npc);
        } else if (npc.getShop() != null) {
            return new EntityNPCShopkeeper(owning_player, world, npc);
        } else return new EntityNPC(owning_player, world, npc);
    }
    
    public static int getGuiIDForNPC(INPC npc, World world, EntityPlayer player, boolean isSneaking) {
        return npc.getShop() != null && npc.getShop().isOpen(world, player) && npc.getShop().getContents(player).size() > 0 ? (npc.isBuilder()? GuiHandler.SHOP_BUILDER: GuiHandler.SHOP) : (isSneaking) ? GuiHandler.GIFT : GuiHandler.NPC;
    }
    
    public static EntityNPCBuilder getBuilderForPlayer(World world, EntityPlayer player) {
        return world.isRemote ? PlayerHelper.getData().getBuilder(world) : getBuilderForPlayer(player);
    }

    public static EntityNPCBuilder getBuilderForPlayer(EntityLivingBase entityLiving) {      
        return PlayerHelper.getData((EntityPlayer)entityLiving).getBuilder(entityLiving.worldObj);
    }
}
