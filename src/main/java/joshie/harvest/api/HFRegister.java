package joshie.harvest.api;

/** Annotate any of the following, to have them automatically registered
 *  @see joshie.harvest.api.quests.Quest
 */
public @interface HFRegister {
    /** Return the mod id **/
    String mod() default "harvestfestival";

    /** Extra data,
     * for most things this is simply the resource path
     * You can also have this be "events" to have it autoamtically registered to the event bus**/
    String data() default "";
}
