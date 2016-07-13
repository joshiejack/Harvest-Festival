package joshie.harvest.api;

/** Annotate any of the following, to have them automatically registered
 *  @see joshie.harvest.api.quests.Quest
 */
public @interface HFRegister {
    /** Return the resource domain **/
    String domain() default "harvestfestival";

    /** Return the resource path **/
    String path();
}
