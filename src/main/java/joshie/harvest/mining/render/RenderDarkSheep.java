package joshie.harvest.mining.render;

import joshie.harvest.animals.render.ModelHarvestSheep;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderDarkSheep extends RenderDarkMob {
    public RenderDarkSheep(RenderManager manager) {
        super(manager, new ModelHarvestSheep.Wooly(), "dark_sheep");
    }
}
