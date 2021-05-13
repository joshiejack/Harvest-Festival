package uk.joshiejack.penguinlib.item.custom.tools;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.custom.item.tools.CustomItemWateringCanData;
import uk.joshiejack.penguinlib.item.base.ItemBaseWateringCan;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;

import javax.annotation.Nonnull;
import java.util.Set;

public class ItemCustomWateringCan extends ItemBaseWateringCan {
    private final ResourceLocation script;
    private final int width;
    private final int depth;

    public ItemCustomWateringCan(ResourceLocation registryName, CustomItemWateringCanData data) {
        super(registryName, data.getToolMaterial().maxUses);
        this.script = data.getScript();
        this.width = data.width;
        this.depth = data.depth;
        setCreativeTab(PenguinLib.CUSTOM_TAB);
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (isSelected && entity instanceof EntityPlayer) {
            Scripting.callFunction(script, "onItemSelected", entity);
        }
    }

    @Override
    public Set<BlockPos> getPositions(EntityPlayer player, World world, BlockPos pos) {
        Set<BlockPos> set = Sets.newHashSet();
        EnumFacing front = EntityHelper.getFacingFromEntity(player);
        for (int w = -depth; w <= depth; w++) {
            for (int h = -1; h <= 0; h++) {
                for (int d = 0; d <= width; d++) {
                    set.add(front == EnumFacing.EAST || front == EnumFacing.WEST ?
                            pos.add(((front == EnumFacing.WEST) ? d : -d), h, w) :
                            pos.add(w, h, ((front == EnumFacing.NORTH) ? d : -d)));
                }
            }
        }

        return set;
    }
}
