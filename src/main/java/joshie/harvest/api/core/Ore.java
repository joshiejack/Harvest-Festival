package joshie.harvest.api.core;

public class Ore {
    private final String string;

    private Ore(String string) {
        this.string = string;
    }

    public String getOre() {
        return string;
    }

    public static Ore of(String string) {
        return new Ore(string);
    }
}
