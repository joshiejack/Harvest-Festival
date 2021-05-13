package uk.joshiejack.settlements.item;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.client.renderer.item.BuildingItemRenderer;
import uk.joshiejack.penguinlib.item.base.ItemMultiMap;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

public class ItemBuildingRenderer extends ItemMultiMap<Building> implements IPenguinItem {
    public ItemBuildingRenderer() {
        super(new ResourceLocation(Settlements.MODID, "building_renderer"), Building.REGISTRY);
        setCreativeTab(Settlements.TAB);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "inventory"));
        this.setTileEntityItemStackRenderer(new BuildingItemRenderer());
    }

    @Override
    protected Building getNullEntry() {
        return Building.NULL;
    }
}
