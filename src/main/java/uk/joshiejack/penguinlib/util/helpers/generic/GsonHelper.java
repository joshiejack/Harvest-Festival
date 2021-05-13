package uk.joshiejack.penguinlib.util.helpers.generic;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomObject;
import uk.joshiejack.penguinlib.data.adapters.*;
import uk.joshiejack.penguinlib.template.Placeable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class GsonHelper {
    private static Gson gson; //Temporary

    public static Gson get() {
        //Create the gson if it's null
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().setExclusionStrategies(new SuperClassExclusionStrategy());
            builder.registerTypeAdapter(Placeable.class, new PlaceableAdapter());
            builder.registerTypeAdapter(IBlockState.class, new StateAdapter());
            builder.registerTypeAdapter(ItemStack.class, new StackAdapter());
            builder.registerTypeAdapter(ResourceLocation.class, new ResourceAdapter());
            builder.registerTypeAdapter(ITextComponent.class, new TextComponentAdapter());
            builder.registerTypeAdapter(BlockPos.class, new BlockPosAdapter());
            builder.registerTypeAdapter(EnumFacing.class, new FacingAdapter());
            builder.registerTypeAdapter(EnumAction.class, new ActionAdapter());
            builder.registerTypeAdapter(NBTTagCompound.class, new NBTAdapter());
            builder.registerTypeAdapter(Material.class, new MaterialAdapter());
            builder.registerTypeAdapter(SoundType.class, new SoundTypeAdapter());
            builder.registerTypeAdapter(AxisAlignedBB.class, new AABBAdapter());
            builder.registerTypeAdapter(BlockRenderLayer.class, new BlockRenderLayerAdapter());
            builder.registerTypeAdapter(EnumCreatureType.class, new CreatureTypeAdapter());
            builder.registerTypeAdapter(AbstractCustomObject.class, new CustomObjectAdapter());
            builder.registerTypeAdapter(Block.EnumOffsetType.class, new OffsetAdapter());
            gson = builder.create();
        }

        return gson;
    }

    private static class SuperClassExclusionStrategy implements ExclusionStrategy {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getDeclaringClass().equals(IForgeRegistryEntry.Impl.class);
        }
    }
}
