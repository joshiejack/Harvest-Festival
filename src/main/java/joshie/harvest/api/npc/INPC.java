package joshie.harvest.api.npc;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.INPCRegistry.Age;
import joshie.harvest.api.npc.INPCRegistry.Gender;
import joshie.harvest.api.npc.gift.IGiftHandler;
import joshie.harvest.api.npc.gift.IGiftHandler.Quality;
import joshie.harvest.api.shops.IShop;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public interface INPC {
    /** Assigns a shop to this NPC
     *  @param shop the shop
     *  @return the npc**/
    INPC setShop(IShop shop);

    @Deprecated //TODO: Remove in 0.7+
    INPC setIsBuilder();

    /** Set the height of this npc for rendering purposes
     *  @param height   the npc height
     *  @param offset   the npc positioning offset
     *  @return the npc**/
    INPC setHeight(float height, float offset);

     @Deprecated
    INPC setLocation(Location location, Building building, String name);

    /** Mark this NPCS home/work
     ** @param location location type
     * @param building the building location */
    INPC setLocation(Location location, BuildingLocation building);

    /** Set whether this npc uses alex skin or not
     *  @return the npc */
    INPC setAlexSkin();

    /** Sets the gift handler
     * @param handler   the gift handler object
     * @return the npc**/
    INPC setGiftHandler(IGiftHandler handler);

    /** Sets the schedule handler
     * @param handler   the schedule handler object
     * @return the npc**/
    INPC setScheduleHandler(ISchedule handler);

    /** Marks this npc as not respawning when they die
     * @return the npc **/
    INPC setNoRespawn();

    /** Makes the npc display a specific chat
     *  @param stack the item to use as the icon
     *  @param infoGreeting  the greeting handler to use**/
    INPC setHasInfo(ItemStack stack, IGreeting infoGreeting);

    /** Allows you to add additional greetings to npcs
     *  @param greeting the greeting type
     *  @return the npc**/
    INPC addGreeting(IConditionalGreeting greeting);

    /** Returns the resource name **/
    ResourceLocation getResource();

    /** Returns the localised name for this NPC **/
    String getLocalizedName();

    /** Returns the birthday of this npc **/
    CalendarDate getBirthday();

    /** Gets the age of this npc
     *  @return the npcs age **/
    Age getAge();

    /** Gets the gender of this npc
     *  @return the npcs gender **/
    Gender getGender();

    /** Returns this quality of this gift **/
    Quality getGiftValue(ItemStack gift);

    /** Returns the building location for the passed in type of residence**/
    BuildingLocation getLocation(Location location);

    /** Returns the unique id of this npc **/
    UUID getUUID();

    /** Returns true if this npc can be married **/
    boolean isMarriageCandidate();

    @Deprecated //TODO: Remove in 0.7+
    boolean isBuilder();

    enum Location {
        HOME, SHOP
    }
}