package joshie.harvest.buildings.render;

import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.util.Direction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;

public class BuildingRenderer {
    private Direction direction;
    private Building building;
    private ItemStack held;

    public Building getBuilding() {
        return building;
    }

    public void render(World world, VertexBuffer buffer) {
        for (PlaceableBlock placeable: building.getPreviewList()) {
            Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(placeable.getTransformedState(direction), placeable.transformBlockPos(direction), world, buffer);
        }
    }

    public void setDirection(Mirror mirror, Rotation rotation) {
        direction = Direction.withMirrorAndRotation(mirror, rotation);
    }

    public void setBuilding(ItemStack stack) {
        if (stack != held) {
            if (stack.getItem() == HFBuildings.BLUEPRINTS) building = HFBuildings.BLUEPRINTS.getObjectFromStack(stack);
            else building = HFBuildings.STRUCTURES.getObjectFromStack(stack);
            held = stack; //Cache the held item
        }
    }
}
