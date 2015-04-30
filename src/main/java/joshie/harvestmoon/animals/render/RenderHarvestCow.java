package joshie.harvestmoon.animals.render;

import joshie.harvestmoon.core.lib.HMModInfo;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderHarvestCow extends RenderLiving {
    private static final ResourceLocation texture = new ResourceLocation(HMModInfo.MODPATH, "textures/entity/cow.png");
    
    public RenderHarvestCow() {
        super(new ModelHarvestCow(), 1F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}