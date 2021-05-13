package uk.joshiejack.harvestcore.data.custom.letter;

import uk.joshiejack.harvestcore.registry.letter.LetterGold;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("letter:gift")
public class CustomLetterGoldData extends AbstractCustomLetterData<LetterGold, CustomLetterGoldData> {
    private long gold;

    @Nonnull
    @Override
    public LetterGold build(ResourceLocation registryName, @Nonnull CustomLetterGoldData main, @Nullable CustomLetterGoldData... data) {
        return new LetterGold(registryName, main.text, main.isRepeatable, main.deliveryTime, main.gold);
    }
}
