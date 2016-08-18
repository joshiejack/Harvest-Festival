package joshie.harvest.animals.render;

import joshie.harvest.core.render.ModelHF;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.math.MathHelper;

public class ModelHarvestSheep extends ModelBase {
    private final Wooly wooly;
    private final Sheared sheared;
    private final Child child;

    public ModelHarvestSheep() {
        wooly = new Wooly();
        sheared = new Sheared();
        child = new Child();
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (isChild) child.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        else {
            EntitySheep sheep = (EntitySheep) entity;
            if (sheep != null && sheep.getSheared())
                sheared.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            else wooly.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public static class Wooly extends ModelHF {
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

        public Wooly() {
            textureWidth = 128;
            textureHeight = 64;
            head = new ModelRenderer(this, 0, 33);
            head.setRotationPoint(0.0F, 14.0F, -8.0F);
            head.addBox(-5.0F, -6.0F, -5.0F, 10, 10, 4, 0.0F);
            bodyMiddle = new ModelRenderer(this, 64, 19);
            bodyMiddle.setRotationPoint(-1.0F, 19.0F, 0.0F);
            bodyMiddle.addBox(-4.0F, -11.0F, -9.0F, 10, 2, 14, 0.0F);
            bodyTop = new ModelRenderer(this, 0, 0);
            bodyTop.setRotationPoint(-1.0F, 19.0F, 0.0F);
            bodyTop.addBox(-4.0F, -13.0F, -10.0F, 10, 2, 14, 0.0F);
            hair = new ModelRenderer(this, 71, 40);
            hair.setRotationPoint(0.0F, 14.0F, -8.0F);
            hair.addBox(-6.0F, -7.0F, -5.0F, 12, 1, 4, 0.0F);
            hornLeft = new ModelRenderer(this, 64, 10);
            hornLeft.setRotationPoint(0.0F, 14.0F, -8.0F);
            hornLeft.addBox(5.0F, -6.0F, -4.0F, 4, 4, 2, 0.0F);
            neck = new ModelRenderer(this, 0, 22);
            neck.setRotationPoint(0.0F, 14.0F, -8.0F);
            neck.addBox(-6.0F, -4.0F, -1.0F, 12, 6, 1, 0.0F);
            body = new ModelRenderer(this, 22, 33);
            body.setRotationPoint(-1.0F, 19.0F, 0.0F);
            body.addBox(-5.0F, -9.0F, -8.0F, 12, 10, 15, 0.0F);
            backRightLeg = new ModelRenderer(this, 0, 52);
            backRightLeg.setRotationPoint(-5.0F, 18.0F, 3.0F);
            backRightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
            frontLeftLeg = new ModelRenderer(this, 0, 52);
            frontLeftLeg.setRotationPoint(2.0F, 18.0F, -7.0F);
            frontLeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
            bodyLeft = new ModelRenderer(this, 38, 8);
            bodyLeft.setRotationPoint(-1.0F, 19.0F, 0.0F);
            bodyLeft.addBox(7.0F, -8.0F, -7.0F, 2, 8, 13, 0.0F);
            backLeftLeg = new ModelRenderer(this, 0, 52);
            backLeftLeg.setRotationPoint(2.0F, 18.0F, 3.0F);
            backLeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
            frontRightLeg = new ModelRenderer(this, 0, 52);
            frontRightLeg.setRotationPoint(-5.0F, 18.0F, -7.0F);
            frontRightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
            hornRight = new ModelRenderer(this, 64, 10);
            hornRight.mirror = true;
            hornRight.setRotationPoint(0.0F, 14.0F, -8.0F);
            hornRight.addBox(-9.0F, -6.0F, -4.0F, 4, 4, 2, 0.0F);
            bodyRight = new ModelRenderer(this, 38, 8);
            bodyRight.setRotationPoint(-1.0F, 19.0F, 0.0F);
            bodyRight.addBox(-7.0F, -8.0F, -7.0F, 2, 8, 13, 0.0F);
            bodyBack = new ModelRenderer(this, 95, 5);
            bodyBack.setRotationPoint(-1.0F, 19.0F, 0.0F);
            bodyBack.addBox(-4.0F, -8.0F, 6.0F, 10, 8, 2, 0.0F);
            earRight = new ModelRenderer(this, 64, 0);
            earRight.mirror = true;
            earRight.setRotationPoint(0.0F, 14.0F, -8.0F);
            earRight.addBox(-9.0F, -3.0F, -4.0F, 5, 3, 2, 0.0F);
            setRotateAngle(earRight, 0.0F, 0.0F, -0.18203784098300857F);
            earLeft = new ModelRenderer(this, 64, 0);
            earLeft.setRotationPoint(0.0F, 14.0F, -8.0F);
            earLeft.addBox(3.0F, -3.0F, -4.0F, 5, 3, 2, 0.0F);
            setRotateAngle(earLeft, 0.0F, 0.0F, 0.18203784098300857F);
        }

        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            head.render(scale);
            bodyMiddle.render(scale);
            bodyTop.render(scale);
            hair.render(scale);
            hornLeft.render(scale);
            neck.render(scale);
            body.render(scale);
            backRightLeg.render(scale);
            frontLeftLeg.render(scale);
            bodyLeft.render(scale);
            backLeftLeg.render(scale);
            frontRightLeg.render(scale);
            hornRight.render(scale);
            bodyRight.render(scale);
            bodyBack.render(scale);
            earRight.render(scale);
            earLeft.render(scale);
        }

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
            head.rotateAngleX = headPitch / (360F / (float) Math.PI);
            head.rotateAngleY = netHeadYaw / (720F / (float) Math.PI);
            earLeft.rotateAngleX = head.rotateAngleX;
            earLeft.rotateAngleY = head.rotateAngleY;
            earRight.rotateAngleX = head.rotateAngleX;
            earRight.rotateAngleY = head.rotateAngleY;
            hair.rotateAngleX = head.rotateAngleX;
            hair.rotateAngleY = head.rotateAngleY;
            neck.rotateAngleX = head.rotateAngleX;
            neck.rotateAngleY = head.rotateAngleY;
            hornLeft.rotateAngleX = head.rotateAngleX;
            hornLeft.rotateAngleY = head.rotateAngleY;
            hornRight.rotateAngleX = head.rotateAngleX;
            hornRight.rotateAngleY = head.rotateAngleY;
            frontLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            frontRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            backLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            backRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }
    }

