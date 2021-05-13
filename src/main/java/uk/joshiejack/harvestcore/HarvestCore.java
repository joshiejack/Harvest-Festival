package uk.joshiejack.harvestcore;

import net.minecraft.block.BlockBeetroot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import uk.joshiejack.harvestcore.client.gui.GuiLetter;
import uk.joshiejack.harvestcore.client.gui.hud.MineHUDRender;
import uk.joshiejack.harvestcore.client.gui.page.PageCollection;
import uk.joshiejack.harvestcore.client.gui.page.PageCrafting;
import uk.joshiejack.harvestcore.client.gui.page.PageNotes;
import uk.joshiejack.harvestcore.database.Collections;
import uk.joshiejack.harvestcore.item.HCItems;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.harvestcore.registry.Note;
import uk.joshiejack.harvestcore.ticker.crop.CropTicker;
import uk.joshiejack.harvestcore.tile.TileMailbox;
import uk.joshiejack.harvestcore.tweaks.VanillaTweaks;
import uk.joshiejack.harvestcore.world.mine.Mine;
import uk.joshiejack.harvestcore.world.storage.loot.functions.SetCrop;
import uk.joshiejack.harvestcore.world.storage.loot.functions.SetQuality;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.seasons.client.renderer.HUDRenderer;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@Mod(modid = HarvestCore.MODID, name = "Harvest Core", version = "@HC_VERSION@", dependencies = "required-after:penguinlib;required-after:seasons")
public class HarvestCore implements IGuiHandler {
    public static final String MODID = "harvestcore";

    public static Logger logger;

    @SidedProxy
    public static ServerProxy proxy;

    public static class ServerProxy { public void postInit() {}}
    @SideOnly(Side.CLIENT)
    public static class ClientProxy extends ServerProxy {
        @Override
        public void postInit() {
            Note.CATEGORIES.keySet().forEach(category -> Page.REGISTRY.put("notes:" + category, new PageNotes(category, Note.ICONS.get(category))));
            Blueprint.CATEGORIES.keySet().forEach(category -> Page.REGISTRY.put("blueprints:" + category, new PageCrafting(category, Blueprint.ICONS.get(category))));
            Collections.ALL.values().forEach(collection -> Page.REGISTRY.put("collection:" + collection.getID(), new PageCollection(collection)));

            //Mine HUD
            MineHUDRender renderer = new MineHUDRender();
            Mine.BY_ID.forEach((i, map) -> HUDRenderer.RENDERERS.put(i, renderer));
        }
    }

    @Mod.Instance(HarvestCore.MODID)
    public static HarvestCore instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        CustomLoader.add(MODID);
        LootFunctionManager.registerFunction(new SetCrop.Serializer());
        LootFunctionManager.registerFunction(new SetQuality.Serializer());
        NetworkRegistry.INSTANCE.registerGuiHandler(HarvestCore.instance, this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        HCItems.init();
        VanillaTweaks.fuckWithItems();
        CropTicker.OVERRIDE_MAP.put(Blocks.BEETROOTS, (world, pos, state) -> {
            if (!world.isAreaLoaded(pos, 1))  return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            if (world.getLightFromNeighbors(pos.up()) >= 9) {
                int i = state.getValue(BlockBeetroot.BEETROOT_AGE);
                if (i < 3) {
                    float f = 1F;
                    if (ForgeHooks.onCropsGrowPre(world, pos, state, world.rand.nextInt((int) (25.0F / f) + 1) == 0)) {
                        world.setBlockState(pos, ((BlockBeetroot)Blocks.BEETROOTS).withAge(i + 1), 2);
                        ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
                    }
                }
            }
        });
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (HCConfig.disableVanillaVillages) {
            Biome.REGISTRY.forEach(BiomeManager::removeVillageBiome);
        }

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
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile instanceof TileMailbox) {
            return new GuiLetter();
        }

        return null;
    }
}
