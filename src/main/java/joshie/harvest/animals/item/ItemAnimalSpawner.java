package joshie.harvest.animals.item;

import joshie.harvest.animals.entity.EntityHarvestChicken;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

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

    public EntityAgeable getEntityFromEnum(World world, Spawner spawner) {
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
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            EntityAgeable entity = getEntityFromEnum(world, getEnumFromStack(stack));
            if (entity != null) {
                if (player.isSneaking()) entity.setGrowingAge(-24000);
                entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                ((IAnimalTracked)entity).getData().setOwner(EntityHelper.getPlayerUUID(player));
                world.spawnEntityInWorld(entity);
            }
        }

        stack.splitStack(1);
        return EnumActionResult.SUCCESS;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(MODID, getPrefix(values[i]) + "_" + values[i].name().toLowerCase(Locale.ENGLISH)), "inventory"));
        }
    }
}