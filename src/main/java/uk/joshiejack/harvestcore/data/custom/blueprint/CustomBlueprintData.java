package uk.joshiejack.harvestcore.data.custom.blueprint;

import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("blueprint:standard")
public class CustomBlueprintData extends AbstractCustomData<Blueprint, CustomBlueprintData> {
    public String category;
    public String result;
    public Requirement[] ingredients;

    @Nonnull
    @Override
    public Blueprint build(ResourceLocation registryName, @Nonnull CustomBlueprintData main, @Nullable CustomBlueprintData... data) {
        return new Blueprint(registryName, main.category, main.result, main.ingredients);
    }

    public static class Requirement {
        public String item;
        public int count;
    }
}
