package uk.joshiejack.harvestcore.data.custom.letter;

import uk.joshiejack.harvestcore.registry.letter.LetterScript;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("letter:gift")
public class CustomLetterScriptData extends AbstractCustomLetterData<LetterScript, CustomLetterScriptData> {
    @Nonnull
    @Override
    public LetterScript build(ResourceLocation registryName, @Nonnull CustomLetterScriptData main, @Nullable CustomLetterScriptData... data) {
        return new LetterScript(registryName, main.text, main.isRepeatable, main.deliveryTime, main.getScript());
    }
}
