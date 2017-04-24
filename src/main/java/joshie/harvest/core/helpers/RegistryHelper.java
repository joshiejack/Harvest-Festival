package joshie.harvest.core.helpers;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.DropHandler;
import joshie.harvest.api.crops.GrowthHandler;
import joshie.harvest.api.crops.IStateHandler;
import joshie.harvest.api.trees.Tree;
import joshie.harvest.core.base.render.FakeEntityRenderer;
import joshie.harvest.core.base.render.FakeEntityRenderer.EntityItemRenderer;
import joshie.harvest.core.handlers.DisableHandler;
import joshie.harvest.core.proxy.HFClientProxy;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.handlers.SeedRecipeHandler;
import joshie.harvest.crops.handlers.drop.DropHandlerTree;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Locale;

import static joshie.harvest.core.HFTab.FARMING;
import static joshie.harvest.core.handlers.DisableHandler.SEEDS_BLACKLIST;
import static joshie.harvest.core.lib.HFModInfo.*;
import static joshie.harvest.crops.HFCrops.DISABLE_VANILLA_SEEDS;
import static joshie.harvest.crops.HFCrops.DISABLE_VANILLA_WHEAT_SEEDS;

public class RegistryHelper {
    public static void registerSounds(String... sounds) {
        for (String sound : sounds) {
            ResourceLocation resource = new ResourceLocation(MODID, sound);
            GameRegistry.register(new SoundEvent(resource), resource);
        }
    }

    @SafeVarargs
    public static void registerTiles(Class<? extends TileEntity>... tiles) {
        for (Class<? extends TileEntity> tile : tiles) {
            GameRegistry.registerTileEntity(tile, MODID + ":" + tile.getSimpleName().replace("Tile", "").toLowerCase(Locale.ENGLISH));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerFluidBlockRendering(Block block, String name) {
        final ModelResourceLocation fluidLocation = new ModelResourceLocation(MODID + ":fluids", name);
        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return fluidLocation;
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public static void registerEntityRenderer(Item item, EntityItemRenderer instance) {
        HFClientProxy.RENDER_MAP.put(item, instance);
        ClientRegistry.bindTileEntitySpecialRenderer(instance.getClass(), FakeEntityRenderer.INSTANCE);
    }

    public static Crop registerCrop(String name) {
        return addHandlersToCrop(name, new Crop(new ResourceLocation(MODID, name)));
    }

    public static Tree registerTree(String name) {
        return addHandlersToCrop(name, (Tree) new Tree(new ResourceLocation(MODID, name)).setDropHandler(new DropHandlerTree()));
    }

    public static <C extends Crop> C addHandlersToCrop(String name, C crop) {
        //Atempt to add a drop handler
        try {
            DropHandler handler = (DropHandler) Class.forName(DROPHANDLERS + WordUtils.capitalizeFully(name.replace("_", " ")).replace(" ", "")).newInstance();
            if (handler != null) crop.setDropHandler(handler);
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {/**/}

        //Atempt to add a growth handler
        try {
            GrowthHandler handler = (GrowthHandler) Class.forName(GROWTHHANDLERS + WordUtils.capitalizeFully(name.replace("_", " ")).replace(" ", "")).newInstance();
            if (handler != null) crop.setGrowthHandler(handler);
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {/**/}

        //Atempt to add a state handler
        try {
            IStateHandler handler = (IStateHandler) Class.forName(CROPSTATES + WordUtils.capitalizeFully(name.replace("_", " ")).replace(" ", "")).newInstance();
            if (handler != null) crop.setStateHandler(handler);
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {/**/}

        //Atempt to add a rules handler
        try {
            ISpecialRules handler = (ISpecialRules) Class.forName(RULES + WordUtils.capitalizeFully(name.replace("_", " ")).replace(" ", "")).newInstance();
            if (handler != null) crop.setPurchaseRules(handler);
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {/**/}

        return crop;
    }

    private static void addSeeds(Crop crop, ItemStack seeds) {
        if (DISABLE_VANILLA_WHEAT_SEEDS || DISABLE_VANILLA_SEEDS) {
            SEEDS_BLACKLIST.register(seeds.getItem()); //Disable the item
        }

        //Add a bag > seed recipe
        if (crop.getCropStack(1).getItem() != seeds.getItem()) {
            GameRegistry.addRecipe(new SeedRecipeHandler(seeds, crop.getSeedStack(1)));
        }
    }

    public static void registerVanillaCrop(Block cropBlock, ItemStack item, ItemStack seeds, Crop crop) {
        addSeeds(crop, seeds);
        HFApi.crops.registerCropProvider(item, crop);
        crop.setSkipRender();
        item.getItem().setCreativeTab(FARMING);
        if (HFCrops.DISABLE_VANILLA_GROWTH || HFCrops.DISABLE_VANILLA_DROPS) DisableHandler.CROPS.add(cropBlock);
        if (HFCrops.DISABLE_VANILLA_GROWTH) {
            cropBlock.setTickRandomly(false);
        }
    }

    private static boolean isInDictionary(String name, ItemStack stack) {
        for (ItemStack check: OreDictionary.getOres(name)) {
            if (check.getItem() == stack.getItem() && (check.getItemDamage() == OreDictionary.WILDCARD_VALUE || check.getItemDamage() == stack.getItemDamage())) {
                return true;
            }
        }

        return false;
    }

    public static void registerOreIfNotExists(String name, ItemStack clone) {
        if (!isInDictionary(name, clone)) {
            OreDictionary.registerOre(name, clone);
        }
    }
}