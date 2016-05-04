package joshie.harvest.animals.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.math.MathHelper;

public class ModelHarvestSheep extends ModelBase {
    /**
     * Adult Models
     **/
    private ModelRenderer bodyTop;
    private ModelRenderer backRightLeg;
    private ModelRenderer frontLeftLeg;
    private ModelRenderer backLeftLeg;
    private ModelRenderer frontRightLeg;
    private ModelRenderer bodyLeft;
    private ModelRenderer bodyBack;
    private ModelRenderer bodyRight;
    private ModelRenderer hornLeft;
    private ModelRenderer body;
    private ModelRenderer neck;
    private ModelRenderer hair;
    private ModelRenderer hornRight;
    private ModelRenderer earLeft;
    private ModelRenderer bodyMiddle;
    private ModelRenderer earRight;
    private ModelRenderer head;

    /**
     * Sheared Models
     **/
    private ModelRenderer sheared_backRightLeg;
    private ModelRenderer sheared_frontLeftLeg;
    private ModelRenderer sheared_backLeftLeg;
    private ModelRenderer sheared_frontRightLeg;
    private ModelRenderer sheared_bodyTop;
    private ModelRenderer sheared_bodyMiddle;
    private ModelRenderer sheared_tail;
    private ModelRenderer sheared_hair;

    /**
     * Child Models
     **/
    private ModelRenderer child_backRightLeg;
    private ModelRenderer child_frontLeftLeg;
    private ModelRenderer child_backLeftLeg;
    private ModelRenderer child_frontRightLeg;
    private ModelRenderer child_neck;
    private ModelRenderer child_earLeft;
    private ModelRenderer child_tail;
    private ModelRenderer child_earRight;
    private ModelRenderer child_head;
    private ModelRenderer child_bodyTop;
    private ModelRenderer child_bodyMiddle;

    public ModelHarvestSheep() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.head = new ModelRenderer(this, 0, 33);
        this.head.setRotationPoint(0.0F, 14.0F, -8.0F);
        this.head.addBox(-5.0F, -6.0F, -5.0F, 10, 10, 4, 0.0F);
        this.bodyMiddle = new ModelRenderer(this, 64, 19);
        this.bodyMiddle.setRotationPoint(-1.0F, 19.0F, 0.0F);
        this.bodyMiddle.addBox(-4.0F, -11.0F, -9.0F, 10, 2, 14, 0.0F);
        this.bodyTop = new ModelRenderer(this, 0, 0);
        this.bodyTop.setRotationPoint(-1.0F, 19.0F, 0.0F);
        this.bodyTop.addBox(-4.0F, -13.0F, -10.0F, 10, 2, 14, 0.0F);
        this.hair = new ModelRenderer(this, 71, 40);
        this.hair.setRotationPoint(0.0F, 14.0F, -8.0F);
        this.hair.addBox(-6.0F, -7.0F, -5.0F, 12, 1, 4, 0.0F);
        this.hornLeft = new ModelRenderer(this, 64, 10);
        this.hornLeft.setRotationPoint(0.0F, 14.0F, -8.0F);
        this.hornLeft.addBox(5.0F, -6.0F, -4.0F, 4, 4, 2, 0.0F);
        this.neck = new ModelRenderer(this, 0, 22);
        this.neck.setRotationPoint(0.0F, 14.0F, -8.0F);
        this.neck.addBox(-6.0F, -4.0F, -1.0F, 12, 6, 1, 0.0F);
        this.body = new ModelRenderer(this, 22, 33);
        this.body.setRotationPoint(-1.0F, 19.0F, 0.0F);
        this.body.addBox(-5.0F, -9.0F, -8.0F, 12, 10, 15, 0.0F);
        this.backRightLeg = new ModelRenderer(this, 0, 52);
        this.backRightLeg.setRotationPoint(-5.0F, 18.0F, 3.0F);
        this.backRightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
        this.frontLeftLeg = new ModelRenderer(this, 0, 52);
        this.frontLeftLeg.setRotationPoint(2.0F, 18.0F, -7.0F);
        this.frontLeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
        this.bodyLeft = new ModelRenderer(this, 38, 8);
        this.bodyLeft.setRotationPoint(-1.0F, 19.0F, 0.0F);
        this.bodyLeft.addBox(7.0F, -8.0F, -7.0F, 2, 8, 13, 0.0F);
        this.backLeftLeg = new ModelRenderer(this, 0, 52);
        this.backLeftLeg.setRotationPoint(2.0F, 18.0F, 3.0F);
        this.backLeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
        this.frontRightLeg = new ModelRenderer(this, 0, 52);
        this.frontRightLeg.setRotationPoint(-5.0F, 18.0F, -7.0F);
        this.frontRightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
        this.hornRight = new ModelRenderer(this, 64, 10);
        this.hornRight.mirror = true;
        this.hornRight.setRotationPoint(0.0F, 14.0F, -8.0F);
        this.hornRight.addBox(-9.0F, -6.0F, -4.0F, 4, 4, 2, 0.0F);
        this.bodyRight = new ModelRenderer(this, 38, 8);
        this.bodyRight.setRotationPoint(-1.0F, 19.0F, 0.0F);
        this.bodyRight.addBox(-7.0F, -8.0F, -7.0F, 2, 8, 13, 0.0F);
        this.bodyBack = new ModelRenderer(this, 95, 5);
        this.bodyBack.setRotationPoint(-1.0F, 19.0F, 0.0F);
        this.bodyBack.addBox(-4.0F, -8.0F, 6.0F, 10, 8, 2, 0.0F);
        this.earRight = new ModelRenderer(this, 64, 0);
        this.earRight.mirror = true;
        this.earRight.setRotationPoint(0.0F, 14.0F, -8.0F);
        this.earRight.addBox(-9.0F, -3.0F, -4.0F, 5, 3, 2, 0.0F);
        this.setRotateAngle(earRight, 0.0F, 0.0F, -0.18203784098300857F);
        this.earLeft = new ModelRenderer(this, 64, 0);
        this.earLeft.setRotationPoint(0.0F, 14.0F, -8.0F);
        this.earLeft.addBox(3.0F, -3.0F, -4.0F, 5, 3, 2, 0.0F);
        this.setRotateAngle(earLeft, 0.0F, 0.0F, 0.18203784098300857F);

