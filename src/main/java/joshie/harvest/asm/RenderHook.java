package joshie.harvest.asm;

import joshie.harvest.core.HFClientProxy;
import joshie.harvest.core.render.FakeEntityRenderer.EntityItemRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class RenderHook {
    /** Added at the start of renderByItem to render my special renderers **/
    public static boolean render(ItemStack stack) {
        EntityItemRenderer tile = HFClientProxy.RENDER_MAP.get(stack.getItem());
        if (tile == null) return false;
        else {
            tile.setID(stack.getItemDamage()); //Set the id and render the tile
            TileEntityRendererDispatcher.instance.renderTileEntityAt(tile, 0.0D, 0.0D, 0.0D, 0.0F);
            return true;
        }
    }
}
