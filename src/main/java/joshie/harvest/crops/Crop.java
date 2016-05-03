package joshie.harvest.crops;

import com.google.gson.annotations.Expose;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.IDropHandler;
import joshie.harvest.api.crops.ISoilHandler;
import joshie.harvest.api.crops.IStateHandler;
import joshie.harvest.core.config.HFConfig;
import joshie.harvest.core.helpers.SeedHelper;
import joshie.harvest.core.util.Translate;
import joshie.harvest.crops.handlers.SoilHandlers;
import joshie.harvest.crops.handlers.StateHandlerDefault;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Random;

public class Crop implements ICrop {
    public static final ArrayList<ICrop> CROPS = new ArrayList<ICrop>(30);
    private static final Random rand = new Random();

    //CropData
    @SideOnly(Side.CLIENT)
    protected IStateHandler iconHandler;
    @Expose
    protected String unlocalized = "null_crop";

    protected ISoilHandler soilHandler;
    protected IDropHandler dropHandler;
    protected Block growsToSide;
    protected boolean needsWatering;
    protected boolean alternativeName;
    protected boolean requiresSickle;
    protected ItemStack item;
    protected Season[] seasons;
    protected int cost;
    protected int sell;
    protected int stages;
    protected int regrow;
    protected int year;
    protected int bag_color;
    protected int doubleStage;
    protected AnimalFoodType foodType;

    public Crop() {}

    /**
     * Constructor for crop
     *
     * @param unlocalized name, works as the uuid
     * @param season      the season this crop grows in
     * @param cost        how much the seed costs
     * @param sell        how much this sells for
     * @param stages      how many stages this crop has
     * @param regrow      the stage this returns to once harvested
     * @param year        the year in which this crop can be purchased
     */
    public Crop(String unlocalized, Season season, int cost, int sell, int stages, int regrow, int year, int color) {
        this(unlocalized, new Season[]{season}, cost, sell, stages, regrow, year, color);
    }

    public Crop(String unlocalized, Season[] seasons, int cost, int sell, int stages, int regrow, int year, int color) {
        this.unlocalized = unlocalized;
        this.seasons = seasons;
        this.cost = cost;
        this.sell = sell;
        this.stages = stages;
        this.regrow = regrow;
        this.year = year;
        this.alternativeName = false;
        this.foodType = AnimalFoodType.VEGETABLE;
        this.bag_color = color;
        this.iconHandler = new StateHandlerDefault(this);
        this.soilHandler = SoilHandlers.farmland;
        this.needsWatering = true;
        this.doubleStage = Integer.MAX_VALUE;
        this.dropHandler = null;
        this.growsToSide = null;
        HFConfig.mappings.addCrop(this);
        CROPS.add(this);
    }

    @Override
    public ICrop setHasAlternativeName() {
        this.alternativeName = true;
        return this;
    }

    @Override
    public ICrop setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    @Override
    public ICrop setStateHandler(IStateHandler handler) {
        this.iconHandler = handler;
        return this;
    }

    @Override
    public ICrop setRequiresSickle() {
        this.requiresSickle = true;
        return this;
    }

    @Override
    public ICrop setAnimalFoodType(AnimalFoodType foodType) {
        this.foodType = foodType;
        return this;
    }

    @Override
    public ICrop setNoWaterRequirements() {
        this.needsWatering = false;
        return this;
    }

    @Override
    public ICrop setSoilRequirements(ISoilHandler handler) {
        this.soilHandler = handler;
        return this;
    }

    @Override
    public ICrop setBecomesDouble(int doubleStage) {
        this.doubleStage = doubleStage;
        return this;
    }

    @Override
    public ICrop setDropHandler(IDropHandler handler) {
        this.dropHandler = handler;
        return this;
    }

    @Override
    public ICrop setGrowsToSide(Block block) {
        this.growsToSide = block;
        return this;
    }


    /**
     * Return true if this crop uses an item other than HFCrop item
     **/
    public boolean hasItemAssigned() {
        return item != null;
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
     * The year in which this crop can be purchased
     **/
    public int getPurchaseYear() {
        return year;
    }

    /**
     * Return the stage that the plant returns to when it's harvested.
     * A return value of 0, means the crop is destroyed.
     *
     * @return the stage
     */
    @Override
    public int getRegrowStage() {
        return regrow;
    }

    @Override
    public boolean requiresSickle() {
        return requiresSickle;
    }

    @Override
    public boolean requiresWater() {
        return needsWatering;
    }

    @Override
    public boolean isDouble(int stage) {
        return stage == doubleStage;
    }

    @Override
    public Block growsToSide() {
        return growsToSide;
    }

    @Override
    public ISoilHandler getSoilHandler() {
        return soilHandler;
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
        return bag_color;
    }

    /**
     * The type of animal food this is
     **/
    public AnimalFoodType getFoodType() {
        return foodType;
    }

    @Override
    public ItemStack getSeedStack() {
        return SeedHelper.getSeedsFromCrop(this);
    }

    @Override
    public ItemStack getCropStack() {
        return item == null ? null : item.copy();
    }

    @Override
    public ItemStack getHarvested() {
        return dropHandler == null ? getCropStack() : dropHandler.getDrop(rand, item.getItem());
    }

    @Override
    public IStateHandler getStateHandler() {
        return iconHandler;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return (stack.getItem() == getCropStack().getItem());
    }

    /**
     * Gets the unlocalized name for this crop
     *
     * @return unlocalized name
     */
    @Override
    public String getUnlocalizedName() {
        return unlocalized;
    }

    /**
     * Gets the localized crop name for this crop
     *
     * @param isItem the item
     * @return crop name
     */
    public String getLocalizedName(boolean isItem) {
        String suffix = alternativeName ? ((isItem) ? ".item" : ".block") : "";
        return Translate.translate("crop." + StringUtils.replace(getUnlocalizedName(), "_", ".") + suffix);
    }

    /**
     * Gets the localized seed name for this crop
     *
     * @return seed name
     */
    public String getSeedsName() {
        String suffix = alternativeName ? ".block" : "";
        String name = Translate.translate("crop." + StringUtils.replace(getUnlocalizedName(), "_", ".") + suffix);
        String seeds = Translate.translate("crop.seeds");
        String text = Translate.translate("crop.seeds.format.standard");
        text = StringUtils.replace(text, "%C", name);
        text = StringUtils.replace(text, "%S", seeds);
        return text;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof ICrop && getUnlocalizedName().equals(((ICrop) o).getUnlocalizedName());
    }

    @Override
    public int hashCode() {
        return unlocalized.hashCode();
    }
}