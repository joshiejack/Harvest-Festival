package uk.joshiejack.penguinlib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import uk.joshiejack.penguinlib.PenguinCommon;
import uk.joshiejack.penguinlib.client.util.ModelCache;
import uk.joshiejack.penguinlib.data.textures.PenguinResourcePack;
import uk.joshiejack.penguinlib.tile.machine.DoubleMachine;
import uk.joshiejack.penguinlib.tile.machine.TileMachineRegistry;
import uk.joshiejack.penguinlib.client.renderer.tile.SpecialRendererSimpleMachine;

import java.util.List;
import java.util.Map;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

@SuppressWarnings("unused, rawtypes")
public class PenguinClient extends PenguinCommon {
    public static final TileEntitySpecialRenderer SINGLE_MACHINE = new SpecialRendererSimpleMachine(1.25F);
    public static final TileEntitySpecialRenderer DOUBLE_MACHINE = new SpecialRendererSimpleMachine(2.25F);

    @SuppressWarnings("unchecked")
    @Override
    public void onConstruction() {
        try {
                ((List<IResourcePack>) ObfuscationReflectionHelper.getPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), "resourcePackList"))
                        .add(new PenguinResourcePack()); //Load textures from files
        } catch (Exception ignored) {}
    }

    @Override
    protected boolean isClient() {
        return true;
    }

    @SuppressWarnings("unchecked, deprecation")
    @Override
    public void setupClient(ASMDataTable.ASMData data, Map<Class, PenguinRegister> runnableMap) {
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(ModelCache.INSTANCE);
        RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> registry = ReflectionHelper.getPrivateValue(TileEntity.class, null, "REGISTRY", "field_190562_f");
        try {
            Map<String, Object> map = data.getAnnotationInfo();
            String loadingData = map.get("value") != null ? (String) map.get("value") : "";
            Class clazz = Class.forName(data.getClassName());
            if (isClient() && TileEntitySpecialRenderer.class.isAssignableFrom(clazz)) {
                Class<? extends TileEntity> tile = registry.getObject(new ResourceLocation(MOD_ID, loadingData));
                ClientRegistry.bindTileEntitySpecialRenderer(tile, (TileEntitySpecialRenderer) clazz.newInstance());
            } else if (DoubleMachine.class.isAssignableFrom(clazz)) {
                Class<? extends TileEntity> tile = registry.getObject(new ResourceLocation(MOD_ID, loadingData));
                ClientRegistry.bindTileEntitySpecialRenderer(tile, DOUBLE_MACHINE);
            } else if (TileMachineRegistry.class.isAssignableFrom(clazz)) {
                Class<? extends TileEntity> tile = registry.getObject(new ResourceLocation(MOD_ID, loadingData));
                ClientRegistry.bindTileEntitySpecialRenderer(tile, SINGLE_MACHINE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
