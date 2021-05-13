package uk.joshiejack.economy.shipping;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import uk.joshiejack.economy.event.ItemShippedEvent;
import uk.joshiejack.economy.gold.Bank;
import uk.joshiejack.economy.gold.Vault;
import uk.joshiejack.economy.network.PacketShip;
import uk.joshiejack.economy.network.PacketSyncLastSold;
import uk.joshiejack.economy.network.PacketSyncSold;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Shipping implements INBTSerializable<NBTTagCompound> {
    private final Cache<ItemStack, Integer> countCache = CacheBuilder.newBuilder().build();
    private final Market market;
    private final UUID uuid;
    private boolean shared;
    private final Set<HolderSold> lastSell = Sets.newHashSet();
    private final Set<HolderSold> toSell = Sets.newHashSet();
    private final Set<HolderSold> sold = Sets.newHashSet();

    public Shipping(Market market, UUID uuid) {
        this.market = market;
        this.uuid = uuid;
    }

    public Shipping shared() {
        this.shared = true;
        return this;
    }

    public int getCount(ItemStack stack) {
        try {
            return countCache.get(stack, () -> {
                int total = 0;
                for (Shipping.HolderSold s: sold) {
                    if (s.matches(stack)) return s.getStack().getCount();
                }

                return total;
            });
        } catch (ExecutionException e) {
            return 0;
        }
    }

    public void syncToPlayer(EntityPlayer player) {
        PenguinNetwork.sendToClient(new PacketSyncSold(sold), player);
        PenguinNetwork.sendToClient(new PacketSyncLastSold(lastSell), player);
    }

    public Set<HolderSold> getSold() {
        return sold;
    }

    public void add(ItemStack stack) {
        long value = ShippingRegistry.INSTANCE.getValue(stack);
        //Add the tax
        if (value > 0) {
            for (HolderSold sold : toSell) {
                if (sold.matches(stack)) {
                    sold.merge(stack, value * stack.getCount());
                    market.markDirty();
                    return; //Found the match, so exit
                }
            }

            toSell.add(new HolderSold(stack, value * stack.getCount()));
            market.markDirty(); //Save!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
    }

    public void onNewDay(World world) {
        if (toSell.size() > 0) {
            Vault vault = shared ? Bank.get(world).getVaultForTeam(uuid).shared() : Bank.get(world).getVaultForTeam(uuid).personal();
            toSell.forEach(holder -> vault.increaseGold(world, holder.gold)); //Increase the vault for this players uuid
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("ToSell", NBTHelper.writeHolderCollection(toSell));
            if (shared) PenguinNetwork.sendToTeam(new PacketShip(tag), world, uuid);
            else PenguinNetwork.sendToClient(new PacketShip(tag), PlayerHelper.getPlayerFromUUID(world, uuid));

            PenguinTeam team = shared ? PenguinTeams.getTeamFromID(world, uuid) : PenguinTeams.getTeamForPlayer(world, uuid);
            toSell.forEach(holder -> {
                MinecraftForge.EVENT_BUS.post(new ItemShippedEvent(world, team, holder.stack, holder.gold)); //Statistics go to the team, no matter what
                boolean merged = false;
                for (HolderSold sold: this.sold) {
                    if (sold.matches(holder.stack)) {
                        sold.merge(holder); //Merge in the holder
                        merged = true;
                    }
                }

                if (!merged) this.sold.add(holder); //Add the holder if it doesn't exist yet
            });

            lastSell.clear();
            lastSell.addAll(toSell); //Add everything we just sold
            if (shared) PenguinNetwork.sendToTeam(new PacketSyncLastSold(lastSell), world, uuid); //Send the new last sold info to the clients
            else PenguinNetwork.sendToClient(new PacketSyncLastSold(lastSell), PlayerHelper.getPlayerFromUUID(world, uuid));
            countCache.invalidateAll();
            toSell.clear();
            market.markDirty();
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("LastSold", NBTHelper.writeHolderCollection(lastSell));
        tag.setTag("ToSell", NBTHelper.writeHolderCollection(toSell));
        tag.setTag("Sold", NBTHelper.writeHolderCollection(sold));
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTHelper.readHolderCollection(nbt.getTagList("LastSold", 10), lastSell);
        NBTHelper.readHolderCollection(nbt.getTagList("ToSell", 10), toSell);
        NBTHelper.readHolderCollection(nbt.getTagList("Sold", 10), sold);
    }

    @PenguinLoader
    public static class HolderSold extends Holder {
        private ItemStack stack;
        private long gold;

        public HolderSold() {
            super("sold");
        }

        public HolderSold(ItemStack stack, long gold) {
            super("sold");
            this.stack = stack;
            this.gold = gold;
        }

        public ItemStack getStack() {
            return stack;
        }

        public long getValue() {
            return gold;
        }

        @Override
        public boolean matches(ItemStack stack) {
            return ItemStack.areItemsEqual(stack, this.stack) && ItemStack.areItemStackTagsEqual(stack, this.stack);
        }

        public void merge(HolderSold holder) {
            this.stack.grow(holder.stack.getCount());
            this.gold += holder.getValue();
        }

        void merge(ItemStack stack, long value) {
            this.stack.grow(stack.getCount());
            this.gold += value;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            StackHelper.writeToNBT(stack, tag);
            tag.setLong("Gold", gold);
            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            stack = StackHelper.readFromNBT(nbt);
            gold = nbt.getLong("Gold");
        }
    }
}
