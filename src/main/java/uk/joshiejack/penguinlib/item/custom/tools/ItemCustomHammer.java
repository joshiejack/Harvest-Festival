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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.renderer.RenderToolBreak;
import uk.joshiejack.penguinlib.data.custom.item.tools.CustomItemHammerData;
import uk.joshiejack.penguinlib.data.custom.material.CustomToolMaterialData;
import uk.joshiejack.penguinlib.item.base.ItemBaseHammer;
import uk.joshiejack.penguinlib.item.interfaces.SmashingTool;
import uk.joshiejack.penguinlib.item.tools.AreaOfEffect;
import uk.joshiejack.penguinlib.item.util.Charge;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemCustomHammer extends ItemBaseHammer implements SmashingTool, RenderToolBreak.MultiRender {
    private final ResourceLocation script;
    private final Charge charge;
    private final int area;
    private final int level;

    public ItemCustomHammer(ResourceLocation registryName, CustomItemHammerData data) {
        super(registryName, data.getToolMaterial().efficiency, data.getToolMaterial().damage);
        this.setMaxDamage(data.getToolMaterial().maxUses);
        this.area = data.area;
        this.level = data.getToolMaterial().harvestLevel;
        this.charge = new Charge(data.getToolMaterial().harvestLevel, "hammer");
        this.attackSpeed = -3.8F + (data.getToolMaterial().harvestLevel * 0.1F);
        this.script = data.getScript();
        if (data.getToolMaterial().harvestLevel != 0) {
            aoeByHarvestLevel.put(data.getToolMaterial().harvestLevel, new AreaOfEffect(data.widthAndHeight, -1, data.depth));
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
    protected int getToolLevel() {
        return level;
    }

    @Override
    public int getArea() {
        return area;
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

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        int level = getChargeLevel(stack);
        AreaOfEffect aoe = aoeByHarvestLevel.get(level);
        int area = (1 + (this.area * 2));
        tooltip.add(TextFormatting.DARK_GREEN + StringHelper.format("harvestfestival.item.hammer.tooltip.smash", area, area));
        tooltip.add(TextFormatting.GOLD + StringHelper.localize(charge.prefix + CustomToolMaterialData.byInt.get(level).name.toLowerCase(Locale.ENGLISH)));
        int widthAndHeight = aoe.widthAndHeight;
        int width = widthAndHeight == 0 ? 1 : 3;
        int height = aoe.singular ? 1 : widthAndHeight == 0 ? 2 : 3;
        int depth = aoe.depth + 1;
        tooltip.add(TextFormatting.BLUE + StringHelper.format("harvestfestival.item.hammer.tooltip.dimensions", width, height, depth));
        tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC + StringHelper.localize("harvestfestival.item.hammer.tooltip.titles"));
        if (level < this.level)
            tooltip.add(TextFormatting.AQUA + "" + TextFormatting.ITALIC + StringHelper.localize("harvestfestival.item.tools.tooltip.charge"));
        if (level != 0)
            tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + StringHelper.localize("harvestfestival.item.tools.tooltip.discharge"));
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        return charge.onItemRightClick(world, player, hand);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ImmutableList<BlockPos> getPositions(EntityPlayer player, World world, BlockPos pos) {
        return aoeByHarvestLevel.get(getChargeLevel(player.getHeldItemMainhand())).getPositions(player, world, pos);
    }
}
