package joshie.harvest.mining.render;

import joshie.harvest.core.render.FakeEntityRenderer.EntityItemRenderer;
import joshie.harvest.core.render.FakeEntityRenderer.RenderPair;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.items.ItemDarkSpawner.DarkSpawner;
import net.minecraft.client.model.ModelBase;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FakeAnimalsItemRenderer extends EntityItemRenderer {
    public static final FakeAnimalsItemRenderer INSTANCE = new FakeAnimalsItemRenderer();

    public void register(DarkSpawner spawner, String name, ModelBase model) {
        map.put(spawner.ordinal(), new RenderPair(name, model));
        ForgeHooksClient.registerTESRItemStack(HFMining.DARK_SPAWNER, spawner.ordinal(), this.getClass());

    }
}