    public static class Sheared extends ModelHF {
        private ModelRenderer backRightLeg;
        private ModelRenderer frontLeftLeg;
        private ModelRenderer backLeftLeg;
        private ModelRenderer frontRightLeg;
        private ModelRenderer bodyTop;
        private ModelRenderer bodyMiddle;
        private ModelRenderer tail;
        private ModelRenderer hair;
        private ModelRenderer earLeft;
        private ModelRenderer earRight;
        private ModelRenderer head;
        private ModelRenderer neck;
        private ModelRenderer hornLeft;
        private ModelRenderer hornRight;

        public Sheared() {
            textureWidth = 128;
            textureHeight = 64;
            frontLeftLeg = new ModelRenderer(this, 0, 52);
            frontLeftLeg.setRotationPoint(2.0F, 18.0F, -7.0F);
            frontLeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
            backLeftLeg = new ModelRenderer(this, 0, 52);
            backLeftLeg.setRotationPoint(2.0F, 18.0F, 3.0F);
            backLeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
            tail = new ModelRenderer(this, 64, 20);
            tail.setRotationPoint(-1.0F, 19.0F, 0.0F);
            tail.addBox(-0.4F, -6.0F, 7.0F, 3, 3, 2, 0.0F);
            frontRightLeg = new ModelRenderer(this, 0, 52);
            frontRightLeg.setRotationPoint(-5.0F, 18.0F, -7.0F);
            frontRightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
            bodyTop = new ModelRenderer(this, 64, 19);
            bodyTop.setRotationPoint(-1.0F, 19.0F, 0.0F);
            bodyTop.addBox(-4.0F, -10.0F, -9.0F, 10, 1, 14, 0.0F);
            backRightLeg = new ModelRenderer(this, 0, 52);
            backRightLeg.setRotationPoint(-5.0F, 18.0F, 3.0F);
            backRightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
            bodyMiddle = new ModelRenderer(this, 64, 40);
            bodyMiddle.setRotationPoint(-1.0F, 19.0F, 0.0F);
            bodyMiddle.addBox(-4.4F, -9.0F, -8.0F, 11, 8, 15, 0.0F);
            hair = new ModelRenderer(this, 71, 35);
            hair.setRotationPoint(0.0F, 14.0F, -8.0F);
            hair.addBox(-6.0F, -7.0F, -5.0F, 12, 1, 4, 0.0F);
            earRight = new ModelRenderer(this, 64, 0);
            earRight.mirror = true;
            earRight.setRotationPoint(0.0F, 14.0F, -8.0F);
            earRight.addBox(-9.0F, -3.0F, -4.0F, 5, 3, 2, 0.0F);
            setRotateAngle(earRight, 0.0F, 0.0F, -0.18203784098300857F);
            earLeft = new ModelRenderer(this, 64, 0);
            earLeft.setRotationPoint(0.0F, 14.0F, -8.0F);
            earLeft.addBox(3.0F, -3.0F, -4.0F, 5, 3, 2, 0.0F);
            setRotateAngle(earLeft, 0.0F, 0.0F, 0.18203784098300857F);
            head = new ModelRenderer(this, 0, 33);
            head.setRotationPoint(0.0F, 14.0F, -8.0F);
            head.addBox(-5.0F, -6.0F, -5.0F, 10, 10, 4, 0.0F);
            neck = new ModelRenderer(this, 0, 22);
            neck.setRotationPoint(0.0F, 14.0F, -8.0F);
            neck.addBox(-6.0F, -4.0F, -1.0F, 12, 6, 1, 0.0F);
            hornLeft = new ModelRenderer(this, 64, 10);
            hornLeft.setRotationPoint(0.0F, 14.0F, -8.0F);
            hornLeft.addBox(5.0F, -6.0F, -4.0F, 4, 4, 2, 0.0F);
            hornRight = new ModelRenderer(this, 64, 10);
            hornRight.mirror = true;
            hornRight.setRotationPoint(0.0F, 14.0F, -8.0F);
            hornRight.addBox(-9.0F, -6.0F, -4.0F, 4, 4, 2, 0.0F);
        }

        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            frontLeftLeg.render(scale);
            earLeft.render(scale);
            hair.render(scale);
            earRight.render(scale);
            backLeftLeg.render(scale);
            tail.render(scale);
            hornLeft.render(scale);
            hornRight.render(scale);
            head.render(scale);
            frontRightLeg.render(scale);
            neck.render(scale);
            bodyTop.render(scale);
            backRightLeg.render(scale);
            bodyMiddle.render(scale);
        }

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
            head.rotateAngleX = headPitch / (360F / (float) Math.PI);
            head.rotateAngleY = netHeadYaw / (720F / (float) Math.PI);
            earLeft.rotateAngleX = head.rotateAngleX;
            earLeft.rotateAngleY = head.rotateAngleY;
            earRight.rotateAngleX = head.rotateAngleX;
            earRight.rotateAngleY = head.rotateAngleY;
            hair.rotateAngleX = head.rotateAngleX;
            hair.rotateAngleY = head.rotateAngleY;
            neck.rotateAngleX = head.rotateAngleX;
            neck.rotateAngleY = head.rotateAngleY;
            hornLeft.rotateAngleX = head.rotateAngleX;
            hornLeft.rotateAngleY = head.rotateAngleY;
            hornRight.rotateAngleX = head.rotateAngleX;
            hornRight.rotateAngleY = head.rotateAngleY;
            frontLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            frontRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            backLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            backRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }
    }

    public static class Child extends ModelHF {
        private ModelRenderer backRightLeg;
        private ModelRenderer frontLeftLeg;
        private ModelRenderer backLeftLeg;
        private ModelRenderer frontRightLeg;
        private ModelRenderer neck;
        private ModelRenderer earLeft;
        private ModelRenderer child_tail;
        private ModelRenderer earRight;
        private ModelRenderer head;
        private ModelRenderer child_bodyTop;
        private ModelRenderer child_bodyMiddle;

        public Child() {
            textureWidth = 128;
            textureHeight = 64;
            head = new ModelRenderer(this, 0, 33);
            head.setRotationPoint(0.0F, 14.0F, -8.0F);
            head.addBox(-5.0F, -7.0F, -4.0F, 10, 10, 4, -1.0F);
            child_bodyMiddle = new ModelRenderer(this, 64, 40);
            child_bodyMiddle.setRotationPoint(-1.0F, 19.0F, 0.0F);
            child_bodyMiddle.addBox(-3.4F, -9.0F, -8.0F, 9, 8, 12, 0.0F);
            frontLeftLeg = new ModelRenderer(this, 0, 52);
            frontLeftLeg.setRotationPoint(1.0F, 17.0F, -7.0F);
            frontLeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 7, 3, 0.0F);
            earLeft = new ModelRenderer(this, 64, 0);
            earLeft.setRotationPoint(0.0F, 14.0F, -8.0F);
            earLeft.addBox(3.0F, -4.0F, -3.0F, 5, 3, 2, -0.5F);
            setRotateAngle(earLeft, 0.0F, 0.0F, 0.18203784098300857F);
            frontRightLeg = new ModelRenderer(this, 0, 52);
            frontRightLeg.setRotationPoint(-4.0F, 17.0F, -7.0F);
            frontRightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 7, 3, 0.0F);
            child_bodyTop = new ModelRenderer(this, 64, 19);
            child_bodyTop.setRotationPoint(-1.0F, 19.0F, 0.0F);
            child_bodyTop.addBox(-3.5F, -10.0F, -9.0F, 9, 1, 11, 0.0F);
            neck = new ModelRenderer(this, 0, 22);
            neck.setRotationPoint(0.0F, 14.0F, -8.0F);
            neck.addBox(-4.0F, -4.0F, -1.0F, 8, 6, 1, 0.0F);
            earRight = new ModelRenderer(this, 64, 0);
            earRight.mirror = true;
            earRight.setRotationPoint(0.0F, 14.0F, -8.0F);
            earRight.addBox(-8.0F, -4.0F, -3.0F, 5, 3, 2, -0.5F);
            setRotateAngle(earRight, 0.0F, 0.0F, -0.18203784098300857F);
            child_tail = new ModelRenderer(this, 64, 20);
            child_tail.setRotationPoint(-1.0F, 19.0F, 0.0F);
            child_tail.addBox(-0.4F, -6.0F, 4.0F, 3, 3, 2, 0.0F);
            backLeftLeg = new ModelRenderer(this, 0, 52);
            backLeftLeg.setRotationPoint(1.0F, 17.0F, 0.5F);
            backLeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 7, 3, 0.0F);
            backRightLeg = new ModelRenderer(this, 0, 52);
            backRightLeg.setRotationPoint(-4.0F, 17.0F, 0.5F);
            backRightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 7, 3, 0.0F);
        }


        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.7F, 0.7F, 0.7F);
            GlStateManager.translate(0F, 0.6F, 0F);
            head.render(scale);
            child_bodyMiddle.render(scale);
            frontLeftLeg.render(scale);
            earLeft.render(scale);
            frontRightLeg.render(scale);
            child_bodyTop.render(scale);
            neck.render(scale);
            earRight.render(scale);
            child_tail.render(scale);
            backLeftLeg.render(scale);
            backRightLeg.render(scale);
            GlStateManager.popMatrix();
        }

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
            head.rotateAngleX = headPitch / (360F / (float) Math.PI);
            head.rotateAngleY = netHeadYaw / (720F / (float) Math.PI);
            earLeft.rotateAngleX = head.rotateAngleX;
            earLeft.rotateAngleY = head.rotateAngleY;
            earRight.rotateAngleX = head.rotateAngleX;
            earRight.rotateAngleY = head.rotateAngleY;
            neck.rotateAngleX = head.rotateAngleX;
            neck.rotateAngleY = head.rotateAngleY;
            frontLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            frontRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            backLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            backRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }
    }
}