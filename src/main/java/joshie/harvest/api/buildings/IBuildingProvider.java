package joshie.harvest.api.buildings;

import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IBuildingProvider {
    IBuilding getBuilding();
    PlaceableNPC getNPCOffset(String npc_location);
    EnumActionResult generate(World world, BlockPos pos);
    List<PlaceableBlock> getPreviewList();
    List<Placeable> getFullList();
    ItemStack getPreview();
    int getSize();
}
