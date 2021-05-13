package uk.joshiejack.husbandry.client.gui.pages;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.client.gui.labels.LabelStatsDisplay;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple;
import uk.joshiejack.penguinlib.item.PenguinItems;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.List;

import static uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple.PageSide.LEFT;

public class PageStats extends PageMultiple.PageMultipleLabel<EntityAgeable> {
    public static final PageStats INSTANCE = new PageStats();
    public PageStats() {
        super("husbandry.tracker.stats", 12);
        this.icon = new Icon(PenguinItems.ENTITY.withEntity("minecraft:cow"), 0, 0);
    }

    @Override
    public List<EntityAgeable> getList() {
        List<EntityAgeable> list = EntityHelper.getEntities(EntityAgeable.class, gui.mc.player.world, new BlockPos(gui.mc.player), 32D, 32D);
        Iterator<EntityAgeable> it = list.iterator();
        while (it.hasNext()) {
            EntityLiving entity = it.next();
            AnimalStats<?> stats = AnimalStats.getStats(entity);
            if (stats == null) {
                it.remove();
            }
        }

        return list;
    }

    @Override
    protected LabelBook createLabel(GuiBook gui, EntityAgeable entry, PageSide side, int position) {
        return new LabelStatsDisplay(gui, entry, AnimalStats.getStats(entry), (side == LEFT ? 21: 163), 24 + (position) * 25);
    }
}
