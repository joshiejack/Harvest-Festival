package uk.joshiejack.economy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import uk.joshiejack.economy.api.EconomyAPI;
import uk.joshiejack.economy.api.IEconomyAPI;
import uk.joshiejack.economy.api.gold.IVault;
import uk.joshiejack.economy.client.gui.GuiManager;
import uk.joshiejack.economy.client.gui.page.PageEconomyManager;
import uk.joshiejack.economy.gold.Bank;
import uk.joshiejack.economy.shipping.Market;
import uk.joshiejack.economy.world.storage.loot.functions.CapValue;
import uk.joshiejack.economy.world.storage.loot.functions.RatioValue;
import uk.joshiejack.economy.world.storage.loot.functions.SetValue;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
@Mod(modid = Economy.MODID, name = "Economy", version = "@ECONOMY_VERSION@", dependencies = "required-after:penguinlib")
public class Economy implements IEconomyAPI, IGuiHandler {
    public static final String MODID = "economy";

    @Mod.Instance(MODID)
    public static Economy instance;
    public static Logger logger;

    @SidedProxy
    public static ServerProxy proxy;

    public static class ServerProxy { public void postInit() {}}
    @SideOnly(Side.CLIENT)
    public static class ClientProxy extends ServerProxy {
        @Override
        public void postInit() {
            Page.REGISTRY.put("economy_manager", PageEconomyManager.INSTANCE);
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        EconomyAPI.instance = this;
        logger = event.getModLog();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, instance);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LootFunctionManager.registerFunction(new SetValue.Serializer());
        LootFunctionManager.registerFunction(new RatioValue.Serializer());
        LootFunctionManager.registerFunction(new CapValue.Serializer());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @SubscribeEvent
    public static void onPlayerJoinedWorld(PlayerEvent.PlayerLoggedInEvent event) {
        Bank.get(event.player.world).syncToPlayer(event.player);
        Market.get(event.player.world).getShippingForPlayer(event.player).syncToPlayer(event.player);
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiManager();
    }

    @Override
    public IVault getVaultForPlayer(World world, EntityPlayer player) {
        return Bank.get(world).getVaultForPlayer(player);
    }
}

