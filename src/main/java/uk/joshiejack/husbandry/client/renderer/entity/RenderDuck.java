package uk.joshiejack.husbandry.client.renderer.entity;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.husbandry.entity.EntityDuck;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderDuck extends RenderLiving<EntityDuck> {
    private static final ResourceLocation DUCK_TEXTURE = new ResourceLocation(Husbandry.MODID, "textures/entity/duck.png");

    public RenderDuck(RenderManager rm) {
        super(rm, new ModelChicken(), 0.3F);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityDuck entity) {
        return DUCK_TEXTURE;
    }

    @Override
    protected float handleRotationFloat(EntityDuck livingBase, float partialTicks) {
        float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}