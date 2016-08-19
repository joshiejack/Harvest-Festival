package joshie.harvest.animals.render;

import joshie.harvest.core.render.ModelHF;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;


public class ModelHarvestCow extends ModelBase {
    private final Adult adult;
    private final Child child;

    public ModelHarvestCow() {
        adult = new Adult();
        child = new Child();
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (isChild) child.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        else adult.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public static class Adult extends ModelHF {
        private ModelRenderer frontRightLeg;
        private ModelRenderer backRightLeg;
        private ModelRenderer frontLeftLeg;
        private ModelRenderer backLeftLeg;
        private ModelRenderer udder;
        private ModelRenderer belly;
        private ModelRenderer backTop;
        private ModelRenderer snout;
        private ModelRenderer sideRight;
        private ModelRenderer tail;
        private ModelRenderer backMiddle;
        private ModelRenderer sideLeft;
        private ModelRenderer body;
        private ModelRenderer neck;
        private ModelRenderer earRight;
        private ModelRenderer earLeft;
        private ModelRenderer hornLeft;
        private ModelRenderer hornRight;
        private ModelRenderer head;

        public Adult() {
            textureWidth = 128;
            textureHeight = 128;
            earLeft = new ModelRenderer(this, 115, 38);
            earLeft.setRotationPoint(-1.0F, 17.0F, -9.0F);
            earLeft.addBox(6.5F, -15.5F, -4.5F, 4, 3, 2, 0.0F);
            setRotateAngle(earLeft, 0.0F, 0.0F, -0.091106186954104F);
            backRightLeg = new ModelRenderer(this, 64, 0);
            backRightLeg.setRotationPoint(-3.5F, 16.0F, 9.0F);
            backRightLeg.addBox(-1.5F, 0.0F, -0.5F, 3, 8, 3, 0.0F);
            frontLeftLeg = new ModelRenderer(this, 64, 0);
            frontLeftLeg.setRotationPoint(3.5F, 16.0F, -4.0F);
            frontLeftLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
            snout = new ModelRenderer(this, 85, 0);
            snout.setRotationPoint(-1.0F, 17.0F, -9.0F);
            snout.addBox(-6.5F, -6.0F, -7.5F, 15, 7, 6, 0.0F);
            neck = new ModelRenderer(this, 87, 29);
            neck.setRotationPoint(-1.0F, 17.0F, -9.0F);
            neck.addBox(-3.5F, -14.0F, -1.5F, 9, 10, 2, 0.0F);
            sideLeft = new ModelRenderer(this, 64, 96);
            sideLeft.setRotationPoint(-1.0F, 17.0F, -5.0F);
            sideLeft.addBox(6.5F, -13.5F, -3.5F, 1, 11, 19, 0.0F);
            udder = new ModelRenderer(this, 0, 45);
            udder.setRotationPoint(-1.0F, 17.0F, -5.0F);
            udder.addBox(-0.5F, 1.0F, 9.0F, 3, 2, 3, 0.0F);
            earRight = new ModelRenderer(this, 115, 33);
            earRight.mirror = true;
            earRight.setRotationPoint(-1.0F, 17.0F, -7.0F);
            earRight.addBox(-8.5F, -15.5F, -6.5F, 4, 3, 2, 0.0F);
            setRotateAngle(earRight, 0.0F, 0.0F, 0.091106186954104F);
            frontRightLeg = new ModelRenderer(this, 64, 0);
            frontRightLeg.setRotationPoint(-3.5F, 16.0F, -4.0F);
            frontRightLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
            backLeftLeg = new ModelRenderer(this, 64, 0);
            backLeftLeg.setRotationPoint(3.5F, 16.0F, 9.0F);
            backLeftLeg.addBox(-1.5F, 0.0F, -0.5F, 3, 8, 3, 0.0F);
            hornRight = new ModelRenderer(this, 117, 27);
            hornRight.mirror = true;
            hornRight.setRotationPoint(-1.0F, 17.0F, -7.0F);
            hornRight.addBox(-4.0F, -20.0F, -6.5F, 3, 4, 2, 0.0F);
            head = new ModelRenderer(this, 95, 13);
            head.setRotationPoint(-1.0F, 17.0F, -9.0F);
            head.addBox(-4.5F, -16.0F, -5.5F, 11, 10, 4, 0.0F);
            backMiddle = new ModelRenderer(this, 29, 40);
            backMiddle.setRotationPoint(-1.0F, 17.0F, -5.0F);
            backMiddle.addBox(-4.5F, -12.0F, -3.5F, 11, 3, 21, 0.0F);
            backTop = new ModelRenderer(this, 66, 67);
            backTop.setRotationPoint(-1.0F, 17.0F, -5.0F);
            backTop.addBox(-4.5F, -15.0F, -3.5F, 11, 3, 20, 0.0F);
            tail = new ModelRenderer(this, 35, 2);
            tail.setRotationPoint(3.0F, 18.0F, 19.0F);
            tail.addBox(-3.5F, -10.0F, -3.5F, 3, 3, 9, 0.0F);
            setRotateAngle(tail, 0.40980330836826856F, 0.0F, 0.0F);
            belly = new ModelRenderer(this, 0, 24);
            belly.setRotationPoint(-1.0F, 17.0F, -5.0F);
            belly.addBox(-1.5F, 0.0F, -1.5F, 5, 1, 13, 0.0F);
            hornLeft = new ModelRenderer(this, 117, 27);
            hornLeft.setRotationPoint(-1.0F, 17.0F, -9.0F);
            hornLeft.addBox(2.5F, -20.0F, -4.5F, 3, 4, 2, 0.0F);
            body = new ModelRenderer(this, 0, 71);
            body.setRotationPoint(-1.0F, 17.0F, -5.0F);
            body.addBox(-4.5F, -9.0F, -3.5F, 11, 9, 22, 0.0F);
            sideRight = new ModelRenderer(this, 64, 96);
            sideRight.setRotationPoint(-1.0F, 17.0F, -5.0F);
            sideRight.addBox(-5.5F, -13.5F, -3.5F, 1, 11, 19, 0.0F);
        }

        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            earLeft.render(scale);
            backRightLeg.render(scale);
            frontLeftLeg.render(scale);
            snout.render(scale);
            neck.render(scale);
            sideLeft.render(scale);
            udder.render(scale);
            earRight.render(scale);
            frontRightLeg.render(scale);
            backLeftLeg.render(scale);
            hornRight.render(scale);
            head.render(scale);
            backMiddle.render(scale);
            backTop.render(scale);
            tail.render(scale);
            belly.render(scale);
            hornLeft.render(scale);
            body.render(scale);
            sideRight.render(scale);
        }

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity)  {
            head.rotateAngleX = headPitch / (360F / (float) Math.PI);
            head.rotateAngleY = netHeadYaw / (720F / (float) Math.PI);
            earLeft.rotateAngleX = head.rotateAngleX;
            earLeft.rotateAngleY = head.rotateAngleY;
            earRight.rotateAngleX = head.rotateAngleX;
            earRight.rotateAngleY = head.rotateAngleY;
            snout.rotateAngleX = head.rotateAngleX;
            snout.rotateAngleY = head.rotateAngleY;
            neck.rotateAngleX = head.rotateAngleX;
            neck.rotateAngleY = head.rotateAngleY;
            hornLeft.rotateAngleX = head.rotateAngleX;
            hornLeft.rotateAngleY = head.rotateAngleY;
            hornRight.rotateAngleX = head.rotateAngleX;
            hornRight.rotateAngleY = head.rotateAngleY;
            frontRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            frontLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            backLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            backRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }
    }

    public static class Child extends ModelHF {
        private ModelRenderer frontRightLeg;
        private ModelRenderer backRightLeg;
        private ModelRenderer frontLeftLeg;
        private ModelRenderer backLeftLeg;
        private ModelRenderer belly;
        private ModelRenderer backTop;
        private ModelRenderer snout;
        private ModelRenderer sideRight;
        private ModelRenderer tail;
        private ModelRenderer backMiddle;
        private ModelRenderer sideLeft;
        private ModelRenderer body;
        private ModelRenderer neck;
        private ModelRenderer earRight;
        private ModelRenderer earLeft;
        private ModelRenderer head;

        public Child() {
            textureWidth = 128;
            textureHeight = 128;
            backMiddle = new ModelRenderer(this, 47, 25);
            backMiddle.setRotationPoint(-1.0F, 18.0F, -5.0F);
            backMiddle.addBox(-4.5F, -12.0F, -3.5F, 11, 3, 16, 0.0F);
            neck = new ModelRenderer(this, 87, 29);
            neck.setRotationPoint(-1.0F, 18.0F, -9.0F);
            neck.addBox(-3.5F, -14.0F, -1.5F, 9, 10, 2, 0.0F);
            sideLeft = new ModelRenderer(this, 0, 0);
            sideLeft.setRotationPoint(-1.0F, 18.0F, -5.0F);
            sideLeft.addBox(6.5F, -10.5F, -3.5F, 1, 9, 14, 0.0F);
            frontRightLeg = new ModelRenderer(this, 64, 0);
            frontRightLeg.setRotationPoint(-3.5F, 17.0F, -4.0F);
            frontRightLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 7, 3, 0.0F);
            backTop = new ModelRenderer(this, 80, 50);
            backTop.setRotationPoint(-1.0F, 18.0F, -5.0F);
            backTop.addBox(-4.5F, -13.0F, -3.5F, 11, 1, 13, 0.0F);
            body = new ModelRenderer(this, 0, 38);
            body.setRotationPoint(-1.0F, 18.0F, -5.0F);
            body.addBox(-4.5F, -9.0F, -3.5F, 11, 9, 17, 0.0F);
            tail = new ModelRenderer(this, 35, 2);
            tail.setRotationPoint(3.0F, 20.0F, 13.0F);
            tail.addBox(-3.5F, -10.0F, -3.5F, 3, 3, 9, 0.0F);
            setRotateAngle(tail, 0.40980330836826856F, 0.0F, 0.0F);
            earLeft = new ModelRenderer(this, 115, 38);
            earLeft.setRotationPoint(-1.0F, 18.0F, -9.0F);
            earLeft.addBox(6.5F, -15.5F, -4.5F, 4, 3, 2, 0.0F);
            setRotateAngle(earLeft, 0.0F, 0.0F, -0.091106186954104F);
            belly = new ModelRenderer(this, 0, 24);
            belly.setRotationPoint(-1.0F, 18.0F, -5.0F);
            belly.addBox(-1.5F, 0.0F, -1.5F, 5, 1, 13, 0.0F);
            head = new ModelRenderer(this, 95, 13);
            head.setRotationPoint(-1.0F, 18.0F, -9.0F);
            head.addBox(-4.5F, -16.0F, -5.5F, 11, 10, 4, 0.0F);
            backLeftLeg = new ModelRenderer(this, 64, 0);
            backLeftLeg.setRotationPoint(3.5F, 17.0F, 4.0F);
            backLeftLeg.addBox(-1.5F, 0.0F, -0.5F, 3, 7, 3, 0.0F);
            frontLeftLeg = new ModelRenderer(this, 64, 0);
            frontLeftLeg.setRotationPoint(3.5F, 17.0F, -4.0F);
            frontLeftLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 7, 3, 0.0F);
            earRight = new ModelRenderer(this, 115, 33);
            earRight.mirror = true;
            earRight.setRotationPoint(-1.0F, 18.0F, -7.0F);
            earRight.addBox(-8.5F, -15.5F, -6.5F, 4, 3, 2, 0.0F);
            setRotateAngle(earRight, 0.0F, 0.0F, 0.091106186954104F);
            backRightLeg = new ModelRenderer(this, 64, 0);
            backRightLeg.setRotationPoint(-3.5F, 17.0F, 4.0F);
            backRightLeg.addBox(-1.5F, 0.0F, -0.5F, 3, 7, 3, 0.0F);
            snout = new ModelRenderer(this, 85, 0);
            snout.setRotationPoint(-1.0F, 18.0F, -9.0F);
            snout.addBox(-6.5F, -6.0F, -7.5F, 15, 7, 6, 0.0F);
            sideRight = new ModelRenderer(this, 0, 0);
            sideRight.setRotationPoint(-1.0F, 18.0F, -5.0F);
            sideRight.addBox(-5.5F, -10.5F, -3.5F, 1, 9, 14, 0.0F);
        }

        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0F, 1.3F, 0F);
            frontLeftLeg.render(scale);
            frontRightLeg.render(scale);
            earLeft.render(scale);
            sideLeft.render(scale);
            snout.render(scale);
            backMiddle.render(scale);
            backRightLeg.render(scale);
            body.render(scale);
            backTop.render(scale);
            earRight.render(scale);
            head.render(scale);
            neck.render(scale);
            belly.render(scale);
            backLeftLeg.render(scale);
            sideRight.render(scale);
            tail.render(scale);
            GlStateManager.popMatrix();
        }

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity)  {
            head.rotateAngleX = headPitch / (360F / (float) Math.PI);
            head.rotateAngleY = netHeadYaw / (720F / (float) Math.PI);
            earLeft.rotateAngleX = head.rotateAngleX;
            earLeft.rotateAngleY = head.rotateAngleY;
            earRight.rotateAngleX = head.rotateAngleX;
            earRight.rotateAngleY = head.rotateAngleY;
            snout.rotateAngleX = head.rotateAngleX;
            snout.rotateAngleY = head.rotateAngleY;
            neck.rotateAngleY = head.rotateAngleY;
            neck.rotateAngleY = head.rotateAngleY;
            frontRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            frontLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            backLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            backRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }
    }
}