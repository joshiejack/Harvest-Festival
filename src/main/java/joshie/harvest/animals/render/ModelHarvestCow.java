package joshie.harvest.animals.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelHarvestCow extends ModelBase {
    /** Child Models **/
    public ModelRenderer child_frontRightLeg;
    public ModelRenderer child_backRightLeg;
    public ModelRenderer child_frontLeftLeg;
    public ModelRenderer child_backLeftLeg;
    public ModelRenderer child_belly;
    public ModelRenderer child_backTop;
    public ModelRenderer child_snout;
    public ModelRenderer child_sideRight;
    public ModelRenderer child_tail;
    public ModelRenderer child_backMiddle;
    public ModelRenderer child_sideLeft;
    public ModelRenderer child_body;
    public ModelRenderer child_neck;
    public ModelRenderer child_earRight;
    public ModelRenderer child_earLeft;
    public ModelRenderer child_head;
    
    /** Adult Models **/
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
        this.textureHeight = 128;
        this.child_backMiddle = new ModelRenderer(this, 47, 25);
        this.child_backMiddle.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.child_backMiddle.addBox(-4.5F, -12.0F, -3.5F, 11, 3, 16, 0.0F);
        this.child_neck = new ModelRenderer(this, 87, 29);
        this.child_neck.setRotationPoint(-1.0F, 18.0F, -9.0F);
        this.child_neck.addBox(-3.5F, -14.0F, -1.5F, 9, 10, 2, 0.0F);
        this.child_sideLeft = new ModelRenderer(this, 0, 0);
        this.child_sideLeft.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.child_sideLeft.addBox(6.5F, -10.5F, -3.5F, 1, 9, 14, 0.0F);
        this.child_frontRightLeg = new ModelRenderer(this, 64, 0);
        this.child_frontRightLeg.setRotationPoint(-3.5F, 17.0F, -4.0F);
        this.child_frontRightLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 7, 3, 0.0F);
        this.child_backTop = new ModelRenderer(this, 80, 50);
        this.child_backTop.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.child_backTop.addBox(-4.5F, -13.0F, -3.5F, 11, 1, 13, 0.0F);
        this.child_body = new ModelRenderer(this, 0, 38);
        this.child_body.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.child_body.addBox(-4.5F, -9.0F, -3.5F, 11, 9, 17, 0.0F);
        this.child_tail = new ModelRenderer(this, 35, 2);
        this.child_tail.setRotationPoint(3.0F, 20.0F, 13.0F);
        this.child_tail.addBox(-3.5F, -10.0F, -3.5F, 3, 3, 9, 0.0F);
        this.setRotateAngle(child_tail, 0.40980330836826856F, 0.0F, 0.0F);
        this.child_earLeft = new ModelRenderer(this, 115, 38);
        this.child_earLeft.setRotationPoint(-1.0F, 18.0F, -9.0F);
        this.child_earLeft.addBox(6.5F, -15.5F, -4.5F, 4, 3, 2, 0.0F);
        this.setRotateAngle(child_earLeft, 0.0F, 0.0F, -0.091106186954104F);
        this.child_belly = new ModelRenderer(this, 0, 24);
        this.child_belly.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.child_belly.addBox(-1.5F, 0.0F, -1.5F, 5, 1, 13, 0.0F);
        this.child_head = new ModelRenderer(this, 95, 13);
        this.child_head.setRotationPoint(-1.0F, 18.0F, -9.0F);
        this.child_head.addBox(-4.5F, -16.0F, -5.5F, 11, 10, 4, 0.0F);
        this.child_backLeftLeg = new ModelRenderer(this, 64, 0);
        this.child_backLeftLeg.setRotationPoint(3.5F, 17.0F, 4.0F);
        this.child_backLeftLeg.addBox(-1.5F, 0.0F, -0.5F, 3, 7, 3, 0.0F);
        this.child_frontLeftLeg = new ModelRenderer(this, 64, 0);
        this.child_frontLeftLeg.setRotationPoint(3.5F, 17.0F, -4.0F);
        this.child_frontLeftLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 7, 3, 0.0F);
        this.child_earRight = new ModelRenderer(this, 115, 33);
        this.child_earRight.mirror = true;
        this.child_earRight.setRotationPoint(-1.0F, 18.0F, -7.0F);
        this.child_earRight.addBox(-8.5F, -15.5F, -6.5F, 4, 3, 2, 0.0F);
        this.setRotateAngle(child_earRight, 0.0F, 0.0F, 0.091106186954104F);
        this.child_backRightLeg = new ModelRenderer(this, 64, 0);
        this.child_backRightLeg.setRotationPoint(-3.5F, 17.0F, 4.0F);
        this.child_backRightLeg.addBox(-1.5F, 0.0F, -0.5F, 3, 7, 3, 0.0F);
        this.child_snout = new ModelRenderer(this, 85, 0);
        this.child_snout.setRotationPoint(-1.0F, 18.0F, -9.0F);
        this.child_snout.addBox(-6.5F, -6.0F, -7.5F, 15, 7, 6, 0.0F);
        this.child_sideRight = new ModelRenderer(this, 0, 0);
        this.child_sideRight.setRotationPoint(-1.0F, 18.0F, -5.0F);
        this.child_sideRight.addBox(-5.5F, -10.5F, -3.5F, 1, 9, 14, 0.0F);
        
        /** Adult **/
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.earLeft = new ModelRenderer(this, 115, 38);
        this.earLeft.setRotationPoint(-1.0F, 17.0F, -9.0F);
        this.earLeft.addBox(6.5F, -15.5F, -4.5F, 4, 3, 2, 0.0F);
        this.setRotateAngle(earLeft, 0.0F, 0.0F, -0.091106186954104F);
        this.backRightLeg = new ModelRenderer(this, 64, 0);
        this.backRightLeg.setRotationPoint(-3.5F, 16.0F, 9.0F);
        this.backRightLeg.addBox(-1.5F, 0.0F, -0.5F, 3, 8, 3, 0.0F);
        this.frontLeftLeg = new ModelRenderer(this, 64, 0);
        this.frontLeftLeg.setRotationPoint(3.5F, 16.0F, -4.0F);
        this.frontLeftLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.snout = new ModelRenderer(this, 85, 0);
        this.snout.setRotationPoint(-1.0F, 17.0F, -9.0F);
        this.snout.addBox(-6.5F, -6.0F, -7.5F, 15, 7, 6, 0.0F);
        this.neck = new ModelRenderer(this, 87, 29);
        this.neck.setRotationPoint(-1.0F, 17.0F, -9.0F);
        this.neck.addBox(-3.5F, -14.0F, -1.5F, 9, 10, 2, 0.0F);
        this.sideLeft = new ModelRenderer(this, 64, 96);
        this.sideLeft.setRotationPoint(-1.0F, 17.0F, -5.0F);
        this.sideLeft.addBox(6.5F, -13.5F, -3.5F, 1, 11, 19, 0.0F);
        this.udder = new ModelRenderer(this, 0, 45);
        this.udder.setRotationPoint(-1.0F, 17.0F, -5.0F);
        this.udder.addBox(-0.5F, 1.0F, 9.0F, 3, 2, 3, 0.0F);
        this.earRight = new ModelRenderer(this, 115, 33);
        this.earRight.mirror = true;
        this.earRight.setRotationPoint(-1.0F, 17.0F, -7.0F);
        this.earRight.addBox(-8.5F, -15.5F, -6.5F, 4, 3, 2, 0.0F);
        this.setRotateAngle(earRight, 0.0F, 0.0F, 0.091106186954104F);
        this.frontRightLeg = new ModelRenderer(this, 64, 0);
        this.frontRightLeg.setRotationPoint(-3.5F, 16.0F, -4.0F);
        this.frontRightLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.backLeftLeg = new ModelRenderer(this, 64, 0);
        this.backLeftLeg.setRotationPoint(3.5F, 16.0F, 9.0F);
        this.backLeftLeg.addBox(-1.5F, 0.0F, -0.5F, 3, 8, 3, 0.0F);
        this.hornRight = new ModelRenderer(this, 117, 27);
        this.hornRight.mirror = true;
        this.hornRight.setRotationPoint(-1.0F, 17.0F, -7.0F);
        this.hornRight.addBox(-4.0F, -20.0F, -6.5F, 3, 4, 2, 0.0F);
        this.head = new ModelRenderer(this, 95, 13);
        this.head.setRotationPoint(-1.0F, 17.0F, -9.0F);
        this.head.addBox(-4.5F, -16.0F, -5.5F, 11, 10, 4, 0.0F);
        this.backMiddle = new ModelRenderer(this, 29, 40);
        this.backMiddle.setRotationPoint(-1.0F, 17.0F, -5.0F);
        this.backMiddle.addBox(-4.5F, -12.0F, -3.5F, 11, 3, 21, 0.0F);
        this.backTop = new ModelRenderer(this, 66, 67);
        this.backTop.setRotationPoint(-1.0F, 17.0F, -5.0F);
        this.backTop.addBox(-4.5F, -15.0F, -3.5F, 11, 3, 20, 0.0F);
        this.tail = new ModelRenderer(this, 35, 2);
        this.tail.setRotationPoint(3.0F, 18.0F, 19.0F);
        this.tail.addBox(-3.5F, -10.0F, -3.5F, 3, 3, 9, 0.0F);
        this.setRotateAngle(tail, 0.40980330836826856F, 0.0F, 0.0F);
        this.belly = new ModelRenderer(this, 0, 24);
        this.belly.setRotationPoint(-1.0F, 17.0F, -5.0F);
        this.belly.addBox(-1.5F, 0.0F, -1.5F, 5, 1, 13, 0.0F);
        this.hornLeft = new ModelRenderer(this, 117, 27);
        this.hornLeft.setRotationPoint(-1.0F, 17.0F, -9.0F);
        this.hornLeft.addBox(2.5F, -20.0F, -4.5F, 3, 4, 2, 0.0F);
        this.body = new ModelRenderer(this, 0, 71);
        this.body.setRotationPoint(-1.0F, 17.0F, -5.0F);
        this.body.addBox(-4.5F, -9.0F, -3.5F, 11, 9, 22, 0.0F);
        this.sideRight = new ModelRenderer(this, 64, 96);
        this.sideRight.setRotationPoint(-1.0F, 17.0F, -5.0F);
        this.sideRight.addBox(-5.5F, -13.5F, -3.5F, 1, 11, 19, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        if (this.isChild) {
            this.setChildAngles(f, f1, f2, f3, f4, f5, entity);
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GL11.glTranslatef(0F, 1.3F, 0F);
            this.child_frontLeftLeg.render(f5);
            this.child_frontRightLeg.render(f5);
            this.child_earLeft.render(f5);
            this.child_sideLeft.render(f5);
            this.child_snout.render(f5);
            this.child_backMiddle.render(f5);
            this.child_backRightLeg.render(f5);
            this.child_body.render(f5);
            this.child_backTop.render(f5);
            this.child_earRight.render(f5);
            this.child_head.render(f5);
            this.child_neck.render(f5);
            this.child_belly.render(f5);
            this.child_backLeftLeg.render(f5);
            this.child_sideRight.render(f5);
            this.child_tail.render(f5);
            GL11.glPopMatrix();
        } else {
            this.setAdultAngles(f, f1, f2, f3, f4, f5, entity);
            this.earLeft.render(f5);
            this.backRightLeg.render(f5);
            this.frontLeftLeg.render(f5);
            this.snout.render(f5);
            this.neck.render(f5);
            this.sideLeft.render(f5);
            this.udder.render(f5);
            this.earRight.render(f5);
            this.frontRightLeg.render(f5);
            this.backLeftLeg.render(f5);
            this.hornRight.render(f5);
            this.head.render(f5);
            this.backMiddle.render(f5);
            this.backTop.render(f5);
            this.tail.render(f5);
            this.belly.render(f5);
            this.hornLeft.render(f5);
            this.body.render(f5);
            this.sideRight.render(f5);
        }
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setChildAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        float f6 = (180F / (float) Math.PI);
        this.child_head.rotateAngleX = f4 / (360F / (float) Math.PI);
        this.child_head.rotateAngleY = f3 / (720F / (float) Math.PI);
        this.child_earLeft.rotateAngleX = this.child_head.rotateAngleX;
        this.child_earLeft.rotateAngleY = this.child_head.rotateAngleY;
        this.child_earRight.rotateAngleX = this.child_head.rotateAngleX;
        this.child_earRight.rotateAngleY = this.child_head.rotateAngleY;
        this.child_snout.rotateAngleX = this.child_head.rotateAngleX;
        this.child_snout.rotateAngleY = this.child_head.rotateAngleY;
        this.child_neck.rotateAngleY = this.child_head.rotateAngleY;
        this.child_neck.rotateAngleY = this.child_head.rotateAngleY;
        this.child_frontRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.child_frontLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.child_backLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.child_backRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }

    public void setAdultAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        float f6 = (180F / (float) Math.PI);
        this.head.rotateAngleX = f4 / (360F / (float) Math.PI);
        this.head.rotateAngleY = f3 / (720F / (float) Math.PI);
        this.earLeft.rotateAngleX = this.head.rotateAngleX;
        this.earLeft.rotateAngleY = this.head.rotateAngleY;
        this.earRight.rotateAngleX = this.head.rotateAngleX;
        this.earRight.rotateAngleY = this.head.rotateAngleY;
        this.snout.rotateAngleX = this.head.rotateAngleX;
        this.snout.rotateAngleY = this.head.rotateAngleY;
        this.neck.rotateAngleX = this.head.rotateAngleX;
        this.neck.rotateAngleY = this.head.rotateAngleY;
        this.hornLeft.rotateAngleX = this.head.rotateAngleX;
        this.hornLeft.rotateAngleY = this.head.rotateAngleY;
        this.hornRight.rotateAngleX = this.head.rotateAngleX;
        this.hornRight.rotateAngleY = this.head.rotateAngleY;
        this.frontRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.frontLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.backLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.backRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }
}
