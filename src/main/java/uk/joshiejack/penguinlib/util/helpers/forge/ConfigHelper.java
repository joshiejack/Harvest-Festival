package uk.joshiejack.penguinlib.util.helpers.forge;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

import java.lang.reflect.Field;

public class ConfigHelper {
    public static void readFromDatabase(DatabaseLoadedEvent.ConfigLoad event, String config, Class<?> clazz) throws IllegalAccessException {
        for (Row row: event.table(config).rows()) {
            for (Field field : clazz.getFields()) {
                String search = field.getName();
                if (row.get("key").toString().equals(search)) {
                    field.set(null, row.get("value"));
                }
            }
        }
    }
}
