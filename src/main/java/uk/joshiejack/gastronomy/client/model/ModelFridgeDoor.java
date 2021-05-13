package uk.joshiejack.gastronomy.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelFridgeDoor extends ModelBase {
    private final ModelRenderer topHandleBar;
    private final ModelRenderer topHandleRoof;
    private final ModelRenderer topHandleBase;
    private final ModelRenderer bottomHandleBar;
    private final ModelRenderer bottomHandleRoof;
    private final ModelRenderer bottomHandleBase;
    public final ModelRenderer topDoor;
    public final ModelRenderer bottomDoor;

    public ModelFridgeDoor() {
        textureWidth = 64;
        textureHeight = 32;

        topHandleBar = new ModelRenderer(this, 0, 6);
        topHandleBar.addBox(-11F, -12F, -1.5F, 1, 3, 1);
        topHandleBar.setRotationPoint(7F, 8F, -7F);
        topHandleBar.setTextureSize(64, 32);
        topHandleBar.mirror = true;
        topDoor = new ModelRenderer(this, 34, 20);
        topDoor.addBox(-14F, -15F, 0F, 14, 11, 1);
        topDoor.setRotationPoint(7F, 8F, -7F);
        topDoor.setTextureSize(64, 32);
        topDoor.mirror = true;
        topHandleRoof = new ModelRenderer(this, 0, 0);
        topHandleRoof.addBox(-11F, -12F, -0.5F, 1, 1, 1);
        topHandleRoof.setRotationPoint(7F, 8F, -7F);
        topHandleRoof.setTextureSize(64, 32);
        topHandleRoof.mirror = true;
        topHandleBase = new ModelRenderer(this, 0, 3);
        topHandleBase.addBox(-11F, -10F, -0.5F, 1, 1, 1);
        topHandleBase.setRotationPoint(7F, 8F, -7F);
        topHandleBase.setTextureSize(64, 32);
        topHandleBase.mirror = true;
        bottomDoor = new ModelRenderer(this, 0, 13);
        bottomDoor.addBox(-14F, -3F, 0F, 14, 18, 1);
        bottomDoor.setRotationPoint(7F, 8F, -7F);
        bottomDoor.setTextureSize(64, 32);
        bottomDoor.mirror = true;
        bottomHandleBar = new ModelRenderer(this, 0, 6);
        bottomHandleBar.addBox(-11F, -1F, -1.5F, 1, 3, 1);
        bottomHandleBar.setRotationPoint(7F, 8F, -7F);
        bottomHandleBar.setTextureSize(64, 32);
        bottomHandleBar.mirror = true;
        bottomHandleRoof = new ModelRenderer(this, 0, 0);
        bottomHandleRoof.addBox(-11F, -1F, -0.5F, 1, 1, 1);
        bottomHandleRoof.setRotationPoint(7F, 8F, -7F);
        bottomHandleRoof.setTextureSize(64, 32);
        bottomHandleRoof.mirror = true;
        bottomHandleBase = new ModelRenderer(this, 0, 3);
        bottomHandleBase.addBox(-11F, 1F, -0.5F, 1, 1, 1);
        bottomHandleBase.setRotationPoint(7F, 8F, -7F);
        bottomHandleBase.setTextureSize(64, 32);
        bottomHandleBase.mirror = true;
    }

    public void renderAll() {
        topHandleBar.rotateAngleY = topDoor.rotateAngleY;
        topHandleBase.rotateAngleY = topDoor.rotateAngleY;
        topHandleRoof.rotateAngleY = topDoor.rotateAngleY;
        bottomHandleBar.rotateAngleY = bottomDoor.rotateAngleY;
        bottomHandleBase.rotateAngleY = bottomDoor.rotateAngleY;
        bottomHandleRoof.rotateAngleY = bottomDoor.rotateAngleY;
        topHandleBar.render(0.0625F);
        topDoor.render(0.0625F);
        topHandleRoof.render(0.0625F);
        topHandleBase.render(0.0625F);
        bottomDoor.render(0.0625F);
        bottomHandleBar.render(0.0625F);
        bottomHandleRoof.render(0.0625F);
        bottomHandleBase.render(0.0625F);
    }
}
