package joshie.harvest.tools.item;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import joshie.harvest.api.gathering.ISmashable.ToolType;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemToolSmashing;
import joshie.harvest.core.lib.HFSounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

import static joshie.harvest.api.core.ITiered.ToolTier.BASIC;

public class ItemAxe extends ItemToolSmashing {
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

    @Override
    public void playSound(World world, BlockPos pos) {
        playSound(world, pos, HFSounds.SMASH_WOOD, SoundCategory.BLOCKS);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if (canUse(stack)) {
            Material material = state.getMaterial();
            return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getStrVsBlock(stack, state) : this.getEffiency(stack);
        } else return 0.1F;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
        ToolTier tier = getTier(stack);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double)(tier == BASIC ? 6F : 8F), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double)ATTACK_SPEEDS[tier.ordinal()], 0));
        }

        return multimap;
    }
}