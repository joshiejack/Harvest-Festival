package joshie.harvest.npcs.render;

import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.npcs.entity.EntityNPC.Mode;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModelNPC extends ModelBiped {
    private final boolean smallArms;

    public ModelNPC(boolean alex) {
        super(0F, 0.0F, 64, 64);
        float modelSize = 0F;
        smallArms = alex;

        if (alex) {
            bipedLeftArm = new ModelRenderer(this, 32, 48);
            bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
            bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
            bipedRightArm = new ModelRenderer(this, 40, 16);
            bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
            bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
        } else {
            bipedLeftArm = new ModelRenderer(this, 32, 48);
            bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
            bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        }
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, @Nullable Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        setAnglesBasedOnMode((EntityNPC) entity);
    }

    private void setAnglesBasedOnMode(EntityNPC npc) {
        Mode mode = npc.getMode();
        if (mode == Mode.GIFT) {
            bipedRightArm.rotateAngleX = -45F;
            bipedLeftArm.rotateAngleX = -45F;
        }
    }

    @Override
    public void postRenderArm(float scale, @Nonnull EnumHandSide side) {
        ModelRenderer modelrenderer = this.getArmForSide(side);

        if (smallArms) {
            float f = 0.5F * (float)(side == EnumHandSide.RIGHT ? 1 : -1);
            modelrenderer.rotationPointX += f;
            modelrenderer.postRender(scale);
            modelrenderer.rotationPointX -= f;
        } else {
            modelrenderer.postRender(scale);
        }
    }
}
