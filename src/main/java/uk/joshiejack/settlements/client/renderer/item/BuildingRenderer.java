package uk.joshiejack.settlements.client.renderer.item;

import uk.joshiejack.settlements.building.BuildingWorldAccess;
import uk.joshiejack.penguinlib.template.render.TemplateRendererer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class BuildingRenderer extends TemplateRendererer<BuildingWorldAccess> {
    private static int minFloor;

    public BuildingRenderer(BuildingWorldAccess world, int minFloor) {
        super(false, world, setOrigin(minFloor));
    }

    private static BlockPos setOrigin(int minFloor) {
        BuildingRenderer.minFloor = minFloor;
        return BlockPos.ORIGIN;
    }

    @Override
    public void setupRender(BuildingWorldAccess world) {
        for (BlockRenderLayer layer: BlockRenderLayer.values()) {
            BufferBuilder buffer = renderer.getWorldRendererByLayer(layer);
            buffer.begin(7, DefaultVertexFormats.BLOCK);
            for (Map.Entry<BlockPos, IBlockState> placeable: world.getBlockMap().entrySet()) {
                if (placeable.getKey().getY() >= minFloor)
                    addRender(world, placeable.getValue(), placeable.getKey(), layer, buffer);
            }
        }
    }
}
