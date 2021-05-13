package uk.joshiejack.harvestcore.data.custom.letter;

import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("letter:info")
public class CustomLetterData extends AbstractCustomLetterData<Letter, CustomLetterData> {
    @Nonnull
    @Override
    public Letter build(ResourceLocation registryName, @Nonnull CustomLetterData main, @Nullable CustomLetterData... data) {
        return new Letter(registryName, main.text, main.isRepeatable, main.deliveryTime);
    }
}
