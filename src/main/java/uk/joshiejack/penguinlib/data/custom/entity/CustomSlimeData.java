package uk.joshiejack.penguinlib.data.custom.entity;

import uk.joshiejack.penguinlib.client.renderer.entity.RenderCustomSlime;
import uk.joshiejack.penguinlib.entity.custom.EntityCustomSlime;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("entity:slime")
public class CustomSlimeData extends AbstractEntityData<EntityCustomSlime> {
    @Nonnull
    @SuppressWarnings("unchecked")
    @Override
    public Integer build(ResourceLocation registryName, @Nonnull AbstractEntityData main, @Nullable AbstractEntityData... unused) {
        Integer ret = main.register(registryName, EntityCustomSlime.class, 0);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            main.setRenderer(RenderCustomSlime::new);
        }

        return ret;
    }
}
