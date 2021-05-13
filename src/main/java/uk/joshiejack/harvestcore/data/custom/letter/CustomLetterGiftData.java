package uk.joshiejack.harvestcore.data.custom.letter;

import uk.joshiejack.harvestcore.registry.letter.LetterGift;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("letter:gift")
public class CustomLetterGiftData extends AbstractCustomLetterData<LetterGift, CustomLetterGiftData> {
    private String[] items;

    @Nonnull
    @Override
    public LetterGift build(ResourceLocation registryName, @Nonnull CustomLetterGiftData main, @Nullable CustomLetterGiftData... data) {
        return new LetterGift(registryName, main.text, main.isRepeatable, main.deliveryTime, main.items);
    }
}
