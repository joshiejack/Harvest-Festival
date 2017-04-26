package joshie.harvest.animals.item;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.entity.EntityHarvestChicken;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;
import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ItemAnimalSpawner extends ItemHFEnum<ItemAnimalSpawner, Spawner> {
    public enum Spawner implements IStringSerializable {
        COW, SHEEP, CHICKEN;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemAnimalSpawner() {
        super(Spawner.class);
    }

    private EntityAgeable getEntityFromEnum(World world, Spawner spawner) {
        switch (spawner) {
            case COW:
                return new EntityHarvestCow(world);
            case SHEEP:
                return new EntityHarvestSheep(world);
            case CHICKEN:
                return new EntityHarvestChicken(world);
            default:
                return null;
        }
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            EntityAgeable entity = getEntityFromEnum(world, getEnumFromStack(stack));
            if (entity != null) {
                if (player.isSneaking()) entity.setGrowingAge(-(int)(TICKS_PER_DAY * HFAnimals.AGING_TIMER));
                entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                AnimalStats stats = EntityHelper.getStats(entity);
                if (stats != null) {
                    world.spawnEntity(entity);
                }
            }
        }

        stack.splitStack(1);
        return EnumActionResult.SUCCESS;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@Nonnull ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
            tooltip.add(TextFormatting.AQUA + TextHelper.translate("spawner.tooltip"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(MODID, getPrefix(values[i]) + "_" + values[i].name().toLowerCase(Locale.ENGLISH)), "inventory"));
        }
    }
}