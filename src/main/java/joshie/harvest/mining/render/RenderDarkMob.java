package joshie.harvest.mining.render;

import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderDarkMob<T extends EntityLiving> extends RenderLiving<T> {
    protected final ResourceLocation texture;

    public RenderDarkMob(RenderManager manager, ModelBase model, String animal) {
        super(manager, model, 1F);
        texture = new ResourceLocation(HFModInfo.MODID, "textures/entity/" + animal + ".png");
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLiving entityLiving) {
        return texture;
    }
}
