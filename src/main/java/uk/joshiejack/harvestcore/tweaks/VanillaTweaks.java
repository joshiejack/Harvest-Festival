package uk.joshiejack.harvestcore.tweaks;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.HCConfig;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class VanillaTweaks {
    @SuppressWarnings("deprecation")
    public static void fuckWithItems() {
        Item.REGISTRY.forEach(item -> {
            int maxStack = item.getItemStackLimit();
            if (maxStack  >= 60) {
                item.setMaxStackSize(60);
            } else if (maxStack == 30) {
                item.setMaxStackSize(30);
            } else if (maxStack == 10) {
                item.setMaxStackSize(10);
            }
        });

        Item.getItemFromBlock(Blocks.CHEST).setMaxStackSize(1);
    }

    @SubscribeEvent //Add the player when you toss an item...
    public static void onItemTossed(ItemTossEvent event) {
        event.getEntityItem().setThrower(event.getPlayer().getName());
    }

    @SubscribeEvent
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (!event.isSpawner() && event.getWorld().provider.getDimension() == 0) {
            if (event.getEntityLiving() instanceof IMob) {
                if (HCConfig.mobSpawnsMaxHeight <= 255) {
                    BlockPos target = new BlockPos(event.getX(), event.getY(), event.getZ());
                    World world = event.getWorld();
                    if (world.canBlockSeeSky(target) || target.getY() > HCConfig.mobSpawnsMaxHeight) {
                        event.setResult(Event.Result.DENY);
                    }
                }
            } else {
                if (HCConfig.disableOverworldAnimals && !event.isSpawner() && event.getEntityLiving() instanceof IAnimals) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.HarvestDropsEvent event) {
        if (event.getHarvester() != null) {
            if (event.getState().getBlock().isWood(event.getWorld(), event.getPos())) {
                ItemStack tool = event.getHarvester().getHeldItemMainhand();
                if (tool.getItem().getToolClasses(tool).stream().noneMatch(s -> s.contains("axe"))) {
                    event.getDrops().clear(); //Clear out the drops if it isn't an axe
                }
            }
        }
    }
}
