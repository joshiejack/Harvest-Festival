package uk.joshiejack.economy.shop.condition;

import com.google.common.collect.Lists;
import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;

@PenguinLoader("time")
public class ConditionTime extends Condition {
    private final List<Pair<Integer, Integer>> times = Lists.newArrayList();

    public ConditionTime() {}
    public ConditionTime(String openinghours) {
        //Convert > MONDAY
        String[] hours = openinghours.replace(" ", "").split(";");
        for (String time: hours) {
            processTimeString(time);
        }
    }

    private void processTimeString(String time) {
        String[] split = time.split("-");
        if (split.length == 2) {
            int start = Integer.parseInt(split[0]);
            int end = Integer.parseInt(split[1]);
            addOpening(start, end);
        }
    }

    private void addOpening(int start, int end) {
        times.add(Pair.of(start, end));
    }

    @Override
    public Condition create(Row data, String id) {
        ConditionTime validator = new ConditionTime();
        int open = data.getTime("start");
        int close = data.getTime("end");
        if (open >= 0 && close >= 0) {
            validator.times.add(Pair.of(open, close));
        }

        return validator;
    }

    @Override
    public void merge(Row data) {
        int open = data.getTime("start");
        int close = data.getTime("end");
        if (open >= 0 && close >= 0) {
            times.add(Pair.of(open, close));
        }
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        if (type == CheckType.SHOP_EXISTS) return true; //Always true no matter the time
        for (Pair<Integer, Integer> time: times) {
            if (TimeHelper.isBetween(target.world, time.getLeft(), time.getRight())) return true;
        }

        return false;
    }
}
