package joshie.harvest.cooking.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelOvenDoor extends ModelBase {
    public ModelRenderer handle;
    public ModelRenderer door;

    public ModelOvenDoor() {
        textureWidth = 32;
        textureHeight = 16;
        handle = new ModelRenderer(this, 0, 0);
        handle.addBox(-2.5F, -6F, -1F, 5, 1, 1);
        handle.setRotationPoint(0F, 21F, -7F);
        handle.setTextureSize(32, 16);
        handle.mirror = true;
        door = new ModelRenderer(this, 0, 3);
        door.addBox(-5F, -7F, 0F, 10, 7, 1);
        door.setRotationPoint(0F, 21F, -7F);
        door.setTextureSize(32, 16);
        door.mirror = true;
    }

    public void renderAll() {
        handle.rotateAngleX = door.rotateAngleX;
        handle.render(0.0625F);
        door.render(0.0625F);
    }
}
