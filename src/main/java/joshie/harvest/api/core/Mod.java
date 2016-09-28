package joshie.harvest.api.core;

public class Mod {
    private final String string;

    private Mod(String string) {
        this.string = string;
    }

    public String getMod() {
        return string;
    }

    public static Mod of(String string) {
        return new Mod(string);
    }
}
