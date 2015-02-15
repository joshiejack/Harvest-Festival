package joshie.harvestmoon.handlers.events;

import static joshie.harvestmoon.helpers.CalendarHelper.getSeason;
import joshie.harvestmoon.blocks.BlockFlower;
import joshie.harvestmoon.blocks.items.ItemBlockFlower;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.helpers.ClientHelper;
import joshie.harvestmoon.helpers.ServerHelper;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.npc.EntityNPC;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GeneralEvents {
    @SubscribeEvent
    public void onItemExpire(ItemExpireEvent event) {
        World world = event.entityItem.worldObj;
        if (!world.isRemote) {
            if (event.entityItem.isInsideOfMaterial(Material.water)) {
                ItemStack stack = event.entityItem.getEntityItem();
                if (stack != null) {
                    if (stack.getItem() instanceof ItemBlockFlower) {
                        if (stack.getItemDamage() == BlockFlower.GODDESS) {
                            EntityNPC goddess = HMNPCs.goddess.getEntity(world, (int) event.entityItem.posX, (int) event.entityItem.posY, (int) event.entityItem.posZ);
                            goddess.setPosition((int) event.entityItem.posX, (int) event.entityItem.posY + 1, (int) event.entityItem.posZ);
                            world.spawnEntityInWorld(goddess);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        if (event.world.provider.dimensionId == 0) {
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
                ServerHelper.setServer(event.world);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onOpenGUI(GuiOpenEvent event) {
        if (event.gui instanceof GuiSelectWorld || event.gui instanceof GuiMultiplayer) {
            ClientHelper.resetClient();
        }
    }

    @SubscribeEvent
    public void getFoliageColor(GetFoliageColor event) {
        if (getSeason() == Season.AUTUMN) {
            event.newColor = 0xFF9900;
        }
    }
}
