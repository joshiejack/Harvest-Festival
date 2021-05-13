package uk.joshiejack.husbandry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import uk.joshiejack.husbandry.animals.stats.CapabilityStatsHandler;
import uk.joshiejack.husbandry.client.gui.GuiTracker;
import uk.joshiejack.husbandry.client.gui.pages.PageStats;
import uk.joshiejack.husbandry.client.renderer.entity.RenderDuck;
import uk.joshiejack.husbandry.entity.EntityDuck;
import uk.joshiejack.husbandry.item.HusbandryItems;
import uk.joshiejack.husbandry.item.ItemTool;
import uk.joshiejack.husbandry.world.HusbandryTrades;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.creativetab.PenguinTab;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
@Mod(modid = Husbandry.MODID, name = "Animal Husbandry", version = "@HUSBANDRY_VERSION@", dependencies = "required-after:penguinlib")
public class Husbandry implements IGuiHandler {
    public static final String MODID = "husbandry";
    @SuppressWarnings("ConstantConditions")
    public static final CreativeTabs TAB = new PenguinTab(MODID, () ->  HusbandryItems.TOOL.getStackFromEnum(ItemTool.Tool.MILKER));

    @Mod.Instance(MODID)
    public static Husbandry instance;
    public static Logger logger;

    @SidedProxy
    public static ServerProxy proxy;

    public static class ServerProxy { public void preInit() {} public void postInit() {}}
    @SideOnly(Side.CLIENT)
    public static class ClientProxy extends ServerProxy {
        @Override
        public void preInit() {
            RenderingRegistry.registerEntityRenderingHandler(EntityDuck.class, RenderDuck::new);
        }

        @Override
        public void postInit() {
            Page.REGISTRY.put("animal_stats", PageStats.INSTANCE);
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        CapabilityStatsHandler.register();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, instance);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        HusbandryItems.init();
        if (HusbandryConfig.enableTreatTrades) {
            VillagerRegistry.VillagerProfession farmer = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft:farmer"));
            if (farmer != null) { //2 = shepherd
                farmer.getCareer(2).addTrade(1, new HusbandryTrades.Generic()); //Tier 1 generic
                farmer.getCareer(2).addTrade(2, new HusbandryTrades.Special()); //Tier 2 special
            }
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return GuiTracker.INSTANCE;
    }
}
