package joshie.harvest.api.knowledge;

public class Category {
    public static final Category ACTIVITIES = new Category("activities");
    public static final Category FARMING = new Category("farming");
    public static final Category TOWNSHIP = new Category("town");
    //public static final Category OTHER = new Category("other");

    private final String unlocalized;

    public Category(String unlocalized) {
        this.unlocalized = unlocalized;
    }

    public String getUnlocalizedName() {
        return unlocalized;
    }
}
