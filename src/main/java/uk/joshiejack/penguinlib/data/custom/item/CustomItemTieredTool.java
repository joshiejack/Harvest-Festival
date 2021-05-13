package uk.joshiejack.penguinlib.data.custom.item;

import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.data.custom.material.CustomToolMaterialData;

public abstract class CustomItemTieredTool <B, A extends CustomItemTieredTool> extends AbstractItemData<B, A> {
    public String toolMaterial;

    public CustomToolMaterialData getToolMaterial() {
        return CustomLoader.tool_materials.get(toolMaterial);
    }
}
