package uk.joshiejack.horticulture.client.renderer.tileentity;


import uk.joshiejack.horticulture.tileentity.TileSeedMaker;
import uk.joshiejack.penguinlib.client.renderer.tile.SpecialRendererBubble;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("unused")
@PenguinLoader(value = "seed_maker", side = Side.CLIENT)
public class SpecialRendererSeedMaker extends SpecialRendererBubble<TileSeedMaker> {
    @Override
    public float getYOffset() {
        return 2.5F;
    }

    @Override
    public ItemStack getStack(TileSeedMaker tile) {
        return tile.getStack();
    }

    @Override
    public boolean shouldRender(TileSeedMaker tile) {
        return tile.hasSeeds();
    }
}
