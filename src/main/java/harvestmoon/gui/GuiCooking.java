package harvestmoon.gui;

import harvestmoon.blocks.TileFridge;
import harvestmoon.gui.feature.FeatureButton;
import harvestmoon.gui.feature.FeatureButton.Button;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiCooking extends GuiBase {
    //The screen this cooking is currently on
    private static String screen;
    
    private static final Button fridge_contents = new Button("contents.fridge", 0, 238, 0xF0E68C, screen, "fridge");
    private static final Button player_contents = new Button("contents.player", 32, 238, 0xF0E68C, screen, "player");
    private static final Button recipe_books = new Button("recipes", 64, 238, 0xF0E68C, screen, "recipes");
    
    public GuiCooking(TileFridge fridge, InventoryPlayer player) {
        super(new ContainerCooking(fridge, player), "cooking", 0);
        features.add(new FeatureButton(fridge_contents, player_contents, recipe_books));
    }
    
    @Override
    public void drawForeground() {
    }
}
