package joshie.harvestmoon.animals.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelHarvestCow extends ModelBase {
    public ModelRenderer frontRightLeg;
    public ModelRenderer backRightLeg;
    public ModelRenderer frontLeftLeg;
    public ModelRenderer backLeftLeg;
    public ModelRenderer udder;
    public ModelRenderer belly;
    public ModelRenderer backTop;
    public ModelRenderer snout;
    public ModelRenderer sideRight;
    public ModelRenderer tail;
    public ModelRenderer backMiddle;
    public ModelRenderer sideLeft;
    public ModelRenderer body;
    public ModelRenderer neck;
    public ModelRenderer earRight;
    public ModelRenderer earLeft;
    public ModelRenderer hornLeft;
    public ModelRenderer hornRight;
    public ModelRenderer head;

    public ModelHarvestCow() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.backMiddle = new ModelRenderer(this, 47, 25);
        this.backMiddle.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.backMiddle.addBox(-4.5F, -12.0F, -3.5F, 11, 3, 16, 0.0F);
        this.neck = new ModelRenderer(this, 87, 29);
        this.neck.setRotationPoint(-1.0F, 18.0F, -9.0F);
        this.neck.addBox(-3.5F, -14.0F, -1.5F, 9, 10, 2, 0.0F);
        this.sideLeft = new ModelRenderer(this, 0, 0);
        this.sideLeft.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.sideLeft.addBox(6.5F, -10.5F, -3.5F, 1, 9, 14, 0.0F);
        this.frontRightLeg = new ModelRenderer(this, 64, 0);
        this.frontRightLeg.setRotationPoint(-3.5F, 17.0F, -4.0F);
        this.frontRightLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 7, 3, 0.0F);
        this.backTop = new ModelRenderer(this, 80, 50);
        this.backTop.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.backTop.addBox(-4.5F, -13.0F, -3.5F, 11, 1, 13, 0.0F);
        this.body = new ModelRenderer(this, 0, 38);
        this.body.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.body.addBox(-4.5F, -9.0F, -3.5F, 11, 9, 17, 0.0F);
        this.hornRight = new ModelRenderer(this, 117, 27);
        this.hornRight.mirror = true;
        this.hornRight.setRotationPoint(-1.0F, 18.0F, -7.0F);
        this.hornRight.addBox(-4.0F, -20.0F, -6.5F, 3, 4, 2, 0.0F);
        this.tail = new ModelRenderer(this, 35, 2);
        this.tail.setRotationPoint(3.0F, 20.0F, 13.0F);
        this.tail.addBox(-3.5F, -10.0F, -3.5F, 3, 3, 9, 0.0F);
        this.setRotateAngle(tail, 0.40980330836826856F, 0.0F, 0.0F);
        this.hornLeft = new ModelRenderer(this, 117, 27);
        this.hornLeft.setRotationPoint(-1.0F, 18.0F, -9.0F);
        this.hornLeft.addBox(2.5F, -20.0F, -4.5F, 3, 4, 2, 0.0F);
        this.earLeft = new ModelRenderer(this, 115, 38);
        this.earLeft.setRotationPoint(-1.0F, 18.0F, -9.0F);
        this.earLeft.addBox(6.5F, -15.5F, -4.5F, 4, 3, 2, 0.0F);
        this.setRotateAngle(earLeft, 0.0F, 0.0F, -0.091106186954104F);
        this.belly = new ModelRenderer(this, 0, 24);
        this.belly.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.belly.addBox(-1.5F, 0.0F, -1.5F, 5, 1, 13, 0.0F);
        this.head = new ModelRenderer(this, 95, 13);
        this.head.setRotationPoint(-1.0F, 18.0F, -9.0F);
        this.head.addBox(-4.5F, -16.0F, -5.5F, 11, 10, 4, 0.0F);
        this.backLeftLeg = new ModelRenderer(this, 64, 0);
        this.backLeftLeg.setRotationPoint(3.5F, 17.0F, 4.0F);
        this.backLeftLeg.addBox(-1.5F, 0.0F, -0.5F, 3, 7, 3, 0.0F);
        this.frontLeftLeg = new ModelRenderer(this, 64, 0);
        this.frontLeftLeg.setRotationPoint(3.5F, 17.0F, -4.0F);
        this.frontLeftLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 7, 3, 0.0F);
        this.udder = new ModelRenderer(this, 0, 45);
        this.udder.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.udder.addBox(-0.5F, 1.0F, 5.0F, 3, 2, 3, 0.0F);
        this.earRight = new ModelRenderer(this, 115, 33);
        this.earRight.mirror = true;
        this.earRight.setRotationPoint(-1.0F, 18.0F, -7.0F);
        this.earRight.addBox(-8.5F, -15.5F, -6.5F, 4, 3, 2, 0.0F);
        this.setRotateAngle(earRight, 0.0F, 0.0F, 0.091106186954104F);
        this.backRightLeg = new ModelRenderer(this, 64, 0);
        this.backRightLeg.setRotationPoint(-3.5F, 17.0F, 4.0F);
        this.backRightLeg.addBox(-1.5F, 0.0F, -0.5F, 3, 7, 3, 0.0F);
        this.snout = new ModelRenderer(this, 85, 0);
        this.snout.setRotationPoint(-1.0F, 18.0F, -9.0F);
        this.snout.addBox(-6.5F, -6.0F, -7.5F, 15, 7, 6, 0.0F);
        this.sideRight = new ModelRenderer(this, 0, 0);
        this.sideRight.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.sideRight.addBox(-5.5F, -10.5F, -3.5F, 1, 9, 14, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glTranslatef(0F, 1.25F, 0F);
        this.frontLeftLeg.render(f5);
        this.frontRightLeg.render(f5);
        this.earLeft.render(f5);
        this.sideLeft.render(f5);
        //this.hornLeft.render(f5);
        this.snout.render(f5);
        this.backMiddle.render(f5);
        this.backRightLeg.render(f5);
        this.body.render(f5);
        this.backTop.render(f5);
        this.earRight.render(f5);
        this.head.render(f5);
        this.neck.render(f5);
        this.belly.render(f5);
        this.backLeftLeg.render(f5);
        //this.hornRight.render(f5);
        this.sideRight.render(f5);
        this.tail.render(f5);
        //this.udder.render(f5);
        GL11.glPopMatrix();
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        float f6 = (180F / (float) Math.PI);
        this.head.rotateAngleX = f4 / (360F / (float) Math.PI);
        this.head.rotateAngleY = f3 / (360F / (float) Math.PI);
        this.earLeft.rotateAngleX = this.head.rotateAngleX;
        this.earLeft.rotateAngleY = this.head.rotateAngleY;
        this.earRight.rotateAngleX = this.head.rotateAngleX;
        this.earRight.rotateAngleY = this.head.rotateAngleY;
        this.hornLeft.rotateAngleX = this.head.rotateAngleX;
        this.hornLeft.rotateAngleY = this.head.rotateAngleY;
        this.hornRight.rotateAngleX = this.head.rotateAngleX;
        this.hornRight.rotateAngleY = this.head.rotateAngleY;
        this.snout.rotateAngleX = this.head.rotateAngleX;
        this.snout.rotateAngleY = this.head.rotateAngleY;
        this.neck.rotateAngleY = this.head.rotateAngleY;
        this.neck.rotateAngleY = this.head.rotateAngleY;
        this.frontRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.frontLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.backLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.backRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }
}
