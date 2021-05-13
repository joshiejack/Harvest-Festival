package uk.joshiejack.harvestcore.client.renderer.fertilizer;

import uk.joshiejack.harvestcore.registry.Fertilizer;
import uk.joshiejack.penguinlib.template.render.TemplateWorldAccess;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

public class FertilizerWorldAccess extends TemplateWorldAccess {
    final Fertilizer fertilizer;
    FertilizerWorldAccess(Fertilizer fertilizer, Collection<BlockPos> blockPos) {
        blockPos.forEach(pos -> mapping.put(pos, Blocks.FARMLAND.getDefaultState())); //Do we need this?
        this.fertilizer = fertilizer;
    }
}
