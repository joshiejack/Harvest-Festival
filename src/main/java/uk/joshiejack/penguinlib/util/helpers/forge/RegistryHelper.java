package uk.joshiejack.penguinlib.util.helpers.forge;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import uk.joshiejack.penguinlib.block.base.BlockFluidBase;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class RegistryHelper {
    private static List<IPenguinItem> items;

    public static List<IPenguinItem> getItems() {
        return items;
    }

    public static void clearItems() {
        items = null;
    }

    public static void setRegistryAndName(ResourceLocation resource, Potion potion) {
        potion.setRegistryName(resource);
        potion.setPotionName(resource.getNamespace() + ".effect." + resource.getPath());
    }

    public static SoundEvent createSoundEvent(String modid, String name) {
        ResourceLocation resource = new ResourceLocation(modid, name);
        SoundEvent event = new SoundEvent(resource).setRegistryName(resource);
        return event;
    }

    public static void setRegistryAndLocalizedName(ResourceLocation registry, Item item) {
        item.setRegistryName(registry);
        item.setTranslationKey(registry.getNamespace() + "." + registry.getPath());
        if (item instanceof IPenguinItem) {
            if (items == null) {
                items = new ArrayList<>();
            }

            items.add((IPenguinItem) item);
        }
    }

    public static ItemBlock[] getItemBlocks(IPenguinBlock... blocks) {
       return Arrays.stream(blocks).map(RegistryHelper::getItemBlock).collect(Collectors.toList()).toArray(new ItemBlock[blocks.length]);
    }

    private static ItemBlock getItemBlock(IPenguinBlock block) {
        ItemBlock item = block.createItemBlock();
        if (item.getRegistryName() == null) {
            RegistryHelper.setRegistryAndLocalizedName(((Block) block).getRegistryName(), item);
        }

        return item;
    }

    public static void registerBlock(ResourceLocation registry, Block block) {
        block.setRegistryName(registry);
    }

    public static Fluid createFluid(String name, ResourceLocation still, CreativeTabs tabs) {
        return createFluid(name, still, still, tabs);
    }

    public static Fluid createFluid(String name, ResourceLocation still, ResourceLocation flow, CreativeTabs tab) {
        if (!FluidRegistry.isFluidRegistered(name)) {
            Fluid fluid = new Fluid(name, still, flow);
            FluidRegistry.registerFluid(fluid);
            if (fluid.getBlock() == null) {
                fluid.setBlock(new BlockFluidBase(fluid, still.getNamespace(), tab));
            }
        }

        return FluidRegistry.getFluid(name);
    }

    public static Fluid createFluid(String name, ResourceLocation still, ResourceLocation flow) {
        if (!FluidRegistry.isFluidRegistered(name)) {
            Fluid fluid = new Fluid(name, still, flow);
            FluidRegistry.registerFluid(fluid);
        }

        return FluidRegistry.getFluid(name);
    }

    @SuppressWarnings("unchecked")
    public static <I extends IForgeRegistryEntry<I>> void register(String type, String modId, IForgeRegistry<I> registry) {
        try {
            ((Multimap<String, I> )RegistryHelper.class.getDeclaredField(type.toUpperCase(Locale.ENGLISH)).get(null)).get(modId).forEach(registry::register);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
