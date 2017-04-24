package joshie.harvest.api.npc;

public enum RelationStatus {
    GIFTED,
    TALKED,
    CHRISTMAS_GIFT {
        @Override
        public boolean isSeasonal() {
            return true;
        }
    },
    BIRTHDAY_GIFT {
        @Override
        public boolean isSeasonal() {
            return true;
        }
    },
    MARRIED {
        @Override
        public boolean isPermenant() {
            return true;
        }
    },
    MET {
        @Override
        public boolean isPermenant() {
            return true;
        }
    };

    public boolean isSeasonal() {
        return false;
    }

    public boolean isPermenant() {
        return false;
    }
}
