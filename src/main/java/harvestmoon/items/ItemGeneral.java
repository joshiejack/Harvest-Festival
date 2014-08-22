package harvestmoon.items;

public class ItemGeneral extends ItemBaseMeta {
    public static final int BLUE_FEATHER = 0;
    public static final int MILKER = 1;
    public static final int BRUSH = 2;

    public ItemGeneral() {
        setMaxMetaDamage(3);
    }

    @Override
    public String getName(int dmg) {
        switch (dmg) {
            case BLUE_FEATHER:
                return "feather_blue";
            case MILKER:
                return "milker";
            case BRUSH:
                return "brush";
            default:
                return "invalid";
        }
    }
}
