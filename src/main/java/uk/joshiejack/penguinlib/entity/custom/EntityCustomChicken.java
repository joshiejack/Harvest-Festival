package uk.joshiejack.penguinlib.entity.custom;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.data.custom.entity.AbstractEntityData;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EntityCustomChicken extends EntityChicken implements IEntityAdditionalSpawnData {
    public AbstractEntityData data;
    private ResourceLocation subid;

    public EntityCustomChicken(World world, ResourceLocation subid) {
        super(world);
        this.subid = subid;
        this.data = AbstractEntityData.DATA_MAP.get(subid);
    }

    public EntityCustomChicken(World worldIn) {
        super(worldIn);
        List<ResourceLocation> subids = Lists.newArrayList(CustomLoader.subids.get(this.getClass()));
        subid = subids.get(worldIn.rand.nextInt(subids.size()));
        data = AbstractEntityData.DATA_MAP.get(subid);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return data.lootTable;
    }

    @Override
    public EntityCustomChicken createChild(EntityAgeable ageable) {
        return new EntityCustomChicken(world, subid);
    }

    @Override
    public EntityItem dropItemWithOffset(@Nonnull Item itemIn, int size, float offsetY) {
        return this.entityDropItem(data.getDropItem(size), offsetY);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        subid = new ResourceLocation(nbt.getString("ID"));
        data = AbstractEntityData.DATA_MAP.get(subid);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setString("ID", subid.toString());
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeBoolean(subid != null);
        if (subid != null) ByteBufUtils.writeUTF8String(buffer, subid.toString());
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        if (additionalData.readBoolean()) {
            subid = new ResourceLocation(ByteBufUtils.readUTF8String(additionalData));
            data = AbstractEntityData.DATA_MAP.get(subid);
        }
    }
}
