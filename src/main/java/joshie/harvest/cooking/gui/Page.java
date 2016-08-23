package joshie.harvest.cooking.gui;

import joshie.harvest.cooking.Utensil;

public abstract class Page {
    protected GuiCookbook gui;

    public Page initGui(GuiCookbook gui) {
        this.gui = gui;
        return this;
    }

    public abstract void draw(int mouseX, int mouseY);
    public abstract void mouseClicked(int mouseX, int mouseY);

    public abstract Page getOwner();

    public Utensil getUtensil() {
        return null;
    }
}
