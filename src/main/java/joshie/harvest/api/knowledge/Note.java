package joshie.harvest.api.knowledge;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringEscapeUtils;

public class Note extends IForgeRegistryEntry.Impl<Note> {
    public static final IForgeRegistry<Note> REGISTRY = new RegistryBuilder<Note>().setName(new ResourceLocation("harvestfestival", "notes")).setType(Note.class).setIDRange(0, 100000).create();

    private final Category category;
    private boolean isSecret;
    @SideOnly(Side.CLIENT)
    private NoteRender render;

    public Note(Category category) {
        this.category = category;
    }

    @SideOnly(Side.CLIENT)
    public Note setRender(NoteRender render) {
        this.render = render;
        return this;
    }

    public Note setSecretNote() {
        this.isSecret = true;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public NoteRender getRender() {
        return render;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isSecret() {
        return isSecret;
    }

    public String getDescription() {
        return StringEscapeUtils.unescapeJava(I18n.translateToLocal(getRegistryName().getResourceDomain() + ".note." + getRegistryName().getResourcePath() + ".description"));
    }

    public String getTitle() {
        return I18n.translateToLocal(getRegistryName().getResourceDomain() + ".note." + getRegistryName().getResourcePath() + ".title");
    }
}
