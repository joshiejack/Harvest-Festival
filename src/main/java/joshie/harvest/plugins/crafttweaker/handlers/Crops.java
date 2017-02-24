package joshie.harvest.plugins.crafttweaker.handlers;

import com.google.common.collect.ImmutableList;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.StateHandlerBlock;
import joshie.harvest.plugins.crafttweaker.CraftTweaker;
import joshie.harvest.plugins.crafttweaker.base.BaseCrop;
import joshie.harvest.plugins.crafttweaker.base.BaseOnce;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Locale;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.plugins.crafttweaker.CraftTweaker.asBlock;
import static joshie.harvest.plugins.crafttweaker.CraftTweaker.asStack;

@ZenClass("mods.harvestfestival.Crops")
public class Crops {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @ZenMethod
    @SuppressWarnings("unused")
    public static void addCrop(String name) {
        MineTweakerAPI.apply(new Add(name));
    }

    private static class Add extends BaseOnce {
        private final ResourceLocation resource;


        public Add(String name) {
            this.resource = new ResourceLocation(MODID, name);
        }

        @Override
        public String getDescription() {
            return "Added " + resource +  " as a harvest festival crop";
        }

        @Override
        public boolean isApplied() {
            return false;
        }

        @Override
        public void applyOnce() {
            new Crop(resource).setSkipRender();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setDrop(String name, IItemStack drop) {
        ItemStack stack = asStack(drop);
        if (stack == null) CraftTweaker.logError(String.format("Could not set the drop for %s as the stack item was null", name));
        else MineTweakerAPI.apply(new SetDrop(name, stack));
    }

    private static class SetDrop extends BaseCrop {
        private final ItemStack drop;

        public SetDrop(String name, ItemStack drop) {
            super(name);
            this.drop = drop;
        }

        @Override
        public String getDescription() {
            return "Setting drop for " + resource + " to " + drop.getDisplayName();
        }

        @Override
        protected void applyToCrop(Crop crop) {
            crop.setItem(drop);
            HFApi.crops.registerCropProvider(crop.getCropStack(1), crop);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @ZenMethod
    @SuppressWarnings("unused, deprecation")
    public static void setStages(String name, int[] stages, IItemStack[] blocks, int[] meta) {
        if (stages.length != meta.length || blocks.length != meta.length) CraftTweaker.logError(String.format("Could not set the stages for %s as the array lengths didn't match", name));
        else {
            IBlockState[] states = new IBlockState[stages.length];
            for (int i = 0; i < states.length; i++) {
                Block theBlock = asBlock(blocks[i]);
                if (theBlock == null) {
                    CraftTweaker.logError(String.format("Could not set the stages for %s as one of the items was not a block", name));
                    return; //Don't continue any further
                }

                states[i] = theBlock.getStateFromMeta(meta[i]);
            }

            MineTweakerAPI.apply(new SetStages(name, null, stages, states));
        }
    }

    @ZenMethod
    @SuppressWarnings("unused, deprecation")
    public static void setStages(String name, IItemStack block, int[] stages, int[] meta) {
        Block theBlock = asBlock(block);
        if (theBlock == null) CraftTweaker.logError(String.format("Could not set the stages for %s as the stack item was null or not a block", name));
        else if (stages.length != meta.length) CraftTweaker.logError(String.format("Could not set the stages for %s as the meta values didn't match the stage values", name));
        else {
            IBlockState[] states = new IBlockState[stages.length];
            for (int i = 0; i < states.length; i++) states[i] = theBlock.getStateFromMeta(meta[i]);
            MineTweakerAPI.apply(new SetStages(name, null, stages, states));
        }
    }


    @ZenMethod
    @SuppressWarnings("unused, deprecation")
    public static void setStages(String name, IItemStack block, int[] stages) {
        Block theBlock = asBlock(block);
        if (theBlock == null) CraftTweaker.logError(String.format("Could not set the stages for %s as the stack item was null or not a block", name));
        else MineTweakerAPI.apply(new SetStages(name, theBlock, stages, null));

    }

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setStages(String name, int[] stages) {
        MineTweakerAPI.apply(new SetStages(name, stages));
    }

    private static class SetStages extends BaseCrop {
        private final Block block;
        private final int[] stages;
        private final IBlockState[] states;

        public SetStages(String name, Block block, int[] stages, IBlockState[] states) {
            super(name);
            this.block = block;
            this.stages = stages;
            this.states = states;
        }

        public SetStages(String name, int[] stages) {
            super(name);
            this.block = null;
            this.stages = stages;
            this.states = null;
        }

        @Override
        public String getDescription() {
            return "Setting stages for " + resource + " to " + stages;
        }

        @Override
        protected void applyToCrop(Crop crop) {
            if (block == null) crop.setStages(stages);
            else {
                if (states != null) {
                    crop.setStages(stages[stages.length - 1]);
                    crop.setStateHandler(new StateHandlerBlockWithStates(stages, states));
                } else crop.setStages(block, stages);
            }
        }
    }

    private static class StateHandlerBlockWithStates extends StateHandlerBlock {
        private final IBlockState[] states;

        StateHandlerBlockWithStates(int[] values, IBlockState[] states) {
            super(null, values);
            this.states = states;
        }

        @Override
        public ImmutableList<IBlockState> getValidStates() {
            return ImmutableList.copyOf(states);
        }

        @Override
        @SuppressWarnings("deprecation")
        public IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, Crop crop, int stage, boolean withered) {
            for (int i = 0; i < values.length; i++) {
                if (stage <= values[i]) return states[i];
            }

            return states[states.length - 1];
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setSeasons(String name, String[] seasons) {
        MineTweakerAPI.apply(new SetSeason(name, seasons));
    }

    private static class SetSeason extends BaseCrop {
        private final Season[] seasons;

        public SetSeason(String name, String[] strings) {
            super(name);
            seasons = new Season[strings.length];
            for (int i = 0; i < seasons.length; i++) {
                seasons[i] = Season.valueOf(strings[i].toUpperCase(Locale.ENGLISH));
            }
        }

        @Override
        public String getDescription() {
            return "Setting seasons for " + resource + " to " + seasons;
        }

        @Override
        protected void applyToCrop(Crop crop) {
            crop.setSeasons(seasons);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setValue(String name, long cost, long sell) {
        MineTweakerAPI.apply(new SetValue(name, cost, sell));
    }

    private static class SetValue extends BaseCrop {
        private final long cost;
        private final long sell;

        public SetValue(String name, long cost, long sell) {
            super(name);
            this.cost = cost;
            this.sell = sell;
        }

        @Override
        public String getDescription() {
            return "Setting cost for " + resource + " to " + cost + " and sell value to " + sell;
        }

        @Override
        protected void applyToCrop(Crop crop) {
            crop.setValue(cost, sell);
            HFApi.shipping.registerSellable(crop.getCropStack(1), sell);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setSeedColor(String name, String hex) {
        MineTweakerAPI.apply(new SetColor(name, hex));
    }

    private static class SetColor extends BaseCrop {
        private final String hex;

        public SetColor(String name, String hex) {
            super(name);
            this.hex = hex.replace("#", "");
        }

        @Override
        public String getDescription() {
            return "Setting color for " + resource + " to " + hex;
        }

        @Override
        protected void applyToCrop(Crop crop) {
            crop.setSeedColours(Integer.parseInt(hex, 16));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setRegrow(String name, int stage) {
        MineTweakerAPI.apply(new SetRegrow(name, stage));
    }

    private static class SetRegrow extends BaseCrop {
        private final int regrow;

        public SetRegrow(String name, int stage) {
            super(name);
            this.regrow = stage;
        }

        @Override
        public String getDescription() {
            return "Setting regrow for " + resource + " to " + regrow;
        }

        @Override
        protected void applyToCrop(Crop crop) {
            crop.setRegrow(regrow);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setFoodType(String name, String type) {
        MineTweakerAPI.apply(new SetFoodType(name, type));
    }

    private static class SetFoodType extends BaseCrop {
        private final AnimalFoodType foodType;

        public SetFoodType(String name, String type) {
            super(name);
            foodType = AnimalFoodType.valueOf(type.toUpperCase(Locale.ENGLISH));
        }

        @Override
        public String getDescription() {
            return "Setting food type for " + resource + " to " + foodType;
        }

        @Override
        protected void applyToCrop(Crop crop) {
            crop.setAnimalFoodType(foodType);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setYearUnlocked(String name, int year) {
        MineTweakerAPI.apply(new SetUnlocked(name, year));
    }

    private static class SetUnlocked extends BaseCrop {
        private final int year;

        public SetUnlocked(String name, int year) {
            super(name);
            this.year = year;
        }

        @Override
        public String getDescription() {
            return "Setting year unlocked for " + resource + " to " + year;
        }

        @Override
        protected void applyToCrop(Crop crop) {
            crop.setUnlocked(year);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod //If you use a value that is -1 or lower, it removes the requirement
    @SuppressWarnings("unused")
    public static void setRequiresSickle(String name, int minCut) {
        MineTweakerAPI.apply(new SetSickle(name, minCut));
    }

    private static class SetSickle extends BaseCrop {
        private final int minCut;

        public SetSickle(String name, int minCut) {
            super(name);
            this.minCut = minCut;
        }

        @Override
        public String getDescription() {
            return "Setting sickle requirements for " + resource + " to " + minCut;
        }

        @Override
        protected void applyToCrop(Crop crop) {
            crop.setRequiresSickle(minCut);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setPlantType(String name, String type) {
        MineTweakerAPI.apply(new SetPlantType(name, type));
    }

    private static class SetPlantType extends BaseCrop {
        private final EnumPlantType plantType;

        public SetPlantType(String name, String type) {
            super(name);
            plantType = EnumPlantType.valueOf(type.toUpperCase(Locale.ENGLISH));
        }

        @Override
        public String getDescription() {
            return "Setting plant type for " + resource + " to " + plantType;
        }

        @Override
        protected void applyToCrop(Crop crop) {
            crop.setPlantType(plantType);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setWaterRequirements(String name, boolean value) {
        MineTweakerAPI.apply(new SetWaterRequirements(name, value));
    }

    private static class SetWaterRequirements extends BaseCrop {
        private final boolean value;

        public SetWaterRequirements(String name, boolean value) {
            super(name);
            this.value = value;
        }

        @Override
        public String getDescription() {
            return "Setting water requirement for " + resource + " to " + value;
        }

        @Override
        protected void applyToCrop(Crop crop) {
            if (value) crop.setWaterRequirements();
            else crop.setNoWaterRequirements();
        }
    }
}
