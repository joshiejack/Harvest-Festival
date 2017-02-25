package joshie.harvest.tools.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import joshie.harvest.api.core.Ore;
import joshie.harvest.api.crops.WateringHandler;
import joshie.harvest.api.gathering.ISmashable.ToolType;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemToolSmashing;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.HFSounds;
import joshie.harvest.core.util.holders.HolderRegistrySet;
import joshie.harvest.crops.CropHelper;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.minecraft.block.Block.spawnAsEntity;

public class ItemHammer extends ItemToolSmashing<ItemHammer> {
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE);
    private static final double[] ATTACK_DAMAGES = new double[] { 3D, 3.5D, 4D, 4.5D, 5D, 5.5D, 5.5D, 6D};
    private final HolderRegistrySet blocks = new HolderRegistrySet();

    public ItemHammer() {
        super("pickaxe", EFFECTIVE_ON);
        setCreativeTab(HFTab.MINING);
        blocks.register(Ore.of("stone"));
        blocks.register(Ore.of("blockLimestone"));
        blocks.register(Ore.of("blockMarble"));
    }

    @Override
    public ToolType getToolType() {
        return ToolType.HAMMER;
    }

    private int getWidthAndHeight(ToolTier tier) {
        switch (tier) {
            case COPPER:
            case SILVER:
                return 0;
            case GOLD:
            case MYSTRIL:
            case CURSED:
            case BLESSED:
            case MYTHIC:
                return 1;
            default: return 0;
        }
    }

    private int getDepth(ToolTier tier) {
        switch (tier) {
            case SILVER:
            case MYSTRIL:
                return 2;
            case CURSED:
            case BLESSED:
                return 5;
            case MYTHIC:
                return 11;
            default: return 0;
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos position, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (canUse(stack) && canBeDamaged()) {
                if (canLevel(stack, state)) ToolHelper.levelTool(stack);
                ArrayList<ItemStack> drops = new ArrayList<>();
                for (BlockPos pos : getBlocks(worldIn, position, player, stack)) {
                    if (canUse(stack) && canBeDamaged()) {
                        ToolHelper.performTask(player, stack, this);
                        ToolHelper.collectDrops(worldIn, pos, worldIn.getBlockState(pos), player, drops);
                        worldIn.setBlockToAir(pos); //No particles
                    } else break; //Exist since we can't damage anymore
                }

                drops.stream().forEach(item -> spawnAsEntity(worldIn, new BlockPos(player), item));
            }

            return true;
        } else return false;
    }

    @SuppressWarnings("ConstantConditions")
    public ImmutableList<BlockPos> getBlocks(World world, BlockPos position, EntityPlayer player, ItemStack tool) {
        ToolTier tier = getTier(tool);
        IBlockState state = world.getBlockState(position);
        ItemStack stackState = ToolHelper.getStackFromBlockState(state);
        if (stackState == null) return ImmutableList.of();
        if (!blocks.contains(stackState)) return ImmutableList.of();
        if (tier == ToolTier.BASIC || player.isSneaking()) return ImmutableList.of(position);

        RayTraceResult rt = rayTrace(world, player, true);
        if (rt == null || !position.equals(rt.getBlockPos())) {
            rt = rayTrace(world, player, false);
            if (rt == null || !position.equals(rt.getBlockPos())) {
                return ImmutableList.of();
            }
        }

        EnumFacing front = rt.sideHit;
        ImmutableList.Builder<BlockPos> builder = ImmutableList.builder();
        for (int horizontal = -getWidthAndHeight(tier); horizontal <= getWidthAndHeight(tier); horizontal++) {
            for (int vertical = -1; vertical <= getWidthAndHeight(tier); vertical++) {
                for (int depth = 0; depth <= getDepth(tier); depth++) {
                    BlockPos pos = front == EnumFacing.EAST || front == EnumFacing.WEST ?
                            new BlockPos(position.getX() + ((front == EnumFacing.WEST) ? depth : -depth), position.getY() + vertical, position.getZ() + horizontal) :
                            new BlockPos(position.getX() + horizontal, position.getY() + vertical, position.getZ() + + ((front == EnumFacing.NORTH) ? depth : -depth));
                    if (front == EnumFacing.DOWN || front == EnumFacing.UP) {
                        EnumFacing playerFacing = EntityHelper.getFacingFromEntity(player);
                        if (playerFacing == EnumFacing.EAST) {
                            pos = new BlockPos(position.getX() - vertical, position.getY() + (front == EnumFacing.UP ? -depth: depth), position.getZ() + horizontal);
                        } else if (playerFacing == EnumFacing.WEST) {
                            pos = new BlockPos(position.getX() + vertical, position.getY() + (front == EnumFacing.UP ? -depth: depth), position.getZ() + horizontal);
                        } else if (playerFacing == EnumFacing.SOUTH) {
                            pos = new BlockPos(position.getX() + horizontal, position.getY() + (front == EnumFacing.UP ? -depth: depth), position.getZ() - vertical);
                        } else if (playerFacing == EnumFacing.NORTH) {
                            pos = new BlockPos(position.getX() + horizontal, position.getY() + (front == EnumFacing.UP ? -depth: depth), position.getZ() + vertical);
                        }
                    }

                    if (world.getBlockState(pos).getBlock() == Blocks.STONE) {
                        builder.add(pos);
                    }
                }
            }
        }

        return builder.build();
    }

    @Override
    public void playSound(World world, BlockPos pos) {
        world.playSound(null, pos, HFSounds.SMASH_ROCK, SoundCategory.BLOCKS, world.rand.nextFloat() * 0.45F, world.rand.nextFloat() * 1.0F + 0.5F);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if (canUse(stack)) {
            Material material = state.getMaterial();
            return material != Material.IRON && material != Material.ANVIL && material != Material.ROCK ? super.getStrVsBlock(stack, state) : this.getEffiency(stack);
        } else return 0.05F;
    }

    @Override
    public boolean onSmashed(EntityPlayer player, ItemStack stack, ToolTier tier, int harvestLevel, World world, BlockPos pos, IBlockState state) {
        if (canUse(stack)) {
            WateringHandler handler = CropHelper.getWateringHandler(world, pos, state);
            if (handler != null) {
                ToolHelper.performTask(player, stack, this);
                handler.dehydrate(world, pos, state);
                return true;
            }
        }

        return super.onSmashed(player, stack, tier, harvestLevel, world, pos, state);
    }

    @Override
    @Nonnull
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{ getCreativeTab(), HFTab.GATHERING };
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        ToolTier tier = getTier(stack);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", ATTACK_DAMAGES[tier.ordinal()], 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -3.0D, 0));
        }

        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        super.addInformation(stack, player, list, flag);
        ToolTier tier = getTier(stack);
        int width = getWidthAndHeight(tier) == 0 ? 1 : 3;
        int height = tier == ToolTier.BASIC ? 1: getWidthAndHeight(tier) == 0 ? 2 : 3;
        int depth = getDepth(tier) + 1;
        list.add(TextFormatting.AQUA + "" + TextFormatting.ITALIC + TextHelper.translate("hammer.tooltip.titles"));
        list.add(TextFormatting.GOLD + TextHelper.formatHF("hammer.tooltip.dimensions", width, height, depth));
    }
}