package joshie.harvest.api.buildings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IBuilding {
    /** Returns the name of this building group **/
    public String getName();
    
    /** How much gold this building costs **/
    public long getCost();

    /** How much wood this building costs **/
    public int getWoodCount();
    
    /** How much stone this building costs **/
    public int getStoneCount();

    /** Whether this building can be purchased or not **/
    public boolean canBuy(World world, EntityPlayer player);
}
