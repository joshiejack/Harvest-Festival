package joshie.harvest.core.handlers.events;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.blocks.BlockFlower;
import joshie.harvest.blocks.items.ItemBlockFlower;
import joshie.harvest.core.handlers.HFTracker;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.init.HFConfig;
import joshie.harvest.init.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GeneralEvents {
    //Make vanilla hoes useless
    @SubscribeEvent
    public void onUseHoe(UseHoeEvent event) {
        if (HFConfig.vanilla.HOES_ARE_USELESS) {
            event.setCanceled(true);
        }
    }
    
    //Goddess flower spawns goddess
    @SubscribeEvent
    public void onItemExpire(ItemExpireEvent event) {
        World world = event.entityItem.worldObj;
        if (!world.isRemote) {
            if (event.entityItem.isInsideOfMaterial(Material.water)) {
                ItemStack stack = event.entityItem.getEntityItem();
                if (stack != null) {
                    if (stack.getItem() instanceof ItemBlockFlower) {
                        if (stack.getItemDamage() == BlockFlower.GODDESS) {
                            EntityNPC goddess = NPCHelper.getEntityForNPC(null, world, HFNPCs.goddess);
                            goddess.setPosition((int) event.entityItem.posX, (int) event.entityItem.posY + 1, (int) event.entityItem.posZ);
                            world.spawnEntityInWorld(goddess);
                        }
                    }
                }
            }
        }
    }

    //Right clicking flower pot with stick creates goddess
    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack held = event.entityPlayer.getCurrentEquippedItem();
            if (held != null && held.getItem() == Items.stick) {
                if (event.world.getBlock(event.x, event.y, event.z) == Blocks.flower_pot) {
                    TileEntityFlowerPot tile = (TileEntityFlowerPot) event.world.getTileEntity(event.x, event.y, event.z);
                    if (tile.getFlowerPotItem() == Item.getItemFromBlock(Blocks.yellow_flower)) {
                        Block xMinus = event.world.getBlock(event.x - 1, event.y - 1, event.z);
                        Block xPlus = event.world.getBlock(event.x + 1, event.y - 1, event.z);
                        Block zMinus = event.world.getBlock(event.x, event.y - 1, event.z - 1);
                        Block zPlus = event.world.getBlock(event.x, event.y - 1, event.z + 1);
                        int water = 0;
                        int flower = 0;

                        if (xMinus == Blocks.water) water++;
                        if (xPlus == Blocks.water) water++;
                        if (zMinus == Blocks.water) water++;
                        if (zPlus == Blocks.water) water++;
                        xMinus = event.world.getBlock(event.x - 1, event.y, event.z);
                        xPlus = event.world.getBlock(event.x + 1, event.y, event.z);
                        zMinus = event.world.getBlock(event.x, event.y, event.z - 1);
                        zPlus = event.world.getBlock(event.x, event.y, event.z + 1);
                        if (xMinus == Blocks.red_flower || xMinus == Blocks.double_plant || xMinus == Blocks.tallgrass) flower++;
                        if (xPlus == Blocks.red_flower || xPlus == Blocks.double_plant || xPlus == Blocks.tallgrass) flower++;
                        if (zMinus == Blocks.red_flower || zMinus == Blocks.double_plant || zMinus == Blocks.tallgrass) flower++;
                        if (zPlus == Blocks.red_flower || zPlus == Blocks.double_plant || zPlus == Blocks.tallgrass) flower++;
                        
                        if (water == 2 && flower == 2) {
                            if (!event.world.isRemote) {
                                event.world.playAuxSFX(2005, event.x, event.y, event.z, 0);
                                if (event.world.rand.nextInt(5) == 0) {
                                    EntityNPC goddess = NPCHelper.getEntityForNPC(null, event.world, HFNPCs.goddess);
                                    goddess.setPosition((int) event.x, (int) event.y + 1, (int) event.z);
                                    event.world.spawnEntityInWorld(goddess);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Setup the Server
    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        World world = event.world;
        if (!world.isRemote && world.provider.dimensionId == 0) {
            HFTracker.reset(world);
        }
    }

    //Setup the Client
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if (event.gui instanceof GuiSelectWorld || event.gui instanceof GuiMultiplayer) {
            HFTracker.reset(null);
        }
    }

    //Orange Leaves in Autumn
    @SubscribeEvent
    public void getFoliageColor(GetFoliageColor event) {
        if (HFTracker.getCalendar().getDate().getSeason() == Season.AUTUMN) {
            event.newColor = 0xFF9900;
        }
    }
}
