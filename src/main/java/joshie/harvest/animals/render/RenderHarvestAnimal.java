package joshie.harvest.animals.render;

import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderHarvestAnimal<T extends EntityLiving> extends RenderLiving<T> {
    protected final ResourceLocation texture_child;
    protected final ResourceLocation texture_adult;


    public RenderHarvestAnimal(RenderManager manager, ModelBase model, String animal) {
        super(manager, model, 1F);

        texture_child = new ResourceLocation(HFModInfo.MODID, "textures/entity/" + animal + "_child.png");
        texture_adult = new ResourceLocation(HFModInfo.MODID, "textures/entity/" + animal + "_adult.png");
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityLiving entityLiving) {
        return entityLiving.isChild() ? texture_child : texture_adult;
    }
}