package uk.joshiejack.economy.shop.condition;

import com.google.common.collect.Lists;
import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.penguinlib.data.database.Database;

import java.util.List;
import java.util.Map;

public abstract class ConditionList extends Condition {
    protected List<Condition> conditions = Lists.newArrayList();

    protected abstract String getTableName();
    protected abstract String getFieldName();

    public void initList(Database database, String id, Map<String, Condition> conditions) {
        database.table(getTableName()).where("id="+id).forEach(row -> {
            String other_id = row.get(getFieldName());
            if (!other_id.equals(id)) {
                this.conditions.add(conditions.get(other_id));
            }
        });
    }
}
