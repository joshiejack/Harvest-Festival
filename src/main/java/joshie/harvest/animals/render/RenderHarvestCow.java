package joshie.harvest.animals.render;

import joshie.harvest.animals.entity.EntityHarvestCow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHarvestCow extends RenderHarvestAnimal<EntityHarvestCow> {
    public RenderHarvestCow(RenderManager manager) {
        super(manager, new ModelHarvestCow(), "cow");
    }
}
