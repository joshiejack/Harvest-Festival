package joshie.harvest.api.core;

import joshie.harvest.api.calendar.CalendarDate;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static joshie.harvest.api.calendar.CalendarDate.DAYS_PER_SEASON;

public class Letter {
    public static final Map<ResourceLocation, Letter> REGISTRY = new HashMap<>();
    public static final Letter NONE = new Letter(new ResourceLocation("harvestfestival", "none"));
    private final ResourceLocation resource;
    private EventPriority priority;
    private boolean isTownLetter;
    private String toLocalize;
    private int expiry;

    public Letter(ResourceLocation resource) {
        this.resource = resource;
        this.priority = EventPriority.NORMAL;
        this.toLocalize = resource.getResourceDomain() + ".letter." + resource;
        this.expiry = -1;
        REGISTRY.put(resource, this);
    }

    public ResourceLocation getResource() {
        return resource;
    }

    /** Marks this letter as being saved to the town **/
    public Letter setTownLetter() {
        this.isTownLetter = true;
        return this;
    }

    /** Set the priority for this letter
     *  Letters with higher priority will display in the mailbox first
     * @param priority      the priority */
    public Letter setPriority(EventPriority priority) {
        this.priority = priority;
        return this;
    }

    /** If this a town saved letter **/
    public boolean isTownLetter() {
        return isTownLetter;
    }

    /** The priority of this letter **/
    public EventPriority getPriority() {
        return priority;
    }

    /** If this letter expires **/
    protected boolean expires() {
        return expiry >= 0;
    }

    /** Number of days it takes for the letter to expire **/
    protected int getExpiry() {
        return (expiry / 30) * DAYS_PER_SEASON;
    }

    /** If the letter expires today
     * @param today     todays date
     * @param days      the number of days this letter has been in the mail
     * @return if it has expired */
    public boolean isExpired(CalendarDate today, int days) {
        return expires() && days >= getExpiry();
    }

    /** Called when the gui is init, so that you can add buttons
     * @param list  the list of buttons
     * @param x     guiLeft
     * @param y     guiTop */
    @SideOnly(Side.CLIENT)
    public void initGui(List<GuiButton> list, int x, int y){}

    /** Called when rendering the letter
     * @param gui   the gui
     * @param font  the font
     * @param x     guiLeft
     * @param y     guiTop */
    @SideOnly(Side.CLIENT)
    public void renderLetter(GuiScreen gui, FontRenderer font, int x, int y) {
        font.drawSplitString(StringEscapeUtils.unescapeJava(I18n.translateToLocal(toLocalize)), 42, 38, 142, 4210752);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Letter)) return false;
        Letter letter = (Letter) o;
        return resource != null ? resource.equals(letter.resource) : letter.resource == null;
    }

    @Override
    public int hashCode() {
        return resource != null ? resource.hashCode() : 0;
    }
}
