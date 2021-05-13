package uk.joshiejack.penguinlib.data.custom.potion;

import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.potion.PotionCustom;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("potion:standard")
public class CustomPotionData extends AbstractCustomData<Potion, CustomPotionData> {
    public String color = null;
    public int x, y;
    public Attributes[] attributes;
    private transient int colorInt = -1;

    @Nonnull
    @Override
    public Potion build(ResourceLocation registryName, @Nonnull CustomPotionData data, @Nullable CustomPotionData... unused) {
        return new PotionCustom(registryName, data);
    }

    public int getColor() {
        if (color != null && colorInt == -1) {
            return Integer.parseInt(color.replace("0x", ""), 16);
        } else return 0;
    }

}
