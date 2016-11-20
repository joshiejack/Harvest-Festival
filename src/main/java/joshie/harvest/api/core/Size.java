package joshie.harvest.api.core;

import joshie.harvest.api.player.RelationshipType;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum Size implements IStringSerializable {
    SMALL(0), MEDIUM(RelationshipType.ANIMAL.getMaximumRP() / 2), LARGE(RelationshipType.ANIMAL.getMaximumRP());

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
