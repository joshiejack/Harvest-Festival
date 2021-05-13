package uk.joshiejack.penguinlib.data.custom.block;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.data.custom.HasLoot;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

import static net.minecraft.block.Block.NULL_AABB;

public abstract class AbstractCustomBlockData extends AbstractCustomData.ItemOrBlock<Block, AbstractCustomBlockData> implements HasLoot, Comparable<AbstractCustomBlockData> {
    private transient List<String> toolTypes;
    public String propertyName;
    public Material material;
    public SoundType soundType;
    public float hardness = -10F;
    public float explosionResistance = -10F;
    public AxisAlignedBB collisionBox = null;
    public AxisAlignedBB boundingBox = NULL_AABB;
    public String toolType;
    public int toolLevel = -1;
    public ResourceLocation lootTable;
    public BlockRenderLayer renderLayer;
    public Boolean silkHarvest;
    public Block.EnumOffsetType offset;

    @Override
    public ResourceLocation getLootTable() {
        return lootTable;
    }

    public List<String> getToolTypes(AbstractCustomBlockData defaults) {
        if (toolTypes == null) {
            toolTypes = Lists.newArrayList();
            if (toolType == null) {
                toolTypes.addAll(Lists.newArrayList(defaults.toolType.split("&")));
            } else toolTypes.addAll(Lists.newArrayList(toolType.split("&")));
        }

        return toolTypes;
    }

    @Override
    public int compareTo(AbstractCustomBlockData o) {
        return name.compareTo(o.name);
    }
}
