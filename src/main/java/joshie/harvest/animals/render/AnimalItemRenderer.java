package joshie.harvest.animals.render;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.core.render.FakeEntityRenderer.EntityItemRenderer;
import joshie.harvest.core.render.FakeEntityRenderer.RenderPair;
import net.minecraft.client.model.ModelBase;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimalItemRenderer extends EntityItemRenderer {
    public static final AnimalItemRenderer INSTANCE = new AnimalItemRenderer();

    @SuppressWarnings("deprecation")
    public void register(Spawner spawner, String name, ModelBase model) {
        map.put(spawner.ordinal(), new RenderPair(name, model));
        ForgeHooksClient.registerTESRItemStack(HFAnimals.ANIMAL, spawner.ordinal(), this.getClass());
    }
}
