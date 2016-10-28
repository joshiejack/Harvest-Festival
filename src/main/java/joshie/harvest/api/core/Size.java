package joshie.harvest.api.core;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum Size implements IStringSerializable {
    SMALL(0), MEDIUM(20000), LARGE(40000);

    private final int relationship;

    Size(int relationship) {
        this.relationship = relationship;
    }

    public int getRelationshipRequirement() {
        return relationship;
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
