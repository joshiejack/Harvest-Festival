package joshie.harvest.mining.render;

import joshie.harvest.animals.render.ModelHarvestChicken;
import joshie.harvest.mining.entity.EntityDarkChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.MathHelper;

public class RenderDarkChicken extends RenderDarkMob<EntityDarkChicken> {
    public RenderDarkChicken(RenderManager manager) {
        super(manager, new ModelHarvestChicken.Adult(), "dark_chicken");
    }

    @Override
    protected float handleRotationFloat(EntityDarkChicken livingBase, float partialTicks) {
        this.shadowSize = 0.3F;
        float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}
