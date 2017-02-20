package joshie.harvest.api.knowledge;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.LinkedHashMap;

public class Note {
    public static final LinkedHashMap<ResourceLocation, Note> REGISTRY = new LinkedHashMap<>();
    public static final ItemStack PAPER = new ItemStack(Items.PAPER);
    private final Category category;
    private final ResourceLocation resource;
    private final String title;
    private final String description;
    private ItemStack icon;
    private boolean isSecret;
    @SideOnly(Side.CLIENT)
    private NoteRender render;

    public Note(Category category, ResourceLocation resource) {
        this.category = category;
        this.resource = resource;
        this.icon = PAPER;
        this.title = resource.getResourceDomain() + ".note." + resource.getResourcePath() + ".title";
        this.description = resource.getResourceDomain() + ".note." + resource.getResourcePath() + ".description";
        REGISTRY.put(resource, this);
    }

    @SideOnly(Side.CLIENT)
    public Note setRender(NoteRender render) {
        this.render = render;
        return this;
    }

    /** Set the icon for this note
     *  @param icon     the stack to reprsent this note **/
    public Note setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public Note setSecretNote() {
        this.isSecret = true;
        return this;
    }

    public ResourceLocation getResource() {
        return resource;
    }

    @SideOnly(Side.CLIENT)
    public NoteRender getRender() {
        return render;
    }

    /** Returns the stack that this note displays as **/
    public ItemStack getIcon() {
        return icon;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isSecret() {
        return isSecret;
    }

    public String getDescription() {
        return StringEscapeUtils.unescapeJava(I18n.translateToLocal(description));
    }

    public String getTitle() {
        return I18n.translateToLocal(title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return resource != null ? resource.equals(note.resource) : note.resource == null;

    }

    @Override
    public int hashCode() {
        return resource != null ? resource.hashCode() : 0;
    }
}
