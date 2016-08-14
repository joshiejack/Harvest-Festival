package joshie.harvest.mining.render;

import joshie.harvest.animals.render.ModelHarvestChicken;
import joshie.harvest.mining.entity.EntityDarkChick;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.MathHelper;

public class RenderDarkChick extends RenderDarkMob<EntityDarkChick> {
    public RenderDarkChick(RenderManager manager) {
        super(manager, new ModelHarvestChicken.Child(), "dark_chick");
    }

    @Override
    protected float handleRotationFloat(EntityDarkChick livingBase, float partialTicks) {
        this.shadowSize = 0.3F;
        float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}
