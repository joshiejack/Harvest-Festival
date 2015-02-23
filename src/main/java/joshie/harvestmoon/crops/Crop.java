package joshie.harvestmoon.crops;

import java.util.ArrayList;

import joshie.harvestmoon.animals.AnimalType.FoodType;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.crops.CropData.WitherType;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.lib.CropMeta;
import joshie.harvestmoon.lib.HMModInfo;
import joshie.harvestmoon.util.Translate;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Crop {
    public static final ArrayList<Crop> crops = new ArrayList(30);

    //CropData
    @SideOnly(Side.CLIENT)
    protected IIcon[] stageIcons;
    @SideOnly(Side.CLIENT)
    protected IIcon giantIcon;
    protected String unlocalized;
    protected boolean alternativeName;
    protected boolean isStatic;
    protected Item vanillaItem;
    protected Season[] seasons;
    protected int cost;
    protected int sell;
    protected int stages;
    protected int regrow;
    protected int year;
    protected int bag_color;
    protected FoodType foodType;
    protected CropMeta meta;

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
    public Crop(String unlocalized, Season season, int cost, int sell, int stages, int regrow, int year, CropMeta meta, int color) {
        this(unlocalized, new Season[] { season }, cost, sell, stages, regrow, year, meta, color);
    }

    public Crop(String unlocalized, Season[] seasons, int cost, int sell, int stages, int regrow, int year, CropMeta meta, int color) {
        this.unlocalized = unlocalized;
        this.seasons = seasons;
        this.cost = cost;
        this.sell = sell;
        this.stages = stages;
        this.regrow = regrow;
        this.meta = meta;
        this.year = year;
        this.isStatic = false;
        this.alternativeName = false;
        this.foodType = FoodType.VEGETABLE;
        this.bag_color = color;

        crops.add(this);
    }

    public Crop setHasAlternativeName() {
        this.alternativeName = true;
        return this;
    }

    public Crop setIsStatic() {
        this.isStatic = true;
        return this;
    }

    public Crop setFoodType(FoodType foodType) {
        this.foodType = foodType;
        return this;
    }
    
    public Crop setVanillaItem(Item item) {
        this.vanillaItem = item;
        return this;
    }
    
    /** Return true if this crop uses an item other than HMCrop item **/
    public boolean isVanilla() {
        return vanillaItem != null;
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

    /** Returns the type of withering this plant will produce **/
    public final WitherType getWitherType(int stage) {
        if (isSeed(stage)) return WitherType.SEED;
        else if (isGrowing(stage)) return WitherType.GROWING;
        else if (isGrown(stage)) return WitherType.GROWN;
        else return WitherType.NONE;
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
    public int getRegrowStage() {
        return regrow;
    }

    /** This is called to get the drop of this crop once it's ready for harvest 
     * @return the CropMeta that this crop should drop */
    public int getCropMeta() {
        return meta.ordinal();
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

    public ItemStack getItemStack(int cropSize, int cropMeta, int cropQuality) {
        if (vanillaItem != null) {
            return new ItemStack(vanillaItem, 1, cropSize + cropQuality);
        }
        
        return new ItemStack(HMItems.crops, 1, cropSize + cropMeta + cropQuality);
    }

    /** Gets the unlocalized name for this crop
     * @return unlocalized name */
    public String getUnlocalizedName() {
        return unlocalized;
    }

    /** Returns a render appropriate name
     * return render name */
    public final String getRenderName() {
        return "Render" + WordUtils.capitalize(getUnlocalizedName(), '_').replace("_", "");
    }

    /** Gets the localized crop name for this crop
     * @param stack 
     * @return crop name */
    public final String getCropName(boolean isItem, boolean isGiant) {
        String suffix = alternativeName ? ((isItem) ? ".item" : ".block") : "";
        String name = Translate.translate("crop." + StringUtils.replace(getUnlocalizedName(), "_", ".") + suffix);
        if (!isGiant) {
            return name;
        } else {
            String text = Translate.translate("crop.giant.format");
            if (!isStatic()) {
                String giant = Translate.translate("crop.giant");
                text = StringUtils.replace(text, "%G", giant);
            } else text = StringUtils.replace(text, " %G", "");

            text = StringUtils.replace(text, "%C", name);
            return text;
        }
    }

    /** Gets the localized seed name for this crop
     * @return seed name */
    public final String getSeedsName(boolean isGiant) {
        String suffix = alternativeName ? ".block" : "";
        String name = Translate.translate("crop." + StringUtils.replace(getUnlocalizedName(), "_", ".") + suffix);
        String seeds = Translate.translate("crop.seeds");
        String text = Translate.translate("crop.seeds.format.standard");
        if (isGiant) {
            text = StatCollector.translateToLocal("hm.crop.seeds.format.giant");
            text = StringUtils.replace(text, "%G", Translate.translate("crop.giant"));
        }

        text = StringUtils.replace(text, "%C", name);
        text = StringUtils.replace(text, "%S", seeds);
        return text;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int stage, boolean isGiantCrop) {
        return isGiantCrop && stage == getStages() ? giantIcon : stageIcons[Math.max(0, stage - 1)];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        giantIcon = register.registerIcon(HMModInfo.CROPPATH + unlocalized + "/stage_giant");
        stageIcons = new IIcon[getStages()];
        for (int i = 0; i < getStages(); i++) {
            stageIcons[i] = register.registerIcon(HMModInfo.CROPPATH + unlocalized + "/stage_" + i);
        }
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
