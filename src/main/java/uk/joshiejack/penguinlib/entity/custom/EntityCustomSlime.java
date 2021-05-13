package uk.joshiejack.penguinlib.entity.custom;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.data.custom.entity.AbstractEntityData;
import uk.joshiejack.penguinlib.scripting.Scripting;

import javax.annotation.Nullable;
import java.util.List;

public class EntityCustomSlime extends EntitySlime implements IEntityAdditionalSpawnData {
    public AbstractEntityData data;
    private ResourceLocation subid;

    public EntityCustomSlime(World world, ResourceLocation subid) {
        super(world);
        this.subid = subid;
        this.data = AbstractEntityData.DATA_MAP.get(subid);
    }

    public EntityCustomSlime(World worldIn) {
        super(worldIn); //Create a random custom slime
        List<ResourceLocation> subids = Lists.newArrayList(CustomLoader.subids.get(this.getClass()));
        subid = subids.get(worldIn.rand.nextInt(subids.size()));
        data = AbstractEntityData.DATA_MAP.get(subid);
    }

    @Override
    protected EntitySlime createInstance() {
        return new EntityCustomSlime(world, subid);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return this.getSlimeSize() == 1 ? data.lootTable : LootTableList.EMPTY;
    }

    @Override
    public EntityItem dropItemWithOffset(Item itemIn, int size, float offsetY) {
        return this.entityDropItem(data.getDropItem(size), offsetY);
    }

    @Override
    public boolean getCanSpawnHere() {
        return Scripting.getResult(data.getScript(), "canSpawnHere", super.getCanSpawnHere(), this);
    }

    @Override
    public boolean spawnCustomParticles() {
        if (Scripting.getResult(data.getScript(), "spawnCustomParticles", false, this)) return true;
        else return super.spawnCustomParticles();
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        if (Scripting.functionExists(data.getScript(), "onInitialSpawn")) {
            Scripting.callFunction(data.getScript(), "onInitialSpawn", this);
        }

        return super.onInitialSpawn(difficulty, livingdata);
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
