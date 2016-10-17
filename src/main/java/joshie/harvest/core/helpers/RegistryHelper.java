package joshie.harvest.core.helpers;

import joshie.harvest.core.base.render.FakeEntityRenderer;
import joshie.harvest.core.base.render.FakeEntityRenderer.EntityItemRenderer;
import joshie.harvest.core.proxy.HFClientProxy;
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
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
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

    @SuppressWarnings("unchecked")
    public static void removeSeed(ItemStack stack) {
        try {
            Class<? extends WeightedRandom.Item> seedEntry = (Class<? extends WeightedRandom.Item>) Class.forName("net.minecraftforge.common.ForgeHooks$SeedEntry");
            Field seedField = ReflectionHelper.findField(seedEntry, "seed");
            List<? extends WeightedRandom.Item> seedList = ReflectionHelper.getPrivateValue(ForgeHooks.class, null, "seedList");
            Iterator<? extends WeightedRandom.Item> it = seedList.iterator();
            while ((it.hasNext())) {
                WeightedRandom.Item random = it.next();
                ItemStack seed = ((ItemStack) seedField.get(random));
                if ((stack.getItemDamage() == 0 && seed.getItem() == stack.getItem()) || stack.isItemEqual(seed)) {
                    it.remove();
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException ex) {}
    }
}