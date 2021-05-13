package uk.joshiejack.penguinlib.data.custom;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.data.custom.block.AbstractCustomBlockData;
import net.minecraft.block.properties.PropertyHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

public class PropertyCustomData extends PropertyHelper<AbstractCustomBlockData> {
    private final ImmutableSet<AbstractCustomBlockData> allowedValues;
    private final Map<String, AbstractCustomBlockData> nameToValue = Maps.newHashMap();

    public PropertyCustomData(String name, AbstractCustomBlockData... data) {
        super(name, AbstractCustomBlockData.class);
        this.allowedValues = ImmutableSet.copyOf(Lists.newArrayList(data));
        for (AbstractCustomBlockData cd : allowedValues) {
            if (this.nameToValue.containsKey(cd.name)) {
                throw new IllegalArgumentException("Multiple values have the same name '" + cd.name + "'");
            }

            this.nameToValue.put(cd.name, cd);
        }
    }

    @Nonnull
    @Override
    public Collection<AbstractCustomBlockData> getAllowedValues() {
        return this.allowedValues;
    }

    @SuppressWarnings("all")
    @Nonnull
    @Override
    public Optional<AbstractCustomBlockData> parseValue(@Nonnull String value) {
        return Optional.fromNullable(this.nameToValue.get(value));
    }

    @Nonnull
    @Override
    public String getName(@Nonnull AbstractCustomBlockData value) {
        return value.name;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (p_equals_1_ instanceof PropertyCustomData && super.equals(p_equals_1_)) {
            PropertyCustomData pcd = (PropertyCustomData) p_equals_1_;
            return this.allowedValues.equals(pcd.allowedValues) && this.nameToValue.equals(pcd.nameToValue);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int i = super.hashCode();
        i = 31 * i + this.allowedValues.hashCode();
        i = 31 * i + this.nameToValue.hashCode();
        return i;
    }
}
