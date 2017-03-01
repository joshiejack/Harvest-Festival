package joshie.harvest.core.base.render;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.item.ItemBuilding;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class BuildingDefinition implements ItemMeshDefinition {
    private static BuildingDefinition INSTANCE;
    protected final HashMap<Building, ModelResourceLocation> models = new HashMap<>();
    protected final ItemBuilding item;

    public BuildingDefinition(ItemBuilding item) {
        this.item = item;
        ModelBakery.registerItemVariants(item);
        BuildingDefinition.INSTANCE = this;
    }

    public static void registerEverything() {
        for (Building building : Building.REGISTRY.values()) {
            ModelResourceLocation model = new ModelResourceLocation(new ResourceLocation(building.getResource().getResourceDomain(), "buildings/" + building.getResource().getResourcePath()), "inventory");
            ModelBakery.registerItemVariants(HFBuildings.STRUCTURES, model);
            INSTANCE.models.put(building, model);
        }
    }

    @Override
    @Nonnull
    public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
        return models.get(item.getObjectFromStack(stack));
    }
}
