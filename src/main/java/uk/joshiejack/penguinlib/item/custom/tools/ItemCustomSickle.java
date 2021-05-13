package uk.joshiejack.penguinlib.item.custom.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.custom.item.tools.CustomItemSickleData;
import uk.joshiejack.penguinlib.item.base.ItemBaseSickle;
import uk.joshiejack.penguinlib.item.util.Charge;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TerrainHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCustomSickle extends ItemBaseSickle {
    private final ResourceLocation script;
    private final Charge charge;

    public ItemCustomSickle (ResourceLocation registryName, CustomItemSickleData data) {
        super(registryName);
        this.setMaxDamage(data.getToolMaterial().maxUses);
        this.script = data.getScript();
        this.charge = new Charge(data.getToolMaterial().harvestLevel, "sickle");
        this.efficiency = data.getToolMaterial().efficiency;
        areaByHarvestLevel.put(data.getToolMaterial().harvestLevel, data.area);
        setCreativeTab(PenguinLib.CUSTOM_TAB);
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (isSelected && entity instanceof EntityPlayer) {
            Scripting.callFunction(script, "onItemSelected", entity);
        }
    }

    private int getChargeLevel(ItemStack tool) {
        return charge.getChargeLevel(tool);
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return charge.action;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return charge.duration;
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, EntityLivingBase entity) {
        return charge.onItemUseFinish(stack);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        return charge.onItemRightClick(world, player, hand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        charge.addInformation(stack, tooltip);
    }

    @Override
    public boolean onBlockDestroyed(@Nonnull ItemStack stack, World world, @Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull EntityLivingBase entityLiving) {
        if (!world.isRemote && !entityLiving.isSneaking()) {
            int area = areaByHarvestLevel.get(getChargeLevel(stack));
            for (BlockPos target : BlockPos.getAllInBox(pos.add(-area, 0, -area), pos.add(area, 0, area))) {
                if (stack.getItemDamage() < stack.getMaxDamage() && getDestroySpeed(stack, world.getBlockState(target)) != 1F) {
                    stack.damageItem(1, entityLiving);
                    if (!target.equals(pos))
                        TerrainHelper.destroyBlock(world, target, (EntityPlayerMP) entityLiving);
                }
            }
        }

        return true;
    }
}
