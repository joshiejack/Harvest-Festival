package joshie.harvest.api.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.core.HFRegistry;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

//TODO: Remove forge registry in 0.7+
//Do not call setRegistryName or anything
//This is only extending the old forge registry for 0.5 > 0.6 compatability reason
public class Crop extends HFRegistry<Crop> implements IPlantable {
    public static final Map<ResourceLocation, Crop> REGISTRY = new HashMap<>();
    private static final GrowthHandler SEASONAL = new GrowthHandler() {};
    private static final DropHandler DROPS = new DropHandler();
    private static final ISpecialRules RULES = (w, p, a) -> true;
    public static final Crop NULL_CROP = new Crop();

    //CropData
    private IStateHandler stateHandler;
    private GrowthHandler growthHandler;
    private DropHandler dropHandler;
    private ISpecialRules rules;
    private AnimalFoodType foodType;
    private EnumPlantType type;
    private Ingredient ingredient;
    private boolean needsWatering;
    private boolean alternativeName;
    private boolean requiresSickle;
    private ItemStack item;
    private Season[] seasons;
    private long cost;
    private long sell;
    private int stages;
    private int regrow;
    private int maxHarvests;
    private int bagColor;
    private int doubleStage;
    private int minCut;
    private boolean skipRender;

    private Crop() {
        this(new ResourceLocation("harvestfestival", "null_crop"));
    }

    public Crop(ResourceLocation key) {
        super(key);
        this.seasons = new Season[] { Season.SPRING };
        this.stages = 3;
        this.foodType = AnimalFoodType.VEGETABLE;
        this.bagColor = 0xFFFFFF;
        this.stateHandler = new StateHandlerDefault(this);
        this.dropHandler = Crop.DROPS;
        this.rules = Crop.RULES;
        this.growthHandler = SEASONAL;
        this.needsWatering = true;
        this.doubleStage = Integer.MAX_VALUE;
        this.type = EnumPlantType.Crop;
        this.maxHarvests = 1;
    }

    /** Set how much this tree costs to buy and sell
     * @param cost the cost
     * @param sell the sell value**/
    public Crop setValue(long cost, long sell) {
        this.cost = cost;
        this.sell = sell;
        return this;
    }

    /** Set the colour of the seeds
     *  @param color the color */
    public Crop setSeedColours(int color) {
        this.bagColor = color;
        return this;
    }

    /** Set the seasons this tree grows in **/
    public Crop setSeasons(Season... seasons) {
        this.seasons = seasons;
        return this;
    }

    /** Set the growth lengths for this tree
     * @param stages the number of stages **/
    public Crop setStages(int stages) {
        this.stages = stages;
        return this;
    }

    /**
     * Set the stage at which crops regrow to
     **/
    public Crop setRegrow(int regrow) {
        this.regrow = regrow;
        return this;
    }

    /**
     * Set the maximum amount of harvests this crop can have
     **/
    public Crop setMaxHarvests(int maxHarvests) {
        this.maxHarvests = maxHarvests;
        return this;
    }

    /**
     * If the crop/seeds should have different names set this to true
     **/
    public Crop setHasAlternativeName() {
        this.alternativeName = true;
        return this;
    }

    /** Set the item for this crop
     *  @param item     the item to set this as**/
    public Crop setItem(ItemStack item) {
        this.item = item;
        if (this.item != null && this.item.getItem() instanceof ItemFood) {
            ItemFood food = (ItemFood)this.item.getItem();
            setIngredient(food.getHealAmount(item), food.getSaturationModifier(item));
        }

        return this;
    }

    /** Overloaded mehtod for setItem
     *  @param item the item **/
    public Crop setItem(Item item) {
        setItem(new ItemStack(item));
        return this;
    }

    /**
     * Set the state handler for this crop
     **/
    public Crop setStateHandler(IStateHandler handler) {
        this.stateHandler = handler;
        return this;
    }

    /**
     * Creates a state handler based on the passed in values
     */
    public Crop setStages(int... stages) {
        this.stages = stages[stages.length - 1];
        this.stateHandler = new StateHandlerBasic(stages);
        return this;
    }

