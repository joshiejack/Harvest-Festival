package uk.joshiejack.piscary.client;

import uk.joshiejack.penguinlib.client.renderer.tile.SpecialRendererBubble;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.piscary.tile.TileTrap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
@PenguinLoader(value = "fish_trap", side = Side.CLIENT)
public class SpecialRendererTrap extends SpecialRendererBubble<TileTrap> {
    @Override
    public ItemStack getStack(TileTrap tile) {
        return tile.getStack();
    }

    @Override
    public boolean shouldRender(TileTrap tile) {
        return !tile.isBaited();
    }
}
