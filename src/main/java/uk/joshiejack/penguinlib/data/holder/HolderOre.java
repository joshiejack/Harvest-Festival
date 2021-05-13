package uk.joshiejack.penguinlib.data.holder;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.util.Matcher;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.forge.OreDictionaryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@PenguinLoader
public class HolderOre extends Holder {
    public MatchType type;
    public String ore;

    public HolderOre() { super("ore");}
    public HolderOre(String ore) {
        super("ore");
        this.ore = ore;
        this.type = MatchType.WHOLE;
    }

    public HolderOre setMatchType(MatchType type) {
        this.type = type;
        return this;
    }

    @Override
    public List<Holder> createList(ItemStack stack) {
        List<Holder> holders = Lists.newArrayList();
        for (String name: OreDictionaryHelper.getOreNames(stack)) {
            holders.add(new HolderOre(name));
            String pre = Matcher.ORE_PREFIX.qualifies(name);
            if (pre != null) holders.add(new HolderOre(pre).setMatchType(MatchType.PREFIX));
            String suf = Matcher.ORE_SUFFIX.qualifies(name);
            if (suf != null) holders.add(new HolderOre(suf).setMatchType(MatchType.SUFFIX));
        }

        return holders;
    }

    @Override
    public boolean matches(ItemStack stack) {
        switch (type) {
            case WHOLE:
                return Matcher.ORE.matches(stack, ore);
            case PREFIX:
                return Matcher.ORE_PREFIX.matches(stack, ore);
            case SUFFIX:
                return Matcher.ORE_SUFFIX.matches(stack, ore);
            case CONTAINS:
                return Matcher.ORE_CONTAINS.matches(stack, ore);
            default:
                return false;
        }
    }

    @Override
    public boolean isEmpty() {
        if (type == MatchType.WHOLE) return OreDictionary.getOres(name).size() > 0;
        else if (type == MatchType.PREFIX) {
            for (String s: OreDictionary.getOreNames()) {
                if (s.startsWith(name)) return false;
            }

            return true;
        } else if (type == MatchType.SUFFIX) {
            for (String s: OreDictionary.getOreNames()) {
                if (s.endsWith(name)) return false;
            }

            return true;
        } else if (type == MatchType.CONTAINS) {
            for (String s: OreDictionary.getOreNames()) {
                if (s.contains(name)) return false;
            }

            return true;
        } else return true;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Ore", ore);
        tag.setByte("Type", (byte) type.ordinal());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        ore = nbt.getString("Ore");
        type = MatchType.values()[nbt.getByte("Type")];
    }

    @Override
    public String toString() {
        return type.name() + ":" + ore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HolderOre holderOre = (HolderOre) o;
        return type == holderOre.type && Objects.equals(ore, holderOre.ore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, ore);
    }

    public enum MatchType {
        PREFIX, SUFFIX, WHOLE, CONTAINS
    }
}
