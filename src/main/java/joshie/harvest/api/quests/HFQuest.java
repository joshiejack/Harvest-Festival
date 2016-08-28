package joshie.harvest.api.quests;

import joshie.harvest.core.commands.AbstractHFCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotate any of the following, to have them automatically registered
 *  @see AbstractHFCommand
 *  @see joshie.harvest.api.quests.Quest
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HFQuest {
    /** Extra data,
     * for most things this is simply the resource path
     * You can also have this be "events" to have it autoamtically registered to the event bus**/
    String value() default "";

    /** Return the mod id **/
    String mod() default "harvestfestival";
}
