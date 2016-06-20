package joshie.harvest.core.helpers.generic;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.render.FakeAnimalRenderer;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.base.BlockHFEnum;
import joshie.harvest.core.util.base.ItemBlockHF;
import joshie.harvest.core.util.base.ItemHFEnum;
import joshie.harvest.core.util.base.ItemHFFML;
import joshie.harvest.core.util.generic.Library;
import joshie.harvest.crops.blocks.BlockHFCrops;
import joshie.harvest.crops.items.ItemHFSeeds;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.render.FakeNPCRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.npc.HFNPCs.SPAWNER_NPC;

public class RegistryHelper {

    public static Item registerItem(Item item, String name) {
        name = name.replace(".", "_");

        GameRegistry.register(item, new ResourceLocation(MODID, name));

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            if (item instanceof ItemHFSeeds) {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(MODID, "crops_seeds"), "inventory"));
            } else if (item instanceof ItemHFFML) {
                ((ItemHFFML)item).registerModels(item, name);
            } else if (item instanceof ItemHFEnum) {
                ((ItemHFEnum)item).registerModels(item, name);
            } else if (item.getHasSubtypes()) {
                List<ItemStack> subItems = new ArrayList<ItemStack>();
                if (item.getCreativeTabs() != null && item.getCreativeTabs().length > 0) {
                    for (CreativeTabs tab : item.getCreativeTabs()) {
                        item.getSubItems(item, tab, subItems);
                    }
                }

                for (ItemStack stack : subItems) {
                    String subItemName = item.getUnlocalizedName(stack).replace("item.", "").replace(".", "_");

                    ModelLoader.setCustomModelResourceLocation(item, item.getDamage(stack), new ModelResourceLocation(new ResourceLocation(MODID, subItemName), "inventory"));
                    HarvestFestival.LOGGER.log(Level.INFO, "Sub item name " + subItemName);
                }
            } else {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(MODID, name), "inventory"));
                HarvestFestival.LOGGER.log(Level.INFO, "Item Name " + name);
            }
        }

        if (Library.DEBUG_ON) {
            Library.log(Level.DEBUG, "Successfully registered the item " + item.getClass().getSimpleName() + " as " + MODID + ":" + name);
        }

        return item;
    }

    public static Block registerBlock(Block block, String name) {
        ResourceLocation resource = new ResourceLocation(MODID, name.replace(".", "_"));
        ItemBlock item = block instanceof BlockHFEnum ? new ItemBlockHF((BlockHFEnum)block) : new ItemBlock(block);
        GameRegistry.register(block, resource);
        GameRegistry.register(item, resource);

        if (!(block instanceof BlockHFCrops)) {
            HarvestFestival.proxy.setBlockModelResourceLocation(Item.getItemFromBlock(block), name);
        }

        if (Library.DEBUG_ON) {
            Library.log(Level.DEBUG, "Successfully registered the block " + block.getClass().getSimpleName() + " as " + MODID + ":" + name);
        }

        return block;
    }

    public static void registerTiles(Class<? extends TileEntity>... tiles) {
        for (Class<? extends TileEntity> tile : tiles) {
            GameRegistry.registerTileEntity(tile, MODID + ":" + tile.getSimpleName().replace("Tile", "").toLowerCase());
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerEntityRendererItem(ItemStack stack, ModelBase model) {
        String name = model.getClass().getSimpleName().replace("ModelHarvest", "").toLowerCase();
        Class fake = FakeTileHelper.getFakeClass("Fake" + name, HFModInfo.FAKEANIMAL);
        if (fake != null) {
            ForgeHooksClient.registerTESRItemStack(stack.getItem(), stack.getItemDamage(), fake);
            ClientRegistry.bindTileEntitySpecialRenderer(fake, new FakeAnimalRenderer(name, model));
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
}