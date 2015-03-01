package joshie.harvestmoon.api.crops;

public interface ICropData {
    /** Return the crop itself **/
    public ICrop getCrop();
    
    /** Returns the stage this crop is at **/
    public int getStage();

    /** Returns the quality of this crop **/
    public int getQuality();
    
    /** Resets the crop to it's regrow stage **/
    public void regrow();

    /** Wipes out the data for this crop **/
    public void clear();
}
