package joshie.harvestmoon.crops;

import java.util.ArrayList;

import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.crops.CropData.WitherType;
import joshie.harvestmoon.lib.CropMeta;
import joshie.harvestmoon.util.Translate;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;


public class Crop {
    public static final ArrayList<Crop> crops = new ArrayList(30);
        
    //CropData
    protected String unlocalized;
    protected Season season;
    protected int cost;
    protected int sell;
    protected int stages;
    protected int regrow;
    protected int year;
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
    public Crop(String unlocalized, Season season, int cost, int sell, int stages, int regrow, int year, CropMeta meta) {
        this.unlocalized = unlocalized;
        this.season = season;
        this.cost = cost;
        this.sell = sell;
        this.stages = stages;
        this.regrow = regrow;
        this.meta = meta;
        this.year = year;
        
        crops.add(this);
    }
    
    /** This is the season this crop survives in 
     * @return the season that is valid for this crop */
    public Season getSeason() {
        return season;
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
        if(isSeed(stage)) return WitherType.SEED;
        else if (isGrowing(stage)) return WitherType.GROWING;
        else if(isGrown(stage)) return WitherType.GROWN;
        else return WitherType.NONE;
    }
    

    /** Whether this crop is considered a seed at this stage **/
    public boolean isSeed(int stage) {
        return stage == 0;
    }
    
    /** Whether this crop is considered growing at this stage **/
    public boolean isGrowing(int stage) {
        if(getRegrowStage() > 0) {
            return stage < getRegrowStage();
        } else return stage < getStages() && stage > 0;
    }
    
    /** Whether this crop is considered grown this stage **/
    public boolean isGrown(int stage) {
        if(getRegrowStage() > 0) {
            return stage >= getRegrowStage();
        } else return stage == getStages();
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
    public final String getCropName(boolean isGiant) {
        String name = Translate.translate("crop." + StringUtils.replace(getUnlocalizedName(), "_", "."));
        if (!isGiant) {
            return name;
        } else {
            String text = Translate.translate("crop.giant.format");
            String giant = Translate.translate("crop.giant");            
            text = StringUtils.replace(text, "%G", giant);
            text = StringUtils.replace(text, "%C", name);
            return text;
        }
    }
    
    /** Gets the localized seed name for this crop
     * @return seed name */
    public final String getSeedsName(boolean isGiant) {
        String name = Translate.translate("crop." + StringUtils.replace(getUnlocalizedName(), "_", "."));
        String seeds = Translate.translate("crop.seeds");
        String text = Translate.translate("crop.seeds.format.standard");
        if(isGiant) {
            text = StatCollector.translateToLocal("hm.crop.seeds.format.giant");
            text = StringUtils.replace(text, "%G", Translate.translate("crop.giant"));
        }
        
        text = StringUtils.replace(text, "%C", name);
        text = StringUtils.replace(text, "%S", seeds);
        return text;
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
