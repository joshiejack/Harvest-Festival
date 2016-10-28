package joshie.harvest.api.quests;

import joshie.harvest.api.npc.INPC;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import javax.annotation.Nullable;

/** Used for selection menus **/
public abstract class Selection<Q extends Quest> {
    private final String[] lines;

    public Selection(String title, String line1, String line2) {
        this.lines = new String[3];
        this.lines[0] = title;
        this.lines[1] = line1;
        this.lines[2] = line2;
    }

    public Selection(String title, String line1, String line2, String line3) {
        this.lines = new String[4];
        this.lines[0] = title;
        this.lines[1] = line1;
        this.lines[2] = line2;
        this.lines[3] = line3;
    }

    /** Returns the unlocalised text **/
    public final String[] getText() {
        return lines;
    }

    /** Called when these options are selected
     * @param player        the player interacting
     * @param entity        the entity the player is interacting with
     * @param npc           the npc associated with the entity
     * @param option        which option they selected (1/2/3)
     * @param quest         the quest object associated with this
     * @return return what happens next,
     *              return DENY to close the options menu
     *              return ALLOW to open the npc chat window
     *              return DEFAULT to do nothing */
    public abstract Result onSelected(EntityPlayer player, EntityLiving entity, INPC npc, @Nullable Q quest, int option);
}
