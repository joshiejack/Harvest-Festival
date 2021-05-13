package uk.joshiejack.penguinlib.data.custom.sound;

import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("sound:standard")
public class CustomSoundData extends AbstractCustomData<SoundEvent, CustomSoundData> {
    @Nonnull
    @Override
    public SoundEvent build(ResourceLocation registryName, @Nonnull CustomSoundData data, @Nullable CustomSoundData... unused) {
        return RegistryHelper.createSoundEvent(registryName.getNamespace(), registryName.getPath());
    }
}
