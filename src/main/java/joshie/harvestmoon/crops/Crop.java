package joshie.harvestmoon.crops;

import java.util.ArrayList;

import joshie.harvestmoon.animals.AnimalType.FoodType;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropRenderHandler;
import joshie.harvestmoon.api.crops.ISoilHandler;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.core.util.Translate;
import joshie.harvestmoon.crops.icons.IconHandlerDefault;
import joshie.harvestmoon.init.HMConfiguration;
import joshie.harvestmoon.init.HMItems;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.annotations.Expose;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Crop implements ICrop {
    public static final ArrayList<Crop> crops = new ArrayList(30);

    //CropData
    @SideOnly(Side.CLIENT)
    protected ICropRenderHandler iconHandler;
    @Expose
    protected String unlocalized;

    protected ISoilHandler soilHandler;
    protected boolean needsWatering;
    protected boolean alternativeName;
    protected boolean isStatic;
    protected boolean requiresSickle;
    protected boolean isEdible;
    protected Item item;
    protected Season[] seasons;
    protected int cost;
    protected int sell;
    protected int stages;
    protected int regrow;
    protected int year;
    protected int bag_color;
    protected FoodType foodType;
    protected int ordinal;

    public Crop() {}

    /** Constructor for crop
     * @param unlocalized name, works as the uuid
     * @param season the season this crop grows in
     * @param cost how much the seed costs
     * @param sell how much this sells for
     * @param stages how many stages this crop has
     * @param regrow the stage this returns to once harvested
     * @param year the year in which this crop can be purchased
     * @param meta the crop meta value for dropping the correct item  */
    public Crop(String unlocalized, Season season, int cost, int sell, int stages, int regrow, int year, int color) {
        this(unlocalized, new Season[] { season }, cost, sell, stages, regrow, year, color);
    }

    public Crop(String unlocalized, Season[] seasons, int cost, int sell, int stages, int regrow, int year, int color) {
        this.unlocalized = unlocalized;
        this.seasons = seasons;
        this.cost = cost;
        this.sell = sell;
        this.stages = stages;
        this.regrow = regrow;
        this.year = year;
        this.isStatic = false;
        this.alternativeName = false;
        this.foodType = FoodType.VEGETABLE;
        this.bag_color = color;
        this.ordinal = HMConfiguration.addCropMapping(this); //Fetch the meta value of this crop
        this.iconHandler = new IconHandlerDefault(this);
        this.soilHandler = SoilHandlers.farmland;
        this.needsWatering = true;
        crops.add(this);
    }

    @Override
    public ICrop setHasAlternativeName() {
        this.alternativeName = true;
        return this;
    }

    @Override
    public ICrop setIsStatic() {
        this.isStatic = true;
        return this;
    }

    @Override
    public ICrop setItem(Item item) {
        this.item = item;
        return this;
    }

    @Override
    public ICrop setCropIconHandler(ICropRenderHandler handler) {
        this.iconHandler = handler;
        return this;
    }

    @Override
    public ICrop setRequiresSickle() {
        this.requiresSickle = true;
        return this;
    }

    public Crop setFoodType(FoodType foodType) {
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
    public ICrop setIsEdible() {
        this.isEdible = true;
        return this;
    }

    /** Return true if this crop uses an item other than HMCrop item **/
    public boolean hasItemAssigned() {
        return item != null;
    }

    /** This is the season this crop survives in 
     * @return the season that is valid for this crop */
    public Season[] getSeasons() {
        return seasons;
    }

    /** This is how much the seed costs in the store.
     * If the seed isn't purchasable return 0
     * @return the cost in gold */
    public long getSeedCost() {
        return cost;
    }

    /** This is how much this crop well sell for at level 1.
     * If this crop cannot be sold return 0
     * @return the sell value in gold */
    public long getSellValue() {
        return sell;
    }

    /** Return how many stages this crop has.
     * A return value of 0, means the crop is instantly grown.
     * @return the stage */
    public int getStages() {
        return stages;
    }

    /** The year in which this crop can be purchased **/
    public int getPurchaseYear() {
        return year;
    }

    /** Whether this crop is considered a seed at this stage **/
    public boolean isSeed(int stage) {
        return stage == 0;
    }

    /** Whether this crop is considered growing at this stage **/
    public boolean isGrowing(int stage) {
        if (getRegrowStage() > 0) {
            return stage < getRegrowStage();
        } else return stage < getStages() && stage > 0;
    }

    /** Whether this crop is considered grown this stage **/
    public boolean isGrown(int stage) {
        if (getRegrowStage() > 0) {
            return stage >= getRegrowStage();
        } else return stage == getStages();
    }

    /** Return true if this crop doesn't have quality/larger sizes **/
    public boolean isStatic() {
        return isStatic;
    }

    /** Return the stage that the plant returns to when it's harvested.
     * A return value of 0, means the crop is destroyed.
     * @return the stage */
    @Override
    public int getRegrowStage() {
        return regrow;
    }

    /** This is called to get the drop of this crop once it's ready for harvest 
     * @return the CropMeta that this crop should drop */
    @Override
    public int getCropMeta() {
        return ordinal;
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
    public boolean isEdible() {
        return isEdible;
    }

    @Override
    public ISoilHandler getSoilHandler() {
        return soilHandler;
    }

    /** This is called when bringing up the list of crops for sale 
     * @param shop The shop that is currently open
     * @return whether this item can be purchased in this shop or not */
    public boolean canPurchase() {
        return getSeedCost() > 0;
    }

    /** Return the colour of the seed bag **/
    public int getColor() {
        return bag_color;
    }

    /** The type of animal food this is **/
    public FoodType getFoodType() {
        return foodType;
    }

    public ItemStack getCropStackForQuality(int quality) {
        return new ItemStack(item, 1, quality);
    }

    @Override
    public ItemStack getSeedStack() {
        return new ItemStack(HMItems.seeds, 1, getCropMeta());
    }

    @Override
    public ItemStack getCropStack() {
        return new ItemStack(item);
    }

    @Override
    public ICropRenderHandler getCropHandler() {
        return iconHandler;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return (stack.getItem() == getCropStack().getItem());
    }

    /** Gets the unlocalized name for this crop
     * @return unlocalized name */
    @Override
    public String getUnlocalizedName() {
        return unlocalized;
    }

    /** Gets the localized crop name for this crop
     * @param stack 
     * @return crop name */
    public String getLocalizedName(boolean isItem) {
        String suffix = alternativeName ? ((isItem) ? ".item" : ".block") : "";
        return Translate.translate("crop." + StringUtils.replace(getUnlocalizedName(), "_", ".") + suffix);
    }

    /** Gets the localized seed name for this crop
     * @return seed name */
    public String getSeedsName() {
        String suffix = alternativeName ? ".block" : "";
        String name = Translate.translate("crop." + StringUtils.replace(getUnlocalizedName(), "_", ".") + suffix);
        String seeds = Translate.translate("crop.seeds");
        String text = Translate.translate("crop.seeds.format.standard");
        text = StringUtils.replace(text, "%C", name);
        text = StringUtils.replace(text, "%S", seeds);
        return text;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int stage) {
        return iconHandler.getIconForStage(stage);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        iconHandler.registerIcons(register);
    }

    @Override
    public boolean equals(Object o) {
        return unlocalized.equals(o);
    }

    @Override
    public int hashCode() {
        return unlocalized.hashCode();
    }
}
