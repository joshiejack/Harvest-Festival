package uk.joshiejack.harvestcore.registry.letter;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.util.PenguinGroup;
import uk.joshiejack.penguinlib.util.PenguinRegistry;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Letter extends PenguinRegistry {
    public static final Map<ResourceLocation, Letter> REGISTRY = Maps.newHashMap();
    private final String toLocalize;
    private String text;
    private PenguinGroup group;
    private boolean rejectable;
    private boolean repeatable;
    private int expiry;
    private int delay;

    public Letter(ResourceLocation resource, String text, boolean repeatable, int delay) {
        super(REGISTRY, resource);
        this.group = PenguinGroup.PLAYER;
        this.toLocalize = resource.getNamespace() + ".letter." + resource.getPath();
        this.text = text;
        this.repeatable = repeatable;
        this.delay = delay;
        REGISTRY.put(resource, this);
    }

    public boolean init() {
        return true;
    }

    public void setRejectable() {
        this.rejectable = true;
    }

    public String getUnlocalizedName() {
        return toLocalize;
    }

    public boolean isRejectable() {
        return rejectable;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public int getDelay() {
        return delay;
    }

    public boolean expires() {
        return expiry > 0;
    }

    public int getExpiry() {
        return expiry;
    }

    public void accept(EntityPlayer player) {}

    public void reject(EntityPlayer player) {}

    public void onClosed(EntityPlayer player) {}

    /** Called when the gui is init, so that you can add buttons
     *  Take note that buttons that accept the letter or reject
     *  are automatically added
     * @param list  the list of buttons
     * @param x     guiLeft
     * @param y     guiTop */
    @SideOnly(Side.CLIENT)
    public void initGui(List<GuiButton> list, int x, int y){}

    /** Called when rendering the letter
     * @param gui   the gui
     * @param font  the font
     * @param x     guiLeft
     * @param y     guiTop
     * @param mouseX    x position of mouse
     * @param mouseY    y position of mouse */
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public void renderLetter(GuiScreen gui, FontRenderer font, int x, int y, int mouseX, int mouseY) {
        font.drawSplitString(text != null ? text : StringHelper.localize(toLocalize), 15, 15, 142, 4210752);
    }

    public PenguinGroup getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Letter letter = (Letter) o;
        return Objects.equals(getRegistryName(), letter.getRegistryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }
}
