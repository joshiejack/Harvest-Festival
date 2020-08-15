package joshie.harvest.npcs.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.npcs.item.ItemNPCTool.NPCTool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.core.lib.LootStrings.MINING_GEMS;
import static joshie.harvest.npcs.item.ItemNPCTool.NPCTool.NPC_KILLER;
import static net.minecraft.util.text.TextFormatting.AQUA;
import static net.minecraft.util.text.TextFormatting.GOLD;

public class ItemNPCTool extends ItemHFEnum<ItemNPCTool, NPCTool> {
    public static final String SPECIAL = "Gift";
    public enum NPCTool implements IStringSerializable {
        BLUE_FEATHER, NPC_KILLER, GIFT, SPEECH, MAIL;

        public boolean isReal() {
            return this == NPC_KILLER;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemNPCTool() {
        super(HFTab.TOWN, NPCTool.class);
    }

    @Override
    public boolean onLeftClickEntity(@Nonnull ItemStack stack, EntityPlayer player, Entity entity) {
        if (getEnumFromStack(stack) == NPC_KILLER) {
            entity.setDead();
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldDisplayInCreative(NPCTool cheat) {
        return cheat.isReal();
    }

    @Override
    public int getSortValue(@Nonnull ItemStack stack) {
        return 1;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(SPECIAL)) return GOLD + TextHelper.translate("npctool.gift.special");
        else if (getEnumFromStack(stack).isReal()) return AQUA + super.getItemStackDisplayName(stack);
        else return super.getItemStackDisplayName(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    @SuppressWarnings("ConstantConditions")
    public void addInformation(@Nonnull ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(SPECIAL)) {
            tooltip.add(TextHelper.translate("npctool.gift.special.tooltip"));
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (held.hasTagCompound() && held.getTagCompound().hasKey(SPECIAL)) {
            if (!world.isRemote) {
                for (ItemStack stack: MiningHelper.getLoot(MINING_GEMS, world, player, 3F)) {
                    SpawnItemHelper.spawnByEntity(player, stack);
                }
            }

            held.splitStack(1);
            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        } else return super.onItemRightClick(world, player, hand);
    }
}