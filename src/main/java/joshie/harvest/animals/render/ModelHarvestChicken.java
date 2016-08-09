package joshie.harvest.animals.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelHarvestChicken extends ModelBase {
    /**
     * Adult Models
     **/
    ModelRenderer adult_legRight;
    ModelRenderer adult_crest1;
    ModelRenderer adult_legLeft;
    ModelRenderer adult_wingLeft;
    ModelRenderer adult_footLeft;
    ModelRenderer adult_wingRight;
    ModelRenderer adult_footRight;
    ModelRenderer adult_head;
    ModelRenderer adult_tail;
    ModelRenderer adult_beak;
    ModelRenderer adult_brest;
    ModelRenderer adult_butt;
    ModelRenderer adult_crest2;
    ModelRenderer adult_back;
    ModelRenderer adult_chest;

    /**
     * Child Models
     **/
    private ModelRenderer child_legRight;
    private ModelRenderer child_legLeft;
    private ModelRenderer child_wingLeft;
    private ModelRenderer child_footLeft;
    private ModelRenderer child_wingRight;
    private ModelRenderer child_footRight;
    private ModelRenderer child_tail;
    private ModelRenderer child_beak;
    private ModelRenderer child_chest;
    private ModelRenderer child_head;

    public ModelHarvestChicken() {
        textureWidth = 64;
        textureHeight = 32;
        adult_legRight = new ModelRenderer(this, 0, 20);
        adult_legRight.addBox(-2F, 0F, -3F, 2, 1, 2);
        adult_legRight.setRotationPoint(-1F, 22F, 2F);
        adult_legRight.setTextureSize(64, 32);
        adult_legRight.mirror = false;
        adult_crest1 = new ModelRenderer(this, 30, 0);
        adult_crest1.addBox(-2F, -7F, -2F, 2, 2, 6);
        adult_crest1.setRotationPoint(1F, 13F, -4F);
        adult_crest1.setTextureSize(64, 32);
        adult_crest1.mirror = true;
        adult_legLeft = new ModelRenderer(this, 0, 20);
        adult_legLeft.addBox(0F, 0F, -3F, 2, 1, 2);
        adult_legLeft.setRotationPoint(1F, 22F, 2F);
        adult_legLeft.setTextureSize(64, 32);
        adult_legLeft.mirror = true;
        adult_wingLeft = new ModelRenderer(this, 0, 23);
        adult_wingLeft.addBox(0F, 0F, 0F, 1, 4, 5);
        adult_wingLeft.setRotationPoint(4F, 16F, -2F);
        adult_wingLeft.setTextureSize(64, 32);
        adult_wingLeft.mirror = true;
        setRotateAngle(adult_wingLeft, 0F, 0.2617994F, 0F);
        adult_footLeft = new ModelRenderer(this, 10, 18);
        adult_footLeft.addBox(0F, 1F, -5F, 2, 1, 4);
        adult_footLeft.setRotationPoint(1F, 22F, 2F);
        adult_footLeft.setTextureSize(64, 32);
        adult_wingRight = new ModelRenderer(this, 0, 23);
        adult_wingRight.addBox(0F, 0F, 0F, 1, 4, 5);
        adult_wingRight.setRotationPoint(-4F, 16F, -2F);
        adult_wingRight.setTextureSize(64, 32);
        adult_wingRight.mirror = true;
        setRotateAngle(adult_wingRight, 0F, -0.2617994F, 0F);
        adult_footRight = new ModelRenderer(this, 10, 18);
        adult_footRight.addBox(-2F, 1F, -5F, 2, 1, 4);
        adult_footRight.setRotationPoint(-1F, 22F, 2F);
        adult_footRight.setTextureSize(64, 32);
        adult_footRight.mirror = false;
        adult_head = new ModelRenderer(this, 0, 0);
        adult_head.addBox(-5F, -5F, -4F, 8, 8, 8);
        adult_head.setRotationPoint(1F, 13F, -4F);
        adult_head.setTextureSize(64, 32);
        adult_head.mirror = true;
        adult_tail = new ModelRenderer(this, 32, 13);
        adult_tail.addBox(-3F, -3F, 7F, 4, 3, 2);
        adult_tail.setRotationPoint(1F, 19F, -1F);
        adult_tail.setTextureSize(64, 32);
        adult_tail.mirror = true;
        adult_beak = new ModelRenderer(this, 13, 28);
        adult_beak.addBox(-2F, -1F, -6F, 2, 2, 2);
        adult_beak.setRotationPoint(1F, 13F, -4F);
        adult_beak.setTextureSize(64, 32);
        adult_beak.mirror = true;
        adult_brest = new ModelRenderer(this, 22, 18);
        adult_brest.addBox(-4F, -3F, -6F, 6, 5, 2);
        adult_brest.setRotationPoint(1F, 19F, -1F);
        adult_brest.setTextureSize(64, 32);
        adult_brest.mirror = true;
        adult_butt = new ModelRenderer(this, 44, 8);
        adult_butt.addBox(-4F, -3F, 3F, 6, 5, 4);
        adult_butt.setRotationPoint(1F, 19F, -1F);
        adult_butt.setTextureSize(64, 32);
        adult_butt.mirror = true;
        adult_crest2 = new ModelRenderer(this, 21, 25);
        adult_crest2.addBox(-2F, -6F, 4F, 2, 5, 2);
        adult_crest2.setRotationPoint(1F, 13F, -4F);
        adult_crest2.setTextureSize(64, 32);
        adult_crest2.mirror = true;
        adult_back = new ModelRenderer(this, 46, 0);
        adult_back.addBox(-3F, -5F, 1F, 4, 2, 5);
        adult_back.setRotationPoint(1F, 19F, -1F);
        adult_back.setTextureSize(64, 32);
        adult_back.mirror = true;
        adult_chest = new ModelRenderer(this, 34, 19);
        adult_chest.addBox(-5F, -3F, -4F, 8, 6, 7);
        adult_chest.setRotationPoint(1F, 19F, -1F);
        adult_chest.setTextureSize(64, 32);
        adult_chest.mirror = true;

        /** Child Models **/
        child_legRight = new ModelRenderer(this, 0, 20);
        child_legRight.addBox(-2F, 0F, -3F, 1, 1, 1);
        child_legRight.setRotationPoint(0F, 22F, 2F);
        child_legRight.setTextureSize(64, 32);
        child_legRight.mirror = true;
        child_legLeft = new ModelRenderer(this, 0, 20);
        child_legLeft.addBox(0F, 0F, -3F, 1, 1, 1);
        child_legLeft.setRotationPoint(1F, 22F, 2F);
        child_legLeft.setTextureSize(64, 32);
        child_legLeft.mirror = true;
        child_wingLeft = new ModelRenderer(this, 0, 23);
        child_wingLeft.addBox(0F, 0F, 0F, 1, 2, 3);
        child_wingLeft.setRotationPoint(2F, 19F, -1F);
        child_wingLeft.setTextureSize(64, 32);
        child_wingLeft.mirror = true;
        setRotateAngle(child_wingLeft, 0F, 0.2617994F, 0F);
        child_footLeft = new ModelRenderer(this, 10, 18);
        child_footLeft.addBox(0F, 1F, -4F, 1, 1, 2);
        child_footLeft.setRotationPoint(1F, 22F, 2F);
        child_footLeft.setTextureSize(64, 32);
        child_footLeft.mirror = true;
        child_wingRight = new ModelRenderer(this, 0, 23);
        child_wingRight.addBox(0F, 0F, 0F, 1, 2, 3);
        child_wingRight.setRotationPoint(-3F, 19F, -2F);
        child_wingRight.setTextureSize(64, 32);
        child_wingRight.mirror = true;
        setRotateAngle(child_wingRight, 0F, -0.2617994F, 0F);
        child_footRight = new ModelRenderer(this, 10, 18);
        child_footRight.addBox(-2F, 1F, -4F, 1, 1, 2);
        child_footRight.setRotationPoint(0F, 22F, 2F);
        child_footRight.setTextureSize(64, 32);
        child_footRight.mirror = true;
        child_tail = new ModelRenderer(this, 0, 29);
        child_tail.setRotationPoint(0F, 21F, -2F);
        child_tail.setTextureSize(64, 32);
        child_tail.mirror = true;
        child_beak = new ModelRenderer(this, 13, 28);
        child_beak.addBox(-0.5F, -3F, -3F, 1, 1, 1);
        child_beak.setRotationPoint(0F, 20F, -2F);
        child_beak.setTextureSize(64, 32);
        child_beak.mirror = true;
        child_chest = new ModelRenderer(this, 0, 8);
        child_chest.addBox(-2F, -2F, -1F, 4, 3, 5);
        child_chest.setRotationPoint(0F, 21F, -2F);
        child_chest.setTextureSize(64, 32);
        child_chest.mirror = true;
        child_head = new ModelRenderer(this, 0, 0);
        child_head.addBox(-2F, -5F, -2F, 4, 4, 4);
        child_head.setRotationPoint(0F, 20F, -2F);
        child_head.setTextureSize(64, 32);
        child_head.mirror = true;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        if (this.isChild) {
            this.setChildAngles(f, f1, f2, f3, f4, f5, entity);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.7F, 0.7F, 0.7F);
            GlStateManager.translate(0F, 0.6F, 0F);
            this.child_head.render(f5);
            this.child_legRight.render(f5);
            this.child_beak.render(f5);
            this.child_chest.render(f5);
            this.child_footLeft.render(f5);
            this.child_footRight.render(f5);
            this.child_legLeft.render(f5);
            this.child_tail.render(f5);
            this.child_wingLeft.render(f5);
            this.child_wingRight.render(f5);
            GlStateManager.popMatrix();
        } else {
            this.setAdultAngles(f, f1, f2, f3, f4, f5, entity);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.7F, 0.7F, 0.7F);
            GlStateManager.translate(0F, 0.6F, 0F);
            this.adult_head.render(f5);
            adult_legRight.render(f5);
            adult_crest1.render(f5);
            adult_legLeft.render(f5);
            adult_wingLeft.render(f5);
            adult_footLeft.render(f5);
            adult_wingRight.render(f5);
            adult_footRight.render(f5);
            adult_head.render(f5);
            adult_tail.render(f5);
            adult_beak.render(f5);
            adult_brest.render(f5);
            adult_butt.render(f5);
            adult_crest2.render(f5);
            adult_back.render(f5);
            adult_chest.render(f5);
            GlStateManager.popMatrix();
        }
    }

    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    //Wings 15F and -15F

    private void setChildAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        this.child_head.rotateAngleX = headPitch * 0.017453292F;
        this.child_head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.child_beak.rotateAngleX = this.child_head.rotateAngleX;
        this.child_beak.rotateAngleY = this.child_head.rotateAngleY;
        this.child_tail.rotateAngleX = this.child_chest.rotateAngleX;
        this.child_legRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.child_footRight.rotateAngleX = this.child_legRight.rotateAngleX;
        this.child_legLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.child_footLeft.rotateAngleX = this.child_legLeft.rotateAngleX;
        this.child_wingRight.rotateAngleZ = ageInTicks;
        this.child_wingLeft.rotateAngleZ = -ageInTicks;
    }

    private void setAdultAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        this.adult_head.rotateAngleX = headPitch * 0.017453292F;
        this.adult_head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.adult_beak.rotateAngleX = this.adult_head.rotateAngleX;
        this.adult_beak.rotateAngleY = this.adult_head.rotateAngleY;
        this.adult_crest1.rotateAngleX = this.adult_head.rotateAngleX;
        this.adult_crest1.rotateAngleY = this.adult_head.rotateAngleY;
        this.adult_crest2.rotateAngleX = this.adult_head.rotateAngleX;
        this.adult_crest2.rotateAngleY = this.adult_head.rotateAngleY;
        this.adult_legRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.adult_footRight.rotateAngleX = this.adult_legRight.rotateAngleX;
        this.adult_legLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.adult_footLeft.rotateAngleX = this.adult_legLeft.rotateAngleX;
        this.adult_wingRight.rotateAngleZ = ageInTicks;
        this.adult_wingLeft.rotateAngleZ = -ageInTicks;
    }
}