    /**
     * Creates a state handler for this block, based on the passed in values
     */
    public Crop setStages(Block block, int... stages) {
        this.stages = stages[stages.length - 1];
        this.stateHandler = new StateHandlerBlock(block, stages);
        return this;
    }

    /**
     * Make this crop require a sickle to be harvested
     * Sets the minimum cut
     * Used when a crop requires the sickle,
     *  The crop will only be destroyable/harvestable
     *  if it's below this stage
     */
    public Crop setRequiresSickle(int minCut) {
        if (minCut <= - 1) {
            this.requiresSickle = false;
            this.minCut = 0;
        } else {
            this.requiresSickle = true;
            this.minCut = minCut;
        }

        return this;
    }

    /**
     * Set what this plant counts as for feeding animals
     * @param foodType the type of food
     **/
    public Crop setAnimalFoodType(AnimalFoodType foodType) {
        this.foodType = foodType;
        return this;
    }

    /**
     * Set the plant type
     * @param plantType     the plant type of this crop
     **/
    public Crop setPlantType(EnumPlantType plantType) {
        this.type = plantType;
        return this;
    }

    /**
     * Set the ingredient stats for this crop
     **/
    public Crop setIngredient(int hunger, float saturation) {
        String name = getResource().getResourcePath();
        if (Ingredient.INGREDIENTS.containsKey(name)) {
            this.ingredient = Ingredient.INGREDIENTS.get(name);
        } else this.ingredient = new Ingredient(name, hunger, saturation);

        return this;
    }

    /**
     * Make this crop need zero water to grow
     **/
    public Crop setNoWaterRequirements() {
        this.needsWatering = false;
        return this;
    }

    /**
     * Make this crop need water to grow
     **/
    public Crop setWaterRequirements() {
        this.needsWatering = true;
        return this;
    }

    /**
     * Set the growth handler, to determine when this plant can grow/where
     * @param handler   the growth handler
     **/
    public Crop setGrowthHandler(GrowthHandler handler) {
        this.growthHandler = handler;
        return this;
    }

    /**
     * Set the stage at which this plant becomes two tall
     * @param doubleStage   the stage this plant becomes double
     **/
    public Crop setBecomesDouble(int doubleStage) {
        this.doubleStage = doubleStage;
        return this;
    }

    /**
     * Set the drop handler for this crop
     * @param handler   the drop handler
     **/
    public Crop setDropHandler(DropHandler handler) {
        this.dropHandler = handler;
        return this;
    }

    /**
     * Set the purchase rules for this crop
     * @param rules the rules to set
     * **/
    public Crop setPurchaseRules(ISpecialRules rules) {
        this.rules = rules;
        return this;
    }

    /**
     * This crop doesn't need to load it's renders,
     * It's blocks will be tinted as well
     **/
    public Crop setSkipRender() {
        this.skipRender = true;
        return this;
    }

    @Override
    public final Map<ResourceLocation, Crop> getRegistry() {
        return REGISTRY;
    }

    /**
     * This is the season this crop survives in
     *
     * @return the season that is valid for this crop
     */
    public Season[] getSeasons() {
        return seasons;
    }

    /**
     * This is how much the seed costs in the store.
     * If the seed isn't purchasable return 0
     *
     * @return the cost in gold
     */
    public long getSeedCost() {
        return cost;
    }

    /**
     * This is how much this crop well sell for at level 1.
     * If this crop cannot be sold return 0
     *
     * @return the sell value in gold
     */
    public long getSellValue() {
        return sell;
    }

    /**
     * Return how many stages this crop has.
     * A return value of 0, means the crop is instantly grown.
     *
     * @return the stage
     */
    public int getStages() {
        return stages;
    }

    /**
     * Return the stage that the plant returns to when it's harvested.
     * A return value of 0, means the crop is destroyed.
     *
     * @return the stage
     */
    public int getRegrowStage() {
        return regrow;
    }

