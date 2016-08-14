package joshie.harvest.core.helpers.generic;

import joshie.harvest.core.render.FakeEntityRenderer;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.render.FakeNPCRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.npc.HFNPCs.SPAWNER_NPC;

public class RegistryHelper {
    public static void registerSounds(String... sounds) {
        for (String sound : sounds) {
            ResourceLocation resource = new ResourceLocation(MODID, sound);
            GameRegistry.register(new SoundEvent(resource), resource);
        }
    }

    public static void registerTiles(Class<? extends TileEntity>... tiles) {
        for (Class<? extends TileEntity> tile : tiles) {
            GameRegistry.registerTileEntity(tile, MODID + ":" + tile.getSimpleName().replace("Tile", "").toLowerCase());
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerEntityRendererItem(String name, ItemStack stack, ModelBase model) {
        Class fake = FakeTileHelper.getFakeClass("Fake" + WordUtils.capitalizeFully(name.replace("_", " ")).replace(" ", ""), HFModInfo.FAKEANIMAL);
        if (fake != null) {
            ForgeHooksClient.registerTESRItemStack(stack.getItem(), stack.getItemDamage(), fake);
            ClientRegistry.bindTileEntitySpecialRenderer(fake, new FakeEntityRenderer(name, model));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerNPCRendererItem(NPC npc) {
        Class fake = FakeTileHelper.getFakeClass(npc.getRegistryName().toString().replace(":", ""), HFModInfo.FAKENPC);
        if (fake != null) {
            ItemStack stack = SPAWNER_NPC.getStackFromObject(npc);
            ForgeHooksClient.registerTESRItemStack(stack.getItem(), stack.getItemDamage(), fake);
            ClientRegistry.bindTileEntitySpecialRenderer(fake, new FakeNPCRenderer(npc));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerFluidBlockRendering(Block block, String name) {
        final ModelResourceLocation fluidLocation = new ModelResourceLocation(MODID + ":fluids", name);
        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return fluidLocation;
            }
        });
    }
}