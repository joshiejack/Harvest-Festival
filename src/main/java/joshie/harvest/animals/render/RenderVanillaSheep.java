package joshie.harvest.animals.render;

import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVanillaSheep extends RenderSheep {
    public RenderVanillaSheep(RenderManager manager) {
        super(manager, new ModelSheep2(), 0.7F);
    }
}
