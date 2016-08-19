package joshie.harvest.animals.render;

import joshie.harvest.animals.entity.EntityHarvestChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHarvestChicken extends RenderHarvestAnimal<EntityHarvestChicken> {
    public RenderHarvestChicken(RenderManager manager) {
        super(manager, new ModelHarvestChicken(), "chicken");
    }

    @Override
    protected float handleRotationFloat(EntityHarvestChicken livingBase, float partialTicks) {
        this.shadowSize = 0.3F;
        float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}
