package joshie.harvest.api.quests;

import joshie.harvest.api.npc.NPCEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import javax.annotation.Nonnull;

/** Used for selection menus **/
public abstract class Selection<Q extends Quest> {
    protected String[] lines;

    public Selection() {}
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

    protected void setLines(String... lines) {
        this.lines = lines;
    }

    /** Returns the unlocalised text **/
    public String[] getText(@Nonnull EntityPlayer player, Q quest) {
        return lines;
    }

    /** Called when these options are selected
     *  On the server side only, any changes that
     *  are internally saved, will be synced to the client
     * @param player        the player interacting
     * @param entity        the npc associated with the entity
     * @param option        which option they selected (1/2/3)
     * @param quest         the quest object associated with this
     * @return return what happens next,
     *              return DENY to close the options menu
     *              return ALLOW to open the npc chat window
     *              return DEFAULT to do nothing */
    public abstract Result onSelected(EntityPlayer player, NPCEntity entity, Q quest, int option);
}