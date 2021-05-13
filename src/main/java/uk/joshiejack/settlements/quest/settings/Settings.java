package uk.joshiejack.settlements.quest.settings;

import uk.joshiejack.penguinlib.util.PenguinGroup;

import java.util.Locale;

public class Settings {
    private Repeat repeat = Repeat.NONE;
    private PenguinGroup type = PenguinGroup.PLAYER;
    private boolean daily = false;
    private boolean isDefault = false;
    private String triggers = "onRightClickedNPC";

    public Repeat getRepeat() {
        return repeat;
    }

    public PenguinGroup getType() {
        return type;
    }

    public boolean isDaily() {
        return daily;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getTriggers() {
        return triggers;
    }

    public void setRepeat(String repeat) {
        this.repeat = Repeat.valueOf(repeat.toUpperCase(Locale.ENGLISH));
    }

    public void setType(String type) {
        this.type = PenguinGroup.valueOf(type.toUpperCase(Locale.ENGLISH));
        if (this.type == PenguinGroup.GLOBAL) {
            this.isDefault = true;
        }
    }

    public void setDaily() {
        this.isDefault = false;
        this.daily = true;
    }

    public void setDefault() { this.isDefault = true; }

    public void setTriggers(String triggers) {
        this.triggers = triggers;
    }
}
