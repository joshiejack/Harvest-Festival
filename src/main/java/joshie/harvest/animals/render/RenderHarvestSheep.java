package joshie.harvest.animals.render;

import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderHarvestSheep extends RenderHarvestAnimal<EntityHarvestSheep> {
    private final ResourceLocation texture_sheared;

    public RenderHarvestSheep(RenderManager manager) {
        super(manager, new ModelHarvestSheep(), "sheep");
        texture_sheared = new ResourceLocation(HFModInfo.MODID, "textures/entity/sheep_adult_sheared.png");
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityHarvestSheep sheep) {
        if (sheep.isChild()) return texture_child;
        else if (sheep.getSheared()) return texture_sheared;
        else return texture_adult;
    }
}
