package joshie.harvest.core.helpers;

import joshie.harvest.core.proxy.HFClientProxy;
import joshie.harvest.core.base.render.FakeEntityRenderer;
import joshie.harvest.core.base.render.FakeEntityRenderer.EntityItemRenderer;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.render.NPCItemRenderer;
import joshie.harvest.npc.render.NPCItemRenderer.NPCTile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
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

import java.util.Locale;

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
            GameRegistry.registerTileEntity(tile, MODID + ":" + tile.getSimpleName().replace("Tile", "").toLowerCase(Locale.ENGLISH));
        }
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public static void registerNPCRendererItem(NPC npc) {
        ItemStack stack = SPAWNER_NPC.getStackFromObject(npc);
        ForgeHooksClient.registerTESRItemStack(stack.getItem(), stack.getItemDamage(), NPCTile.class);
        ClientRegistry.bindTileEntitySpecialRenderer(NPCTile.class, new NPCItemRenderer());
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

    @SideOnly(Side.CLIENT)
    public static void registerEntityRenderer(Item item, EntityItemRenderer instance) {
        HFClientProxy.RENDER_MAP.put(item, instance);
        ClientRegistry.bindTileEntitySpecialRenderer(instance.getClass(), FakeEntityRenderer.INSTANCE);
    }
}