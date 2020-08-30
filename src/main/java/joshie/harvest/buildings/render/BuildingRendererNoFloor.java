package joshie.harvest.buildings.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@SuppressWarnings("WeakerAccess")
public class BuildingRendererNoFloor extends BuildingRenderer {

    BuildingRendererNoFloor(BuildingAccess world, BuildingKey key) {
        super(world, key);
    }

    @Override
    protected void setupRender(BuildingAccess world) {
        for (BlockRenderLayer layer: BlockRenderLayer.values()) {
            BufferBuilder buffer = renderer.getWorldRendererByLayer(layer);
            buffer.begin(7, DefaultVertexFormats.BLOCK);
            world.getBlockMap().entrySet().stream().filter(placeable -> placeable.getKey().getY() != 0)
                    .forEach(placeable -> addRender(world, placeable.getValue(), placeable.getKey(), layer, buffer));
        }
    }
}
