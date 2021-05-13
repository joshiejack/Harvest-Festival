package uk.joshiejack.penguinlib.item.custom.tools;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
import uk.joshiejack.penguinlib.client.renderer.RenderToolBreak;
import uk.joshiejack.penguinlib.data.custom.item.tools.CustomItemShovelData;
import uk.joshiejack.penguinlib.item.base.ItemBaseShovel;
import uk.joshiejack.penguinlib.item.tools.AreaOfEffect;
import uk.joshiejack.penguinlib.item.util.Charge;
import uk.joshiejack.penguinlib.scripting.Scripting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCustomShovel extends ItemBaseShovel implements RenderToolBreak.MultiRender {
    private final ResourceLocation script;
    private final Charge charge;

    public ItemCustomShovel(ResourceLocation registryName, CustomItemShovelData data) {
        super(registryName, data.getToolMaterial().efficiency, data.getToolMaterial().damage);
        this.setMaxDamage(data.getToolMaterial().maxUses);
        this.script = data.getScript();
        this.charge = new Charge(data.getToolMaterial().harvestLevel, "shovel");
        if (data.getToolMaterial().harvestLevel != 0) {
            aoeByHarvestLevel.put(data.getToolMaterial().harvestLevel, new AreaOfEffect(data.width, -data.width, data.depth));
        }

        setCreativeTab(PenguinLib.CUSTOM_TAB);
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (isSelected && entity instanceof EntityPlayer) {
            Scripting.callFunction(script, "onItemSelected", entity);
        }
    }

    @Override
    public int getChargeLevel(ItemStack tool) {
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
    public ImmutableList<BlockPos> getPositions(EntityPlayer player, World world, BlockPos pos) {
        return aoeByHarvestLevel.get(getChargeLevel(player.getHeldItemMainhand())).getPositions(player, world, pos);
    }
}
