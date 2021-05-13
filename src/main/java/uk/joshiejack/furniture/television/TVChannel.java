package uk.joshiejack.furniture.television;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.PenguinRegistry;

import javax.annotation.Nullable;
import java.util.Map;

public class TVChannel extends PenguinRegistry {
    public static final Map<ResourceLocation, TVChannel> REGISTRY = Maps.newHashMap();
    public static final TVChannel OFF = new TVChannel(new ResourceLocation(Furniture.MODID, "off"));

    //The texture displayed in the menu selection and the coordinates, 62x46 pixels
    private Interpreter script;
    private ResourceLocation scriptID;
    private ResourceLocation screenshot;
    private int screenshotX, screenshotY;
    private String unlocalized;
    private boolean selectable = false;

    public static TVChannel create(ResourceLocation id) { return new TVChannel(id); }
    private TVChannel(ResourceLocation resource) {
        super(REGISTRY, resource);
        this.screenshot = resource;
        this.unlocalized = resource.getNamespace() + ".television.channel."+ resource.getPath() + ".name";
    }

    public ResourceLocation getScreenshot() {
        return screenshot;
    }

    public int getScreenshotX() {
        return screenshotX;
    }

    public int getScreenshotY() {
        return screenshotY;
    }

    public void setScript(ResourceLocation script) {
        this.scriptID = script;
    }

    public void setScreenshot(ResourceLocation screenshot, int x, int y) {
        this.screenshot = screenshot;
        this.screenshotX = x;
        this.screenshotY = y;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable() {
        this.selectable = true;
    }

    @Nullable
    public Interpreter getScript() {
        if (scriptID == null) return null; //No continue
        if (script == null) {
            script = Scripting.get(scriptID);
        }

        return script;
    }

    public String getUnlocalizedName() {
        return unlocalized;
    }
}
