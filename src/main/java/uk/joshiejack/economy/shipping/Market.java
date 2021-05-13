package uk.joshiejack.economy.shipping;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import uk.joshiejack.economy.Economy;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Market extends WorldSavedData {
    private static final String DATA_NAME = "Market";
    private final Map<UUID, Shipping> shippingData = new HashMap<>();

    public Market(String data) {
        super(data);
    }

    public static Market get(World world) {
        Market instance = (Market) world.loadData(Market.class, DATA_NAME);
        if (instance == null) {
            instance = new Market(DATA_NAME);
            world.setData(DATA_NAME, instance);
            instance.markDirty(); //Save the file
        }

        return instance;
    }

    public void newDay(World world) {
        shippingData.values().forEach(s -> s.onNewDay(world));
    }

    private Shipping getShippingForTeam(UUID uuid) {
        if (!shippingData.containsKey(uuid)) {
            Shipping shipping = new Shipping(this, uuid);
            shippingData.put(uuid, shipping);
            markDirty();
            return shipping;
        } else return shippingData.get(uuid);
    }

    public Shipping getShippingForPlayer(EntityPlayer player) {
        PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
        boolean shared = player.getEntityData().hasKey(Economy.MODID + "Settings") &&
                player.getEntityData().getCompoundTag(Economy.MODID + "Settings").getBoolean("SharedGold"); //Player's Shared gold status
        return shared ? getShippingForTeam(team.getID()).shared() : getShippingForTeam(PlayerHelper.getUUIDForPlayer(player));
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        NBTHelper.deserialize(this, Shipping.class, nbt.getTagList("Shipping", 10), shippingData);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        nbt.setTag("Shipping", NBTHelper.serialize(shippingData));
       return nbt;
    }
}
