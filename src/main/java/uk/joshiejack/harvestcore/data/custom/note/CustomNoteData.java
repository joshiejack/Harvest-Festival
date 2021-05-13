package uk.joshiejack.harvestcore.data.custom.note;

import uk.joshiejack.harvestcore.registry.Note;
import uk.joshiejack.penguinlib.data.custom.CustomIcon;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("note:standard")
public class CustomNoteData extends AbstractCustomData<Note, CustomNoteData> {
    public String category;
    public CustomIcon icon = new CustomIcon();
    public boolean hidden = false;

    @Nonnull
    @Override
    public Note build(ResourceLocation registryName, @Nonnull CustomNoteData main, @Nullable CustomNoteData... data) {
        Note note =  new Note(registryName, main.category, main.icon);
        if (hidden) note.setHidden();
        note.setRenderScript(getScript());
        return note;
    }

}
