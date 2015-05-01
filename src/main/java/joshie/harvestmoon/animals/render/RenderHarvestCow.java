package joshie.harvestmoon.animals.render;

import joshie.harvestmoon.animals.EntityHarvestCow;
import joshie.harvestmoon.core.lib.HMModInfo;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderHarvestCow extends RenderLiving {
    private static final ResourceLocation texture_child = new ResourceLocation(HMModInfo.MODPATH, "textures/entity/cow_child.png");
    private static final ResourceLocation texture_adult = new ResourceLocation(HMModInfo.MODPATH, "textures/entity/cow_adult.png");
    
    public RenderHarvestCow() {
        super(new ModelHarvestCowChild(), 1F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return ((EntityHarvestCow)entity).isChild() ? texture_child : texture_adult;
    }
}