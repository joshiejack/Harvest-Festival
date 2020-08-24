package joshie.harvest.npcs;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.INPCHelper;
import joshie.harvest.api.npc.ISchedule;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.RelationStatus;
import joshie.harvest.api.npc.gift.IGiftHandler.Quality;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.npc.schedule.ScheduleBuilder;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.npcs.entity.*;
import joshie.harvest.npcs.gift.GiftRegistry;
import joshie.harvest.npcs.schedule.Schedule;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.shops.HFShops;
import joshie.harvest.shops.gui.ShopSelection;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@HFApiImplementation
public class NPCHelper implements INPCHelper {
    public static final NPCHelper INSTANCE = new NPCHelper();
    private final GiftRegistry gifts = new GiftRegistry();

    @Override
    public GiftRegistry getGifts() {
        return gifts;
    }

    @Override
    @Nonnull
    public ItemStack getStackForNPC(NPC npc) {
        return HFNPCs.SPAWNER_NPC.getStackFromObject(npc);
    }

    @Override
    public String getRandomSpeech(NPC npc, String text, int maximumAlternatives, Object... data) {
        return TextHelper.getRandomSpeech(npc, text, maximumAlternatives, data);
    }

    @Override
    public ISchedule buildSchedule(ScheduleBuilder builder) {
        return new Schedule(builder);
    }

    @Override
    public void forceScriptOpen(EntityPlayer player, EntityAgeable npc, Script script) {
        player.openGui(HarvestFestival.instance, GuiHandler.FORCED_NPC, player.world, npc.getEntityId(), Script.REGISTRY.getValues().indexOf(script), -1);
    }

    public static BlockPos getCoordinatesForLocation(EntityNPCHuman npc, @Nonnull BuildingLocation location) {
        return npc.getHomeTown().getCoordinatesFor(location);
    }

    public static Selection getShopSelection(World world, BlockPos pos, NPC npc, EntityPlayer player) {
        return new ShopSelection(npc.getShop(world, pos, player), player);
    }

    public static BlockPos getHomeForEntity(EntityNPC entity) {
        NPC npc = entity.getNPC(); //Shorthand
        return npc.getHome() == null ? null : TownHelper.getClosestTownToEntity(entity, false).getCoordinatesFor(npc.getHome());
    }

    @SuppressWarnings("unchecked")
    public static <N extends EntityNPC> N getEntityForNPC(World world, NPC npc) {
        if (npc == HFNPCs.MINER) {
            return (N) new EntityNPCMiner(world, npc);
        }else if (npc == HFNPCs.CARPENTER) {
            return (N) new EntityNPCBuilder(world, npc);
        } else if (npc == HFNPCs.GODDESS) {
            return (N) new EntityNPCGoddess(world, npc);
        } else return (N) new EntityNPCVillager(world, npc);
    }

    @Nullable
    public static Entity getNPCIfExists(WorldServer server, BlockPos pos, NPC npc) {
        UUID uuid = TownHelper.getClosestTownToBlockPos(server, pos, false).getID();
        List<EntityNPC> npcs = EntityHelper.getEntities(EntityNPC.class, server, pos, 128D, 256D);
        for (EntityNPC entity: npcs) {
            if (entity.getNPC() == npc && (entity.getHome() != null && entity.getHome().equals(uuid))) return entity;
        }

        return npc == HFNPCs.CARPENTER ? server.getEntityFromUuid(uuid) : null;
    }

    private static boolean canPlayerOpenShop(NPC npc, Shop shop, @Nonnull EntityPlayer player) {
        return (!player.world.isRemote && HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getRelationships().isStatusMet(npc, RelationStatus.MET) || player.world.isRemote) && (shop.canBuyFromShop(player) || shop.canSellToShop(player));
    }

    public static boolean isShopOpen(EntityNPC npc, World world, @Nonnull EntityPlayer player) {
        Shop shop = npc.getNPC().getShop(world, new BlockPos(npc), player); //Grab the shop
        return (shop != null && isShopOpen(world, npc, player, shop) && canPlayerOpenShop(npc.getNPC(), shop, player)) && (shop.getContents().size() > 0);
    }

    public static int getGuiIDForNPC(EntityNPC npc, World world, @Nonnull EntityPlayer player) {
        return !npc.isBusy() && isShopOpen(npc, world, player) ? GuiHandler.SHOP_WELCOME: GuiHandler.NPC;
    }

    public static Quality getGiftValue(NPC npc, @Nonnull ItemStack stack) {
        if (stack.getItem() == HFCooking.MEAL && !stack.hasTagCompound()) return Quality.DISLIKE;
        else return npc.getGiftValue(stack);
    }

    @SuppressWarnings("unchecked")
    public static boolean isShopOpen(World world, EntityAgeable npc, @Nullable EntityPlayer player, Shop shop) {
        return HFShops.TWENTY_FOUR_HOUR_SHOPPING || shop.getOpeningHandler().isOpen(world, npc, player, shop);
    }

    @Nullable
    @SuppressWarnings("deprecation")
    public static NPC getNPCFromUUID(UUID uuid) {
        for (NPC npc: NPC.REGISTRY.values()) {
            if (npc.getUUID().equals(uuid)) return npc;
        }

        return null;
    }
}