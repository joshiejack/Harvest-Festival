package uk.joshiejack.economy.gold;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.network.PacketSetWalletUsed;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bank extends WorldSavedData {
    private static final String DATA_NAME = "Bank";
    private final Map<UUID, Vault> vaults = new HashMap<>();

    public Bank(String data) {
        super(data);
    }

    public static Bank get(World world) {
        Bank instance = (Bank) world.loadData(Bank.class, DATA_NAME);
        if (instance == null) {
            instance = new Bank(DATA_NAME);
            world.setData(DATA_NAME, instance);
            instance.markDirty(); //Save the file
        }

        return instance;
    }

    public Vault getVaultForTeam(UUID uuid) {
        if (!vaults.containsKey(uuid)) {
            Vault vault = new Vault(this, uuid);
            vaults.put(uuid, vault);
            markDirty();
            return vault;
        } else return vaults.get(uuid);
    }

    public Vault getVaultForPlayer(EntityPlayer player) {
        //Change this to be based on a person player toggle instead
        PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
        boolean shared = player.getEntityData().hasKey(Economy.MODID + "Settings") &&
                player.getEntityData().getCompoundTag(Economy.MODID + "Settings").getBoolean("SharedGold"); //Player's Shared gold status
        return shared ? getVaultForTeam(team.getID()).shared() : getVaultForTeam(PlayerHelper.getUUIDForPlayer(player)).personal();
    }

    public void syncToPlayer(EntityPlayer player) {
        //Sync both
        PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
        boolean shared = player.getEntityData().hasKey(Economy.MODID + "Settings") &&
                player.getEntityData().getCompoundTag(Economy.MODID + "Settings").getBoolean("SharedGold"); //Player's Shared gold status
        PenguinNetwork.sendToClient(new PacketSetWalletUsed(shared), player);
        getVaultForTeam(team.getID()).shared().synchronize(player.world);
        getVaultForTeam(PlayerHelper.getUUIDForPlayer(player)).personal().synchronize(player.world);
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        NBTHelper.deserialize(this, Vault.class, nbt.getTagList("Vaults", 10), vaults);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        nbt.setTag("Vaults", NBTHelper.serialize(vaults));
        return nbt;
    }
}
