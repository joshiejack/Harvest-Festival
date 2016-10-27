package joshie.harvest.fishing.item;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.fishing.item.ItemFish.Fish;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Locale;

public class ItemFish extends ItemHFEnum<ItemFish, Fish> implements IShippable, ISizedProvider {
    public ItemFish() {
        super(HFTab.FISHING, Fish.class);
    }

    @Override
    public Fish getEnumFromStack(ItemStack stack) {
        int real = (int)Math.floor(stack.getItemDamage() / 3);
        int id = Math.max(0, Math.min(values.length - 1, real));
        return values[id];
    }

    @Override
    public ItemStack getStackFromEnum(Fish fish) {
        return fish.getStack(this, Size.SMALL);
    }

    @Override
    public ItemStack getStackFromEnum(Fish fish, int size) {
        return fish.getStackOfSize(this, Size.SMALL, size);
    }

    @Override
    public Size getSize(ItemStack stack) {
        return Size.values()[Math.min(2, stack.getItemDamage() % 3)];
    }

    @Override
    public long getSellValue(ItemStack stack) {
        Size size = getSize(stack);
        Fish fish = getEnumFromStack(stack);
        return fish.getSellValue(size);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String size = TextHelper.translate("sizeable." + getSize(stack).name().toLowerCase(Locale.ENGLISH));
        String name = TextHelper.translate("fish." + getEnumFromStack(stack).getName());
        String format = TextHelper.translate("sizeable.format");
        return String.format(format, size, name);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.SIZEABLE + stack.getItemDamage() + (getEnumFromStack(stack).ordinal() * 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (Fish fish: values) {
            for (Size size: Size.values()) {
                list.add(fish.getStack(item, size));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        for (Fish fish: values) {
            for (Size size: Size.values()) {
                ItemStack stack = fish.getStack(item, size);
                ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getItemDamage(), new ModelResourceLocation(getRegistryName(), fish.getName() + "_" + size.getName()));
            }
        }
    }

    public enum Fish implements IStringSerializable {
        ANGEL, ANGLER, BLAASOP, BOWFIN, BUTTERFLY, CATFISH, CHUB, CLOWN, COD, DAMSEL, ELECTRICRAY, GOLD, HERRING, KOI, LAMPREY,
        LUNGFISH, MANTARAY, MINNOW, PERCH, PICKEREL, PIRANHA, PUFFER, PUPFISH, SALMON, SIAMESE, STARGAZER, STINGRAY, TANG, TETRA, TROUT, TUNA;

        private final long small;
        private final long medium;
        private final long large;

        Fish() {
            this.small = 0;
            this.medium = 0;
            this.large = 0;
        }

        Fish(long small, long medium, long large) {
            this.small = small;
            this.medium = medium;
            this.large = large;
        }

        public ItemStack getStack(Item item, Size size) {
            return getStackOfSize(item, size, 1);
        }

        public ItemStack getStackOfSize(Item item, Size size, int stackSize) {
            return new ItemStack(item, stackSize, (ordinal() * 3) + size.ordinal());
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }

        public long getSellValue(Size size) {
            return size == Size.SMALL ? small : size == Size.MEDIUM ? medium : large;
        }
    }
}
