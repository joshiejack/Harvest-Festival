package uk.joshiejack.penguinlib.item.custom.tools;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.custom.item.tools.CustomItemLumberAxeData;
import uk.joshiejack.penguinlib.item.base.ItemBaseAxe;
import uk.joshiejack.penguinlib.item.interfaces.SmashingTool;
import uk.joshiejack.penguinlib.item.util.TreeTasks;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCustomLumberAxe extends ItemBaseAxe implements SmashingTool {
    private final ResourceLocation script;
    private final int hits;
    private final int level;
    private final int area;

    public ItemCustomLumberAxe(ResourceLocation registryName, CustomItemLumberAxeData data) {
        super(registryName, data.getToolMaterial().efficiency, data.getToolMaterial().damage);
        this.setMaxDamage(data.getToolMaterial().maxUses);
        this.script = data.getScript();
        this.hits = data.hits;
        this.level = data.getToolMaterial().harvestLevel;
        this.area = data.area;
        setCreativeTab(PenguinLib.CUSTOM_TAB);
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (isSelected && entity instanceof EntityPlayer) {
            Scripting.callFunction(script, "onItemSelected", entity);
        }
    }

    @Override
    protected int getToolLevel() {
        return level;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        if (stack.getItemDamage() < stack.getMaxDamage()) {
            if (!player.isSneaking() && TreeTasks.findTree(player.world, pos)) {
                if (canChopTree(player, stack, pos))
                    return chopTree(pos, player, stack);
                else if (isWood(player.world, pos))
                    return TreeTasks.replaceTree(pos, player);
            }
        }

        return false;
    }

    private boolean isWood(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isWood(world, pos);
    }

    private boolean canChopTree(EntityPlayer player, ItemStack stack, BlockPos pos) {
        NBTTagCompound tag = stack.getOrCreateSubCompound("Chopping");
        if (tag.hasKey("Block")) {
            BlockPos internal = BlockPos.fromLong(tag.getLong("Block"));
            if (internal.equals(pos)) {
                int times = tag.getInteger("Times");
                tag.setInteger("Times", times + 1);
                player.world.playSound(null, pos, Sounds.TREE_CHOP, SoundCategory.BLOCKS, (player.world.rand.nextFloat() * 0.25F) * times * 10F, player.world.rand.nextFloat() * 1.0F + 0.5F);
                return times >= hits;
            }
        }

        int times = 1;
        tag.setLong("Block", pos.toLong());
        tag.setInteger("Times", times);
        player.world.playSound(null, pos, Sounds.TREE_CHOP, SoundCategory.BLOCKS, (player.world.rand.nextFloat() * 0.25F) * times * 10F, player.world.rand.nextFloat() * 1.0F + 0.5F);
        return times > hits;
    }

    private boolean chopTree(BlockPos pos, EntityPlayer player, ItemStack stack) {
        NBTTagCompound tag = stack.getOrCreateSubCompound("Chopping");
        tag.removeTag("Block"); //Remove the data now we're chopping
        tag.removeTag("Times"); //Remove the data now we're chopping
        if (player.world.isRemote) return true;
        MinecraftForge.EVENT_BUS.register(new TreeTasks.ChopTree(pos, player, stack));
        player.world.playSound(null, pos, Sounds.TREE_FALL, SoundCategory.BLOCKS, player.world.rand.nextFloat() * 0.25F, player.world.rand.nextFloat() * 1.0F + 0.5F);
        return true;
    }

    @Override
    public int getArea() {
        return area;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextFormatting.AQUA + "" + TextFormatting.ITALIC + StringHelper.localize("harvestfestival.item.axe.tooltip.sneak"));
        tooltip.add(TextFormatting.GREEN + StringHelper.format("harvestfestival.item.axe.tooltip.chops", hits + 1));
    }

    @Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
    public static class Sounds {
        public static final SoundEvent TREE_CHOP = null;
        public static final SoundEvent TREE_FALL = null;

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onSoundRegistration(final RegistryEvent.Register<SoundEvent> event) {
            event.getRegistry().registerAll(RegistryHelper.createSoundEvent(PenguinLib.MOD_ID, "tree_chop"),
                    RegistryHelper.createSoundEvent(PenguinLib.MOD_ID, "tree_fall"));
        }
    }
}
