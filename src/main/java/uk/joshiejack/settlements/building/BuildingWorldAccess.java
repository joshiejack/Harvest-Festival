package uk.joshiejack.settlements.building;

import uk.joshiejack.penguinlib.template.Placeable;
import uk.joshiejack.penguinlib.template.Template;
import uk.joshiejack.penguinlib.template.blocks.PlaceableBlock;
import uk.joshiejack.penguinlib.template.blocks.PlaceableDouble;
import uk.joshiejack.penguinlib.template.blocks.PlaceableDoubleOpposite;
import uk.joshiejack.penguinlib.template.render.TemplateWorldAccess;
import uk.joshiejack.penguinlib.util.helpers.minecraft.BlockPosHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public class BuildingWorldAccess extends TemplateWorldAccess {
    public BuildingWorldAccess(Building building, Rotation rotation) {
        Template template = building.getTemplate();
        if (template == null || template.getComponents() == null) return;
        for (Placeable placeable : template.getComponents()) {
            if (placeable == null) continue;
            if (placeable instanceof PlaceableBlock) {
                PlaceableBlock block = (PlaceableBlock) placeable;
                if (block.getBlock() != Blocks.AIR) {
                    IBlockState state = block.getTransformedState(rotation);
                    BlockPos pos = BlockPosHelper.transformBlockPos(block.getOffsetPos(), rotation);
                    mapping.put(pos, state);
                    if (block instanceof PlaceableDouble) {
                        mapping.put(pos.up(), state.getBlock().getStateFromMeta(8));
                    } else if (block instanceof PlaceableDoubleOpposite) {
                        mapping.put(pos.up(), state.getBlock().getStateFromMeta(9));
                    }
                }
            }
        }
    }
}
