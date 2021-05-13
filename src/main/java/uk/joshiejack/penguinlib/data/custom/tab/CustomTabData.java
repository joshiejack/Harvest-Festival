package uk.joshiejack.penguinlib.data.custom.tab;

import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.creativetab.CustomPenguinTab;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.creativetab.PenguinTab;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("tab:standard")
public class CustomTabData extends AbstractCustomData<PenguinTab, CustomTabData> {
    private ResourceLocation[] items;
    private String icon;

    @Nonnull
    @Override
    public PenguinTab build(ResourceLocation registryName, @Nonnull CustomTabData data, @Nullable CustomTabData... unused) {
        return new CustomPenguinTab(registryName.getNamespace().equals("minecraft") ? registryName.getPath() : registryName.getNamespace() + "." + registryName.getPath(),
                data.items, () -> StackHelper.getStackFromString(data.icon));
    }
}