        /** Sheared Models **/
        this.sheared_frontLeftLeg = new ModelRenderer(this, 0, 52);
        this.sheared_frontLeftLeg.setRotationPoint(2.0F, 18.0F, -7.0F);
        this.sheared_frontLeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
        this.sheared_backLeftLeg = new ModelRenderer(this, 0, 52);
        this.sheared_backLeftLeg.setRotationPoint(2.0F, 18.0F, 3.0F);
        this.sheared_backLeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
        this.sheared_tail = new ModelRenderer(this, 64, 20);
        this.sheared_tail.setRotationPoint(-1.0F, 19.0F, 0.0F);
        this.sheared_tail.addBox(-0.4F, -6.0F, 7.0F, 3, 3, 2, 0.0F);
        this.sheared_frontRightLeg = new ModelRenderer(this, 0, 52);
        this.sheared_frontRightLeg.setRotationPoint(-5.0F, 18.0F, -7.0F);
        this.sheared_frontRightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
        this.sheared_bodyTop = new ModelRenderer(this, 64, 19);
        this.sheared_bodyTop.setRotationPoint(-1.0F, 19.0F, 0.0F);
        this.sheared_bodyTop.addBox(-4.0F, -10.0F, -9.0F, 10, 1, 14, 0.0F);
        this.sheared_backRightLeg = new ModelRenderer(this, 0, 52);
        this.sheared_backRightLeg.setRotationPoint(-5.0F, 18.0F, 3.0F);
        this.sheared_backRightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
        this.sheared_bodyMiddle = new ModelRenderer(this, 64, 40);
        this.sheared_bodyMiddle.setRotationPoint(-1.0F, 19.0F, 0.0F);
        this.sheared_bodyMiddle.addBox(-4.4F, -9.0F, -8.0F, 11, 8, 15, 0.0F);
        this.sheared_hair = new ModelRenderer(this, 71, 35);
        this.sheared_hair.setRotationPoint(0.0F, 14.0F, -8.0F);
        this.sheared_hair.addBox(-6.0F, -7.0F, -5.0F, 12, 1, 4, 0.0F);

