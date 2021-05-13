package uk.joshiejack.penguinlib.template;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ResourceLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.GsonHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Template {
    private Placeable[] components;
    private transient boolean isInit;

    public Template() { this.components = new Placeable[0]; }
    private Template(Placeable[] components) {
        this.components = components;
    }

    public Template(ArrayList<Placeable> ret) {
        ret.sort(Comparator.comparingInt(Placeable::getY)); //Sort by y?
        components = new Placeable[ret.size()];
        for (int j = 0; j < ret.size(); j++) {
            components[j] = ret.get(j);
        }
    }

    public static WrappedTemplate wrap(Placeable... components) {
        return new WrappedTemplate(new Template(components));
    }

    public void merge(Template park) {
        List<Placeable> set = new ArrayList<>();
        for (Placeable component: park.components) {
            if (!set.contains(component)) set.add(component);
        }

        for (Placeable component: components) {
            if (!set.contains(component)) set.add(component);
        }

        components = set.toArray(new Placeable[set.size()]);
    }

    public Placeable[] getComponents() {
        if (!isInit) {
            //Time to sort
            List<Placeable> list = Lists.newArrayList(components);
            list.sort(Comparator.comparingInt(Placeable::getZ));
            list.sort(Comparator.comparingInt(Placeable::getX));
            list.sort(Comparator.comparingInt(Placeable::getY));
            components = list.toArray(new Placeable[0]);
            isInit = true;
        }

        return components;
    }

    public void removeBlocks(World world, BlockPos pos, Rotation rotation, IBlockState state, boolean removeEntities) {
        if (!world.isRemote) {
            if (getComponents() != null) {
                if (removeEntities) for (int i = getComponents().length - 1; i >= 0; i--) getComponents()[i].remove(world, pos, rotation, Placeable.ConstructionStage.MOVEIN, state);
                if (removeEntities) for (int i = getComponents().length - 1; i >= 0; i--) getComponents()[i].remove(world, pos, rotation, Placeable.ConstructionStage.PAINT, state);
                for (int i = getComponents().length - 1; i >= 0; i--) getComponents()[i].remove(world, pos, rotation, Placeable.ConstructionStage.DECORATE, state);
                for (int i = getComponents().length - 1; i >= 0; i--) getComponents()[i].remove(world, pos, rotation, Placeable.ConstructionStage.BUILD, state);
            }
        }
    }

    public EnumActionResult placeBlocks(World world, BlockPos pos, Rotation rotation) {
        return placeBlocks(world, pos, rotation, PlaceableHelper.DEFAULT);
    }

    public EnumActionResult placeBlocks(World world, BlockPos pos, Rotation rotation, @Nullable Replaceable replaceable) {
        if (!world.isRemote) {
            if (getComponents() != null) {
                for (Placeable placeable : getComponents()) placeable.place(world, pos, rotation, Placeable.ConstructionStage.BUILD, false, replaceable);
                for (Placeable placeable : getComponents()) placeable.place(world, pos, rotation, Placeable.ConstructionStage.DECORATE, false, replaceable);
                for (Placeable placeable : getComponents()) placeable.place(world, pos, rotation, Placeable.ConstructionStage.PAINT, false, replaceable);
                for (Placeable placeable : getComponents()) placeable.place(world, pos, rotation, Placeable.ConstructionStage.MOVEIN, false, replaceable);
            }
        }


        return EnumActionResult.SUCCESS;
    }

    public static Template getTemplate(File file) {
        return GsonHelper.get().fromJson(ResourceLoader.getStringFromFile(file), Template.class);
    }

    public static Template getTemplate(ResourceLocation name, String path) {
        return GsonHelper.get().fromJson(ResourceLoader.getJSONResource(name, path), Template.class);
    }

    public static class Replaceable {
        public boolean canReplace(World world, BlockPos transformed) {
            return world.getBlockState(transformed).getBlockHardness(world, transformed) != -1F;
        }
    }

    public static class WrappedTemplate {
        private Template data;

        public WrappedTemplate(Template template) {
            this.data = template;
        }

        public Template get() {
            return data;
        }

        public void clear() {
            data = null;
        }

        public Template set(@Nonnull WrappedTemplate template) {
            data = template.data;
            template.data = null;
            return data;
        }
    }
}
