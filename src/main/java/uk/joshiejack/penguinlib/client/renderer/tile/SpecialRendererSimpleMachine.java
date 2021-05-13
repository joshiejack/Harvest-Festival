package uk.joshiejack.penguinlib.client.renderer.tile;

import uk.joshiejack.penguinlib.tile.machine.TileMachineSimple;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpecialRendererSimpleMachine extends SpecialRendererBubble<TileMachineSimple> {
    private final float offset;

    public SpecialRendererSimpleMachine(float offset) {
        this.offset = offset;
    }

    @Override
    public float getYOffset() {
        return offset;
    }

    @Override
    public ItemStack getStack(TileMachineSimple tile) {
        return tile.getStack();
    }

    @Override
    public boolean shouldRender(TileMachineSimple tile) {
        return !tile.isActive();
    }
}
