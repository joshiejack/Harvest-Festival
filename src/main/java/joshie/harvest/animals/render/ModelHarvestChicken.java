package joshie.harvest.animals.render;

import joshie.harvest.core.base.render.ModelHF;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelHarvestChicken extends ModelBase {
    private final Adult adult;
    private final Child child;

    public ModelHarvestChicken() {
        adult = new Adult();
        child = new Child();
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (isChild) child.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        else adult.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public static class Adult extends ModelHF {
        private ModelRenderer legRight;
        private ModelRenderer crest1;
        private ModelRenderer legLeft;
        private ModelRenderer wingLeft;
        private ModelRenderer footLeft;
        private ModelRenderer wingRight;
        private ModelRenderer footRight;
        private ModelRenderer head;
        private ModelRenderer tail;
        private ModelRenderer beak;
        private ModelRenderer brest;
        private ModelRenderer butt;
        private ModelRenderer crest2;
        private ModelRenderer back;
        private ModelRenderer chest;

        public Adult() {
            textureWidth = 64;
            textureHeight = 32;
            legRight = new ModelRenderer(this, 0, 20);
            legRight.addBox(-2F, 0F, -3F, 2, 1, 2);
            legRight.setRotationPoint(-1F, 22F, 2F);
            legRight.setTextureSize(64, 32);
            legRight.mirror = false;
            crest1 = new ModelRenderer(this, 30, 0);
            crest1.addBox(-2F, -7F, -2F, 2, 2, 6);
            crest1.setRotationPoint(1F, 13F, -4F);
            crest1.setTextureSize(64, 32);
            crest1.mirror = true;
            legLeft = new ModelRenderer(this, 0, 20);
            legLeft.addBox(0F, 0F, -3F, 2, 1, 2);
            legLeft.setRotationPoint(1F, 22F, 2F);
            legLeft.setTextureSize(64, 32);
            legLeft.mirror = true;
            wingLeft = new ModelRenderer(this, 0, 23);
            wingLeft.addBox(0F, 0F, 0F, 1, 4, 5);
            wingLeft.setRotationPoint(4F, 16F, -2F);
            wingLeft.setTextureSize(64, 32);
            wingLeft.mirror = true;
            setRotateAngle(wingLeft, 0F, 0.2617994F, 0F);
            footLeft = new ModelRenderer(this, 10, 18);
            footLeft.addBox(0F, 1F, -5F, 2, 1, 4);
            footLeft.setRotationPoint(1F, 22F, 2F);
            footLeft.setTextureSize(64, 32);
            wingRight = new ModelRenderer(this, 0, 23);
            wingRight.addBox(0F, 0F, 0F, 1, 4, 5);
            wingRight.setRotationPoint(-4F, 16F, -2F);
            wingRight.setTextureSize(64, 32);
            wingRight.mirror = true;
            setRotateAngle(wingRight, 0F, -0.2617994F, 0F);
            footRight = new ModelRenderer(this, 10, 18);
            footRight.addBox(-2F, 1F, -5F, 2, 1, 4);
            footRight.setRotationPoint(-1F, 22F, 2F);
            footRight.setTextureSize(64, 32);
            footRight.mirror = false;
            head = new ModelRenderer(this, 0, 0);
            head.addBox(-5F, -5F, -4F, 8, 8, 8);
            head.setRotationPoint(1F, 13F, -4F);
            head.setTextureSize(64, 32);
            head.mirror = true;
            tail = new ModelRenderer(this, 32, 13);
            tail.addBox(-3F, -3F, 7F, 4, 3, 2);
            tail.setRotationPoint(1F, 19F, -1F);
            tail.setTextureSize(64, 32);
            tail.mirror = true;
            beak = new ModelRenderer(this, 13, 28);
            beak.addBox(-2F, -1F, -6F, 2, 2, 2);
            beak.setRotationPoint(1F, 13F, -4F);
            beak.setTextureSize(64, 32);
            beak.mirror = true;
            brest = new ModelRenderer(this, 22, 18);
            brest.addBox(-4F, -3F, -6F, 6, 5, 2);
            brest.setRotationPoint(1F, 19F, -1F);
            brest.setTextureSize(64, 32);
            brest.mirror = true;
            butt = new ModelRenderer(this, 44, 8);
            butt.addBox(-4F, -3F, 3F, 6, 5, 4);
            butt.setRotationPoint(1F, 19F, -1F);
            butt.setTextureSize(64, 32);
            butt.mirror = true;
            crest2 = new ModelRenderer(this, 21, 25);
            crest2.addBox(-2F, -6F, 4F, 2, 5, 2);
            crest2.setRotationPoint(1F, 13F, -4F);
            crest2.setTextureSize(64, 32);
            crest2.mirror = true;
            back = new ModelRenderer(this, 46, 0);
            back.addBox(-3F, -5F, 1F, 4, 2, 5);
            back.setRotationPoint(1F, 19F, -1F);
            back.setTextureSize(64, 32);
            back.mirror = true;
            chest = new ModelRenderer(this, 34, 19);
            chest.addBox(-5F, -3F, -4F, 8, 6, 7);
            chest.setRotationPoint(1F, 19F, -1F);
            chest.setTextureSize(64, 32);
            chest.mirror = true;
        }

        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.7F, 0.7F, 0.7F);
            GlStateManager.translate(0F, 0.6F, 0F);
            head.render(scale);
            legRight.render(scale);
            crest1.render(scale);
            legLeft.render(scale);
            wingLeft.render(scale);
            footLeft.render(scale);
            wingRight.render(scale);
            footRight.render(scale);
            head.render(scale);
            tail.render(scale);
            beak.render(scale);
            brest.render(scale);
            butt.render(scale);
            crest2.render(scale);
            back.render(scale);
            chest.render(scale);
            GlStateManager.popMatrix();
        }

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
            head.rotateAngleX = headPitch * 0.017453292F;
            head.rotateAngleY = netHeadYaw * 0.017453292F;
            beak.rotateAngleX = head.rotateAngleX;
            beak.rotateAngleY = head.rotateAngleY;
            crest1.rotateAngleX = head.rotateAngleX;
            crest1.rotateAngleY = head.rotateAngleY;
            crest2.rotateAngleX = head.rotateAngleX;
            crest2.rotateAngleY = head.rotateAngleY;
            legRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            footRight.rotateAngleX = legRight.rotateAngleX;
            legLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            footLeft.rotateAngleX = legLeft.rotateAngleX;
            wingRight.rotateAngleZ = ageInTicks;
            wingLeft.rotateAngleZ = -ageInTicks;

        }
    }

    public static class Child extends ModelHF {
        private ModelRenderer legRight;
        private ModelRenderer legLeft;
        private ModelRenderer wingLeft;
        private ModelRenderer footLeft;
        private ModelRenderer wingRight;
        private ModelRenderer footRight;
        private ModelRenderer tail;
        private ModelRenderer beak;
        private ModelRenderer chest;
        private ModelRenderer head;

        public Child() {
            textureWidth = 64;
            textureHeight = 32;
            legRight = new ModelRenderer(this, 0, 20);
            legRight.addBox(-2F, 0F, -3F, 1, 1, 1);
            legRight.setRotationPoint(0F, 22F, 2F);
            legRight.setTextureSize(64, 32);
            legRight.mirror = true;
            legLeft = new ModelRenderer(this, 0, 20);
            legLeft.addBox(0F, 0F, -3F, 1, 1, 1);
            legLeft.setRotationPoint(1F, 22F, 2F);
            legLeft.setTextureSize(64, 32);
            legLeft.mirror = true;
            wingLeft = new ModelRenderer(this, 0, 23);
            wingLeft.addBox(0F, 0F, 0F, 1, 2, 3);
            wingLeft.setRotationPoint(2F, 19F, -1F);
            wingLeft.setTextureSize(64, 32);
            wingLeft.mirror = true;
            setRotateAngle(wingLeft, 0F, 0.2617994F, 0F);
            footLeft = new ModelRenderer(this, 10, 18);
            footLeft.addBox(0F, 1F, -4F, 1, 1, 2);
            footLeft.setRotationPoint(1F, 22F, 2F);
            footLeft.setTextureSize(64, 32);
            footLeft.mirror = true;
            wingRight = new ModelRenderer(this, 0, 23);
            wingRight.addBox(0F, 0F, 0F, 1, 2, 3);
            wingRight.setRotationPoint(-3F, 19F, -2F);
            wingRight.setTextureSize(64, 32);
            wingRight.mirror = true;
            setRotateAngle(wingRight, 0F, -0.2617994F, 0F);
            footRight = new ModelRenderer(this, 10, 18);
            footRight.addBox(-2F, 1F, -4F, 1, 1, 2);
            footRight.setRotationPoint(0F, 22F, 2F);
            footRight.setTextureSize(64, 32);
            footRight.mirror = true;
            tail = new ModelRenderer(this, 0, 29);
            tail.setRotationPoint(0F, 21F, -2F);
            tail.setTextureSize(64, 32);
            tail.mirror = true;
            beak = new ModelRenderer(this, 13, 28);
            beak.addBox(-0.5F, -3F, -3F, 1, 1, 1);
            beak.setRotationPoint(0F, 20F, -2F);
            beak.setTextureSize(64, 32);
            beak.mirror = true;
            chest = new ModelRenderer(this, 0, 8);
            chest.addBox(-2F, -2F, -1F, 4, 3, 5);
            chest.setRotationPoint(0F, 21F, -2F);
            chest.setTextureSize(64, 32);
            chest.mirror = true;
            head = new ModelRenderer(this, 0, 0);
            head.addBox(-2F, -5F, -2F, 4, 4, 4);
            head.setRotationPoint(0F, 20F, -2F);
            head.setTextureSize(64, 32);
            head.mirror = true;
        }

        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.7F, 0.7F, 0.7F);
            GlStateManager.translate(0F, 0.6F, 0F);
            head.render(scale);
            legRight.render(scale);
            beak.render(scale);
            chest.render(scale);
            footLeft.render(scale);
            footRight.render(scale);
            legLeft.render(scale);
            tail.render(scale);
            wingLeft.render(scale);
            wingRight.render(scale);
            GlStateManager.popMatrix();

        }

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
            head.rotateAngleX = headPitch * 0.017453292F;
            head.rotateAngleY = netHeadYaw * 0.017453292F;
            beak.rotateAngleX = head.rotateAngleX;
            beak.rotateAngleY = head.rotateAngleY;
            tail.rotateAngleX = chest.rotateAngleX;
            legRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            footRight.rotateAngleX = legRight.rotateAngleX;
            legLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            footLeft.rotateAngleX = legLeft.rotateAngleX;
            wingRight.rotateAngleZ = ageInTicks;
            wingLeft.rotateAngleZ = -ageInTicks;
        }
    }
}