    /**
     * This is the maximum amount of times this crop can be harvested
     * Once it's reached, the crop will be destroyed instead
     *
     * @return the max harvests
     */
    public int getMaxHarvests() {
        return maxHarvests;
    }

    /** Used when a crop requires the sickle,
     *  The crop will only be destroyable/harvestable
     *  if it's below this stage */
    public int getMinimumCut() {
        return minCut;
    }

    /**
     * Return true if this crop required a sickle
     * **/
    public boolean requiresSickle() {
        return requiresSickle;
    }

    /**
     * Return true if this crop requires water
     * **/
    public boolean requiresWater() {
        return needsWatering;
    }

    /**
     * Return true if this crop is turning double crop at this stage
     * @param stage the stage of the crop
     * **/
    public boolean isTurningToDouble(int stage) {
        return stage == doubleStage;
    }

    /**
     * Return true if this crop is a double crop at this stage
     * @param stage the stage of the crop
     * **/
    public boolean isCurrentlyDouble(int stage) {
        return stage >= doubleStage;
    }

    /**
     * Return the growth handler for this crop
     * **/
    public GrowthHandler getGrowthHandler() {
        return growthHandler;
    }

    /**
     * Return the colour of the seed bag
     **/
    public int getColor() {
        return bagColor;
    }

    /**
     * The type of animal food this is
     **/
    public AnimalFoodType getFoodType() {
        return foodType;
    }

    /**
     * The ingredient this crop represents
     **/
    public Ingredient getIngredient() {
        return ingredient;
    }

    /**
     * This crop as a seed stack
     **/
    public ItemStack getSeedStack(int amount) {
        return HFApi.crops.getSeedStack(this, amount);
    }

    /**
     *  This crop as a stack
     **/
    public ItemStack getCropStack(int amount) {
        if (item != null) {
            ItemStack copy = item.copy();
            copy.stackSize = amount;
            return copy;
        } else return new ItemStack(Items.BRICK);
    }

    /**
     * What this crop drops
     **/
    public DropHandler getDropHandler() {
        return dropHandler;
    }

    /**
     * What this crop rules for purchase are
     **/
    public ISpecialRules getRules() {
        return rules;
    }

    /**
     * The state handler for crop
     **/
    public IStateHandler getStateHandler() {
        return stateHandler;
    }

    /**
     * If the stack is this crop
     **/
    public boolean matches(ItemStack stack) {
        return HFApi.crops.getCropFromStack(stack) == this;
    }

    /**
     * The planty type of this crop
     **/
    public EnumPlantType getPlantType() {
        return type;
    }

    /** Whether to skip the render loading of this crop **/
    public boolean skipLoadingRender() {
        return skipRender;
    }

    /**
     * Gets the localized crop name for this crop
     *
     * @param isItem the item
     * @return crop name
     */
    @SuppressWarnings("deprecation")
    public String getLocalizedName(boolean isItem) {
        String suffix = alternativeName ? ((isItem) ? ".item" : ".block") : "";
        return I18n.translateToLocal((getResource().getResourceDomain() + ".crop." + StringUtils.replace(getResource().getResourcePath(), "_", ".") + suffix));
    }

    /**
     * Gets the localized seed name for this crop
     *
     * @return seed name
     */
    @SuppressWarnings("deprecation")
    public String getSeedsName() {
        String name = alternativeName ? I18n.translateToLocalFormatted((getResource().getResourceDomain() + ".crop." + StringUtils.replace(getResource().getResourcePath(), "_", ".") + ".block")) : item == null ? "NULL" : item.getDisplayName();
        String seeds = I18n.translateToLocal("harvestfestival.crop.seeds");
        String format = I18n.translateToLocal("harvestfestival.crop.seeds.format");
        return String.format(format, name, seeds);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return getPlantType();
    }

    @Override
    @SuppressWarnings("unchecked")
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return getStateHandler().getState(world, pos, PlantSection.BOTTOM, this, 1, false);
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Crop && getResource().equals(((Crop) o).getResource());
    }

    @Override
    public int hashCode() {
        return getResource().hashCode();
    }
}