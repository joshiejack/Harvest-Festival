package joshie.harvest.plugins.thaumcraft;

import static thaumcraft.api.aspects.Aspect.SLIME;
import static thaumcraft.api.aspects.Aspect.LIFE;
import static thaumcraft.api.aspects.Aspect.BEAST;
import static thaumcraft.api.aspects.Aspect.ELDRITCH;
import static thaumcraft.api.aspects.Aspect.EARTH;
import static thaumcraft.api.aspects.Aspect.AIR;
import static thaumcraft.api.aspects.Aspect.HEAL;
import static thaumcraft.api.aspects.Aspect.FLIGHT;
import static thaumcraft.api.aspects.Aspect.GREED;
import static thaumcraft.api.aspects.Aspect.PLANT;
import static thaumcraft.api.aspects.Aspect.METAL;
import static thaumcraft.api.aspects.Aspect.VOID;
import static thaumcraft.api.aspects.Aspect.EARTH;
import static thaumcraft.api.aspects.Aspect.CROP;
import static thaumcraft.api.aspects.Aspect.HUNGER;
import static thaumcraft.api.aspects.Aspect.ENTROPY;
import static thaumcraft.api.aspects.Aspect.EXCHANGE;
import static thaumcraft.api.aspects.Aspect.MAGIC;
import static thaumcraft.api.aspects.Aspect.WATER;
import static thaumcraft.api.aspects.Aspect.CLOTH;
import static thaumcraft.api.aspects.Aspect.HARVEST;
import static thaumcraft.api.aspects.Aspect.DEATH;
import static thaumcraft.api.aspects.Aspect.MINE;
import static thaumcraft.api.aspects.Aspect.CRAFT;
import static thaumcraft.api.aspects.Aspect.MECHANISM;
import static thaumcraft.api.aspects.Aspect.AURA;
import static thaumcraft.api.aspects.Aspect.SENSES;
import static thaumcraft.api.aspects.Aspect.TREE;
import static thaumcraft.api.aspects.Aspect.AURA;
import static thaumcraft.api.aspects.Aspect.AURA;
import static thaumcraft.api.aspects.Aspect.AURA;
import static thaumcraft.api.aspects.Aspect.AURA;
import static thaumcraft.api.aspects.Aspect.AURA;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.asm.overrides.ItemPamSeedFood;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.helpers.SizeableHelper;
import joshie.harvest.core.lib.SizeableMeta;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.items.HFItems;
import joshie.harvest.plugins.HFPlugins.Plugin;
import joshie.harvest.plugins.harvestcraft.HarvestCraftCrop;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class Thaumcraft extends Plugin {
	
    @Override
    public void preInit() {
    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {
    	///Blocks
    	//Cooking & Farming
        ThaumcraftApi.registerObjectTag(new ItemStack(HFBlocks.cookware, 0, OreDictionary.WILDCARD_VALUE), new AspectList().add(CRAFT, 4).add(MECHANISM, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFBlocks.crops, 0, OreDictionary.WILDCARD_VALUE), new AspectList().add(PLANT, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFBlocks.flowers, 0), new AspectList().add(PLANT, 1).add(AURA, 4).add(SENSES, 4));
        //Mine
        ThaumcraftApi.registerObjectTag(new ItemStack(HFBlocks.stone, 0, OreDictionary.WILDCARD_VALUE), new AspectList().add(EARTH, 2).add(ENTROPY, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFBlocks.dirt, 0, OreDictionary.WILDCARD_VALUE), new AspectList().add(EARTH, 2).add(ENTROPY, 2));
        //Misc
        ThaumcraftApi.registerObjectTag(new ItemStack(HFBlocks.woodmachines, 0, OreDictionary.WILDCARD_VALUE), new AspectList().add(TREE, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFBlocks.preview, 0, OreDictionary.WILDCARD_VALUE), new AspectList().add(TREE, 10).add(CRAFT, 8).add(EXCHANGE, 8));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFBlocks.goddessWater), new AspectList().add(WATER, 4).add(AURA, 1));
        
    	///Items
        
        //Sizeables
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.egg, 0, Size.SMALL.ordinal()), new AspectList().add(SLIME, 1).add(LIFE, 1).add(BEAST, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.egg, 0, Size.MEDIUM.ordinal()), new AspectList().add(SLIME, 2).add(LIFE, 2).add(BEAST, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.egg, 0, Size.LARGE.ordinal()), new AspectList().add(SLIME, 3).add(LIFE, 3).add(BEAST, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.milk, 0, Size.SMALL.ordinal()), new AspectList().add(WATER, 1).add(HUNGER, 1).add(HEAL, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.milk, 0, Size.MEDIUM.ordinal()), new AspectList().add(WATER, 2).add(HUNGER, 2).add(HEAL, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.milk, 0, Size.LARGE.ordinal()), new AspectList().add(WATER, 3).add(HUNGER, 3).add(HEAL, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.mayonnaise, 0, Size.SMALL.ordinal()), new AspectList().add(SLIME, 1).add(HUNGER, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.mayonnaise, 0, Size.MEDIUM.ordinal()), new AspectList().add(SLIME, 2).add(HUNGER, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.mayonnaise, 0, Size.LARGE.ordinal()), new AspectList().add(SLIME, 3).add(HUNGER, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.wool, 0, Size.SMALL.ordinal()), new AspectList().add(CLOTH, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.wool, 0, Size.MEDIUM.ordinal()), new AspectList().add(CLOTH, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.wool, 0, Size.LARGE.ordinal()), new AspectList().add(CLOTH, 3));
        //Tools
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hoe, 0, ToolTier.BASIC.ordinal()), new AspectList().add(EARTH, 2).add(HARVEST, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hoe, 0, ToolTier.COPPER.ordinal()), new AspectList().add(METAL, 2).add(HARVEST, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hoe, 0, ToolTier.SILVER.ordinal()), new AspectList().add(METAL, 2).add(GREED, 1).add(HARVEST, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hoe, 0, ToolTier.GOLD.ordinal()), new AspectList().add(METAL, 2).add(GREED, 2).add(HARVEST, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hoe, 0, ToolTier.MYSTRIL.ordinal()), new AspectList().add(METAL, 2).add(GREED, 3).add(HARVEST, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hoe, 0, ToolTier.CURSED.ordinal()), new AspectList().add(METAL, 2).add(DEATH, 4).add(HARVEST, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hoe, 0, ToolTier.BLESSED.ordinal()), new AspectList().add(METAL, 2).add(LIFE, 4).add(HARVEST, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hoe, 0, ToolTier.MYTHIC.ordinal()), new AspectList().add(METAL, 2).add(MAGIC, 5).add(HARVEST, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.sickle, 0, ToolTier.BASIC.ordinal()), new AspectList().add(EARTH, 2).add(HARVEST, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.sickle, 0, ToolTier.COPPER.ordinal()), new AspectList().add(METAL, 2).add(HARVEST, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.sickle, 0, ToolTier.SILVER.ordinal()), new AspectList().add(METAL, 2).add(GREED, 1).add(HARVEST, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.sickle, 0, ToolTier.GOLD.ordinal()), new AspectList().add(METAL, 2).add(GREED, 2).add(HARVEST, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.sickle, 0, ToolTier.MYSTRIL.ordinal()), new AspectList().add(METAL, 2).add(GREED, 3).add(HARVEST, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.sickle, 0, ToolTier.CURSED.ordinal()), new AspectList().add(METAL, 2).add(DEATH, 4).add(HARVEST, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.sickle, 0, ToolTier.BLESSED.ordinal()), new AspectList().add(METAL, 2).add(LIFE, 4).add(HARVEST, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.sickle, 0, ToolTier.MYTHIC.ordinal()), new AspectList().add(METAL, 2).add(MAGIC, 5).add(HARVEST, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.wateringcan, 0, ToolTier.BASIC.ordinal()), new AspectList().add(EARTH, 2).add(WATER, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.wateringcan, 0, ToolTier.COPPER.ordinal()), new AspectList().add(METAL, 2).add(WATER, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.wateringcan, 0, ToolTier.SILVER.ordinal()), new AspectList().add(METAL, 2).add(GREED, 1).add(WATER, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.wateringcan, 0, ToolTier.GOLD.ordinal()), new AspectList().add(METAL, 2).add(GREED, 2).add(WATER, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.wateringcan, 0, ToolTier.MYSTRIL.ordinal()), new AspectList().add(METAL, 2).add(GREED, 3).add(WATER, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.wateringcan, 0, ToolTier.CURSED.ordinal()), new AspectList().add(METAL, 2).add(DEATH, 4).add(WATER, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.wateringcan, 0, ToolTier.BLESSED.ordinal()), new AspectList().add(METAL, 2).add(LIFE, 4).add(WATER, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.wateringcan, 0, ToolTier.MYTHIC.ordinal()), new AspectList().add(METAL, 2).add(MAGIC, 5).add(WATER, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hammer, 0, ToolTier.BASIC.ordinal()), new AspectList().add(EARTH, 2).add(MINE, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hammer, 0, ToolTier.COPPER.ordinal()), new AspectList().add(METAL, 2).add(MINE, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hammer, 0, ToolTier.SILVER.ordinal()), new AspectList().add(METAL, 2).add(GREED, 1).add(MINE, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hammer, 0, ToolTier.GOLD.ordinal()), new AspectList().add(METAL, 2).add(GREED, 2).add(MINE, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hammer, 0, ToolTier.MYSTRIL.ordinal()), new AspectList().add(METAL, 2).add(GREED, 3).add(MINE, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hammer, 0, ToolTier.CURSED.ordinal()), new AspectList().add(METAL, 2).add(DEATH, 4).add(MINE, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hammer, 0, ToolTier.BLESSED.ordinal()), new AspectList().add(METAL, 2).add(LIFE, 4).add(MINE, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.hammer, 0, ToolTier.MYTHIC.ordinal()), new AspectList().add(METAL, 2).add(MAGIC, 5).add(MINE, 5));
        //Misc
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 0), new AspectList().add(FLIGHT, 2).add(AIR, 1).add(LIFE, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 1), new AspectList().add(METAL, 2).add(VOID, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 2), new AspectList().add(BEAST, 2).add(HEAL, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 3), new AspectList().add(BEAST, 2).add(HEAL, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 4), new AspectList().add(CROP, 2).add(HUNGER, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 5), new AspectList().add(EARTH, 1).add(ENTROPY, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 6), new AspectList().add(EARTH, 1).add(METAL, 1).add(EXCHANGE, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 7), new AspectList().add(EARTH, 1).add(METAL, 1).add(GREED, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 8), new AspectList().add(EARTH, 1).add(METAL, 1).add(GREED, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 9), new AspectList().add(EARTH, 1).add(METAL, 1).add(GREED, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 10), new AspectList().add(EARTH, 1).add(MAGIC, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 11), new AspectList().add(CROP, 2).add(HUNGER, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 12), new AspectList().add(WATER, 2).add(HUNGER, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 13), new AspectList().add(CROP, 4).add(HUNGER, 2)); //Remind : Needs to go after rice aspects
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 14), new AspectList().add(EARTH, 1).add(HUNGER, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.general, 0, 15), new AspectList().add(HEAL, 4).add(LIFE, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.meal), new AspectList().add(HEAL, 4).add(LIFE, 6)); //Remind: Cooking Ingredients, Burnt, Normal are differentiated
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.animal, 0, 0), new AspectList().add(BEAST, 3).add(EARTH, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.animal, 0, 1), new AspectList().add(BEAST, 2).add(EARTH, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.animal, 0, 2), new AspectList().add(AIR, 1).add(BEAST, 2).add(FLIGHT, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.treats, OreDictionary.WILDCARD_VALUE), new AspectList().add(HEAL, 2).add(GREED, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(HFItems.seeds, OreDictionary.WILDCARD_VALUE), new AspectList().add(PLANT, 1));
        
        ///Entity
        ThaumcraftApi.registerEntityTag("HarvestFestival.HarvestCow", new AspectList().add(BEAST, 3).add(EARTH, 3));
        ThaumcraftApi.registerEntityTag("HarvestFestival.HarvestSheep", new AspectList().add(BEAST, 2).add(EARTH, 2));
        ThaumcraftApi.registerEntityTag("HarvestFestival.HarvestChicken", new AspectList().add(AIR, 1).add(BEAST, 2).add(FLIGHT, 2));

    }
    public ItemStack getCropStack(ICrop crop) { ItemStack stack = crop.getCropStack().copy(); stack.setItemDamage(OreDictionary.WILDCARD_VALUE); return stack; }

}
