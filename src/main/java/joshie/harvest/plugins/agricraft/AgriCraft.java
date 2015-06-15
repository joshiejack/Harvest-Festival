package joshie.harvest.plugins.agricraft;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getBoolean;

import java.util.ArrayList;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.crops.SoilHandlers;
import joshie.harvest.plugins.HFPlugins;
import joshie.harvest.plugins.HFPlugins.Plugin;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import com.InfinityRaider.AgriCraft.blocks.BlockCrop;

import cpw.mods.fml.common.registry.GameRegistry;

public class AgriCraft extends Plugin {
    public static ArrayList<AgriCraftCrop> cropsList = new ArrayList();
    public static boolean DISABLE_TICKING;

    public static BlockCrop blockCrops;
    public static Item crops;

    @Override
    public void loadConfig(Configuration config) {
        DISABLE_TICKING = getBoolean("Disable Agricraft Crop Ticking", true);
    }

    @Override
    public void preInit() {
        HFPlugins.AGRICRAFT_LOADED = true;
        addCrop("Cactus", 7, 1, 0x00B22D, Season.SUMMER).setSoilRequirements(SoilHandlers.sand).setNoWaterRequirements();

        addCrop("Sugarcane", 7, 1, 0x83B651, Season.SPRING);
        addCrop("Dandelion", 7, 1, 0xFFFF00, Season.SPRING);
        addCrop("Poppy", 7, 1, 0x8C0000, Season.SPRING);
        addCrop("Orchid", 7, 1, 0x00A3D9, Season.SPRING);
        addCrop("Allium", 7, 1, 0x69008C, Season.SPRING);
        addCrop("TulipRed", 7, 1, 0xFF2626, Season.SPRING);
        addCrop("TulipOrange", 7, 1, 0xFF8000, Season.SPRING);
        addCrop("TulipPink", 7, 1, 0xFF99FF, Season.SPRING);
        addCrop("TulipWhite", 7, 1, 0xFFFFFF, Season.SPRING);
        addCrop("Daisy", 7, 1, 0xFFFFBF, Season.SPRING);

        addCrop("ShroomRed", 7, 1, 0xB28500, Season.WINTER).setSoilRequirements(SoilHandlers.mushroom).setNoWaterRequirements();
        addCrop("ShroomBrown", 7, 1, 0xD90000, Season.WINTER).setSoilRequirements(SoilHandlers.mushroom).setNoWaterRequirements();
    }

    @Override
    public void init() {
        blockCrops = (BlockCrop) GameRegistry.findBlock("AgriCraft", "crops");
        crops = GameRegistry.findItem("AgriCraft", "cropsItem");

        if (DISABLE_TICKING) {
            GameRegistry.findBlock("AgriCraft", "crops").setTickRandomly(false);
            MinecraftForge.EVENT_BUS.register(this);
        }

        for (AgriCraftCrop crop : cropsList) {
            crop.loadItem();
        }
    }

    @Override
    public void postInit() {}

    private AgriCraftCrop addCrop(String unlocalized, int stages, int regrow, int color, Season... seasons) {
        AgriCraftCrop crop = (AgriCraftCrop) HFApi.CROPS.registerCrop(new AgriCraftCrop(unlocalized, stages, regrow, color, seasons));
        cropsList.add((AgriCraftCrop) crop);
        return crop;
    }
}
