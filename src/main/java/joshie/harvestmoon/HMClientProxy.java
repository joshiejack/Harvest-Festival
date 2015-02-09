package joshie.harvestmoon;

import static joshie.harvestmoon.HarvestMoon.handler;
import static joshie.harvestmoon.lib.HMModInfo.JAVAPATH;
import joshie.harvestmoon.blocks.BlockGeneral;
import joshie.harvestmoon.blocks.items.ItemBlockGeneral;
import joshie.harvestmoon.blocks.render.SpecialRendererFryingPan;
import joshie.harvestmoon.blocks.tiles.TileFryingPan;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.entities.EntityNPC;
import joshie.harvestmoon.entities.EntityNPCBuilder;
import joshie.harvestmoon.entities.RenderNPC;
import joshie.harvestmoon.handlers.RenderHandler;
import joshie.harvestmoon.handlers.events.RenderEvents;
import joshie.harvestmoon.init.HMBlocks;
import joshie.harvestmoon.lib.RenderIds;
import joshie.lib.util.EntityFakeItem;
import joshie.lib.util.RenderFakeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class HMClientProxy extends HMCommonProxy {
    @Override
    public void init() {
        handler.resetClient();

        for (Crop crop : Crop.crops) {
            try {
                RenderHandler.registerCrop(crop.getUnlocalizedName(), Class.forName(JAVAPATH + "crops.render." + crop.getRenderName()));
            } catch (Exception e) {}
        }

        BlockGeneral general = ((BlockGeneral) HMBlocks.tiles);
        ItemBlockGeneral item = (ItemBlockGeneral) Item.getItemFromBlock(general);
        for (int i = 0; i < general.getMetaCount(); i++) {
            try {
                String name = sanitizeGeneral(item.getName(new ItemStack(general, 1, i)));
                RenderHandler.register(general, i, Class.forName(JAVAPATH + "blocks.render.Render" + name));
            } catch (Exception e) {}
        }

        RenderIds.ALL = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderHandler());
        FMLCommonHandler.instance().bus().register(new RenderEvents());
        MinecraftForge.EVENT_BUS.register(new RenderEvents());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFryingPan.class, new SpecialRendererFryingPan());
        RenderingRegistry.registerEntityRenderingHandler(EntityNPC.class, new RenderNPC());
        RenderingRegistry.registerEntityRenderingHandler(EntityNPCBuilder.class, new RenderNPC());
        RenderingRegistry.registerEntityRenderingHandler(EntityFakeItem.class, new RenderFakeItem());
    }

    private String sanitizeGeneral(String name) {
        name = name.replace(".", " ");
        name = WordUtils.capitalize(name);
        return name.replace(" ", "");
    }
}
