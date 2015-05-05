package joshie.harvestmoon.core;

import static joshie.harvestmoon.core.lib.HMModInfo.JAVAPATH;
import joshie.harvestmoon.animals.ChickenRenderFix;
import joshie.harvestmoon.animals.EntityHarvestCow;
import joshie.harvestmoon.animals.EntityHarvestSheep;
import joshie.harvestmoon.animals.render.ModelHarvestCow;
import joshie.harvestmoon.animals.render.ModelHarvestSheep;
import joshie.harvestmoon.animals.render.RenderHarvestAnimal;
import joshie.harvestmoon.blocks.BlockHMBaseMeta;
import joshie.harvestmoon.blocks.render.RenderCrops;
import joshie.harvestmoon.blocks.render.RenderPreview;
import joshie.harvestmoon.blocks.render.SpecialRendererFryingPan;
import joshie.harvestmoon.blocks.tiles.TileFryingPan;
import joshie.harvestmoon.core.config.Client;
import joshie.harvestmoon.core.config.General;
import joshie.harvestmoon.core.handlers.RenderHandler;
import joshie.harvestmoon.core.handlers.events.RenderEvents;
import joshie.harvestmoon.core.helpers.ClientHelper;
import joshie.harvestmoon.core.lib.RenderIds;
import joshie.harvestmoon.core.util.base.ItemBlockBase;
import joshie.harvestmoon.core.util.generic.EntityFakeItem;
import joshie.harvestmoon.core.util.generic.RenderFakeItem;
import joshie.harvestmoon.init.HMBlocks;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.items.render.RenderItemAnimal;
import joshie.harvestmoon.items.render.RenderSeedBag;
import joshie.harvestmoon.npc.EntityNPC;
import joshie.harvestmoon.npc.EntityNPCBuilder;
import joshie.harvestmoon.npc.EntityNPCMiner;
import joshie.harvestmoon.npc.EntityNPCShopkeeper;
import joshie.harvestmoon.npc.RenderNPC;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class HMClientProxy extends HMCommonProxy {
    @Override
    public void init() {
        super.init();
        
        ClientHelper.resetClient();
        RenderIds.ALL = RenderingRegistry.getNextAvailableRenderId();
        RenderIds.CROPS = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderHandler());
        RenderingRegistry.registerBlockHandler(new RenderCrops());
        FMLCommonHandler.instance().bus().register(new RenderEvents());
        MinecraftForge.EVENT_BUS.register(new RenderEvents());
        MinecraftForgeClient.registerItemRenderer(HMItems.seeds, new RenderSeedBag());
        MinecraftForgeClient.registerItemRenderer(HMItems.animal, new RenderItemAnimal());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFryingPan.class, new SpecialRendererFryingPan());
        RenderingRegistry.registerEntityRenderingHandler(EntityNPC.class, new RenderNPC());
        RenderingRegistry.registerEntityRenderingHandler(EntityNPCBuilder.class, new RenderNPC());
        RenderingRegistry.registerEntityRenderingHandler(EntityNPCShopkeeper.class, new RenderNPC());
        RenderingRegistry.registerEntityRenderingHandler(EntityNPCMiner.class, new RenderNPC());
        RenderingRegistry.registerEntityRenderingHandler(EntityFakeItem.class, new RenderFakeItem());
        RenderingRegistry.registerEntityRenderingHandler(EntityHarvestCow.class, new RenderHarvestAnimal(new ModelHarvestCow(), "cow"));
        RenderingRegistry.registerEntityRenderingHandler(EntityHarvestSheep.class, new RenderHarvestAnimal(new ModelHarvestSheep(), "sheep"));
        registerRenders(HMBlocks.cookware);
        registerRenders(HMBlocks.woodmachines);
        
        for (int i = 0; i < 8; i++) {
            RenderHandler.register(HMBlocks.preview, i, RenderPreview.class);
        }
        
        if (Client.CHICKEN_OFFSET_FIX) {
            MinecraftForge.EVENT_BUS.register(new ChickenRenderFix());
        }
        
        if (General.DEBUG_MODE) {
            //MinecraftForgeClient.registerItemRenderer(HMItems.structures, new RenderBuilding());
        }
    }

    private void registerRenders(Block b) {
        BlockHMBaseMeta block = (BlockHMBaseMeta) b;
        ItemBlockBase item = (ItemBlockBase) Item.getItemFromBlock(block);
        for (int i = 0; i < block.getMetaCount(); i++) {
            try {
                String name = sanitizeGeneral(item.getName(new ItemStack(block, 1, i)));
                RenderHandler.register(block, i, Class.forName(JAVAPATH + "blocks.render.Render" + name));
            } catch (Exception e) {}
        }
    }

    private String sanitizeGeneral(String name) {
        name = name.replace(".", " ");
        name = WordUtils.capitalize(name);
        return name.replace(" ", "");
    }
}
