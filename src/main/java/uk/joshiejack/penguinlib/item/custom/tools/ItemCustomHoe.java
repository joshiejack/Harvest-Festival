package uk.joshiejack.penguinlib.item.custom.tools;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.renderer.RenderToolBreak;
import uk.joshiejack.penguinlib.data.custom.item.tools.CustomItemHoeData;
import uk.joshiejack.penguinlib.item.base.ItemBaseHoe;
import uk.joshiejack.penguinlib.item.util.Charge;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCustomHoe extends ItemBaseHoe implements RenderToolBreak.MultiRender {
    private final Charge charge;
    private final ResourceLocation script;

    public ItemCustomHoe(ResourceLocation registryName, CustomItemHoeData data) {
        super(registryName);
        this.setMaxDamage(data.getToolMaterial().maxUses);
        script = data.getScript();
        widthByHarvestLevel.put(data.getToolMaterial().harvestLevel, data.width);
        depthByHarvestLevel.put(data.getToolMaterial().harvestLevel, data.depth);
        charge = new Charge(data.getToolMaterial().harvestLevel, "hoe");
        setCreativeTab(PenguinLib.CUSTOM_TAB);
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (isSelected && entity instanceof EntityPlayer) {
            Scripting.callFunction(script, "onItemSelected", entity);
        }
    }

    @Override
    public boolean drawDamage() {
        return false;
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
    public ImmutableList<BlockPos> getPositions(EntityPlayer player, World world, BlockPos pos) {
        int hoe = charge.getChargeLevel(player.getHeldItemMainhand());
        if (hoe != 0) {
            int width = widthByHarvestLevel.get(hoe);
            int depth = depthByHarvestLevel.get(hoe);
            ImmutableList.Builder<BlockPos> builder = ImmutableList.builder();
            EnumFacing front = EntityHelper.getFacingFromEntity(player);
            for (int w = -depth; w <= depth; w++) {
                for (int h = 0; h <= 0; h++) {
                    for (int d = 0; d <= width; d++) {
                        builder.add(front == EnumFacing.EAST || front == EnumFacing.WEST ?
                                pos.add(((front == EnumFacing.WEST) ? d : -d), h, w) :
                                pos.add(w, h, ((front == EnumFacing.NORTH) ? d : -d)));
                    }
                }
            }

            return builder.build();
        } else return super.getPositions(player, world, pos);
    }
}
