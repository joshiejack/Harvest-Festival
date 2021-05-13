package uk.joshiejack.horticulture;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.horticulture.client.renderer.block.statemap.StateMapperLeaves;
import uk.joshiejack.horticulture.client.renderer.block.statemap.StateMapperStump;
import uk.joshiejack.horticulture.item.HorticultureItems;
import uk.joshiejack.horticulture.item.ItemCrop;
import uk.joshiejack.horticulture.world.FarmerTrades;
import uk.joshiejack.horticulture.world.FruitTreeGenerator;
import uk.joshiejack.penguinlib.creativetab.PenguinTab;

import static uk.joshiejack.horticulture.Horticulture.MODID;

@Mod.EventBusSubscriber
@Mod(modid = MODID, name = "Horticulture", version = "@HORTICULTURE_VERSION@", dependencies = "required-after:penguinlib")
public class Horticulture {
    public static final String MODID = "horticulture";
    @SuppressWarnings("ConstantConditions")
    public static final CreativeTabs TAB = new PenguinTab(MODID, () -> HorticultureItems.CROP.getStackFromEnum(ItemCrop.Crops.STRAWBERRY, 1));

    @SidedProxy
    public static ServerProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    public static class ServerProxy { public void preInit() {} }

    @SuppressWarnings("unused")
    @SideOnly(Side.CLIENT)
    public static class ClientProxy extends ServerProxy {
        @Override
        public void preInit() {
            MinecraftForge.EVENT_BUS.register(new StateMapperLeaves());
            MinecraftForge.EVENT_BUS.register(new StateMapperStump());
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        HorticultureItems.init();
        if (HorticultureConfig.enableGrassDrops) {
            MinecraftForge.addGrassSeed(HorticultureItems.SEEDS.getStackFromEnum(ItemCrop.Crops.TURNIP, 1), 6);
            MinecraftForge.addGrassSeed(HorticultureItems.SEEDS.getStackFromEnum(ItemCrop.Crops.CUCUMBER, 1), 3);
            MinecraftForge.addGrassSeed(HorticultureItems.SEEDS.getStackFromEnum(ItemCrop.Crops.ONION, 1), 6);
            MinecraftForge.addGrassSeed(HorticultureItems.SEEDS.getStackFromEnum(ItemCrop.Crops.TOMATO, 1), 3);
            MinecraftForge.addGrassSeed(HorticultureItems.SEEDS.getStackFromEnum(ItemCrop.Crops.EGGPLANT, 1), 3);
            MinecraftForge.addGrassSeed(HorticultureItems.SEEDS.getStackFromEnum(ItemCrop.Crops.SPINACH, 1), 6);
        }

        if (HorticultureConfig.enableVillagerTrades) {
            VillagerRegistry.VillagerProfession farmer = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft:farmer"));
            if (farmer != null) {
                farmer.getCareer(0).addTrade(1, new FarmerTrades());
            }
        }

        if (HorticultureConfig.enableAppleTrees || HorticultureConfig.enableBananaTrees) {
            GameRegistry.registerWorldGenerator(new FruitTreeGenerator(), 0);
        }
    }
}
