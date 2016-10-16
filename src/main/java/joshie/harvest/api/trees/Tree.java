package joshie.harvest.api.trees;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.crops.DropHandler;
import joshie.harvest.api.crops.GrowthHandler;
import joshie.harvest.api.trees.ITreeRegistry.TreeStage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import org.apache.commons.lang3.StringUtils;

public class Tree extends IForgeRegistryEntry.Impl<Tree> implements IShippable {
    public static final IForgeRegistry<Tree> REGISTRY = new RegistryBuilder<Tree>().setName(new ResourceLocation("harvestfestival", "trees")).setType(Tree.class).setIDRange(0, 32000).create();
    public static final GrowthHandler SEASONAL = new GrowthHandler() {};
    public static final DropHandler DROPS = new DropHandler();
    public static final Tree NULL_CROP = new Tree();

    //Tree Data
    private final TObjectIntMap<TreeStage> stages;
    private ITreeStateHandler stateHandler;
    private GrowthHandler growthHandler;
    private DropHandler dropHandler;
    private AnimalFoodType foodType;
    private Ingredient ingredient;
    private boolean alternativeName;
    private int hunger;
    private float saturation;
    private ItemStack item;
    private Season[] seasons;
    private long cost;
    private long sell;
    private int year;
    private int bagColor;
    private boolean skipRender;
    private IBlockState wood;
    private IBlockState leaves;

    private Tree() {
        this(new ResourceLocation("harvestfestival", "null_tree"));
    }

    /** Constructor for tree **/
    public Tree (ResourceLocation key) {
        this.stages =  new TObjectIntHashMap<>();
        this.stages.put(TreeStage.SEED, 1);
        this.stages.put(TreeStage.SAPLING, 1);
        this.stages.put(TreeStage.JUVENILE, 1);
        this.stages.put(TreeStage.MATURE, 1);
        this.stages.put(TreeStage.HARVESTABLE, 0);
        this.seasons = new Season[] { Season.SPRING };
        this.foodType = AnimalFoodType.FRUIT;
        this.bagColor = 0xFFFFFF;
        this.growthHandler = SEASONAL;
        this.setRegistryName(key);
        REGISTRY.register(this);
    }

    /** Set how much this tree costs to buy and sell
     * @param cost the cost
     * @param sell the sell value**/
    public Tree setGoldValues(long cost, long sell) {
        this.cost = cost;
        this.sell = sell;
        return this;
    }

    /** Set the colour of the seeds
     *  @param color the color */
    public Tree setSeedColours(int color) {
        this.bagColor = color;
        return this;
    }

    /** Set the seasons this tree grows in **/
    public Tree setSeasons(Season... seasons) {
        this.seasons = seasons;
        return this;
    }

    /** Set the growth lengths for this tree
     * @param seed  the amount of time to be a seed
     * @param sapling the amount of time to be a sapling
     * @param juvenile the amount of time to be a juvenile
     * @param mature the amount of time as adult before being harvestable **/
    public Tree setGrowthLength(int seed, int sapling, int juvenile, int mature) {
        this.stages.put(TreeStage.SEED, seed);
        this.stages.put(TreeStage.SAPLING, sapling);
        this.stages.put(TreeStage.JUVENILE, juvenile);
        this.stages.put(TreeStage.MATURE, mature);
        return this;
    }

    /** Set the blocks used for wood and leaves **/
    public Tree setBlocks(IBlockState wood, IBlockState leaves) {
        this.wood = wood;
        this.leaves = leaves;
        return this;
    }

    /**
     * Set the year this year is unlocked for purchased
     **/
    public Tree setYearUnlocked(int year) {
        this.year = year;
        return this;
    }

    /**
     * If the tree/seeds should have different names set this to true
     **/
    public Tree setHasAlternativeName() {
        this.alternativeName = true;
        return this;
    }

    /**
     * Set the item this tree produces
     **/
    public Tree setItem(ItemStack item) {
        this.item = item;
        if (this.item.getItem() instanceof ItemFood) {
            ItemFood food = (ItemFood)this.item.getItem();
            setFoodStats(food.getHealAmount(item), food.getSaturationModifier(item));
        }

        return this;
    }

    /**
     * Set the state handler for this tree
     **/
    public Tree setStateHandler(ITreeStateHandler handler) {
        this.stateHandler = handler;
        return this;
    }

    /**
     * Set what this plant counts as for feeding animals
     * @param foodType the type of food
     **/
    public Tree setAnimalFoodType(AnimalFoodType foodType) {
        this.foodType = foodType;
        return this;
    }

