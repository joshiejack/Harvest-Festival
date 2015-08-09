package joshie.harvest.blocks;

import static joshie.harvest.core.helpers.generic.RegistryHelper.registerTiles;
import static joshie.harvest.core.lib.HFModInfo.JAVAPATH;
import static net.minecraft.block.Block.soundTypeGrass;
import static net.minecraft.block.Block.soundTypeGravel;
import static net.minecraft.block.Block.soundTypeMetal;
import static net.minecraft.block.Block.soundTypePiston;
import static net.minecraft.block.Block.soundTypeWood;
import joshie.harvest.blocks.render.RenderCrops;
import joshie.harvest.blocks.render.RenderFryingPan;
import joshie.harvest.blocks.render.RenderHandler;
import joshie.harvest.blocks.render.RenderKitchen;
import joshie.harvest.blocks.render.RenderPreview;
import joshie.harvest.blocks.render.SpecialRendererFryingPan;
import joshie.harvest.blocks.tiles.TileCooking;
import joshie.harvest.blocks.tiles.TileFridge;
import joshie.harvest.blocks.tiles.TileFryingPan;
import joshie.harvest.blocks.tiles.TileKitchen;
import joshie.harvest.blocks.tiles.TileMarker;
import joshie.harvest.blocks.tiles.TileMixer;
import joshie.harvest.blocks.tiles.TileOven;
import joshie.harvest.blocks.tiles.TilePot;
import joshie.harvest.blocks.tiles.TileSteamer;
import joshie.harvest.core.HFClientProxy;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import joshie.harvest.core.util.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.apache.commons.lang3.text.WordUtils;

import com.cricketcraft.ctmlib.CTMRenderer;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HFBlocks {
	public static int renderIDCTM;
    public static Block cookware;
    public static Block crops;
    public static Block dirt;
    public static Block flowers;
    public static Block preview;
    public static Block stone;
    public static Block woodmachines;
    public static Block goddessWater;
    
    public static Fluid goddess;

    public static void preInit() {
        crops = new BlockCrop().setStepSound(soundTypeGrass).setBlockName("crops.block");
        dirt = new BlockDirt("hf", "dirt").setStepSound(soundTypeGravel).setBlockName("dirt")/*, new SubmapManagerRCTM(4, "dirt", TextureType.R16));*/;
        flowers = new BlockFlower().setStepSound(soundTypeGrass).setBlockName("flowers.block");
        cookware = new BlockCookware().setStepSound(soundTypeMetal).setBlockName("cookware");
        woodmachines = new BlockWood().setStepSound(soundTypeWood).setBlockName("general.block");
        preview = new BlockPreview().setBlockName("preview");
        stone = new BlockStone().setStepSound(soundTypePiston).setBlockName("stone");
        goddess = new Fluid("hf_goddess_water").setRarity(EnumRarity.rare);
        FluidRegistry.registerFluid(goddess);
        goddessWater = new BlockGoddessWater(goddess).setBlockName("goddess.water");
        goddess.setBlock(goddessWater);

        registerTiles(HFModInfo.CAPNAME, TileCooking.class, TileFridge.class, TileFryingPan.class, TileKitchen.class, TileMarker.class, 
                            TileMixer.class, TileOven.class, TilePot.class, TileSteamer.class);
    }
    
    @SideOnly(Side.CLIENT)
    public static void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileFryingPan.class, new SpecialRendererFryingPan());
        RenderingRegistry.registerBlockHandler(new RenderHandler());
        RenderingRegistry.registerBlockHandler(new RenderCrops());
        RenderingRegistry.registerBlockHandler(new CTMRenderer(renderIDCTM));
    	renderIDCTM = RenderingRegistry.getNextAvailableRenderId();
        RenderHandler.register(HFBlocks.cookware, BlockCookware.KITCHEN, RenderKitchen.class);
        RenderHandler.register(HFBlocks.cookware, BlockCookware.FRYING_PAN, RenderFryingPan.class);
        registerRenders(HFBlocks.woodmachines);

        for (int i = 0; i < 8; i++) {
            RenderHandler.register(HFBlocks.preview, i, RenderPreview.class);
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
