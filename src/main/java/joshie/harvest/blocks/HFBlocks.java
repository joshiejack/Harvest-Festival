package joshie.harvest.blocks;

import static joshie.harvest.core.helpers.generic.RegistryHelper.registerTiles;
import static net.minecraft.block.Block.soundTypeGrass;
import static net.minecraft.block.Block.soundTypeGravel;
import static net.minecraft.block.Block.soundTypeMetal;
import static net.minecraft.block.Block.soundTypePiston;
import static net.minecraft.block.Block.soundTypeWood;

import org.apache.commons.lang3.text.WordUtils;

import joshie.harvest.blocks.tiles.TileCooking;
import joshie.harvest.blocks.tiles.TileCounter;
import joshie.harvest.blocks.tiles.TileFridge;
import joshie.harvest.blocks.tiles.TileFryingPan;
import joshie.harvest.blocks.tiles.TileMarker;
import joshie.harvest.blocks.tiles.TileMixer;
import joshie.harvest.blocks.tiles.TileOven;
import joshie.harvest.blocks.tiles.TilePot;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HFBlocks {
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
        cookware = new BlockCookware().setStepSound(soundTypeMetal).setUnlocalizedName("cookware");
        crops = new BlockCrop().setStepSound(soundTypeGrass).setUnlocalizedName("crops.block");
        flowers = new BlockFlower().setStepSound(soundTypeGrass).setUnlocalizedName("flowers.block");
        //Mine
        stone = new BlockStone().setStepSound(soundTypePiston).setUnlocalizedName("stone");
        dirt = new BlockDirt("hf", "ctm/dirt").setStepSound(soundTypeGravel).setUnlocalizedName("dirt");
        //Misc
        woodmachines = new BlockWood().setStepSound(soundTypeWood).setUnlocalizedName("general.block");
        preview = new BlockPreview().setUnlocalizedName("preview");
        goddess = new Fluid("hf_goddess_water").setRarity(EnumRarity.RARE);
        FluidRegistry.registerFluid(goddess);
        goddessWater = new BlockGoddessWater(goddess).setUnlocalizedName("goddess.water");
        goddess.setBlock(goddessWater);

        registerTiles(HFModInfo.CAPNAME, TileCooking.class, TileFridge.class, TileFryingPan.class, TileCounter.class, TileMarker.class, 
                            TileMixer.class, TileOven.class, TilePot.class);
    }
    
    @SideOnly(Side.CLIENT)
    public static void initClient() {
        
    }
    
    @SideOnly(Side.CLIENT)
    private static void registerRenders(Block b) {
        
    }

    @SideOnly(Side.CLIENT)
    private static String sanitizeGeneral(String name) {
        name = name.replace(".", " ");
        name = WordUtils.capitalize(name);
        return name.replace(" ", "");
    }
}
