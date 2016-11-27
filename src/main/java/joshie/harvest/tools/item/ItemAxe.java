package joshie.harvest.tools.item;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import joshie.harvest.api.gathering.ISmashable.ToolType;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemToolSmashing;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.HFSounds;
import joshie.harvest.tools.item.TreeTasks.ChopTree;
import joshie.harvest.tools.item.TreeTasks.TreeReplace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static joshie.harvest.api.core.ITiered.ToolTier.BASIC;

public class ItemAxe extends ItemToolSmashing<ItemAxe> {
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);
    private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.1F, -3.1F, -3.0F, -3.0F, -2.9F, -2.9F, -2.8F};

    public ItemAxe() {
        super("axe", EFFECTIVE_ON);
        setCreativeTab(HFTab.GATHERING);
    }

    @Override
    public ToolType getToolType() {
        return ToolType.AXE;
    }

    private boolean isWood(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isWood(world, pos);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        if (!player.isSneaking() && findTree(player.worldObj, pos)) {
            if (canChopTree(stack, pos))
                return chopTree(pos, player, stack);
             else if (isWood(player.worldObj, pos))
                return replaceTree(pos, player);
        }

        return super.onBlockStartBreak(stack, pos, player);
    }

    private int getTimes(ItemStack stack) {
        switch (getTier(stack)) {
            case BASIC:
                return 6;
            case COPPER:
                return 5;
            case SILVER:
                return 4;
            case GOLD:
                return 3;
            case MYSTRIL:
                return 2;
            case CURSED:
            case BLESSED:
                return 1;
            case MYTHIC:
                return 0;
            default:
                return 7;
        }
    }

    public boolean hasReachedLimit(ItemStack stack) {
        return stack.getSubCompound("Chopping", true).getInteger("Times") > getTimes(stack);
    }

    private boolean canChopTree(ItemStack stack, BlockPos pos) {
        NBTTagCompound tag = stack.getSubCompound("Chopping", true);
        if (tag.hasKey("Block")) {
            BlockPos internal = BlockPos.fromLong(tag.getLong("Block"));
            if (internal.equals(pos)) {
                int times = tag.getInteger("Times");
                tag.setInteger("Times", times + 1);
                return times >= getTimes(stack);
            }
        }

        int times = 1;
        tag.setLong("Block", pos.toLong());
        tag.setInteger("Times", times);
        return times > getTimes(stack);
    }

    private boolean chopTree(BlockPos pos, EntityPlayer player, ItemStack stack) {
        NBTTagCompound tag = stack.getSubCompound("Chopping", true);
        tag.removeTag("Block"); //Remove the data now we're chopping
        tag.removeTag("Times"); //Remove the data now we're chopping
        if(player.worldObj.isRemote) return true;
        MinecraftForge.EVENT_BUS.register(new ChopTree(pos, player, stack));
        return true;
    }

    private boolean replaceTree(BlockPos pos, EntityPlayer player) {
        if(player.worldObj.isRemote) return true;
        MinecraftForge.EVENT_BUS.register(new TreeReplace(player.worldObj, pos, player.worldObj.getBlockState(pos)));
        return true;
    }

    //Borrowed from Tinkers Construct by boni
    private boolean findTree(World world, BlockPos origin) {
        BlockPos pos = null;
        Stack<BlockPos> candidates = new Stack<>();
        candidates.add(origin);
        while(!candidates.isEmpty()) {
            BlockPos candidate = candidates.pop();
            if((pos == null || candidate.getY() > pos.getY()) && world.getBlockState(candidate).getBlock().isWood(world, pos)) {
                pos = candidate.up();
                while(world.getBlockState(pos).getBlock().isWood(world, pos)) {
                    pos = pos.up();
                }

                candidates.add(pos.north());
                candidates.add(pos.east());
                candidates.add(pos.south());
                candidates.add(pos.west());
            }
        }

        if(pos == null) return false;
        int d = 5;
        int o = -1; // -(d-1)/2
        int leaves = 0;
        for(int x = 0; x < d; x++) {
            for(int y = 0; y < d; y++) {
                for(int z = 0; z < d; z++) {
                    BlockPos leaf = pos.add(o + x, o + y, o + z);
                    IBlockState state = world.getBlockState(leaf);
                    if (state.getBlock().isLeaves(state, world, leaf)) {
                        if (++leaves >= 5) return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void playSound(World world, BlockPos pos) {
        playSound(world, pos, HFSounds.SMASH_WOOD, SoundCategory.BLOCKS);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if (canUse(stack)) {
            Material material = state.getMaterial();
            return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getStrVsBlock(stack, state) : this.getEffiency(stack);
        } else return 0.05F;
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        ToolTier tier = getTier(stack);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double)(tier == BASIC ? 6F : 8F), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double)ATTACK_SPEEDS[tier.ordinal()], 0));
        }

        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        super.addInformation(stack, player, list, flag);
        list.add(TextFormatting.AQUA + "" + TextFormatting.ITALIC + TextHelper.translate("axe.tooltip.sneak"));
        list.add(TextFormatting.GREEN + TextHelper.formatHF("axe.tooltip.chops", getTimes(stack) + 1));
    }
}