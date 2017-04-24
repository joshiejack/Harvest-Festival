package joshie.harvest.mining.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.mining.entity.EntityDarkChick;
import joshie.harvest.mining.entity.EntityDarkChicken;
import joshie.harvest.mining.entity.EntityDarkCow;
import joshie.harvest.mining.entity.EntityDarkSheep;
import joshie.harvest.mining.item.ItemDarkSpawner.DarkSpawner;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.monster.EntityMob;
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

public class ItemDarkSpawner extends ItemHFEnum<ItemDarkSpawner, DarkSpawner> {
    public enum DarkSpawner implements IStringSerializable {
        COW, SHEEP, CHICKEN, CHICK;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemDarkSpawner() {
        super(HFTab.MINING, DarkSpawner.class);
    }

    public EntityMob getEntityFromEnum(World world, DarkSpawner spawner) {
        switch (spawner) {
            case COW:
                return new EntityDarkCow(world);
            case SHEEP:
                return new EntityDarkSheep(world);
            case CHICKEN:
                return new EntityDarkChicken(world);
            case CHICK:
                return new EntityDarkChick(world);
            default:
                return null;
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            EntityMob entity = getEntityFromEnum(world, getEnumFromStack(stack));
            if (entity != null) {
                entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                world.spawnEntity(entity);
            }
        }

        stack.splitStack(1);

        return EnumActionResult.FAIL;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(MODID, "dark_spawner_" + values[i].name().toLowerCase(Locale.ENGLISH)), "inventory"));
        }
    }
}