    /**
     * Set the food stats for this crop
     * @param hunger    how much hunger to restore
     * @param saturation how much saturation to fill
     **/
    public Tree setFoodStats(int hunger, float saturation) {
        this.hunger = hunger;
        this.saturation = saturation;
        String name = getRegistryName().getResourcePath();
        if (Ingredient.INGREDIENTS.containsKey(name)) {
            this.ingredient = Ingredient.INGREDIENTS.get(name);
        } else this.ingredient = new Ingredient(name, hunger, saturation);
        return this;
    }

    /**
     * Set the ingredient this crop counts as
     **/
    public Tree setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    /**
     * Set the growth handler, to determine when this plant can grow/where
     * @param handler   the growth handler
     **/
    public Tree setGrowthHandler(GrowthHandler handler) {
        this.growthHandler = handler;
        return this;
    }

    /**
     * Set the drop handler for this crop
     * @param handler   the drop handler
     **/
    public Tree setDropHandler(DropHandler handler) {
        this.dropHandler = handler;
        return this;
    }

    /**
     * This crop doesn't need to load it's renders,
     * It's blocks will be tinted as well
     **/
    public Tree setSkipRender() {
        this.skipRender = true;
        return this;
    }

    /**
     * This is the seasons this tree survives in
     * @return the seasons that is valid for this tree
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
     * This is how much this tree well sell for at level 1.
     * If this tree cannot be sold return 0
     *
     * @param stack the crop
     * @return the sell value in gold
     */
    public long getSellValue(ItemStack stack) {
        return sell;
    }

    /**
     * Return how many stages this tree has.
     * A return value of 0, means the tree is instantly grown.
     *
     * @return the stage
     */
    public int getStages(TreeStage stage) {
        return stages.get(stage);
    }

    /**
     * The year in which this tree can be purchased
     **/
    public int getPurchaseYear() {
        return year;
    }

    /**
     * Return the growth handler for this crop
     * **/
    public GrowthHandler getGrowthHandler() {
        return growthHandler;
    }

    /**
     * This is called when bringing up the list of crops for sale
     *
     * @return whether this item can be purchased in this shop or not
     */
    public boolean canPurchase() {
        return getSeedCost() > 0;
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
     * The hunger this crop restores
     **/
    public int getHunger() {
        return hunger;
    }

    /**
     * The saturation this tree fills
     **/
    public float getSaturation() {
        return saturation;
    }

    /**
     * This tree as a seed stack
     **/
    public ItemStack getSeedStack(int amount) {
        return HFApi.trees.getSeedStack(this, amount);
    }

    /**
     *  This tree as a stack
     **/
    public ItemStack getTreeDrop(int amount) {
        if (item != null) {
            ItemStack copy = item.copy();
            copy.stackSize = amount;
            return item.copy();
        } else return HFApi.trees.getFruitStack(this, amount);
    }

    /**
     * What this tree drops
     **/
    public DropHandler getDropHandler() {
        return dropHandler == null ? DROPS : dropHandler;
    }

    /**
     * The state handler for tree
     **/
    public ITreeStateHandler getStateHandler() {
        return stateHandler;
    }

    /**
     * If the stack is this tree
     **/
    public boolean matches(ItemStack stack) {
        return HFApi.trees.getTreeFromStack(stack) == this;
    }

    /** Whether to skip the render loading of this crop **/
    public boolean skipLoadingRender() {
        return skipRender;
    }

    /**
     * Gets the localized crop name for this tree
     *
     * @param isItem the item
     * @return crop name
     */
    @SuppressWarnings("deprecation")
    public String getLocalizedName(boolean isItem) {
        String suffix = alternativeName ? ((isItem) ? ".item" : ".block") : "";
        return I18n.translateToLocal((getRegistryName().getResourceDomain() + ".tree." + StringUtils.replace(getRegistryName().getResourcePath(), "_", ".") + suffix));
    }

    /**
     * Gets the localized seed name for this crop
     *
     * @return seed name
     */
    @SuppressWarnings("deprecation")
    public String getSeedsName() {
        String suffix = alternativeName ? ".block" : "";
        String name = I18n.translateToLocalFormatted((getRegistryName().getResourceDomain() + ".tree." + StringUtils.replace(getRegistryName().getResourcePath(), "_", ".") + suffix));
        String seeds = I18n.translateToLocal("harvestfestival.crop.seeds");
        String format = I18n.translateToLocal("harvestfestival.crop.seeds.format");
        return String.format(format, name, seeds);
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Tree && getRegistryName().equals(((Tree) o).getRegistryName());
    }

    @Override
    public int hashCode() {
        return getRegistryName().hashCode();
    }
}