package joshie.harvest.core;

import static joshie.harvest.core.lib.HFModInfo.JAVAPATH;
import joshie.harvest.animals.AnimalTracker;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.animals.render.ChickenRenderFix;
import joshie.harvest.animals.render.ModelHarvestCow;
import joshie.harvest.animals.render.ModelHarvestSheep;
import joshie.harvest.animals.render.RenderHarvestAnimal;
import joshie.harvest.blocks.BlockCookware;
import joshie.harvest.blocks.BlockHFBaseMeta;
import joshie.harvest.blocks.render.RenderCrops;
import joshie.harvest.blocks.render.RenderKitchen;
import joshie.harvest.blocks.render.RenderPreview;
import joshie.harvest.blocks.render.SpecialRendererFryingPan;
import joshie.harvest.blocks.tiles.TileFryingPan;
import joshie.harvest.core.config.Client;
import joshie.harvest.core.handlers.RenderHandler;
import joshie.harvest.core.handlers.events.RenderEvents;
import joshie.harvest.core.helpers.ClientHelper;
import joshie.harvest.core.lib.RenderIds;
import joshie.harvest.core.util.base.ItemBlockBase;
import joshie.harvest.core.util.generic.EntityFakeItem;
import joshie.harvest.core.util.generic.RenderFakeItem;
import joshie.harvest.init.HFBlocks;
import joshie.harvest.init.HFItems;
import joshie.harvest.init.HFShops;
import joshie.harvest.items.render.RenderItemAnimal;
import joshie.harvest.items.render.RenderItemNPC;
import joshie.harvest.npc.EntityNPC;
import joshie.harvest.npc.EntityNPCBuilder;
import joshie.harvest.npc.EntityNPCMiner;
import joshie.harvest.npc.EntityNPCShopkeeper;
import joshie.harvest.npc.RenderNPC;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class HFClientProxy extends HFCommonProxy {
    @Override
    public void init() {
        super.init();

        ClientHelper.resetClient();
        RenderIds.ALL = RenderingRegistry.getNextAvailableRenderId();
        RenderIds.CROPS = RenderingRegistry.getNextAvailableRenderId();
        RenderIds.COOKING = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderHandler());
        RenderingRegistry.registerBlockHandler(new RenderCrops());
        FMLCommonHandler.instance().bus().register(new RenderEvents());
        MinecraftForge.EVENT_BUS.register(new RenderEvents());
        MinecraftForgeClient.registerItemRenderer(HFItems.animal, new RenderItemAnimal());
        MinecraftForgeClient.registerItemRenderer(HFItems.spawnerNPC, new RenderItemNPC());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFryingPan.class, new SpecialRendererFryingPan());
        RenderingRegistry.registerEntityRenderingHandler(EntityNPC.class, new RenderNPC());
        RenderingRegistry.registerEntityRenderingHandler(EntityNPCBuilder.class, new RenderNPC());
        RenderingRegistry.registerEntityRenderingHandler(EntityNPCShopkeeper.class, new RenderNPC());
        RenderingRegistry.registerEntityRenderingHandler(EntityNPCMiner.class, new RenderNPC());
        RenderingRegistry.registerEntityRenderingHandler(EntityFakeItem.class, new RenderFakeItem());
        RenderingRegistry.registerEntityRenderingHandler(EntityHarvestCow.class, new RenderHarvestAnimal(new ModelHarvestCow(), "cow"));
        RenderingRegistry.registerEntityRenderingHandler(EntityHarvestSheep.class, new RenderHarvestAnimal(new ModelHarvestSheep(), "sheep"));
        RenderHandler.register(HFBlocks.cookware, BlockCookware.KITCHEN, RenderKitchen.class);
        registerRenders(HFBlocks.woodmachines);

        for (int i = 0; i < 8; i++) {
            RenderHandler.register(HFBlocks.preview, i, RenderPreview.class);
        }

        if (Client.CHICKEN_OFFSET_FIX) {
            MinecraftForge.EVENT_BUS.register(new ChickenRenderFix());
        }
        
        HFShops.initClient();
    }

    private void registerRenders(Block b) {
        BlockHFBaseMeta block = (BlockHFBaseMeta) b;
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
    
    @Override
    public AnimalTracker getAnimalTracker() {
        return ClientHelper.getAnimalTracker();
    }
}
