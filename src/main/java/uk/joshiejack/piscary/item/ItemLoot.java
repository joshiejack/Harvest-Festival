package uk.joshiejack.piscary.item;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.item.base.ItemMulti;
import uk.joshiejack.piscary.Piscary;
import uk.joshiejack.piscary.PiscaryConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import static uk.joshiejack.piscary.Piscary.MODID;

public class ItemLoot extends ItemMulti<ItemLoot.Loot> {
    public ItemLoot() {
        super(new ResourceLocation(MODID, "loot"), Loot.class);
        setCreativeTab(Piscary.TAB);
    }

    public enum Loot implements IStringSerializable {
        BONES, BOOT, CAN, FOSSIL, TREASURE;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    private ResourceLocation getRandomResourceLocation(Random random) {
        Set<ResourceLocation> loottables = LootTableList.getAll();
        int size = loottables.size();
        int item = random.nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for(ResourceLocation obj : loottables)  {
            if (i == item) {
                return obj;
            }

            i++;
        }

        return LootTableList.GAMEPLAY_FISHING;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand hand) {
        ItemStack stack = playerIn.getHeldItem(hand);
        if (PiscaryConfig.enableTreasure && getEnumFromStack(stack) == Loot.TREASURE) {
            if (!worldIn.isRemote) {
                List<ItemStack> result = Lists.newArrayList();
                while(result.size() == 0) {
                    LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) worldIn);
                    lootcontext$builder.withLuck(100).withPlayer(playerIn);
                    result = worldIn.getLootTableManager()
                            .getLootTableFromLocation(getRandomResourceLocation(worldIn.rand))
                            .generateLootForPools(worldIn.rand, lootcontext$builder.build());
                    result.forEach((itemstack) -> ItemHandlerHelper.giveItemToPlayer(playerIn, itemstack));
                }
            }

            stack.shrink(1);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else return new ActionResult<>(EnumActionResult.PASS, stack);
    }
}
