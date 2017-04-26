package joshie.harvest.knowledge.gui.stats.relations.page;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.button.ButtonNext;
import joshie.harvest.knowledge.gui.stats.button.ButtonPrevious;
import joshie.harvest.knowledge.gui.stats.relations.button.ButtonRelationsAnimal;
import joshie.harvest.knowledge.gui.stats.relations.button.ButtonRelationsAnimalNull;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.List;

public class PageAnimals extends PageRelationship {
    public static final BookPage INSTANCE = new PageAnimals();

    private PageAnimals() {
        super("animals", HFAnimals.ANIMAL.getStackFromEnum(Spawner.COW));
    }

    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList, List<GuiLabel> labelList) {
        super.initGui(gui, buttonList, labelList);
        //Build the list
        List<EntityAnimal> list = EntityHelper.getEntities(EntityAnimal.class, MCClientHelper.getWorld(), new BlockPos(MCClientHelper.getPlayer()), 128D, 128D);
        Iterator<EntityAnimal> it = list.iterator();
        while (it.hasNext()) {
            EntityAnimal animal = it.next();
            AnimalStats stats = EntityHelper.getStats(animal);
            if (stats == null) {
                it.remove();
            }
        }

        int x = 0;
        int y = 0;
        for (int i = start * 12; i < 12 + start * 12 && i < list.size(); i++) {
            EntityAnimal animal = list.get(i);
            buttonList.add(new ButtonRelationsAnimal(gui, animal, EntityHelper.getStats(animal), buttonList.size(), 16 + x * 144, 26 + y * 25));
            y++;

            if (y >= 6) {
                y = 0;
                x++;
            }
        }

        if (buttonList.size() == 2) buttonList.add(new ButtonRelationsAnimalNull(gui, buttonList.size(), 16 + x * 144, 26 + y * 25));
        if (start < (list.size() / 12)) buttonList.add(new ButtonNext(gui, buttonList.size(), 273, 172));
        if (start != 0) buttonList.add(new ButtonPrevious(gui, buttonList.size(), 20, 172));
    }
}
