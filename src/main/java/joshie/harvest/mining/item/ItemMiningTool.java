package joshie.harvest.mining.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.mining.item.ItemMiningTool.MiningTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

import java.util.Locale;

public class ItemMiningTool extends ItemHFEnum<ItemMiningTool, MiningTool> {
    public enum MiningTool implements IStringSerializable {
        ESCAPE_ROPE;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.US);
        }
    }

    public ItemMiningTool() {
        super(HFTab.MINING, MiningTool.class);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (getEnumFromStack(stack) == MiningTool.ESCAPE_ROPE && world.provider.getDimension() == HFMining.MINING_ID) {
            stack.splitStack(1);
            if (!world.isRemote) MiningHelper.teleportToOverworld(player); //Back we go!
            return new ActionResult(EnumActionResult.SUCCESS, stack);
        } else return new ActionResult(EnumActionResult.PASS, stack);
    }
}
