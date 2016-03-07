package joshie.harvest.blocks;

import static joshie.harvest.core.helpers.generic.RegistryHelper.registerTiles;
import static joshie.harvest.core.lib.HFModInfo.JAVAPATH;
import static net.minecraft.block.Block.soundTypeGrass;
import static net.minecraft.block.Block.soundTypeGravel;
import static net.minecraft.block.Block.soundTypeMetal;
import static net.minecraft.block.Block.soundTypePiston;
import static net.minecraft.block.Block.soundTypeWood;

import org.apache.commons.lang3.text.WordUtils;

import joshie.harvest.blocks.render.RenderCounter;
import joshie.harvest.blocks.render.RenderCrops;
import joshie.harvest.blocks.render.RenderFryingPan;
import joshie.harvest.blocks.render.RenderHandler;
import joshie.harvest.blocks.render.RenderPreview;
import joshie.harvest.blocks.render.SpecialRendererCounter;
import joshie.harvest.blocks.render.SpecialRendererFridge;
import joshie.harvest.blocks.render.SpecialRendererFryingPan;
import joshie.harvest.blocks.render.SpecialRendererMixer;
import joshie.harvest.blocks.render.SpecialRendererOven;
import joshie.harvest.blocks.render.SpecialRendererPot;
import joshie.harvest.blocks.tiles.TileCooking;
import joshie.harvest.blocks.tiles.TileCounter;
import joshie.harvest.blocks.tiles.TileFridge;
import joshie.harvest.blocks.tiles.TileFryingPan;
import joshie.harvest.blocks.tiles.TileMarker;
import joshie.harvest.blocks.tiles.TileMixer;
import joshie.harvest.blocks.tiles.TileOven;
import joshie.harvest.blocks.tiles.TilePot;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import joshie.harvest.core.util.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HFBlocks {
	
	public static int renderIDCTM;
	//Cooking & Farming
    public static Block cookware;
    public static Block crops;
    public static Block flowers;
    //Mine
    public static Block stone;
    public static Block dirt;
    //Misc
    public static Block woodmachines;
    public static Block preview;
    public static Block goddessWater;
    public static Fluid goddess;

    public static void preInit() {
    	
    	//Cooking & Farming
        cookware = new BlockCookware().setStepSound(soundTypeMetal).setBlockName("cookware");
        crops = new BlockCrop().setStepSound(soundTypeGrass).setBlockName("crops.block");
        flowers = new BlockFlower().setStepSound(soundTypeGrass).setBlockName("flowers.block");
        //Mine
        stone = new BlockStone().setStepSound(soundTypePiston).setBlockName("stone");
        dirt = new BlockDirt("hf", "ctm/dirt").setStepSound(soundTypeGravel).setBlockName("dirt");
        //Misc
        woodmachines = new BlockWood().setStepSound(soundTypeWood).setBlockName("general.block");
        preview = new BlockPreview().setBlockName("preview");
        goddess = new Fluid("hf_goddess_water").setRarity(EnumRarity.rare);
        FluidRegistry.registerFluid(goddess);
        goddessWater = new BlockGoddessWater(goddess).setBlockName("goddess.water");
        goddess.setBlock(goddessWater);

        registerTiles(HFModInfo.CAPNAME, TileCooking.class, TileFridge.class, TileFryingPan.class, TileCounter.class, TileMarker.class, 
                            TileMixer.class, TileOven.class, TilePot.class);
    }
    
    @SideOnly(Side.CLIENT)
    public static void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileCounter.class, new SpecialRendererCounter());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFridge.class, new SpecialRendererFridge());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFryingPan.class, new SpecialRendererFryingPan());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMixer.class, new SpecialRendererMixer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileOven.class, new SpecialRendererOven());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePot.class, new SpecialRendererPot());

        RenderingRegistry.registerBlockHandler(new RenderHandler());
        RenderingRegistry.registerBlockHandler(new RenderCrops());
        RenderHandler.register(HFBlocks.cookware, BlockCookware.COUNTER, RenderCounter.class);
        RenderHandler.register(HFBlocks.cookware, BlockCookware.FRIDGE, RenderCounter.class);
        RenderHandler.register(HFBlocks.cookware, BlockCookware.FRYING_PAN, RenderCounter.class);
        RenderHandler.register(HFBlocks.cookware, BlockCookware.MIXER, RenderCounter.class);
        RenderHandler.register(HFBlocks.cookware, BlockCookware.OVEN, RenderCounter.class);
        RenderHandler.register(HFBlocks.cookware, BlockCookware.POT, RenderFryingPan.class);
        registerRenders(HFBlocks.woodmachines);
        for (int i = 0; i < 8; i++) {
            RenderHandler.register(HFBlocks.preview, i, RenderPreview.class);
        renderIDCTM = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new CTMRenderer(renderIDCTM));
        }
    }
    
    @SideOnly(Side.CLIENT)
    private static void registerRenders(Block b) {
        BlockHFBaseMeta block = (BlockHFBaseMeta) b;
        ItemBlockBase item = (ItemBlockBase) Item.getItemFromBlock(block);
        for (int i = 0; i < block.getMetaCount(); i++) {
            try {
                String name = sanitizeGeneral(item.getName(new ItemStack(block, 1, i)));
                RenderHandler.register(block, i, Class.forName(JAVAPATH + "blocks.render.Render" + name));
            } catch (Exception e) {}
        }
    }

    @SideOnly(Side.CLIENT)
    private static String sanitizeGeneral(String name) {
        name = name.replace(".", " ");
        name = WordUtils.capitalize(name);
        return name.replace(" ", "");
    }
}