        /** Child Models **/
        this.child_head = new ModelRenderer(this, 0, 33);
        this.child_head.setRotationPoint(0.0F, 14.0F, -8.0F);
        this.child_head.addBox(-5.0F, -7.0F, -4.0F, 10, 10, 4, -1.0F);
        this.child_bodyMiddle = new ModelRenderer(this, 64, 40);
        this.child_bodyMiddle.setRotationPoint(-1.0F, 19.0F, 0.0F);
        this.child_bodyMiddle.addBox(-3.4F, -9.0F, -8.0F, 9, 8, 12, 0.0F);
        this.child_frontLeftLeg = new ModelRenderer(this, 0, 52);
        this.child_frontLeftLeg.setRotationPoint(1.0F, 17.0F, -7.0F);
        this.child_frontLeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 7, 3, 0.0F);
        this.child_earLeft = new ModelRenderer(this, 64, 0);
        this.child_earLeft.setRotationPoint(0.0F, 14.0F, -8.0F);
        this.child_earLeft.addBox(3.0F, -4.0F, -3.0F, 5, 3, 2, -0.5F);
        this.setRotateAngle(child_earLeft, 0.0F, 0.0F, 0.18203784098300857F);
        this.child_frontRightLeg = new ModelRenderer(this, 0, 52);
        this.child_frontRightLeg.setRotationPoint(-4.0F, 17.0F, -7.0F);
        this.child_frontRightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 7, 3, 0.0F);
        this.child_bodyTop = new ModelRenderer(this, 64, 19);
        this.child_bodyTop.setRotationPoint(-1.0F, 19.0F, 0.0F);
        this.child_bodyTop.addBox(-3.5F, -10.0F, -9.0F, 9, 1, 11, 0.0F);
        this.child_neck = new ModelRenderer(this, 0, 22);
        this.child_neck.setRotationPoint(0.0F, 14.0F, -8.0F);
        this.child_neck.addBox(-4.0F, -4.0F, -1.0F, 8, 6, 1, 0.0F);
        this.child_earRight = new ModelRenderer(this, 64, 0);
        this.child_earRight.mirror = true;
        this.child_earRight.setRotationPoint(0.0F, 14.0F, -8.0F);
        this.child_earRight.addBox(-8.0F, -4.0F, -3.0F, 5, 3, 2, -0.5F);
        this.setRotateAngle(child_earRight, 0.0F, 0.0F, -0.18203784098300857F);
        this.child_tail = new ModelRenderer(this, 64, 20);
        this.child_tail.setRotationPoint(-1.0F, 19.0F, 0.0F);
        this.child_tail.addBox(-0.4F, -6.0F, 4.0F, 3, 3, 2, 0.0F);
        this.child_backLeftLeg = new ModelRenderer(this, 0, 52);
        this.child_backLeftLeg.setRotationPoint(1.0F, 17.0F, 0.5F);
        this.child_backLeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 7, 3, 0.0F);
        this.child_backRightLeg = new ModelRenderer(this, 0, 52);
        this.child_backRightLeg.setRotationPoint(-4.0F, 17.0F, 0.5F);
        this.child_backRightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 7, 3, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        if (this.isChild) {
            this.setChildAngles(f, f1, f2, f3, f4, f5, entity);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.7F, 0.7F, 0.7F);
            GlStateManager.translate(0F, 0.6F, 0F);
            this.child_head.render(f5);
            this.child_bodyMiddle.render(f5);
            this.child_frontLeftLeg.render(f5);
            this.child_earLeft.render(f5);
            this.child_frontRightLeg.render(f5);
            this.child_bodyTop.render(f5);
            this.child_neck.render(f5);
            this.child_earRight.render(f5);
            this.child_tail.render(f5);
            this.child_backLeftLeg.render(f5);
            this.child_backRightLeg.render(f5);
            GlStateManager.popMatrix();
        } else {
            EntitySheep sheep = (EntitySheep) entity;
            if (sheep.getSheared()) {
                this.setShearedAngles(f, f1, f2, f3, f4, f5, entity);
                this.sheared_frontLeftLeg.render(f5);
                this.earLeft.render(f5);
                this.sheared_hair.render(f5);
                this.earRight.render(f5);
                this.sheared_backLeftLeg.render(f5);
                this.sheared_tail.render(f5);
                this.hornLeft.render(f5);
                this.hornRight.render(f5);
                this.head.render(f5);
                this.sheared_frontRightLeg.render(f5);
                this.neck.render(f5);
                this.sheared_bodyTop.render(f5);
                this.sheared_backRightLeg.render(f5);
                this.sheared_bodyMiddle.render(f5);
            } else {
                this.setAdultAngles(f, f1, f2, f3, f4, f5, entity);
                this.head.render(f5);
                this.bodyMiddle.render(f5);
                this.bodyTop.render(f5);
                this.hair.render(f5);
                this.hornLeft.render(f5);
                this.neck.render(f5);
                this.body.render(f5);
                this.backRightLeg.render(f5);
                this.frontLeftLeg.render(f5);
                this.bodyLeft.render(f5);
                this.backLeftLeg.render(f5);
                this.frontRightLeg.render(f5);
                this.hornRight.render(f5);
                this.bodyRight.render(f5);
                this.bodyBack.render(f5);
                this.earRight.render(f5);
                this.earLeft.render(f5);
            }
        }
    }

    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    private void setChildAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        this.child_head.rotateAngleX = f4 / (360F / (float) Math.PI);
        this.child_head.rotateAngleY = f3 / (720F / (float) Math.PI);
        this.child_earLeft.rotateAngleX = this.child_head.rotateAngleX;
        this.child_earLeft.rotateAngleY = this.child_head.rotateAngleY;
        this.child_earRight.rotateAngleX = this.child_head.rotateAngleX;
        this.child_earRight.rotateAngleY = this.child_head.rotateAngleY;
        this.child_neck.rotateAngleX = this.child_head.rotateAngleX;
        this.child_neck.rotateAngleY = this.child_head.rotateAngleY;
        this.child_frontLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.child_frontRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.child_backLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.child_backRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }

    private void setAdultAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        this.head.rotateAngleX = f4 / (360F / (float) Math.PI);
        this.head.rotateAngleY = f3 / (720F / (float) Math.PI);
        this.earLeft.rotateAngleX = this.head.rotateAngleX;
        this.earLeft.rotateAngleY = this.head.rotateAngleY;
        this.earRight.rotateAngleX = this.head.rotateAngleX;
        this.earRight.rotateAngleY = this.head.rotateAngleY;
        this.hair.rotateAngleX = this.head.rotateAngleX;
        this.hair.rotateAngleY = this.head.rotateAngleY;
        this.neck.rotateAngleX = this.head.rotateAngleX;
        this.neck.rotateAngleY = this.head.rotateAngleY;
        this.hornLeft.rotateAngleX = this.head.rotateAngleX;
        this.hornLeft.rotateAngleY = this.head.rotateAngleY;
        this.hornRight.rotateAngleX = this.head.rotateAngleX;
        this.hornRight.rotateAngleY = this.head.rotateAngleY;
        this.frontLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.frontRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.backLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.backRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }

    private void setShearedAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        this.head.rotateAngleX = f4 / (360F / (float) Math.PI);
        this.head.rotateAngleY = f3 / (720F / (float) Math.PI);
        this.earLeft.rotateAngleX = this.head.rotateAngleX;
        this.earLeft.rotateAngleY = this.head.rotateAngleY;
        this.earRight.rotateAngleX = this.head.rotateAngleX;
        this.earRight.rotateAngleY = this.head.rotateAngleY;
        this.sheared_hair.rotateAngleX = this.head.rotateAngleX;
        this.sheared_hair.rotateAngleY = this.head.rotateAngleY;
        this.neck.rotateAngleX = this.head.rotateAngleX;
        this.neck.rotateAngleY = this.head.rotateAngleY;
        this.hornLeft.rotateAngleX = this.head.rotateAngleX;
        this.hornLeft.rotateAngleY = this.head.rotateAngleY;
        this.hornRight.rotateAngleX = this.head.rotateAngleX;
        this.hornRight.rotateAngleY = this.head.rotateAngleY;
        this.sheared_frontLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.sheared_frontRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.sheared_backLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.sheared_backRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }
}