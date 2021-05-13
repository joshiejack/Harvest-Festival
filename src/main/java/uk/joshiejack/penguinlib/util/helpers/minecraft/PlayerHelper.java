package uk.joshiejack.penguinlib.util.helpers.minecraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerHelper {
    public static UUID getUUIDForPlayer(@Nonnull EntityPlayer player) {
        return EntityPlayer.getUUID(player.getGameProfile());
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static EntityPlayer getPlayerFromUUID(World world, UUID uuid) {
        return !world.isRemote ? world.getMinecraftServer().getPlayerList().getPlayerByUUID(uuid) : world.getPlayerEntityByUUID(uuid);
    }

    @Nullable
    public static EntityPlayer getPlayerFromSource(DamageSource damage) {
        Entity source = damage.getTrueSource();
        if (!(source instanceof EntityPlayer)) {
            source = damage.getTrueSource();
        }

        return source instanceof EntityPlayer ? (EntityPlayer) source : null;
    }

    public static boolean hasTag(EntityPlayer player, String compoundTag, String tag) {
        return player.getEntityData().getCompoundTag(compoundTag).hasKey(tag);
    }

    public static void setTag(EntityPlayer player, String compoundTag, String tag) {
        if (!player.getEntityData().hasKey(compoundTag)) {
            player.getEntityData().setTag(compoundTag, new NBTTagCompound());
        }

        player.getEntityData().getCompoundTag(compoundTag).setBoolean(tag, true);
    }
}
